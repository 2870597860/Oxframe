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

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import org.n52.oxf.OXFException;
import org.n52.oxf.owsCommon.capabilities.ITime;
import org.n52.oxf.serviceAdapters.ParameterContainer;
import org.n52.oxf.serviceAdapters.sos.ISOSRequestBuilder;
import org.n52.oxf.valueDomains.time.ITimePosition;
import org.n52.oxf.valueDomains.time.TimeFactory;

import com.topografix.gpx.x1.x0.GpxDocument.Gpx.Trk.Trkseg.Trkpt;

/**
 * @author <a href="mailto:daniel.nuest@uni-muenster.de">Daniel Nüst</a>
 * 
 */
@SuppressWarnings("static-access")
public class TestSOSmobileRequestBuilder {

    /**
     * @param args
     * @throws OXFException
     */
    public static void main(String[] args) {
        try {

            // foiTime();
            // foi();
            // register();
            // update();
            // insert();

            describe();

//            getDF();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unused")
    private static void getDF() throws OXFException {
        SOSmobileRequestBuilder requestBuilder = (SOSmobileRequestBuilder) SOSmobileRequestBuilderFactory.generateRequestBuilder("1.0.0");

        ParameterContainer parameters = generateDummyParameterContainerGetDomainFeature();
        String s = requestBuilder.buildGetDomainFeature(parameters);

        System.out.println("\n\n" + s);
    }

    private static ParameterContainer generateDummyParameterContainerGetDomainFeature() throws OXFException {
        ParameterContainer parameters = new ParameterContainer();
        parameters.addParameterShell(SOSmobileRequestBuilder.GET_DOMAIN_FEATURE_SERVICE_PARAMETER, "SOS");
        parameters.addParameterShell(SOSmobileRequestBuilder.GET_DOMAIN_FEATURE_VERSION_PARAMETER, "1.0.0");
        parameters.addParameterShell(SOSmobileRequestBuilder.GET_DOMAIN_FEATURE_ID, "eu-001");
        return parameters;
    }

    private static void describe() throws OXFException {
        SOSmobileRequestBuilder requestBuilder = (SOSmobileRequestBuilder) SOSmobileRequestBuilderFactory.generateRequestBuilder("1.0.0");

        ParameterContainer parameters = generateDummyParameterContainerDescribeSensor();
        String s = requestBuilder.buildDescribeSensorRequest(parameters);

        System.out.println("\n\n" + s);
    }

    @SuppressWarnings("unused")
    private static ParameterContainer generateDummyParameterContainerDescribeSensor() throws OXFException {
        ParameterContainer parameters = new ParameterContainer();
        parameters.addParameterShell(ISOSRequestBuilder.DESCRIBE_SENSOR_SERVICE_PARAMETER, "SOS");
        parameters.addParameterShell(ISOSRequestBuilder.DESCRIBE_SENSOR_VERSION_PARAMETER, "1.0.0");
        parameters.addParameterShell(ISOSRequestBuilder.DESCRIBE_SENSOR_PROCEDURE_PARAMETER,
                                     "urn:ogc:object:feature:Sensor:IFGI:ifgi-sensor-100");
        parameters.addParameterShell(ISOSRequestBuilder.DESCRIBE_SENSOR_OUTPUT_FORMAT,
                                     "text/xml;subtype=\"sensorML/1.0.1\"");

        ITime after1 = TimeFactory.createTime("2008-01-01");
        ITime after2 = TimeFactory.createTime("2008-02-02");
        ITime before = TimeFactory.createTime("2009-02-04");
        ITime equals = TimeFactory.createTime("2008-12-05");
        ITime period1 = TimeFactory.createTime("2006-11-01/2006-12-01");
        ITime period2 = TimeFactory.createTime("2004-11-01/2004-12-01");
        ITime period3 = TimeFactory.createTime("2005-11-01/2009-02-01");

        ITime[] afters = {after1, after2};
        parameters.addParameterShell(DescribeSensorTemporalOpsBuilder.TM_AFTER_PARAMETER, afters);
        parameters.addParameterShell(DescribeSensorTemporalOpsBuilder.TM_BEFORE_PARAMETER, before);
        ITime[] equalsArray = {equals, period2};
        parameters.addParameterShell(DescribeSensorTemporalOpsBuilder.TM_EQUALS_PARAMETER, equalsArray);
        parameters.addParameterShell(DescribeSensorTemporalOpsBuilder.TM_DURING_PARAMETER, period3);

        return parameters;
    }

    @SuppressWarnings("unused")
    private static void foi() throws OXFException {
        SOSmobileRequestBuilder requestBuilder = (SOSmobileRequestBuilder) SOSmobileRequestBuilderFactory.generateRequestBuilder("1.0.0");

        ParameterContainer parameters = generateDummyParameterContainerGetFoi();
        String s = requestBuilder.buildGetFeatureOfInterestRequest(parameters);

        System.out.println("\n\n" + s);
    }

    private static ParameterContainer generateDummyParameterContainerGetFoi() throws OXFException {
        ParameterContainer parameters = new ParameterContainer();
        parameters.addParameterShell(ISOSRequestBuilder.GET_FOI_SERVICE_PARAMETER, "SOS");
        parameters.addParameterShell(ISOSRequestBuilder.GET_FOI_VERSION_PARAMETER, "1.0.0");

        /** optional **/
        // switch between ids and location
        if (false) {
            // switch between one and multiple foi ids
            if (false) {
                parameters.addParameterShell(ISOSRequestBuilder.GET_FOI_ID_PARAMETER, "urn:ogc:object:feature:id-1");
            }
            else {
                String[] ids = {"urn:ogc:object:feature:id-1",
                                "urn:ogc:object:feature:id-42",
                                "urn:ogc:object:feature:id-17"};
                parameters.addParameterShell(ISOSRequestBuilder.GET_FOI_ID_PARAMETER, ids);
            }
        }
        else {
            parameters.addParameterShell(ISOSRequestBuilder.GET_FOI_LOCATION_PARAMETER, "has a location parameter");

            // BBOX
            parameters.addParameterShell(SOSmobileRequestBuilder.OGC_SPATIAL_OPS_FILTER_NAME,
                                         SOSmobileRequestBuilder.OGC_SPATIAL_OPS_FILTER_BBOX);
            parameters.addParameterShell(SOSmobileRequestBuilder.OGC_SPATIAL_OPS_FILTER_PROPERTY_NAME,
                                         "urn:ogc:data:location");
            parameters.addParameterShell(SOSmobileRequestBuilder.OGC_SPATIAL_OPS_FILTER_BBOX_UPPER_CORNER, "9.0 51.8");
            parameters.addParameterShell(SOSmobileRequestBuilder.OGC_SPATIAL_OPS_FILTER_BBOX_LOWER_CORNER, "8.6 51.2");
            parameters.addParameterShell(SOSmobileRequestBuilder.OGC_SPATIAL_OPS_FILTER_SRS_NAME,
                                         "urn:ogc:def:crs:EPSG:4326");
        }

        // TODO eventTime!

        return parameters;
    }

    @SuppressWarnings("unused")
    private static void foiTime() throws OXFException {
        SOSmobileRequestBuilder requestBuilder = (SOSmobileRequestBuilder) SOSmobileRequestBuilderFactory.generateRequestBuilder("1.0.0");

        ParameterContainer parameters = generateDummyParameterContainerGetFoiTime();
        String s = requestBuilder.buildGetFeatureOfInterestTimeRequest(parameters);

        System.out.println("\n\n" + s);
    }

    private static ParameterContainer generateDummyParameterContainerGetFoiTime() throws OXFException {

        String id = "muenster-1";

        ParameterContainer parameters = new ParameterContainer();
        parameters.addParameterShell(SOSmobileRequestBuilder.GET_FOI_TIME_SERVICE_PARAMETER, "SOS");
        parameters.addParameterShell(SOSmobileRequestBuilder.GET_FOI_TIME_VERSION_PARAMETER, "1.0.0");
        parameters.addParameterShell(SOSmobileRequestBuilder.GET_FOI_TIME_ID_PARAMETER, id);

        /** optional **/
        // switch between one and multiple observed properties
        if (false) {
            parameters.addParameterShell(SOSmobileRequestBuilder.GET_FOI_TIME_OBSERVED_PROPERTY_PARAMETER,
                                         "urn:ogc:def:phenomenon:temperature");
        }
        else {
            String[] observedProperties = {"urn:ogc:def:phenomenon:temperature",
                                           "urn:ogc:def:phenomenon:pressure",
                                           "urn:spacefleet:uss:enterprise:warpspeed"};
            parameters.addParameterShell(SOSmobileRequestBuilder.GET_FOI_TIME_OBSERVED_PROPERTY_PARAMETER,
                                         observedProperties);
        }

        // switch between one and multiple sensor ids
        if (false) {
            parameters.addParameterShell(SOSmobileRequestBuilder.GET_FOI_TIME_SENSOR_ID_PARAMETER,
                                         "urn:ogc:object:sensor:ifgi-sensor-mobile-1");
        }
        else {
            String[] ids = {"urn:ogc:object:sensor:ifgi-sensor-mobile-1",
                            "urn:ogc:object:sensor:ifgi-sensor-mobile-2",
                            "urn:ogc:object:sensor:ifgi-sensor-mobile-3"};
            parameters.addParameterShell(SOSmobileRequestBuilder.GET_FOI_TIME_SENSOR_ID_PARAMETER, ids);
        }

        // switch between id(s) and filter (BBOX) for domain feature
        if (false) {
            if (false) {
                parameters.addParameterShell(SOSmobileRequestBuilder.DOMAIN_FEATURE_ID, "muenster-1");
            }
            else {
                String[] ids = {"muenster-1", "uppsala-17", "moon-2"};
                parameters.addParameterShell(SOSmobileRequestBuilder.DOMAIN_FEATURE_ID, ids);
            }
        }
        else {
            parameters.addParameterShell(SOSmobileRequestBuilder.OGC_SPATIAL_OPS_FILTER_NAME,
                                         SOSmobileRequestBuilder.OGC_SPATIAL_OPS_FILTER_BBOX);
            parameters.addParameterShell(SOSmobileRequestBuilder.OGC_SPATIAL_OPS_FILTER_PROPERTY_NAME,
                                         "urn:ogc:data:location");
            parameters.addParameterShell(SOSmobileRequestBuilder.OGC_SPATIAL_OPS_FILTER_BBOX_UPPER_CORNER, "9.0 51.8");
            parameters.addParameterShell(SOSmobileRequestBuilder.OGC_SPATIAL_OPS_FILTER_BBOX_LOWER_CORNER, "8.6 51.2");
            parameters.addParameterShell(SOSmobileRequestBuilder.OGC_SPATIAL_OPS_FILTER_SRS_NAME,
                                         "urn:ogc:def:crs:EPSG:4326");
        }

        return parameters;
    }

    @SuppressWarnings("unused")
    private static void update() throws Exception {
        SOSmobileRequestBuilder requestBuilder = (SOSmobileRequestBuilder) SOSmobileRequestBuilderFactory.generateRequestBuilder("1.0.0");

        ParameterContainer parameters = generateDummyParameterContainerUpdateSensor();
        String s = requestBuilder.buildUpdateSensor(parameters);

        System.out.println("\n\n" + s);
    }

    private static ParameterContainer generateDummyParameterContainerUpdateSensor() throws OXFException {
        
        /**
        
        Date now = new Date();
        String time = TimeFactory.ISO8601LocalFormat.format(now);


        FeedProperties fP = new HamburgLissabonFeederProperties();
        fP.init();
        fP.setTimeString(time);
        UpdateMobile update = new UpdateMobile(fP);

        return update.buildParameterContainer();
        **/
        
        return null;
    }

    @SuppressWarnings("unused")
    private static void register() throws Exception {
        ISOSRequestBuilder requestBuilder = SOSmobileRequestBuilderFactory.generateRequestBuilder("1.0.0");

        ParameterContainer parameters = generateDummyParameterContainerRegisterSensor();
        String s = requestBuilder.buildRegisterSensor(parameters);

        System.out.println("\n\n" + s);
    }

    private static ParameterContainer generateDummyParameterContainerRegisterSensor() throws Exception {
        ParameterContainer[] inputList = new ParameterContainer[1];
        inputList[0] = new ParameterContainer();
        inputList[0].addParameterShell(SOSmobileRequestBuilder.REGISTER_SENSOR_INPUT_LIST_OBS_PROP_NAME,
                                       "atmosphericTemperature");
        inputList[0].addParameterShell(SOSmobileRequestBuilder.REGISTER_SENSOR_INPUT_LIST_OBS_PROP_DEFINITION,
                                       "urn:ogc:def:property:OGC::temperature");
        // inputList[1] = new Pair<String, String>("atmosphericPressure",
        // "urn:ogc:def:property:OGC::pressure");

        ParameterContainer[] outputList = new ParameterContainer[1];
        outputList[0] = new ParameterContainer();
        outputList[0].addParameterShell(SOSmobileRequestBuilder.REGISTER_SENSOR_OUTPUT_LIST_NAME, "temperature");
        outputList[0].addParameterShell(SOSmobileRequestBuilder.REGISTER_SENSOR_OUTPUT_LIST_QUANTITY_DEF,
                                        "urn:ogc:def:property:OGC::temperature");
        outputList[0].addParameterShell(SOSmobileRequestBuilder.REGISTER_SENSOR_OUTPUT_LIST_OFFERING_ID,
                                        "temperature_MS");
        outputList[0].addParameterShell(SOSmobileRequestBuilder.REGISTER_SENSOR_OUTPUT_LIST_OFFERING_NAME,
                                        "temperatures in Muenster");
        outputList[0].addParameterShell(SOSmobileRequestBuilder.REGISTER_SENSOR_OUTPUT_LIST_UOM_CODE, "Cel");

        ParameterContainer paramCon = new ParameterContainer();

        paramCon.addParameterShell(ISOSRequestBuilder.REGISTER_SENSOR_SERVICE_PARAMETER, "SOS");
        paramCon.addParameterShell(ISOSRequestBuilder.REGISTER_SENSOR_VERSION_PARAMETER, "1.0.0");
        paramCon.addParameterShell(ISOSRequestBuilder.REGISTER_SENSOR_ID_PARAMETER, "ifgi-sensor-mobile-1234");

        paramCon.addParameterShell(ISOSRequestBuilder.REGISTER_SENSOR_OBSERVED_PROPERTY_PARAMETER,
                                   "urn:ogc:def:phenomenon:OGC:1.0.30:temperature");

        paramCon.addParameterShell(SOSmobileRequestBuilder.POSITION_LATITUDE_PARAMETER, "51.941154417631516");
        paramCon.addParameterShell(SOSmobileRequestBuilder.POSITION_LONGITUDE_PARAMETER, "7.61026918888092");
        paramCon.addParameterShell(SOSmobileRequestBuilder.POSITION_NAME_PARAMETER, "M�nster");
        paramCon.addParameterShell(SOSmobileRequestBuilder.POSITION_REFERENCE_FRAME_PARAMETER,
                                   "urn:ogc:crs:EPSG:4326");
        paramCon.addParameterShell(SOSmobileRequestBuilder.POSITION_FIXED_PARAMETER,
                                   Boolean.toString(false));

        paramCon.addParameterShell(ISOSRequestBuilder.REGISTER_SENSOR_UOM_PARAMETER, "�Celsius");

        paramCon.addParameterShell(SOSmobileRequestBuilder.REGISTER_SENSOR_STATUS_PARAMETER,
                                   Boolean.toString(true));
        paramCon.addParameterShell(SOSmobileRequestBuilder.REGISTER_SENSOR_MOBILE_PARAMETER,
                                   Boolean.toString(true));

        paramCon.addParameterShell(SOSmobileRequestBuilder.REGISTER_SENSOR_INPUT_LIST, inputList);
        paramCon.addParameterShell(SOSmobileRequestBuilder.REGISTER_SENSOR_OUTPUT_LIST, outputList);

        paramCon.addParameterShell(SOSmobileRequestBuilder.DOMAIN_FEATURE_NAME, "M�nster");
        paramCon.addParameterShell(SOSmobileRequestBuilder.DOMAIN_FEATURE_ID, "muenster-1");
        paramCon.addParameterShell(SOSmobileRequestBuilder.DOMAIN_FEATURE_DESCRIPTION, "Roughly estimated area of the city of M�nster with the border along major roads");
        paramCon.addParameterShell(SOSmobileRequestBuilder.DOMAIN_FEATURE_LOCATION_LINEAR_RING_STRING,
                                   "7.581596374511719 51.987079081231315, "
                                   + "7.61077880859375 51.99151892470434, "
                                   + "7.623138427734375 51.99236455926818, 7.65987396240234 51.98750194243185, "
                                   + "7.663993835449219 51.96994984709194, 7.67120361328125 51.97143040982793, "
                                   + "7.672920227050781 51.96868075438375, 7.67292022705078, 51.96296939243455, "
                                   + "7.663307189941406 51.9538720171228, 7.646827697753906 51.94583093952526, "
                                   + "7.642364501953125 51.94392626259336, 7.636871337890625 51.94096327104416, "
                                   + "7.627944946289062 51.93863506895226, 7.614898681640625 51.93651841674403");
        paramCon.addParameterShell(SOSmobileRequestBuilder.DOMAIN_FEATURE_LOCATION_SRS_NAME, "4326");

        return paramCon;
    }

    @SuppressWarnings("unused")
    private static void insert() throws OXFException {
        ISOSRequestBuilder requestBuilder = SOSmobileRequestBuilderFactory.generateRequestBuilder("1.0.0");

        ParameterContainer parameters = generateDummyParameterContainerInsertObs();

        String s = requestBuilder.buildInsertObservation(parameters);

        System.out.println("\n\n" + s);
    }

    private static ParameterContainer generateDummyParameterContainerInsertObs() throws OXFException {

        double value = 42.17;
        ITimePosition period = (ITimePosition) TimeFactory.createTime("2008-11-01T10:00:00+01:00");
        Trkpt tp = Trkpt.Factory.newInstance();
        tp.setLat(new BigDecimal(52.0));
        tp.setLon(new BigDecimal(8.0));

        ParameterContainer paramCon = new ParameterContainer();

        paramCon.addParameterShell(ISOSRequestBuilder.INSERT_OBSERVATION_SERVICE_PARAMETER, "SOS");
        paramCon.addParameterShell(ISOSRequestBuilder.INSERT_OBSERVATION_VERSION_PARAMETER, "1.0.0");
        paramCon.addParameterShell(ISOSRequestBuilder.INSERT_OBSERVATION_OBSERVED_PROPERTY_PARAMETER,
                                   "temperature");
        paramCon.addParameterShell(ISOSRequestBuilder.INSERT_OBSERVATION_SENSOR_ID_PARAMETER, "urn:ogc:object:feature:Sensor:IFGI:ifgi-sensor-100");
        paramCon.addParameterShell(ISOSRequestBuilder.INSERT_OBSERVATION_SAMPLING_TIME,
                                   period.toISO8601Format());

        paramCon.addParameterShell(ISOSRequestBuilder.INSERT_OBSERVATION_VALUE_UOM_ATTRIBUTE,
                                   "degree celsius");
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.ENGLISH);
        DecimalFormat valueFormat = new DecimalFormat("#.000", symbols);
        String formattedValue = valueFormat.format(value);
        paramCon.addParameterShell(ISOSRequestBuilder.INSERT_OBSERVATION_VALUE_PARAMETER, formattedValue);

        // DOMAIN FEATURE (stays the same)
        paramCon.addParameterShell(SOSmobileRequestBuilder.DOMAIN_FEATURE_ID, "eu-001");
        paramCon.addParameterShell(SOSmobileRequestBuilder.DOMAIN_FEATURE_NAME, "Europe");
        paramCon.addParameterShell(SOSmobileRequestBuilder.DOMAIN_FEATURE_DESCRIPTION, "Europe and stuff");
        String dFLEUstartend = "25.83984375 71.26694980922422";
        String dFLEU = dFLEUstartend
                + ", 19.3359375 70.46032874320807, 12.3046875 68.29540429506305, 9.4921875 65.9034471904945"
                + ", 5.80078125 63.73585307238071, 1.23046875 62.21880354905434, -4.921875 59.92419185901303, -9.4921875 58.30025274211579"
                + ", -11.42578125 55.61807011936093, -13.18359375 52.203566364440995, -13.7109375 45.262514498373584"
                + ", -13.0078125 41.1654214723505, -10.72265625 36.51316734886143, -7.91015625 36.08817178048462"
                + ", -2.63671875 36.51316734886143, 1.93359375 38.18983996724313, 8.4375 38.74037299991194, 11.07421875 38.05155080658339"
                + ", 14.58984375 34.943586620477944, 20.91796875 34.79936932613168, 28.125 34.65489930099977, 32.6953125 34.65489930099977"
                + ", 35.68359375 36.37175953328168, 40.4296875 37.774187545982066, 44.47265625 37.91299989407177, 48.515625 39.558270399240385"
                + ", 49.39453125 44.13806655389196, 47.109375 49.88330863099783, 42.36328125 67.23125915059808, 39.0234375 69.62038929507884"
                + ", 34.1015625 71.37952989910096, 29.70703125 71.43557496264391, 27.0703125 71.37952989910096, 25.87726593017578 71.27043409783333"
                + ", " + dFLEUstartend;
        
        paramCon.addParameterShell(SOSmobileRequestBuilder.DOMAIN_FEATURE_LOCATION_LINEAR_RING_STRING,
                                   dFLEU);
        paramCon.addParameterShell(SOSmobileRequestBuilder.DOMAIN_FEATURE_LOCATION_SRS_NAME,
                                   "4326");

        // ADD (new) FOI
        paramCon.addParameterShell(ISOSRequestBuilder.INSERT_OBSERVATION_FOI_ID_PARAMETER, "foiPoint" + "_"
                + System.currentTimeMillis());

        paramCon.addParameterShell(ISOSRequestBuilder.INSERT_OBSERVATION_NEW_FOI_NAME, "eu-001" + "_"
                + "SamplingPoint" + " " + System.currentTimeMillis());
        paramCon.addParameterShell(ISOSRequestBuilder.INSERT_OBSERVATION_NEW_FOI_POSITION,
                                   tp.getLat().toPlainString() + " "
                                           + tp.getLon().toPlainString()); // "40.85 -74.0608");
        paramCon.addParameterShell(ISOSRequestBuilder.INSERT_OBSERVATION_NEW_FOI_POSITION_SRS,
                                   "urn:ogc:def:crs:EPSG:4326");
        paramCon.addParameterShell(ISOSRequestBuilder.INSERT_OBSERVATION_NEW_FOI_DESC, "description of the foi "
                + "SamplingPoint" + " " + System.currentTimeMillis());

        paramCon.addParameterShell(SOSmobileRequestBuilder.OGC_SPATIAL_OPS_FILTER_NAME,
                                     SOSmobileRequestBuilder.OGC_SPATIAL_OPS_FILTER_BBOX);
        paramCon.addParameterShell(SOSmobileRequestBuilder.OGC_SPATIAL_OPS_FILTER_PROPERTY_NAME,
                                     "urn:ogc:data:location");
        paramCon.addParameterShell(SOSmobileRequestBuilder.OGC_SPATIAL_OPS_FILTER_BBOX_UPPER_CORNER, "9.0 51.8");
        paramCon.addParameterShell(SOSmobileRequestBuilder.OGC_SPATIAL_OPS_FILTER_BBOX_LOWER_CORNER, "8.6 51.2");
        paramCon.addParameterShell(SOSmobileRequestBuilder.OGC_SPATIAL_OPS_FILTER_SRS_NAME,
                                     "urn:ogc:def:crs:EPSG:4326");

        return paramCon;
    }

}
