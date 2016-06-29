/**********************************************************************************
 Copyright (C) 2007 by 52 North Initiative for Geospatial Open Source Software GmbH

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
 
 Created on: 07.01.2006
 *********************************************************************************/
package org.n52.oxf.rest.sos;

import java.util.List;

import org.n52.oxf.feature.OXFFeature;
import org.n52.oxf.feature.OXFFeatureCollection;
import org.n52.oxf.feature.OXFMeasurementType;
import org.n52.oxf.feature.dataTypes.OXFMeasureType;
import org.n52.oxf.feature.dataTypes.OXFPhenomenonPropertyType;
import org.n52.oxf.owsCommon.capabilities.ITime;

/**
 * Preliminary encoder hack for RDF.
 * 
 * @author staschc, Elsaesser, Broering
 * 
 */
public class RDFEncoder {
	
	public static final String RDF_ROOT_START="<?xml version=\"1.0\"?><rdf:RDF xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\" xmlns:rdfs=\"http://www.w3.org/2000/01/rdf-schema#\" xmlns:minioamld=\"http://v-swe.uni-muenster.de:8080/52nRESTfulSOS/RESTful/minionmld/\" xmlns:dct=\"http://purl.org/dc/terms/\">";
	public static final String RDF_ROOT_END="</rdf:RDF>";
	
	
	// RDF types:
	private static final String RDF_FOI_TYPE = "http://v-swe.uni-muenster.de:8080/52nRESTfulSOS/miniOnM.owl#FeatureOfInterest";
	private static final String RDF_OBS_TYPE = "http://v-swe.uni-muenster.de:8080/52nRESTfulSOS/miniOnM.owl#Observation";
	private static final String RDF_OBSCOL_TYPE = "http://v-swe.uni-muenster.de:8080/52nRESTfulSOS/miniOnM.owl#ObservationCollection";
	private static final String RDF_OBSPROP_TYPE = "http://v-swe.uni-muenster.de:8080/52nRESTfulSOS/miniOnM.owl#ObservedProperty";
	private static final String RDF_RESULT_TYPE = "http://v-swe.uni-muenster.de:8080/52nRESTfulSOS/miniOnM.owl#Result";
	private static final String RDF_SAMPLINGTIME_TYPE = "http://v-swe.uni-muenster.de:8080/52nRESTfulSOS/miniOnM.owl#SamplingTime";
	private static final String RDF_SENSOR_TYPE = "http://v-swe.uni-muenster.de:8080/52nRESTfulSOS/miniOnM.owl#Sensor";
	
	// URLs [hard-coded :-(
	private static final String OBSERVATION_BY_ID_PREFIX = "http://v-swe.uni-muenster.de:8080/52nRESTfulSOS/RESTful/sos/AirBase_SOS/observations/ids/";
	private static final String SENSOR_OBSERVATIONS_PREFIX = "http://v-swe.uni-muenster.de:8080/52nRESTfulSOS/RESTful/sos/AirBase_SOS/observations/procedures/";
	private static final String FEATURE_OBSERVATIONS_PREFIX = "http://v-swe.uni-muenster.de:8080/52nRESTfulSOS/RESTful/sos/AirBase_SOS/observations/featuresOfInterest/";
	private static final String RESULT_URL_PREFIX = "http://v-swe.uni-muenster.de:8080/52nRESTfulSOS/RESTful/sos/AirBase_SOS/results/";
	private static final String SAMPLINGTIMES_URL_PREFIX = "http://v-swe.uni-muenster.de:8080/52nRESTfulSOS/RESTful/sos/AirBase_SOS/samplingTimes/";
	
	
	/**
	 * Answer to a normal GetObservation URL.
	 * For example:
	 * http://myRESTfulSOS/observations/sensors/HR:0002A/samplingtimes/2008-01-01,2008-12-31/observedproperties/concentration[NO2]
	 * 
	 * @param observationColl
	 * @param calledURL
	 * @return
	 */
	public static String getObsColTriples(
			OXFFeatureCollection observationColl,
            String calledURL) {
		
		String responseString = "";
		responseString += RDF_ROOT_START;
		
		String[] obsIds = new String[observationColl.size()];
		for (int i=0; i<observationColl.size(); i++) {
			List obsList = observationColl.toList();
			org.n52.oxf.feature.OXFFeature observation = (OXFFeature)obsList.get(i);
			String obsID = observation.getID();
			obsIds[i] = OBSERVATION_BY_ID_PREFIX + obsID;
		}

		responseString += encodeObsColTriple(calledURL, calledURL, obsIds);
		
		responseString += RDF_ROOT_END;
		
		return responseString;
	}
	
	/**
	 * Answer to a GetObservationById URL.
	 * For example:
	 * http://myRESTfulSOS/observations/ids/o_6543
	 * 
	 * @param observation
	 * @param calledURL
	 * @return
	 */
	public static String getObsTriple(
			OXFFeature observation,
            String calledURL) {
		
		OXFPhenomenonPropertyType observedProperty = (OXFPhenomenonPropertyType) observation.getAttribute(OXFMeasurementType.OBSERVED_PROPERTY);
		String observedPropertyURI = observedProperty.getURN();

		String procedureURL = (String) observation.getAttribute(OXFMeasurementType.PROCEDURE);
		
		OXFMeasureType result = (OXFMeasureType) observation.getAttribute(OXFMeasurementType.RESULT);
		double resultValue = result.getValue();
		
		ITime time = (ITime) observation.getAttribute(OXFMeasurementType.SAMPLING_TIME);
		String timeString = time.toISO8601Format();
		
		String obsId = observation.getID();
		
		String responseString = "";
		responseString += RDF_ROOT_START;
		responseString += RDFEncoder.encodeObsTriple(calledURL, obsId, observedPropertyURI, SAMPLINGTIMES_URL_PREFIX + timeString, procedureURL, RESULT_URL_PREFIX + resultValue);
		responseString += RDF_ROOT_END;
		return responseString;
	}
	
	/**
	 * Answer to a DescribeSensor URL.
	 * For example:
	 * http://myRESTfulSOS/sensors/HR:0002A.
	 * 
	 * @param calledURL  see above
	 * @param sensorShortID  for example: 'HR:0002A'
	 * @return
	 */
	public static String getSensorTriple(
            String calledURL,
            String sensorShortID) {
		
		String responseString = "";
		responseString += RDF_ROOT_START;
		responseString += encodeSensorTriple(calledURL, calledURL, SENSOR_OBSERVATIONS_PREFIX + sensorShortID);
		responseString += RDF_ROOT_END;
		return responseString;
	}
	
	/**
	 * Answer to a GetFeatureOfInterest URL.
	 * For example:
	 * http://myRESTfulSOS/features/HR0002A.
	 * 
	 * @param calledURL  see above
	 * @param sensorShortID  for example: 'HR0002A'
	 * @return
	 */
	public static String getFeatureTriple(
            String calledURL,
            String featureShortID) {
		
		String responseString = "";
		responseString += RDF_ROOT_START;
		responseString += encodeFoiTriple(calledURL, calledURL, "UNKNOWN", FEATURE_OBSERVATIONS_PREFIX + featureShortID);
		responseString += RDF_ROOT_END;
		return responseString;
	}
	
	
	//////////////////////////////////////////
	//// HELPER METHODS:
	//////////////////////////////////////////
	
	private static String encodeFoiTriple(String foiUrl, String foiLabel, String obsPropUrl, String relatedObsUrl){
		return "<rdf:Description rdf:about=\""+foiUrl+"\">" + "\n" +
				"<rdfs:label>"+foiLabel+"</rdfs:label>"+ "\n" +
				"<rdf:type rdf:resource=\"" + RDF_FOI_TYPE + "\"/>"+ "\n" +
				"<minioamld:hasProperty rdf:resource=\"" + obsPropUrl +"\"/>" + "\n" +
				"<minioamld:relatedObservations rdf:resource=\"" + relatedObsUrl +"\"/>" + "\n" + 
				"</rdf:Description>" + "\n";
	}
	
	private static String encodeObsTriple(String obsUrl, String obsLabel, String obsPropUrl, String samplingTimeUrl, String sensorUrl, String resultUrl){
		return "<rdf:Description rdf:about=\""+obsUrl+"\">" + "\n" +
				"<rdfs:label>"+obsLabel+"</rdfs:label>"+ "\n" +
				"<rdf:type rdf:resource=\"" + RDF_OBS_TYPE + "\"/>"+ "\n" +
				"<minioamld:aboutProperty rdf:resource=\"" + obsPropUrl +"\"/>" + "\n" +
				"<minioamld:samplingTime rdf:resource=\"" + samplingTimeUrl +"\"/>" + "\n" +
				"<minioamld:performedBy rdf:resource=\"" + sensorUrl +"\"/>" + "\n" +
				"<minioamld:hasResult rdf:resource=\"" + resultUrl +"\"/>" + "\n" +
				"</rdf:Description>"+ "\n";
	}
	
	private static String encodeObsColTriple(String obsColUrl, String obsColLabel, String[] relatedObsURLs){
		String result =  "<rdf:Description rdf:about=\""+obsColUrl+"\">" + "\n" +
		"<rdfs:label>"+obsColLabel+"</rdfs:label>"+ "\n" +
		"<rdf:type rdf:resource=\"" + RDF_OBSCOL_TYPE + "\"/>" + "\n";
		for (int i=0;i<relatedObsURLs.length;i++){
			result+="<minioamld:hasObservation rdf:resource=\"" + relatedObsURLs[i] +"\"/>" + "\n";
		}
		result+="</rdf:Description>" + "\n";
		return result;
	}
	
	private static String encodeObsPropTriple(String obsPropUrl, String obsPropLabel, String foiUrl, String relatedObsUrl){
		return "<rdf:Description rdf:about=\""+obsPropUrl+"\">" + "\n" +
				"<rdfs:label>"+obsPropLabel+"</rdfs:label>"+ "\n" +
				"<rdf:type rdf:resource=\"" + RDF_OBSPROP_TYPE + "\"/>"+ "\n" +
				"<minioamld:isPropertyOf rdf:resource=\"" + foiUrl +"\"/>" + "\n" +
				"<minioamld:relatedObservations rdf:resource=\"" + relatedObsUrl +"\"/>" + "\n" +
				"</rdf:Description>" + "\n";
	}
	
	private static String encodeResultTriple(String resultUrl, String resultLabel, String relatedObsUrl){
		return "<rdf:Description rdf:about=\""+resultUrl+"\">" + "\n" +
				"<rdfs:label>"+resultLabel+"</rdfs:label>"+ "\n" +
				"<rdf:type rdf:resource=\"" + RDF_RESULT_TYPE + "\"/>"+ "\n" +
				"<minioamld:relatedObservations rdf:resource=\"" + relatedObsUrl +"\"/>" + "\n" +
				"</rdf:Description>" + "\n";
	}
	
	private static String encodeSamplingTimeTriple(String stUrl, String stLabel, String relatedObsUrl){
		return "<rdf:Description rdf:about=\""+stUrl+"\">" + "\n" +
				"<rdfs:label>"+stLabel+"</rdfs:label>"+ "\n" +
				"<rdf:type rdf:resource=\"" + RDF_SAMPLINGTIME_TYPE + "\"/>"+ "\n" +
				"<minioamld:relatedObservations rdf:resource=\"" + relatedObsUrl +"\"/>" + "\n" +
				"</rdf:Description>" + "\n";
	}
	
	private static String encodeSensorTriple(String sensorUrl, String sensorLabel, String relatedObsUrl){
		return "<rdf:Description rdf:about=\""+sensorUrl+"\">" + "\n" +
				"<rdfs:label>"+sensorLabel+"</rdfs:label>"+ "\n" +
				"<rdf:type rdf:resource=\"" + RDF_SENSOR_TYPE + "\"/>"+ "\n" +
				"<minioamld:relatedObservations rdf:resource=\"" + relatedObsUrl +"\"/>" + "\n" +
				"</rdf:Description>" + "\n";
	}
}
