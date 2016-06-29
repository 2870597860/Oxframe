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
 
 Created on: 28.03.2006
 *********************************************************************************/

package org.n52.oxf.serviceAdapters.sas;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import net.opengis.sas.x00.CapabilitiesDocument;

import org.apache.xmlbeans.XmlException;
import org.n52.oxf.OXFException;
import org.n52.oxf.owsCommon.ExceptionReport;
import org.n52.oxf.owsCommon.ServiceDescriptor;
import org.n52.oxf.owsCommon.capabilities.Operation;
import org.n52.oxf.serviceAdapters.IServiceAdapter;
import org.n52.oxf.serviceAdapters.OperationResult;
import org.n52.oxf.serviceAdapters.ParameterContainer;
import org.n52.oxf.util.IOHelper;

/**
 * SAS-Adapter for the ox-framework
 * 
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 */
public class SASAdapter implements IServiceAdapter {

    public static final String GET_CAPABILITIES_OP_NAME = "GetCapabilities";

    public static final String DESCRIBE_SENSOR_OP_NAME = "DescribeSensor";
    
    public static final String ALERT_OP_NAME = "Alert";
    
    public static final String ADVERTISE_OP_NAME = "Advertise";
    public static final String RENEW_ADVERTISEMENT_OP_NAME = "RenewAdvertisement";
    public static final String CANCEL_ADVERTISEMENT_OP_NAME = "CancelAdvertisement";

    public static final String SUBSCRIBE_OP_NAME = "Subscribe";
    public static final String RENEW_SUBSCRIPTION_OP_NAME = "RenewSubscription";
    public static final String CANCEL_SUBSCRIPTION_OP_NAME = "CancelSubscription";


    /**
     * Description of the SOSAdapter
     */
    public static final String DESCRIPTION = "This Class implements the Service Adapter Interface and is"
            + "an SAS Adapter for the OXF Framework";

    /**
     * The Type of the service which is connectable by this ServiceAdapter
     */
    public static final String SERVICE_TYPE = "SAS";

    /**
     * the Versions of the services which are connectable by this ServiceAdapter. Should look like e.g.
     * {"1.1.0","1.2.0"}.
     */
    public static final String[] SUPPORTED_VERSIONS = {"1.0.0"};

    /** the requestbuilder of the adapter */
    private SASRequestBuilder sasrb;

    /**
     * standard constructor
     */
    public SASAdapter() {
        this.sasrb = new SASRequestBuilder();
    }

    /**
     * initializes the ServiceDescriptor by requesting the capabilities document of the SAS.
     * 
     * @param url
     *        the base URL of the SAS
     * 
     * @return the ServiceDescriptor based on the retrieved capabilities document
     * 
     * @throws ExceptionReport
     *         if service side exception occurs
     * @throws OXFException
     *         if internal exception (in general parsing error or Capabilities doc is incorrect) occurs
     * 
     */
    public ServiceDescriptor initService(String url) throws ExceptionReport, OXFException {
        try {
            ParameterContainer paramCon = new ParameterContainer();
            paramCon.addParameterShell(SASRequestBuilder.GET_CAPABILITIES_ACCEPT_VERSIONS_PARAMETER,
                                       SUPPORTED_VERSIONS[0]);
            paramCon.addParameterShell(SASRequestBuilder.GET_CAPABILITIES_SERVICE_PARAMETER,
                                       SERVICE_TYPE);

            OperationResult opResult = doOperation(new Operation(GET_CAPABILITIES_OP_NAME,
                                                                 url.toString() + "?",
                                                                 url.toString()), paramCon);

            CapabilitiesDocument capsDoc = CapabilitiesDocument.Factory.parse(opResult.getIncomingResultAsStream());

            ServiceDescriptor result = SASCapabilitiesMapper.mapCapabilities(capsDoc);

            return result;
        }
        catch (XmlException e) {
            throw new OXFException(e);
        }
        catch (IOException e) {
            throw new OXFException(e);
        }
    }

    /**
     * performs a given service-operation with the given parameters
     */
    public OperationResult doOperation(Operation operation, ParameterContainer parameterContainer) throws ExceptionReport,
            OXFException {

        String request = null;
        
        // GetCapabilities Operation
        if (operation.getName().equals(GET_CAPABILITIES_OP_NAME)) {
            request = SASRequestBuilder.buildCapabilitiesRequest(parameterContainer);
        }

        // Subscribe Operation
        else if (operation.getName().equals(SUBSCRIBE_OP_NAME)) {
            request = SASRequestBuilder.buildSubscribeRequest(parameterContainer);
        }

        // RenewSubscription
        else if (operation.getName().equals(RENEW_SUBSCRIPTION_OP_NAME)) {
            request = SASRequestBuilder.buildRenewSubscriptionRequest(parameterContainer);
        }
        
        // CancelSubscribition Operation
        else if (operation.getName().equals(CANCEL_SUBSCRIPTION_OP_NAME)) {
            request = SASRequestBuilder.buildCancelSubscriptionRequest(parameterContainer);
        }
        
        // Advertise Operation
        else if (operation.getName().equals(ADVERTISE_OP_NAME)) {
            request = SASRequestBuilder.buildAdvertiseRequest(parameterContainer);
        }
        
        // RenewAdvertisement Operation
        else if (operation.getName().equals(RENEW_ADVERTISEMENT_OP_NAME)) {
            request = SASRequestBuilder.buildRenewAdvertisementRequest(parameterContainer);
        }
        
        // CancelAdvertisement Operation
        else if (operation.getName().equals(CANCEL_ADVERTISEMENT_OP_NAME)) {
            request = SASRequestBuilder.buildCancelAdvertisementRequest(parameterContainer);
        }
        
        // DescriSensor Operation
        else if (operation.getName().equals(DESCRIBE_SENSOR_OP_NAME)) {
            request = SASRequestBuilder.buildDescribeSensor(parameterContainer);
        }

        // AlertSensor Operation
        else if (operation.getName().equals(ALERT_OP_NAME)) {
        	request = SASRequestBuilder.buildAlertRequest(parameterContainer);
        }
        
        // Operation not supported
        else {
            throw new OXFException("The operation '" + operation.getName() + "' is not supported.");
        }
        
        try {
            InputStream is = IOHelper.sendPostMessage(operation.getDcps()[0].getHTTPPostRequestMethods().get(0).getOnlineResource().getHref(),
                                                      request);
            
            return new OperationResult(is, parameterContainer, request);
        }
        catch (IOException e) {
            throw new OXFException(e);
        }
    }

    public String getDescription() {
        return DESCRIPTION;
    }

    public String getServiceType() {
        return "OGC:" + SERVICE_TYPE;
    }

    public String[] getSupportedVersions() {
        return SUPPORTED_VERSIONS;
    }

    public String getResourceOperationName() {
        return null;
    }

    public SASRequestBuilder getSASRequestBuilder() {
        return sasrb;
    }

}