package org.n52.oxf.rest.sos;

import java.util.Iterator;
import java.util.logging.Logger;

import org.n52.oxf.OXFException;
import org.n52.oxf.feature.IFeatureStore;
import org.n52.oxf.feature.OXFFeature;
import org.n52.oxf.feature.OXFFeatureCollection;
import org.n52.oxf.feature.sos.SOSFoiStore;
import org.n52.oxf.feature.sos.SOSObservationStore;
import org.n52.oxf.owsCommon.ExceptionReport;
import org.n52.oxf.owsCommon.capabilities.Operation;
import org.n52.oxf.serviceAdapters.OperationResult;
import org.n52.oxf.serviceAdapters.ParameterContainer;
import org.n52.oxf.serviceAdapters.sos.ISOSRequestBuilder;
import org.n52.oxf.serviceAdapters.sos.SOSAdapter;
import org.n52.oxf.serviceAdapters.sos.SOSRequestBuilderFactory;
import org.n52.util.Log;

public class TestRDFEncoding {

    private static Logger LOGGER = Log.setUpLogger(TestRDFEncoding.class);
    
    private final String url = "http://v-swsl.uni-muenster.de:8080/52nSOS_AirBase_LinkedData/sos";
	
	private final String serviceVersion = "1.0.0";

	public static void main(String[] args) throws OXFException, ExceptionReport {
		//new TestRDFEncoding().testGetObservation();
		//new TestRDFEncoding().testEncodeFeatureOfInterest();
		new TestRDFEncoding().testEncodeObservation();
	}

	public void testEncodeObservationCollections() throws OXFException, ExceptionReport {
		SOSAdapter sosAdapter = new SOSAdapter(serviceVersion);

		Operation op = new Operation("GetObservation", "http://GET_URL_not_used", url);

		// put all parameters you want to use into a ParameterContainer:
		ParameterContainer paramCon = new ParameterContainer();

        paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_SERVICE_PARAMETER, SOSAdapter.SERVICE_TYPE);
        paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_VERSION_PARAMETER,
                                   SOSAdapter.SUPPORTED_VERSIONS[1]);
        paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_OFFERING_PARAMETER, "AMMONIA_(AIR)");
        paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_RESPONSE_FORMAT_PARAMETER,
                                   "text/xml;subtype=\"om/1.0.0\"");
        paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_OBSERVED_PROPERTY_PARAMETER,
                                   "http://giv-genesis.uni-muenster.de:8080/SOR/REST/phenomenon/OGC/Concentration[NH3]");
        paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_RESULT_MODEL_PARAMETER, "Measurement"); // "Measurement");

        paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_EVENT_TIME_PARAMETER, "2007-11-13T09:54:19+0200/2011-11-15T09:54:19+0200");

        paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_RESPONSE_MODE_PARAMETER, "inline");

		LOGGER.info("Sent request: " + SOSRequestBuilderFactory.generateRequestBuilder(serviceVersion).buildGetObservationRequest(paramCon));

        // now use this ParameterContainer as an input for the 'doOperation' method of your SOSAdapter. What
        // you receive is an OperationResult.
        OperationResult opResult = sosAdapter.doOperation(op, paramCon);

        LOGGER.info("Received response: " + new String(opResult.getIncomingResult()));

        IFeatureStore featureStore = new SOSObservationStore();

        OXFFeatureCollection observationColl = featureStore.unmarshalFeatures(opResult);
        
        LOGGER.info("Count of received observations: " + observationColl.size());
        		
        String s = new RDFEncoder().getObsColTriples(observationColl, "http://myServer/");
        
        LOGGER.info(s);
    }
	
	public void testEncodeObservation() throws OXFException, ExceptionReport {
		SOSAdapter sosAdapter = new SOSAdapter(serviceVersion);

		Operation op = new Operation(SOSAdapter.GET_OBSERVATION_BY_ID, "http://GET_URL_not_used", url);

		// put all parameters you want to use into a ParameterContainer:
		ParameterContainer paramCon = new ParameterContainer();

        paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_BY_ID_SERVICE_PARAMETER, SOSAdapter.SERVICE_TYPE);
        paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_BY_ID_VERSION_PARAMETER,SOSAdapter.SUPPORTED_VERSIONS[1]);
        paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_BY_ID_RESPONSE_FORMAT_PARAMETER,"text/xml;subtype=\"om/1.0.0\"");
        paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_BY_ID_RESULT_MODEL_PARAMETER, "Measurement");
        paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_BY_ID_OBSERVATION_ID_PARAMETER, "o_6160");

        OperationResult opResult = sosAdapter.doOperation(op, paramCon);

		SOSObservationStore obsStore = new SOSObservationStore();
		
		OXFFeatureCollection observationColl = obsStore.unmarshalFeatures(opResult);
		
		LOGGER.info("Count of received observations: " + observationColl.size());
		
        String s = new RDFEncoder().getObsTriple(observationColl.toList().get(0), "http://myServer/");
        
        LOGGER.info(s);
	}

	public void testEncodeFeatureOfInterest() throws OXFException, ExceptionReport {
		SOSAdapter sosAdapter = new SOSAdapter(serviceVersion);

		Operation op = new Operation("GetFeatureOfInterest", "http://GET_URL_not_used", url);

		ParameterContainer paramCon = new ParameterContainer();

		paramCon.addParameterShell(ISOSRequestBuilder.GET_FOI_SERVICE_PARAMETER, SOSAdapter.SERVICE_TYPE);
		paramCon.addParameterShell(ISOSRequestBuilder.GET_FOI_VERSION_PARAMETER, SOSAdapter.SUPPORTED_VERSIONS[1]);
		paramCon.addParameterShell(ISOSRequestBuilder.GET_FOI_ID_PARAMETER, new String[] {"http://v-swe.uni-muenster.de:8080/52nRESTfulSOS/RESTful/sos/AirBase_SOS/fois/HR0002A"});

		OperationResult opResult = sosAdapter.doOperation(op, paramCon);

		SOSFoiStore featureStore = new SOSFoiStore();
		OXFFeatureCollection featureCollection = featureStore.unmarshalFeatures(opResult);

		LOGGER.info("featureCollection.size:" + featureCollection.size());
		for (Iterator iterator = featureCollection.iterator(); iterator.hasNext();) {
			OXFFeature feature = (OXFFeature) iterator.next();
			LOGGER.info("feature ID: " + feature.toString());
			LOGGER.info("feature geometry: " + feature.getGeometry());
		}
		
		String s = new RDFEncoder().getFeatureTriple("http://myServer/", "HR0002A");
        
        LOGGER.info(s);
	}

		
}
