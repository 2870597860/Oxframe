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
@XmlType(name = "valueEnumBaseType", namespace = "http://www.opengis.net/wcs")
public class ValueEnumBaseType {

    @XmlElements(value = {
        @XmlElement(name = "singleValue", namespace = "http://www.opengis.net/wcs", type = TypedLiteralType.class),
        @XmlElement(name = "interval", namespace = "http://www.opengis.net/wcs", type = Interval.class)
    })
    protected List<Object> intervalOrSingleValue;

    protected List<Object> _getIntervalOrSingleValue() {
        if (intervalOrSingleValue == null) {
            intervalOrSingleValue = new ArrayList<Object>();
        }
        return intervalOrSingleValue;
    }

    /**
     * Gets the value of the intervalOrSingleValue property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the intervalOrSingleValue property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIntervalOrSingleValue().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link org.n52.oxf.wcsModel.version100.wcsCapabilities.TypedLiteralType}
     * {@link org.n52.oxf.wcsModel.version100.wcsCapabilities.Interval}
     * 
     */
    public List<Object> getIntervalOrSingleValue() {
        return this._getIntervalOrSingleValue();
    }

}
