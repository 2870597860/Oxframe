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
 
 Created on: 22.07.2007
 *********************************************************************************/

package org.n52.oxf.feeder.transactional;

import java.net.URL;
import java.util.Arrays;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.n52.oxf.OXFException;
import org.n52.oxf.owsCommon.ExceptionReport;
import org.n52.oxf.owsCommon.OWSException;
import org.n52.oxf.owsCommon.capabilities.Operation;
import org.n52.oxf.serviceAdapters.OperationResult;
import org.n52.oxf.serviceAdapters.ParameterContainer;
import org.n52.oxf.serviceAdapters.sos.ISOSRequestBuilder;
import org.n52.oxf.serviceAdapters.sos.SOSAdapter;
import org.n52.oxf.serviceAdapters.sosMobile.SOSmobileAdapter;
import org.n52.oxf.serviceAdapters.sosMobile.SOSmobileRequestBuilder;
import org.n52.oxf.util.LoggingHandler;

/**
 * 
 * Class encapsulates all information necessary for registering a sensor with a SOS. It builds the parameter
 * container and has the register() method to do the registration.
 * 
 * @author <a href="mailto:daniel.nuest@uni-muenster.de">Daniel Nüst</a>
 * 
 */
public class RegisterMobileSensor extends RegisterSensor {

    private static Logger LOGGER = LoggingHandler.getLogger(RegisterMobileSensor.class);

    private String lon;

    private String lat;

    private boolean positionFixed;

    private String positionName;

    private boolean status;

    private boolean mobile;

    private ParameterContainer[] inputList;

    private ParameterContainer[] outputList;

    private String dFName;

    private String dFId;

    private String dFDesc;

    private String dFLocLinRing;

    private String dFLocSRS;

    private String positionReferenceFrame;

    /**
     * 
     * @param feedProperties
     */
    public RegisterMobileSensor(FeedProperties feedProperties) {
        this(feedProperties.getPositionReferenceFrame(),
             feedProperties.getServiceType(),
             feedProperties.getServiceVersion(),
             feedProperties.getServiceURL(),
             feedProperties.getSensorID(),
             feedProperties.getLat(),
             feedProperties.getLon(),
             feedProperties.getObservedProperty(),
             feedProperties.isPositionFixed(),
             feedProperties.getPositionName(),
             feedProperties.getUom(),
             feedProperties.isStatus(),
             feedProperties.isMobile(),
             feedProperties.getInputList(),
             feedProperties.getOutputList(),
             feedProperties.getDomainFeatureName(),
             feedProperties.getDomainFeatureID(),
             feedProperties.getDomainFeatureDescription(),
             feedProperties.getDomainFeatureLocationLinearRing(),
             feedProperties.getDomainFeatureLocationSrsName());
    }

    /**
     * 
     * @param serviceURL
     * @param sensorID
     * @param lat
     * @param lon
     * @param observedProperty
     * @param positionFixed
     * @param positionName
     * @param uom
     * @param mobile
     * @param status
     */
    public RegisterMobileSensor(String positionReferenceFrame,
                                String serviceType,
                                String serviceVersion,
                                URL serviceURL,
                                String sensorID,
                                String lat,
                                String lon,
                                String observedProperty,
                                boolean positionFixed,
                                String positionName,
                                String uom,
                                boolean status,
                                boolean mobile,
                                ParameterContainer[] inputList,
                                ParameterContainer[] outputList,
                                String dFName,
                                String dFId,
                                String dFDesc,
                                String dFLocLinRing,
                                String dFLocSRS) {

        super(serviceType, serviceVersion, serviceURL, sensorID, observedProperty, uom);

        this.positionReferenceFrame = positionReferenceFrame;
        this.lat = lat;
        this.lon = lon;
        this.observedProperty = observedProperty;
        this.positionFixed = positionFixed;
        this.positionName = positionName;
        this.uom = uom;
        this.status = status;
        this.mobile = mobile;
        this.inputList = inputList;
        this.outputList = outputList;
        this.dFName = dFName;
        this.dFId = dFId;
        this.dFDesc = dFDesc;
        this.dFLocLinRing = dFLocLinRing;
        this.dFLocSRS = dFLocSRS;

        this.sosAdapter = new SOSmobileAdapter(this.serviceVersion);
    }

    /**
     * 
     * @param args
     */
    public static void main(String[] args) {
        FeedProperties properties = new HamburgLissabonFeederProperties();

        RegisterMobileSensor registerM = new RegisterMobileSensor(properties);

        registerM.register();
    }

    public void register() {

        ParameterContainer paramCon = null;
        try {
            paramCon = buildParameterContainer();
        }
        catch (OXFException e1) {
            LOGGER.error("creating parameter container", e1);
        }

        Operation op = new Operation(SOSAdapter.REGISTER_SENSOR,
                                     this.serviceURL.toString() + "?",
                                     this.serviceURL.toString());

        OperationResult opResult = null;
        try {
            opResult = this.sosAdapter.doOperation(op, paramCon);
        }
        catch (OXFException e) {
            LOGGER.error("error in run()", e);
        }
        catch (ExceptionReport e) {
            LOGGER.error("ows error in run()");
            Iterator<OWSException> iter = e.getExceptionsIterator();

            while (iter.hasNext()) {
                OWSException ex = iter.next();
                System.out.println(ex.getExceptionCode() + " --- " + ex.getMessage() + "\n"
                        + Arrays.toString(ex.getExceptionTexts()));
                System.out.println("\n" + ex.getSendedRequest());
            }

            e.printStackTrace();
        }

        if (LOGGER.isDebugEnabled()) {
            System.out.println(opResult);
        }
    }

    /**
     * 
     * @return
     * @throws OXFException
     */
    public ParameterContainer buildParameterContainer() throws OXFException {
        ParameterContainer paramCon = new ParameterContainer();

        paramCon.addParameterShell(SOSmobileRequestBuilder.POSITION_LATITUDE_PARAMETER, this.lat);
        paramCon.addParameterShell(SOSmobileRequestBuilder.POSITION_LONGITUDE_PARAMETER, this.lon);
        paramCon.addParameterShell(SOSmobileRequestBuilder.POSITION_NAME_PARAMETER, this.positionName);
        paramCon.addParameterShell(SOSmobileRequestBuilder.POSITION_REFERENCE_FRAME_PARAMETER,
                                   this.positionReferenceFrame);
        paramCon.addParameterShell(SOSmobileRequestBuilder.POSITION_FIXED_PARAMETER,
                                   Boolean.toString(this.positionFixed));

        paramCon.addParameterShell(SOSmobileRequestBuilder.REGISTER_SENSOR_STATUS_PARAMETER,
                                   Boolean.toString(this.status));
        paramCon.addParameterShell(SOSmobileRequestBuilder.REGISTER_SENSOR_MOBILE_PARAMETER,
                                   Boolean.toString(this.mobile));

        paramCon.addParameterShell(SOSmobileRequestBuilder.REGISTER_SENSOR_INPUT_LIST, this.inputList);
        paramCon.addParameterShell(SOSmobileRequestBuilder.REGISTER_SENSOR_OUTPUT_LIST, this.outputList);

        paramCon.addParameterShell(SOSmobileRequestBuilder.DOMAIN_FEATURE_NAME, this.dFName);
        paramCon.addParameterShell(SOSmobileRequestBuilder.DOMAIN_FEATURE_ID, this.dFId);
        paramCon.addParameterShell(SOSmobileRequestBuilder.DOMAIN_FEATURE_DESCRIPTION, this.dFDesc);
        paramCon.addParameterShell(SOSmobileRequestBuilder.DOMAIN_FEATURE_LOCATION_LINEAR_RING_STRING,
                                   this.dFLocLinRing);
        paramCon.addParameterShell(SOSmobileRequestBuilder.DOMAIN_FEATURE_LOCATION_SRS_NAME, this.dFLocSRS);

        return paramCon;
    }

}
