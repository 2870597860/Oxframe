/**********************************************************************************
 Copyright (C) 2010
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
 
 21.02.2011
 *********************************************************************************/
package org.n52.oxf.rest.sos;

import java.util.logging.Logger;

import org.n52.oxf.OXFException;
import org.n52.oxf.feature.IFeatureStore;
import org.n52.oxf.feature.OXFFeatureCollection;
import org.n52.oxf.feature.sos.SOSObservationStore;
import org.n52.oxf.owsCommon.ExceptionReport;
import org.n52.oxf.owsCommon.capabilities.Operation;
import org.n52.oxf.serviceAdapters.OperationResult;
import org.n52.oxf.serviceAdapters.ParameterContainer;
import org.n52.oxf.serviceAdapters.sos.ISOSRequestBuilder;
import org.n52.oxf.serviceAdapters.sos.SOSAdapter;
import org.n52.util.Log;

/**
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 *
 */
public class TestObservationMerger {


    private static Logger LOGGER = Log.setUpLogger(TestObservationMerger.class);
    
    private static final String url = "http://v-swsl.uni-muenster.de:8080/52nSOS_AirBase_LinkedData/sos";
	
	private static final String serviceVersion = "1.0.0";
	
	public static void main(String[] args) throws OXFException, ExceptionReport {
		OXFFeatureCollection coll_1 = getObservations("AMMONIA_(AIR)", "http://giv-genesis.uni-muenster.de:8080/SOR/REST/phenomenon/OGC/Concentration[NH3]");
		OXFFeatureCollection coll_2 = getObservations("CARBON_MONOXIDE_(AIR)", "http://giv-genesis.uni-muenster.de:8080/SOR/REST/phenomenon/OGC/Concentration[CO]");
		OXFFeatureCollection coll_3 = getObservations("HYDROGEN_SULPHIDE_(AIR)", "http://giv-genesis.uni-muenster.de:8080/SOR/REST/phenomenon/OGC/Concentration[H2S]");
		
		OXFFeatureCollection allObs = ObservationMerger.mergeObservations(new OXFFeatureCollection[]{coll_1, coll_2, coll_3});
		
		String s = RDFEncoder.getObsColTriples(allObs, "http://here_goes_the_called_URL");
		
		LOGGER.info(s);
	}

	private static OXFFeatureCollection getObservations(String offering, String observedProperty) throws OXFException, ExceptionReport{
		SOSAdapter sosAdapter = new SOSAdapter(serviceVersion);

		Operation op = new Operation("GetObservation", "http://GET_URL_not_used", url);

		// put all parameters you want to use into a ParameterContainer:
		ParameterContainer paramCon = new ParameterContainer();

        paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_SERVICE_PARAMETER, SOSAdapter.SERVICE_TYPE);
        paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_VERSION_PARAMETER,
                                   SOSAdapter.SUPPORTED_VERSIONS[1]);
        paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_OFFERING_PARAMETER, offering);
        paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_RESPONSE_FORMAT_PARAMETER,
                                   "text/xml;subtype=\"om/1.0.0\"");
        paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_OBSERVED_PROPERTY_PARAMETER,
                                   observedProperty);
        paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_RESULT_MODEL_PARAMETER, "Measurement"); // "Measurement");

        paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_EVENT_TIME_PARAMETER, "2008-11-13T09:54:19+0200/2011-11-15T09:54:19+0200");

        paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_RESPONSE_MODE_PARAMETER, "inline");

        // now use this ParameterContainer as an input for the 'doOperation' method of your SOSAdapter. What
        // you receive is an OperationResult.
        OperationResult opResult = sosAdapter.doOperation(op, paramCon);

        IFeatureStore featureStore = new SOSObservationStore();

        return featureStore.unmarshalFeatures(opResult);
	}
}
