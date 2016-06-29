//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.0-b11-EA 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2005.06.25 at 02:38:11 CEST 
//


package org.n52.oxf.serviceAdapters.wcs.model.version100.gml;

import java.util.*;
import javax.xml.bind.annotation.*;

@XmlAccessorType(value = XmlAccessType.FIELD)
@XmlType(name = "RectifiedGridType", namespace = "http://www.opengis.net/gml")
public class RectifiedGridType
    extends GridType
{

    @XmlElement(name = "origin", namespace = "http://www.opengis.net/gml", type = PointType.class)
    protected PointType origin;
    @XmlElement(name = "offsetVector", namespace = "http://www.opengis.net/gml", type = VectorType.class)
    protected List<VectorType> offsetVector;

    /**
     * Gets the value of the origin property.
     * 
     * @return
     *     possible object is
     *     {@link org.n52.oxf.serviceAdapters.wcs.model.version100.gml.PointType}
     */
    public PointType getOrigin() {
        return origin;
    }

    /**
     * Sets the value of the origin property.
     * 
     * @param value
     *     allowed object is
     *     {@link org.n52.oxf.serviceAdapters.wcs.model.version100.gml.PointType}
     */
    public void setOrigin(PointType value) {
        this.origin = value;
    }

    protected List<VectorType> _getOffsetVector() {
        if (offsetVector == null) {
            offsetVector = new ArrayList<VectorType>();
        }
        return offsetVector;
    }

    /**
     * Gets the value of the offsetVector property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the offsetVector property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOffsetVector().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link org.n52.oxf.serviceAdapters.wcs.model.version100.gml.VectorType}
     * 
     */
    public List<VectorType> getOffsetVector() {
        return this._getOffsetVector();
    }

}
