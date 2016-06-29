//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.0-b11-EA 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2005.06.25 at 02:38:11 CEST 
//


package org.n52.oxf.wcsModel.version100.wcsCapabilities;

import javax.xml.bind.annotation.*;

@XmlAccessorType(value = XmlAccessType.FIELD)
@XmlType(name = "ContactType", namespace = "http://www.opengis.net/wcs")
public class ContactType {

    @XmlElement(name = "phone", namespace = "http://www.opengis.net/wcs", type = TelephoneType.class)
    protected TelephoneType phone;
    @XmlElement(name = "address", namespace = "http://www.opengis.net/wcs", type = AddressType.class)
    protected AddressType address;
    @XmlElement(name = "onlineResource", namespace = "http://www.opengis.net/wcs", type = OnlineResourceType.class)
    protected OnlineResourceType onlineResource;

    /**
     * Gets the value of the phone property.
     * 
     * @return
     *     possible object is
     *     {@link org.n52.oxf.wcsModel.version100.wcsCapabilities.TelephoneType}
     */
    public TelephoneType getPhone() {
        return phone;
    }

    /**
     * Sets the value of the phone property.
     * 
     * @param value
     *     allowed object is
     *     {@link org.n52.oxf.wcsModel.version100.wcsCapabilities.TelephoneType}
     */
    public void setPhone(TelephoneType value) {
        this.phone = value;
    }

    /**
     * Gets the value of the address property.
     * 
     * @return
     *     possible object is
     *     {@link org.n52.oxf.wcsModel.version100.wcsCapabilities.AddressType}
     */
    public AddressType getAddress() {
        return address;
    }

    /**
     * Sets the value of the address property.
     * 
     * @param value
     *     allowed object is
     *     {@link org.n52.oxf.wcsModel.version100.wcsCapabilities.AddressType}
     */
    public void setAddress(AddressType value) {
        this.address = value;
    }

    /**
     * Gets the value of the onlineResource property.
     * 
     * @return
     *     possible object is
     *     {@link org.n52.oxf.wcsModel.version100.wcsCapabilities.OnlineResourceType}
     */
    public OnlineResourceType getOnlineResource() {
        return onlineResource;
    }

    /**
     * Sets the value of the onlineResource property.
     * 
     * @param value
     *     allowed object is
     *     {@link org.n52.oxf.wcsModel.version100.wcsCapabilities.OnlineResourceType}
     */
    public void setOnlineResource(OnlineResourceType value) {
        this.onlineResource = value;
    }

}
