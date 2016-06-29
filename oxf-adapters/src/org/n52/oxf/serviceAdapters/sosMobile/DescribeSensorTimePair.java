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

import org.n52.oxf.owsCommon.capabilities.ITime;

/**
 * 
 * @author <a href="mailto:daniel.nuest@uni-muenster.de">Daniel Nüst</a>
 *
 */
public class DescribeSensorTimePair {

    public static enum SOSmobileDescSensorTimeOps {
        AFTER, BEFORE, EQUALS
    }

    private ITime time;
    private SOSmobileDescSensorTimeOps op;

    /**
     * 
     * @param time
     * @param op
     */
    public DescribeSensorTimePair(ITime time, SOSmobileDescSensorTimeOps op) {
        super();
        this.time = time;
        this.op = op;
    }

    /**
     * @return the time
     */
    public ITime getTime() {
        return time;
    }

    /**
     * @param time
     *        the time to set
     */
    public void setTime(ITime time) {
        this.time = time;
    }

    /**
     * @return the op
     */
    public SOSmobileDescSensorTimeOps getOp() {
        return op;
    }

    /**
     * @param op
     *        the op to set
     */
    public void setOp(SOSmobileDescSensorTimeOps op) {
        this.op = op;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.time);
        sb.append(" - ");
        sb.append(this.op);
        return sb.toString();
    }

}
