//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.0-b11-EA 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2005.06.25 at 02:38:11 CEST 
//


package org.n52.oxf.serviceAdapters.wcs.model.version100.gml;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.*;

@XmlAccessorType(value = XmlAccessType.FIELD)
@XmlType(name = "AbstractMetaDataType", namespace = "http://www.opengis.net/gml")
public abstract class AbstractMetaDataType {

    @XmlValue
    protected String content;
    @XmlAttribute(name = "id", namespace = "http://www.opengis.net/gml")
    @XmlJavaTypeAdapter(value = CollapsedStringAdapter.class)
    @XmlID
    protected String id;

    /**
     * An abstract base type for complex metadata types.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    public String getContent() {
        return content;
    }

    /**
     * An abstract base type for complex metadata types.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    public void setContent(String value) {
        this.content = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    public void setId(String value) {
        this.id = value;
    }

}
