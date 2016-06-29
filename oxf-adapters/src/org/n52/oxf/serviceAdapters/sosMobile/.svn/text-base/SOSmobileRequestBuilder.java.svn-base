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

package org.n52.oxf.serviceAdapters.sosMobile;

import javax.xml.namespace.QName;

import net.opengis.gml.AbstractGeometryType;
import net.opengis.gml.AbstractRingPropertyType;
import net.opengis.gml.AbstractRingType;
import net.opengis.gml.AbstractSurfaceType;
import net.opengis.gml.CoordinatesType;
import net.opengis.gml.DirectPositionType;
import net.opengis.gml.EnvelopeType;
import net.opengis.gml.FeaturePropertyType;
import net.opengis.gml.LinearRingType;
import net.opengis.gml.LocationPropertyType;
import net.opengis.gml.MetaDataPropertyType;
import net.opengis.gml.PolygonType;
import net.opengis.gml.StringOrRefType;
import net.opengis.gml.TimeInstantType;
import net.opengis.gml.TimePeriodType;
import net.opengis.gml.TimePositionType;
import net.opengis.ogc.BBOXType;
import net.opengis.ogc.BinaryTemporalOpType;
import net.opengis.ogc.PropertyNameType;
import net.opengis.ogc.SpatialOpsType;
import net.opengis.om.x10.MeasurementType;
import net.opengis.om.x10.ObservationType;
import net.opengis.om.x10.ProcessPropertyType;
import net.opengis.ows.x11.AcceptVersionsType;
import net.opengis.ows.x11.SectionsType;
import net.opengis.sampling.x10.SamplingPointType;
import net.opengis.sensorML.x101.AbstractProcessType;
import net.opengis.sensorML.x101.IoComponentPropertyType;
import net.opengis.sensorML.x101.SensorMLDocument;
import net.opengis.sensorML.x101.SystemType;
import net.opengis.sensorML.x101.CapabilitiesDocument.Capabilities;
import net.opengis.sensorML.x101.IdentificationDocument.Identification;
import net.opengis.sensorML.x101.IdentificationDocument.Identification.IdentifierList;
import net.opengis.sensorML.x101.IdentificationDocument.Identification.IdentifierList.Identifier;
import net.opengis.sensorML.x101.InputsDocument.Inputs;
import net.opengis.sensorML.x101.InputsDocument.Inputs.InputList;
import net.opengis.sensorML.x101.KeywordsDocument.Keywords.KeywordList;
import net.opengis.sensorML.x101.OutputsDocument.Outputs;
import net.opengis.sensorML.x101.OutputsDocument.Outputs.OutputList;
import net.opengis.sensorML.x101.PositionDocument.Position;
import net.opengis.sensorML.x101.SensorMLDocument.SensorML;
import net.opengis.sensorML.x101.SensorMLDocument.SensorML.Member;
import net.opengis.sensorML.x101.TermDocument.Term;
import net.opengis.sos.x10.DescribeSensorDocument;
import net.opengis.sos.x10.DomainFeatureType;
import net.opengis.sos.x10.GenericDomainFeatureDocument;
import net.opengis.sos.x10.GetCapabilitiesDocument;
import net.opengis.sos.x10.GetFeatureOfInterestDocument;
import net.opengis.sos.x10.GetFeatureOfInterestTimeDocument;
import net.opengis.sos.x10.GetObservationByIdDocument;
import net.opengis.sos.x10.GetObservationDocument;
import net.opengis.sos.x10.InsertObservationDocument;
import net.opengis.sos.x10.RegisterSensorDocument;
import net.opengis.sos.x10.ResponseModeType;
import net.opengis.sos.x10.UpdateSensorDocument;
import net.opengis.sos.x10.DescribeSensorDocument.DescribeSensor;
import net.opengis.sos.x10.GetCapabilitiesDocument.GetCapabilities;
import net.opengis.sos.x10.GetDomainFeatureDocument.GetDomainFeature;
import net.opengis.sos.x10.GetFeatureOfInterestDocument.GetFeatureOfInterest;
import net.opengis.sos.x10.GetFeatureOfInterestDocument.GetFeatureOfInterest.Location;
import net.opengis.sos.x10.GetFeatureOfInterestTimeDocument.GetFeatureOfInterestTime;
import net.opengis.sos.x10.GetFeatureOfInterestTimeDocument.GetFeatureOfInterestTime.DomainFeature;
import net.opengis.sos.x10.GetObservationByIdDocument.GetObservationById;
import net.opengis.sos.x10.GetObservationDocument.GetObservation;
import net.opengis.sos.x10.GetObservationDocument.GetObservation.EventTime;
import net.opengis.sos.x10.GetObservationDocument.GetObservation.Result;
import net.opengis.sos.x10.InsertObservationDocument.InsertObservation;
import net.opengis.sos.x10.ObservationTemplateDocument.ObservationTemplate;
import net.opengis.sos.x10.RegisterSensorDocument.RegisterSensor;
import net.opengis.sos.x10.RegisterSensorDocument.RegisterSensor.SensorDescription;
import net.opengis.sos.x10.UpdateSensorDocument.UpdateSensor;
import net.opengis.swe.x101.AbstractDataRecordType;
import net.opengis.swe.x101.AnyScalarPropertyType;
import net.opengis.swe.x101.PhenomenonPropertyType;
import net.opengis.swe.x101.PositionType;
import net.opengis.swe.x101.SimpleDataRecordType;
import net.opengis.swe.x101.TimeObjectPropertyType;
import net.opengis.swe.x101.VectorType;
import net.opengis.swe.x101.ObservablePropertyDocument.ObservableProperty;
import net.opengis.swe.x101.QuantityDocument.Quantity;
import net.opengis.swe.x101.VectorType.Coordinate;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.n52.oxf.OXFException;
import org.n52.oxf.owsCommon.capabilities.ITime;
import org.n52.oxf.owsCommon.capabilities.Parameter;
import org.n52.oxf.serviceAdapters.ParameterContainer;
import org.n52.oxf.serviceAdapters.ParameterShell;
import org.n52.oxf.serviceAdapters.sos.ISOSRequestBuilder;
import org.n52.oxf.serviceAdapters.sos.SOSAdapter;
import org.n52.oxf.util.LoggingHandler;
import org.n52.oxf.util.XmlBeansHelper;
import org.n52.oxf.valueDomains.time.ITimePeriod;
import org.n52.oxf.valueDomains.time.ITimePosition;
import org.n52.oxf.valueDomains.time.TimeFactory;

/**
 * Contains attributes and methods to encode SOSOperationRequests as String in xml-format.
 * 
 * This class always (!) adds the property mobileEnabled="true" to the requests.
 * 
 * @author <a href="mailto:daniel.nuest@uni-muenster.de">Daniel Nüst</a>
 */
public class SOSmobileRequestBuilder implements ISOSRequestBuilder {

    private static Logger LOGGER = LoggingHandler.getLogger(SOSmobileRequestBuilder.class);

    public static String REGISTER_SENSOR_INPUT_LIST_OBS_PROP_NAME = "inputListName";
    public static String REGISTER_SENSOR_INPUT_LIST_OBS_PROP_DEFINITION = "inputListNameURI";

    public static final String GET_FOI_TIME_SERVICE_PARAMETER = "service";
    public static final String GET_FOI_TIME_VERSION_PARAMETER = "version";
    public static final String GET_FOI_TIME_OFFERING_PARAMETER = "offering";
    public static final String GET_FOI_TIME_ID_PARAMETER = "FeatureOfInterestId";
    public static final String GET_FOI_TIME_SAMPLING_FEATURE_PARAMETER = "samplingFeature";
    public static final String GET_FOI_TIME_SENSOR_ID_PARAMETER = "sensorID";
    public static final String GET_FOI_TIME_OBSERVED_PROPERTY_PARAMETER = "observedProperty";

    public static final String REGISTER_SENSOR_FOI_PARAMETER = "featureOfInterestId";
    public static final String REGISTER_SENSOR_OFFERING_PARAMETER = "offering";

    public static final String UPDATE_SENSOR_SERVICE_PARAMETER = "service";
    public static final String UPDATE_SENSOR_VERSION_PARAMETER = "version";
    public static final String UPDATE_SENSOR_SENSOR_ID_PARAMETER = "sensorID";
    public static final String UPDATE_SENSOR_POSITION_PARAMETER = "position";
    public static final String UPDATE_SENSOR_ACTIVE_PARAMETER = "isActive";
    public static final String UPDATE_SENSOR_MOBILE_PARAMETER = "isMobile";

    public static final String GET_SAMPLING_FEATURE_SERVICE_PARAMETER = "service";
    public static final String GET_SAMPLING_FEATURE_VERSION_PARAMETER = "version";
    public static final String GET_SAMPLING_FEATURE_EVENT_TIME_PARAMETER = "eventTime";
    public static final String GET_SAMPLING_FEATURE_ID_PARAMETER = "samplingFeatureId";
    public static final String GET_SAMPLING_FEATURE_LOCATION_PARAMETER = "location";

    public static final String DOMAIN_FEATURE_NAME = "domainFeatureName";
    public static final String DOMAIN_FEATURE_ID = "domainFeatureID";
    public static final String DOMAIN_FEATURE_DESCRIPTION = "domainFeatureDesc";
    public static final String DOMAIN_FEATURE_LOCATION_LINEAR_RING_STRING = "dFLLinearRing";
    public static final String DOMAIN_FEATURE_LOCATION_SRS_NAME = "srsName";
    public static final String DOMAIN_FEATURE_LOCATION_BOUNDING_BOX_UPPER_CORNER = "upperCorner";
    public static final String DOMAIN_FEATURE_LOCATION_BOUNDING_BOX_LOWER_CORNER = "lowerCorner";

    public static final String SENSOR_ML_VERSION = "1.0.1";
    public static final String REGISTER_SENSOR_STATUS_PARAMETER = "status";
    public static final String REGISTER_SENSOR_MOBILE_PARAMETER = "mobile";
    public static final String REGISTER_SENSOR_INPUT_LIST = "inputList";
    public static final String REGISTER_SENSOR_OUTPUT_LIST = "outputList";
    public static final String REGISTER_SENSOR_OUTPUT_LIST_NAME = "outputListName";
    public static final String REGISTER_SENSOR_OUTPUT_LIST_QUANTITY_DEF = "outputListQuantityDef";
    public static final String REGISTER_SENSOR_OUTPUT_LIST_OFFERING_ID = "outputListOfferingId";
    public static final String REGISTER_SENSOR_OUTPUT_LIST_OFFERING_NAME = "outputListOfferingName";
    public static final String REGISTER_SENSOR_OUTPUT_LIST_UOM_CODE = "outputListUomCode";

    public static final String UPDATE_SENSOR_TIME_PARAMETER = "time";
    public static final String POSITION_FIXED_PARAMETER = "posFixed";
    public static final String POSITION_LATITUDE_PARAMETER = "posLat";
    public static final String POSITION_LONGITUDE_PARAMETER = "posLon";
    public static final String POSITION_NAME_PARAMETER = "posName";
    public static final String POSITION_UOM_CODE_PARAMETER = "posUomCode";
    public static final String POSITION_COORDINATE_LIST = "coordsList";
    public static final String POSITION_COORDINATE_LIST_LOC_VECTOR_COORDINATE_NAME = "posCordListName";
    public static final String POSITION_COORDINATE_LIST_LOC_VECTOR_COORDINATE_VALUE = "posCordListVal";
    public static final String POSITION_COORDINATE_LIST_LOC_VECTOR_COORDINATE_AXIS_ID = "posCordListAxisId";
    public static final String POSITION_COORDINATE_LIST_LOC_VECTOR_COORDINATE_UOM_CODE = "posCordListUomCode";

    public static final String OGC_SPATIAL_OPS_FILTER_SRS_NAME = "dFFilterSrs";
    public static final String OGC_SPATIAL_OPS_FILTER_BBOX_LOWER_CORNER = "dFFilterBBoxLower";
    public static final String OGC_SPATIAL_OPS_FILTER_BBOX_UPPER_CORNER = "dFFilterBBoxUpper";

    public static final String OGC_SPATIAL_OPS_FILTER_PROPERTY_NAME = "dFFilterPropertyName";

    public static final String OGC_SPATIAL_OPS_FILTER_BBOX = "BBOX";

    public static final String OGC_SPATIAL_OPS_FILTER_NAME = "filterName";

    public static final String POSITION_REFERENCE_FRAME_PARAMETER = "posReferenceFrame";

    public static final String SWE_QUANTITY_AXIS_ID_X = "x";

    public static final String SWE_QUANTITY_AXIS_ID_Y = "y";

    public static final String SWE_COORDINATE_NAME_LATITUDE = "latitude";

    public static final String SWE_COORDINATE_NAME_LONGITUDE = "longitude";

    public static final String GET_DOMAIN_FEATURE_SERVICE_PARAMETER = "dFServiceP";

    public static final String GET_DOMAIN_FEATURE_VERSION_PARAMETER = "dFVersionP";

    public static final String GET_DOMAIN_FEATURE_ID = "getDFID";

    private static final String GET_DOMAIN_FEATURE_LOCATION = "getDFLocation";

    public static final String DISABLE_MOBILE_FLAG = "disableMobileFlag";

    private static final String SCHEMA_LOCATION_REGISTER_SENSOR = "http://www.opengis.net/sos/1.0 http://schemas.opengis.net/sos/1.0.0/sosAll.xsd http://www.opengis.net/om/1.0 http://schemas.opengis.net/om/1.0.0/extensions/observationSpecialization_constraint.xsd";

    private static final String SCHEMA_LOCATION_INSERT_OBSERVATION = "http://www.opengis.net/sos/1.0 http://schemas.opengis.net/sos/1.0.0/sosAll.xsd http://www.opengis.net/om/1.0 http://schemas.opengis.net/om/1.0.0/extensions/observationSpecialization_constraint.xsd http://www.opengis.net/sampling/1.0 http://schemas.opengis.net/sampling/1.0.0/sampling.xsd";

    public static final String REGISTER_SENSOR_GML_DESCRIPTION = "regSenDescription";

    public static final String REGISTER_SENSOR_SML_KEYWORD_LIST = "keywordList";

    public static final String REGISTER_SENSOR_IDENTIFIER_LIST = "regSenIdentList";

    public static final String REGISTER_SENSOR_IDENTIFIER_LIST_TERM_DEF = "termDef";

    public static final String REGISTER_SENSOR_IDENTIFIER_LIST_TERM_VAL = "termValue";

    public boolean validationMode = false;

    /**
     * builds the GetCapabilities-Request. <br>
     * <br>
     * For the ParameterContainer 'parameters' are the ParameterShells with the following serviceSidedNames
     * required: <li>service</li>
     * 
     * <br>
     * <br>
     * The following are optional: The following are optional: <li>
     * updateSequence</li> <li>acceptVersions</li> <li>sections</li> <li>
     * acceptFormats</li>
     * 
     * @param parameters
     *        the parameters of the request
     * 
     * @return CapabilitiesRequest in xml-Format as String
     */
    public String buildGetCapabilitiesRequest(ParameterContainer parameters) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("buildGetCapabilitiesRequest with parameters: " + parameters);
        }

        GetCapabilitiesDocument getCapDoc = GetCapabilitiesDocument.Factory.newInstance();
        GetCapabilities getCap = getCapDoc.addNewGetCapabilities();

        // set required elements:
        getCap.setService((String) parameters.getParameterShellWithServiceSidedName(GET_CAPABILITIES_SERVICE_PARAMETER).getSpecifiedValue());

        getCap.setMobileEnabled(true);

        // set optional elements:

        // Parameter "updateSequence":
        if (parameters.getParameterShellWithServiceSidedName(GET_CAPABILITIES_UPDATE_SEQUENCE_PARAMETER) != null) {
            getCap.setUpdateSequence((String) parameters.getParameterShellWithServiceSidedName(GET_CAPABILITIES_UPDATE_SEQUENCE_PARAMETER).getSpecifiedValue());
        }

        // Parameter "AcceptVersions":
        ParameterShell versionPS = parameters.getParameterShellWithServiceSidedName(GET_CAPABILITIES_ACCEPT_VERSIONS_PARAMETER);
        if (versionPS == null) {
            versionPS = parameters.getParameterShellWithCommonName(Parameter.COMMON_NAME_VERSION);
        }
        if (versionPS != null) {
            AcceptVersionsType acceptedVersions = getCap.addNewAcceptVersions();

            if (versionPS.hasSingleSpecifiedValue()) {
                acceptedVersions.addVersion((String) versionPS.getSpecifiedValue());
            }
            else {
                Object[] versionArray = versionPS.getSpecifiedValueArray();
                for (Object version : versionArray) {
                    acceptedVersions.addVersion((String) version);
                }
            }
        }

        // Parameter "sections":
        ParameterShell sectionParamShell = parameters.getParameterShellWithServiceSidedName(GET_CAPABILITIES_SECTIONS_PARAMETER);
        if (sectionParamShell != null) {
            SectionsType sections = getCap.addNewSections();

            if (sectionParamShell.hasMultipleSpecifiedValues()) {
                String[] selectedSections = (String[]) sectionParamShell.getSpecifiedValueArray();
                for (int i = 0; i < selectedSections.length; i++) {
                    sections.addSection(selectedSections[i]);
                }
            }
            else if (sectionParamShell.hasSingleSpecifiedValue()) {
                sections.addSection((String) sectionParamShell.getSpecifiedValue());
            }
        }

        if (this.validationMode) {
            XmlBeansHelper.validateToSystemOut(getCapDoc);
        }

        return XmlBeansHelper.formatStringRequest(getCapDoc);
    }

    /**
     * builds the GetObservation-Request. <br>
     * <br>
     * For the ParameterContainer 'parameters' are the ParameterShells with the following serviceSidedNames
     * required: <li>service</li> <li>version</li> <li>offering</li> <li>observedProperty</li> <li>
     * resultFormat</li>
     * 
     * <br>
     * <br>
     * The following ones are optional: <li>eventTime</li> <li>procedure</li> <li>featureOfInterest</li> <li>
     * result</li> <li>resultModel</li> <li>
     * responseMode</li>
     * 
     * @param parameters
     *        parameters of the request
     * 
     * @return GetObservationRequest in XML-Format as String
     * @throws OXFException
     * @throws OXFException
     * 
     */
    public String buildGetObservationRequest(ParameterContainer parameters) throws OXFException {

        GetObservationDocument xb_getObsDoc = GetObservationDocument.Factory.newInstance();

        GetObservation xb_getObs = xb_getObsDoc.addNewGetObservation();

        // set required elements:
        xb_getObs.setService((String) parameters.getParameterShellWithServiceSidedName(GET_OBSERVATION_SERVICE_PARAMETER).getSpecifiedValue());

        xb_getObs.setVersion((String) parameters.getParameterShellWithServiceSidedName(GET_OBSERVATION_VERSION_PARAMETER).getSpecifiedValue());

        xb_getObs.setOffering((String) parameters.getParameterShellWithServiceSidedName(GET_OBSERVATION_OFFERING_PARAMETER).getSpecifiedValue());

        xb_getObs.setResponseFormat((String) parameters.getParameterShellWithServiceSidedName(GET_OBSERVATION_RESPONSE_FORMAT_PARAMETER).getSpecifiedValue());

        ParameterShell observedPropertyPS = parameters.getParameterShellWithServiceSidedName(GET_OBSERVATION_OBSERVED_PROPERTY_PARAMETER);
        if (observedPropertyPS.hasMultipleSpecifiedValues()) {
            Object[] observedProperties = observedPropertyPS.getSpecifiedValueArray();
            xb_getObs.setObservedPropertyArray((String[]) observedProperties);
        }
        else if (observedPropertyPS.hasSingleSpecifiedValue()) {
            String observedProperty = (String) observedPropertyPS.getSpecifiedValue();
            xb_getObs.setObservedPropertyArray(new String[] {observedProperty});
        }

        // set optional elements:
        if (parameters.getParameterShellWithServiceSidedName(GET_OBSERVATION_EVENT_TIME_PARAMETER) != null) {
            ITime specifiedTime;

            Object timeParamValue = parameters.getParameterShellWithServiceSidedName(GET_OBSERVATION_EVENT_TIME_PARAMETER).getSpecifiedValue();
            if (timeParamValue instanceof ITime) {
                specifiedTime = (ITime) timeParamValue;
            }
            else if (timeParamValue instanceof String) {
                specifiedTime = TimeFactory.createTime((String) timeParamValue);
            }
            else {
                throw new OXFException("The class (" + timeParamValue.getClass()
                        + ") of the value of the parameter 'eventTime' is not supported.");
            }

            BinaryTemporalOpType xb_binTempOp = BinaryTemporalOpType.Factory.newInstance();

            XmlCursor cursor = xb_binTempOp.newCursor();
            cursor.toChild(new QName(XmlBeansHelper.OGC_NAMESPACE_URI, "PropertyName"));
            cursor.setTextValue("urn:ogc:data:time:iso8601");

            String timeType = null;

            if (specifiedTime instanceof ITimePeriod) {
                ITimePeriod oc_timePeriod = (ITimePeriod) specifiedTime;
                TimePeriodType xb_timePeriod = TimePeriodType.Factory.newInstance();

                TimePositionType xb_beginPosition = xb_timePeriod.addNewBeginPosition();
                xb_beginPosition.setStringValue(oc_timePeriod.getStart().toISO8601Format());

                TimePositionType xb_endPosition = xb_timePeriod.addNewEndPosition();
                xb_endPosition.setStringValue(oc_timePeriod.getEnd().toISO8601Format());

                xb_binTempOp.setTimeObject(xb_timePeriod);
                timeType = "TimePeriod";
            }
            else if (specifiedTime instanceof ITimePosition) {
                ITimePosition oc_timePosition = (ITimePosition) specifiedTime;
                TimeInstantType xb_timeInstant = TimeInstantType.Factory.newInstance();

                TimePositionType xb_timePosition = TimePositionType.Factory.newInstance();
                xb_timePosition.setStringValue(oc_timePosition.toISO8601Format());

                xb_timeInstant.setTimePosition(xb_timePosition);

                xb_binTempOp.setTimeObject(xb_timeInstant);
                timeType = "TimePosition";
            }

            EventTime eventTime = xb_getObs.addNewEventTime();
            eventTime.setTemporalOps(xb_binTempOp);

            // rename elements:
            cursor = eventTime.newCursor();
            cursor.toChild(new QName(XmlBeansHelper.OGC_NAMESPACE_URI, "temporalOps"));
            cursor.setName(new QName(XmlBeansHelper.OGC_NAMESPACE_URI, "TM_Equals"));

            cursor.toChild(new QName(XmlBeansHelper.GML_NAMESPACE_URI, "_TimeObject"));
            cursor.setName(new QName(XmlBeansHelper.GML_NAMESPACE_URI, timeType));

            // TODO Spec-Too-Flexible-Problem: for eventTime are several other
            // "temporalOps" possible (not only "TMEquals")
        }

        if (parameters.getParameterShellWithServiceSidedName(GET_OBSERVATION_PROCEDURE_PARAMETER) != null) {
            Object[] procedures = parameters.getParameterShellWithServiceSidedName(GET_OBSERVATION_PROCEDURE_PARAMETER).getSpecifiedValueArray();
            xb_getObs.setProcedureArray((String[]) procedures);
        }

        if (parameters.getParameterShellWithServiceSidedName(GET_OBSERVATION_FEATURE_OF_INTEREST_PARAMETER) != null) {
            ParameterShell foiParamShell = parameters.getParameterShellWithServiceSidedName(GET_OBSERVATION_FEATURE_OF_INTEREST_PARAMETER);
            if (foiParamShell.hasMultipleSpecifiedValues()) {
                Object[] fois = foiParamShell.getSpecifiedValueArray();
                xb_getObs.addNewFeatureOfInterest().setObjectIDArray((String[]) fois);
            }
            else {
                Object foi = foiParamShell.getSpecifiedValue();
                xb_getObs.addNewFeatureOfInterest().setObjectIDArray(new String[] {(String) foi});
            }
            // TODO Spec-Too-Flexible-Problem: it is also possible that the
            // FeatureOfInterest is specified as
            // a "spatialOps"
        }

        if (parameters.getParameterShellWithServiceSidedName(GET_OBSERVATION_RESULT_PARAMETER) != null) {
            String filter = (String) parameters.getParameterShellWithServiceSidedName(GET_OBSERVATION_RESULT_PARAMETER).getSpecifiedValue();

            Result resultFilter = xb_getObs.addNewResult();

            try {
                XmlObject xobj = XmlObject.Factory.parse(filter);
                resultFilter.set(xobj);
            }
            catch (XmlException e) {
                throw new OXFException(e);
            }
        }

        if (parameters.getParameterShellWithServiceSidedName(GET_OBSERVATION_RESULT_MODEL_PARAMETER) != null) {
            xb_getObs.setResultModel( (new QName(XmlBeansHelper.GML_NAMESPACE_URI,
                                                 (String) parameters.getParameterShellWithServiceSidedName(GET_OBSERVATION_RESULT_MODEL_PARAMETER).getSpecifiedValue())));
        }

        if (parameters.getParameterShellWithServiceSidedName(GET_OBSERVATION_RESPONSE_MODE_PARAMETER) != null) {
            ResponseModeType.Enum responseModeEnum = ResponseModeType.Enum.forString((String) parameters.getParameterShellWithServiceSidedName(GET_OBSERVATION_RESPONSE_MODE_PARAMETER).getSpecifiedValue());
            xb_getObs.setResponseMode(responseModeEnum);
        }

        return XmlBeansHelper.formatStringRequest(xb_getObsDoc);
    }

    /**
     * builds the GetObservationByID-Request. <br>
     * <br>
     * For the ParameterContainer 'parameters' are the ParameterShells with the following serviceSidedNames
     * required:
     * 
     * <li>service</li>
     * 
     * <li>version</li>
     * 
     * <li>observationid</li>
     * 
     * <li>responseFormat</li>
     * 
     * <br>
     * The following ones are optional:
     * 
     * <li>responseMode</li>
     * 
     * TODO: implement optional responseFormat TODO: implement optional resultModel
     * 
     * @param parameters
     *        parameters of the request
     * 
     * @return GetObservationByID-Request in XML-Format as String
     * @throws OXFException
     * @throws OXFException
     * 
     */
    public String buildGetObservationByIDRequest(ParameterContainer parameters) throws OXFException {
        GetObservationByIdDocument xb_getObsDoc = GetObservationByIdDocument.Factory.newInstance();
        GetObservationById xb_getObs = xb_getObsDoc.addNewGetObservationById();

        // set required elements:
        xb_getObs.setService((String) parameters.getParameterShellWithServiceSidedName(GET_OBSERVATION_BY_ID_SERVICE_PARAMETER).getSpecifiedValue());
        xb_getObs.setVersion((String) parameters.getParameterShellWithServiceSidedName(GET_OBSERVATION_BY_ID_VERSION_PARAMETER).getSpecifiedValue());
        xb_getObs.setObservationId((String) parameters.getParameterShellWithServiceSidedName(GET_OBSERVATION_BY_ID_OBSERVATION_ID_PARAMETER).getSpecifiedValue());
        xb_getObs.setResponseFormat((String) parameters.getParameterShellWithServiceSidedName(GET_OBSERVATION_BY_ID_RESPONSE_FORMAT_PARAMETER).getSpecifiedValue());

        // set optional elements:
        if (parameters.getParameterShellWithServiceSidedName(GET_OBSERVATION_BY_ID_RESPONSE_MODE_PARAMETER) != null) {
            ResponseModeType.Enum responseModeEnum = ResponseModeType.Enum.forString((String) parameters.getParameterShellWithServiceSidedName(GET_OBSERVATION_BY_ID_RESPONSE_MODE_PARAMETER).getSpecifiedValue());
            xb_getObs.setResponseMode(responseModeEnum);
        }
        // TODO ResultModel?

        if (this.validationMode) {
            XmlBeansHelper.validateToSystemOut(xb_getObsDoc);
        }

        return XmlBeansHelper.formatStringRequest(xb_getObsDoc);
    }

    /**
     * builds a DescribeSensor-Request. <br>
     * <br>
     * For the ParameterContainer 'parameters' are the ParameterShells with the following serviceSidedNames
     * required:
     * 
     * <li>service</li>
     * 
     * <li>version</li>
     * 
     * <li>sensorID</li>
     * 
     * <li>outputFormat</li>
     * 
     * optional:
     * 
     * sos:time elements
     * 
     * @param parameters
     *        parameters and values of the request
     * 
     * @return DescribeSensorRequest in XML-Format as String
     */
    public String buildDescribeSensorRequest(ParameterContainer parameters) {
        DescribeSensorDocument descSensorDoc = DescribeSensorDocument.Factory.newInstance();
        descSensorDoc.xmlText(XmlBeansHelper.xmlOptionsForNamespaces());

        DescribeSensor descSensor = descSensorDoc.addNewDescribeSensor();

        /** required **/
        descSensor.setService((String) parameters.getParameterShellWithServiceSidedName(DESCRIBE_SENSOR_SERVICE_PARAMETER).getSpecifiedValue());
        descSensor.setVersion((String) parameters.getParameterShellWithServiceSidedName(DESCRIBE_SENSOR_VERSION_PARAMETER).getSpecifiedValue());
        descSensor.setProcedure((String) parameters.getParameterShellWithServiceSidedName(DESCRIBE_SENSOR_PROCEDURE_PARAMETER).getSpecifiedValue());
        if (parameters.getParameterShellWithServiceSidedName(ISOSRequestBuilder.DESCRIBE_SENSOR_OUTPUT_FORMAT) != null) {
            descSensor.setOutputFormat((String) parameters.getParameterShellWithServiceSidedName(DESCRIBE_SENSOR_OUTPUT_FORMAT).getSpecifiedValue());
        }
        else {
            throw new IllegalArgumentException("DESCRIBE_SENSOR_OUTPUT_FORMAT has to be set, default mostly >>text/xml;subtype=\"sensorML/1.0.1\"<<");
        }
        descSensor.setMobileEnabled(true);

        AbstractTemporalOpsBuilder temporalOpsBuilder = TemporalOpsBuilderFactory.generateRequestBuilder(SOSAdapter.DESCRIBE_SENSOR);

        /** optional time **/
        if (parameters.containsParameterShellWithCommonName(AbstractTemporalOpsBuilder.TM_AFTER_PARAMETER)) {
            temporalOpsBuilder.buildTM_After(descSensor, parameters);
        }

        if (parameters.containsParameterShellWithCommonName(AbstractTemporalOpsBuilder.TM_BEFORE_PARAMETER)) {
            temporalOpsBuilder.buildTM_Before(descSensor, parameters);
        }

        if (parameters.containsParameterShellWithCommonName(AbstractTemporalOpsBuilder.TM_EQUALS_PARAMETER)) {
            temporalOpsBuilder.buildTM_Equals(descSensor, parameters);
        }

        if (parameters.containsParameterShellWithCommonName(AbstractTemporalOpsBuilder.TM_DURING_PARAMETER)) {
            temporalOpsBuilder.buildTM_During(descSensor, parameters);
        }

        if (this.validationMode) {
            XmlBeansHelper.validateToSystemOut(descSensorDoc);
        }

        return XmlBeansHelper.formatStringRequest(descSensorDoc);
    }

    /**
	 * 
	 */
    public String buildGetFeatureOfInterestRequest(ParameterContainer parameters) {

        GetFeatureOfInterestDocument getFoIDoc = GetFeatureOfInterestDocument.Factory.newInstance();

        GetFeatureOfInterest getFoI = getFoIDoc.addNewGetFeatureOfInterest();

        // set required elements:
        getFoI.setService((String) parameters.getParameterShellWithServiceSidedName(GET_FOI_SERVICE_PARAMETER).getSpecifiedValue());

        ParameterShell versionPS = parameters.getParameterShellWithServiceSidedName(GET_FOI_VERSION_PARAMETER);
        getFoI.setVersion((String) versionPS.getSpecifiedValue());

        // set optional elements:
        if (parameters.containsParameterShellWithServiceSidedName(GET_FOI_ID_PARAMETER)) {
            ParameterShell foiIDParamShell = parameters.getParameterShellWithServiceSidedName(GET_FOI_ID_PARAMETER);
            if (foiIDParamShell.hasSingleSpecifiedValue()) {
                String foiIDParamValue = (String) foiIDParamShell.getSpecifiedValue();
                getFoI.setFeatureOfInterestIdArray(new String[] {foiIDParamValue});
            }
            else {
                String[] foiIDParamValue = (String[]) foiIDParamShell.getSpecifiedValueArray();
                getFoI.setFeatureOfInterestIdArray(foiIDParamValue);
            }
        }
        else if (parameters.containsParameterShellWithServiceSidedName(ISOSRequestBuilder.GET_FOI_LOCATION_PARAMETER)) {
            Location location = getFoI.addNewLocation();
            SpatialOpsType spatialOps = location.addNewSpatialOps();

            buildBBOX(spatialOps, parameters);
        }

        if (parameters.getParameterShellWithServiceSidedName(GET_FOI_EVENT_TIME_PARAMETER) != null) {
            throw new UnsupportedOperationException("The parameter '" + GET_FOI_EVENT_TIME_PARAMETER
                    + "' is not yet supported.");
            // TODO implement foi eventTime parameter
        }

        if (this.validationMode) {
            XmlBeansHelper.validateToSystemOut(getFoIDoc);
        }

        return XmlBeansHelper.formatStringRequest(getFoIDoc);
    }

    /**
	 * 
	 */
    public String buildInsertObservation(ParameterContainer parameters) {
        InsertObservationDocument insObDoc = InsertObservationDocument.Factory.newInstance();
        insObDoc.xmlText(XmlBeansHelper.xmlOptionsForNamespaces());

        InsertObservation insert = insObDoc.addNewInsertObservation();

        insert.setVersion((String) parameters.getParameterShellWithCommonName(ISOSRequestBuilder.INSERT_OBSERVATION_VERSION_PARAMETER).getSpecifiedValue());
        insert.setService((String) parameters.getParameterShellWithCommonName(ISOSRequestBuilder.INSERT_OBSERVATION_SERVICE_PARAMETER).getSpecifiedValue());
        insert.setAssignedSensorId((String) parameters.getParameterShellWithCommonName(ISOSRequestBuilder.INSERT_OBSERVATION_SENSOR_ID_PARAMETER).getSpecifiedValue());

        if (parameters.containsParameterShellWithCommonName(SOSmobileRequestBuilder.DISABLE_MOBILE_FLAG)) {
            boolean disable = Boolean.parseBoolean((String) parameters.getParameterShellWithCommonName(SOSmobileRequestBuilder.DISABLE_MOBILE_FLAG).getSpecifiedValue());
            if ( !disable) {
                insert.setMobileEnabled(true);
            }
        }
        else {
            insert.setMobileEnabled(true);
        }

        /** Observation **/
        ObservationType observation = insert.addNewObservation();
        MeasurementType measurement = (MeasurementType) observation.substitute(new QName(XmlBeansHelper.OM_1_0_NAMESPACE_URI,
                                                                                         "Measurement"),
                                                                               MeasurementType.type);

        /** samplingTime **/
        TimeObjectPropertyType timeProp = measurement.addNewSamplingTime();
        // AbstractTimeObjectType timeProp2 = (AbstractTimeObjectType) timeProp;

        TimeInstantType time = TimeInstantType.Factory.newInstance();
        TimePositionType timePos = time.addNewTimePosition();

        String timeParamValue = (String) parameters.getParameterShellWithCommonName(ISOSRequestBuilder.INSERT_OBSERVATION_SAMPLING_TIME).getSpecifiedValue();
        ITimePosition timePosition = (ITimePosition) TimeFactory.createTime(timeParamValue);
        timePos.setStringValue(timePosition.toISO8601Format());
        timeProp.setTimeObject(time);

        XmlCursor cursor = timeProp.newCursor();
        cursor.toChild(new QName(XmlBeansHelper.GML_NAMESPACE_URI, "_TimeObject"));
        cursor.setName(new QName(XmlBeansHelper.GML_NAMESPACE_URI, "TimeInstant"));

        /** procedure **/
        ProcessPropertyType procedure = measurement.addNewProcedure();
        procedure.setHref((String) parameters.getParameterShellWithCommonName(ISOSRequestBuilder.INSERT_OBSERVATION_PROCEDURE_PARAMETER).getSpecifiedValue());

        /** observedProperty **/
        PhenomenonPropertyType phenProp = measurement.addNewObservedProperty();
        phenProp.setHref((String) parameters.getParameterShellWithCommonName(ISOSRequestBuilder.INSERT_OBSERVATION_OBSERVED_PROPERTY_PARAMETER).getSpecifiedValue());

        /** domainFeature (optional) **/
        if (parameters.containsParameterShellWithCommonName(SOSmobileRequestBuilder.DOMAIN_FEATURE_ID)) {
            // TODO: fix Exception in bean: java.lang.ClassCastException:
            // org.apache.xmlbeans.impl.values.XmlAnyTypeImpl cannot be cast to
            // net.opengis.gml.FeaturePropertyType
            // FeaturePropertyType featurePropType = measurement.addNewDomainFeature();

            FeaturePropertyType[] featurePropTypeArray = new FeaturePropertyType[1];
            FeaturePropertyType fpt = FeaturePropertyType.Factory.newInstance();
            GenericDomainFeatureDocument domainFeatureDoc = buildDomainFeature(null, parameters);

            fpt.addNewFeature().set(domainFeatureDoc.getGenericDomainFeature());
            cursor = fpt.newCursor();
            cursor.toChild(new QName(XmlBeansHelper.GML_NAMESPACE_URI, "_Feature"));
            cursor.setName(new QName(XmlBeansHelper.SOS_1_0_NAMESPACE_URI, "GenericDomainFeature", "sos"));
            
            featurePropTypeArray[0] = fpt;
            measurement.setDomainFeatureArray(featurePropTypeArray);
        }

        /** featureOfInterest **/
        FeaturePropertyType featureProp = measurement.addNewFeatureOfInterest();
        // AbstractFeatureType abstractFeature = featureProp.addNewFeature();
        // SamplingFeatureType samplingFeature = (SamplingFeatureType) abstractFeature.substitute(new
        // QName(XmlBeansHelper.SA_1_0_NAMESPACE_URI, "SamplingFeature"), SamplingFeatureType.type);
        // java.lang.ClassCastException: net.opengis.gml.impl.AbstractFeatureTypeImpl cannot be cast to
        // net.opengis.sampling.x10.SamplingFeatureType

        SamplingPointType sampPoint = SamplingPointType.Factory.newInstance();
        sampPoint.setId((String) parameters.getParameterShellWithCommonName(ISOSRequestBuilder.INSERT_OBSERVATION_FOI_ID_PARAMETER).getSpecifiedValue());

        // Code for new Features
        if (parameters.containsParameterShellWithCommonName(ISOSRequestBuilder.INSERT_OBSERVATION_NEW_FOI_NAME)) {
            ParameterShell nameObj = parameters.getParameterShellWithCommonName(ISOSRequestBuilder.INSERT_OBSERVATION_NEW_FOI_NAME);
            String name = (String) nameObj.getSpecifiedValue();
            sampPoint.addNewName().setStringValue(name);
            String desc = (String) parameters.getParameterShellWithCommonName(ISOSRequestBuilder.INSERT_OBSERVATION_NEW_FOI_DESC).getSpecifiedValue();
            sampPoint.addNewDescription().setStringValue(desc);
            sampPoint.addNewSampledFeature();
            DirectPositionType pos = sampPoint.addNewPosition().addNewPoint().addNewPos();
            pos.setSrsName((String) parameters.getParameterShellWithCommonName(ISOSRequestBuilder.INSERT_OBSERVATION_NEW_FOI_POSITION_SRS).getSpecifiedValue());
            pos.setStringValue((String) parameters.getParameterShellWithCommonName(ISOSRequestBuilder.INSERT_OBSERVATION_NEW_FOI_POSITION).getSpecifiedValue());
        } // End Code for new Features

        featureProp.setFeature(sampPoint);
        XmlCursor cursorProp = featureProp.newCursor();
        cursorProp.xmlText(XmlBeansHelper.xmlOptionsForNamespaces());
        cursorProp.toChild(new QName(XmlBeansHelper.GML_NAMESPACE_URI, "_Feature"));
        cursorProp.setName(new QName(XmlBeansHelper.SA_1_0_NAMESPACE_URI, "SamplingPoint"));

        /** result **/
        XmlObject result = measurement.addNewResult();
        XmlCursor cursorRes = result.newCursor();
        cursorRes.setAttributeText(new QName("uom"),
                                   (String) parameters.getParameterShellWithCommonName(ISOSRequestBuilder.INSERT_OBSERVATION_VALUE_UOM_ATTRIBUTE).getSpecifiedValue());
        cursorRes.setTextValue((String) parameters.getParameterShellWithCommonName(ISOSRequestBuilder.INSERT_OBSERVATION_VALUE_PARAMETER).getSpecifiedValue());

        insertSchemaLocation(insert, SCHEMA_LOCATION_INSERT_OBSERVATION);

        if (this.validationMode) {
            XmlBeansHelper.validateToSystemOut(insObDoc);
        }

        return XmlBeansHelper.formatStringRequest(insObDoc);
    }

    /**
     * The RegisterSensor cocument consists of:
     * 
     * required attributes:
     * 
     * <li>service version, REGISTER_SENSOR_VERSION_PARAMETER</li>
     * 
     * <li>service type, REGISTER_SENSOR_SERVICE_PARAMETER</li>
     * 
     * <li>SensorDescription, see: buildSensorDescription()</li>
     * 
     * <li>ObservationTemplate, see: buildObservationTemplate</li>
     * 
     * optional:
     * 
     * <li>domainFeature, see: buildDomainFeature()</li>
     * 
     * 
     * TODO: support optional sos:offering TODO: support optional multiple domain features
     * 
     * @param parameters
     * 
     */
    public String buildRegisterSensor(ParameterContainer parameters) {
        RegisterSensorDocument regSensorDoc = RegisterSensorDocument.Factory.newInstance();
        regSensorDoc.xmlText(XmlBeansHelper.xmlOptionsForNamespaces());
        RegisterSensor regSensor = regSensorDoc.addNewRegisterSensor();

        regSensor.setService((String) parameters.getParameterShellWithCommonName(ISOSRequestBuilder.REGISTER_SENSOR_SERVICE_PARAMETER).getSpecifiedValue());
        regSensor.setVersion((String) parameters.getParameterShellWithCommonName(ISOSRequestBuilder.REGISTER_SENSOR_VERSION_PARAMETER).getSpecifiedValue());

        if (parameters.containsParameterShellWithCommonName(SOSmobileRequestBuilder.DISABLE_MOBILE_FLAG)) {
            boolean disable = Boolean.parseBoolean((String) parameters.getParameterShellWithCommonName(SOSmobileRequestBuilder.DISABLE_MOBILE_FLAG).getSpecifiedValue());
            if ( !disable) {
                regSensor.setMobileEnabled(true);
            }
        }
        else {
            regSensor.setMobileEnabled(true);
        }

        SensorDescription sensorDescription = regSensor.addNewSensorDescription();
        SensorMLDocument smlDoc = buildSensorDescription(parameters);
        sensorDescription.set(smlDoc);

        ObservationTemplate obsTemplate = regSensor.addNewObservationTemplate();
        buildObservationTemplate(obsTemplate);

        if (parameters.containsParameterShellWithCommonName(SOSmobileRequestBuilder.DOMAIN_FEATURE_ID)) {
            FeaturePropertyType domainFeatureType = regSensor.addNewDomainFeature();
            buildDomainFeature(domainFeatureType, parameters);
        }

        insertSchemaLocation(regSensor, SCHEMA_LOCATION_REGISTER_SENSOR);

        if (this.validationMode) {
            XmlBeansHelper.validateToSystemOut(regSensorDoc);
        }

        return XmlBeansHelper.formatStringRequest(regSensorDoc);
    }

    /**
     * required:
     * 
     * <li>service, GET_FOI_TIME_SERVICE_PARAMETER</li>
     * 
     * <li>version, GET_FOI_TIME_VERSION_PARAMETER</li>
     * 
     * <li>feature of interest id, GET_FOI_TIME_ID_PARAMETER</li>
     * 
     * 
     * optional (can be combined):
     * 
     * <li>offering</li>
     * 
     * <li>domainFeature as (list of) id(s) xor ogc:spatialOps (implemented ONLY BBOX!), DOMAIN_FEATURE_... If
     * both are contained, the id(s) are used!</li>
     * 
     * <li>sensorID, GET_FOI_TIME_SENSOR_ID_PARAMETER</li>
     * 
     * <li>observedProperty, GET_FOI_TIME_OBSERVED_PROPERTY_PARAMETER</li>
     * 
     * response is a GML time primitive
     * 
     */
    public String buildGetFeatureOfInterestTimeRequest(ParameterContainer parameters) {
        GetFeatureOfInterestTimeDocument getFoITimeDoc = GetFeatureOfInterestTimeDocument.Factory.newInstance();
        getFoITimeDoc.xmlText(XmlBeansHelper.xmlOptionsForNamespaces());
        GetFeatureOfInterestTime getFoiTime = getFoITimeDoc.addNewGetFeatureOfInterestTime();

        /** required **/
        getFoiTime.setService((String) parameters.getParameterShellWithServiceSidedName(SOSmobileRequestBuilder.GET_FOI_TIME_SERVICE_PARAMETER).getSpecifiedValue());
        getFoiTime.setVersion((String) parameters.getParameterShellWithServiceSidedName(SOSmobileRequestBuilder.GET_FOI_TIME_VERSION_PARAMETER).getSpecifiedValue());
        getFoiTime.setMobileEnabled(true);
        getFoiTime.setFeatureOfInterestId((String) parameters.getParameterShellWithServiceSidedName(SOSmobileRequestBuilder.GET_FOI_TIME_ID_PARAMETER).getSpecifiedValue());

        /** offering **/
        // TODO implement offering option
        /** [optional] domainFeature (id or bounding box) **/
        if (parameters.containsParameterShellWithServiceSidedName(SOSmobileRequestBuilder.DOMAIN_FEATURE_ID)) {
            ParameterShell domainFeatureShell = parameters.getParameterShellWithServiceSidedName(SOSmobileRequestBuilder.DOMAIN_FEATURE_ID);

            if (domainFeatureShell.hasSingleSpecifiedValue()) {
                DomainFeature domainFeature = getFoiTime.addNewDomainFeature();
                domainFeature.addNewObjectID().setStringValue((String) domainFeatureShell.getSpecifiedValue());
            }
            else if (domainFeatureShell.hasMultipleSpecifiedValues()) {
                DomainFeature domainFeature = getFoiTime.addNewDomainFeature();
                String[] ids = (String[]) domainFeatureShell.getSpecifiedValueArray();
                domainFeature.setObjectIDArray(ids);
            }
        }
        else if (parameters.containsParameterShellWithServiceSidedName(SOSmobileRequestBuilder.OGC_SPATIAL_OPS_FILTER_PROPERTY_NAME)) {
            DomainFeature domainFeature = getFoiTime.addNewDomainFeature();
            SpatialOpsType spatialOps = domainFeature.addNewSpatialOps();

            buildBBOX(spatialOps, parameters);

        }

        /** [optional] sensorID **/
        if (parameters.containsParameterShellWithServiceSidedName(SOSmobileRequestBuilder.GET_FOI_TIME_SENSOR_ID_PARAMETER)) {
            ParameterShell sensorIdShell = parameters.getParameterShellWithServiceSidedName(SOSmobileRequestBuilder.GET_FOI_TIME_SENSOR_ID_PARAMETER);
            if (sensorIdShell.hasSingleSpecifiedValue()) {
                getFoiTime.addSensorID((String) parameters.getParameterShellWithServiceSidedName(SOSmobileRequestBuilder.GET_FOI_TIME_SENSOR_ID_PARAMETER).getSpecifiedValue());
            }
            else if (sensorIdShell.hasMultipleSpecifiedValues()) {
                String[] sensorIds = (String[]) sensorIdShell.getSpecifiedValueArray();
                getFoiTime.setSensorIDArray(sensorIds);
            }

        }

        /** [optional] observedProperty **/
        if (parameters.containsParameterShellWithServiceSidedName(SOSmobileRequestBuilder.GET_FOI_TIME_OBSERVED_PROPERTY_PARAMETER)) {

            ParameterShell observedPropertyShell = parameters.getParameterShellWithServiceSidedName(SOSmobileRequestBuilder.GET_FOI_TIME_OBSERVED_PROPERTY_PARAMETER);

            if (observedPropertyShell.hasSingleSpecifiedValue()) {
                getFoiTime.addObservedProperty((String) parameters.getParameterShellWithServiceSidedName(SOSmobileRequestBuilder.GET_FOI_TIME_OBSERVED_PROPERTY_PARAMETER).getSpecifiedValue());
            }
            else if (observedPropertyShell.hasMultipleSpecifiedValues()) {
                String[] observedProperties = (String[]) observedPropertyShell.getSpecifiedValueArray();
                getFoiTime.setObservedPropertyArray(observedProperties);
            }
        }

        if (this.validationMode) {
            XmlBeansHelper.validateToSystemOut(getFoITimeDoc);
        }

        return XmlBeansHelper.formatStringRequest(getFoITimeDoc);
    }

    /**
     * 
     * required:
     * 
     * <li>service version, UPDATE_SENSOR_SERVICE_PARAMETER</li>
     * 
     * <li>service type, UPDATE_SENSOR_VERSION_PARAMETER</li>
     * 
     * <li>SensorID, UPDATE_SENSOR_SENSOR_ID_PARAMETER</li>
     * 
     * <li>timeStamp, UPDATE_SENSOR_TIME_PARAMETER</li>
     * 
     * optional:
     * 
     * <li>position, see: buildLatLonPosition()</li>
     * 
     * <li>domainFeature, see: buildDomainFeature()</li>
     * 
     * <li>isMobile, UPDATE_SENSOR_MOBILE_PARAMETER</li>
     * 
     * <li>isActive, UPDATE_SENSOR_ACTIVE_PARAMETER</li>
     * 
     * 
     * TODO: implement optional sos:property
     * 
     * 
     * @param parameters
     * @return
     */
    public String buildUpdateSensor(ParameterContainer parameters) {
        UpdateSensorDocument updateSensorDoc = UpdateSensorDocument.Factory.newInstance();
        updateSensorDoc.xmlText(XmlBeansHelper.xmlOptionsForNamespaces());

        UpdateSensor updateSensor = updateSensorDoc.addNewUpdateSensor();

        // XmlCursor obsCursor = updateSensor.newCursor();
        // obsCursor.toFirstContentToken();
        // obsCursor.insertNamespace("sos", "http://www.opengis.net/sos/1.0");
        // obsCursor.dispose();

        // XmlBeansHelper.addDefaultNS(updateSensor);

        /** REQUIRED **/
        updateSensor.setService((String) parameters.getParameterShellWithCommonName(SOSmobileRequestBuilder.UPDATE_SENSOR_SERVICE_PARAMETER).getSpecifiedValue());
        updateSensor.setVersion((String) parameters.getParameterShellWithCommonName(SOSmobileRequestBuilder.UPDATE_SENSOR_VERSION_PARAMETER).getSpecifiedValue());
        updateSensor.setMobileEnabled(true);
        updateSensor.setSensorID((String) parameters.getParameterShellWithCommonName(SOSmobileRequestBuilder.UPDATE_SENSOR_SENSOR_ID_PARAMETER).getSpecifiedValue());

        /** TimeStamp **/
        TimeInstantType time = updateSensor.addNewTimeStamp();
        TimePositionType timePos = time.addNewTimePosition();
        timePos.setStringValue((String) parameters.getParameterShellWithCommonName(SOSmobileRequestBuilder.UPDATE_SENSOR_TIME_PARAMETER).getSpecifiedValue());

        /** OPTIONAL **/
        /** Position **/
        if (parameters.containsParameterShellWithServiceSidedName(SOSmobileRequestBuilder.POSITION_LATITUDE_PARAMETER)
                && parameters.containsParameterShellWithServiceSidedName(SOSmobileRequestBuilder.POSITION_LONGITUDE_PARAMETER)) {
            PositionType positionType = updateSensor.addNewPosition();
            buildLatLonPosition(parameters, positionType);
        }

        /** DomainFeature **/
        if (parameters.containsParameterShellWithCommonName(SOSmobileRequestBuilder.DOMAIN_FEATURE_ID)) {
            FeaturePropertyType featurePropType = updateSensor.addNewDomainFeature();
            buildDomainFeature(featurePropType, parameters);
        }

        /** more attributes **/
        if (parameters.containsParameterShellWithCommonName(SOSmobileRequestBuilder.UPDATE_SENSOR_MOBILE_PARAMETER)) {
            updateSensor.setIsMobile(Boolean.parseBoolean((String) parameters.getParameterShellWithCommonName(SOSmobileRequestBuilder.UPDATE_SENSOR_MOBILE_PARAMETER).getSpecifiedValue()));
        }
        if (parameters.containsParameterShellWithCommonName(SOSmobileRequestBuilder.UPDATE_SENSOR_ACTIVE_PARAMETER)) {

            updateSensor.setIsActive(Boolean.parseBoolean((String) parameters.getParameterShellWithCommonName(SOSmobileRequestBuilder.UPDATE_SENSOR_ACTIVE_PARAMETER).getSpecifiedValue()));
        }

        if (this.validationMode) {
            XmlBeansHelper.validateToSystemOut(updateSensorDoc);
        }

        return XmlBeansHelper.formatStringRequest(updateSensorDoc);
    }

    /**
     * 
     * @param parameters
     * @return
     */
    public String buildGetDomainFeature(ParameterContainer parameters) {
        GetDomainFeature getDomFeatDoc = GetDomainFeature.Factory.newInstance();
        getDomFeatDoc.xmlText(XmlBeansHelper.xmlOptionsForNamespaces());

        /** REQUIRED **/
        getDomFeatDoc.setService((String) parameters.getParameterShellWithCommonName(SOSmobileRequestBuilder.GET_DOMAIN_FEATURE_SERVICE_PARAMETER).getSpecifiedValue());
        getDomFeatDoc.setVersion((String) parameters.getParameterShellWithCommonName(SOSmobileRequestBuilder.GET_DOMAIN_FEATURE_VERSION_PARAMETER).getSpecifiedValue());
        getDomFeatDoc.setMobileEnabled(true);

        if (parameters.containsParameterShellWithCommonName(SOSmobileRequestBuilder.GET_DOMAIN_FEATURE_ID)) {

            ParameterShell domainFeatureIDShell = parameters.getParameterShellWithServiceSidedName(SOSmobileRequestBuilder.GET_DOMAIN_FEATURE_ID);
            if (domainFeatureIDShell.hasSingleSpecifiedValue()) {
                String id = (String) parameters.getParameterShellWithCommonName(SOSmobileRequestBuilder.GET_DOMAIN_FEATURE_ID).getSpecifiedValue();
                getDomFeatDoc.addDomainFeatureId(id);
            }
            else if (domainFeatureIDShell.hasMultipleSpecifiedValues()) {
                String[] ids = (String[]) domainFeatureIDShell.getSpecifiedValueArray();
                getDomFeatDoc.setDomainFeatureIdArray(ids);
            }
        }
        else if (parameters.containsParameterShellWithCommonName(SOSmobileRequestBuilder.GET_DOMAIN_FEATURE_LOCATION)) {
            // TODO add location to get domain feature
            throw new UnsupportedOperationException("get domain feature with location is not implemented!");
        }

        if (this.validationMode) {
            XmlBeansHelper.validateToSystemOut(getDomFeatDoc);
        }

        return XmlBeansHelper.formatStringRequest(getDomFeatDoc);
    }

    private void insertSchemaLocation(XmlObject doc, String schemaLocation) {
        XmlCursor cursor = doc.newCursor();
        cursor.toFirstContentToken();
        cursor.toLastAttribute();
        cursor.insertAttributeWithValue(new QName("http://www.w3.org/2001/XMLSchema-instance", "schemaLocation"),
                                        schemaLocation);
    }

    /**
     * Builds a description in SensorML (version 1.0.1) puts it into the given {@link RegisterSensor}.
     * Included are:
     * 
     * - Identifier
     * 
     * - Capabilities
     * 
     * - InputList (Array of Pair<String, String>s with name (elem1) and observable property (elem2)
     * 
     * - OutputList
     * 
     * - Components
     * 
     * @param smlMember
     * @param parameters
     */
    private SensorMLDocument buildSensorDescription(ParameterContainer parameters) {
        SensorMLDocument smlDocument = SensorMLDocument.Factory.newInstance();
        smlDocument.xmlText(XmlBeansHelper.xmlOptionsForNamespaces());

        SensorML sml = smlDocument.addNewSensorML();
        sml.setVersion(SENSOR_ML_VERSION);
        Member smlMember = sml.addNewMember();

        AbstractProcessType abstractProcess = smlMember.addNewProcess();
        SystemType systemType = (SystemType) abstractProcess.substitute(new QName(XmlBeansHelper.SML_1_0_1_NAMESPACE_URI,
                                                                                  "System"),
                                                                        SystemType.type);

        /** gml:description **/
        if (parameters.containsParameterShellWithCommonName(SOSmobileRequestBuilder.REGISTER_SENSOR_GML_DESCRIPTION)) {
            StringOrRefType descr = systemType.addNewDescription();
            descr.setStringValue((String) parameters.getParameterShellWithCommonName(SOSmobileRequestBuilder.REGISTER_SENSOR_GML_DESCRIPTION).getSpecifiedValue());
        }

        /** sml:keywords **/
        if (parameters.containsParameterShellWithCommonName(SOSmobileRequestBuilder.REGISTER_SENSOR_SML_KEYWORD_LIST)) {
            String[] keywords = (String[]) parameters.getParameterShellWithCommonName(SOSmobileRequestBuilder.REGISTER_SENSOR_SML_KEYWORD_LIST).getSpecifiedValueArray();
            KeywordList keywordList = systemType.addNewKeywords().addNewKeywordList();
            for (String s : keywords) {
                keywordList.addKeyword(s);
            }
        }

        /** sml:Identification **/
        if (parameters.containsParameterShellWithCommonName(SOSmobileRequestBuilder.REGISTER_SENSOR_IDENTIFIER_LIST)) {
            Object o = parameters.getParameterShellWithCommonName(SOSmobileRequestBuilder.REGISTER_SENSOR_IDENTIFIER_LIST).getSpecifiedValueArray();
            if (o instanceof ParameterContainer[]) {
                Identification identification = systemType.addNewIdentification();
                IdentifierList identifierList = identification.addNewIdentifierList();
                ParameterContainer[] identifiers = (ParameterContainer[]) o;
                for (ParameterContainer i : identifiers) {
                    Identifier ident = identifierList.addNewIdentifier();
                    Term term = ident.addNewTerm();
                    term.setDefinition((String) i.getParameterShellWithCommonName(SOSmobileRequestBuilder.REGISTER_SENSOR_IDENTIFIER_LIST_TERM_DEF).getSpecifiedValue());
                    term.setValue((String) i.getParameterShellWithCommonName(SOSmobileRequestBuilder.REGISTER_SENSOR_IDENTIFIER_LIST_TERM_VAL).getSpecifiedValue());
                }
            }
            else {
                LOGGER.error("InputList does not contain array of ParameterContainers.");
            }
        }
        else {
            Identification identification = systemType.addNewIdentification();
            Identifier identifier = identification.addNewIdentifierList().addNewIdentifier();
            Term term = identifier.addNewTerm();
            term.setDefinition("urn:ogc:def:identifier:OGC:uniqueID");
            term.setValue((String) parameters.getParameterShellWithCommonName(ISOSRequestBuilder.REGISTER_SENSOR_ID_PARAMETER).getSpecifiedValue());
        }

        /** sml:capabilities **/
        Capabilities capabilities = systemType.addNewCapabilities();
        AbstractDataRecordType abstractDataRec = capabilities.addNewAbstractDataRecord();
        SimpleDataRecordType simpleDataRec = (SimpleDataRecordType) abstractDataRec.substitute(new QName(XmlBeansHelper.SWE_1_0_1_NAMESPACE_URI,
                                                                                                         "SimpleDataRecord"),
                                                                                               SimpleDataRecordType.type);

        if (parameters.containsParameterShellWithCommonName(SOSmobileRequestBuilder.REGISTER_SENSOR_STATUS_PARAMETER)) {
            AnyScalarPropertyType statusBoolean = simpleDataRec.addNewField();
            statusBoolean.setName(SOSmobileRequestBuilder.REGISTER_SENSOR_STATUS_PARAMETER);
            statusBoolean.addNewBoolean().setValue(Boolean.parseBoolean((String) parameters.getParameterShellWithCommonName(SOSmobileRequestBuilder.REGISTER_SENSOR_STATUS_PARAMETER).getSpecifiedValue()));
        }

        if (parameters.containsParameterShellWithCommonName(SOSmobileRequestBuilder.REGISTER_SENSOR_MOBILE_PARAMETER)) {
            AnyScalarPropertyType mobileBoolean = simpleDataRec.addNewField();
            mobileBoolean.setName(SOSmobileRequestBuilder.REGISTER_SENSOR_MOBILE_PARAMETER);
            mobileBoolean.addNewBoolean().setValue(Boolean.parseBoolean((String) parameters.getParameterShellWithCommonName(SOSmobileRequestBuilder.REGISTER_SENSOR_MOBILE_PARAMETER).getSpecifiedValue()));
        }

        /** swe:Position **/
        Position position = systemType.addNewPosition();
        // cannot use ISOSRequestBuilder.REGISTER_SENSOR_POSITION_NAME_PARAMETER as this method is used for
        // more than just register sensor
        position.setName((String) parameters.getParameterShellWithCommonName(SOSmobileRequestBuilder.POSITION_NAME_PARAMETER).getSpecifiedValue());
        AbstractProcessType abstractProcessPos = position.addNewProcess();
        PositionType positionType = (PositionType) abstractProcessPos.substitute(new QName(XmlBeansHelper.SWE_1_0_1_NAMESPACE_URI,
                                                                                           "Position"),
                                                                                 PositionType.type);
        buildLatLonPosition(parameters, positionType);

        /** sml:inputs **/
        Inputs inputs = systemType.addNewInputs();
        InputList inputList = inputs.addNewInputList();
        Object o = parameters.getParameterShellWithCommonName(SOSmobileRequestBuilder.REGISTER_SENSOR_INPUT_LIST).getSpecifiedValueArray();
        if (o instanceof ParameterContainer[]) {
            ParameterContainer[] outputSets = (ParameterContainer[]) o;
            for (ParameterContainer inputElement : outputSets) {
                IoComponentPropertyType ioComp = inputList.addNewInput();
                ioComp.setName((String) inputElement.getParameterShellWithCommonName(SOSmobileRequestBuilder.REGISTER_SENSOR_INPUT_LIST_OBS_PROP_NAME).getSpecifiedValue());
                ObservableProperty obsProp = ioComp.addNewObservableProperty();
                String def = (String) inputElement.getParameterShellWithCommonName(SOSmobileRequestBuilder.REGISTER_SENSOR_INPUT_LIST_OBS_PROP_DEFINITION).getSpecifiedValue();
                obsProp.setDefinition(def);
            }
        }
        else {
            LOGGER.error("InputList does not contain array of ParameterContainers.");
        }
        o = null;

        /**
         * <!-- list containing the output phenomena of this sensor system; ATTENTION: these phenomena are
         * parsed and inserted into the database; they have to contain offering elements to determine the
         * correct offering for the sensors and measured phenomena -->
         * 
         * <sml:outputs
         **/
        Outputs outputs = systemType.addNewOutputs();
        OutputList outputList = outputs.addNewOutputList();
        o = parameters.getParameterShellWithCommonName(SOSmobileRequestBuilder.REGISTER_SENSOR_OUTPUT_LIST).getSpecifiedValueArray();
        if (o instanceof ParameterContainer[]) {
            ParameterContainer[] outputSets = (ParameterContainer[]) o;
            for (ParameterContainer outputElement : outputSets) {
                IoComponentPropertyType ioComp = outputList.addNewOutput();

                ioComp.setName((String) outputElement.getParameterShellWithCommonName(SOSmobileRequestBuilder.REGISTER_SENSOR_OUTPUT_LIST_NAME).getSpecifiedValue());

                Quantity quantity = ioComp.addNewQuantity();
                quantity.setDefinition((String) outputElement.getParameterShellWithCommonName(SOSmobileRequestBuilder.REGISTER_SENSOR_OUTPUT_LIST_QUANTITY_DEF).getSpecifiedValue());

                MetaDataPropertyType metaDataProperty = quantity.addNewMetaDataProperty();
                XmlCursor cursor = metaDataProperty.newCursor();
                cursor.toNextToken();
                cursor.beginElement("offering");
                cursor.insertElementWithText("id",
                                             (String) outputElement.getParameterShellWithCommonName(SOSmobileRequestBuilder.REGISTER_SENSOR_OUTPUT_LIST_OFFERING_ID).getSpecifiedValue());
                cursor.insertElementWithText("name",
                                             (String) outputElement.getParameterShellWithCommonName(SOSmobileRequestBuilder.REGISTER_SENSOR_OUTPUT_LIST_OFFERING_NAME).getSpecifiedValue());

                quantity.addNewUom().setCode((String) outputElement.getParameterShellWithCommonName(SOSmobileRequestBuilder.REGISTER_SENSOR_OUTPUT_LIST_UOM_CODE).getSpecifiedValue());

            }
        }
        else {
            LOGGER.error("OutputList does not contain array of ParameterContainers.");
        }
        o = null;

        /** swe:Components **/
        // TODO: implement components for RegisterSensor
        // components currently not used by 52N SOS
        // Components components = systemType.addNewComponents();
        return smlDocument;
    }

    /**
     * coordinate names hard coded to: longitude, latitude
     * 
     * uom hard coded to: degree
     * 
     * @param parameters
     * @param position
     */
    private void buildLatLonPosition(ParameterContainer parameters, PositionType positionType) {
        positionType.setReferenceFrame((String) parameters.getParameterShellWithCommonName(SOSmobileRequestBuilder.POSITION_REFERENCE_FRAME_PARAMETER).getSpecifiedValue());

        if (parameters.containsParameterShellWithCommonName(SOSmobileRequestBuilder.POSITION_FIXED_PARAMETER)) {
            positionType.setFixed(Boolean.getBoolean((String) parameters.getParameterShellWithCommonName(SOSmobileRequestBuilder.POSITION_FIXED_PARAMETER).getSpecifiedValue()));
        }

        String uom;
        if (parameters.containsParameterShellWithCommonName(SOSmobileRequestBuilder.POSITION_UOM_CODE_PARAMETER)) {
            uom = (String) parameters.getParameterShellWithCommonName(SOSmobileRequestBuilder.POSITION_UOM_CODE_PARAMETER).getSpecifiedValue();
        }
        else {
            uom = "degree";
        }

        if (parameters.containsParameterShellWithCommonName(SOSmobileRequestBuilder.POSITION_COORDINATE_LIST)) {
            VectorType vector = positionType.addNewLocation().addNewVector();

            Object o = parameters.getParameterShellWithCommonName(SOSmobileRequestBuilder.POSITION_COORDINATE_LIST).getSpecifiedValueArray();
            if (o instanceof ParameterContainer[]) {
                ParameterContainer[] coordinates = (ParameterContainer[]) o;
                for (ParameterContainer coord : coordinates) {
                    Coordinate coordLatitude = vector.addNewCoordinate();
                    ParameterShell coordName = coord.getParameterShellWithCommonName(SOSmobileRequestBuilder.POSITION_COORDINATE_LIST_LOC_VECTOR_COORDINATE_NAME);
                    coordLatitude.setName((String) coordName.getSpecifiedValue());
                    Quantity quantityLatitude = coordLatitude.addNewQuantity();
                    quantityLatitude.setAxisID((String) coord.getParameterShellWithCommonName(SOSmobileRequestBuilder.POSITION_COORDINATE_LIST_LOC_VECTOR_COORDINATE_AXIS_ID).getSpecifiedValue());
                    quantityLatitude.addNewUom().setCode((String) coord.getParameterShellWithCommonName(SOSmobileRequestBuilder.POSITION_COORDINATE_LIST_LOC_VECTOR_COORDINATE_UOM_CODE).getSpecifiedValue());
                    quantityLatitude.setValue(Double.parseDouble((String) coord.getParameterShellWithCommonName(SOSmobileRequestBuilder.POSITION_COORDINATE_LIST_LOC_VECTOR_COORDINATE_VALUE).getSpecifiedValue()));
                }
            }
            else {
                LOGGER.error("Coordinates does not contain array of ParameterContainers.");
            }
        }
        else {

            VectorType vector = positionType.addNewLocation().addNewVector();
            Coordinate coordLatitude = vector.addNewCoordinate();
            coordLatitude.setName(SWE_COORDINATE_NAME_LATITUDE);
            Quantity quantityLatitude = coordLatitude.addNewQuantity();
            quantityLatitude.setAxisID(SWE_QUANTITY_AXIS_ID_Y);
            quantityLatitude.addNewUom().setCode(uom);
            quantityLatitude.setValue(Double.parseDouble((String) parameters.getParameterShellWithCommonName(SOSmobileRequestBuilder.POSITION_LATITUDE_PARAMETER).getSpecifiedValue()));

            Coordinate coordLongitude = vector.addNewCoordinate();
            coordLongitude.setName(SWE_COORDINATE_NAME_LONGITUDE);
            Quantity quantityLongitude = coordLongitude.addNewQuantity();
            quantityLongitude.setAxisID(SWE_QUANTITY_AXIS_ID_X);
            quantityLongitude.setValue(Double.parseDouble((String) parameters.getParameterShellWithCommonName(SOSmobileRequestBuilder.POSITION_LONGITUDE_PARAMETER).getSpecifiedValue()));
        }
    }

    /**
     * replace given {@link FeaturePropertyType} with a GenericDomainFeature
     * 
     * TODO: add support for multiple domain features (as required by RegisterSensor)
     * 
     * @param featurePropType
     *        {@link FeaturePropertyType} the built {@link DomainFeatureType} is inserted into. This parameter
     *        can be null, so the returned {@link DomainFeatureType} can be used.
     * @param parameters
     * @return
     */
    private GenericDomainFeatureDocument buildDomainFeature(FeaturePropertyType featurePropType,
                                                            ParameterContainer parameters) {

        // TODO: fix substitution once substitution from several jars is
        // supported
        // AbstractFeatureType abstractFeature =
        // featurePropType.addNewFeature();
        // DomainFeatureType domf = (DomainFeatureType) abstractFeature
        // .substitute(new QName(XmlBeansHelper.SOS_1_0_NAMESPACE_URI,
        // "GenericDomainFeature"), DomainFeatureType.type);

        GenericDomainFeatureDocument genericDFDoc = GenericDomainFeatureDocument.Factory.newInstance();

        DomainFeatureType domainFeature = genericDFDoc.addNewGenericDomainFeature();

        domainFeature.setId((String) parameters.getParameterShellWithCommonName(SOSmobileRequestBuilder.DOMAIN_FEATURE_ID).getSpecifiedValue());

        /** might not be included... **/
        if (parameters.containsParameterShellWithCommonName(SOSmobileRequestBuilder.DOMAIN_FEATURE_DESCRIPTION)) {
            domainFeature.addNewDescription().setStringValue((String) parameters.getParameterShellWithCommonName(SOSmobileRequestBuilder.DOMAIN_FEATURE_DESCRIPTION).getSpecifiedValue());
        }
        if (parameters.containsParameterShellWithCommonName(DOMAIN_FEATURE_NAME)) {
            domainFeature.addNewName().setStringValue((String) parameters.getParameterShellWithCommonName(SOSmobileRequestBuilder.DOMAIN_FEATURE_NAME).getSpecifiedValue());
        }

        if (parameters.containsParameterShellWithCommonName(SOSmobileRequestBuilder.DOMAIN_FEATURE_LOCATION_SRS_NAME)
                && parameters.containsParameterShellWithCommonName(SOSmobileRequestBuilder.DOMAIN_FEATURE_LOCATION_LINEAR_RING_STRING)) {
            LocationPropertyType locationType = domainFeature.addNewLocation();
            AbstractGeometryType abstractGeometry = locationType.addNewGeometry();
            AbstractSurfaceType abstractSurface = (AbstractSurfaceType) abstractGeometry.substitute(new QName(XmlBeansHelper.GML_NAMESPACE_URI,
                                                                                                              "_Surface"),
                                                                                                    AbstractSurfaceType.type);
            PolygonType polygon = (PolygonType) abstractSurface.substitute(new QName(XmlBeansHelper.GML_NAMESPACE_URI,
                                                                                     "Polygon"), PolygonType.type);
            polygon.setSrsName((String) parameters.getParameterShellWithCommonName(SOSmobileRequestBuilder.DOMAIN_FEATURE_LOCATION_SRS_NAME).getSpecifiedValue());
            AbstractRingPropertyType ringPropType = polygon.addNewExterior();
            AbstractRingType ringType = ringPropType.addNewRing();
            LinearRingType linearRingType = (LinearRingType) ringType.substitute(new QName(XmlBeansHelper.GML_NAMESPACE_URI,
                                                                                           "LinearRing"),
                                                                                 LinearRingType.type);
            CoordinatesType coordsType = linearRingType.addNewCoordinates();
            coordsType.setStringValue((String) parameters.getParameterShellWithCommonName(SOSmobileRequestBuilder.DOMAIN_FEATURE_LOCATION_LINEAR_RING_STRING).getSpecifiedValue());
        }

        // directly insert into given featureProperty
        if (featurePropType != null) {
            featurePropType.set(genericDFDoc);
        }
        return genericDFDoc; // allows use of .set(XmlObject) calls
    }

    /**
     * ObservationTemplate has to be an empty measurement at the moment, as the 52N SOS only supports
     * Measurements to be inserted.
     * 
     * @param obsTemp
     */
    private void buildObservationTemplate(ObservationTemplate obsTemp) {
        ObservationType obsType = obsTemp.addNewObservation();

        MeasurementType measurementType = (MeasurementType) obsType.substitute(new QName(XmlBeansHelper.OM_1_0_NAMESPACE_URI,
                                                                                         "Measurement"),
                                                                               MeasurementType.type);

        measurementType.addNewSamplingTime();
        measurementType.addNewProcedure();
        measurementType.addNewObservedProperty();
        measurementType.addNewFeatureOfInterest();
        XmlObject result = measurementType.addNewResult();

        XmlCursor resultCursor = result.newCursor();
        resultCursor.toNextToken();
        resultCursor.insertAttributeWithValue("uom", "");
        resultCursor.insertChars("0.0");
        resultCursor.dispose();

        // XmlBeansHelper.addDefaultNS(measurementType);
    }

    /**
     * 
     * @param spatialOps
     * @param parameters
     */
    private void buildBBOX(SpatialOpsType spatialOps, ParameterContainer parameters) {

        BBOXType bbox = (BBOXType) spatialOps.substitute(new QName(XmlBeansHelper.OGC_NAMESPACE_URI, "BBOX"),
                                                         BBOXType.type);
        PropertyNameType propName = bbox.addNewPropertyName();
        XmlCursor cursor = propName.newCursor();
        cursor.setTextValue((String) parameters.getParameterShellWithServiceSidedName(SOSmobileRequestBuilder.OGC_SPATIAL_OPS_FILTER_PROPERTY_NAME).getSpecifiedValue());
        cursor.dispose();

        EnvelopeType envelope = bbox.addNewEnvelope();
        envelope.setSrsName((String) parameters.getParameterShellWithServiceSidedName(SOSmobileRequestBuilder.OGC_SPATIAL_OPS_FILTER_SRS_NAME).getSpecifiedValue());
        envelope.addNewLowerCorner().setStringValue((String) parameters.getParameterShellWithServiceSidedName(SOSmobileRequestBuilder.OGC_SPATIAL_OPS_FILTER_BBOX_LOWER_CORNER).getSpecifiedValue());
        envelope.addNewUpperCorner().setStringValue((String) parameters.getParameterShellWithServiceSidedName(SOSmobileRequestBuilder.OGC_SPATIAL_OPS_FILTER_BBOX_UPPER_CORNER).getSpecifiedValue());
    }

}