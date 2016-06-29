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

import org.apache.log4j.Logger;
import org.n52.oxf.OXFException;
import org.n52.oxf.feature.IFeatureStore;
import org.n52.oxf.feature.OXFFeature;
import org.n52.oxf.feature.OXFFeatureCollection;
import org.n52.oxf.feature.sos.SOSFoiStore;
import org.n52.oxf.feature.sos.SOSObservationStore;
import org.n52.oxf.owsCommon.ExceptionReport;
import org.n52.oxf.owsCommon.ServiceDescriptor;
import org.n52.oxf.owsCommon.capabilities.Operation;
import org.n52.oxf.render.IVisualization;
import org.n52.oxf.render.sos.SimpleOM2KMLRenderer;
import org.n52.oxf.serviceAdapters.OperationResult;
import org.n52.oxf.serviceAdapters.ParameterContainer;
import org.n52.oxf.serviceAdapters.sos.ISOSRequestBuilder;
import org.n52.oxf.serviceAdapters.sos.caps.ObservationOffering;
import org.n52.oxf.util.LoggingHandler;

/**
 * This class demonstrates how to use the SOSmobileAdapter.
 * 
 * @author <a href="mailto:daniel.nuest@uni-muenster.de">Daniel Nüst</a>
 * 
 */
public class TestSOSmobileAdapter {

    private static Logger LOGGER = LoggingHandler.getLogger(TestSOSmobileAdapter.class);

    // private final String url =
    // "http://localhost:8080/52nSOSv2_WeatherSA_artifical/sos";

    // private final String url = "http://140.114.79.14:8080/52nSOSv2/sos";

    private final String url = "http://mars.uni-muenster.de:8080/OWS5SOS/sos";

    private final String serviceVersion = "1.0.0";

    public static void main(String[] args) throws OXFException, ExceptionReport {
        new TestSOSmobileAdapter().testGetObservation();
    }

    public void testGetCapabilities() throws ExceptionReport, OXFException {

        SOSmobileAdapter adapter = new SOSmobileAdapter(serviceVersion);

        ParameterContainer paramCon = new ParameterContainer();
        paramCon.addParameterShell(ISOSRequestBuilder.GET_CAPABILITIES_ACCEPT_VERSIONS_PARAMETER, serviceVersion);
        paramCon.addParameterShell(ISOSRequestBuilder.GET_CAPABILITIES_SERVICE_PARAMETER, SOSmobileAdapter.SERVICE_TYPE);

        OperationResult opResult = adapter.doOperation(new Operation("GetCapabilities", url + "?", url), paramCon);

        ServiceDescriptor desc = adapter.initService(opResult);

        LOGGER.info(desc.getServiceIdentification().getTitle());

        ObservationOffering obsOff = (ObservationOffering) desc.getContents().getDataIdentification(0);

        LOGGER.info("Offering: " + obsOff.getIdentifier());

    }

    /**
     * This method shows you how you may use the SOSAdapter in your application. It shows how to connect to
     * and access the SOS and to unmarshall the returned O&M data into OXFFeature objects:
     * 
     * <pre>
     * SOSAdapter sosAdapter = new SOSAdapter();
     * 
     * Operation op = new Operation(&quot;GetObservation&quot;, &quot;get_not_used&quot;, url);
     * 
     * // put all parameters you want to use into a ParameterContainer:
     * ParameterContainer paramCon = new ParameterContainer();
     * 
     * paramCon.addParameterShell(SOSRequestBuilder_000.GET_OBSERVATION_SERVICE_PARAMETER, SOSAdapter.SERVICE_TYPE);
     * paramCon.addParameterShell(SOSRequestBuilder_000.GET_OBSERVATION_VERSION_PARAMETER, SOSAdapter.SUPPORTED_VERSIONS[0]);
     * paramCon.addParameterShell(SOSRequestBuilder_000.GET_OBSERVATION_OFFERING_PARAMETER, &quot;WeatherSA&quot;);
     * paramCon.addParameterShell(SOSRequestBuilder_000.GET_OBSERVATION_RESPONSE_FORMAT_PARAMETER,
     *                            &quot;text/xml;subtype=\&quot;om/0.0.0\&quot;&quot;);
     * paramCon.addParameterShell(SOSRequestBuilder_000.GET_OBSERVATION_OBSERVED_PROPERTY_PARAMETER,
     *                            &quot;urn:ogc:def:phenomenon:OGC:1.0.30:dryBulbTemp&quot;);
     * paramCon.addParameterShell(SOSRequestBuilder_000.GET_OBSERVATION_RESULT_MODEL_PARAMETER, &quot;Observation&quot;); // &quot;Measurement&quot;);
     * 
     * ITime eventTime = TimeFactory.createTime(&quot;2007-04-10T01:45:00+02/2007-05-10T17:45:00+02&quot;);
     * paramCon.addParameterShell(new ParameterShell(new Parameter(SOSRequestBuilder_000.GET_OBSERVATION_EVENT_TIME_PARAMETER,
     *                                                             false,
     *                                                             new TemporalValueDomain(eventTime),
     *                                                             Parameter.COMMON_NAME_TIME), eventTime));
     * 
     * paramCon.addParameterShell(SOSRequestBuilder_000.GET_OBSERVATION_RESPONSE_MODE_PARAMETER, &quot;inline&quot;);
     * 
     * LOGGER.info(sosAdapter.getSOSRequestBuilder().buildGetObservationRequest(paramCon));
     * 
     * // now use this ParameterContainer as an input for the 'doOperation' method of your SOSAdapter. What
     * // you receive is an OperationResult.
     * OperationResult opResult = sosAdapter.doOperation(op, paramCon);
     * 
     * IFeatureStore featureStore = new SOSObservationStore();
     * 
     * // The OperationResult can be used as an input for the 'unmarshalFeatures' operation of your
     * // SOSObservationStore to parse the returned O&amp;M document and to build up OXFFeature objects.
     * OXFFeatureCollection featureCollection = featureStore.unmarshalFeatures(opResult);
     * 
     * LOGGER.info(&quot;featureCollection.size:&quot; + featureCollection.size());
     * 
     * // and now do something with these feature objects ...
     * 
     * LOGGER.info(&quot;geom of '0'.foi: &quot;
     *         + ((OXFFeature) featureCollection.toList().get(0).getAttribute(&quot;featureOfInterest&quot;)).getGeometry());
     * 
     * </pre>
     * 
     */
    public void testGetObservation() throws OXFException, ExceptionReport {
        SOSmobileAdapter SOSmobileAdapter = new SOSmobileAdapter(serviceVersion);

        Operation op = new Operation("GetObservation", "http://GET_URL_not_used", url);

        // put all parameters you want to use into a ParameterContainer:
        ParameterContainer paramCon = new ParameterContainer();

        paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_SERVICE_PARAMETER, SOSmobileAdapter.SERVICE_TYPE);
        paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_VERSION_PARAMETER,
                                   SOSmobileAdapter.SUPPORTED_VERSIONS[1]);
        paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_OFFERING_PARAMETER, "WeatherMS");
        paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_RESPONSE_FORMAT_PARAMETER,
                                   "text/xml;subtype=\"om/1.0.0\"");
        paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_OBSERVED_PROPERTY_PARAMETER,
                                   "urn:ogc:def:phenomenon:OGC:1.0.30:temperature");
        paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_RESULT_MODEL_PARAMETER, "Observation"); // "Measurement");

        paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_EVENT_TIME_PARAMETER,
                                   "2007-08-29T01:00:00+02/2007-08-31T01:00:00+02");

        paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_RESPONSE_MODE_PARAMETER, "inline");

        LOGGER.info(SOSmobileRequestBuilderFactory.generateRequestBuilder(serviceVersion).buildGetObservationRequest(paramCon));

        // now use this ParameterContainer as an input for the 'doOperation'
        // method of your SOSmobileAdapter. What
        // you receive is an OperationResult.
        OperationResult opResult = SOSmobileAdapter.doOperation(op, paramCon);

        LOGGER.info("Received data: " + new String(opResult.getIncomingResult()));

        IFeatureStore featureStore = new SOSObservationStore();

        // The OperationResult can be used as an input for the
        // 'unmarshalFeatures' operation of your
        // SOSObservationStore to parse the returned O&M document and to build
        // up OXFFeature objects.
        OXFFeatureCollection featureCollection = featureStore.unmarshalFeatures(opResult);

        LOGGER.info("featureCollection.size:" + featureCollection.size());

        // and now do something with these feature objects ...

        LOGGER.info("geom of '0'.foi: "
                + ((OXFFeature) featureCollection.toList().get(0).getAttribute("featureOfInterest")).getGeometry());

        SimpleOM2KMLRenderer renderer = new SimpleOM2KMLRenderer();
        IVisualization vis = renderer.renderLayer(featureCollection,
                                                  paramCon,
                                                  0,
                                                  0,
                                                  null,
                                                  testGetFeatureOfInterest().toSet());
        System.out.println(vis.getRendering());
    }

    public OXFFeatureCollection testGetFeatureOfInterest() throws OXFException, ExceptionReport {
        SOSmobileAdapter SOSmobileAdapter = new SOSmobileAdapter(serviceVersion);

        Operation op = new Operation("GetFeatureOfInterest", "http://GET_URL_not_used", url);

        ParameterContainer paramCon = new ParameterContainer();

        paramCon.addParameterShell(ISOSRequestBuilder.GET_FOI_SERVICE_PARAMETER, SOSmobileAdapter.SERVICE_TYPE);
        paramCon.addParameterShell(ISOSRequestBuilder.GET_FOI_VERSION_PARAMETER, SOSmobileAdapter.SUPPORTED_VERSIONS[1]);
        paramCon.addParameterShell(ISOSRequestBuilder.GET_FOI_ID_PARAMETER, new String[] {"FOI_JFK", "FOI_LGA"});

        LOGGER.info(SOSmobileRequestBuilderFactory.generateRequestBuilder(serviceVersion).buildGetFeatureOfInterestRequest(paramCon));

        OperationResult opResult = SOSmobileAdapter.doOperation(op, paramCon);

        SOSFoiStore featureStore = new SOSFoiStore();
        OXFFeatureCollection featureCollection = featureStore.unmarshalFeatures(opResult);

        LOGGER.info("featureCollection.size:" + featureCollection.size());

        // and now do something with those features ...

        return featureCollection;
    }

    public void testInsertObservation() throws OXFException, ExceptionReport {
        SOSmobileAdapter SOSmobileAdapter = new SOSmobileAdapter(serviceVersion);

        Operation op = new Operation("InsertObservation", "http://GET_URL_not_used", url);

        // put all parameters you want to use into a ParameterContainer:
        ParameterContainer paramCon = new ParameterContainer();

        paramCon.addParameterShell(ISOSRequestBuilder.INSERT_OBSERVATION_SERVICE_PARAMETER, "SOS");
        paramCon.addParameterShell(ISOSRequestBuilder.INSERT_OBSERVATION_VERSION_PARAMETER, serviceVersion);
        paramCon.addParameterShell(ISOSRequestBuilder.INSERT_OBSERVATION_OBSERVED_PROPERTY_PARAMETER,
                                   "urn:ogc:def:phenomenon:OGC:1.0.30:waterspeed");
        paramCon.addParameterShell(ISOSRequestBuilder.INSERT_OBSERVATION_FOI_ID_PARAMETER, "id_2001");
        paramCon.addParameterShell(ISOSRequestBuilder.INSERT_OBSERVATION_SENSOR_ID_PARAMETER,
                                   "urn:ogc:object:feature:Sensor:IFGI:ifgi-sensor-2");
        paramCon.addParameterShell(ISOSRequestBuilder.INSERT_OBSERVATION_SAMPLING_TIME, "2008-02-15T15:13:13Z");
        paramCon.addParameterShell(ISOSRequestBuilder.INSERT_OBSERVATION_VALUE_PARAMETER, "23");

        // Test with new FOI
        paramCon.addParameterShell(ISOSRequestBuilder.INSERT_OBSERVATION_NEW_FOI_NAME, "Airport Berlin");
        paramCon.addParameterShell(ISOSRequestBuilder.INSERT_OBSERVATION_NEW_FOI_POSITION, "40.85 -74.0608");
        paramCon.addParameterShell(ISOSRequestBuilder.INSERT_OBSERVATION_NEW_FOI_DESC, "The Airport in Berlin Germany");

        LOGGER.info(SOSmobileRequestBuilderFactory.generateRequestBuilder(serviceVersion).buildInsertObservation(paramCon));

        // now use this ParameterContainer as an input for the 'doOperation'
        // method of your SOSmobileAdapter. What
        // you receive is an OperationResult.
        // OperationResult opResult = SOSmobileAdapter.doOperation(op,
        // paramCon);

    }

    public void testRegisterSensor() throws OXFException, ExceptionReport {
        SOSmobileAdapter SOSmobileAdapter = new SOSmobileAdapter(serviceVersion);

        Operation op = new Operation("RegisterSensor", "http://GET_URL_not_used", url);

        // put all parameters you want to use into a ParameterContainer:
        ParameterContainer paramCon = new ParameterContainer();

        paramCon.addParameterShell(ISOSRequestBuilder.REGISTER_SENSOR_SERVICE_PARAMETER, "SOS");
        paramCon.addParameterShell(ISOSRequestBuilder.REGISTER_SENSOR_VERSION_PARAMETER, serviceVersion);
        paramCon.addParameterShell(ISOSRequestBuilder.REGISTER_SENSOR_ID_PARAMETER, "ifgi-sensor-5");
        paramCon.addParameterShell(ISOSRequestBuilder.REGISTER_SENSOR_LATITUDE_POSITION_PARAMETER, "52.00");
        paramCon.addParameterShell(ISOSRequestBuilder.REGISTER_SENSOR_LONGITUDE_POSITION_PARAMETER, "7.97");
        paramCon.addParameterShell(ISOSRequestBuilder.REGISTER_SENSOR_OBSERVED_PROPERTY_PARAMETER,
                                   "urn:ogc:def:phenomenon:OGC:1.0.30:waterlevel");
        paramCon.addParameterShell(ISOSRequestBuilder.REGISTER_SENSOR_POSITION_FIXED_PARAMETER, "true");
        paramCon.addParameterShell(ISOSRequestBuilder.REGISTER_SENSOR_POSITION_NAME_PARAMETER, "TestArea_5200_0797");
        paramCon.addParameterShell(ISOSRequestBuilder.REGISTER_SENSOR_UOM_PARAMETER, "mm");

        LOGGER.info(SOSmobileRequestBuilderFactory.generateRequestBuilder(serviceVersion).buildRegisterSensor(paramCon));

    }
}