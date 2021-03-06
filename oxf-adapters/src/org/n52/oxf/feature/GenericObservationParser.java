/**********************************************************************************
 Copyright (C) 2009
 by 52 North Initiative for Geospatial Open Source Software GmbH

 Contact: Andreas Wytzisk 
 52 North Initiative for Geospatial Open Source Software GmbH
 Martin-Luther-King-Weg 24
 48155 Muenster, Germany
 info@52north.org

 This program is free software; you can redistribute and/or modify it under the
 terms of the GNU General Public License version 2 as published by the Free
 Software Foundation.

 This program is distributed WITHOUT ANY WARRANTY; even without the implied
 WARRANTY OF MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 General Public License for more details.

 You should have received a copy of the GNU General Public License along with this 
 program (see gnu-gplv2.txt). If not, write to the Free Software Foundation, Inc., 
 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA or visit the Free Software
 Foundation web page, http://www.fsf.org.
 
 Created on: 29.01.2006
 *********************************************************************************/

package org.n52.oxf.feature;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import net.opengis.gml.FeatureCollectionDocument2;
import net.opengis.gml.FeaturePropertyType;
import net.opengis.gml.x32.CodeWithAuthorityType;
import net.opengis.swe.x00.AnyScalarPropertyType;
import net.opengis.swe.x00.SimpleDataRecordType;
import net.opengis.swe.x00.TextBlockDocument.TextBlock;
import net.opengis.waterml.x20.MonitoringPointDocument;
import net.opengis.waterml.x20.MonitoringPointType;
import net.opengis.waterml.x20.TimeValuePairPropertyType;
import net.opengis.waterml.x20.TimeValuePairType;
import net.opengis.waterml.x20.TimeseriesDocument;
import net.opengis.waterml.x20.TimeseriesObservationType;
import net.opengis.waterml.x20.TimeseriesType;
import net.opengis.waterml.x20.TimeseriesType.Point;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.n52.oxf.OXFException;
import org.n52.oxf.feature.dataTypes.OXFMeasureType;
import org.n52.oxf.feature.dataTypes.OXFPhenomenonPropertyType;
import org.n52.oxf.feature.dataTypes.OXFScopedName;
import org.n52.oxf.feature.sos.SOSFoiStore;
import org.n52.oxf.owsCommon.capabilities.ITime;
import org.n52.oxf.util.LoggingHandler;
import org.n52.oxf.valueDomains.time.TimeFactory;

import com.sun.java.swing.plaf.windows.WindowsTreeUI.CollapsedIcon;

/**
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public class GenericObservationParser {

	private static final Logger LOGGER = LoggingHandler
	.getLogger(GenericObservationParser.class);
	
    /**
     * supports O&M 0.0
     * 
     * this method only works with the following assumptions:
     * 
     * <pre>
     * - ASCII-Block-Encoding
     * - for swe:dataComponents -&gt; swe:DataGroup will be used
     * - the first token in the result is the time; the next ones are the values for the phenomenons.
     * &lt;
     * </pre>
     * 
     * 
     * @param featureCollection
     *        the values contained in the generic observation will be added to this collection.
     * @param xb_genericObs
     *        the generic observation bean which shall be parsed.
     */
    public static void addElementsFromGenericObservation(OXFFeatureCollection featureCollection,
                                                         net.opengis.om.x00.ObservationType xb_genericObs) throws OXFException {

        if (xb_genericObs.getProcedure() == null) {
            LOGGER.debug("No observation to parse.");
            return;
        }
        
        //
        // parsing the procedure:
        //
        String procedure = xb_genericObs.getProcedure().getHref();

        //
        // parsing the featureOfInterest:
        //
        Map<String, OXFFeature> foiMap = new HashMap<String, OXFFeature>();
        FeaturePropertyType xb_foiProp = xb_genericObs.getFeatureOfInterest();

        XmlCursor c = xb_foiProp.newCursor();
        boolean isFeatureColl = c.toChild(new QName("http://www.opengis.net/gml", "FeatureCollection"));

        if (isFeatureColl) {
            try {
                FeatureCollectionDocument2 xb_featureColl = FeatureCollectionDocument2.Factory.parse(c.getDomNode());

                for (FeaturePropertyType xb_featureMember : xb_featureColl.getFeatureCollection().getFeatureMemberArray()) {

                    // the feature to add:
                    OXFFeature feature = new SOSFoiStore().parseFoi(xb_featureMember);
                    foiMap.put(feature.getID(), feature);
                }
            }
            catch (XmlException e) {
                throw new OXFException(e);
            }
        }

        //
        // parsing the resultDefinition:
        //
        List<String> definitionList = new ArrayList<String>();
        List<String> typeList = new ArrayList<String>();
        List<String> nameList = new ArrayList<String>();

        // Map-association: quantity-URN --> uom-URN
        Map<String, String> uomMap = new HashMap<String, String>();

        SimpleDataRecordType dataRecord = (SimpleDataRecordType) xb_genericObs.getResultDefinition().getDataBlockDefinition().getComponents().getAbstractDataRecord();

        AnyScalarPropertyType[] fieldArray = dataRecord.getFieldArray();
        for (int i = 0; i < fieldArray.length; i++) {
        	if (fieldArray[i].getName() != null){
        		nameList.add(fieldArray[i].getName());
        	} else{
        		nameList.add("");
        	}
            if (fieldArray[i].getTime() != null) {
                definitionList.add(fieldArray[i].getTime().getDefinition());
                typeList.add("time");
            }
            else if (fieldArray[i].getText() != null) {
                definitionList.add(fieldArray[i].getText().getDefinition());
                typeList.add("text");
            }
            else if (fieldArray[i].getQuantity() != null) {
                String quantityURN = fieldArray[i].getQuantity().getDefinition();
                definitionList.add(quantityURN);
                typeList.add("quantity");

                String uomURN = fieldArray[i].getQuantity().getUom().getHref();
                uomMap.put(quantityURN, uomURN);
            }
            else if (fieldArray[i].getCategory() != null) {
                definitionList.add(fieldArray[i].getCategory().getDefinition());
                typeList.add("category");
            }
            else if (fieldArray[i].getBoolean() != null) {
                definitionList.add(fieldArray[i].getBoolean().getDefinition());
                typeList.add("boolean");
            }
            else if (fieldArray[i].getCount() != null) {
                definitionList.add(fieldArray[i].getCount().getDefinition());
                typeList.add("count");
            }
            // ... TODO there are more possibilities...
        }

        // TODO Spec-Too-Flexible-Problem --> 3 encoding types are possible:
        TextBlock xb_textBlock = xb_genericObs.getResultDefinition().getDataBlockDefinition().getEncoding().getTextBlock();

        String decimalSeparator = xb_textBlock.getDecimalSeparator();
        String tokenSeparator = xb_textBlock.getTokenSeparator();
        String blockSeparator = xb_textBlock.getBlockSeparator();

        //
        // parsing the result:
        //
        String resultText = xb_genericObs.getResult().getDomNode().getFirstChild().getNodeValue();

        parseTextBlock(featureCollection,
                       resultText,
                       decimalSeparator,
                       tokenSeparator,
                       blockSeparator,
                       definitionList,
                       typeList,
                       nameList,
                       foiMap,
                       uomMap,
                       procedure);
    }

    /**
     * supports O&M 1.0
     * 
     * @param featureCollection
     *        the values contained in the generic observation will be added to this collection.
     * @param xb_genericObs
     *        the generic observation bean which shall be parsed.
     */
    public static void addElementsFromGenericObservation(OXFFeatureCollection featureCollection,
                                                         net.opengis.om.x10.ObservationType xb_genericObs) throws OXFException {
        
        if (xb_genericObs.getProcedure() == null) {
            LOGGER.debug("No observation to parse.");
            return;
        }
        
        //
        // parsing the procedure:
        //
        String procedure = xb_genericObs.getProcedure().getHref();

        //
        // parsing the featureOfInterest:
        //
        Map<String, OXFFeature> foiMap = new HashMap<String, OXFFeature>();
        FeaturePropertyType xb_foiProp = xb_genericObs.getFeatureOfInterest();

        XmlCursor c = xb_foiProp.newCursor();
        boolean isFeatureColl = c.toChild(new QName("http://www.opengis.net/gml", "FeatureCollection"));

        if (isFeatureColl) {
            try {
                FeatureCollectionDocument2 xb_featureColl = FeatureCollectionDocument2.Factory.parse(c.getDomNode());

                for (FeaturePropertyType xb_featureMember : xb_featureColl.getFeatureCollection().getFeatureMemberArray()) {

                    // the feature to add:
                    OXFFeature feature = new SOSFoiStore().parseFoi(xb_featureMember);
                    foiMap.put(feature.getID(), feature);
                }
            }
            catch (XmlException e) {
                throw new OXFException(e);
            }
        }

        //
        // parsing the resultDefinition:
        //
        

        // Map-association: quantity-URN --> uom-URN
        Map<String, String> uomMap = new HashMap<String, String>();

        XmlCursor cResult = xb_genericObs.getResult().newCursor();
        cResult.toChild(new QName("http://www.opengis.net/swe/1.0.1", "DataArray"));
        net.opengis.swe.x101.DataArrayDocument xb_dataArrayDoc = null;
        try {
            xb_dataArrayDoc = net.opengis.swe.x101.DataArrayDocument.Factory.parse(cResult.getDomNode());
        }
        catch (XmlException e) {
            throw new OXFException(e);
        }

        List<String> definitionList = new ArrayList<String>();
        List<String> typeList = new ArrayList<String>();
        List<String> nameList = new ArrayList<String>();
        
        net.opengis.swe.x101.AbstractDataRecordType xb_abstractDataRecord = xb_dataArrayDoc.getDataArray1().getElementType().getAbstractDataRecord();
        
        // 1. in case of 'SimpleDataRecord':
        if (xb_abstractDataRecord instanceof net.opengis.swe.x101.SimpleDataRecordType) {
        	net.opengis.swe.x101.SimpleDataRecordType xb_simpleDataRecord = (net.opengis.swe.x101.SimpleDataRecordType) xb_abstractDataRecord;
        	net.opengis.swe.x101.AnyScalarPropertyType[] xb_fieldArray = xb_simpleDataRecord.getFieldArray();
        	
        	for (int i = 0; i < xb_fieldArray.length; i++) {
            	if (xb_fieldArray[i].getName() != null){
            		nameList.add(xb_fieldArray[i].getName());
            	} else{
            		nameList.add("");
            	}
                if (xb_fieldArray[i].getTime() != null) {
                    definitionList.add(xb_fieldArray[i].getTime().getDefinition());
                    typeList.add("time");
                }
                else if (xb_fieldArray[i].getText() != null) {
                    definitionList.add(xb_fieldArray[i].getText().getDefinition());
                    typeList.add("text");
                }
                else if (xb_fieldArray[i].getQuantity() != null) {
                    String quantityURN = xb_fieldArray[i].getQuantity().getDefinition();
                    definitionList.add(quantityURN);
                    typeList.add("quantity");

                    String uomURN = xb_fieldArray[i].getQuantity().getUom().getCode();
                    uomMap.put(quantityURN, uomURN);
                }
                else if (xb_fieldArray[i].getCategory() != null) {
                    definitionList.add(xb_fieldArray[i].getCategory().getDefinition());
                    typeList.add("category");
                }
                else if (xb_fieldArray[i].getBoolean() != null) {
                    definitionList.add(xb_fieldArray[i].getBoolean().getDefinition());
                    typeList.add("boolean");
                }
                else if (xb_fieldArray[i].getCount() != null) {
                    definitionList.add(xb_fieldArray[i].getCount().getDefinition());
                    typeList.add("count");
                }
                else {
                	LOGGER.warn("Could not parse following resultData: "+xb_fieldArray[i].toString());
                }
                
                // ... TODO there are more possibilities...
            }
        }
        
        // 2. in case of 'DataRecord':
        else if (xb_abstractDataRecord instanceof net.opengis.swe.x101.DataRecordType) {
        	net.opengis.swe.x101.DataRecordType xb_dataRecord = (net.opengis.swe.x101.DataRecordType) xb_dataArrayDoc.getDataArray1().getElementType().getAbstractDataRecord();
        	net.opengis.swe.x101.DataComponentPropertyType[] xb_fieldArray = xb_dataRecord.getFieldArray();
        	
        	for (int i = 0; i < xb_fieldArray.length; i++) {
            	if (xb_fieldArray[i].getName() != null){
            		nameList.add(xb_fieldArray[i].getName());
            	} else{
            		nameList.add("");
            	}
                if (xb_fieldArray[i].getTime() != null) {
                    definitionList.add(xb_fieldArray[i].getTime().getDefinition());
                    typeList.add("time");
                }
                else if (xb_fieldArray[i].getText() != null) {
                    definitionList.add(xb_fieldArray[i].getText().getDefinition());
                    typeList.add("text");
                }
                else if (xb_fieldArray[i].getQuantity() != null) {
                    String quantityURN = xb_fieldArray[i].getQuantity().getDefinition();
                    definitionList.add(quantityURN);
                    typeList.add("quantity");

                    String uomURN = xb_fieldArray[i].getQuantity().getUom().getCode();
                    uomMap.put(quantityURN, uomURN);
                }
                else if (xb_fieldArray[i].getCategory() != null) {
                    definitionList.add(xb_fieldArray[i].getCategory().getDefinition());
                    typeList.add("category");
                }
                else if (xb_fieldArray[i].getBoolean() != null) {
                    definitionList.add(xb_fieldArray[i].getBoolean().getDefinition());
                    typeList.add("boolean");
                }
                else if (xb_fieldArray[i].getCount() != null) {
                    definitionList.add(xb_fieldArray[i].getCount().getDefinition());
                    typeList.add("count");
                }
                else {
                	LOGGER.warn("Could not parse following resultData: "+xb_fieldArray[i].toString());
                }
                
                // ... TODO there are more possibilities...
            }
        }
        
        // TODO Spec-Too-Flexible-Problem --> 3 encoding types are possible:
        net.opengis.swe.x101.TextBlockDocument.TextBlock xb_textBlock = xb_dataArrayDoc.getDataArray1().getEncoding().getTextBlock();

        String decimalSeparator = xb_textBlock.getDecimalSeparator();
        String tokenSeparator = xb_textBlock.getTokenSeparator();
        String blockSeparator = xb_textBlock.getBlockSeparator();

        //
        // parsing the result:
        //
        String resultText = xb_dataArrayDoc.getDataArray1().getValues().getDomNode().getFirstChild().getNodeValue();

        parseTextBlock(featureCollection,
                       resultText,
                       decimalSeparator,
                       tokenSeparator,
                       blockSeparator,
                       definitionList,
                       typeList,
                       nameList,
                       foiMap,
                       uomMap,
                       procedure);
    }
    
    public static void addElementsFromTimeSeries(
			OXFFeatureCollection featureCollection,
			TimeseriesObservationType xb_timeseriesObsType) throws OXFException {
    	
        if (xb_timeseriesObsType.getProcedure() == null) {
            LOGGER.debug("No timeseries to parse.");
            return;
        }
        
    	// procedure
    	String procedure = xb_timeseriesObsType.getProcedure().getTitle();

    	// featureOfInterest
    	String foiID = null;
    	net.opengis.gml.x32.FeaturePropertyType foiType = xb_timeseriesObsType.getFeatureOfInterest();
    	XmlCursor cFoi = foiType.newCursor();
    	cFoi.toFirstChild();
    	try {
			XmlObject foi = XmlObject.Factory.parse(cFoi.getObject().getDomNode());
			if (foi instanceof MonitoringPointDocument) {
				MonitoringPointDocument monPointDoc = (MonitoringPointDocument) foi;
				MonitoringPointType monPoint = monPointDoc.getMonitoringPoint();
				CodeWithAuthorityType identifier = monPoint.getIdentifier();
				foiID = identifier.getStringValue();
			}
		} catch (XmlException e) {
			throw new OXFException(e);
		}
    	
    	OXFSamplingPointType pointType = new OXFSamplingPointType();
    	OXFFeature foi = new OXFFeature(foiID, pointType);
    	
    	// phenomenonURN
    	String phenomenonURN = xb_timeseriesObsType.getObservedProperty().getTitle();
    	
    	// result
    	XmlCursor cResult = xb_timeseriesObsType.getResult().newCursor();
    	cResult.toFirstChild();
    	try {
			XmlObject xb_timeseries = XmlObject.Factory.parse(cResult.getObject().getDomNode());
			if (xb_timeseries instanceof TimeseriesDocument) {
		     	TimeseriesType timeseries = ((TimeseriesDocument) xb_timeseries).getTimeseries();
				TimeValuePairPropertyType defaultTimeValuePair = timeseries.getDefaultTimeValuePair();
				String uomURN = defaultTimeValuePair.getTimeValuePair().getUnitOfMeasure().getUom();
				Point[] pointArray = timeseries.getPointArray();
				for (Point point : pointArray) {
			    	OXFMeasurementType oxf_measurementType = new OXFMeasurementType();
			        OXFFeature feature = new OXFFeature("anyID", oxf_measurementType);
			        
					TimeValuePairType timeValuePair = point.getTimeValuePair();
					
					String timeString = timeValuePair.getTime().getStringValue();
					OXFMeasureType resultValue = new OXFMeasureType(uomURN, timeValuePair.getValue());
					
			        oxf_measurementType.initializeFeature(feature,
			                new String[] {phenomenonURN},
			                "anyDescription",
			                null,// featureOfInterestValue.getGeometry(),
			                TimeFactory.createTime(timeString),
			                procedure,
			                new OXFPhenomenonPropertyType(phenomenonURN, uomURN),
			                foi,
			                resultValue);
			        
			        featureCollection.add(feature);
				}
			}
			
		} catch (XmlException e) {
			throw new OXFException(e);
		}
	}

    /**
     * 
     * @param featureCollection
     *        the collection where the single observed values shall be added to.
     * @param resultText
     *        the result's text block.
     */
    private static void parseTextBlock(OXFFeatureCollection featureCollection,
                                       String resultText,
                                       String decimalSeparator,
                                       String tokenSeparator,
                                       String blockSeparator,
                                       List<String> definitionList,
                                       List<String> typeList,
                                       List<String> nameList,
                                       Map<String, OXFFeature> foiMap,
                                       Map<String, String> uomMap,
                                       String procedure) {

        String[] blockArray = resultText.split(blockSeparator);

        for (String block : blockArray) {
            String[] tokenArray = block.split(tokenSeparator);

            if (tokenArray.length > 0) {
                String timeString = tokenArray[0];

                OXFFeature foi = null;

                //
                // for each phenomenon: add an observation to the collection
                //
                for (int i = 0; i < definitionList.size(); i++) {

                    if (definitionList.get(i).equals("urn:ogc:data:time:iso8601") 
                    		|| definitionList.get(i).equals("http://www.opengis.net/def/property/OGC/0/SamplingTime")) {
                        // do nothing
                    }
                    
                    //else if(nameList.get(i).equalsIgnoreCase("SamplingTime")) {
                    //	//dn -> DLR time urn
                    //}
                    else if (definitionList.get(i).equals("urn:ogc:data:feature") 
                    		|| definitionList.get(i).equals("http://www.opengis.net/def/property/OGC/0/FeatureOfInterest")) {
                        String foiID = tokenArray[i];
                        foi = foiMap.get(foiID);
                    }
                    else if (typeList.get(i).equals("quantity")) {
                        String phenomenonURN = definitionList.get(i);
                        String phenomenValue = tokenArray[i];
                        String uomURN = uomMap.get(phenomenonURN);

                        OXFMeasurementType oxf_measurementType = new OXFMeasurementType();
                        OXFFeature feature = new OXFFeature("anyID", oxf_measurementType);

                        OXFMeasureType resultValue = null;
                        if (phenomenValue.equalsIgnoreCase("nodata")) {
                            resultValue = new OXFMeasureType(uomURN, Double.NaN);
                        }
                        else {
                            phenomenValue = phenomenValue.replace(decimalSeparator, ".");
                            resultValue = new OXFMeasureType(uomURN, Double.valueOf(phenomenValue));
                        }

                        oxf_measurementType.initializeFeature(feature,
                                                              new String[] {nameList.get(i)},
                                                              "anyDescription",
                                                              null,// featureOfInterestValue.getGeometry(),
                                                              TimeFactory.createTime(timeString),
                                                              procedure,
                                                              new OXFPhenomenonPropertyType(phenomenonURN, uomURN),
                                                              foi,
                                                              resultValue);
                        featureCollection.add(feature);
                    }
                    else if (typeList.get(i).equals("category")) {
                        String phenomenonURN = definitionList.get(i);
                        String phenomenValue = tokenArray[i];

                        OXFCategoryObservationType oxf_categoryType = new OXFCategoryObservationType();
                        OXFFeature feature = new OXFFeature("anyID", oxf_categoryType);

                        OXFScopedName resultValue = new OXFScopedName("anyCode", phenomenValue);

                        oxf_categoryType.initializeFeature(feature, new String[] {nameList.get(i)}, "anyDescription", null,// featureOfInterestValue.getGeometry(),
                                                           TimeFactory.createTime(timeString),
                                                           procedure,
                                                           new OXFPhenomenonPropertyType(phenomenonURN),
                                                           foi,
                                                           resultValue);
                        featureCollection.add(feature);
                    }
                    else if (typeList.get(i).equals("boolean")) {
                        String phenomenonURN = definitionList.get(i);
                        String phenomenValue = tokenArray[i];

                        OXFTruthObservationType oxf_TruthType = new OXFTruthObservationType();
                        OXFFeature feature = new OXFFeature("anyID", oxf_TruthType);

                        Boolean resultValue = Boolean.parseBoolean(phenomenValue);

                        oxf_TruthType.initializeFeature(feature, new String[] {nameList.get(i)}, "anyDescription", null,// featureOfInterestValue.getGeometry(),
                                                           TimeFactory.createTime(timeString),
                                                           procedure,
                                                           new OXFPhenomenonPropertyType(phenomenonURN),
                                                           foi,
                                                           resultValue);
                        
                        featureCollection.add(feature);
                    }
                    else if (typeList.get(i).equals("count")) {
                        String phenomenonURN = definitionList.get(i);
                        String phenomenValue = tokenArray[i];

                        OXFCountObservationType oxf_CountType = new OXFCountObservationType();
                        OXFFeature feature = new OXFFeature("anyID", oxf_CountType);

                        Number resultValue = Integer.parseInt(phenomenValue);

                        oxf_CountType.initializeFeature(feature, new String[] {nameList.get(i)}, "anyDescription", null,// featureOfInterestValue.getGeometry(),
                                                           TimeFactory.createTime(timeString),
                                                           procedure,
                                                           new OXFPhenomenonPropertyType(phenomenonURN),
                                                           foi,
                                                           resultValue);
                        featureCollection.add(feature);
                    }
                    else if (typeList.get(i).equals("time")) {
                        String phenomenonURN = definitionList.get(i);
                        String phenomenValue = tokenArray[i];

                        OXFTemporalObservationType oxf_TimeType = new OXFTemporalObservationType();
                        OXFFeature feature = new OXFFeature("anyID", oxf_TimeType);

                        ITime resultValue = TimeFactory.createTime(phenomenValue);

                        oxf_TimeType.initializeFeature(feature, new String[] {nameList.get(i)}, "anyDescription", null,// featureOfInterestValue.getGeometry(),
                                                           TimeFactory.createTime(timeString),
                                                           procedure,
                                                           new OXFPhenomenonPropertyType(phenomenonURN),
                                                           foi,
                                                           resultValue);
                        featureCollection.add(feature);
                    }
                }
            }
        }
    }
}