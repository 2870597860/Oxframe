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

import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlException;
import org.n52.oxf.OXFException;
import org.n52.oxf.owsCommon.ExceptionReport;
import org.n52.oxf.owsCommon.OWSException;
import org.n52.oxf.owsCommon.ServiceDescriptor;
import org.n52.oxf.owsCommon.capabilities.Operation;
import org.n52.oxf.serviceAdapters.OperationResult;
import org.n52.oxf.serviceAdapters.ParameterContainer;
import org.n52.oxf.serviceAdapters.sos.ISOSRequestBuilder;
import org.n52.oxf.serviceAdapters.sos.SOSAdapter;
import org.n52.oxf.util.IOHelper;
import org.n52.oxf.util.LoggingHandler;

/**
 * SOS-Adapter for the OX-Framework adapted to the mobile profile of the 52N SOS.
 * 
 * @author <a href="mailto:daniel.nuest@uni-muenster.de">Daniel Nüst</a>
 */
public class SOSmobileAdapter extends SOSAdapter {

    private static Logger LOGGER = LoggingHandler.getLogger(SOSmobileAdapter.class);

    public static final String GET_FEATURE_OF_INTEREST_TIME = "GetFeatureOfInterestTime";
    public static final String UPDATE_SENSOR = "UpdateSensor";
    public static final String GET_DOMAIN_FEATURE = "GetDomainFeature";
    
    /**
     * Description of the SOSAdapter
     */
    @SuppressWarnings("hiding")
    public static String DESCRIPTION = "This Class implements the Service "
            + "Adapter Interface and is an SOSmobile Adapter for the OXF Framework";

    /**
     * the Versions of the services which are connectable by this ServiceAdapter. Should look like e.g.
     * {"1.1.0","1.2.0"}.
     */
    @SuppressWarnings("hiding")
    public static final String[] SUPPORTED_VERSIONS = {"1.0.0"};


    /**
     * constructor which instantiates
     */
    public SOSmobileAdapter() {
        super(SUPPORTED_VERSIONS[0]);
    }

    /**
     * 
     * @param serviceVersion
     *        the schema version for which this adapter instance shall be initialized.
     */
    public SOSmobileAdapter(String serviceVersionP) {
        super(serviceVersionP);
    }

    /**
     * initializes the ServiceDescriptor by requesting the capabilities document of the SOS.
     * 
     * @param url
     *        the base URL of the SOS
     * @param serviceVersion
     *        the schema version to which the service description shall be conform.
     * 
     * @return the ServiceDescriptor based on the retrieved capabilities document
     * 
     * @throws ExceptionReport
     *         if service side exception occurs
     * @throws OXFException
     *         if internal exception (in general parsing error or Capabilities doc is incorrect) occurs
     * 
     */
    @Override
    public ServiceDescriptor initService(String url) throws ExceptionReport, OXFException {

        ParameterContainer paramCon = new ParameterContainer();
        paramCon.addParameterShell("version", this.serviceVersion);
        paramCon.addParameterShell("service", "SOS");
        paramCon.addParameterShell("mobileEnabled", "true");

        OperationResult opResult = doOperation(new Operation("GetCapabilities", url.toString() + "?", url.toString()),
                                               paramCon);

        return initService(opResult);
    }

    /**
     * 
     * @param getCapabilitiesResult
     * @return
     * @throws ExceptionReport
     * @throws OXFException
     */
    @Override
    public ServiceDescriptor initService(OperationResult getCapabilitiesResult) throws ExceptionReport, OXFException {

        try {
            if (this.serviceVersion.equals(SOSmobileAdapter.SUPPORTED_VERSIONS[0])) {
                net.opengis.sos.x10.CapabilitiesDocument capsDoc = net.opengis.sos.x10.CapabilitiesDocument.Factory.parse(getCapabilitiesResult.getIncomingResultAsStream());
                return initService(capsDoc);
            }
            throw new OXFException("Unsupported Version requested, supported are: "
                    + SOSmobileAdapter.SUPPORTED_VERSIONS);
        }
        catch (XmlException e) {
            throw new OXFException(e);
        }
        catch (IOException e) {
            throw new OXFException(e);
        }
    }

    /**
     * 
     * @param capsDoc
     * @return
     * @throws OXFException
     */
    @Override
    public ServiceDescriptor initService(net.opengis.sos.x10.CapabilitiesDocument capsDoc) throws OXFException {
        SOSmobileCapabilitiesMapper capsMapper = new SOSmobileCapabilitiesMapper();

        ServiceDescriptor result = capsMapper.mapCapabilities(capsDoc);

        return result;
    }

    /**
     * 
     * @param operation
     *        the operation which the adapter has to execute on the service. this operation includes also the
     *        parameter values.
     * 
     * @param parameters
     *        Map which contains the parameters of the operation and the corresponding parameter values
     * 
     * @param serviceVersion
     *        the schema version to which the operation execution shall be conform.
     * 
     * @throws ExceptionReport
     *         Report which contains the service sided exceptions
     * 
     * @throws OXFException
     *         if the sending of the post message failed.<br>
     *         if the specified Operation is not supported.
     * 
     * @return the result of the executed operation
     */
    @Override
    public OperationResult doOperation(Operation operation, ParameterContainer parameters) throws ExceptionReport,
            OXFException {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("starting Operation: " + operation + "\n	with parameters: " + parameters);
        }

        SOSmobileRequestBuilder requestBuilder = (SOSmobileRequestBuilder) SOSmobileRequestBuilderFactory.generateRequestBuilder(this.serviceVersion);

        OperationResult result = null;

        String request = null;

        // GetCapabilities Operation
        if (operation.getName().equals(GET_CAPABILITIES)) {
            request = requestBuilder.buildGetCapabilitiesRequest(parameters);
        }

        // GetObservation Operation
        else if (operation.getName().equals(GET_OBSERVATION)) {
            request = requestBuilder.buildGetObservationRequest(parameters);
        }

        // DescribeSensor Operation
        else if (operation.getName().equals(DESCRIBE_SENSOR)) {
            request = requestBuilder.buildDescribeSensorRequest(parameters);
        }

        // GetFeatureOfInterest Operation
        else if (operation.getName().equals(GET_FEATURE_OF_INTEREST)) {
            request = requestBuilder.buildGetFeatureOfInterestRequest(parameters);
        }

        // InsertObservation Operation
        else if (operation.getName().equals(INSERT_OBSERVATION)) {
            request = requestBuilder.buildInsertObservation(parameters);
        }

        // RegisterSensor Operation
        else if (operation.getName().equals(REGISTER_SENSOR)) {
            request = requestBuilder.buildRegisterSensor(parameters);
        }

        // GetObservationByID Operation
        else if (operation.getName().equals(GET_OBSERVATION_BY_ID)) {
            request = requestBuilder.buildGetObservationByIDRequest(parameters);
        }

        /*
         * MOBILE
         */
        // GetFeatureOfInterestTime Operation
        else if (operation.getName().equals(GET_FEATURE_OF_INTEREST_TIME)) {
            request = requestBuilder.buildGetFeatureOfInterestTimeRequest(parameters);
        }

        // UpdateSensor Operation
        else if (operation.getName().equals(UPDATE_SENSOR)) {
            request = requestBuilder.buildUpdateSensor(parameters);
        }

        // Operation not supported
        else {
            throw new OXFException("The operation '" + operation.getName() + "' is not supported.");
        }

        try {
            InputStream is;

            is = IOHelper.sendPostMessage(operation.getDcps()[0].getHTTPPostRequestMethods().get(0).getOnlineResource().getHref(),
                                          request);

            result = new OperationResult(is, parameters, request);

            try {
                ExceptionReport execRep = parseExceptionReport_100(result);

                throw execRep;
            }
            catch (XmlException e) {
                // parseError --> no ExceptionReport was returned.
                LOGGER.info("Service reported no 1.0.0 exceptions.");
            }
        }
        catch (IOException e) {
            throw new OXFException(e);
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Operation result: " + result);
        }

        return result;
    }

    @Override
    public ISOSRequestBuilder getRequestBuilder() {
        return SOSmobileRequestBuilderFactory.generateRequestBuilder(this.serviceVersion);
    }

    /**
     * 
     * @param result
     * @return
     * @throws XmlException
     */
    private ExceptionReport parseExceptionReport_100(OperationResult result) throws XmlException {

        String requestResult = new String(result.getIncomingResult());

        net.opengis.ows.x11.ExceptionReportDocument xb_execRepDoc = net.opengis.ows.x11.ExceptionReportDocument.Factory.parse(requestResult);
        net.opengis.ows.x11.ExceptionType[] xb_exceptions = xb_execRepDoc.getExceptionReport().getExceptionArray();

        String language = xb_execRepDoc.getExceptionReport().getLang();
        String version = xb_execRepDoc.getExceptionReport().getVersion();

        ExceptionReport oxf_execReport = new ExceptionReport(version, language);
        for (net.opengis.ows.x11.ExceptionType xb_exec : xb_exceptions) {
            String execCode = xb_exec.getExceptionCode();
            String[] execMsgs = xb_exec.getExceptionTextArray();
            String locator = xb_exec.getLocator();

            OWSException owsExec = new OWSException(execMsgs, execCode, result.getSendedRequest(), locator);

            oxf_execReport.addException(owsExec);
        }

        return oxf_execReport;

    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SOSmobileAdapter[serviceVersion: ");
        sb.append(getServiceVersion());
        sb.append(", serviceType: ");
        sb.append(getServiceType());
        sb.append(", description: ");
        sb.append(getDescription());
        sb.append("]");
        return sb.toString();
    }

}