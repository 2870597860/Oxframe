//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.0-b11-EA 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2005.06.25 at 02:38:11 CEST 
//


package org.n52.oxf.wcsModel.version100.wcsCapabilities;

import javax.xml.bind.annotation.*;

@XmlAccessorType(value = XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "rangeSet"
})
@XmlRootElement(name = "rangeSet", namespace = "http://www.opengis.net/wcs")
public class RangeSet {

    @XmlElement(name = "RangeSet", namespace = "http://www.opengis.net/wcs", type = RangeSetType.class)
    protected RangeSetType rangeSet;

    /**
     * Gets the value of the rangeSet property.
     * 
     * @return
     *     possible object is
     *     {@link org.n52.oxf.wcsModel.version100.wcsCapabilities.RangeSetType}
     */
    public RangeSetType getRangeSet() {
        return rangeSet;
    }

    /**
     * Sets the value of the rangeSet property.
     * 
     * @param value
     *     allowed object is
     *     {@link org.n52.oxf.wcsModel.version100.wcsCapabilities.RangeSetType}
     */
    public void setRangeSet(RangeSetType value) {
        this.rangeSet = value;
    }

}
