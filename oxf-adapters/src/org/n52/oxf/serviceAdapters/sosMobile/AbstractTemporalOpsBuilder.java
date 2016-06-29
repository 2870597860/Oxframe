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

import javax.xml.namespace.QName;

import net.opengis.gml.AbstractTimeGeometricPrimitiveType;
import net.opengis.gml.AbstractTimeObjectType;
import net.opengis.gml.AbstractTimePrimitiveType;
import net.opengis.gml.TimeInstantType;
import net.opengis.gml.TimePeriodType;
import net.opengis.gml.TimePositionType;
import net.opengis.ogc.BinaryTemporalOpType;
import net.opengis.ogc.PropertyNameType;
import net.opengis.ogc.TMAfterDocument;
import net.opengis.ogc.TMBeforeDocument;
import net.opengis.ogc.TMDuringDocument;
import net.opengis.ogc.TMEqualsDocument;
import net.opengis.ogc.TemporalOpsType;
import net.opengis.sos.x10.DescribeSensorDocument.DescribeSensor.Time;

import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
import org.n52.oxf.OXFRuntimeException;
import org.n52.oxf.owsCommon.capabilities.ITime;
import org.n52.oxf.serviceAdapters.ParameterContainer;
import org.n52.oxf.util.XmlBeansHelper;
import org.n52.oxf.valueDomains.time.ITimePeriod;
import org.n52.oxf.valueDomains.time.ITimePosition;

/**
 * This abstract class gives helper methods to build gml time elements to subclasses that apply them to build
 * the temporal filters for a certain operation request.
 * 
 * Currently only temporal filters supported by the 52�North SOS are required to be overridden.
 * 
 * @author <a href="mailto:daniel.nuest@uni-muenster.de">Daniel Nüst</a>
 * 
 */
public abstract class AbstractTemporalOpsBuilder {

    public static final String TM_AFTER_PARAMETER = "tm_after";

    public static final String TM_BEFORE_PARAMETER = "tm_before";

    public static final String TM_EQUALS_PARAMETER = "tm_equals";

    public static final String TM_DURING_PARAMETER = "tm_during";

    public static final String TM_TIME_PROPERTY_NAME_PARAMETER = "urn:ogc:data:time:iso8601";

    public abstract void buildTM_After(XmlObject insertInto, ParameterContainer parameters);

    public abstract void buildTM_Before(XmlObject insertInto, ParameterContainer parameters);

    public abstract void buildTM_Equals(XmlObject insertInto, ParameterContainer parameters);

    public abstract void buildTM_During(XmlObject insertInto, ParameterContainer parameters);

    /**
     * 
     * @param time
     * @param timePosition
     */
    protected void buildTM_Equals(Time xb_time, ITime time) {
        TemporalOpsType temporalOp = xb_time.addNewTemporalOps();

        TMEqualsDocument tm_equalsDoc = TMEqualsDocument.Factory.newInstance();
        BinaryTemporalOpType binTemporalOp = tm_equalsDoc.addNewTMEquals();

        if (time instanceof ITimePosition) {
            ITimePosition timePos = (ITimePosition) time;
            build8601TimeInstant(binTemporalOp, timePos);
        }
        else if (time instanceof ITimePeriod) {
            ITimePeriod timePeriod = (ITimePeriod) time;
            build8601TimePeriod(binTemporalOp, timePeriod);
        }
        else
            throw new OXFRuntimeException("Invalid parameter: for TM_Equals a time position or period is required.");

        temporalOp.set(tm_equalsDoc);
        xb_time.set(temporalOp);
    }
    
    /**
     * 
     * @param time
     * @param timePosition
     */
    protected void buildTM_During(Time xb_time, ITime time) {
        TemporalOpsType temporalOp = xb_time.addNewTemporalOps();

        TMDuringDocument tm_duringDoc = TMDuringDocument.Factory.newInstance();
        BinaryTemporalOpType binTemporalOp = tm_duringDoc.addNewTMDuring();

        if (time instanceof ITimePosition) {
            ITimePosition timePos = (ITimePosition) time;
            build8601TimeInstant(binTemporalOp, timePos);
        }
        else if (time instanceof ITimePeriod) {
            ITimePeriod timePeriod = (ITimePeriod) time;
            build8601TimePeriod(binTemporalOp, timePeriod);
        }
        else
            throw new OXFRuntimeException("Invalid parameter: for TM_During a time position or period is required.");

        temporalOp.set(tm_duringDoc);
        xb_time.set(temporalOp);
    }

    /**
     * 
     * @param xb_binTemporalOp
     * @param timePeriod
     */
    protected void build8601TimePeriod(BinaryTemporalOpType xb_binTemporalOp, ITimePeriod timePeriod) {
        AbstractTimeObjectType xb_timeObject = xb_binTemporalOp.addNewTimeObject();

        AbstractTimePrimitiveType xb_timePrimitive = (AbstractTimePrimitiveType) xb_timeObject.substitute(new QName(XmlBeansHelper.GML_NAMESPACE_URI,
                                                                                                                    "_TimePrimitive"),
                                                                                                          AbstractTimePrimitiveType.type);
        AbstractTimeGeometricPrimitiveType xb_timeGeometricPrimitive = (AbstractTimeGeometricPrimitiveType) xb_timePrimitive.substitute(new QName(XmlBeansHelper.GML_NAMESPACE_URI,
                                                                                                                                                  "_TimeGeometricPrimitive"),
                                                                                                                                        AbstractTimeGeometricPrimitiveType.type);
        TimePeriodType xb_timePeriod = (TimePeriodType) xb_timeGeometricPrimitive.substitute(new QName(XmlBeansHelper.GML_NAMESPACE_URI,
                                                                                                       "TimePeriod"),
                                                                                             TimePeriodType.type);
        
        /** begin **/
        xb_timePeriod.addNewBeginPosition().setStringValue(timePeriod.getStart().toISO8601Format());
//        TimeInstantPropertyType xb_begin = xb_timePeriod.addNewBegin();
//        TimeInstantType xb_beginInstant = xb_begin.addNewTimeInstant();
//        TimePositionType xb_beginTimePos = xb_beginInstant.addNewTimePosition();
//        xb_beginTimePos.setStringValue(timePeriod.getStart().toISO8601Format());

        /** end **/
        xb_timePeriod.addNewEndPosition().setStringValue(timePeriod.getEnd().toISO8601Format());
//        TimeInstantPropertyType xb_end = xb_timePeriod.addNewEnd();
//        TimeInstantType xb_endInstant = xb_end.addNewTimeInstant();
//        TimePositionType xb_endTimePos = xb_endInstant.addNewTimePosition();
//        xb_endTimePos.setStringValue(timePeriod.getStart().toISO8601Format());

        PropertyNameType xb_propertyName = xb_binTemporalOp.addNewPropertyName();
        XmlCursor cursor = xb_propertyName.newCursor();
        cursor.setTextValue(TM_TIME_PROPERTY_NAME_PARAMETER);
    }

    /**
     * 
     * @param describeSensorTime
     * @param time
     */
    protected void buildTM_Before(Time describeSensorTime, ITime time) {
        TemporalOpsType temporalOp = describeSensorTime.addNewTemporalOps();

        TMBeforeDocument tm_beforeDoc = TMBeforeDocument.Factory.newInstance();
        BinaryTemporalOpType binTemporalOp = tm_beforeDoc.addNewTMBefore();

        if (time instanceof ITimePosition) {
            ITimePosition timePos = (ITimePosition) time;
            build8601TimeInstant(binTemporalOp, timePos);
        }
        else
            throw new OXFRuntimeException("Invalid parameter: for TM_Before a time position is required.");

        temporalOp.set(tm_beforeDoc);
        describeSensorTime.set(temporalOp);
    }

    /**
     * 
     * @param describeSensorTime
     * @param time
     */
    protected void buildTM_After(Time describeSensorTime, ITime time) {
        TemporalOpsType temporalOp = describeSensorTime.addNewTemporalOps();
        TMAfterDocument tm_afterDoc = TMAfterDocument.Factory.newInstance();
        BinaryTemporalOpType binTemporalOp = tm_afterDoc.addNewTMAfter();

        if (time instanceof ITimePosition) {
            ITimePosition timePos = (ITimePosition) time;
            build8601TimeInstant(binTemporalOp, timePos);
        }
        else
            throw new OXFRuntimeException("Invalid parameter: for TM_After a time position is required.");

        temporalOp.set(tm_afterDoc);
        describeSensorTime.set(temporalOp);
    }

    /**
     * 
     * @param binTemporalOp
     * @param time
     */
    protected void build8601TimeInstant(BinaryTemporalOpType binTemporalOp, ITimePosition time) {
        String timeString = time.toISO8601Format();

        AbstractTimeObjectType timeObject = binTemporalOp.addNewTimeObject();

        AbstractTimePrimitiveType timePrimitive = (AbstractTimePrimitiveType) timeObject.substitute(new QName(XmlBeansHelper.GML_NAMESPACE_URI,
                                                                                                              "_TimePrimitive"),
                                                                                                    AbstractTimePrimitiveType.type);

        AbstractTimeGeometricPrimitiveType timeGeometricPrimitive = (AbstractTimeGeometricPrimitiveType) timePrimitive.substitute(new QName(XmlBeansHelper.GML_NAMESPACE_URI,
                                                                                                                                            "_TimeGeometricPrimitive"),
                                                                                                                                  AbstractTimeGeometricPrimitiveType.type);

        TimeInstantType timeInstant = (TimeInstantType) timeGeometricPrimitive.substitute(new QName(XmlBeansHelper.GML_NAMESPACE_URI,
                                                                                                    "TimeInstant"),
                                                                                          TimeInstantType.type);
        TimePositionType timePos = timeInstant.addNewTimePosition();
        timePos.setStringValue(timeString);

        PropertyNameType propertyName = binTemporalOp.addNewPropertyName();
        XmlCursor cursor = propertyName.newCursor();
        cursor.setTextValue(TM_TIME_PROPERTY_NAME_PARAMETER);
    }

}
