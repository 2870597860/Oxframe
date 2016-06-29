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
@XmlType(name = "AddressType", namespace = "http://www.opengis.net/wcs")
public class AddressType {

    @XmlElement(name = "deliveryPoint", namespace = "http://www.opengis.net/wcs", type = String.class)
    protected List<String> deliveryPoint;
    @XmlElement(name = "city", namespace = "http://www.opengis.net/wcs", type = String.class)
    protected String city;
    @XmlElement(name = "administrativeArea", namespace = "http://www.opengis.net/wcs", type = String.class)
    protected String administrativeArea;
    @XmlElement(name = "postalCode", namespace = "http://www.opengis.net/wcs", type = String.class)
    protected String postalCode;
    @XmlElement(name = "country", namespace = "http://www.opengis.net/wcs", type = String.class)
    protected String country;
    @XmlElement(name = "electronicMailAddress", namespace = "http://www.opengis.net/wcs", type = String.class)
    protected List<String> electronicMailAddress;

    protected List<String> _getDeliveryPoint() {
        if (deliveryPoint == null) {
            deliveryPoint = new ArrayList<String>();
        }
        return deliveryPoint;
    }

    /**
     * Gets the value of the deliveryPoint property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the deliveryPoint property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDeliveryPoint().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link java.lang.String}
     * 
     */
    public List<String> getDeliveryPoint() {
        return this._getDeliveryPoint();
    }

    /**
     * Gets the value of the city property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    public String getCity() {
        return city;
    }

    /**
     * Sets the value of the city property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    public void setCity(String value) {
        this.city = value;
    }

    /**
     * Gets the value of the administrativeArea property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    public String getAdministrativeArea() {
        return administrativeArea;
    }

    /**
     * Sets the value of the administrativeArea property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    public void setAdministrativeArea(String value) {
        this.administrativeArea = value;
    }

    /**
     * Gets the value of the postalCode property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * Sets the value of the postalCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    public void setPostalCode(String value) {
        this.postalCode = value;
    }

    /**
     * Gets the value of the country property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    public String getCountry() {
        return country;
    }

    /**
     * Sets the value of the country property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    public void setCountry(String value) {
        this.country = value;
    }

    protected List<String> _getElectronicMailAddress() {
        if (electronicMailAddress == null) {
            electronicMailAddress = new ArrayList<String>();
        }
        return electronicMailAddress;
    }

    /**
     * Gets the value of the electronicMailAddress property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the electronicMailAddress property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getElectronicMailAddress().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link java.lang.String}
     * 
     */
    public List<String> getElectronicMailAddress() {
        return this._getElectronicMailAddress();
    }

}
