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
 
 Created on: 25.07.2005
 *********************************************************************************/

package org.n52.oxf.util;

import java.util.*;

/**
 * 
 * 
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 *
 */
public class OXFEvent extends EventObject {

    private EventName name;
    private Object value = null;
    
    
    public OXFEvent(Object source, EventName eventName, Object eventValue)  {
        super(source);
        
        this.name = eventName;
        this.value = eventValue;
    }


    public Object getValue() {
        return value;
    }


    public EventName getName() {
        return name;
    }
    
    public String toString(){
        return "[name=" + name + ", source= " + source + "]";
    }

}