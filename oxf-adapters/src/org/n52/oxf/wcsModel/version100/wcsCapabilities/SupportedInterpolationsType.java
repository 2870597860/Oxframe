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
@XmlType(name = "SupportedInterpolationsType", namespace = "http://www.opengis.net/wcs")
public class SupportedInterpolationsType {

    @XmlElement(name = "interpolationMethod", namespace = "http://www.opengis.net/wcs", type = InterpolationMethodType.class)
    protected List<InterpolationMethodType> interpolationMethod;
    @XmlAttribute(name = "default", namespace = "")
    protected InterpolationMethodType _default;

    protected List<InterpolationMethodType> _getInterpolationMethod() {
        if (interpolationMethod == null) {
            interpolationMethod = new ArrayList<InterpolationMethodType>();
        }
        return interpolationMethod;
    }

    /**
     * Gets the value of the interpolationMethod property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the interpolationMethod property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInterpolationMethod().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link org.n52.oxf.wcsModel.version100.wcsCapabilities.InterpolationMethodType}
     * 
     */
    public List<InterpolationMethodType> getInterpolationMethod() {
        return this._getInterpolationMethod();
    }

    /**
     * Gets the value of the default property.
     * 
     * @return
     *     possible object is
     *     {@link org.n52.oxf.wcsModel.version100.wcsCapabilities.InterpolationMethodType}
     */
    public InterpolationMethodType getDefault() {
        return _default;
    }

    /**
     * Sets the value of the default property.
     * 
     * @param value
     *     allowed object is
     *     {@link org.n52.oxf.wcsModel.version100.wcsCapabilities.InterpolationMethodType}
     */
    public void setDefault(InterpolationMethodType value) {
        this._default = value;
    }

}
