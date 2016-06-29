//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.0-b11-EA 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2005.06.25 at 02:38:11 CEST 
//


package org.n52.oxf.wcsModel.version100.wcsCapabilities;

import java.util.*;
import javax.xml.bind.annotation.*;

@XmlAccessorType(value = XmlAccessType.FIELD)
@XmlType(name = "TelephoneType", namespace = "http://www.opengis.net/wcs")
public class TelephoneType {

    @XmlElement(name = "voice", namespace = "http://www.opengis.net/wcs", type = String.class)
    protected List<String> voice;
    @XmlElement(name = "facsimile", namespace = "http://www.opengis.net/wcs", type = String.class)
    protected List<String> facsimile;

    protected List<String> _getVoice() {
        if (voice == null) {
            voice = new ArrayList<String>();
        }
        return voice;
    }

    /**
     * Gets the value of the voice property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the voice property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getVoice().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link java.lang.String}
     * 
     */
    public List<String> getVoice() {
        return this._getVoice();
    }

    protected List<String> _getFacsimile() {
        if (facsimile == null) {
            facsimile = new ArrayList<String>();
        }
        return facsimile;
    }

    /**
     * Gets the value of the facsimile property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the facsimile property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFacsimile().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link java.lang.String}
     * 
     */
    public List<String> getFacsimile() {
        return this._getFacsimile();
    }

}