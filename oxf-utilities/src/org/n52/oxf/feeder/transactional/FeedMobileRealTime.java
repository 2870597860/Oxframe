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
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.n52.oxf.OXFException;
import org.n52.oxf.owsCommon.ExceptionReport;
import org.n52.oxf.util.LoggingHandler;
import org.n52.oxf.valueDomains.time.ITimePeriod;
import org.n52.oxf.valueDomains.time.ITimePosition;
import org.n52.oxf.valueDomains.time.TimeFactory;

import com.topografix.gpx.x1.x0.GpxDocument.Gpx.Trk.Trkseg.Trkpt;

/**
 * 
 * A feeder for mobile sensors that utilizes the current system time for creation of sampling times.
 * 
 * @author <a href="mailto:daniel.nuest@uni-muenster.de">Daniel Nüst</a>
 * 
 */
public class FeedMobileRealTime extends FeedMobile {
    
    private static Logger LOGGER = LoggingHandler.getLogger(FeedMobileRealTime.class);

    /**
     * @param feedProperties
     */
    public FeedMobileRealTime(FeedProperties feedProperties) {
        super(feedProperties);
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
    public FeedMobileRealTime(String serviceVersion,
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
        super(serviceVersion,
              observedProperty,
              assignedSensorID,
              valueUnitOfMeasurement,
              domainFeatureName,
              domainFeatureID,
              domainFeatureDescription,
              domainFeatureLocationLinearRing,
              domainFeatureLocationSrsName,
              foiID,
              foiName,
              maxDoubleValue,
              minDoubleValue,
              valueFormat,
              sleepTime,
              serviceUrl,
              points,
              timePeriod);
    }

    @Override
    protected void innerRun() throws OXFException, ExceptionReport {
        // super.innerRun();
        if (this.timeAndPointIter.hasNext()) {

            Pair<ITimePosition, Trkpt> tP = timeAndPointIter.next();

            Date now = new Date();
            String nowString = TimeFactory.ISO8601LocalFormat.format(now);
            ITimePosition nowTime = (ITimePosition) TimeFactory.createTime(nowString);
            tP.setElem1(nowTime);
            
            feedNext(tP);
        }
        else {
            LOGGER.info("Feeding finished!");
            stop();
            System.exit(0);
        }

    }

}
