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
    "section"
})
@XmlRootElement(name = "GetCapabilities", namespace = "http://www.opengis.net/wcs")
public class GetCapabilities {

    @XmlElement(name = "section", namespace = "http://www.opengis.net/wcs", type = CapabilitiesSectionType.class)
    protected CapabilitiesSectionType section;
    @XmlAttribute(name = "service", namespace = "", required = true)
    protected String service;
    @XmlAttribute(name = "updateSequence", namespace = "")
    protected String updateSequence;
    @XmlAttribute(name = "version", namespace = "")
    protected String version;

    /**
     * Gets the value of the section property.
     * 
     * @return
     *     possible object is
     *     {@link org.n52.oxf.wcsModel.version100.wcsCapabilities.CapabilitiesSectionType}
     */
    public CapabilitiesSectionType getSection() {
        return section;
    }

    /**
     * Sets the value of the section property.
     * 
     * @param value
     *     allowed object is
     *     {@link org.n52.oxf.wcsModel.version100.wcsCapabilities.CapabilitiesSectionType}
     */
    public void setSection(CapabilitiesSectionType value) {
        this.section = value;
    }

    /**
     * Gets the value of the service property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    public String getService() {
        if (service == null) {
            return "WCS";
        } else {
            return service;
        }
    }

    /**
     * Sets the value of the service property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    public void setService(String value) {
        this.service = value;
    }

    /**
     * Gets the value of the updateSequence property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    public String getUpdateSequence() {
        return updateSequence;
    }

    /**
     * Sets the value of the updateSequence property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    public void setUpdateSequence(String value) {
        this.updateSequence = value;
    }

    /**
     * Gets the value of the version property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    public String getVersion() {
        if (version == null) {
            return "1.0.0";
        } else {
            return version;
        }
    }

    /**
     * Sets the value of the version property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    public void setVersion(String value) {
        this.version = value;
    }

}
