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
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

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
import org.n52.oxf.valueDomains.time.ITimePeriod;
import org.n52.oxf.valueDomains.time.ITimePosition;
import org.n52.oxf.valueDomains.time.TimeFactory;

import com.topografix.gpx.x1.x0.GpxDocument.Gpx.Trk.Trkseg.Trkpt;

/**
 * Class holds all information about the domain feature and the sensor, as well as the 
 * generation of values.
 * 
 * Once this runnable is started, it sends sensor update requests in a defined intervall
 * to a certain SOS and logs the responses by the service.
 * 
 * @author <a href="mailto:daniel.nuest@uni-muenster.de">Daniel Nüst</a>
 * 
 */
public class FeedMobile implements Runnable {

    private static final long THREAD_SLEEP_TIME_MASTER_WHILE_LOOP = 100;

    private static Logger LOGGER = Logger.getLogger(FeedMobile.class);

    private static DateFormat ISO8601Format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

    private boolean initialized = false;

    private boolean running;

    private ArrayList<Pair<ITimePosition, Trkpt>> timeAndPoint;

    protected Iterator<Pair<ITimePosition, Trkpt>> timeAndPointIter;

    private int feedCounter = 0;

    private SOSmobileAdapter sosAdapter;

    private Random rand = new Random();

    private String serviceVersion;

    private String observedProperty;

    private String assignedSensorID;

    private String valueUnitOfMeasurement;

    private String domainFeatureName;

    private String domainFeatureID;

    private String domainFeatureDescription;

    private String domainFeatureLocationLinearRing;

    private String domainFeatureLocationSrsName;

    private String foiID;

    private String foiName;

    private double maxDoubleValue;

    private double minDoubleValue;

    private DecimalFormat valueFormat;

    private long sleepTime;

    private URL serviceUrl;

    private List<Trkpt> points;

    private ITimePeriod timePeriod;

    /** enable contacting of service here **/
    private boolean sendRequestsToService = true;

    private SOSmobileRequestBuilder requestBuilder;

    private boolean doUpdates = true;

    private boolean doInserts = true;

    private FeedProperties feedProperties = null;

    private UpdateMobile updateMobile;

    private boolean printResultsToSysout = false;

    /**
     * 
     * @param mobileEnabled
     * @param timePeriod2
     * @param points
     */
    public FeedMobile(FeedProperties feedProperties) {
        this(feedProperties.getServiceVersion(),
             feedProperties.getObservedProperty(),
             feedProperties.getSensorID(),
             feedProperties.getValueUnitOfMeasurement(),
             feedProperties.getDomainFeatureName(),
             feedProperties.getDomainFeatureID(),
             feedProperties.getDomainFeatureDescription(),
             feedProperties.getDomainFeatureLocationLinearRing(),
             feedProperties.getDomainFeatureLocationSrsName(),
             feedProperties.getFoiID(),
             feedProperties.getFoiName(),
             feedProperties.getMaxDoubleValue(),
             feedProperties.getMinDoubleValue(),
             feedProperties.getValueFormat(),
             feedProperties.getSleepTime(),
             feedProperties.getServiceURL(),
             feedProperties.getTrackPoints(),
             feedProperties.getTimePeriod());
        this.feedProperties = feedProperties;
    }

    /**
     * @param serviceVersion
     * @param observedProperty
     * @param assignedSensorID
     * @param valueUnitOfMeasurement
     * @param domainFeatureName
     * @param domainFeatureID
     * @param domainFeatureDescription
     * @param domainFeatureLocationLinearRing
     * @param domainFeatureLocationSrsName
     * @param foiID
     * @param foiName
     * @param maxDoubleValue
     * @param minDoubleValue
     * @param valueFormat
     * @param sleepTime
     * @param serviceUrl
     * @param points
     * @param timePeriod
     */
    public FeedMobile(String serviceVersion,
                      String observedProperty,
                      String assignedSensorID,
                      String valueUnitOfMeasurement,
                      String domainFeatureName,
                      String domainFeatureID,
                      String domainFeatureDescription,
                      String domainFeatureLocationLinearRing,
                      String domainFeatureLocationSrsName,
                      String foiID,
                      String foiName,
                      double maxDoubleValue,
                      double minDoubleValue,
                      DecimalFormat valueFormat,
                      long sleepTime,
                      URL serviceUrl,
                      List<Trkpt> points,
                      ITimePeriod timePeriod) {
        this.serviceVersion = serviceVersion;
        this.observedProperty = observedProperty;
        this.assignedSensorID = assignedSensorID;
        this.valueUnitOfMeasurement = valueUnitOfMeasurement;
        this.domainFeatureName = domainFeatureName;
        this.domainFeatureID = domainFeatureID;
        this.domainFeatureDescription = domainFeatureDescription;
        this.domainFeatureLocationLinearRing = domainFeatureLocationLinearRing;
        this.domainFeatureLocationSrsName = domainFeatureLocationSrsName;
        this.foiID = foiID;
        this.foiName = foiName;
        this.maxDoubleValue = maxDoubleValue;
        this.minDoubleValue = minDoubleValue;
        this.valueFormat = valueFormat;
        this.sleepTime = sleepTime;
        this.serviceUrl = serviceUrl;
        this.points = points;
        this.timePeriod = timePeriod;
    }

    /**
		 * 
		 */
    private void initialize() {
        // create data using points and time period

        timeAndPoint = new ArrayList<Pair<ITimePosition, Trkpt>>();

        List<ITimePosition> times = getTimeList();
        Iterator<ITimePosition> timesIter = times.iterator();
        for (Trkpt point : this.points) {
            timeAndPoint.add(new Pair<ITimePosition, Trkpt>(timesIter.next(), point));
        }

        timeAndPointIter = timeAndPoint.iterator();

        this.sosAdapter = new SOSmobileAdapter(this.serviceVersion);

        this.requestBuilder = (SOSmobileRequestBuilder) this.sosAdapter.getRequestBuilder();
        this.requestBuilder.validationMode = false;

        this.updateMobile = new UpdateMobile(feedProperties);

        initialized = true;
        LOGGER.info("initialized, sending (UpdateSensor=" + this.doUpdates + ", InsertObservation=" + this.doInserts
                + ") requests to service=" + sendRequestsToService);
    }

    /**
     * simulate a feed over a certain time period, i.e. use the generated data elements every SLEEP_TIME
     * seconds to create a insert observation
     */
    @Override
    public void run() {
        if ( !initialized) {
            initialize();
        }

        try {
            while (true) {
                if (running) {

                    innerRun();

                    try {
                        Thread.sleep(this.sleepTime);
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    try {
                        Thread.sleep(THREAD_SLEEP_TIME_MASTER_WHILE_LOOP);
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
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
            // e.printStackTrace();
        }
    }

    /**
     * @throws ExceptionReport 
     * @throws OXFException 
     * 
     */
    protected void innerRun() throws OXFException, ExceptionReport {
        if (this.timeAndPointIter.hasNext()) {

            Pair<ITimePosition, Trkpt> tP = timeAndPointIter.next();

            feedNext(tP);
        }
        else {
            LOGGER.info("Feeding finished!");
            stop();
            System.exit(0);
        }

    }

    /**
     * this method builts the request and uses a {@link SOSmobileAdapter} to do the InsertObservation
     * operation.
     * 
     * @param tp
     * 
     * @throws OXFException
     * @throws ExceptionReport
     */
    protected void feedNext(Pair<ITimePosition, Trkpt> tP) throws OXFException, ExceptionReport {
        double value = getRandomDoubleValue();

        OperationResult opResultUpdate = null;
        OperationResult opResultInsert = null;

        StringBuilder sb = new StringBuilder();
        sb.append("start next feeding (");
        sb.append(this.feedCounter);
        sb.append(" of ");
        sb.append(this.points.size());
        sb.append(")    @ ");
        sb.append(tP.getElem2().getLat());
        sb.append(" ");
        sb.append(tP.getElem2().getLon());
        sb.append("    @ ");
        sb.append(tP.getElem1().toISO8601Format());
        sb.append("     active=");
        sb.append(this.feedProperties.getActiveList().get(this.feedCounter).booleanValue());
        sb.append(" mobile=");
        sb.append(this.feedProperties.getMobileList().get(this.feedCounter).booleanValue());

        LOGGER.info(sb.toString());

        if (doUpdates) {
            ParameterContainer paramCon = generateUpdateSensorParameterContainer(tP);

            Operation op = new Operation(SOSmobileAdapter.UPDATE_SENSOR,
                                         this.serviceUrl.toString() + "?",
                                         this.serviceUrl.toString());

            if (sendRequestsToService) {
                opResultUpdate = this.sosAdapter.doOperation(op, paramCon);
            }
        }

        if (doInserts) {
            ParameterContainer paramCon = generateInsertParameterContainer(tP, value);

            Operation op = new Operation(SOSAdapter.INSERT_OBSERVATION,
                                         this.serviceUrl.toString() + "?",
                                         this.serviceUrl.toString());

            if (sendRequestsToService) {
                opResultInsert = this.sosAdapter.doOperation(op, paramCon);
            }

        }

        handleOperationResult(opResultUpdate, opResultInsert);

        this.feedCounter++;
    }

    /**
     * 
     * @param tp
     * @param value
     * @return
     * @throws OXFException
     */
    public ParameterContainer generateUpdateSensorParameterContainer(Pair<ITimePosition, Trkpt> tp) throws OXFException {

        // change necessary values:
        String lat = tp.getElem2().getLat().toPlainString();
        String lon = tp.getElem2().getLon().toPlainString();

        this.updateMobile.setLat(lat);
        this.updateMobile.setLon(lon);
        this.updateMobile.setTime(tp.getElem1().toISO8601Format());

        Boolean active = this.feedProperties.getActiveList().get(this.feedCounter);
        Boolean mobile = this.feedProperties.getMobileList().get(this.feedCounter);

        this.updateMobile.setStatus(active.booleanValue());
        this.updateMobile.setMobile(mobile.booleanValue());

        ParameterContainer paramCon = this.updateMobile.buildParameterContainer();
        return paramCon;
    }

    /**
     * 
     * @param timePositionPair
     * @param value
     * @return
     * @throws OXFException
     */
    public ParameterContainer generateInsertParameterContainer(Pair<ITimePosition, Trkpt> timePositionPair, double value) throws OXFException {

        // put all parameters you want to use into a ParameterContainer:
        ParameterContainer paramCon = new ParameterContainer();

        paramCon.addParameterShell(ISOSRequestBuilder.INSERT_OBSERVATION_SERVICE_PARAMETER, "SOS");
        paramCon.addParameterShell(ISOSRequestBuilder.INSERT_OBSERVATION_VERSION_PARAMETER, this.serviceVersion);
        paramCon.addParameterShell(ISOSRequestBuilder.INSERT_OBSERVATION_OBSERVED_PROPERTY_PARAMETER,
                                   this.observedProperty);
        paramCon.addParameterShell(ISOSRequestBuilder.INSERT_OBSERVATION_SENSOR_ID_PARAMETER, this.assignedSensorID);
        paramCon.addParameterShell(ISOSRequestBuilder.INSERT_OBSERVATION_SAMPLING_TIME,
                                   timePositionPair.getElem1().toISO8601Format());

        paramCon.addParameterShell(ISOSRequestBuilder.INSERT_OBSERVATION_VALUE_UOM_ATTRIBUTE,
                                   this.valueUnitOfMeasurement);
        String formattedValue = this.valueFormat.format(value);
        paramCon.addParameterShell(ISOSRequestBuilder.INSERT_OBSERVATION_VALUE_PARAMETER, formattedValue);

        // DOMAIN FEATURE (stays the same)
        paramCon.addParameterShell(SOSmobileRequestBuilder.DOMAIN_FEATURE_ID, this.domainFeatureID);
        paramCon.addParameterShell(SOSmobileRequestBuilder.DOMAIN_FEATURE_NAME, this.domainFeatureName);
        paramCon.addParameterShell(SOSmobileRequestBuilder.DOMAIN_FEATURE_DESCRIPTION, this.domainFeatureDescription);
        paramCon.addParameterShell(SOSmobileRequestBuilder.DOMAIN_FEATURE_LOCATION_LINEAR_RING_STRING,
                                   this.domainFeatureLocationLinearRing);
        paramCon.addParameterShell(SOSmobileRequestBuilder.DOMAIN_FEATURE_LOCATION_SRS_NAME,
                                   this.domainFeatureLocationSrsName);

        // ADD (new) FOI
        paramCon.addParameterShell(ISOSRequestBuilder.INSERT_OBSERVATION_FOI_ID_PARAMETER, this.foiID + "_"
                + System.currentTimeMillis());

        paramCon.addParameterShell(ISOSRequestBuilder.INSERT_OBSERVATION_NEW_FOI_NAME, this.domainFeatureID + "_"
                + this.foiName + " " + this.feedCounter);
        paramCon.addParameterShell(ISOSRequestBuilder.INSERT_OBSERVATION_NEW_FOI_POSITION,
                                   timePositionPair.getElem2().getLat().toPlainString() + " "
                                           + timePositionPair.getElem2().getLon().toPlainString()); // "40.85 -74.0608");
        paramCon.addParameterShell(ISOSRequestBuilder.INSERT_OBSERVATION_NEW_FOI_POSITION_SRS,
                                   "urn:ogc:def:crs:EPSG:4326");
        paramCon.addParameterShell(ISOSRequestBuilder.INSERT_OBSERVATION_NEW_FOI_DESC, "description of the foi "
                + this.foiName + " " + this.feedCounter);

        return paramCon;
    }

    /**
     * 
     * @param opResult
     * @param opResultInsert
     */
    private void handleOperationResult(OperationResult opResultUpdate, OperationResult opResultInsert) {
        if ( !this.printResultsToSysout) {
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("\n ____  _____ ____  _   _ _   _____ \n|  _ \\| ____/ ___|| | | | | |_   _|\n| |_) |  _| \\___ \\| | | | |   | |  \n|  _ <| |___ ___) | |_| | |___| |  \n|_| \\_\\_____|____/ \\___/|_____|_| \n");

        if (opResultUpdate == null) {
            sb.append("     OperationResult for UPDATE is null.");
        }
        else {
            sb.append(new String(opResultUpdate.getIncomingResult()));
        }
        if (opResultInsert == null) {
            sb.append("     OperationResult for INSERT is null.");
        }
        else {
            sb.append(new String(opResultInsert.getIncomingResult()));
        }
        LOGGER.info(sb.toString());
    }

    /**
     * 
     * @return
     */
    private List<ITimePosition> getTimeList() {
        long start = this.timePeriod.getStart().getCalendar().getTimeInMillis();
        long end = this.timePeriod.getEnd().getCalendar().getTimeInMillis();
        long length = end - start;

        double intervallD = length / this.points.size();
        long intervall = (long) intervallD;

        List<ITimePosition> times = new ArrayList<ITimePosition>();

        // get equally divided time positions
        for (long l = start; l <= end + intervall; l += intervall) {
            Date d = new Date(l);
            String dateString = ISO8601Format.format(d);
            ITimePosition pos = (ITimePosition) TimeFactory.createTime(dateString);
            times.add(pos);
        }

        return times;
    }

    /**
     * 
     * @return
     */
    private double getRandomDoubleValue() {
        double d = this.rand.nextDouble();

        d *= this.maxDoubleValue;

        if (this.minDoubleValue <= d && d <= this.maxDoubleValue) {
            return d;
        }
        return getRandomDoubleValue();
    }

    /**
	 * 
	 */
    synchronized public void start() {
        this.running = true;
        LOGGER.info("start feeding for " + this.assignedSensorID);
    }

    /**
	 * 
	 */
    synchronized public void stop() {
        this.running = false;
        LOGGER.info("stopp");
    }

}
