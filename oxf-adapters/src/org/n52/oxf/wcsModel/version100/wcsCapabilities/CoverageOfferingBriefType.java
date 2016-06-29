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
@XmlType(name = "CoverageOfferingBriefType", namespace = "http://www.opengis.net/wcs")
public class CoverageOfferingBriefType
    extends AbstractDescriptionType
{

    @XmlElement(name = "lonLatEnvelope", namespace = "http://www.opengis.net/wcs", type = LonLatEnvelopeType.class)
    protected LonLatEnvelopeType lonLatEnvelope;
    @XmlElement(name = "keywords", namespace = "http://www.opengis.net/wcs", type = Keywords.class)
    protected List<Keywords> keywords;

    /**
     * Gets the value of the lonLatEnvelope property.
     * 
     * @return
     *     possible object is
     *     {@link org.n52.oxf.wcsModel.version100.wcsCapabilities.LonLatEnvelopeType}
     */
    public LonLatEnvelopeType getLonLatEnvelope() {
        return lonLatEnvelope;
    }

    /**
     * Sets the value of the lonLatEnvelope property.
     * 
     * @param value
     *     allowed object is
     *     {@link org.n52.oxf.wcsModel.version100.wcsCapabilities.LonLatEnvelopeType}
     */
    public void setLonLatEnvelope(LonLatEnvelopeType value) {
        this.lonLatEnvelope = value;
    }

    protected List<Keywords> _getKeywords() {
        if (keywords == null) {
            keywords = new ArrayList<Keywords>();
        }
        return keywords;
    }

    /**
     * Gets the value of the keywords property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the keywords property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getKeywords().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link org.n52.oxf.wcsModel.version100.wcsCapabilities.Keywords}
     * 
     */
    public List<Keywords> getKeywords() {
        return this._getKeywords();
    }

}
