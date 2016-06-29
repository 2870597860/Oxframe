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

import org.apache.log4j.Logger;
import org.n52.oxf.OXFException;
import org.n52.oxf.owsCommon.ExceptionReport;
import org.n52.oxf.owsCommon.capabilities.Operation;
import org.n52.oxf.serviceAdapters.OperationResult;
import org.n52.oxf.serviceAdapters.ParameterContainer;
import org.n52.oxf.serviceAdapters.sosMobile.SOSmobileAdapter;
import org.n52.oxf.serviceAdapters.sosMobile.SOSmobileRequestBuilder;
import org.n52.oxf.util.LoggingHandler;

/**
 * @author <a href="mailto:daniel.nuest@uni-muenster.de">Daniel Nüst</a>
 * 
 */
public class UpdateMobile {

    private static final String SERVICE_VERSION = "1.0.0";
    private SOSmobileAdapter sosAdapter;
    private URL serviceURL;
    private boolean status;
    private boolean mobile;
    private String lat;
    private String lon;
    private String positionName;
    private boolean positionFixed;
    private String sensorId;
    private String version;
    private String service;
    private String time;
    private String dFName;
    private String dFId;
    private String dFDesc;
    private String dFLocLinRing;
    private String dFLocSRS;
    private String positionReferenceFrame;
    private static Logger LOGGER = LoggingHandler.getLogger(UpdateMobile.class);

    /**
     * @param status
     * @param mobile
     * @param lat
     * @param lon
     * @param positionName
     * @param positionFixed
     * @param sensorId
     * @param version
     * @param service
     * @param time
     * @param name
     * @param id
     * @param desc
     * @param locLinRing
     * @param locSRS
     */
    public UpdateMobile(URL serviceURL,
                        boolean status,
                        boolean mobile,
                        String lat,
                        String lon,
                        String positionName,
                        boolean positionFixed,
                        String positionReferenceFrame,
                        String sensorId,
                        String version,
                        String service,
                        String time,
                        String dFname,
                        String dFid,
                        String dFdesc,
                        String dFlocLinRing,
                        String dFlocSRS) {
        this.status = status;
        this.mobile = mobile;
        this.lat = lat;
        this.lon = lon;
        this.positionName = positionName;
        this.positionFixed = positionFixed;
        this.positionReferenceFrame = positionReferenceFrame;
        this.sensorId = sensorId;
        this.version = version;
        this.service = service;
        this.time = time;
        this.dFName = dFname;
        this.dFId = dFid;
        this.dFDesc = dFdesc;
        this.dFLocLinRing = dFlocLinRing;
        this.dFLocSRS = dFlocSRS;
        this.serviceURL = serviceURL;
    }

    public UpdateMobile(FeedProperties feedProperties) {
        this(feedProperties.getServiceURL(),
             feedProperties.isStatus(),
             feedProperties.isMobile(),
             feedProperties.getLat(),
             feedProperties.getLon(),
             feedProperties.getPositionName(),
             feedProperties.isPositionFixed(),
             feedProperties.getPositionReferenceFrame(),
             feedProperties.getSensorID(),
             feedProperties.getServiceVersion(),
             feedProperties.getServiceType(),
             feedProperties.getTimeString(),
             feedProperties.getDomainFeatureName(),
             feedProperties.getDomainFeatureID(),
             feedProperties.getDomainFeatureDescription(),
             feedProperties.getDomainFeatureLocationLinearRing(),
             feedProperties.getDomainFeatureLocationSrsName());
    }

    /**
     * do the update
     */
    public void update() {
        this.sosAdapter = new SOSmobileAdapter(SERVICE_VERSION);

        ParameterContainer paramCon = null;
        try {
            paramCon = buildParameterContainer();
        }
        catch (OXFException e1) {
            LOGGER.error("creating parameter container", e1);
        }

        Operation op = new Operation(SOSmobileAdapter.UPDATE_SENSOR,
                                     this.serviceURL.toString() + "?",
                                     this.serviceURL.toString());

        System.out.println(op);

        OperationResult opResult = null;
        try {
            opResult = this.sosAdapter.doOperation(op, paramCon);

        }
        catch (ExceptionReport e) {
            e.printStackTrace();
        }
        catch (OXFException e) {
            e.printStackTrace();
        }

        if (LOGGER.isDebugEnabled()) {
            System.out.println(opResult);
        }
    }

    public ParameterContainer buildParameterContainer() throws OXFException {
        ParameterContainer paramCon = new ParameterContainer();

        paramCon.addParameterShell(SOSmobileRequestBuilder.UPDATE_SENSOR_SERVICE_PARAMETER, this.service);

        paramCon.addParameterShell(SOSmobileRequestBuilder.UPDATE_SENSOR_VERSION_PARAMETER, this.version);

        paramCon.addParameterShell(SOSmobileRequestBuilder.UPDATE_SENSOR_SENSOR_ID_PARAMETER, this.sensorId);

        /** Time **/
        paramCon.addParameterShell(SOSmobileRequestBuilder.UPDATE_SENSOR_TIME_PARAMETER, this.time);

        /** Position **/
        paramCon.addParameterShell(SOSmobileRequestBuilder.POSITION_LATITUDE_PARAMETER, this.lat);
        paramCon.addParameterShell(SOSmobileRequestBuilder.POSITION_LONGITUDE_PARAMETER, this.lon);
        paramCon.addParameterShell(SOSmobileRequestBuilder.POSITION_NAME_PARAMETER, this.positionName);
        paramCon.addParameterShell(SOSmobileRequestBuilder.POSITION_FIXED_PARAMETER,
                                   Boolean.toString(this.positionFixed));
        paramCon.addParameterShell(SOSmobileRequestBuilder.POSITION_REFERENCE_FRAME_PARAMETER,
                                   this.positionReferenceFrame);

        /** DomainFeature **/
        paramCon.addParameterShell(SOSmobileRequestBuilder.DOMAIN_FEATURE_NAME, this.dFName);
        paramCon.addParameterShell(SOSmobileRequestBuilder.DOMAIN_FEATURE_ID, this.dFId);
        paramCon.addParameterShell(SOSmobileRequestBuilder.DOMAIN_FEATURE_DESCRIPTION, this.dFDesc);
        paramCon.addParameterShell(SOSmobileRequestBuilder.DOMAIN_FEATURE_LOCATION_LINEAR_RING_STRING,
                                   this.dFLocLinRing);
        paramCon.addParameterShell(SOSmobileRequestBuilder.DOMAIN_FEATURE_LOCATION_SRS_NAME, this.dFLocSRS);

        /** more attributes **/
        paramCon.addParameterShell(SOSmobileRequestBuilder.UPDATE_SENSOR_MOBILE_PARAMETER,
                                   Boolean.toString(this.status));
        paramCon.addParameterShell(SOSmobileRequestBuilder.UPDATE_SENSOR_ACTIVE_PARAMETER,
                                   Boolean.toString(this.mobile));

        return paramCon;
    }

    /**
     * @return the status
     */
    public boolean isStatus() {
        return status;
    }

    /**
     * @param status
     *        the status to set
     */
    public void setStatus(boolean status) {
        this.status = status;
    }

    /**
     * @return the mobile
     */
    public boolean isMobile() {
        return mobile;
    }

    /**
     * @param mobile
     *        the mobile to set
     */
    public void setMobile(boolean mobile) {
        this.mobile = mobile;
    }

    /**
     * @return the lat
     */
    public String getLat() {
        return lat;
    }

    /**
     * @param lat
     *        the lat to set
     */
    public void setLat(String lat) {
        this.lat = lat;
    }

    /**
     * @return the lon
     */
    public String getLon() {
        return lon;
    }

    /**
     * @param lon
     *        the lon to set
     */
    public void setLon(String lon) {
        this.lon = lon;
    }

    /**
     * @return the positionName
     */
    public String getPositionName() {
        return positionName;
    }

    /**
     * @param positionName
     *        the positionName to set
     */
    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    /**
     * @return the positionFixed
     */
    public boolean isPositionFixed() {
        return positionFixed;
    }

    /**
     * @param positionFixed
     *        the positionFixed to set
     */
    public void setPositionFixed(boolean positionFixed) {
        this.positionFixed = positionFixed;
    }

    /**
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * @param version
     *        the version to set
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * @return the time
     */
    public String getTime() {
        return time;
    }

    /**
     * @param time
     *        the time to set
     */
    public void setTime(String time) {
        this.time = time;
    }

    /**
     * @return the positionReferenceFrame
     */
    public String getPositionReferenceFrame() {
        return positionReferenceFrame;
    }

    /**
     * @param positionReferenceFrame
     *        the positionReferenceFrame to set
     */
    public void setPositionReferenceFrame(String positionReferenceFrame) {
        this.positionReferenceFrame = positionReferenceFrame;
    }
}
