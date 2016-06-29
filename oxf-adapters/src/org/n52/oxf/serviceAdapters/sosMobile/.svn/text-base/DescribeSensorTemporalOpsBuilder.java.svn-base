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

import net.opengis.sos.x10.DescribeSensorDocument.DescribeSensor;
import net.opengis.sos.x10.DescribeSensorDocument.DescribeSensor.Time;

import org.apache.xmlbeans.XmlObject;
import org.n52.oxf.owsCommon.capabilities.ITime;
import org.n52.oxf.serviceAdapters.ParameterContainer;
import org.n52.oxf.serviceAdapters.ParameterShell;
import org.n52.oxf.valueDomains.time.ITimePosition;

/**
 * 
 * @author <a href="mailto:daniel.nuest@uni-muenster.de">Daniel Nüst</a>
 *
 */
public class DescribeSensorTemporalOpsBuilder extends AbstractTemporalOpsBuilder {

    /**
	 * 
	 */
    public DescribeSensorTemporalOpsBuilder() {
        //
    }

    /**
     * 
     * @param descSensor
     * @param parameters
     */
    @Override
    public void buildTM_After(XmlObject insertInto, ParameterContainer parameters) {
        DescribeSensor descSensor = (DescribeSensor) insertInto;
        
        ParameterShell afterShell = parameters.getParameterShellWithCommonName(TM_AFTER_PARAMETER);
        if (afterShell.hasSingleSpecifiedValue()) {
            ITimePosition timePosition = (ITimePosition) afterShell.getSpecifiedValue();
            Time time = descSensor.addNewTime();
            buildTM_After(time, timePosition);
        }
        else if (afterShell.hasMultipleSpecifiedValues()) {
            ITime[] times = (ITime[]) afterShell.getSpecifiedValueArray();
    
            for (ITime t : times) {
                Time time = descSensor.addNewTime();
                buildTM_After(time, t);
            }
        }
    }

    /**
     * 
     * @param descSensor
     * @param parameters
     */
    @Override
    public void buildTM_Before(XmlObject insertInto, ParameterContainer parameters) {
        DescribeSensor descSensor = (DescribeSensor) insertInto;
        
        ParameterShell beforeShell = parameters.getParameterShellWithCommonName(TM_BEFORE_PARAMETER);
        if (beforeShell.hasSingleSpecifiedValue()) {
            ITimePosition timePosition = (ITimePosition) beforeShell.getSpecifiedValue();
            Time time = descSensor.addNewTime();
            buildTM_Before(time, timePosition);
        }
        else if (beforeShell.hasMultipleSpecifiedValues()) {
            ITime[] times = (ITime[]) beforeShell.getSpecifiedValueArray();
    
            for (ITime t : times) {
                Time time = descSensor.addNewTime();
                buildTM_Before(time, t);
            }
        }
    }

    /**
     * 
     * @param descSensor
     * @param parameters
     */
    @Override
    public void buildTM_Equals(XmlObject insertInto, ParameterContainer parameters) {
        DescribeSensor descSensor = (DescribeSensor) insertInto;
    
        ParameterShell equalsShell = parameters.getParameterShellWithCommonName(TM_EQUALS_PARAMETER);
        if (equalsShell.hasSingleSpecifiedValue()) {
            ITime timePosition = (ITime) equalsShell.getSpecifiedValue();
            Time time = descSensor.addNewTime();
            buildTM_Equals(time, timePosition);
        }
        else if (equalsShell.hasMultipleSpecifiedValues()) {
            ITime[] times = (ITime[]) equalsShell.getSpecifiedValueArray();

            for (ITime t : times) {
                Time time = descSensor.addNewTime();
                buildTM_Equals(time, t);
            }
        }
    }

    @Override
    public void buildTM_During(XmlObject insertInto, ParameterContainer parameters) {
        DescribeSensor descSensor = (DescribeSensor) insertInto;
        
        ParameterShell duringShell = parameters.getParameterShellWithCommonName(TM_DURING_PARAMETER);
        if (duringShell.hasSingleSpecifiedValue()) {
            ITime timePosition = (ITime) duringShell.getSpecifiedValue();
            Time time = descSensor.addNewTime();
            buildTM_During(time, timePosition);
        }
        else if (duringShell.hasMultipleSpecifiedValues()) {
            ITime[] times = (ITime[]) duringShell.getSpecifiedValueArray();

            for (ITime t : times) {
                Time time = descSensor.addNewTime();
                buildTM_During(time, t);
            }
        }
    }

}
