/**
 * Copyright (C) 2009
 * by 52 North Initiative for Geospatial Open Source Software GmbH
 *
 * Contact: Andreas Wytzisk
 * 52 North Initiative for Geospatial Open Source Software GmbH
 * Martin-Luther-King-Weg 24
 * 48155 Muenster, Germany
 * info@52north.org
 *
 * This program is free software; you can redistribute and/or modify it under
 * the terms of the GNU General Public License version 2 as published by the
 * Free Software Foundation.
 *
 * This program is distributed WITHOUT ANY WARRANTY; even without the implied
 * WARRANTY OF MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program (see gnu-gpl v2.txt). If not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA or
 * visit the Free Software Foundation web page, http://www.fsf.org.
 */
package org.n52.oxf.util;

import net.opengis.gml.FeatureCollectionDocument;
import net.opengis.gml.FeaturePropertyType;
import net.opengis.gml.TimeInstantType;
import net.opengis.gml.TimePositionType;
import net.opengis.om.x10.ObservationDocument;
import net.opengis.om.x10.ObservationType;
import net.opengis.sampling.x10.SamplingPointDocument;
import net.opengis.sampling.x10.SamplingPointType;
import net.opengis.swe.x101.PhenomenonPropertyType;


import org.apache.log4j.Logger;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlObject;
import org.n52.oxf.feature.OXFFeature;
import org.n52.oxf.feature.OXFSamplingPointType;
import org.n52.oxf.owsCommon.capabilities.ITime;
import org.n52.oxf.serviceAdapters.ParameterContainer;
import org.n52.oxf.serviceAdapters.ses.TestSESAdapter;
import org.n52.oxf.ui.swing.ses.SesLayerAdder;
import org.n52.oxf.valueDomains.time.TimeFactory;
import org.opengis.feature.FeatureCollection;


/**
 * @author Artur Osmanov
 * 
 * Parses OGC O&M documents
 *
 */
public class OMParser {
	private static final Logger LOGGER = Logger.getLogger(OMParser.class);
	
	public static ParameterContainer parseOM(ObservationDocument document) throws Exception {
		ObservationType observation = null;
		ParameterContainer paramCon = new ParameterContainer();

		if (document != null) {
			observation = document.getObservation();
		} else throw new Exception("No Observation found!");


		// parse featureOfInterest
		FeaturePropertyType foi = observation.getFeatureOfInterest();
		SamplingPointType sa = SamplingPointType.Factory.parse(foi.toString());
		SamplingPointDocument doc = SamplingPointDocument.Factory.parse(sa.toString()); //出错位置
		 
		OXFFeature feature = OXFSamplingPointType.create(doc);
	
		
		// position
		double x = feature.getGeometry().getInteriorPoint().getCoordinate().x;
		double y = feature.getGeometry().getInteriorPoint().getCoordinate().y;
		paramCon.addParameterShell(SesLayerAdder.X_COORD, Double.toString(x));
		paramCon.addParameterShell(SesLayerAdder.Y_COORD, Double.toString(y));
		
		// foiID
		 
		paramCon.addParameterShell(SesLayerAdder.FOI_ID,feature.getID()); //原来是FOI_ID
		//paramCon.addParameterShell(SesLayerAdder.SENSOR_ID,feature.getID()); //原来的
		
		// observedProperty
		PhenomenonPropertyType phType = observation.getObservedProperty();
		paramCon.addParameterShell(SesLayerAdder.OBSERVED_PROPERTY, phType.getHref());
		
		// samplingTime
		ITime time = null;
		if (observation.getSamplingTime().getTimeObject() != null) {
            XmlObject timeXo = observation.getSamplingTime().getTimeObject().newCursor().getObject();
            SchemaType timeSchemaType = timeXo.schemaType();

            if (timeSchemaType.getJavaClass().isAssignableFrom(TimeInstantType.class)) {
                TimeInstantType xb_timeInstant = (TimeInstantType) timeXo;
                TimePositionType xb_timePosition = xb_timeInstant.getTimePosition();

                time = TimeFactory.createTime(xb_timePosition.getStringValue());
                paramCon.addParameterShell(SesLayerAdder.SAMPLING_TIME, time.toISO8601Format());
            }
		}
		// result
		String resultValue = observation.getResult().newCursor().getTextValue();
		paramCon.addParameterShell(SesLayerAdder.RESULT_VALUE, resultValue);
		
//		PhenomenonType b = (PhenomenonType) bla.getPhenomenon();
//		PhenomenonType type =  PhenomenonType.Factory.parse(bla.toString());
//		XmlObject result = observation.getResult();
//		PhenomenonPropertyType test = PhenomenonPropertyType.Factory.parse(observation.getObservedProperty().toString());
		LOGGER.info("=============进行到这里===================");
		LOGGER.info("paramCon " );
		System.out.println(paramCon);
		return paramCon;
	}
}
