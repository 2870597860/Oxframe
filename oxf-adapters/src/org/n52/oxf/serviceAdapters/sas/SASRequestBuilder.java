/**********************************************************************************
 Copyright (C) 2009
 by 52 North Initiative for Geospatial Open Source Software GmbH

 Contact: Andreas Wytzisk 
 52 North Initiative for Geospatial Open Source Software GmbH
 Martin-Luther-King-Weg 24
 48155 Muenster, Germany
 info@52north.org

 This program is free software; you can redistribute and/or modify it under the
 terms of the GNU General Public License version 2 as published by the Free
 Software Foundation.

 This program is distributed WITHOUT ANY WARRANTY; even without the implied
 WARRANTY OF MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 General Public License for more details.

 You should have received a copy of the GNU General Public License along with this 
 program (see gnu-gplv2.txt). If not, write to the Free Software Foundation, Inc., 
 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA or visit the Free Software
 Foundation web page, http://www.fsf.org.
 
 Created on: 06.01.2006
 *********************************************************************************/

package org.n52.oxf.serviceAdapters.sas;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import net.opengis.sas.x00.AdvertiseDocument;
import net.opengis.sas.x00.AlertDocument;
import net.opengis.sas.x00.CancelSubscriptionDocument;
import net.opengis.sas.x00.GetCapabilitiesDocument;
import net.opengis.sas.x00.RenewSubscriptionDocument;
import net.opengis.sas.x00.SubscribeDocument;
import net.opengis.sas.x00.AdvertiseDocument.Advertise;
import net.opengis.sas.x00.AlertDocument.Alert;
import net.opengis.sas.x00.CancelSubscriptionDocument.CancelSubscription;
import net.opengis.sas.x00.GetCapabilitiesDocument.GetCapabilities;
import net.opengis.sas.x00.MessageStructureDocument.MessageStructure;
import net.opengis.sas.x00.RenewSubscriptionDocument.RenewSubscription;
import net.opengis.sas.x00.SensorDescriptionDocument.SensorDescription;
import net.opengis.sas.x00.SubscribeDocument.Subscribe;
import net.opengis.sas.x00.SubscribeDocument.Subscribe.EventFilter;
import net.opengis.sas.x00.SubscribeDocument.Subscribe.ResultRecipient;
import net.opengis.sas.x00.SubscribeDocument.Subscribe.EventFilter.ValueFilterList;
import net.opengis.sas.x00.SubscribeDocument.Subscribe.EventFilter.ValueFilterList.Member;
import net.opengis.sas.x00.SubscribeDocument.Subscribe.EventFilter.ValueFilterList.Member.ValueFilter;
import net.opengis.sas.x00.SubscribeDocument.Subscribe.EventFilter.ValueFilterList.Member.ValueFilter.FilterCriteria;
import net.opengis.sas.x00.SubscribeDocument.Subscribe.EventFilter.ValueFilterList.Member.ValueFilter.FilterCriteria.IsBetween;
import net.opengis.swe.x10.BlockEncodingPropertyType;
import net.opengis.swe.x10.DataComponentPropertyType;
import net.opengis.swe.x10.UomPropertyType;
import net.opengis.swe.x10.QuantityDocument.Quantity;
import net.opengis.swe.x10.TextBlockDocument.TextBlock;
import net.opengis.wns.x00.NotificationTargetDocument.NotificationTarget;

import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlTokenSource;
import org.n52.oxf.OXFException;
import org.n52.oxf.serviceAdapters.ParameterContainer;
import org.n52.oxf.serviceAdapters.ParameterShell;

/**
 * contains attributes and methods to encode SASOperationRequests as String in xml-format
 * 
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 */
public class SASRequestBuilder {

    public static final String GET_CAPABILITIES_SERVICE_PARAMETER = "service";
    public static final String GET_CAPABILITIES_UPDATE_SEQUENCE_PARAMETER = "updateSequence";
    public static final String GET_CAPABILITIES_ACCEPT_VERSIONS_PARAMETER = "AcceptVersions";
    public static final String GET_CAPABILITIES_SECTIONS_PARAMETER = "Sections";
    public static final String GET_CAPABILITIES_ACCEPT_FORMATS = "AcceptFormats";

    public static final String SUBSCRIBE_SERVICE_PARAM = "service";
    public static final String SUBSCRIBE_VERSION_PARAM = "version";
    public static final String SUBSCRIBE_SENSOR_ID_PARAM = "SensorID";

    // These are parametes that musst be Arrays
    public static final String SUBSCRIBE_FILTER_CRITERIA = "FilterCriteria";
    public static final String SUBSCRIBE_CRITERIA_VALUE = "value";
    public static final String SUBSCRIBE_CRITERIA_UPPER_BOUNDARY = "upper";
    public static final String SUBSCRIBE_CRITERIA_LOWER_BOUNDARY = "lower";
    public static final String SUBSCRIBE_FILTER_DEFINITION = "definition";
    // End Arrays

    public static final String SUBSCRIBE_NOTIFICATION_CHANNEL_SMS = "SMS";
    public static final String SUBSCRIBE_NOTIFICATION_CHANNEL_EMAIL = "Email";

    public static final class Criteria {
        public static final int IS_LESS_THEN = 0;
        public static final int IS_LESS_THEN_OR_EQUAL = 1;
        public static final int IS_GREATER_THEN = 2;
        public static final int IS_GREATER_THEN_OR_EQUAL = 3;
        public static final int IS_EQUAL = 4;
        public static final int IS_NOT_EQUAL = 5;
        public static final int IS_BETWEEN = 6;
        public static final String[] operationNames = new String[] {"less than",
                                                                    "less than or equal",
                                                                    "greater than",
                                                                    "greater than or equal",
                                                                    "equal",
                                                                    "not equal",
                                                                    "between"};
    }

    public static final String CANCEL_SUBSCRIPTION = "CancelSubscription";
    public static final String CANCEL_SUBSCRIPTION_SERVICE_PARAM = "service";
    public static final String CANCEL_SUBSCRIPTION_VERSION_PARAM = "version";
    public static final String CANCEL_SUBSCRIPTION_SUB_ID = "SubscriptionID";

    public static final String SUBSCRIBE_LOCATION_PARAM = "Location";
    public static final String SUBSCRIBE_RESULT_FILTER_PARAM = "ResultFilter";
    public static final String SUBSCRIBE_FOI_NAME_PARAM = "FeatureOfInterestName";
    public static final String SUBSCRIBE_RESULT_REC_PARAM = "ResultRecipient";
    public static final String SUBSCRIBE_WNS_ID = "WnsId";
    public static final String SUBSCRIBE_WNS_URL = "WnsUrl";

    private static final String ADVERTISE_SERVICE_PARAM = "service";
    private static final String ADVERTISE_VERSION_PARAM = "version";
    private static final String ADVERTISE_PHENOMENON_DEFINITION = "phenomenonDef";
    private static final String ADVERTISE_UOM_CODE = "uomCode";
    private static final String ADVERTISE_SENSOR_HREF = "sensorHref";
    private static final String ADVERTISE_COMPONENTS_NAME = "componentsName";

    public static final String ALERT_SENSORID = "SensorID";
    public static final String ALERT_TIMESTAMP = "time";
    public static final String ALERT_DATA = "alertData";

    /**
     * builds the CapabilitiesRequest. <br>
     * <br>
     * For the ParameterContainer 'parameters' are the ParameterShells with the following serviceSidedNames
     * required:
     * <li>service</li>
     * 
     * <br>
     * <br>
     * The following are optional:
     * <li>updateSequence</li>
     * <li>acceptVersions</li>
     * <li>sections</li>
     * <li>acceptFormats</li>
     * 
     * @param parameters
     *        the parameters of the request
     * 
     * @return CapabilitiesRequest in xml-Format as String
     */
    public static String buildCapabilitiesRequest(ParameterContainer parameters) {

        GetCapabilitiesDocument getCapDoc = GetCapabilitiesDocument.Factory.newInstance();

        GetCapabilities getCap = getCapDoc.addNewGetCapabilities();

        //
        // set required elements:
        //  

        getCap.setService((String) parameters.getParameterShellWithServiceSidedName(GET_CAPABILITIES_SERVICE_PARAMETER).getSpecifiedValue());

        return formatStringRequest(getCapDoc);
    }

    /**
     * can be used as a convenience method to build up a ParameterContainer for the Subscribe method.
     * 
     * @throws OXFException
     */
    public static ParameterContainer buildSubscribeParamCon(String service,
                                                            String version,
                                                            String sensorID,
                                                            String sms,
                                                            String email) throws OXFException {
        ParameterContainer parameters = new ParameterContainer();

        parameters.addParameterShell(SUBSCRIBE_SERVICE_PARAM, service);
        parameters.addParameterShell(SUBSCRIBE_VERSION_PARAM, version);

        parameters.addParameterShell(SUBSCRIBE_SENSOR_ID_PARAM, sensorID);

        // parameters.addParameterShell(SUBSCRIBE_FILTER_CRITERIA = "FilterCriteria";
        // parameters.addParameterShell(SUBSCRIBE_CRITERIA_VALUE = "value";
        // parameters.addParameterShell(SUBSCRIBE_CRITERIA_UPPER_BOUNDARY = "upper";
        // parameters.addParameterShell(SUBSCRIBE_CRITERIA_LOWER_BOUNDARY = "lower";
        // parameters.addParameterShell(SUBSCRIBE_FILTER_DEFINITION = "definition";
        if (sms != null) {
            parameters.addParameterShell(SUBSCRIBE_NOTIFICATION_CHANNEL_SMS, sms);
        }
        if (email != null) {
            parameters.addParameterShell(SUBSCRIBE_NOTIFICATION_CHANNEL_EMAIL, email);
        }

        return parameters;
    }

    public static String buildSubscribeRequest(ParameterContainer parameters) throws OXFException {

        SubscribeDocument subscribeDoc = SubscribeDocument.Factory.newInstance();

        Subscribe subscribe = subscribeDoc.addNewSubscribe();

        // SETTING MANDATORY

        // service
        subscribe.setService((String) parameters.getParameterShellWithServiceSidedName(SUBSCRIBE_SERVICE_PARAM).getSpecifiedValue());

        // version
        subscribe.setVersion((String) parameters.getParameterShellWithServiceSidedName(SUBSCRIBE_VERSION_PARAM).getSpecifiedValue());

        try {

            // build subscriptionrequest for an eventfilter

            // create new EventFilter
            EventFilter eventFilter = subscribe.addNewEventFilter();

            // add the sensor id if defined
            try {
                String sensorID = (String) parameters.getParameterShellWithServiceSidedName(SUBSCRIBE_SENSOR_ID_PARAM).getSpecifiedValue();
                eventFilter.setSensorID(sensorID);
            }
            catch (Exception e) {
                throw new OXFException("sensor id not defined");
            }

            // Filterdefinitions

            String[] filterDefinitions = null;
            ParameterShell filterDefinitionPS = parameters.getParameterShellWithServiceSidedName(SUBSCRIBE_FILTER_DEFINITION);
            if (filterDefinitionPS.hasMultipleSpecifiedValues()) {
                filterDefinitions = (String[]) filterDefinitionPS.getSpecifiedValueArray();
            }
            else if (filterDefinitionPS.hasSingleSpecifiedValue()) {
                filterDefinitions = new String[] {(String) filterDefinitionPS.getSpecifiedValue()};
            }
            // Filtercritera

            Integer[] filterCriteria = null;
            ParameterShell filterCriteriaPS = parameters.getParameterShellWithServiceSidedName(SUBSCRIBE_FILTER_CRITERIA);
            if (filterCriteriaPS.hasMultipleSpecifiedValues()) {
                filterCriteria = (Integer[]) filterCriteriaPS.getSpecifiedValueArray();
            }
            else if (filterCriteriaPS.hasSingleSpecifiedValue()) {
                filterCriteria = new Integer[] {(Integer) filterCriteriaPS.getSpecifiedValue()};
            }
            // FiltercriteraValue

            String[] filterCriteriaValue = null;
            ParameterShell filterCriteriaValuePS = parameters.getParameterShellWithServiceSidedName(SUBSCRIBE_CRITERIA_VALUE);
            if (filterCriteriaValuePS != null) {
                if (filterCriteriaValuePS.hasMultipleSpecifiedValues()) {
                    filterCriteriaValue = (String[]) filterCriteriaValuePS.getSpecifiedValueArray();
                }
                else if (filterCriteriaValuePS.hasSingleSpecifiedValue()) {
                    filterCriteriaValue = new String[] {(String) filterCriteriaValuePS.getSpecifiedValue()};
                }
            }
            // FiltercriteraValueUpper

            String[] filterCriteriaValueUpper = null;
            ParameterShell filterCriteriaValueUpperPS = parameters.getParameterShellWithServiceSidedName(SUBSCRIBE_CRITERIA_UPPER_BOUNDARY);
            if (filterCriteriaValueUpperPS != null) {
                if (filterCriteriaValueUpperPS.hasMultipleSpecifiedValues()) {
                    filterCriteriaValueUpper = (String[]) filterCriteriaValueUpperPS.getSpecifiedValueArray();
                }
                else if (filterCriteriaValueUpperPS.hasSingleSpecifiedValue()) {
                    filterCriteriaValueUpper = new String[] {(String) filterCriteriaValueUpperPS.getSpecifiedValue()};
                }
            }
            // FiltercriteraValueLower

            String[] filterCriteriaValueLower = null;
            ParameterShell filterCriteriaValueLowerPS = parameters.getParameterShellWithServiceSidedName(SUBSCRIBE_CRITERIA_LOWER_BOUNDARY);
            if (filterCriteriaValueLowerPS != null) {
                if (filterCriteriaValueLowerPS.hasMultipleSpecifiedValues()) {
                    filterCriteriaValueLower = (String[]) filterCriteriaValueLowerPS.getSpecifiedValueArray();
                }
                else if (filterCriteriaValueLowerPS.hasSingleSpecifiedValue()) {
                    filterCriteriaValueLower = new String[] {(String) filterCriteriaValueLowerPS.getSpecifiedValue()};
                }
            }
            //
            if (filterDefinitions != null) {
                ValueFilterList valueFilterList = eventFilter.addNewValueFilterList();

                for (int i = 0; i < filterDefinitions.length; i++) {
                    // A Member for every criteria
                    Member member = valueFilterList.addNewMember();

                    // get filterParam from parametersshell and parse to xmlObject
                    ValueFilter valueFilter = ValueFilter.Factory.newInstance();

                    // get the phenomenen to filter
                    // String phen = (String)
                    // parameters.getParameterShellWithServiceSidedName(SUBSCRIBE_FILTER_DEFINITION).getSpecifiedValue();
                    // valueFilter.setDefinition(phen);
                    valueFilter.setDefinition(filterDefinitions[i]);

                    // get filterCriteria from parametersshell and parse to xmlObject
                    // String criteria = (String)
                    // parameters.getParameterShellWithServiceSidedName(SUBSCRIBE_FILTER_CRITERIA).getSpecifiedValue();
                    if (filterCriteria != null) {
                        if (filterCriteria[i] != null) {
                            int criteria = filterCriteria[i];

                            String value = new String();

                            double val = 0.0;
                            double upper = 0.0;
                            double lower = 0.0;

                            if (filterCriteriaValue != null) {
                                if (filterCriteriaValue[i] != null) {
                                    value = filterCriteriaValue[i];
                                }
                                else if (filterCriteriaValueUpper != null && filterCriteriaValueLower != null) {
                                    upper = new Double(filterCriteriaValueUpper[i]);
                                    lower = new Double(filterCriteriaValueLower[i]);
                                }
                            }
                            else if (filterCriteriaValueUpper != null && filterCriteriaValueLower != null) {
                                upper = new Double(filterCriteriaValueUpper[i]);
                                lower = new Double(filterCriteriaValueLower[i]);
                            }

                            FilterCriteria fc = FilterCriteria.Factory.newInstance();

                            int crit = new Integer(criteria);

                            setFilterCriteria(fc, crit, value, lower, upper);

                            valueFilter.setFilterCriteria(fc);
                        }
                    }
                    member.setValueFilter(valueFilter);
                }
            }
        }
        catch (Exception e) {
            throw new OXFException(e);
        }

        try {
            ResultRecipient resultRecipient = subscribe.addNewResultRecipient();
            NotificationTarget notifyTarget = resultRecipient.addNewNotificationTarget();
            String email = (String) parameters.getParameterShellWithServiceSidedName(SUBSCRIBE_NOTIFICATION_CHANNEL_EMAIL).getSpecifiedValue();

            notifyTarget.addNewNotificationChannel().addEmail(email);

            notifyTarget.setNotificationFormat(net.opengis.wns.x00.NotificationFormatDocument.NotificationFormat.BASIC);

            subscribe.setResultRecipient(resultRecipient);
        }
        catch (Exception e) {

            try {
                ResultRecipient resultRecipient = subscribe.addNewResultRecipient();
                NotificationTarget notifyTarget = resultRecipient.addNewNotificationTarget();
                String sms = (String) parameters.getParameterShellWithCommonName(SUBSCRIBE_NOTIFICATION_CHANNEL_SMS).getSpecifiedValue();

                notifyTarget.addNewNotificationChannel().addSMS(sms);

                notifyTarget.setNotificationFormat(net.opengis.wns.x00.NotificationFormatDocument.NotificationFormat.BASIC);

                subscribe.setResultRecipient(resultRecipient);
            }
            catch (Exception noRecipientDefined) {
                throw new OXFException(noRecipientDefined);
            }
        }

        return formatStringRequest(subscribeDoc);
    }

    public static String buildRenewSubscriptionRequest(ParameterContainer parameters) {

        RenewSubscriptionDocument renewSubDoc = RenewSubscriptionDocument.Factory.newInstance();

        RenewSubscription renewSub = renewSubDoc.addNewRenewSubscription();

        /*
         * set mandatory elements
         */

        // service
        renewSub.setService((String) parameters.getParameterShellWithServiceSidedName("service").getSpecifiedValue());

        // version
        renewSub.setVersion((String) parameters.getParameterShellWithServiceSidedName("version").getSpecifiedValue());

        // SubscriptionID
        renewSub.setSubscriptionID((String) parameters.getParameterShellWithServiceSidedName("SubscriptionID").getSpecifiedValue());

        return formatStringRequest(renewSubDoc);
    }

    public static String buildCancelSubscriptionRequest(ParameterContainer parameters) {

        CancelSubscriptionDocument cancelSubDoc = CancelSubscriptionDocument.Factory.newInstance();

        CancelSubscription cancelSub = cancelSubDoc.addNewCancelSubscription();

        /*
         * set mandatory elements
         */

        // service
        cancelSub.setService((String) parameters.getParameterShellWithServiceSidedName("service").getSpecifiedValue());

        // version
        cancelSub.setVersion((String) parameters.getParameterShellWithServiceSidedName("version").getSpecifiedValue());

        // SubscriptionID
        cancelSub.setSubscriptionID((String) parameters.getParameterShellWithServiceSidedName("SubscriptionID").getSpecifiedValue());

        return formatStringRequest(cancelSubDoc);
    }

    /**
     * can be used as a convenience method to build up a ParameterContainer for the Advertise method.
     * 
     * @throws OXFException
     */
    public static ParameterContainer buildAdvertiseParamCon(String service,
                                                            String version,
                                                            String components_name,
                                                            String phenomenonDefinition,
                                                            String uomCode,
                                                            String sensorDescHref) throws OXFException {
        ParameterContainer parameters = new ParameterContainer();

        parameters.addParameterShell(ADVERTISE_SERVICE_PARAM, service);
        parameters.addParameterShell(ADVERTISE_VERSION_PARAM, version);

        parameters.addParameterShell(ADVERTISE_COMPONENTS_NAME, components_name);
        parameters.addParameterShell(ADVERTISE_PHENOMENON_DEFINITION, phenomenonDefinition);
        parameters.addParameterShell(ADVERTISE_UOM_CODE, uomCode);
        parameters.addParameterShell(ADVERTISE_SENSOR_HREF, sensorDescHref);

        return parameters;
    }

    /**
     * builds up the POST request for the Advertise method.
     */
    public static String buildAdvertiseRequest(ParameterContainer parameters) {

        AdvertiseDocument xb_advertiseDoc = AdvertiseDocument.Factory.newInstance();

        Advertise xb_advertise = xb_advertiseDoc.addNewAdvertise();

        // service
        xb_advertise.setService((String) parameters.getParameterShellWithServiceSidedName(ADVERTISE_SERVICE_PARAM).getSpecifiedValue());

        // version
        xb_advertise.setVersion((String) parameters.getParameterShellWithServiceSidedName(ADVERTISE_VERSION_PARAM).getSpecifiedValue());

        //
        // add messageStructure element:
        //
        MessageStructure xb_messageStructure = xb_advertise.addNewMessageStructure();

        net.opengis.swe.x10.DataBlockDefinitionType xb_dataBlock = xb_messageStructure.addNewDataBlockDefinition();

        DataComponentPropertyType xb_comp = xb_dataBlock.addNewComponents();
        xb_comp.setName((String) parameters.getParameterShellWithServiceSidedName(ADVERTISE_COMPONENTS_NAME).getSpecifiedValue());

        // <swe:Quantity definition="urn:x-ogc:def:phenomenon:OGC:Temperature">
        // <swe:uom code="Cel"/>
        // </swe:Quantity>
        Quantity xb_quantity = xb_comp.addNewQuantity();
        xb_quantity.setDefinition((String) parameters.getParameterShellWithServiceSidedName(ADVERTISE_PHENOMENON_DEFINITION).getSpecifiedValue());

        UomPropertyType xb_uom = xb_quantity.addNewUom();
        xb_uom.setCode((String) parameters.getParameterShellWithServiceSidedName(ADVERTISE_UOM_CODE).getSpecifiedValue());

        // <swe:TextBlock decimalSeparator="." blockSeparator="|" tokenSeparator=" "/>
        BlockEncodingPropertyType xb_BlockEnc = xb_dataBlock.addNewEncoding();

        TextBlock xb_textBlock = TextBlock.Factory.newInstance();
        xb_textBlock.setBlockSeparator("|");
        xb_textBlock.setDecimalSeparator(".");
        xb_textBlock.setTokenSeparator(" ");

        xb_BlockEnc.setTextBlock(xb_textBlock);

        //
        // add sensorDescription element:
        //
        SensorDescription xb_sensorDesc = xb_advertise.addNewSensorDescription();

        xb_sensorDesc.setHref((String) parameters.getParameterShellWithServiceSidedName(ADVERTISE_SENSOR_HREF).getSpecifiedValue());

        //
        // TODO: optional elements have to be added:
        //
        // xb_advertise.addNewReportingFrequency();
        //        
        // xb_advertise.addNewLocation();
        //        
        // xb_advertise.addNewDesiredPublicationExpiration();
        //        
        // xb_advertise.setXMPPCredentials("...");

        return formatStringRequest(xb_advertiseDoc);
    }

    public static String buildRenewAdvertisementRequest(ParameterContainer parameters) {

        // TODO has to be implemented

        return null;
    }

    public static String buildCancelAdvertisementRequest(ParameterContainer parameters) {
        throw new UnsupportedOperationException("This optional operation is not yet supported.");
    }

    public static String buildDescribeSensor(ParameterContainer parameters) {
        throw new UnsupportedOperationException("This optional operation is not yet supported.");
    }

    /**
     * <?xml version="1.0" encoding="UTF-8"?> <Alert xmlns="http://www.opengis.net/sas/0.0"
     * xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"> <SensorID>urn:x-ogc:object:sensor:IFGI:Temp:1</SensorID>
     * <Timestamp>2007-01-24T14:18:22Z</Timestamp> <AlertData>5.4 90.2 51.9424 7.692</AlertData> </Alert>
     * 
     * @param parameters
     * @return
     */
    public static String buildAlertRequest(ParameterContainer parameters) throws OXFException {

        AlertDocument xb_alertDoc = AlertDocument.Factory.newInstance();

        Alert xb_alert = xb_alertDoc.addNewAlert();

        xb_alert.setSensorID((String) parameters.getParameterShellWithServiceSidedName(ALERT_SENSORID).getSpecifiedValue());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date d;
        try {
            d = sdf.parse((String) parameters.getParameterShellWithServiceSidedName(ALERT_TIMESTAMP).getSpecifiedValue());
            Calendar c = new GregorianCalendar();
            c.setTime(d);
            xb_alert.setTimestamp(c);
        }
        catch (ParseException e) {
            throw new OXFException(e);
        }

        String data = (String) parameters.getParameterShellWithServiceSidedName(ALERT_DATA).getSpecifiedValue();

        XmlObject x = xb_alert.addNewAlertData();
        XmlCursor xmlCursor = x.newCursor();
        xmlCursor.setTextValue(data);

        return formatStringRequest(xb_alertDoc);
    }

    /**
     * help-method that formats a XmlTokenString into a String.
     * 
     * @param unformattedRequestDocument
     * @return
     */
    private static String formatStringRequest(XmlTokenSource unformattedRequestDocument) {
        XmlOptions xmlOpts = new XmlOptions();

        xmlOpts.setSavePrettyPrint();
        xmlOpts.setSavePrettyPrintIndent(5);
        xmlOpts.setCharacterEncoding("UTF-8");

        String s = new String(unformattedRequestDocument.xmlText(xmlOpts));

        // this character replacement must be carried out for a nice format of
        // the XML-String
        s = s.replace("\n     ", "");

        return s;
    }

    /**
     * Switches the operator for FilterCriteria in the request and sets the value
     * 
     * @param {FilterCriteria}
     *        fc
     * @param {int}
     *        crit
     * @param {double}
     *        val
     * @param {double}
     *        upper
     * @param {double}
     *        lower
     */
    private static void setFilterCriteria(FilterCriteria fc, int crit, String value, double upper, double lower) {
        switch (crit) {
        case Criteria.IS_BETWEEN:
            IsBetween ib = fc.addNewIsBetween();
            ib.setLowerBoundary(lower);
            ib.setUpperBoundary(upper);
            break;
        case Criteria.IS_EQUAL:
            fc.setIsEqual((Object) value);
            break;
        case Criteria.IS_GREATER_THEN:
            fc.setIsGreaterThan(new Double(value));
            break;
        case Criteria.IS_GREATER_THEN_OR_EQUAL:
            fc.setIsGreaterThanOrEqualTo(new Double(value));
            break;
        case Criteria.IS_LESS_THEN:
            fc.setIsLessThan(new Double(value));
            break;
        case Criteria.IS_LESS_THEN_OR_EQUAL:
            fc.setIsLessThanOrEqualTo(new Double(value));
            break;
        case Criteria.IS_NOT_EQUAL:
            fc.setIsNotEqualTo((Object) value);
            break;
        default:
            break;
        }
    }
}