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

import java.util.Date;

import javax.xml.namespace.QName;

import net.opengis.sensorML.x101.EventDocument.Event;
import net.opengis.sensorML.x101.EventListDocument.EventList.Member;
import net.opengis.swe.x101.DataComponentPropertyType;
import net.opengis.swe.x101.PositionType;
import net.opengis.swe.x101.VectorPropertyType;
import net.opengis.swe.x101.VectorType;
import net.opengis.swe.x101.BooleanDocument.Boolean;
import net.opengis.swe.x101.VectorType.Coordinate;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlCalendar;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.n52.oxf.OXFRuntimeException;
import org.n52.oxf.owsCommon.OwsExceptionReport;
import org.n52.oxf.util.LoggingHandler;
import org.n52.oxf.util.SrsHelper;
import org.n52.oxf.util.XmlBeansHelper;

import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.impl.CoordinateArraySequence;

/**
 * Class represents a timestamp in a procedure history. This class was taken from
 * the SOS service implementation 
 * 
 * @author Christoph Stasch
 * 
 */
public class ProcedureHistoryMember {

    private static Logger LOGGER = LoggingHandler.getLogger(ProcedureHistoryMember.class);

    private static final String SML_EVENT_POSITION_PROPERTY_NAME = "position";

    private static final String SML_EVENT_IS_ACTIVE_PROPERTY_NAME = "active";

    private static final String SML_EVENT_IS_MOBILE_PROPERTY_NAME = "mobile";

    /** name of the member **/
    private String name;

    /** id of sensor */
    private String procID;

    /** position of sensor */
    private Point position;

    /** time of procedure event */
    private Date date;

    /** status of sensor (active or inactive) */
    private boolean isActive;

    /** mobility of sensor (mobile or fixed) */
    private boolean isMobile;

    /** the event **/
    private Event event;

    /**
     * constructor
     * 
     * @param procIDp
     *        id of sensor
     * @param positionp
     *        position of sensor
     * @param isActivep
     *        status of sensor (active or inactive)
     * @param isMobilep
     *        mobility of sensor (mobile or fixed)
     * @param date
     *        time stamp of event
     */
    public ProcedureHistoryMember(String procIDp, Point positionp, boolean isActivep, boolean isMobilep, Date timeStampp) {
        setProcID(procIDp);
        setPosition(positionp);
        setActive(isActivep);
        setMobile(isMobilep);
        setDate(timeStampp);
    }

    /**
     * 
     * @param m
     */
    public ProcedureHistoryMember(Member m) {
        parseMember(m);
    }

    /**
     * Returns true, if sensor is active, false otherwise
     * 
     * @return Returns the isActive
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * sets the isActive property
     * 
     * @param isActive
     *        the isActive to set
     */
    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    /**
     * Returns true, if sensor is mobile, false otherwise
     * 
     * @return Returns the isMobile
     */
    public boolean isMobile() {
        return isMobile;
    }

    /**
     * sets the isMobile property to the passed value
     * 
     * @param isMobile
     *        the isMobile to set
     */
    public void setMobile(boolean isMobile) {
        this.isMobile = isMobile;
    }

    /**
     * Returns the position of the sensor at this history event
     * 
     * @return Returns the position
     */
    public Point getPosition() {
        return position;
    }

    /**
     * sets the position of this sensor at the history event
     * 
     * @param position
     *        the position to set
     */
    public void setPosition(Point position) {
        this.position = position;
    }

    /**
     * Returns id of sensor of this history event
     * 
     * @return Returns the procID
     * 
     */
    public String getProcID() {
        return procID;
    }

    /**
     * sets id of sensor of this history event
     * 
     * @param procID
     *        the procID to set
     */
    public void setProcID(String procID) {
        this.procID = procID;
    }

    /**
     * Returns the timeStamp of this history event
     * 
     * @return Returns the timeStamp
     */
    public Date getDate() {
        return date;
    }

    /**
     * sets the timeStamp of this history event
     * 
     * @param timeStamp
     *        the timeStamp to set
     */
    public void setDate(Date timeStamp) {
        this.date = timeStamp;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *        the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * @param m
     */
    private void parseMember(Member m) {
        this.name = m.getName();
        this.event = m.getEvent();

        XmlCalendar cal = (XmlCalendar) this.event.getDate();
        setDate(cal.getTime());

        setProcID(this.event.getId());

        DataComponentPropertyType[] propertyArray = this.event.getPropertyArray();

        DataComponentPropertyType positionProperty = getPropery(propertyArray, SML_EVENT_POSITION_PROPERTY_NAME);

        XmlObject[] swe_positionArray = positionProperty.selectChildren(new QName(XmlBeansHelper.SWE_1_0_1_NAMESPACE_URI,
                                                                                  "Position"));
        if (swe_positionArray.length > 1) {
            LOGGER.warn("sml:position of Event contains more than one child, all but first are neglected: "
                    + swe_positionArray);
        }
        XmlObject swe_PositionObj = swe_positionArray[0];

        PositionType swe_position;
        try {
            swe_position = PositionType.Factory.parse(swe_PositionObj.xmlText());
        }
        catch (XmlException e) {
            throw new OXFRuntimeException("error parsing sml:position", e);
        }

        setPosition(parsePointPosition(swe_position));

        DataComponentPropertyType isActiveProperty = getPropery(propertyArray, SML_EVENT_IS_ACTIVE_PROPERTY_NAME);
        Boolean active = isActiveProperty.getBoolean();
        setActive(active.getValue());

        DataComponentPropertyType isMobileProperty = getPropery(propertyArray, SML_EVENT_IS_MOBILE_PROPERTY_NAME);
        Boolean mobile = isMobileProperty.getBoolean();
        setMobile(mobile.getValue());
    }

    /**
     * parses point position of SensorML system
     * 
     * @param xb_positionType
     *        XMLBeans representation of position, which should represent point position of sensor system
     * @return Returns JTS Point created from XMLBean
     * @throws OwsExceptionReport
     *         if parsing of point position failed
     */
    public static Point parsePointPosition(PositionType xb_positionType) {
        VectorPropertyType xb_location = xb_positionType.getLocation();
        VectorType xb_vector = xb_location.getVector();
        Coordinate[] xb_coordArray = xb_vector.getCoordinateArray();
        double xCoord = Double.NaN;
        double yCoord = Double.NaN;
        double zCoord = Double.NaN;
        for (Coordinate coord : xb_coordArray) {
            if (coord.getName().equals("xcoord")) {
                xCoord = coord.getQuantity().getValue();
            }
            else if (coord.getName().equals("ycoord")) {
                yCoord = coord.getQuantity().getValue();
            }
            else if (coord.getName().equals("z")) {
                zCoord = coord.getQuantity().getValue();
            }
        }
        if (xb_coordArray.length < 2 || xb_coordArray.length > 3) {
            LOGGER.error("There are 2 or 3 coordinates required");
        }
        if (xCoord == Double.NaN || yCoord == Double.NaN || (xb_coordArray.length == 3 && zCoord == Double.NaN)) {
            LOGGER.error("If there are 2 Coordinates AxisID have to be xcoord and ycoord, if there are 3, axisID have to be x,y and z");
        }

        GeometryFactory geomFac = new GeometryFactory();
        com.vividsolutions.jts.geom.Coordinate coord;

        if (zCoord == Double.NaN) {
            coord = new com.vividsolutions.jts.geom.Coordinate(xCoord, yCoord);
        }
        else {
            coord = new com.vividsolutions.jts.geom.Coordinate(xCoord, yCoord, zCoord);
        }

        com.vividsolutions.jts.geom.Coordinate[] coordArray = {coord};
        CoordinateArraySequence coordSeqArray = new CoordinateArraySequence(coordArray);
        Point point = new Point(coordSeqArray, geomFac);

        String srsString = xb_positionType.getReferenceFrame();
        int epsgCode = SrsHelper.getEpsgCode(srsString);
        point.setSRID(epsgCode);
        return point;
    }

    /**
     * returns first occurence of {@link DataComponentPropertyType} with the given name, or null if no element
     * with that name is not found.
     * 
     * @param propertyArray
     * @param smlEventPositionPropertyName
     * @return
     */
    private DataComponentPropertyType getPropery(DataComponentPropertyType[] propertyArray,
                                                 String smlEventPositionPropertyName) {
        for (DataComponentPropertyType dataComponentPropertyType : propertyArray) {
            if (dataComponentPropertyType.getName().equals(smlEventPositionPropertyName)) {
                return dataComponentPropertyType;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ProcedureHistoryMember [\n	name=");
        sb.append(this.name);
        sb.append("\n	date=");
        sb.append(this.date);
        sb.append("\n	position=");
        sb.append(this.position);
        sb.append("\n	isActive=");
        sb.append(this.isActive);
        sb.append("\n	isMobile=");
        sb.append(this.isMobile);
        sb.append("\n	procID=");
        sb.append(this.procID);
        sb.append("]");
        return sb.toString();
    }

}
