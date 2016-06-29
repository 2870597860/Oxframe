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
 
 Created on: 17.08.2005
 *********************************************************************************/
package org.n52.oxf.serialization;


/**
 * Interface for XML serialization according to the OGC WebMapContext Spec.
 * 
 * @author <a href="mailto:foerster@52north.org">Theodor Foerster</a>
 */
public interface IContextSerializableXML {

    /**
     * serializes the implementing class (downwardly) compatible to the "Web Map
     * Context Documents" Specification (OGC 05-005) of the OGC in version
     * 1.1.0.
     */
    void serializeToContext(StringBuffer sb);
}