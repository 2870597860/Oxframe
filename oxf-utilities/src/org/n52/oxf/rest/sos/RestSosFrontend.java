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

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.n52.oxf.feature.IFeatureStore;
import org.n52.oxf.feature.OXFFeatureCollection;
import org.n52.oxf.feature.OXFMeasurementType;
import org.n52.oxf.feature.sos.SOSObservationStore;
import org.n52.oxf.owsCommon.ExceptionReport;
import org.n52.oxf.owsCommon.ServiceDescriptor;
import org.n52.oxf.owsCommon.capabilities.Contents;
import org.n52.oxf.render.IChartRenderer;
import org.n52.oxf.render.sos.TimeSeriesChartRenderer4xPhenomenons;
import org.n52.oxf.rest.RestWebService;
import org.n52.oxf.serviceAdapters.IServiceAdapter;
import org.n52.oxf.serviceAdapters.OperationResult;
import org.n52.oxf.serviceAdapters.ParameterContainer;
import org.n52.oxf.serviceAdapters.sos.ISOSRequestBuilder;
import org.n52.oxf.serviceAdapters.sos.SOSAdapter;
import org.n52.oxf.serviceAdapters.sos.caps.ObservationOffering;
import org.n52.oxf.util.IOHelper;
import org.n52.oxf.valueDomains.time.ITimePeriod;
import org.n52.util.Log;

//��������������ĸ��ģ�EclipseĬ�ϰ���Щ�ܷ������Ƶ�API�����ERROR��
//ֻҪ��Windows-Preferences-Java-Complicer-Errors/Warnings�����Deprecated and restricted API�е�
//Forbidden references(access rules)ѡΪWarning�Ϳ��Ա���ͨ����

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;  

/**
 * 
 * @author Elsaesser
 * 
 */
public class RestSosFrontend extends RestWebService {

	private static final long serialVersionUID = 7125211047827477123L;

	private final static Logger LOGGER = Log.setUpLogger(RestSosFrontend.class);

	private String[][] sosMap;
	// OutputFormats of the GetObservation operation
	private final static String[] myOutputFormats = new String[] { "OM", "KML", "DIAGRAM", "RDF" };
	// resources that can be queried
	private final static String[] myResources = new String[] { "observations", "fois", "sensors", "capabilities" };
	// Version of the SOS specification
	private final static String[] versionSOS = new String[] { "1.0.0" };
	// URL Prefix for foiID
	private final static String foiID = "http://v-swe.uni-muenster.de:8080/52nRESTfulSOS/RESTful/sos/AirBase_SOS/fois/";
	// URL Prefix for PhenomenonId/Observed Property
	private final static String PhenomenonId = "http://giv-genesis.uni-muenster.de:8080/SOR/REST/phenomenon/OGC/";
	// URL Prefix for procedures
	private final static String proceduresIdPrefix = "http://v-swe.uni-muenster.de:8080/52nRESTfulSOS/RESTful/sos/AirBase_SOS/sensors/";
	// URL SOS
	private final static String sosName = "AirBase_SOS";

	// Key for new URL schema
	private final static String KEY_OFFERING = "offering";
	private final static String KEY_FOI = "featuresOfInterest";
	private final static String KEY_OBSPROP = "observedProperties";
	private final static String KEY_PROCEDURES = "procedures";
	private final static String KEY_TIMES = "samplingTimes";
	private final static String KEY_RESULTMODEL = "resultModel";
	private final static String KEY_IDS = "ids";
	private final static String KEY_OUTPUTFORMAT = "outputFormat";
	/*
	 * the following attributes define the position of the different parameters
	 * of the GetObservation operation within the RestFul URL. (old URL schema)
	 */
	private final static int LAST_POSITION = 20;
	private final static int POSITION_SOS = 0;
	private final static int POSITION_RESOURCES = 1;
	private final static int POSITION_OFFERING = 2;
	private final static int POSITION_FOI = 3;
	private final static int POSITION_OBSERVED_PROPERTY = 4;
	private final static int POSITION_BEGIN_YEAR = 5;
	private final static int POSITION_BEGIN_MONTH = 6;
	private final static int POSITION_BEGIN_DAY = 7;
	private final static int POSITION_BEGIN_HOUR = 8;
	private final static int POSITION_BEGIN_MINUTE = 9;
	private final static int POSITION_BEGIN_SECOND = 10;
	private final static int POSITION_BEGIN_TIMEZONE = 11;
	private final static int POSITION_END_YEAR = 12;
	private final static int POSITION_END_MONTH = 13;
	private final static int POSITION_END_DAY = 14;
	private final static int POSITION_END_HOUR = 15;
	private final static int POSITION_END_MINUTE = 16;
	private final static int POSITION_END_SECOND = 17;
	private final static int POSITION_END_TIMEZONE = 18;
	private final static int POSITION_RESULT_MODEL = 19;
	private final static int POSITION_OUTPUT_FORMAT = LAST_POSITION;

	/*
	 * the following attributes define the position of the different parameters
	 * of the DescribeSensor operation within the RestFul URL.
	 */
	private final static int LAST_POSITIONds = 3;
	private final static int POSITION_PROCEDURES = 2;

	/*
	 * the following attributes define the position of the different parameters
	 * of the GetFeatureOfInterest operation within the RestFul URL.
	 */
	private final static int LAST_POSITIONgfoi = 3;
	private final static int POSITION_FOIS = 2;

	/*
	 * the following attributes define the position of the different parameters
	 * of the GetCapabiliteis operation within the RestFul URL.
	 */
	private final static int LAST_POSITIONgcap = 3;
	private final static int POSITION_CAPABILITEIS = 2;

	@Override
	public void init() throws ServletException {
		LOGGER.info("init started");

		try {
			sosMap = loadURLs();
		} catch (IOException e) {
			throw new ServletException(e);
		}

		LOGGER.info("init completed");
	}

	/**
	 * RESTful SOS access.
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		OutputStream out = response.getOutputStream();
		response.setContentType("text/html");

		String headerValue = request.getHeader("Accept");
		System.out.println(headerValue);

		String requestURL = request.getRequestURL().toString();

		// HACK: recognize whether the URL is encoded or not:
		// USE SPECIAL CHARACTERS: '%3A' == ':' and '%2B' == '+'
		if (requestURL.contains("%3A") || requestURL.contains("%2B")) {
			requestURL = URLDecoder.decode(requestURL, "UTF-8");
		}

		// HACK: recognize whether the URL is encoded or not:
		// USE SPECIAL CHARACTERS: '%28' == '(' and '%29' == ')'
		if (requestURL.contains("%28") || requestURL.contains("%29")) {
			requestURL = URLDecoder.decode(requestURL, "UTF-8");
		}

		// HACK: recognize whether the URL is encoded or not:
		// USE SPECIAL CHARACTERS: '%5B' == '[' and '%5D' == ']'
		if (requestURL.contains("%5B") || requestURL.contains("%29")) {
			requestURL = URLDecoder.decode(requestURL, "UTF-8");
		}

		String servletPath = request.getServletPath();
		String[] resources = getResourcesStringArray(requestURL, servletPath);

		LOGGER.info("Request URL:" + requestURL);

		// SOS resource:
		if (resources.length == POSITION_SOS) {
			String htmlString = renderResourcesHtml(requestURL, servletPath, "SOS", sosMap[0]);
			out.write(htmlString.getBytes());
		} else {

			SOSAdapter adapter;
			ServiceDescriptor serviceDesc;
			String serviceVersion;
			String serviceURL;

			// Let's connect to the SOS:
			try {
				String chosenSOS = resources[POSITION_SOS];
				int sosIndex = Arrays.asList(sosMap[0]).indexOf(chosenSOS);

				if (sosIndex < 0) {
					LOGGER.severe("SOS URL '" + chosenSOS + "' is unknown.");
					throw new IllegalArgumentException("SOS URL '" + chosenSOS + "' is unknown.");
				}

				serviceURL = sosMap[1][sosIndex];
				serviceVersion = sosMap[2][sosIndex];

				adapter = new SOSAdapter(serviceVersion);
				serviceDesc = adapter.initService(serviceURL);
			} catch (Exception e) {
				throw new ServletException(e);
			}

			// RESOURCES: "observations", "fois", "sensors", "capabilities"
			if (resources.length == POSITION_RESOURCES) {
				String htmlString = renderResourcesHtml(requestURL, servletPath, "Resources", myResources);
				out.write(htmlString.getBytes());
			} else if (resources.length > POSITION_RESOURCES) {
				// GetCapabiliteis request
				if (resources[POSITION_RESOURCES].equalsIgnoreCase("capabilities")) {

					if (resources.length == POSITION_CAPABILITEIS) {
						String htmlString = renderResourcesHtml(requestURL, servletPath, "Version of the SOS specification", versionSOS);
						out.write(htmlString.getBytes());
					}

					// enough resources defined:
					if (resources.length >= LAST_POSITIONgcap) {
						// let's try to request the data:
						try {
							ParameterContainer paramCon = new ParameterContainer();

							// read out resources and use them as request
							// parameters:
							paramCon.addParameterShell(ISOSRequestBuilder.GET_CAPABILITIES_ACCEPT_VERSIONS_PARAMETER, resources[POSITION_CAPABILITEIS]);

							// hard-coded parameters:
							paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_RESPONSE_FORMAT_PARAMETER, "text/xml;subtype=\"om/" + serviceVersion + "\"");
							paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_SERVICE_PARAMETER, SOSAdapter.SERVICE_TYPE);
							paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_VERSION_PARAMETER, serviceVersion);
							if (serviceVersion.equals("0.0.0")) {
								paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_RESPONSE_MODE_PARAMETER, "inline");
							}
							// send GetCapabiliteis request:
							OperationResult opResult = adapter.doOperation(serviceDesc.getOperationsMetadata().getOperationByName(SOSAdapter.GET_CAPABILITIES), paramCon);

							response.setContentType("text/xml");
							out.write(opResult.getIncomingResult());

						} catch (ExceptionReport excReport) {
							out.write(excReport.toHtmlString().getBytes());
						} catch (Exception e) {
							throw new ServletException(e);
						}
					}
				}

				// DescribeSensor request
				if (resources[POSITION_RESOURCES].equalsIgnoreCase("sensors")) {
					Contents contents = serviceDesc.getContents();
					String[] o = contents.getDataIdentificationIDArray();

					int countprocedures = 0;
					for (int i = 0; i < o.length; i++) {
						ObservationOffering offering = (ObservationOffering) contents.getDataIdentification(o[i].toString());
						countprocedures += offering.getProcedures().length;
					}

					String[] procedures = new String[countprocedures];
					int a = 0;
					for (int i = 0; i < o.length; i++) {

						ObservationOffering offering = (ObservationOffering) contents.getDataIdentification(o[i].toString());

						if (offering == null) {
							throw new ServletException("Resource '/" + getResourcesString(requestURL, servletPath) + "' unavailable.");
						}

						String[] procedure = offering.getProcedures();
						for (int j = 0; j < procedure.length; j++) {
							procedures[a] = procedure[j];
							a++;
						}

					}

					if (resources.length == POSITION_PROCEDURES) {
						int j = 0;
						String[] newProcedures = new String[procedures.length];
						for (int i = 0; i < procedures.length; i++) {
							String[] newstring = procedures[i].split("sensors/");
							if (newstring.length == 2) {
								newProcedures[j] = newstring[1];
							} else {
								newProcedures[j] = newstring[0];
							}
							j++;

						}

						String htmlString = renderResourcesHtml(requestURL, servletPath, "Procedures", newProcedures);

						out.write(htmlString.getBytes());
					}

					// enough resources defined:
					if (resources.length >= LAST_POSITIONds | headerValue.contains("text/xml")) {
						// let's try to request the data:
						try {
							ParameterContainer paramCon = new ParameterContainer();

							// read out resources and use them as request
							// parameters:
							String[] url = requestURL.split("sensors/");
							String[] sensor = url[1].split("/");
							if (resources[POSITION_SOS].equals(sosName)) {
								paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_PROCEDURE_PARAMETER, proceduresIdPrefix + sensor[0]);
							} else {
								paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_PROCEDURE_PARAMETER, resources[POSITION_PROCEDURES]);
							}

							// hard-coded parameters:
							paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_RESPONSE_FORMAT_PARAMETER, "text/xml;subtype=\"om/" + serviceVersion + "\"");
							paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_SERVICE_PARAMETER, SOSAdapter.SERVICE_TYPE);
							paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_VERSION_PARAMETER, serviceVersion);
							if (serviceVersion.equals("0.0.0")) {
								paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_RESPONSE_MODE_PARAMETER, "inline");
							}
							// send DescribeSensor request:
							OperationResult opResult = adapter.doOperation(serviceDesc.getOperationsMetadata().getOperationByName(SOSAdapter.DESCRIBE_SENSOR), paramCon);

							// if the accepted MIME type is not set, open OM or RDF
							// else open data in the preferred MIME type
							if (!headerValue.contains("text/xml") && !headerValue.contains("image/jpeg") && !headerValue.contains("application/vnd.google-earth.kml+xml")) {
								if (requestURL.contains("OM")) {
									this.showResultsInOutputFormat("OM", opResult, response, paramCon, serviceVersion, requestURL);
								} else {
									String responseString = RDFEncoder.getSensorTriple(requestURL, sensor[0]);

									response.setContentType("application/rdf+xml");
									out.write(responseString.getBytes());
								}
							} else {
								this.showResultsInPreferredMIMEType(paramCon, opResult, serviceVersion, response, headerValue, requestURL);
							}

						} catch (ExceptionReport excReport) {
							out.write(excReport.toHtmlString().getBytes());
						} catch (Exception e) {
							throw new ServletException(e);
						}
					}
				}

				// GetFeatureOfInterest request
				if (resources[POSITION_RESOURCES].equalsIgnoreCase("fois")) {
					Contents contents = serviceDesc.getContents();
					String[] o = contents.getDataIdentificationIDArray();

					int countFOI = 0;
					for (int i = 0; i < o.length; i++) {
						ObservationOffering offering = (ObservationOffering) contents.getDataIdentification(o[i].toString());
						countFOI += offering.getFeatureOfInterest().length;
					}

					String[] fois = new String[countFOI];
					int a = 0;
					for (int i = 0; i < o.length; i++) {

						ObservationOffering offering = (ObservationOffering) contents.getDataIdentification(o[i].toString());

						if (offering == null) {
							throw new ServletException("Resource '/" + getResourcesString(requestURL, servletPath) + "' unavailable.");
						}

						String[] foi = offering.getFeatureOfInterest();
						for (int j = 0; j < foi.length; j++) {
							fois[a] = foi[j];
							a++;
						}
					}

					if (resources.length == POSITION_FOIS) {
						int j = 0;
						String[] newFois = new String[fois.length];
						for (int i = 0; i < fois.length; i++) {
							String[] newstring = fois[i].split("fois/");
							if (newstring.length == 2) {
								newFois[j] = newstring[1];
							} else {
								newFois[j] = newstring[0];
							}
							j++;

						}

						String htmlString = renderResourcesHtml(requestURL, servletPath, "Feature Of Interest", newFois);
						out.write(htmlString.getBytes());
					}

					// enough resources defined:
					if (resources.length >= LAST_POSITIONgfoi) {
						// let's try to request the data:
						try {
							ParameterContainer paramCon = new ParameterContainer();

							// read out resources and use them as request
							// parameters:
							String[] url = requestURL.split("fois/");
							String[] foi = url[1].split("/");
							if (resources[POSITION_SOS].equals(sosName)) {
								paramCon.addParameterShell(ISOSRequestBuilder.GET_FOI_ID_PARAMETER, foiID + foi[0]);
							} else {
								paramCon.addParameterShell(ISOSRequestBuilder.GET_FOI_ID_PARAMETER, resources[POSITION_FOIS]);
							}

							// hard-coded parameters:
							paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_RESPONSE_FORMAT_PARAMETER, "text/xml;subtype=\"om/" + serviceVersion + "\"");
							paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_SERVICE_PARAMETER, SOSAdapter.SERVICE_TYPE);
							paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_VERSION_PARAMETER, serviceVersion);
							if (serviceVersion.equals("0.0.0")) {
								paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_RESPONSE_MODE_PARAMETER, "inline");
							}

							OperationResult opResult = adapter.doOperation(serviceDesc.getOperationsMetadata().getOperationByName(SOSAdapter.GET_FEATURE_OF_INTEREST), paramCon);

							// if the accepted MIME type is not set, open OM or
							// RDF
							// else open data in the preferred MIME type
							if (!headerValue.contains("text/xml") && !headerValue.contains("image/jpeg") && !headerValue.contains("application/vnd.google-earth.kml+xml")) {
								if (requestURL.contains("OM")) {
									this.showResultsInOutputFormat("OM", opResult, response, paramCon, serviceVersion, requestURL);

								} else {
									String responseString = RDFEncoder.getFeatureTriple(requestURL, foi[0]);

									response.setContentType("application/rdf+xml");
									out.write(responseString.getBytes());
								}
							} else {
								this.showResultsInPreferredMIMEType(paramCon, opResult, serviceVersion, response, headerValue, requestURL);
							}

						} catch (ExceptionReport excReport) {
							out.write(excReport.toHtmlString().getBytes());
						} catch (Exception e) {
							throw new ServletException(e);
						}
					}

				}

				// GetObservations request
				if (resources[POSITION_RESOURCES].equalsIgnoreCase("observations")) {

					// if -> new URL schema
					// else -> old URL schema
					if (resources.length > POSITION_RESOURCES)

						if (requestURL.contains(KEY_IDS) || requestURL.contains(KEY_OFFERING) || requestURL.contains(KEY_FOI) || requestURL.contains(KEY_OBSPROP) || requestURL.contains(KEY_PROCEDURES) || requestURL.contains(KEY_TIMES) || requestURL.contains(KEY_RESULTMODEL)
								|| requestURL.contains(KEY_FOI.toLowerCase()) || requestURL.contains(KEY_OBSPROP.toLowerCase()) || requestURL.contains(KEY_TIMES.toLowerCase()) || requestURL.contains(KEY_RESULTMODEL.toLowerCase())) {

							this.newURLSchema(requestURL, resources, adapter, servletPath, serviceDesc, serviceVersion, response, headerValue);

						} else {

							this.oldURLSchema(requestURL, resources, adapter, servletPath, serviceDesc, serviceVersion, response, headerValue);

						}
				}// end GetObservations request
			}
		}
		out.flush();
		out.close();
	}

	/**
	 * loads the SOS URLs from the properties file. All the URLs point to
	 * services which conform to the OGC SOS 1.0 specification.
	 * 
	 * @throws IOException
	 */
	public String[][] loadURLs() throws IOException {
		Properties properties = new Properties();

		properties.load(IServiceAdapter.class.getResourceAsStream("/sosURLs.properties"));

		int count = properties.size();
		String[][] resArray = new String[3][count];

		Enumeration propKeys = properties.keys();
		int i = 0;
		while (propKeys.hasMoreElements()) {
			String key = (String) propKeys.nextElement();
			String url = (String) properties.get(key);

			resArray[0][i] = key;
			resArray[1][i] = url;
			resArray[2][i] = "1.0.0";

			i++;
		}

		return resArray;
	}

	/**
	 * search the preferred MIME type
	 * 
	 * @param headerValue
	 *            : String with accepted MIME types
	 * @return a String with the preferred MIME type
	 */
	private String getPreferredMIMEtype(String headerValue) {
		String preferredMIMEtype = "";
		String[][] header = splitHeader(headerValue);
		double q = 0.0;
		for (int i = 0; i < header.length; i++) {
			// if(header[i][0].toString().equals("text/html")|| //to test
			if (header[i][0].toString().equals("text/xml") || header[i][0].toString().equals("image/jpeg") || header[i][0].toString().equals("application/vnd.google-earth.kml+xml")) {

				if (q < Double.valueOf(header[i][1].toString())) {
					preferredMIMEtype = header[i][0].toString();
					q = Double.valueOf(header[i][1].toString());
				}
			}
		}
		return preferredMIMEtype;
	}

	/**
	 * split the headerValue in a String [][] with MIME type and
	 * accept-parameter (q)
	 * 
	 * @param headerValue
	 *            : String with accepted MIME types
	 * @return String [][] with MIME type and accept-parameter (q)
	 */
	private String[][] splitHeader(String headerValue) {
		String[] headerV = headerValue.split(",");
		String[][] header = new String[headerV.length][2];
		for (int i = 0; i < headerV.length; i++) {

			if (headerV[i].contains(";q=")) {
				Pattern p = Pattern.compile(";q=");
				String[] list = p.split(headerV[i]);

				header[i][0] = list[0].trim();
				header[i][1] = list[1];
			} else {
				header[i][0] = headerV[i].trim();
				header[i][1] = "1.0";
			}
		}
		return header;
	}

	/**
	 * 
	 * shows results of GetObservation request, if the OutputFormat is defined
	 * in the URL request
	 * 
	 * @param outputFormat
	 * @param opResult
	 * @param response
	 * @param paramCon
	 * @param serviceVersion
	 * @param requestURL
	 * 
	 * @throws IOException
	 * @throws ServletException
	 */
	private void showResultsInOutputFormat(String outputFormat, OperationResult opResult, HttpServletResponse response, ParameterContainer paramCon, String serviceVersion, String requestURL) throws IOException, ServletException {
		try {
			OutputStream out = response.getOutputStream();
			if (outputFormat.equals("OM")) {
				response.setContentType("text/xml");
				out.write(opResult.getIncomingResult());
			} else if (outputFormat.equals("DIAGRAM")) {
				IFeatureStore featureStore = new SOSObservationStore();
				IChartRenderer renderer = new TimeSeriesChartRenderer4xPhenomenons();
				OXFFeatureCollection featureColl = featureStore.unmarshalFeatures(opResult);
				BufferedImage chartImage = (BufferedImage) renderer.renderChart(featureColl, paramCon, 500, 500).getRendering();

				// write image to output stream:
				response.setContentType("image/jpeg");
				JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
				JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(chartImage);
				param.setQuality(1, false);
				encoder.encode(chartImage, param);
			} else if (outputFormat.equals("KML")) {
				String diagramRequestURL = requestURL.replace("KML", "DIAGRAM");

				String xslFile = "om2kml.xsl";
				if (serviceVersion.equals("0.0.0")) {
					xslFile = "om2kml_00.xsl";
				}

				InputStream xslInput = RestSosFrontend.class.getResourceAsStream(xslFile);
				String xslScript = IOHelper.readText(xslInput);
				xslScript = xslScript.replace("@DIAGRAM_URL@", diagramRequestURL + "/diagram.jpg");

				response.setContentType("application/vnd.google-earth.kml+xml");
				XSLTTransformer.transform(new String(opResult.getIncomingResult()), xslScript, out);

			} else if (outputFormat.equals("RDF")) {

				// mime type: application/rdf+xml

				IFeatureStore featureStore = new SOSObservationStore();

				OXFFeatureCollection observationColl = featureStore.unmarshalFeatures(opResult);

				String responseString = RDFEncoder.getObsColTriples(observationColl, requestURL);

				response.setContentType("application/rdf+xml");
				out.write(responseString.getBytes());
			}
		} catch (Exception e) {
			throw new ServletException(e);
		}

	}

	/**
	 * 
	 * shows results of GetObservation request, if the preferred MIME is set
	 * 
	 * @param resources
	 * @param adapter
	 * @param serviceDesc
	 * @param serviceVersion
	 * @param response
	 * @param headerValue
	 * @param requestURL
	 * @throws IOException
	 * @throws ServletException
	 */
	private void showResultsInPreferredMIMEType(ParameterContainer paramCon, OperationResult opResult, String serviceVersion, HttpServletResponse response, String headerValue, String requestURL) throws IOException, ServletException {
		OutputStream out = response.getOutputStream();
		try {

			String preferredMIMEype = getPreferredMIMEtype(headerValue);

			// if(preferredMIMEype.equals("image/jpeg")){ //to test
			if (preferredMIMEype.equals("text/xml")) {

				response.setContentType("text/xml");
				out.write(opResult.getIncomingResult());
			} else if (preferredMIMEype.equals("image/jpeg")) {

				IFeatureStore featureStore = new SOSObservationStore();
				IChartRenderer renderer = new TimeSeriesChartRenderer4xPhenomenons();
				OXFFeatureCollection featureColl = featureStore.unmarshalFeatures(opResult);
				BufferedImage chartImage = (BufferedImage) renderer.renderChart(featureColl, paramCon, 500, 500).getRendering();

				// write image to output stream:
				response.setContentType("image/jpeg");
				JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
				JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(chartImage);
				param.setQuality(1, false);
				encoder.encode(chartImage, param);
				// }else if(preferredMIMEype.equals("image/jpeg")){ //to test
			} else if (preferredMIMEype.equals("application/vnd.google-earth.kml+xml")) {

				String diagramRequestURL = requestURL.replace("KML", "DIAGRAM");

				String xslFile = "om2kml.xsl";
				if (serviceVersion.equals("0.0.0")) {
					xslFile = "om2kml_00.xsl";
				}

				InputStream xslInput = RestSosFrontend.class.getResourceAsStream(xslFile);
				String xslScript = IOHelper.readText(xslInput);
				xslScript = xslScript.replace("@DIAGRAM_URL@", diagramRequestURL + "/diagram.jpg");

				response.setContentType("application/vnd.google-earth.kml+xml");
				XSLTTransformer.transform(new String(opResult.getIncomingResult()), xslScript, out);
			}
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	/**
	 * 
	 * old URL schema for GetObservation request:
	 * 
	 * .../Offering/FeatureOfInterest/ObservedProperty/BeginTimeYear/
	 * BeginTimeMonth/BeginTimeDay/BeginTimeHour/
	 * BeginTimeMinute/BeginTimeSecond
	 * //BeginTimeTimeZone/EndTimeYear/EndTimeMonth/EndTimeDay/EndTimeHour/
	 * EndTimeMinute/EndTimeSecond//EndTimeTimeZone/ResultModel
	 * 
	 * .../Offering/FeatureOfInterest/ObservedProperty/BeginTimeYear/
	 * BeginTimeMonth/BeginTimeDay/BeginTimeHour/
	 * BeginTimeMinute/BeginTimeSecond
	 * //BeginTimeTimeZone/EndTimeYear/EndTimeMonth/EndTimeDay/EndTimeHour/
	 * EndTimeMinute/EndTimeSecond//EndTimeTimeZone/ResultModel/OutputFormat
	 * 
	 * @param requestURL
	 * @param resources
	 * @param adapter
	 * @param servletPath
	 * @param serviceDesc
	 * @param serviceVersion
	 * @param response
	 * @param headerValue
	 * @throws IOException
	 * @throws ServletException
	 */
	private void oldURLSchema(String requestURL, String[] resources, SOSAdapter adapter, String servletPath, ServiceDescriptor serviceDesc, String serviceVersion, HttpServletResponse response, String headerValue) throws IOException, ServletException {
		Contents contents = serviceDesc.getContents();
		OutputStream out = response.getOutputStream();
		// OFFERING resource:
		if (resources.length == POSITION_OFFERING) {

			String htmlString = renderResourcesHtml(requestURL, servletPath, "Offering", contents.getDataIdentificationIDArray());

			out.write(htmlString.getBytes());

		}
		// OFFERING chosen:
		else {

			ObservationOffering chosenOffering = (ObservationOffering) contents.getDataIdentification(resources[POSITION_OFFERING]);

			if (chosenOffering == null) {
				throw new ServletException("Resource '/" + getResourcesString(requestURL, servletPath) + "' unavailable.");
			}

			// OBSERVED_PROPERTY resource: 
			else if (resources.length == POSITION_OBSERVED_PROPERTY) {

				int j = 0;
				String[] newOP = new String[chosenOffering.getObservedProperties().length];
				for (int i = 0; i < chosenOffering.getObservedProperties().length; i++) {

					String[] newstring = chosenOffering.getObservedProperties()[i].split("OGC/");
					if (newstring.length == 2) {
						newOP[j] = newstring[1];
					} else {
						newOP[j] = newstring[0];
					}
					j++;
				}

				String htmlString = renderResourcesHtml(requestURL, servletPath, "Observed Property", newOP);
				out.write(htmlString.getBytes());
			}

			// FOI resource:
			else if (resources.length == POSITION_FOI) {

				int j = 0;
				String[] newFois = new String[chosenOffering.getFeatureOfInterest().length];
				for (int i = 0; i < chosenOffering.getFeatureOfInterest().length; i++) {

					String[] newstring = chosenOffering.getFeatureOfInterest()[i].split("fois/");
					if (newstring.length == 2) {
						newFois[j] = newstring[1];
					} else {
						newFois[j] = newstring[0];
					}
					j++;
				}

				String htmlString = renderResourcesHtml(requestURL, servletPath, "Feature Of Interest", newFois);

				out.write(htmlString.getBytes());
			}

			// BEGIN_TIME resource:
			// YEAR
			else if (resources.length == POSITION_BEGIN_YEAR) {
				ITimePeriod timePeriod = (ITimePeriod) chosenOffering.getTemporalDomain().getPossibleValues().get(0);

				String[] list = timePeriod.getStart().toISO8601Format().split("[-T/:]");

				String htmlString = renderResourcesHtml(requestURL, servletPath, "Begin Time Year", new String[] { list[0] });
				out.write(htmlString.getBytes());
			}

			// BEGIN_TIME resource:
			// MONTH
			else if (resources.length == POSITION_BEGIN_MONTH) {
				ITimePeriod timePeriod = (ITimePeriod) chosenOffering.getTemporalDomain().getPossibleValues().get(0);

				String[] list = timePeriod.getStart().toISO8601Format().split("[-T/:]");

				String htmlString = renderResourcesHtml(requestURL, servletPath, "Begin Time Month", new String[] { list[1] });
				out.write(htmlString.getBytes());
			}

			// BEGIN_TIME resource:
			// DAY
			else if (resources.length == POSITION_BEGIN_DAY) {
				ITimePeriod timePeriod = (ITimePeriod) chosenOffering.getTemporalDomain().getPossibleValues().get(0);

				Pattern p = Pattern.compile("[-T/:]");

				String[] list = p.split(timePeriod.getStart().toISO8601Format());

				String htmlString = renderResourcesHtml(requestURL, servletPath, "Begin Time Day", new String[] { list[2] });
				out.write(htmlString.getBytes());
			}

			// BEGIN_TIME resource:
			// HOUR
			else if (resources.length == POSITION_BEGIN_HOUR) {
				ITimePeriod timePeriod = (ITimePeriod) chosenOffering.getTemporalDomain().getPossibleValues().get(0);

				Pattern p = Pattern.compile("[-T/:]");

				String[] list = p.split(timePeriod.getStart().toISO8601Format());

				String htmlString = renderResourcesHtml(requestURL, servletPath, "Begin Time Hour", new String[] { list[3] });
				out.write(htmlString.getBytes());
			}

			// BEGIN_TIME resource:
			// MINUTE
			else if (resources.length == POSITION_BEGIN_MINUTE) {
				ITimePeriod timePeriod = (ITimePeriod) chosenOffering.getTemporalDomain().getPossibleValues().get(0);

				Pattern p = Pattern.compile("[-T/:]");

				String[] list = p.split(timePeriod.getStart().toISO8601Format());

				String htmlString = renderResourcesHtml(requestURL, servletPath, "Begin Time Minute", new String[] { list[4] });
				out.write(htmlString.getBytes());
			}

			// BEGIN_TIME resource:
			// SECOND
			else if (resources.length == POSITION_BEGIN_SECOND) {
				ITimePeriod timePeriod = (ITimePeriod) chosenOffering.getTemporalDomain().getPossibleValues().get(0);

				Pattern p = Pattern.compile("[-T+/:]");

				String[] list = p.split(timePeriod.getStart().toISO8601Format());

				String htmlString = renderResourcesHtml(requestURL, servletPath, "Begin Time Second", new String[] { list[5] });
				out.write(htmlString.getBytes());
			}

			// BEGIN_TIME resource:
			// TIMEZONE
			else if (resources.length == POSITION_BEGIN_TIMEZONE) {
				ITimePeriod timePeriod = (ITimePeriod) chosenOffering.getTemporalDomain().getPossibleValues().get(0);

				Pattern p = Pattern.compile("[-T+/:]");

				String[] list = p.split(timePeriod.getStart().toISO8601Format());

				String htmlString = renderResourcesHtml(requestURL, servletPath, "Begin Time Time Zone", new String[] { list[6] });
				out.write(htmlString.getBytes());
			}

			// END_TIME resource:
			// YEAR
			else if (resources.length == POSITION_END_YEAR) {
				ITimePeriod timePeriod = (ITimePeriod) chosenOffering.getTemporalDomain().getPossibleValues().get(0);

				String[] list = timePeriod.getEnd().toISO8601Format().split("[-T/:]");

				String htmlString = renderResourcesHtml(requestURL, servletPath, "End Time Year", new String[] { list[0] });
				out.write(htmlString.getBytes());
			}

			// END_TIME resource:
			// MONTH
			else if (resources.length == POSITION_END_MONTH) {
				ITimePeriod timePeriod = (ITimePeriod) chosenOffering.getTemporalDomain().getPossibleValues().get(0);

				String[] list = timePeriod.getEnd().toISO8601Format().split("[-T/:]");

				String htmlString = renderResourcesHtml(requestURL, servletPath, "End Time Month", new String[] { list[1] });
				out.write(htmlString.getBytes());
			}

			// END_TIME resource:
			// DAY
			else if (resources.length == POSITION_END_DAY) {
				ITimePeriod timePeriod = (ITimePeriod) chosenOffering.getTemporalDomain().getPossibleValues().get(0);

				Pattern p = Pattern.compile("[-T/:]");

				String[] list = p.split(timePeriod.getEnd().toISO8601Format());

				String htmlString = renderResourcesHtml(requestURL, servletPath, "End Time Day", new String[] { list[2] });
				out.write(htmlString.getBytes());
			}

			// END_TIME resource:
			// HOUR
			else if (resources.length == POSITION_END_HOUR) {
				ITimePeriod timePeriod = (ITimePeriod) chosenOffering.getTemporalDomain().getPossibleValues().get(0);

				Pattern p = Pattern.compile("[-T/:]");

				String[] list = p.split(timePeriod.getEnd().toISO8601Format());

				String htmlString = renderResourcesHtml(requestURL, servletPath, "End Time Hour", new String[] { list[3] });
				out.write(htmlString.getBytes());
			}

			// END_TIME resource:
			// MINUTE
			else if (resources.length == POSITION_END_MINUTE) {
				ITimePeriod timePeriod = (ITimePeriod) chosenOffering.getTemporalDomain().getPossibleValues().get(0);

				Pattern p = Pattern.compile("[-T/:]");

				String[] list = p.split(timePeriod.getEnd().toISO8601Format());

				String htmlString = renderResourcesHtml(requestURL, servletPath, "End Time Minute", new String[] { list[4] });
				out.write(htmlString.getBytes());
			}

			// END_TIME resource:
			// SECOND
			else if (resources.length == POSITION_END_SECOND) {
				ITimePeriod timePeriod = (ITimePeriod) chosenOffering.getTemporalDomain().getPossibleValues().get(0);

				Pattern p = Pattern.compile("[-T+/:]");

				String[] list = p.split(timePeriod.getEnd().toISO8601Format());

				String htmlString = renderResourcesHtml(requestURL, servletPath, "End Time Second", new String[] { list[5] });
				out.write(htmlString.getBytes());
			}

			// END_TIME resource:
			// TIMEZONE
			else if (resources.length == POSITION_END_TIMEZONE) {
				ITimePeriod timePeriod = (ITimePeriod) chosenOffering.getTemporalDomain().getPossibleValues().get(0);

				Pattern p = Pattern.compile("[-T+/:]");

				String[] list = p.split(timePeriod.getEnd().toISO8601Format());

				String htmlString = renderResourcesHtml(requestURL, servletPath, "End Time Time Zone", new String[] { list[6] });
				out.write(htmlString.getBytes());
			}

			// RESULT_MODEL resource
			else if (resources.length == POSITION_RESULT_MODEL) {
				String htmlString = renderResourcesHtml(requestURL, servletPath, "Result Model", chosenOffering.getResultModels());

				out.write(htmlString.getBytes());
			}

			// if the accepted MIME type is not set,define OUTPUT_FORMAT
			// resource
			// else open data in the preferred MIME type
			else if (resources.length == POSITION_OUTPUT_FORMAT) {
				// if(!headerValue.contains("text/html")&& //to test
				if (!headerValue.contains("text/xml") && !headerValue.contains("image/jpeg") && !headerValue.contains("application/vnd.google-earth.kml+xml")) {
					String htmlString = renderResourcesHtml(requestURL, servletPath, "Output Format", myOutputFormats);
					out.write(htmlString.getBytes());
				} else {
					try {
						ParameterContainer paramCon = getObsParamCon(resources, serviceVersion, serviceDesc);
						// send GetObservation request:
						OperationResult opResult = adapter.doOperation(serviceDesc.getOperationsMetadata().getOperationByName(SOSAdapter.GET_OBSERVATION), paramCon);
						this.showResultsInPreferredMIMEType(paramCon, opResult, serviceVersion, response, headerValue, requestURL);
					} catch (ExceptionReport excReport) {
						out.write(excReport.toHtmlString().getBytes());
					} catch (Exception e) {
						throw new ServletException(e);
					}

				}
			}

			// enough resources defined (if the accepted MIME type don't set in
			// the header), set the MIME type in the URL:
			else if (resources.length >= LAST_POSITION + 1) {
				// let's try to request the data:
				try {
					ParameterContainer paramCon = getObsParamCon(resources, serviceVersion, serviceDesc);

					// send GetObservation request:
					OperationResult opResult = adapter.doOperation(serviceDesc.getOperationsMetadata().getOperationByName(SOSAdapter.GET_OBSERVATION), paramCon);

					// show GetObservation results in defined OUTPUT_FORMAT
					this.showResultsInOutputFormat(resources[POSITION_OUTPUT_FORMAT], opResult, response, paramCon, serviceVersion, requestURL);

				} catch (ExceptionReport excReport) {
					out.write(excReport.toHtmlString().getBytes());
				} catch (Exception e) {
					throw new ServletException(e);
				}
			}
			// // TOO MUCH defined:
			// else {
			// throw new ServletException("Resource '/" +
			// getResourcesString(requestURL, servletPath)
			// + "' is unavailable.");
			// }
		}
	}

	/**
	 * 
	 * read resources and create a ParameterContainer for the GetObservations
	 * request (old URL schema)
	 * 
	 * @param resources
	 *            in the URL
	 * @param serviceVersion
	 *            of the SOS
	 * @return ParameterContainer for the GetObservations request
	 * @throws ServletException
	 * @throws IOException
	 */
	private ParameterContainer getObsParamCon(String[] resources, String serviceVersion, ServiceDescriptor serviceDesc) throws ServletException, IOException {
		ParameterContainer paramCon = new ParameterContainer();

		try {
			// read out resources and use them as request parameters:
			paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_OFFERING_PARAMETER, resources[POSITION_OFFERING]);

			Contents contents = serviceDesc.getContents();
			ObservationOffering offering = (ObservationOffering) contents.getDataIdentification(resources[POSITION_OFFERING]);
			String[] observedPro = offering.getObservedProperties();

			// wildcard "-" for FEATURE_OF_INTEREST_PARAMETER and/or
			// OBSERVED_PROPERTY_PARAMETER
			if (resources[POSITION_FOI].equals("-") && resources[POSITION_OBSERVED_PROPERTY].equals("-")) {
				paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_OBSERVED_PROPERTY_PARAMETER, observedPro);
			} else if (resources[POSITION_FOI].equals("-")) {
				if (resources[POSITION_SOS].equals(sosName))
					paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_OBSERVED_PROPERTY_PARAMETER, PhenomenonId + resources[POSITION_OBSERVED_PROPERTY]);
				else
					paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_OBSERVED_PROPERTY_PARAMETER, resources[POSITION_OBSERVED_PROPERTY]);
			} else if (resources[POSITION_OBSERVED_PROPERTY].equals("-")) {
				if (resources[POSITION_SOS].equals(sosName))
					paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_FEATURE_OF_INTEREST_PARAMETER, foiID + resources[POSITION_FOI]);
				else
					paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_FEATURE_OF_INTEREST_PARAMETER, resources[POSITION_FOI]);
				paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_OBSERVED_PROPERTY_PARAMETER, observedPro);
			} else {
				if (resources[POSITION_SOS].equals(sosName)) {
					paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_FEATURE_OF_INTEREST_PARAMETER, foiID + resources[POSITION_FOI]);

					paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_OBSERVED_PROPERTY_PARAMETER, PhenomenonId + resources[POSITION_OBSERVED_PROPERTY]);
				} else {
					paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_FEATURE_OF_INTEREST_PARAMETER, resources[POSITION_FOI]);

					paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_OBSERVED_PROPERTY_PARAMETER, resources[POSITION_OBSERVED_PROPERTY]);
				}
			}

			// .../BEGIN_YEAR/-/-/-/-/-/-/
			// END_YEAR/-/-/-/-/-/-/...
			if (resources[POSITION_BEGIN_MONTH].equals("-") && resources[POSITION_END_MONTH].equals("-")) {
				paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_EVENT_TIME_PARAMETER, resources[POSITION_BEGIN_YEAR] + "/" + resources[POSITION_END_YEAR]);
			}
			// .../BEGIN_YEAR/-/-/-/-/-/-/
			// END_YEAR/END_MONTH/.../.../.../.../.../...
			else if (resources[POSITION_BEGIN_MONTH].equals("-")) {
				// .../BEGIN_YEAR/-/-/-/-/-/-/
				// END_YEAR/END_MONTH/-/-/-/-/-/...
				if (resources[POSITION_END_DAY].equals("-")) {
					paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_EVENT_TIME_PARAMETER, resources[POSITION_BEGIN_YEAR] + "/" + resources[POSITION_END_YEAR] + "-" + resources[POSITION_END_MONTH]);
				}
				// .../BEGIN_YEAR/-/-/-/-/-/-/
				// END_YEAR/END_MONTH/END_DAY/-/-/-/-/...
				else if (resources[POSITION_END_HOUR].equals("-")) {
					paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_EVENT_TIME_PARAMETER, resources[POSITION_BEGIN_YEAR] + "/" + resources[POSITION_END_YEAR] + "-" + resources[POSITION_END_MONTH] + "-" + resources[POSITION_END_DAY]);
				}
				// .../BEGIN_YEAR/-/-/-/-/-/-/
				// END_YEAR/END_MONTH/END_DAY/END_HOUR/-/-/-/...
				else if (resources[POSITION_END_MINUTE].equals("-")) {
					paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_EVENT_TIME_PARAMETER, resources[POSITION_BEGIN_YEAR] + "/" + resources[POSITION_END_YEAR] + "-" + resources[POSITION_END_MONTH] + "-" + resources[POSITION_END_DAY] + "T" + resources[POSITION_END_HOUR]);
				}
				// .../BEGIN_YEAR/-/-/-/-/-/-/
				// END_YEAR/END_MONTH/END_DAY/END_HOUR/END_MINUTE/-/-/...
				else if (resources[POSITION_END_SECOND].equals("-")) {
					paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_EVENT_TIME_PARAMETER, resources[POSITION_BEGIN_YEAR] + "/" + resources[POSITION_END_YEAR] + "-" + resources[POSITION_END_MONTH] + "-" + resources[POSITION_END_DAY] + "T" + resources[POSITION_END_HOUR] + ":"
							+ resources[POSITION_END_MINUTE]);
				}
				// .../BEGIN_YEAR/-/-/-/-/-/-/
				// END_YEAR/END_MONTH/END_DAY/END_HOUR/END_MINUTE/END_SECOND/-/...
				else if (resources[POSITION_END_TIMEZONE].equals("-")) {
					paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_EVENT_TIME_PARAMETER, resources[POSITION_BEGIN_YEAR] + "/" + resources[POSITION_END_YEAR] + "-" + resources[POSITION_END_MONTH] + "-" + resources[POSITION_END_DAY] + "T" + resources[POSITION_END_HOUR] + ":"
							+ resources[POSITION_END_MINUTE] + ":" + resources[POSITION_END_SECOND]);
				}
				// .../BEGIN_YEAR/-/-/-/-/-/-/
				// END_YEAR/END_MONTH/END_DAY/END_HOUR/END_MINUTE/END_SECOND/END_TIMEZONE/...
				else {
					paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_EVENT_TIME_PARAMETER, resources[POSITION_BEGIN_YEAR] + "/" + resources[POSITION_END_YEAR] + "-" + resources[POSITION_END_MONTH] + "-" + resources[POSITION_END_DAY] + "T" + resources[POSITION_END_HOUR] + ":"
							+ resources[POSITION_END_MINUTE] + ":" + resources[POSITION_END_SECOND] + "+" + resources[POSITION_END_TIMEZONE]);
				}
			}
			// .../BEGIN_YEAR/BEGIN_MONTH/.../.../.../.../.../ 
			// END_YEAR/-/-/-/-/-/-/...
			else if (resources[POSITION_END_MONTH].equals("-")) {
				// .../BEGIN_YEAR/BEGIN_MONTH/-/-/-/-/-/
				// END_YEAR/-/-/-/-/-/-/...
				if (resources[POSITION_BEGIN_DAY].equals("-")) {
					paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_EVENT_TIME_PARAMETER, resources[POSITION_BEGIN_YEAR] + "-" + resources[POSITION_BEGIN_MONTH] + "/" + resources[POSITION_END_YEAR]);
				}
				// .../BEGIN_YEAR/BEGIN_MONTH/BEGIN_DAY/-/-/-/-/
				// END_YEAR/-/-/-/-/-/-/...
				else if (resources[POSITION_BEGIN_HOUR].equals("-")) {
					paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_EVENT_TIME_PARAMETER, resources[POSITION_BEGIN_YEAR] + "-" + resources[POSITION_BEGIN_MONTH] + "-" + resources[POSITION_BEGIN_DAY] + "/" + resources[POSITION_END_YEAR]);
				}
				// .../BEGIN_YEAR/BEGIN_MONTH/BEGIN_DAY/BEGIN_HOUR/-/-/-/
				// END_YEAR/-/-/-/-/-/-/...
				else if (resources[POSITION_BEGIN_MINUTE].equals("-")) {
					paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_EVENT_TIME_PARAMETER, resources[POSITION_BEGIN_YEAR] + "-" + resources[POSITION_BEGIN_MONTH] + "-" + resources[POSITION_BEGIN_DAY] + "T" + resources[POSITION_BEGIN_HOUR] + "/" + resources[POSITION_END_YEAR]);
				}
				// .../BEGIN_YEAR/BEGIN_MONTH/BEGIN_DAY/BEGIN_HOUR/BEGIN_MINUTE/-/-/
				// END_YEAR/-/-/-/-/-/-/...
				else if (resources[POSITION_BEGIN_SECOND].equals("-")) {
					paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_EVENT_TIME_PARAMETER, resources[POSITION_BEGIN_YEAR] + "-" + resources[POSITION_BEGIN_MONTH] + "-" + resources[POSITION_BEGIN_DAY] + "T" + resources[POSITION_BEGIN_HOUR] + ":" + resources[POSITION_BEGIN_MINUTE] + "/"
							+ resources[POSITION_END_YEAR]);
				}
				// .../BEGIN_YEAR/BEGIN_MONTH/BEGIN_DAY/BEGIN_HOUR/BEGIN_MINUTE/BEGIN_SECOND/-/
				// END_YEAR/-/-/-/-/-/-/...
				else if (resources[POSITION_BEGIN_TIMEZONE].equals("-")) {
					paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_EVENT_TIME_PARAMETER, resources[POSITION_BEGIN_YEAR] + "-" + resources[POSITION_BEGIN_MONTH] + "-" + resources[POSITION_BEGIN_DAY] + "T" + resources[POSITION_BEGIN_HOUR] + ":" + resources[POSITION_BEGIN_MINUTE] + ":"
							+ resources[POSITION_BEGIN_SECOND] + "/" + resources[POSITION_END_YEAR]);
				}
				// .../BEGIN_YEAR/BEGIN_MONTH/BEGIN_DAY/BEGIN_HOUR/BEGIN_MINUTE/BEGIN_SECOND/BEGIN_TIMEZONE/
				// END_YEAR/-/-/-/-/-/-/...
				else {
					paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_EVENT_TIME_PARAMETER, resources[POSITION_BEGIN_YEAR] + "-" + resources[POSITION_BEGIN_MONTH] + "-" + resources[POSITION_BEGIN_DAY] + "T" + resources[POSITION_BEGIN_HOUR] + ":" + resources[POSITION_BEGIN_MINUTE] + ":"
							+ resources[POSITION_BEGIN_SECOND] + "+" + resources[POSITION_BEGIN_TIMEZONE] + "/" + resources[POSITION_END_YEAR]);
				}
			}
			// .../BEGIN_YEAR/BEGIN_MONTH/-/-/-/-/-/
			// END_YEAR/END_MONTH/-/-/-/-/-/...
			else if (resources[POSITION_BEGIN_DAY].equals("-") && resources[POSITION_END_DAY].equals("-")) {
				paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_EVENT_TIME_PARAMETER, resources[POSITION_BEGIN_YEAR] + "-" + resources[POSITION_BEGIN_MONTH] + "/" + resources[POSITION_END_YEAR] + "-" + resources[POSITION_END_MONTH]);
			}
			// .../BEGIN_YEAR/BEGIN_MONTH/-/-/-/-/-/
			// END_YEAR/END_MONTH/END_DAY/.../.../.../.../...
			else if (resources[POSITION_BEGIN_DAY].equals("-")) {
				// .../BEGIN_YEAR/BEGIN_MONTH/-/-/-/-/-/
				// END_YEAR/END_MONTH/END_DAY/-/-/-/-/...
				if (resources[POSITION_END_HOUR].equals("-")) {
					paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_EVENT_TIME_PARAMETER, resources[POSITION_BEGIN_YEAR] + "-" + resources[POSITION_BEGIN_MONTH] + "/" + resources[POSITION_END_YEAR] + "-" + resources[POSITION_END_MONTH] + "-" + resources[POSITION_END_DAY]);
				}
				// .../BEGIN_YEAR/BEGIN_MONTH/-/-/-/-/-/
				// END_YEAR/END_MONTH/END_DAY/END_HOUR/-/-/-/...
				else if (resources[POSITION_END_MINUTE].equals("-")) {
					paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_EVENT_TIME_PARAMETER, resources[POSITION_BEGIN_YEAR] + "-" + resources[POSITION_BEGIN_MONTH] + "/" + resources[POSITION_END_YEAR] + "-" + resources[POSITION_END_MONTH] + "-" + resources[POSITION_END_DAY] + "T"
							+ resources[POSITION_END_HOUR]);
				}
				// .../BEGIN_YEAR/BEGIN_MONTH/-/-/-/-/-/
				// END_YEAR/END_MONTH/END_DAY/END_HOUR/END_MINUTE/-/-/...
				else if (resources[POSITION_END_SECOND].equals("-")) {
					paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_EVENT_TIME_PARAMETER, resources[POSITION_BEGIN_YEAR] + "-" + resources[POSITION_BEGIN_MONTH] + "/" + resources[POSITION_END_YEAR] + "-" + resources[POSITION_END_MONTH] + "-" + resources[POSITION_END_DAY] + "T"
							+ resources[POSITION_END_HOUR] + ":" + resources[POSITION_END_MINUTE]);
				}
				// .../BEGIN_YEAR/BEGIN_MONTH/-/-/-/-/-/
				// END_YEAR/END_MONTH/END_DAY/END_HOUR/END_MINUTE/END_SECOND/-/...
				else if (resources[POSITION_END_TIMEZONE].equals("-")) {
					paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_EVENT_TIME_PARAMETER, resources[POSITION_BEGIN_YEAR] + "-" + resources[POSITION_BEGIN_MONTH] + "/" + resources[POSITION_END_YEAR] + "-" + resources[POSITION_END_MONTH] + "-" + resources[POSITION_END_DAY] + "T"
							+ resources[POSITION_END_HOUR] + ":" + resources[POSITION_END_MINUTE] + ":" + resources[POSITION_END_SECOND]);
				}
				// .../BEGIN_YEAR/BEGIN_MONTH/-/-/-/-/-/
				// END_YEAR/END_MONTH/END_DAY/END_HOUR/END_MINUTE/END_SECOND/END_TIMEZONE/...
				else {
					paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_EVENT_TIME_PARAMETER, resources[POSITION_BEGIN_YEAR] + "-" + resources[POSITION_BEGIN_MONTH] + "/" + resources[POSITION_END_YEAR] + "-" + resources[POSITION_END_MONTH] + "-" + resources[POSITION_END_DAY] + "T"
							+ resources[POSITION_END_HOUR] + ":" + resources[POSITION_END_MINUTE] + ":" + resources[POSITION_END_SECOND] + "+" + resources[POSITION_END_TIMEZONE]);
				}
			}
			// .../BEGIN_YEAR/BEGIN_MONTH/.../.../.../.../.../
			// END_YEAR/END_MONTH/-/-/-/-/-/...
			else if (resources[POSITION_END_DAY].equals("-")) {
				// .../BEGIN_YEAR/BEGIN_MONTH/BEGIN_DAY/-/-/-/-/
				// END_YEAR/END_MONTH/-/-/-/-/-/...
				if (resources[POSITION_BEGIN_HOUR].equals("-")) {
					paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_EVENT_TIME_PARAMETER, resources[POSITION_BEGIN_YEAR] + "-" + resources[POSITION_BEGIN_MONTH] + "-" + resources[POSITION_BEGIN_DAY] + "/" + resources[POSITION_END_YEAR] + "-" + resources[POSITION_END_MONTH]);
				}
				// .../BEGIN_YEAR/BEGIN_MONTH/BEGIN_DAY/BEGIN_HOUR/-/-/-/
				// END_YEAR/END_MONTH/-/-/-/-/-/...
				else if (resources[POSITION_BEGIN_MINUTE].equals("-")) {
					paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_EVENT_TIME_PARAMETER, resources[POSITION_BEGIN_YEAR] + "-" + resources[POSITION_BEGIN_MONTH] + "-" + resources[POSITION_BEGIN_DAY] + "T" + resources[POSITION_BEGIN_HOUR] + "/" + resources[POSITION_END_YEAR] + "-"
							+ resources[POSITION_END_MONTH]);
				}
				// .../BEGIN_YEAR/BEGIN_MONTH/BEGIN_DAY/BEGIN_HOUR/BEGIN_MINUTE/-/-/
				// END_YEAR/END_MONTH/-/-/-/-/-/...
				else if (resources[POSITION_BEGIN_SECOND].equals("-")) {
					paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_EVENT_TIME_PARAMETER, resources[POSITION_BEGIN_YEAR] + "-" + resources[POSITION_BEGIN_MONTH] + "-" + resources[POSITION_BEGIN_DAY] + "T" + resources[POSITION_BEGIN_HOUR] + ":" + resources[POSITION_BEGIN_MINUTE] + "/"
							+ resources[POSITION_END_YEAR] + "-" + resources[POSITION_END_MONTH]);
				}
				// .../BEGIN_YEAR/BEGIN_MONTH/BEGIN_DAY/BEGIN_HOUR/BEGIN_MINUTE/BEGIN_SECOND/-/
				// END_YEAR/END_MONTH/-/-/-/-/-/...
				else if (resources[POSITION_BEGIN_TIMEZONE].equals("-")) {
					paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_EVENT_TIME_PARAMETER, resources[POSITION_BEGIN_YEAR] + "-" + resources[POSITION_BEGIN_MONTH] + "-" + resources[POSITION_BEGIN_DAY] + "T" + resources[POSITION_BEGIN_HOUR] + ":" + resources[POSITION_BEGIN_MINUTE] + ":"
							+ resources[POSITION_BEGIN_SECOND] + "/" + resources[POSITION_END_YEAR] + "-" + resources[POSITION_END_MONTH]);
				}
				// .../BEGIN_YEAR/BEGIN_MONTH/BEGIN_DAY/BEGIN_HOUR/BEGIN_MINUTE/BEGIN_SECOND/BEGIN_TIMEZONE/
				// END_YEAR/END_MONTH/-/-/-/-/-/...
				else {
					paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_EVENT_TIME_PARAMETER, resources[POSITION_BEGIN_YEAR] + "-" + resources[POSITION_BEGIN_MONTH] + "-" + resources[POSITION_BEGIN_DAY] + "T" + resources[POSITION_BEGIN_HOUR] + ":" + resources[POSITION_BEGIN_MINUTE] + ":"
							+ resources[POSITION_BEGIN_SECOND] + "+" + resources[POSITION_BEGIN_TIMEZONE] + "/" + resources[POSITION_END_YEAR] + "-" + resources[POSITION_END_MONTH]);
				}
			}
			// .../BEGIN_YEAR/BEGIN_MONTH/BEGIN_DAY/-/-/-/-/
			// END_YEAR/END_MONTH/END_DAY/-/-/-/-/...
			else if (resources[POSITION_BEGIN_HOUR].equals("-") && resources[POSITION_END_HOUR].equals("-")) {
				paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_EVENT_TIME_PARAMETER, resources[POSITION_BEGIN_YEAR] + "-" + resources[POSITION_BEGIN_MONTH] + "-" + resources[POSITION_BEGIN_DAY] + "/" + resources[POSITION_END_YEAR] + "-" + resources[POSITION_END_MONTH] + "-"
						+ resources[POSITION_END_DAY]);
			}
			// .../BEGIN_YEAR/BEGIN_MONTH/BEGIN_DAY/-/-/-/-/
			// END_YEAR/END_MONTH/END_DAY/END_HOUR/.../.../.../...
			else if (resources[POSITION_BEGIN_HOUR].equals("-")) {
				// .../BEGIN_YEAR/BEGIN_MONTH/BEGIN_DAY/-/-/-/-/
				// END_YEAR/END_MONTH/END_DAY/END_HOUR/-/-/-/...
				if (resources[POSITION_END_MINUTE].equals("-")) {
					paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_EVENT_TIME_PARAMETER, resources[POSITION_BEGIN_YEAR] + "-" + resources[POSITION_BEGIN_MONTH] + "-" + resources[POSITION_BEGIN_DAY] + "/" + resources[POSITION_END_YEAR] + "-" + resources[POSITION_END_MONTH] + "-"
							+ resources[POSITION_END_DAY] + "T" + resources[POSITION_END_HOUR]);
				}
				// .../BEGIN_YEAR/BEGIN_MONTH/BEGIN_DAY/-/-/-/-/
				// END_YEAR/END_MONTH/END_DAY/END_HOUR/END_MINUTE/-/-/...
				else if (resources[POSITION_END_SECOND].equals("-")) {
					paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_EVENT_TIME_PARAMETER, resources[POSITION_BEGIN_YEAR] + "-" + resources[POSITION_BEGIN_MONTH] + "-" + resources[POSITION_BEGIN_DAY] + "/" + resources[POSITION_END_YEAR] + "-" + resources[POSITION_END_MONTH] + "-"
							+ resources[POSITION_END_DAY] + "T" + resources[POSITION_END_HOUR] + ":" + resources[POSITION_END_MINUTE]);
				}
				// .../BEGIN_YEAR/BEGIN_MONTH/BEGIN_DAY/-/-/-/-/
				// END_YEAR/END_MONTH/END_DAY/END_HOUR/END_MINUTE/END_SECOND/-/...
				else if (resources[POSITION_END_TIMEZONE].equals("-")) {
					paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_EVENT_TIME_PARAMETER, resources[POSITION_BEGIN_YEAR] + "-" + resources[POSITION_BEGIN_MONTH] + "-" + resources[POSITION_BEGIN_DAY] + "/" + resources[POSITION_END_YEAR] + "-" + resources[POSITION_END_MONTH] + "-"
							+ resources[POSITION_END_DAY] + "T" + resources[POSITION_END_HOUR] + ":" + resources[POSITION_END_MINUTE] + ":" + resources[POSITION_END_SECOND]);
				}
				// .../BEGIN_YEAR/BEGIN_MONTH/BEGIN_DAY/-/-/-/-/
				// END_YEAR/END_MONTH/END_DAY/END_HOUR/END_MINUTE/END_SECOND/END_TIMEZONE/...
				else {
					paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_EVENT_TIME_PARAMETER, resources[POSITION_BEGIN_YEAR] + "-" + resources[POSITION_BEGIN_MONTH] + "-" + resources[POSITION_BEGIN_DAY] + "/" + resources[POSITION_END_YEAR] + "-" + resources[POSITION_END_MONTH] + "-"
							+ resources[POSITION_END_DAY] + "T" + resources[POSITION_END_HOUR] + ":" + resources[POSITION_END_MINUTE] + ":" + resources[POSITION_END_SECOND] + "+" + resources[POSITION_END_TIMEZONE]);
				}
			}
			// .../BEGIN_YEAR/BEGIN_MONTH/BEGIN_DAY/BEGIN_HOUR/.../.../.../
			// END_YEAR/END_MONTH/END_DAY/-/-/-/-/...
			else if (resources[POSITION_END_HOUR].equals("-")) {
				// .../BEGIN_YEAR/BEGIN_MONTH/BEGIN_DAY/BEGIN_HOUR/-/-/-/
				// END_YEAR/END_MONTH/END_DAY/-/-/-/-/...
				if (resources[POSITION_BEGIN_MINUTE].equals("-")) {
					paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_EVENT_TIME_PARAMETER, resources[POSITION_BEGIN_YEAR] + "-" + resources[POSITION_BEGIN_MONTH] + "-" + resources[POSITION_BEGIN_DAY] + "T" + resources[POSITION_BEGIN_HOUR] + "/" + resources[POSITION_END_YEAR] + "-"
							+ resources[POSITION_END_MONTH] + "-" + resources[POSITION_END_DAY]);
				}
				// .../BEGIN_YEAR/BEGIN_MONTH/BEGIN_DAY/BEGIN_HOUR/BEGIN_MINUTE/-/-/
				// END_YEAR/END_MONTH/END_DAY/-/-/-/-/...
				else if (resources[POSITION_BEGIN_SECOND].equals("-")) {
					paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_EVENT_TIME_PARAMETER, resources[POSITION_BEGIN_YEAR] + "-" + resources[POSITION_BEGIN_MONTH] + "-" + resources[POSITION_BEGIN_DAY] + "T" + resources[POSITION_BEGIN_HOUR] + ":" + resources[POSITION_BEGIN_MINUTE] + "/"
							+ resources[POSITION_END_YEAR] + "-" + resources[POSITION_END_MONTH] + "-" + resources[POSITION_END_DAY]);
				}
				// .../BEGIN_YEAR/BEGIN_MONTH/BEGIN_DAY/BEGIN_HOUR/BEGIN_MINUTE/BEGIN_SECOND/-/
				// END_YEAR/END_MONTH/END_DAY/-/-/-/-/...
				else if (resources[POSITION_BEGIN_TIMEZONE].equals("-")) {
					paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_EVENT_TIME_PARAMETER, resources[POSITION_BEGIN_YEAR] + "-" + resources[POSITION_BEGIN_MONTH] + "-" + resources[POSITION_BEGIN_DAY] + "T" + resources[POSITION_BEGIN_HOUR] + ":" + resources[POSITION_BEGIN_MINUTE] + ":"
							+ resources[POSITION_BEGIN_SECOND] + "/" + resources[POSITION_END_YEAR] + "-" + resources[POSITION_END_MONTH] + "-" + resources[POSITION_END_DAY]);
				}
				// .../BEGIN_YEAR/BEGIN_MONTH/BEGIN_DAY/BEGIN_HOUR/BEGIN_MINUTE/BEGIN_SECOND/BEGIN_TIMEZONE/
				// END_YEAR/END_MONTH/END_DAY/-/-/-/-/...
				else {
					paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_EVENT_TIME_PARAMETER, resources[POSITION_BEGIN_YEAR] + "-" + resources[POSITION_BEGIN_MONTH] + "-" + resources[POSITION_BEGIN_DAY] + "T" + resources[POSITION_BEGIN_HOUR] + ":" + resources[POSITION_BEGIN_MINUTE] + ":"
							+ resources[POSITION_BEGIN_SECOND] + "+" + resources[POSITION_BEGIN_TIMEZONE] + "/" + resources[POSITION_END_YEAR] + "-" + resources[POSITION_END_MONTH] + "-" + resources[POSITION_END_DAY]);
				}
			}
			// .../BEGIN_YEAR/BEGIN_MONTH/BEGIN_DAY/BEGIN_HOUR/-/-/-/
			// END_YEAR/END_MONTH/END_DAY/END_HOUR/-/-/-/...
			else if (resources[POSITION_BEGIN_MINUTE].equals("-") && resources[POSITION_END_MINUTE].equals("-")) {
				paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_EVENT_TIME_PARAMETER, resources[POSITION_BEGIN_YEAR] + "-" + resources[POSITION_BEGIN_MONTH] + "-" + resources[POSITION_BEGIN_DAY] + "T" + resources[POSITION_BEGIN_HOUR] + "/" + resources[POSITION_END_YEAR] + "-"
						+ resources[POSITION_END_MONTH] + "-" + resources[POSITION_END_DAY] + "T" + resources[POSITION_END_HOUR]);
			}
			// .../BEGIN_YEAR/BEGIN_MONTH/BEGIN_DAY/BEGIN_HOUR/-/-/-/
			// END_YEAR/END_MONTH/END_DAY/END_HOUR/END_MINUTE/.../.../...
			else if (resources[POSITION_BEGIN_MINUTE].equals("-")) {
				// .../BEGIN_YEAR/BEGIN_MONTH/BEGIN_DAY/BEGIN_HOUR/-/-/-/
				// END_YEAR/END_MONTH/END_DAY/END_HOUR/END_MINUTE/-/-/...
				if (resources[POSITION_END_SECOND].equals("-")) {
					paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_EVENT_TIME_PARAMETER, resources[POSITION_BEGIN_YEAR] + "-" + resources[POSITION_BEGIN_MONTH] + "-" + resources[POSITION_BEGIN_DAY] + "T" + resources[POSITION_BEGIN_HOUR] + "/" + resources[POSITION_END_YEAR] + "-"
							+ resources[POSITION_END_MONTH] + "-" + resources[POSITION_END_DAY] + "T" + resources[POSITION_END_HOUR] + ":" + resources[POSITION_END_MINUTE]);
				}
				// .../BEGIN_YEAR/BEGIN_MONTH/BEGIN_DAY/BEGIN_HOUR/-/-/-/
				// END_YEAR/END_MONTH/END_DAY/END_HOUR/END_MINUTE/END_SECOND/-/...
				else if (resources[POSITION_END_TIMEZONE].equals("-")) {
					paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_EVENT_TIME_PARAMETER, resources[POSITION_BEGIN_YEAR] + "-" + resources[POSITION_BEGIN_MONTH] + "-" + resources[POSITION_BEGIN_DAY] + "T" + resources[POSITION_BEGIN_HOUR] + "/" + resources[POSITION_END_YEAR] + "-"
							+ resources[POSITION_END_MONTH] + "-" + resources[POSITION_END_DAY] + "T" + resources[POSITION_END_HOUR] + ":" + resources[POSITION_END_MINUTE] + ":" + resources[POSITION_END_SECOND]);
				}
				// .../BEGIN_YEAR/BEGIN_MONTH/BEGIN_DAY/BEGIN_HOUR/-/-/-/
				// END_YEAR/END_MONTH/END_DAY/END_HOUR/END_MINUTE/END_SECOND/END_TIMEZONE/...
				else {
					paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_EVENT_TIME_PARAMETER, resources[POSITION_BEGIN_YEAR] + "-" + resources[POSITION_BEGIN_MONTH] + "-" + resources[POSITION_BEGIN_DAY] + "T" + resources[POSITION_BEGIN_HOUR] + "/" + resources[POSITION_END_YEAR] + "-"
							+ resources[POSITION_END_MONTH] + "-" + resources[POSITION_END_DAY] + "T" + resources[POSITION_END_HOUR] + ":" + resources[POSITION_END_MINUTE] + ":" + resources[POSITION_END_SECOND] + "+" + resources[POSITION_END_TIMEZONE]);
				}
			}
			// .../BEGIN_YEAR/BEGIN_MONTH/BEGIN_DAY/BEGIN_HOUR/BEGIN_MINUTE/.../.../
			// END_YEAR/END_MONTH/END_DAY/END_HOUR/-/-/-/...
			else if (resources[POSITION_END_MINUTE].equals("-")) {
				// .../BEGIN_YEAR/BEGIN_MONTH/BEGIN_DAY/BEGIN_HOUR/BEGIN_MINUTE/-/-/
				// END_YEAR/END_MONTH/END_DAY/END_HOUR/-/-/-/...
				if (resources[POSITION_BEGIN_SECOND].equals("-")) {
					paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_EVENT_TIME_PARAMETER, resources[POSITION_BEGIN_YEAR] + "-" + resources[POSITION_BEGIN_MONTH] + "-" + resources[POSITION_BEGIN_DAY] + "T" + resources[POSITION_BEGIN_HOUR] + ":" + resources[POSITION_BEGIN_MINUTE] + "/"
							+ resources[POSITION_END_YEAR] + "-" + resources[POSITION_END_MONTH] + "-" + resources[POSITION_END_DAY] + "T" + resources[POSITION_END_HOUR]);
				}
				// .../BEGIN_YEAR/BEGIN_MONTH/BEGIN_DAY/BEGIN_HOUR/BEGIN_MINUTE/BEGIN_SECOND/-/
				// END_YEAR/END_MONTH/END_DAY/END_HOUR/-/-/-/...
				else if (resources[POSITION_BEGIN_TIMEZONE].equals("-")) {
					paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_EVENT_TIME_PARAMETER, resources[POSITION_BEGIN_YEAR] + "-" + resources[POSITION_BEGIN_MONTH] + "-" + resources[POSITION_BEGIN_DAY] + "T" + resources[POSITION_BEGIN_HOUR] + ":" + resources[POSITION_BEGIN_MINUTE] + ":"
							+ resources[POSITION_BEGIN_SECOND] + "/" + resources[POSITION_END_YEAR] + "-" + resources[POSITION_END_MONTH] + "-" + resources[POSITION_END_DAY] + "T" + resources[POSITION_END_HOUR]);
				}
				// .../BEGIN_YEAR/BEGIN_MONTH/BEGIN_DAY/BEGIN_HOUR/BEGIN_MINUTE/BEGIN_SECOND/BEGIN_TIMEZONE/
				// END_YEAR/END_MONTH/END_DAY/END_HOUR/-/-/-/...
				else {
					paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_EVENT_TIME_PARAMETER, resources[POSITION_BEGIN_YEAR] + "-" + resources[POSITION_BEGIN_MONTH] + "-" + resources[POSITION_BEGIN_DAY] + "T" + resources[POSITION_BEGIN_HOUR] + ":" + resources[POSITION_BEGIN_MINUTE] + ":"
							+ resources[POSITION_BEGIN_SECOND] + "+" + resources[POSITION_BEGIN_TIMEZONE] + "/" + resources[POSITION_END_YEAR] + "-" + resources[POSITION_END_MONTH] + "-" + resources[POSITION_END_DAY] + "T" + resources[POSITION_END_HOUR]);
				}
			}
			// .../BEGIN_YEAR/BEGIN_MONTH/BEGIN_DAY/BEGIN_HOUR/BEGIN_MINUTE/-/-/
			// END_YEAR/END_MONTH/END_DAY/END_HOUR/END_MINUTE/-/-/...
			else if (resources[POSITION_BEGIN_SECOND].equals("-") && resources[POSITION_END_SECOND].equals("-")) {
				paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_EVENT_TIME_PARAMETER, resources[POSITION_BEGIN_YEAR] + "-" + resources[POSITION_BEGIN_MONTH] + "-" + resources[POSITION_BEGIN_DAY] + "T" + resources[POSITION_BEGIN_HOUR] + ":" + resources[POSITION_BEGIN_MINUTE] + "/"
						+ resources[POSITION_END_YEAR] + "-" + resources[POSITION_END_MONTH] + "-" + resources[POSITION_END_DAY] + "T" + resources[POSITION_END_HOUR] + ":" + resources[POSITION_END_MINUTE]);
			}
			// .../BEGIN_YEAR/BEGIN_MONTH/BEGIN_DAY/BEGIN_HOUR/BEGIN_MINUTE/-/-/
			// END_YEAR/END_MONTH/END_DAY/END_HOUR/END_MINUTE/END_SECOND/.../...
			else if (resources[POSITION_BEGIN_SECOND].equals("-")) {
				// .../BEGIN_YEAR/BEGIN_MONTH/BEGIN_DAY/BEGIN_HOUR/BEGIN_MINUTE/-/-/
				// END_YEAR/END_MONTH/END_DAY/END_HOUR/END_MINUTE/END_SECOND/-/...
				if (resources[POSITION_END_TIMEZONE].equals("-")) {
					paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_EVENT_TIME_PARAMETER, resources[POSITION_BEGIN_YEAR] + "-" + resources[POSITION_BEGIN_MONTH] + "-" + resources[POSITION_BEGIN_DAY] + "T" + resources[POSITION_BEGIN_HOUR] + ":" + resources[POSITION_BEGIN_MINUTE] + "/"
							+ resources[POSITION_END_YEAR] + "-" + resources[POSITION_END_MONTH] + "-" + resources[POSITION_END_DAY] + "T" + resources[POSITION_END_HOUR] + ":" + resources[POSITION_END_MINUTE] + ":" + resources[POSITION_END_SECOND]);
				}
				// .../BEGIN_YEAR/BEGIN_MONTH/BEGIN_DAY/BEGIN_HOUR/BEGIN_MINUTE/-/-/
				// END_YEAR/END_MONTH/END_DAY/END_HOUR/END_MINUTE/END_SECOND/END_TIMEZONE/...
				else {
					paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_EVENT_TIME_PARAMETER, resources[POSITION_BEGIN_YEAR] + "-" + resources[POSITION_BEGIN_MONTH] + "-" + resources[POSITION_BEGIN_DAY] + "T" + resources[POSITION_BEGIN_HOUR] + ":" + resources[POSITION_BEGIN_MINUTE] + "/"
							+ resources[POSITION_END_YEAR] + "-" + resources[POSITION_END_MONTH] + "-" + resources[POSITION_END_DAY] + "T" + resources[POSITION_END_HOUR] + ":" + resources[POSITION_END_MINUTE] + ":" + resources[POSITION_END_SECOND] + "+" + resources[POSITION_END_TIMEZONE]);
				}
			}
			// .../BEGIN_YEAR/BEGIN_MONTH/BEGIN_DAY/BEGIN_HOUR/BEGIN_MINUTE/BEGIN_SECOND/.../
			// END_YEAR/END_MONTH/END_DAY/END_HOUR/END_MINUTE/-/-/...
			else if (resources[POSITION_END_SECOND].equals("-")) {
				// .../BEGIN_YEAR/BEGIN_MONTH/BEGIN_DAY/BEGIN_HOUR/BEGIN_MINUTE/BEGIN_SECOND/-/
				// END_YEAR/END_MONTH/END_DAY/END_HOUR/END_MINUTE/-/-/...
				if (resources[POSITION_BEGIN_TIMEZONE].equals("-")) {
					paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_EVENT_TIME_PARAMETER, resources[POSITION_BEGIN_YEAR] + "-" + resources[POSITION_BEGIN_MONTH] + "-" + resources[POSITION_BEGIN_DAY] + "T" + resources[POSITION_BEGIN_HOUR] + ":" + resources[POSITION_BEGIN_MINUTE] + ":"
							+ resources[POSITION_BEGIN_SECOND] + "/" + resources[POSITION_END_YEAR] + "-" + resources[POSITION_END_MONTH] + "-" + resources[POSITION_END_DAY] + "T" + resources[POSITION_END_HOUR] + ":" + resources[POSITION_END_MINUTE]);
				}
				// .../BEGIN_YEAR/BEGIN_MONTH/BEGIN_DAY/BEGIN_HOUR/BEGIN_MINUTE/BEGIN_SECOND/BEGIN_TIMEZONE/
				// END_YEAR/END_MONTH/END_DAY/END_HOUR/END_MINUTE/-/-/...
				else {
					paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_EVENT_TIME_PARAMETER, resources[POSITION_BEGIN_YEAR] + "-" + resources[POSITION_BEGIN_MONTH] + "-" + resources[POSITION_BEGIN_DAY] + "T" + resources[POSITION_BEGIN_HOUR] + ":" + resources[POSITION_BEGIN_MINUTE] + ":"
							+ resources[POSITION_BEGIN_SECOND] + "+" + resources[POSITION_BEGIN_TIMEZONE] + "/" + resources[POSITION_END_YEAR] + "-" + resources[POSITION_END_MONTH] + "-" + resources[POSITION_END_DAY] + "T" + resources[POSITION_END_HOUR] + ":" + resources[POSITION_END_MINUTE]);
				}
			}
			// .../BEGIN_YEAR/BEGIN_MONTH/BEGIN_DAY/BEGIN_HOUR/BEGIN_MINUTE/BEGIN_SECOND/-/
			// END_YEAR/END_MONTH/END_DAY/END_HOUR/END_MINUTE/END_SECOND/-/...
			else if (resources[POSITION_BEGIN_TIMEZONE].equals("-") && resources[POSITION_END_TIMEZONE].equals("-")) {
				paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_EVENT_TIME_PARAMETER, resources[POSITION_BEGIN_YEAR] + "-" + resources[POSITION_BEGIN_MONTH] + "-" + resources[POSITION_BEGIN_DAY] + "T" + resources[POSITION_BEGIN_HOUR] + ":" + resources[POSITION_BEGIN_MINUTE] + ":"
						+ resources[POSITION_BEGIN_SECOND] + "/" + resources[POSITION_END_YEAR] + "-" + resources[POSITION_END_MONTH] + "-" + resources[POSITION_END_DAY] + "T" + resources[POSITION_END_HOUR] + ":" + resources[POSITION_END_MINUTE] + ":" + resources[POSITION_END_SECOND]);
			}
			// .../BEGIN_YEAR/BEGIN_MONTH/BEGIN_DAY/BEGIN_HOUR/BEGIN_MINUTE/BEGIN_SECOND/-/
			// END_YEAR/END_MONTH/END_DAY/END_HOUR/END_MINUTE/END_SECOND/END_TIMEZONE/...
			else if (resources[POSITION_BEGIN_TIMEZONE].equals("-")) {
				paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_EVENT_TIME_PARAMETER, resources[POSITION_BEGIN_YEAR] + "-" + resources[POSITION_BEGIN_MONTH] + "-" + resources[POSITION_BEGIN_DAY] + "T" + resources[POSITION_BEGIN_HOUR] + ":" + resources[POSITION_BEGIN_MINUTE] + ":"
						+ resources[POSITION_BEGIN_SECOND] + "/" + resources[POSITION_END_YEAR] + "-" + resources[POSITION_END_MONTH] + "-" + resources[POSITION_END_DAY] + "T" + resources[POSITION_END_HOUR] + ":" + resources[POSITION_END_MINUTE] + ":" + resources[POSITION_END_SECOND] + "+"
						+ resources[POSITION_END_TIMEZONE]);
			}
			// .../BEGIN_YEAR/BEGIN_MONTH/BEGIN_DAY/BEGIN_HOUR/BEGIN_MINUTE/BEGIN_SECOND/BEGIN_TIMEZONE/
			// END_YEAR/END_MONTH/END_DAY/END_HOUR/END_MINUTE/END_SECOND/-/...
			else if (resources[POSITION_END_TIMEZONE].equals("-")) {
				paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_EVENT_TIME_PARAMETER, resources[POSITION_BEGIN_YEAR] + "-" + resources[POSITION_BEGIN_MONTH] + "-" + resources[POSITION_BEGIN_DAY] + "T" + resources[POSITION_BEGIN_HOUR] + ":" + resources[POSITION_BEGIN_MINUTE] + ":"
						+ resources[POSITION_BEGIN_SECOND] + "+" + resources[POSITION_BEGIN_TIMEZONE] + "/" + resources[POSITION_END_YEAR] + "-" + resources[POSITION_END_MONTH] + "-" + resources[POSITION_END_DAY] + "T" + resources[POSITION_END_HOUR] + ":" + resources[POSITION_END_MINUTE] + ":"
						+ resources[POSITION_END_SECOND]);
			}
			// .../BEGIN_YEAR/BEGIN_MONTH/BEGIN_DAY/BEGIN_HOUR/BEGIN_MINUTE/BEGIN_SECOND/BEGIN_TIMEZONE/
			// END_YEAR/END_MONTH/END_DAY/END_HOUR/END_MINUTE/END_SECOND/END_TIMEZONE/...
			else {
				paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_EVENT_TIME_PARAMETER, resources[POSITION_BEGIN_YEAR] + "-" + resources[POSITION_BEGIN_MONTH] + "-" + resources[POSITION_BEGIN_DAY] + "T" + resources[POSITION_BEGIN_HOUR] + ":" + resources[POSITION_BEGIN_MINUTE] + ":"
						+ resources[POSITION_BEGIN_SECOND] + "+" + resources[POSITION_BEGIN_TIMEZONE] + "/" + resources[POSITION_END_YEAR] + "-" + resources[POSITION_END_MONTH] + "-" + resources[POSITION_END_DAY] + "T" + resources[POSITION_END_HOUR] + ":" + resources[POSITION_END_MINUTE] + ":"
						+ resources[POSITION_END_SECOND] + "+" + resources[POSITION_END_TIMEZONE]);
			}

			paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_RESULT_MODEL_PARAMETER, resources[POSITION_RESULT_MODEL]);

			// hard-coded parameters:
			paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_RESPONSE_FORMAT_PARAMETER, "text/xml;subtype=\"om/" + serviceVersion + "\"");
			paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_SERVICE_PARAMETER, SOSAdapter.SERVICE_TYPE);
			paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_VERSION_PARAMETER, serviceVersion);
			if (serviceVersion.equals("0.0.0")) {
				paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_RESPONSE_MODE_PARAMETER, "inline");
			}
		} catch (Exception e) {
			throw new ServletException(e);
		}
		return paramCon;
	}

	/**
	 * new URL schema for GetObservation request: Parameter array 'urlSegments'
	 * is structured as follows:
	 * 
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
	 * @param requestURL
	 * @param resources
	 * @param adapter
	 * @param servletPath
	 * @param serviceDesc
	 * @param serviceVersion
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	private void newURLSchema(String requestURL, String[] resources, SOSAdapter adapter, String servletPath, ServiceDescriptor serviceDesc, String serviceVersion, HttpServletResponse response, String headerValue) throws IOException, ServletException {
		Contents contents = serviceDesc.getContents();
		OutputStream out = response.getOutputStream();

		int offeringPosition = -1;
		ObservationOffering chosenOffering = null;

		if (requestURL.endsWith(KEY_OFFERING) || requestURL.endsWith(KEY_OFFERING + "/") || requestURL.endsWith(KEY_OFFERING.toLowerCase()) || requestURL.endsWith(KEY_OFFERING.toLowerCase() + "/")) {

			for (int i = 0; i < resources.length; i++) {
				if (resources[i].equals(KEY_OFFERING) || resources[i].equals(KEY_OFFERING.toLowerCase())) {
					offeringPosition = i + 1;
				}
			}
			String htmlString = renderResourcesHtml(requestURL, servletPath, "Offering", contents.getDataIdentificationIDArray());

			out.write(htmlString.getBytes());

		} else if (requestURL.endsWith(KEY_FOI) || requestURL.endsWith(KEY_FOI + "/") || requestURL.endsWith(KEY_FOI.toLowerCase()) || requestURL.endsWith(KEY_FOI.toLowerCase() + "/")) {
			//get foi of the offering
			if (requestURL.contains(KEY_OFFERING) || requestURL.contains(KEY_OFFERING.toLowerCase())) {
				for (int i = 0; i < resources.length; i++) {
					if (resources[i].equals(KEY_OFFERING) || resources[i].equals(KEY_OFFERING.toLowerCase())) {
						offeringPosition = i + 1;
					}
				}

				chosenOffering = (ObservationOffering) contents.getDataIdentification(resources[offeringPosition]);

				if (chosenOffering == null) {
					throw new ServletException("Resource '/" + getResourcesString(requestURL, servletPath) + "' unavailable.");
				}

				int j = 0;
				String[] newFois = new String[chosenOffering.getFeatureOfInterest().length];
				for (int i = 0; i < chosenOffering.getFeatureOfInterest().length; i++) {

					String[] newstring = chosenOffering.getFeatureOfInterest()[i].split("fois/");
					if (newstring.length == 2) {
						newFois[j] = newstring[1];
					} else {
						newFois[j] = newstring[0];
					}
					j++;
				}

				String htmlString = renderResourcesHtml(requestURL, servletPath, "Feature Of Interest", newFois);

				out.write(htmlString.getBytes());

			} else {// get all FOI of all offerings

				String[] o = contents.getDataIdentificationIDArray();

				int countFOI = 0;
				for (int i = 0; i < o.length; i++) {
					ObservationOffering offering = (ObservationOffering) contents.getDataIdentification(o[i].toString());
					countFOI += offering.getFeatureOfInterest().length;
				}

				String[] fois = new String[countFOI];
				int a = 0;
				for (int i = 0; i < o.length; i++) {

					ObservationOffering offering = (ObservationOffering) contents.getDataIdentification(o[i].toString());

					if (offering == null) {
						throw new ServletException("Resource '/" + getResourcesString(requestURL, servletPath) + "' unavailable.");
					}

					String[] foi = offering.getFeatureOfInterest();
					for (int j = 0; j < foi.length; j++) {
						fois[a] = foi[j];
						a++;
					}
				}

				int j = 0;
				String[] newFois = new String[fois.length];
				for (int i = 0; i < fois.length; i++) {
					String[] newstring = fois[i].split("fois/");
					if (newstring.length == 2) {
						newFois[j] = newstring[1];
					} else {
						newFois[j] = newstring[0];
					}
					j++;

				}

				String htmlString = renderResourcesHtml(requestURL, servletPath, "Feature Of Interest", newFois);
				out.write(htmlString.getBytes());
			}
		} else if (requestURL.endsWith(KEY_OBSPROP) || requestURL.endsWith(KEY_OBSPROP + "/") || requestURL.endsWith(KEY_OBSPROP.toLowerCase()) || requestURL.endsWith(KEY_OBSPROP.toLowerCase() + "/")) {
			//get observedProperties of the offering
			if (requestURL.contains(KEY_OFFERING) || requestURL.contains(KEY_OFFERING.toLowerCase())) {
				for (int i = 0; i < resources.length; i++) {
					if (resources[i].equals(KEY_OFFERING) || resources[i].equals(KEY_OFFERING.toLowerCase())) {
						offeringPosition = i + 1;
					}
				}

				chosenOffering = (ObservationOffering) contents.getDataIdentification(resources[offeringPosition]);

				if (chosenOffering == null) {
					throw new ServletException("Resource '/" + getResourcesString(requestURL, servletPath) + "' unavailable.");
				}
				int j = 0;
				String[] newOP = new String[chosenOffering.getObservedProperties().length];
				for (int i = 0; i < chosenOffering.getObservedProperties().length; i++) {

					String[] newstring = chosenOffering.getObservedProperties()[i].split("OGC/");
					if (newstring.length == 2) {
						newOP[j] = newstring[1];
					} else {
						newOP[j] = newstring[0];
					}
					j++;
				}

				String htmlString = renderResourcesHtml(requestURL, servletPath, "Observed Property", newOP);
				out.write(htmlString.getBytes());
			} else {// get all observedProperties
				String[] o = contents.getDataIdentificationIDArray();

				int countObsP = 0;
				for (int i = 0; i < o.length; i++) {
					ObservationOffering offering = (ObservationOffering) contents.getDataIdentification(o[i].toString());
					countObsP += offering.getObservedProperties().length;
				}

				String[] obsP = new String[countObsP];
				int a = 0;
				for (int i = 0; i < o.length; i++) {

					ObservationOffering offering = (ObservationOffering) contents.getDataIdentification(o[i].toString());

					if (offering == null) {
						throw new ServletException("Resource '/" + getResourcesString(requestURL, servletPath) + "' unavailable.");
					}

					String[] foi = offering.getObservedProperties();
					for (int j = 0; j < foi.length; j++) {
						obsP[a] = foi[j];
						a++;
					}
				}

				int j = 0;
				String[] newObsP = new String[obsP.length];
				for (int i = 0; i < obsP.length; i++) {
					String[] newstring = obsP[i].split("OGC/");
					if (newstring.length == 2) {
						newObsP[j] = newstring[1];
					} else {
						newObsP[j] = newstring[0];
					}
					j++;

				}

				String htmlString = renderResourcesHtml(requestURL, servletPath, "Observed Property", newObsP);
				out.write(htmlString.getBytes());
			}

		} else if (requestURL.endsWith(KEY_PROCEDURES) || requestURL.endsWith(KEY_PROCEDURES + "/") || requestURL.endsWith(KEY_PROCEDURES.toLowerCase()) || requestURL.endsWith(KEY_PROCEDURES.toLowerCase() + "/")) {
			//get procedures of the offering
			if (requestURL.contains(KEY_OFFERING) || requestURL.contains(KEY_OFFERING.toLowerCase())) {
				for (int i = 0; i < resources.length; i++) {
					if (resources[i].equals(KEY_OFFERING) || resources[i].equals(KEY_OFFERING.toLowerCase())) {
						offeringPosition = i + 1;
					}
				}

				chosenOffering = (ObservationOffering) contents.getDataIdentification(resources[offeringPosition]);

				if (chosenOffering == null) {
					throw new ServletException("Resource '/" + getResourcesString(requestURL, servletPath) + "' unavailable.");
				}

				int j = 0;
				String[] newProcedures = new String[chosenOffering.getProcedures().length];
				for (int i = 0; i < chosenOffering.getProcedures().length; i++) {

					String[] newstring = chosenOffering.getProcedures()[i].split("sensors/");
					if (newstring.length == 2) {
						newProcedures[j] = newstring[1];
					} else {
						newProcedures[j] = newstring[0];
					}
					j++;
				}

				String htmlString = renderResourcesHtml(requestURL, servletPath, "Procedures", newProcedures);

				out.write(htmlString.getBytes());

			} else {// get all
				String[] o = contents.getDataIdentificationIDArray();

				int countProcedures = 0;
				for (int i = 0; i < o.length; i++) {
					ObservationOffering offering = (ObservationOffering) contents.getDataIdentification(o[i].toString());
					countProcedures += offering.getProcedures().length;
				}

				String[] proc = new String[countProcedures];
				int a = 0;
				for (int i = 0; i < o.length; i++) {

					ObservationOffering offering = (ObservationOffering) contents.getDataIdentification(o[i].toString());

					if (offering == null) {
						throw new ServletException("Resource '/" + getResourcesString(requestURL, servletPath) + "' unavailable.");
					}

					String[] foi = offering.getProcedures();
					for (int j = 0; j < foi.length; j++) {
						proc[a] = foi[j];
						a++;
					}
				}

				int j = 0;
				String[] newProc = new String[proc.length];
				for (int i = 0; i < proc.length; i++) {
					String[] newstring = proc[i].split("sensors/");
					if (newstring.length == 2) {
						newProc[j] = newstring[1];
					} else {
						newProc[j] = newstring[0];
					}
					j++;

				}

				String htmlString = renderResourcesHtml(requestURL, servletPath, "Procedures", newProc);
				out.write(htmlString.getBytes());
			}
		} else if (requestURL.endsWith(KEY_TIMES) || requestURL.endsWith(KEY_TIMES + "/") || requestURL.endsWith(KEY_TIMES.toLowerCase()) || requestURL.endsWith(KEY_TIMES.toLowerCase() + "/")) {
			//get time of the offering
			if (requestURL.contains(KEY_OFFERING) || requestURL.contains(KEY_OFFERING.toLowerCase())) { 
				for (int i = 0; i < resources.length; i++) {
					if (resources[i].equals(KEY_OFFERING) || resources[i].equals(KEY_OFFERING.toLowerCase())) {
						offeringPosition = i + 1;
					}
				}

				chosenOffering = (ObservationOffering) contents.getDataIdentification(resources[offeringPosition]);

				if (chosenOffering == null) {
					throw new ServletException("Resource '/" + getResourcesString(requestURL, servletPath) + "' unavailable.");
				}

				ITimePeriod timePeriod = (ITimePeriod) chosenOffering.getTemporalDomain().getPossibleValues().get(0);

				String startTime = timePeriod.getStart().toISO8601Format();
				String endTime = timePeriod.getEnd().toISO8601Format();

				String htmlString = renderResourcesHtml(requestURL, servletPath, "Time", new String[] { startTime + "," + endTime });
				out.write(htmlString.getBytes());
			}
		}
		// else
		// if(requestURL.endsWith(KEY_RESULTMODEL)||requestURL.endsWith(KEY_RESULTMODEL+"/")){
		// System.out.println("resultModel");
		// //TODO show all resultmodels
		// String htmlString = renderResourcesHtml(requestURL,
		// servletPath,
		// "Result Model",
		// chosenOffering.getResultModels());
		//
		// out.write(htmlString.getBytes());
		// }
		// else if(requestURL.endsWith(KEY_IDS) || requestURL.endsWith(KEY_IDS +
		// "/")){//TODO show all key ids
		//
		// String htmlString = renderResourcesHtml(requestURL, servletPath,
		// "ids", contents.getDataIdentificationIDArray());
		// out.write(htmlString.getBytes());
		//			
		// }
		else {
			String[] urlSegments = new String[resources.length];
			for (int j = 0; j < resources.length; j++) {
				urlSegments[j] = resources[j];
			}

			try {

				if (requestURL.contains(KEY_IDS)) {
					ParameterContainer paramCon = new ParameterContainer();
					int a = 0;
					for (int i = 0; i < urlSegments.length; i++) {

						if (urlSegments[i].equals(KEY_IDS)) {
							a = i + 1;
							if (urlSegments.length == a + 1) {

								String id = urlSegments[a];

								paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_BY_ID_OBSERVATION_ID_PARAMETER, id);
								// hard-coded parameters: //required Parameters
								// service, version and resultFormat
								paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_BY_ID_RESPONSE_FORMAT_PARAMETER, "text/xml;subtype=\"om/" + serviceVersion + "\"");
								paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_BY_ID_SERVICE_PARAMETER, SOSAdapter.SERVICE_TYPE);
								paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_BY_ID_RESULT_MODEL_PARAMETER, "Measurement");
								paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_BY_ID_VERSION_PARAMETER, serviceVersion);
								if (serviceVersion.equals("0.0.0")) {
									paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_BY_ID_RESPONSE_MODE_PARAMETER, "inline");
								}
								OperationResult oper = adapter.doOperation(serviceDesc.getOperationsMetadata().getOperationByName(SOSAdapter.GET_OBSERVATION_BY_ID), paramCon);

								IFeatureStore featureStore = new SOSObservationStore();

								OXFFeatureCollection observationColl = featureStore.unmarshalFeatures(oper);

								String responseString = RDFEncoder.getObsTriple(observationColl.toList().get(0), requestURL);

								response.setContentType("application/rdf+xml");
								out.write(responseString.getBytes());
							} else {
								throw new ServletException("Give an id for " + KEY_IDS);
							}
						}
					}
				} else if (requestURL.contains(KEY_PROCEDURES)) {
					ParameterContainer[] paramCon = GetObservationHandler.getObservationsRESTfully(urlSegments, adapter, serviceDesc, serviceVersion, resources[POSITION_SOS]);
					if (requestURL.contains(KEY_OBSPROP) || requestURL.contains(KEY_OBSPROP.toLowerCase())) {
						OperationResult operationR = adapter.doOperation(serviceDesc.getOperationsMetadata().getOperationByName(SOSAdapter.GET_OBSERVATION), paramCon[0]);
						OXFFeatureCollection allObs = null;
						this.choseAShowMethod(headerValue, requestURL, paramCon, operationR, allObs, response, serviceVersion);
					} else {
						OXFFeatureCollection[] resultOP = new OXFFeatureCollection[paramCon.length];
						OXFFeatureCollection allObs = new OXFFeatureCollection("merged_coll", new OXFMeasurementType());

						for (int k = 0; k < paramCon.length; k++) {
							// send GetObservation request:
							SOSAdapter sosAdapter = new SOSAdapter(serviceVersion);
							OperationResult opResult = sosAdapter.doOperation(serviceDesc.getOperationsMetadata().getOperationByName(SOSAdapter.GET_OBSERVATION), paramCon[k]);
							IFeatureStore featureStore = new SOSObservationStore();
							resultOP[k] = featureStore.unmarshalFeatures(opResult);
						}

						allObs = ObservationMerger.mergeObservations(resultOP);

						OperationResult operationR = null;
						this.choseAShowMethod(headerValue, requestURL, paramCon, operationR, allObs, response, serviceVersion);
					}
				} else {
					ParameterContainer[] paramCon = GetObservationHandler.getObservationsRESTfully(urlSegments, adapter, serviceDesc, serviceVersion, resources[POSITION_SOS]);

					OperationResult operationR = null;
					OXFFeatureCollection[] resultOP = new OXFFeatureCollection[paramCon.length];
					OXFFeatureCollection allObs = new OXFFeatureCollection("merged_coll", new OXFMeasurementType());
					if (paramCon.length == 1) {
						operationR = adapter.doOperation(serviceDesc.getOperationsMetadata().getOperationByName(SOSAdapter.GET_OBSERVATION), paramCon[0]);
					} else if (paramCon.length > 1) {
						for (int k = 0; k < paramCon.length; k++) {
							// send GetObservation request:
							SOSAdapter sosAdapter = new SOSAdapter(serviceVersion);
							OperationResult opResult = sosAdapter.doOperation(serviceDesc.getOperationsMetadata().getOperationByName(SOSAdapter.GET_OBSERVATION), paramCon[k]);
							IFeatureStore featureStore = new SOSObservationStore();
							resultOP[k] = featureStore.unmarshalFeatures(opResult);
						}

						allObs = ObservationMerger.mergeObservations(resultOP);
					}
					this.choseAShowMethod(headerValue, requestURL, paramCon, operationR, allObs, response, serviceVersion);
				}
			} catch (ExceptionReport excReport) {
				out.write(excReport.toHtmlString().getBytes());
			} catch (Exception e) {
				throw new ServletException(e);
			}
		}
	}

	/**
	 * Method to chose a show Method
	 * 
	 * @param headerValue
	 * @param requestURL
	 * @param paramCon
	 * @param operationR
	 * @param allObs
	 * @param response
	 * @param serviceVersion
	 * @throws IOException
	 * @throws ServletException
	 */
	private void choseAShowMethod(String headerValue, String requestURL, ParameterContainer[] paramCon, OperationResult operationR, OXFFeatureCollection allObs, HttpServletResponse response, String serviceVersion) throws IOException, ServletException {
		OutputStream out = response.getOutputStream();
		try {
			// if the accepted MIME type is not set, open OM or defined output
			// format
			// else open data in the preferred MIME type
			if (!headerValue.contains("text/xml") && !headerValue.contains("image/jpeg") && !headerValue.contains("application/vnd.google-earth.kml+xml") && !headerValue.contains("application/rdf+xml")) {
				// show GetObservation results in defined OUTPUT_FORMAT or RDF
				// else show in outputFormat "OM" or RDF
				if (requestURL.contains(KEY_OUTPUTFORMAT) || requestURL.contains(KEY_OUTPUTFORMAT.toLowerCase())) {
					String[] outputFormat;
					String output = null;
					if (requestURL.contains(KEY_OUTPUTFORMAT)) {
						outputFormat = requestURL.split("outputFormat/");
						output = outputFormat[1];
					} else if (requestURL.contains(KEY_OUTPUTFORMAT.toLowerCase())) {
						outputFormat = requestURL.split("outputformat/");
						output = outputFormat[1];
					}

					if (operationR != null) {
						this.showResultsInOutputFormat(output, operationR, response, paramCon[0], serviceVersion, requestURL);
					} else if (allObs != null) {// show the merged result in RDF
						String responseString = RDFEncoder.getObsColTriples(allObs, requestURL);

						response.setContentType("application/rdf+xml");
						out.write(responseString.getBytes());
					}
				} else {
					if (operationR != null) {
						this.showResultsInOutputFormat("OM", operationR, response, paramCon[0], serviceVersion, requestURL);
					} else if (allObs != null) {// show the merged result in RDF
						String responseString = RDFEncoder.getObsColTriples(allObs, requestURL);

						response.setContentType("application/rdf+xml");
						out.write(responseString.getBytes());
					}
				}
			} else {
				if (operationR != null) {
					this.showResultsInPreferredMIMEType(paramCon[0], operationR, serviceVersion, response, headerValue, requestURL);
				} else if (allObs != null) {
					// TODO show the merged result in the preferred MIME type
				}
			}
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

}