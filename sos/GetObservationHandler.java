package org.n52.oxf.rest.sos;

import javax.servlet.ServletException;
import org.n52.oxf.owsCommon.ServiceDescriptor;
import org.n52.oxf.owsCommon.capabilities.Contents;
import org.n52.oxf.serviceAdapters.ParameterContainer;
import org.n52.oxf.serviceAdapters.sos.ISOSRequestBuilder;
import org.n52.oxf.serviceAdapters.sos.SOSAdapter;
import org.n52.oxf.serviceAdapters.sos.caps.ObservationOffering;

/**
 * 
 * @author Elsaesser
 * 
 */
public class GetObservationHandler {

	private static final long serialVersionUID = 7125211047827477124L;

	private final static String KEY_OFFERING = "offering";
	private final static String KEY_FOI = "featuresOfInterest";
	private final static String KEY_OBSPROP = "observedProperties";
	private final static String KEY_PROCEDURES = "procedures";
	private final static String KEY_TIMES = "samplingTimes";
	private final static String KEY_RESULTMODEL = "resultModel";
	// URL Prefix for foiIDPrefix
	private final static String foiIDPrefix = "http://v-swe.uni-muenster.de:8080/52nRESTfulSOS/RESTful/sos/AirBase_SOS/fois/";
	// URL Prefix for PhenomenonId/Observed Property
	private final static String phenomenonIdPrefix = "http://giv-genesis.uni-muenster.de:8080/SOR/REST/phenomenon/OGC/";
	// URL Prefix for procedures
	private final static String proceduresIdPrefix = "http://v-swe.uni-muenster.de:8080/52nRESTfulSOS/RESTful/sos/AirBase_SOS/sensors/";
	// URL SOS
	private final static String urlSosName = "AirBase_SOS";
	private static int numberOfParameterContainer = -1;

	/**
	 * 
	 * Method to divide the urlSegments into String offeringID, String [] foiID,
	 * String [] obspropID, String [] proceduresID String [] time, String
	 * resultModel
	 * 
	 * Parameter array 'urlSegments' is structured as follows: 
	 * ['key','values','key','values', ... ,'key','values']
	 * 
	 * And key is either: 'offering', 'featuresOfInterest',
	 * 'observedProperties', ...
	 * 
	 * The values of offering, featuresOfInterest, observedProperties and
	 * procedures are separated by ';'
	 * 
	 * The values of begin time and end time of the times key are separated by
	 * ',' or there is just one value for times
	 * 
	 * @param urlSegments
	 * @return ParameterContainer for the GetObservation request
	 */
	public static ParameterContainer[] getObservationsRESTfully(String[] urlSegments, SOSAdapter adapter, ServiceDescriptor serviceDesc, String serviceVersion, String sosName) throws ServletException {
		String[] offeringID = null;
		String[] foiID = null;
		String[] obspropID = null;
		String[] proceduresID = null;
		String[] time = null;
		String resultModel = null;
		int a = 0;
		ParameterContainer[] result;

		for (int i = 0; i < urlSegments.length; i++) {

			if (urlSegments[i] != null)
				if (urlSegments[i].equalsIgnoreCase(KEY_OFFERING)) {
					a = i + 1;
					// if next key is != null and not a another key get the
					// offeringID
					if (urlSegments[a] != null) {
						if (!urlSegments[a].equalsIgnoreCase(KEY_FOI) && !urlSegments[a].equalsIgnoreCase(KEY_OBSPROP) && !urlSegments[a].equalsIgnoreCase(KEY_PROCEDURES) && !urlSegments[a].equalsIgnoreCase(KEY_TIMES) && !urlSegments[a].equalsIgnoreCase(KEY_RESULTMODEL)) {
							offeringID = new String[urlSegments[a].split(";").length];
							offeringID = urlSegments[a].split(";");
						} else {
							throw new ServletException("Give an id for " + KEY_OFFERING);
						}
					} else {
						throw new ServletException("Give an id for " + KEY_OFFERING);
					}
				} else if (urlSegments[i].equalsIgnoreCase(KEY_FOI)) {
					a = i + 1;
					// if next key is != null and not a another key get the
					// foiID
					if (urlSegments[a] != null) {
						if (!urlSegments[a].equalsIgnoreCase(KEY_OFFERING) && !urlSegments[a].equalsIgnoreCase(KEY_OBSPROP) && !urlSegments[a].equalsIgnoreCase(KEY_PROCEDURES) && !urlSegments[a].equalsIgnoreCase(KEY_TIMES) && !urlSegments[a].equalsIgnoreCase(KEY_RESULTMODEL)) {
							foiID = new String[urlSegments[a].split(";").length];
							foiID = urlSegments[a].split(";");
							if (urlSosName.equals(sosName)) {
								for (int k = 0; k < foiID.length; k++) {
									foiID[k] = foiIDPrefix + foiID[k].toString();
								}
							}
						} else {
							throw new ServletException("Give an id for " + KEY_FOI);
						}
					} else {
						throw new ServletException("Give an id for " + KEY_FOI);
					}
				} else if (urlSegments[i].equalsIgnoreCase(KEY_OBSPROP)) {
					a = i + 1;
					// if next key is != null and not a another key get the
					// obspropID
					if (urlSegments[a] != null) {
						if (!urlSegments[a].equalsIgnoreCase(KEY_OFFERING) && !urlSegments[a].equalsIgnoreCase(KEY_FOI) && !urlSegments[a].equalsIgnoreCase(KEY_PROCEDURES) && !urlSegments[a].equalsIgnoreCase(KEY_TIMES) && !urlSegments[a].equalsIgnoreCase(KEY_RESULTMODEL)) {
							obspropID = new String[urlSegments[a].split(";").length];
							obspropID = urlSegments[a].split(";");
							if (urlSosName.equals(sosName)) {
								for (int k = 0; k < obspropID.length; k++) {
									obspropID[k] = phenomenonIdPrefix + obspropID[k].toString();
								}
							}
						} else {
							throw new ServletException("Give an id for " + KEY_OBSPROP);
						}
					} else {
						throw new ServletException("Give an id for " + KEY_OBSPROP);
					}
				} else if (urlSegments[i].equalsIgnoreCase(KEY_PROCEDURES)) {
					a = i + 1;
					// if next key is != null and not a another key get the
					// proceduresID
					if (urlSegments[a] != null) {
						if (!urlSegments[a].equalsIgnoreCase(KEY_OFFERING) && !urlSegments[a].equalsIgnoreCase(KEY_FOI) && !urlSegments[a].equalsIgnoreCase(KEY_OBSPROP) && !urlSegments[a].equalsIgnoreCase(KEY_TIMES) && !urlSegments[a].equalsIgnoreCase(KEY_RESULTMODEL)) {
							proceduresID = new String[urlSegments[a].split(";").length];
							proceduresID = urlSegments[a].split(";");
							if (urlSosName.equals(sosName)) {
								for (int k = 0; k < proceduresID.length; k++) {
									proceduresID[k] = proceduresIdPrefix + proceduresID[k].toString();
								}
							}
						} else {
							throw new ServletException("Give an id for " + KEY_PROCEDURES);
						}
					} else {
						throw new ServletException("Give an id for " + KEY_PROCEDURES);
					}
				} else if (urlSegments[i].equalsIgnoreCase(KEY_TIMES)) {
					a = i + 1;
					// if next key is != null and not a another key get the time
					if (urlSegments[a] != null) {
						if (!urlSegments[a].equalsIgnoreCase(KEY_OFFERING) && !urlSegments[a].equalsIgnoreCase(KEY_FOI) && !urlSegments[a].equalsIgnoreCase(KEY_OBSPROP) && !urlSegments[a].equalsIgnoreCase(KEY_PROCEDURES) && !urlSegments[a].equalsIgnoreCase(KEY_RESULTMODEL)) {
							time = urlSegments[a].split(",");
						} else {
							throw new ServletException("Give an id for " + KEY_TIMES);
						}
					} else {
						throw new ServletException("Give an id for " + KEY_TIMES);
					}
				} else if (urlSegments[i].equalsIgnoreCase(KEY_RESULTMODEL)) {
					a = i + 1;
					// if next key is != null and not a another key get the
					// resultModel
					if (urlSegments[a] != null) {
						if (!urlSegments[a].equalsIgnoreCase(KEY_OFFERING) && !urlSegments[a].equalsIgnoreCase(KEY_FOI) && !urlSegments[a].equalsIgnoreCase(KEY_OBSPROP) && !urlSegments[a].equalsIgnoreCase(KEY_PROCEDURES) && !urlSegments[a].equalsIgnoreCase(KEY_TIMES)) {
							resultModel = urlSegments[a];
						} else {
							throw new ServletException("Give an id for " + KEY_TIMES);
						}
					} else {
						throw new ServletException("Give an id for " + KEY_TIMES);
					}
				}
		}
		try {
			numberOfParameterContainer = getNumberOfParameterContainer(offeringID, foiID, obspropID, proceduresID, serviceDesc);
			result = getParameterContainer(offeringID, foiID, obspropID, proceduresID, time, resultModel, serviceDesc, serviceVersion);
		} catch (Exception e) {
			throw new ServletException(e);
		}
		return result;
	}

	/**
	 * 
	 * Method to create a ParameterContainer for the GetObservations request
	 * (new URL schema) For the ParameterContainer 'parameters' are the
	 * ParameterShells with the following serviceSidedNames required: <li>
	 * service</li> <li>version</li> <li>offering</li> <li>observedProperty</li>
	 * <li>resultFormat</li>
	 * 
	 * <br>
	 * <br>
	 * The following ones are optional: <li>eventTime</li> <li>procedure</li>
	 * <li>featureOfInterest</li> <li>result</li> <li>resultModel</li> <li>
	 * responseMode</li>
	 */
	private static ParameterContainer[] getParameterContainer(String[] offeringID, String[] foiID, String[] obspropID, String[] proceduresID, String[] time, String resultModel, ServiceDescriptor serviceDesc, String serviceVersion) throws ServletException {

		ParameterContainer[] paramCon = new ParameterContainer[numberOfParameterContainer];

		int a = 0; // count offering

		// parameter for the request
		Contents contents = serviceDesc.getContents();
		String[] offering = contents.getDataIdentificationIDArray();
		String[] offeringForRequest = new String[offering.length];

		for (int b = 0; b < numberOfParameterContainer; b++) {
			paramCon[b] = new ParameterContainer();
		}

		try {
			// given required parameter: offering
			// find required parameter: observedProperty
			if (offeringID != null) {

				for (int y = 0; y < offeringID.length; y++) {
					offeringForRequest[a] = offeringID[y];
					paramCon[y].addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_OFFERING_PARAMETER, offeringID[y]);
					a++;
				}

				if (obspropID == null & foiID == null & proceduresID == null) {
					for (int l = 0; l < offeringID.length; l++) {
						ObservationOffering offeringOBS = (ObservationOffering) contents.getDataIdentification(offeringForRequest[l]);
						String[] obsPropID = offeringOBS.getObservedProperties();

						paramCon[l].addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_OBSERVED_PROPERTY_PARAMETER, obsPropID);
					}
				}

				// test if observedProperty in the URL is of the offering in the URL
				if (obspropID != null) {
					boolean containedOP = false;

					for (int i = 0; i < offeringForRequest.length; i++) {
						if (offeringForRequest[i] != null) {
							ObservationOffering offeringPro = (ObservationOffering) contents.getDataIdentification(offeringForRequest[i]);
							String[] obsPro = offeringPro.getObservedProperties();

							for (int j = 0; j < obspropID.length; j++) {
								for (int k = 0; k < obsPro.length; k++) {
									if (obspropID[j].equals(obsPro[k])) {
										containedOP = true;
									}
								}
								if (!containedOP) {
									throw new ServletException("\n The offering of " + obspropID[j] + " is not the same as the given offering in the URL!\n");
								}
								containedOP = false;
							}
						}
					}
				}

				// test if foi in the URL is of the offering in the URL
				if (foiID != null) {
					boolean containFOI = false;

					for (int i = 0; i < offeringForRequest.length; i++) {
						if (offeringForRequest[i] != null) {
							ObservationOffering offeringFOI = (ObservationOffering) contents.getDataIdentification(offeringForRequest[i]);
							String foi[] = offeringFOI.getFeatureOfInterest();

							for (int j = 0; j < foiID.length; j++) {
								for (int k = 0; k < foi.length; k++) {
									if (foiID[j].equals(foi[k])) {
										containFOI = true;
									}
								}
								if (!containFOI) {
									throw new ServletException("\n The offering of " + foiID[j] + " is not the same as the given offering in the URL or the offering has not the foi!\n");
								}
								containFOI = false;
							}
						}
					}
				}

				// test if procedures in the URL is of the offering in the URL
				if (proceduresID != null) {
					boolean containProcedures = false;

					for (int i = 0; i < offeringForRequest.length; i++) {
						if (offeringForRequest[i] != null) {
							ObservationOffering offeringProcedures = (ObservationOffering) contents.getDataIdentification(offeringForRequest[i]);
							String procedures[] = offeringProcedures.getProcedures();

							for (int j = 0; j < proceduresID.length; j++) {
								for (int k = 0; k < procedures.length; k++) {
									if (proceduresID[j].equals(procedures[k])) {
										containProcedures = true;
									}
								}
								if (!containProcedures) {
									throw new ServletException("\n The offering of " + proceduresID[j] + " is not the same as the given offering in the URL or the offering has not the procedures!\n");
								}
								containProcedures = false;
							}
						}
					}
				}

			}

			// given required parameter: observedProperty
			// find required parameter: offering
			if (obspropID != null) {

				boolean containedOffering = false;

				for (int i = 0; i < offering.length; i++) {
					ObservationOffering offeringPro = (ObservationOffering) contents.getDataIdentification(offering[i].toString());
					String[] obsPro = offeringPro.getObservedProperties();
					String[] obspropIDForRequest = new String[obsPro.length];

					int z = 0;
					for (int j = 0; j < obspropID.length; j++) {
						for (int k = 0; k < obsPro.length; k++) {
							if (obspropID[j].equals(obsPro[k])) {
								// look if offering is contained in
								// offeringForRequest
								for (int l = 0; l < offeringForRequest.length; l++) {
									if (offeringForRequest[l] != null)
										if (offeringForRequest[l].equals(offering[i])) {
											containedOffering = true;

											obspropIDForRequest[z] = obspropID[j];
											z++;
										}
								}
								// if offering is null and not contained in
								// offeringForRequest
								if (offeringID == null & !containedOffering) {
									offeringForRequest[a] = offering[i].toString();

									paramCon[a].addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_OFFERING_PARAMETER, offeringForRequest[a]);
									obspropIDForRequest[z] = obspropID[j];
									z++;
									a++;
								}
								// if offering is not null and not contained in
								// offeringForRequest
								if (offeringForRequest != null & !containedOffering & foiID != null & proceduresID != null) {
									throw new ServletException("\n The offering \"" + offering[i] + "\" of " + obspropID[j] + " is not the same as the given offering of an other parameter!\n");
								}
								containedOffering = false;
							}
						}
					}
					for (int l = 0; l < offeringForRequest.length; l++) {
						if (offeringForRequest[l] != null)
							if (offeringForRequest[l].equals(offering[i].toString())) {
								if (obspropIDForRequest[0] != null)
									paramCon[l].addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_OBSERVED_PROPERTY_PARAMETER, obspropIDForRequest);
							}
					}
				}

				// test if procedures in the URL is of the offering of the
				// observedProperty in the URL
				if (proceduresID != null) {
					boolean containProcedures = false;
					String[] proceduresIDForRequest = new String[proceduresID.length];
					int z = 0;

					for (int i = 0; i < offeringForRequest.length; i++) {
						if (offeringForRequest[i] != null) {
							ObservationOffering offeringProcedures = (ObservationOffering) contents.getDataIdentification(offeringForRequest[i]);
							String procedures[] = offeringProcedures.getProcedures();

							for (int j = 0; j < proceduresID.length; j++) {
								for (int k = 0; k < procedures.length; k++) {
									if (proceduresID[j].equals(procedures[k])) {
										containProcedures = true;
										proceduresIDForRequest[z] = proceduresID[j];
										z++;
									}
								}
								if (!containProcedures) {
									throw new ServletException("\n The offering of " + proceduresID[j] + " is not the same as the given offering of the observedProperty in the URL or the offering has not the procedures!\n");
								}
								containProcedures = false;
							}
						}
					}
					int proc = -1;
					for (int p = 0; p < proceduresIDForRequest.length; p++) {
						proc++;						
					}

					for (int l = 0; l < offeringForRequest.length; l++) {
						if (offeringForRequest[l] != null)
							if (proceduresIDForRequest[proc] != null)
								paramCon[l].addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_PROCEDURE_PARAMETER, proceduresIDForRequest);
					}

				}
			}

			// given optional parameter: featureOfInterest
			// find required parameter: offering
			if (foiID != null) {
				boolean containedOffering = false;

				for (int i = 0; i < offering.length; i++) {
					ObservationOffering offeringFOI = (ObservationOffering) contents.getDataIdentification(offering[i].toString());
					String foi[] = offeringFOI.getFeatureOfInterest();
					String[] foiIDForRequest = new String[foiID.length];
					int z = 0;
					for (int j = 0; j < foiID.length; j++) {
						for (int k = 0; k < foi.length; k++) {
							if (foiID[j].equals(foi[k])) {
								// look if offering is contained in
								// offeringForRequest
								for (int l = 0; l < offeringForRequest.length; l++) {
									if (offeringForRequest[l] != null)
										if (offeringForRequest[l].equals(offering[i].toString())) {
											containedOffering = true;

											foiIDForRequest[z] = foiID[j];
											z++;
										}
								}
								// if offering is null and not contained in
								// offeringForRequest
								if (offeringID == null & !containedOffering) {
									offeringForRequest[a] = offering[i].toString();

									paramCon[a].addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_OFFERING_PARAMETER, offeringForRequest[a]);
									foiIDForRequest[z] = foiID[j];
									z++;
									a++;
								}
								// if offering is not null and not contained in
								// offeringForRequest
								if (proceduresID == null & foiID != null & obspropID == null) {

								} else if (offeringForRequest != null & !containedOffering) {

									throw new ServletException("\n The offering \"" + offering[i] + "\" of " + foiID[j] + " is not the same as the given offering of an other parameter!\n");
								}
								containedOffering = false;
							}
						}
					}

					for (int l = 0; l < offeringForRequest.length; l++) {
						if (offeringForRequest[l] != null)
							if (offeringForRequest[l].equals(offering[i].toString())) {

								if (foiIDForRequest[0] != null)
									paramCon[l].addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_FEATURE_OF_INTEREST_PARAMETER, foiIDForRequest);
							}
					}

				}
			}

			// given optional parameter: procedures
			// find required parameter: offering
			if (proceduresID != null & obspropID == null) {

				boolean containedOffering = false;

				for (int i = 0; i < offering.length; i++) {
					ObservationOffering offeringPro = (ObservationOffering) contents.getDataIdentification(offering[i].toString());
					String[] pro = offeringPro.getProcedures();
					String[] proceduresIDForRequest = new String[proceduresID.length];
					int z = 0;
					for (int j = 0; j < proceduresID.length; j++) {
						for (int k = 0; k < pro.length; k++) {
							if (proceduresID[j].equals(pro[k])) {
								// look if offering is contained in
								// offeringForRequest
								for (int l = 0; l < offeringForRequest.length; l++) {
									if (offeringForRequest[l] != null)
										if (offeringForRequest[l].equals(offering[i].toString())) {
											containedOffering = true;

											proceduresIDForRequest[z] = proceduresID[j];
											z++;
										}
								}

								// if offering is null and not contained in
								// offeringForRequest
								if (offeringID == null & !containedOffering) {
									if (!offering[i].equals("PARTICULATE_MATTER_<_10_ÎœM_(AEROSOL)")) {
										offeringForRequest[a] = offering[i].toString();
										paramCon[a].addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_OFFERING_PARAMETER, offeringForRequest[a]);
										proceduresIDForRequest[z] = proceduresID[j];
										z++;
										a++;
									}
								}

								// if offering is not null and not contained in
								// offeringForRequest
								if (proceduresID != null & foiID == null & obspropID == null) {

								} else if (offeringForRequest != null & !containedOffering) {

									throw new ServletException("The offering \"" + offering[i] + "\" of " + proceduresID[j] + " is not the same as the given offering of an other parameter!\n");
								}

								containedOffering = false;
							}
						}
					}
					int proc = -1;
					for (int p = 0; p < proceduresIDForRequest.length; p++) {
						proc++;						
					}

					for (int l = 0; l < offeringForRequest.length; l++) {
						if (offeringForRequest[l] != null)
							if (offeringForRequest[l].equals(offering[i].toString())) {
								if (proceduresIDForRequest[proc] != null)
									paramCon[l].addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_PROCEDURE_PARAMETER, proceduresIDForRequest);
							}
					}

				}
			}

			// optional Parameter eventTime //TODO test if time is inside the
			// time interval
			if (time != null) {
				for (int x = 0; x < numberOfParameterContainer; x++) {
					if (time.length == 2) {
						paramCon[x].addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_EVENT_TIME_PARAMETER, time[0] + "/" + time[1]);
					} else {
						paramCon[x].addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_EVENT_TIME_PARAMETER, time[0] + "/" + time[0]);
					}
				}
			}

			// optional Parameter resultModel
			// if not set, set as Measurement
			if (resultModel != null) {
				for (int x = 0; x < numberOfParameterContainer; x++) {
					paramCon[x].addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_RESULT_MODEL_PARAMETER, resultModel);
				}
			} else {
				for (int x = 0; x < numberOfParameterContainer; x++) {
					// paramCon[x].addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_RESULT_MODEL_PARAMETER,
					// "Observation");
					paramCon[x].addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_RESULT_MODEL_PARAMETER, "Measurement");
				}
			}

			// find required parameter: observedProperty for all offering in
			// offeringForRequest
			if (obspropID == null) {
				for (int l = 0; l < offeringForRequest.length; l++) {
					if (offeringForRequest[l] != null) {
						ObservationOffering offeringOBS = (ObservationOffering) contents.getDataIdentification(offeringForRequest[l]);
						String[] obsPropID = offeringOBS.getObservedProperties();

						paramCon[l].addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_OBSERVED_PROPERTY_PARAMETER, obsPropID);
					}
				}
			}

			for (int x = 0; x < numberOfParameterContainer; x++) {
				// hard-coded parameters: required Parameters service, version
				// and resultFormat
				paramCon[x].addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_RESPONSE_FORMAT_PARAMETER, "text/xml;subtype=\"om/" + serviceVersion + "\"");
				paramCon[x].addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_SERVICE_PARAMETER, SOSAdapter.SERVICE_TYPE);
				paramCon[x].addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_VERSION_PARAMETER, serviceVersion);
				if (serviceVersion.equals("0.0.0")) {
					paramCon[x].addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_RESPONSE_MODE_PARAMETER, "inline");
				}

			}
		} catch (Exception e) {
			throw new ServletException(e);
		}

		return paramCon;
	}

	/**
	 * Method to count the number of offering for the ParameterContainer
	 * 
	 * @param offeringID
	 * @param foiID
	 * @param obspropID
	 * @param proceduresID
	 * @param serviceDesc
	 * @return
	 * @throws ServletException
	 */
	private static int getNumberOfParameterContainer(String[] offeringID, String[] foiID, String[] obspropID, String[] proceduresID, ServiceDescriptor serviceDesc) {

		int a = 0; // count offering

		// parameter for the request
		Contents contents = serviceDesc.getContents();
		String[] offering = contents.getDataIdentificationIDArray();
		String[] offeringForRequest = new String[offering.length];

		// given required parameter: offering 
		if (offeringID != null) {
			for (int y = 0; y < offeringID.length; y++) {
				offeringForRequest[a] = offeringID[y];
				a++;
			}
		}

		// given optional parameter: featureOfInterest
		// find required parameter: offering
		if (foiID != null) {

			boolean containedOffering = false;

			for (int i = 0; i < offering.length; i++) {
				ObservationOffering offeringFOI = (ObservationOffering) contents.getDataIdentification(offering[i].toString());
				String foi[] = offeringFOI.getFeatureOfInterest();
				for (int j = 0; j < foiID.length; j++) {
					for (int k = 0; k < foi.length; k++) {
						if (foiID[j].equals(foi[k])) {
							// look if offering is contained in
							// offeringForRequest
							for (int l = 0; l < offeringForRequest.length; l++) {
								if (offeringForRequest[l] != null)
									if (offeringForRequest[l].equals(offering[i].toString())) {
										containedOffering = true;
									}
							}
							if (offeringID == null & !containedOffering) {
								offeringForRequest[a] = offering[i].toString();
								a++;
							}
							containedOffering = false;
						}
					}
				}
			}
		}

		// given optional parameter: procedures
		// find required parameter: offering
		if (proceduresID != null) {

			boolean containedOffering = false;

			for (int i = 0; i < offering.length; i++) {
				ObservationOffering offeringPro = (ObservationOffering) contents.getDataIdentification(offering[i].toString());
				String pro[] = offeringPro.getProcedures();
				for (int j = 0; j < proceduresID.length; j++) {
					for (int k = 0; k < pro.length; k++) {
						if (proceduresID[j].equals(pro[k])) {
							// look if offering is contained in
							// offeringForRequest
							for (int l = 0; l < offeringForRequest.length; l++) {
								if (offeringForRequest[l] != null)
									if (offeringForRequest[l].equals(offering[i].toString())) {
										containedOffering = true;
									}
							}
							if (offeringID == null & !containedOffering) {
								if (!offering[i].equals("PARTICULATE_MATTER_<_10_ÎœM_(AEROSOL)")) {
									offeringForRequest[a] = offering[i].toString();
									a++;
								}
							}
							containedOffering = false;
						}
					}
				}
			}

		}

		// given required parameter: observedProperty
		// find required parameter: offering
		if (obspropID != null) {

			boolean containedOffering = false;

			for (int i = 0; i < offering.length; i++) {
				ObservationOffering offeringPro = (ObservationOffering) contents.getDataIdentification(offering[i].toString());
				String[] obsPro = offeringPro.getObservedProperties();
				for (int j = 0; j < obspropID.length; j++) {
					for (int k = 0; k < obsPro.length; k++) {
						if (obspropID[j].equals(obsPro[k])) {
							// look if offering is contained in
							// offeringForRequest
							for (int l = 0; l < offeringForRequest.length; l++) {
								if (offeringForRequest[l] != null)
									if (offeringForRequest[l].equals(offering[i])) {
										containedOffering = true;
									}
							}
							// if offering is not contained in offeringForRequest
							if (offeringID == null & !containedOffering) {
								offeringForRequest[a] = offering[i].toString();

								a++;
							}
							containedOffering = false;
						}
					}
				}
			}
		}
		return a;
	}

}