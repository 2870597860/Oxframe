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
 
 Created on: 07.01.2008
 *********************************************************************************/

package org.n52.oxf.serviceAdapters.sos;

import org.n52.oxf.OXFException;
import org.n52.oxf.serviceAdapters.ParameterContainer;

/**
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public interface ISOSRequestBuilder {

    public static String GET_CAPABILITIES_UPDATE_SEQUENCE_PARAMETER = "updateSequence";
    public static String GET_CAPABILITIES_ACCEPT_VERSIONS_PARAMETER = "AcceptVersions";
    public static String GET_CAPABILITIES_SECTIONS_PARAMETER = "sections";
    public static String GET_CAPABILITIES_SERVICE_PARAMETER = "service";

    public static String GET_OBSERVATION_SERVICE_PARAMETER = "service";
    public static String GET_OBSERVATION_VERSION_PARAMETER = "version";
    public static String GET_OBSERVATION_OFFERING_PARAMETER = "offering";
    public static String GET_OBSERVATION_OBSERVED_PROPERTY_PARAMETER = "observedProperty";
    public static String GET_OBSERVATION_RESPONSE_FORMAT_PARAMETER = "responseFormat";
    public static String GET_OBSERVATION_EVENT_TIME_PARAMETER = "eventTime";
    public static String GET_OBSERVATION_PROCEDURE_PARAMETER = "procedure";
    public static String GET_OBSERVATION_FEATURE_OF_INTEREST_PARAMETER = "featureOfInterest";
    public static String GET_OBSERVATION_RESULT_PARAMETER = "result";
    public static String GET_OBSERVATION_RESULT_MODEL_PARAMETER = "resultModel";
    public static String GET_OBSERVATION_RESPONSE_MODE_PARAMETER = "responseMode";

    public static String DESCRIBE_SENSOR_SERVICE_PARAMETER = "service";
    public static String DESCRIBE_SENSOR_VERSION_PARAMETER = "version";
    public static String DESCRIBE_SENSOR_PROCEDURE_PARAMETER = "procedure";
    public static String DESCRIBE_SENSOR_OUTPUT_FORMAT = "outputFormat";

    public static String GET_FOI_SERVICE_PARAMETER = "service";
    public static String GET_FOI_VERSION_PARAMETER = "version";
    public static String GET_FOI_EVENT_TIME_PARAMETER = "eventTime";
    public static String GET_FOI_ID_PARAMETER = "featureOfInterestId";
    public static String GET_FOI_LOCATION_PARAMETER = "location";

    public static String INSERT_OBSERVATION_SERVICE_PARAMETER = "service";  //已插入
    public static String INSERT_OBSERVATION_VERSION_PARAMETER = "version";  //已插入
    public static String INSERT_OBSERVATION_FOI_ID_PARAMETER = "featureOfInterestID"; //已插入
    public static String INSERT_OBSERVATION_NEW_FOI_ID_PARAMETER = "newFoiID";
    public static String INSERT_OBSERVATION_NEW_FOI_NAME = "newFoiName";
    public static String INSERT_OBSERVATION_NEW_FOI_DESC = "newFoiDesc";
    public static String INSERT_OBSERVATION_NEW_FOI_POSITION = "newFoiPosition";
    public static String INSERT_OBSERVATION_OBSERVED_PROPERTY_PARAMETER = "observedProperty";
    public static String INSERT_OBSERVATION_SAMPLING_POSITION = "samplingPointPosition"; //添加的
    public static String INSERT_OBSERVATION_SAMPLING_TIME = "samplingTime";
    public static String INSERT_OBSERVATION_VALUE_PARAMETER = "value";
	public static String INSERT_OBSERVATION_POSITION_SRS = "srsPosition";
	public static String INSERT_OBSERVATION_CATEGORY_OBSERVATION_RESULT_CODESPACE = "catObsCodespace";
	public static String INSERT_OBSERVATION_TYPE_CATEGORY = "category";
	public static String INSERT_OBSERVATION_TYPE_MEASUREMENT = "measurement";
	public static String INSERT_OBSERVATION_TYPE = "type";
    public static String INSERT_OBSERVATION_VALUE_UOM_ATTRIBUTE = "resultUom";
    public static String INSERT_OBSERVATION_VALUE_ATTRIBUTE = "result";//添加的
    public static String INSERT_OBSERVATION_NEW_FOI_POSITION_SRS = "insertObSRS";
    public static String INSERT_OBSERVATION_PROCEDURE_PARAMETER = "procedure";  //已插入
	public static String INSERT_OBSERVATION_SENSOR_ID_PARAMETER = "sensorid";
    
    public static String REGISTER_SENSOR_SERVICE_PARAMETER = "service";  //已插入
    public static String REGISTER_SENSOR_VERSION_PARAMETER = "version";  //已插入
    public static String REGISTER_SENSOR_ML_DOC_PARAMETER = "sensorMLDoc";
    public static String REGISTER_SENSOR_OBSERVATION_TEMPLATE = "observationTemplate";
    public static String REGISTER_SENSOR_OBSERVATION_TYPE = "type"; 
    public static String REGISTER_SENSOR_STATUS = "status"; //添加的 capabilities
   
    public static String REGISTER_SENSOR_IDENTIFICATION_TYPE = "Identification"; //添加的
	public static String REGISTER_SENSOR_OBSERVATION_TYPE_CATEGORY = "category";  
	public static String REGISTER_SENSOR_OBSERVATION_TYPE_MEASUREMENT = "measurement";
    public static String REGISTER_SENSOR_ID_PARAMETER = "id";                //已插入
    public static String REGISTER_SENSOR_OBSERVED_PROPERTY_PARAMETER = "observedProperty";  //已插入
    public static String REGISTER_SENSOR_LATITUDE_POSITION_PARAMETER = "latitude";  //已插入
    public static String REGISTER_SENSOR_LONGITUDE_POSITION_PARAMETER = "longitude"; //已插入
    public static String REGISTER_SENSOR_POSITION_NAME_PARAMETER = "positionName"; //已插入
    public static String REGISTER_SENSOR_POSITION_FIXED_PARAMETER = "fixed"; //已插入  
    public static String REGISTER_SENSOR_UOM_PARAMETER = "uom"; //已插入
    public static String REGISTER_SENSOR_LATITUDE_UOM_PARAMETER = "latitudeuom"; //已插入纬度单位
    public static String REGISTER_SENSOR_LONGTITUDE_UOM_PARAMETER = "longtitudeuom"; //已插入经度单位

    public static String GET_OBSERVATION_BY_ID_SERVICE_PARAMETER = "service";
    public static String GET_OBSERVATION_BY_ID_VERSION_PARAMETER = "version";
    public static String GET_OBSERVATION_BY_ID_OBSERVATION_ID_PARAMETER = "ObservationID";
    public static String GET_OBSERVATION_BY_ID_RESPONSE_FORMAT_PARAMETER = "responseFormat";
    public static String GET_OBSERVATION_BY_ID_RESPONSE_MODE_PARAMETER = "responseMode";
    public static String GET_OBSERVATION_BY_ID_RESULT_MODEL_PARAMETER = "resultModel";
	 
    public String buildGetCapabilitiesRequest(ParameterContainer parameters);

    public String buildGetObservationRequest(ParameterContainer parameters) throws OXFException;

    public String buildGetObservationByIDRequest(ParameterContainer parameters) throws OXFException;

    public String buildDescribeSensorRequest(ParameterContainer parameters);

    public String buildGetFeatureOfInterestRequest(ParameterContainer parameters);

    public String buildInsertObservation(ParameterContainer parameters);

    public String buildRegisterSensor(ParameterContainer parameters) throws OXFException;

}