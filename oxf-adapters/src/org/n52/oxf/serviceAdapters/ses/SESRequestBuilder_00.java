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
package org.n52.oxf.serviceAdapters.ses;

import java.util.UUID;

import javax.xml.namespace.QName;

import org.apache.xmlbeans.XmlCursor;
import org.n52.oxf.OXFException;
import org.n52.oxf.serviceAdapters.ParameterContainer;
import org.n52.oxf.ui.swing.ses.SesGUI;
import org.w3.x2003.x05.soapEnvelope.Body;
import org.w3.x2003.x05.soapEnvelope.Envelope;
import org.w3.x2003.x05.soapEnvelope.EnvelopeDocument;
import org.w3.x2003.x05.soapEnvelope.Header;
import org.n52.oxf.ui.swing.ses.*;

/**
 * contains attributes and methods to encode SESOperationRequests as String in xml-format
 * 
 * @author <a href="mailto:artur.osmanov@uni-muenster.de">Artur Osmanov</a>
 * @author <a href="mailto:ehjuerrens@uni-muenster.de">Eike Hinderk J&uuml;rrens</a>
 *
 */
public class SESRequestBuilder_00 implements ISESRequestBuilder{

	//private final String ns_addressing = "http://58.192.82.159:8083";
	private final String ns_addressing = "http://www.w3.org/2005/08/addressing";
    private final String ns_ses = "http://www.opengis.net/ses/0.0";
    private final String ns_wsNotification = "http://docs.oasis-open.org/wsn/b-2";
    private final String ns_wsBrokeredNotification = "http://docs.oasis-open.org/wsn/br-2";
    private final String ns_resourceId = "http://ws.apache.org/muse/addressing";
    
    private final String ns_wfs= "http://www.opengis.net/wfs";//添加的
    private final String ns_fes = "http://www.opengis.net/fes/2.0";//添加的
    private final String ns_gml="http://www.opengis.net/gml/3.2" ; 
    private final String ns_ows= "http://www.opengis.net/ows";
    private final String ns_xmlinstance= "http://www.w3.org/2001/XMLSchema-instance" ;
    private final String ns_filterAll= "http://www.opengis.net/fes/2.0 http://schemas.opengis.net/filter/2.0/filterAll.xsd";
    private final String ns_swe="http://www.opengis.net/swe/1.0.1";
    
    private final String ns_eml="http://www.opengis.net/eml/0.0.1"; //添加的
    
    //private SesGUI view;//添加的
    String selectedfilterLevel;//添加的
    
    
    
    @Override
    public String buildGetCapabilitiesRequest(ParameterContainer parameter) throws OXFException{
        String request = "";

        EnvelopeDocument envDoc = EnvelopeDocument.Factory.newInstance();
        Envelope env = envDoc.addNewEnvelope();
        Header header = env.addNewHeader();
        Body body = env.addNewBody();
        String sesURL = (String)parameter.getParameterShellWithCommonName(ISESRequestBuilder.GET_CAPABILITIES_SES_URL).getSpecifiedValue();
        XmlCursor cur = null;

        SESUtils.addNamespacesToEnvelope_000(env);

        cur = header.newCursor();

        cur.toFirstContentToken();
        cur.insertElementWithText(new QName(ns_addressing,"To","wsa"),sesURL);
        cur.insertElementWithText(new QName(ns_addressing,"Action","wsa"), "http://www.opengis.net/ses/GetCapabilitiesRequest");
        cur.insertElementWithText(new QName(ns_addressing,"MessageID","wsa"),UUID.randomUUID().toString());
        cur.beginElement(new QName(ns_addressing,"From","wsa"));
        cur.insertElementWithText(new QName(ns_addressing,"Address","wsa"),"http://www.w3.org/2005/08/addressing/role/anonymous");
        cur.dispose();
        
        cur = body.newCursor();

        cur.toFirstContentToken();
        cur.beginElement(new QName(ns_ses,"GetCapabilities","ses"));
        cur.insertAttributeWithValue("Service", "SES");
        cur.dispose();

        request = envDoc.xmlText();

        return request;
    }

    @Override
    public String buildNotifyRequest(ParameterContainer parameter) {
        String request = "";

        EnvelopeDocument envDoc = EnvelopeDocument.Factory.newInstance();
        Envelope env = envDoc.addNewEnvelope();
        Header header = env.addNewHeader();
        Body body = env.addNewBody();
        String sesURL = (String)parameter.getParameterShellWithCommonName(ISESRequestBuilder.NOTIFY_SES_URL).getSpecifiedValue();
        XmlCursor cur = null;

        SESUtils.addNamespacesToEnvelope_000(env);

        cur = header.newCursor();

        cur.toFirstContentToken();
        cur.insertElementWithText(new QName(ns_addressing,"To","wsa"),sesURL);
        cur.insertElementWithText(new QName(ns_addressing,"Action","wsa"),
        "http://docs.oasis-open.org/wsn/bw-2/NotificationConsumer/NotifyRequest");
        cur.insertElementWithText(new QName(ns_addressing,"MessageID","wsa"),
                UUID.randomUUID().toString());
        cur.beginElement(new QName(ns_addressing,"From","wsa"));
        cur.insertElementWithText(new QName(ns_addressing,"Address","wsa"),
        "http://www.w3.org/2005/08/addressing/role/anonymous");
        cur.dispose();
        
        
        cur = body.newCursor();
        cur.toFirstContentToken();
        cur.beginElement(new QName(ns_wsNotification,"Notify","wsnt"));

        cur.beginElement(new QName(ns_wsNotification,"NotificationMessage","wsnt"));

        cur.beginElement(new QName(ns_wsNotification,"Topic","wsnt"));
        cur.insertAttributeWithValue(/*new QName(ns_wsNotification,"Dialect","wsnt")*/"Dialect", 
                (String)parameter.getParameterShellWithCommonName(ISESRequestBuilder.NOTIFY_TOPIC_DIALECT).getSpecifiedValue());
        cur.insertChars((String)parameter.getParameterShellWithCommonName(ISESRequestBuilder.NOTIFY_TOPIC).getSpecifiedValue());
        cur.toNextToken();

        cur.beginElement(new QName(ns_wsNotification,"Message","wsnt"));
        cur.insertChars("@MSG_REPLACER@");
        cur.dispose();

        request = envDoc.xmlText();
        request = request.replaceAll("@MSG_REPLACER@",(String)parameter.getParameterShellWithCommonName(ISESRequestBuilder.NOTIFY_XML_MESSAGE).getSpecifiedValue());
        System.out.println("Notify请求消息是--------------");
        System.out.println(request);
        return request;
    }

    @Override
    public String buildRegisterPublisherRequest(ParameterContainer parameter) {
        String request = "";

        EnvelopeDocument envDoc = EnvelopeDocument.Factory.newInstance();
        Envelope env = envDoc.addNewEnvelope();
        Header header = env.addNewHeader();
        Body body = env.addNewBody();
        String sesURL = (String)parameter.getParameterShellWithCommonName(ISESRequestBuilder.REGISTER_PUBLISHER_SES_URL).getSpecifiedValue();
        XmlCursor cur = null;
        String from = "http://www.w3.org/2005/08/addressing/role/anonymous";
        if(parameter.getParameterShellWithCommonName(ISESRequestBuilder.REGISTER_PUBLISHER_FROM) != null){
            from = (String)parameter.getParameterShellWithCommonName(ISESRequestBuilder.REGISTER_PUBLISHER_FROM).getSpecifiedValue();
        }
        SESUtils.addNamespacesToEnvelope_000(env);

        cur = header.newCursor();

        cur.toFirstContentToken();
        cur.insertElementWithText(new QName(ns_addressing,"To","wsa"),sesURL);
        cur.insertElementWithText(new QName(ns_addressing,"Action","wsa"),
        "http://docs.oasis-open.org/wsn/brw-2/RegisterPublisher/RegisterPublisherRequest");
        cur.insertElementWithText(new QName(ns_addressing,"MessageID","wsa"),
                UUID.randomUUID().toString());
        cur.beginElement(new QName(ns_addressing,"From","wsa"));
        cur.insertElementWithText(new QName(ns_addressing,"Address","wsa"),from);
        cur.dispose();

        cur = body.newCursor();

        cur.toFirstContentToken();
        cur.beginElement(new QName(ns_wsBrokeredNotification,"RegisterPublisher","wsbn"));
        cur.insertElementWithText(new QName("http://docs.oasis-open.org/wsrf/rl-2","RequestedLifetimeDuration","wsrf"),
                (String)parameter.getParameterShellWithCommonName(ISESRequestBuilder.REGISTER_PUBLISHER_LIFETIME_DURATION).getSpecifiedValue());

        cur.beginElement(new QName(ns_wsBrokeredNotification,"Topic","wsbn"));
        cur.insertAttributeWithValue(/*new QName(ns_wsBrokeredNotification,"Dialect","wsrf")*/"dialect", 
                (String)parameter.getParameterShellWithCommonName(ISESRequestBuilder.REGISTER_PUBLISHER_TOPIC_DIALECT).getSpecifiedValue());
        cur.insertChars((String)parameter.getParameterShellWithCommonName(ISESRequestBuilder.REGISTER_PUBLISHER_TOPIC).getSpecifiedValue());
        cur.toNextToken();

        cur.beginElement(new QName("http://www.opengis.net/sensorML/1.0.1","SensorML","sml"));
        cur.insertAttributeWithValue(new QName("http://www.opengis.net/sensorML/1.0.1","version","sml"), "1.0.1");
        cur.insertChars("@SML_REPLACER@");
        cur.toNextToken();

        cur.insertElementWithText(new QName(ns_wsBrokeredNotification,"Demand","wsbn"),"no"); // FIXME is this a default value?
        cur.insertElementWithText(new QName(ns_addressing,"EndpointReferenceType","wsa"),"ignore"); // FIXME is this a default value?
        cur.dispose();

        request = envDoc.xmlText();
        request = request.replaceAll("@SML_REPLACER@", (String)parameter.getParameterShellWithCommonName(ISESRequestBuilder.REGISTER_PUBLISHER_SENSORML).getSpecifiedValue());

        return request;
    }

    @Override
    public String buildSubscribeRequest(ParameterContainer parameter) {   	
    	
    	selectedfilterLevel= SesGUIController.a;
    			//getJComboBoxFilterType().getSelectedItem().toString(); //添加的
    	System.out.println("==================filterlevel是==================================");
    	System.out.println(selectedfilterLevel);//添加的
    	
    	String request = "";
    	if (selectedfilterLevel.equals("Filter Level 1")||selectedfilterLevel.equals("Filter Level 2")){
    		

            EnvelopeDocument envDoc = EnvelopeDocument.Factory.newInstance();
            Envelope env = envDoc.addNewEnvelope();
            Header header = env.addNewHeader();
            Body body = env.addNewBody();
            String sesURL = (String)parameter.getParameterShellWithCommonName(ISESRequestBuilder.SUBSCRIBE_SES_URL).getSpecifiedValue();
            String from = (parameter.getParameterShellWithCommonName(ISESRequestBuilder.SUBSCRIBE_FROM)!=null?(String)(parameter.getParameterShellWithCommonName(ISESRequestBuilder.SUBSCRIBE_FROM).getSpecifiedValue()):"http://www.w3.org/2005/08/addressing/role/anonymous");
            XmlCursor cur = null;
            String messageContentFilter = null;

            SESUtils.addNamespacesToEnvelope_000(env);

            cur = header.newCursor();

            cur.toFirstContentToken();
            cur.insertElementWithText(new QName(ns_addressing,"To","wsa"),sesURL);
            cur.insertElementWithText(new QName(ns_addressing,"Action","wsa"),
            "http://docs.oasis-open.org/wsn/bw-2/NotificationProducer/SubscribeRequest");
            cur.insertElementWithText(new QName(ns_addressing,"MessageID","wsa"),
                    UUID.randomUUID().toString());
            cur.beginElement(new QName(ns_addressing,"From","wsa"));
            cur.insertElementWithText(new QName(ns_addressing,"Address","wsa"),from);
            cur.dispose();

            cur = body.newCursor();

            cur.toFirstContentToken();
            cur.beginElement(new QName(ns_wsNotification,"Subscribe","wsnt"));
            cur.beginElement(new QName(ns_wsNotification,"ConsumerReference","wsnt"));
            cur.insertElementWithText(new QName(ns_addressing,"Address","wsa"),
                    (String)parameter.getParameterShellWithCommonName(ISESRequestBuilder.SUBSCRIBE_CONSUMER_REFERENCE_ADDRESS).getSpecifiedValue());
            cur.toNextToken();

            cur.beginElement(new QName(ns_wsNotification,"Filter","wsnt"));

            // we need to check which filters are defined before building the request
            //
            // TOPIC FILTERING
            //
            if(parameter.getParameterShellWithCommonName(ISESRequestBuilder.SUBSCRIBE_FILTER_TOPIC_DIALECT) != null && 
                    parameter.getParameterShellWithCommonName(ISESRequestBuilder.SUBSCRIBE_FILTER_TOPIC) != null){

                cur.beginElement(new QName(ns_wsNotification,"TopicExpression","wsnt"));
                cur.insertAttributeWithValue("Dialect",
                        (String)parameter.getParameterShellWithCommonName(ISESRequestBuilder.SUBSCRIBE_FILTER_TOPIC_DIALECT).getSpecifiedValue());
                cur.insertChars((String)parameter.getParameterShellWithCommonName(ISESRequestBuilder.SUBSCRIBE_FILTER_TOPIC).getSpecifiedValue());
                cur.toNextToken();

            }
            //
            // 消息内容用滤波
            //
            // TODO use level identifier 1,2,3 instead of dialect definition!?
            if(parameter.getParameterShellWithCommonName(ISESRequestBuilder.SUBSCRIBE_FILTER_MESSAGE_CONTENT_DIALECT) != null && 
                    parameter.getParameterShellWithCommonName(ISESRequestBuilder.SUBSCRIBE_FILTER_MESSAGE_CONTENT) != null){

                //messageContentFilter = (String)parameter.getParameterShellWithCommonName(ISESRequestBuilder.SUBSCRIBE_FILTER_MESSAGE_CONTENT).getSpecifiedValue();
               
                cur.beginElement(new QName(ns_wsNotification,"MessageContent","wsnt"));
                cur.insertAttributeWithValue("Dialect",
                        (String)parameter.getParameterShellWithCommonName(ISESRequestBuilder.SUBSCRIBE_FILTER_MESSAGE_CONTENT_DIALECT).getSpecifiedValue());
                //cur.insertChars("@MSG_CONT_FILTER@");
                cur.beginElement(new QName(ns_fes,"Filter","fes"));
                cur.beginElement(new QName(ns_fes,"And","fes"));
                cur.insertElement(new QName(ns_fes,"PropertyIsEqualTo","fes"));
                cur.toPrevToken();
               
                cur.insertElementWithText(new QName(ns_fes,"ValueReference","fes"),"observedProperty");
                cur.insertElementWithText(new QName(ns_fes,"Literal","fes"),(String)parameter.getParameterShellWithCommonName(ISESRequestBuilder.SUBSCRIBE_FILTER_MESSAGE_CONTENT).getSpecifiedValue());
                String s1="值大于";
                if(
                		((String)(parameter.getParameterShellWithCommonName(ISESRequestBuilder.SUBSCRIBE_FILTER_MESSAGE_CONTENT_THRESHOLD).getSpecifiedValue())).equals(s1))
                {
                	 
                cur.toNextToken();
                cur.beginElement(new QName(ns_fes,"PropertyIsGreaterThan","fes"));
                cur.insertElementWithText(new QName(ns_fes,"ValueReference","fes"),"doubleValue");
                cur.insertElement(new QName(ns_fes,"Literal","fes"));
                cur.toPrevToken();
                cur.beginElement(new QName(ns_swe,"Quantity","swe"));
                cur.insertElementWithText(new QName(ns_swe,"value","swe"),
                		parameter.getParameterShellWithCommonName(ISESRequestBuilder.SUBSCRIBE_FILTER_MESSAGE_CONTENT_THRESHOLD_VALUE).getSpecifiedValue().toString());
                }
                else if(((String)(parameter.getParameterShellWithCommonName(ISESRequestBuilder.SUBSCRIBE_FILTER_MESSAGE_CONTENT_THRESHOLD).getSpecifiedValue())).equals("值小于"))
                { cur.toNextToken();
                cur.beginElement(new QName(ns_fes,"PropertyIsLessThan","fes"));
                cur.insertElementWithText(new QName(ns_fes,"ValueReference","fes"),"doubleValue");
                cur.insertElement(new QName(ns_fes,"Literal","fes"));
                cur.toPrevToken();
                cur.beginElement(new QName(ns_swe,"Quantity","swe"));
                cur.insertElementWithText(new QName(ns_swe,"value","swe"),
                		parameter.getParameterShellWithCommonName(ISESRequestBuilder.SUBSCRIBE_FILTER_MESSAGE_CONTENT_THRESHOLD_VALUE).getSpecifiedValue().toString());
                }
                else if(((String)(parameter.getParameterShellWithCommonName(ISESRequestBuilder.SUBSCRIBE_FILTER_MESSAGE_CONTENT_THRESHOLD).getSpecifiedValue())).equals("值等于"))
                { cur.toNextToken();
                cur.beginElement(new QName(ns_fes,"PropertyIsEqualTo","fes"));
                cur.insertElementWithText(new QName(ns_fes,"ValueReference","fes"),"doubleValue");
                cur.insertElement(new QName(ns_fes,"Literal","fes"));
                cur.toPrevToken();
                cur.beginElement(new QName(ns_swe,"Quantity","swe"));
                cur.insertElementWithText(new QName(ns_swe,"value","swe"),
                		parameter.getParameterShellWithCommonName(ISESRequestBuilder.SUBSCRIBE_FILTER_MESSAGE_CONTENT_THRESHOLD_VALUE).getSpecifiedValue().toString());
                }

            }
            
            //
            // if a initial termination time is not wanted add attribute xsi:nil="true"
            //
           /*cur.toNextToken();
            cur.toNextToken();
            cur.toNextToken();
            if(parameter.getParameterShellWithCommonName(ISESRequestBuilder.SUBSCRIBE_INITIAL_TERMINATION_TIME) == null){

                cur.beginElement(new QName(ns_wsNotification,"InitialTerminationTime","wsnt"));
                cur.insertAttributeWithValue(new QName("http://www.w3.org/2001/XMLSchema-instance","nil","xsi"), "true");
                cur.toNextToken();
              
            } else{

                cur.insertElementWithText(new QName(ns_wsNotification,"InitialTerminationTime","wsnt"), 
                        (String)parameter.getParameterShellWithCommonName(ISESRequestBuilder.SUBSCRIBE_INITIAL_TERMINATION_TIME).getSpecifiedValue());

            }
            */
            
            cur.dispose();
            
            request = envDoc.xmlText();

            if(messageContentFilter != null){
                request = request.replaceAll("@MSG_CONT_FILTER@", messageContentFilter);
            }
            
            System.out.println("订阅请求是------------------------------");

            System.out.println(request);
             
            
            
    	}
    	else if(selectedfilterLevel.equals("Filter Level 3")){

    		
            EnvelopeDocument envDoc = EnvelopeDocument.Factory.newInstance();
            Envelope env = envDoc.addNewEnvelope();
            Header header = env.addNewHeader();
            Body body = env.addNewBody();
            String sesURL = (String)parameter.getParameterShellWithCommonName(ISESRequestBuilder.SUBSCRIBE_SES_URL).getSpecifiedValue();
            String from = (parameter.getParameterShellWithCommonName(ISESRequestBuilder.SUBSCRIBE_FROM)!=null?(String)(parameter.getParameterShellWithCommonName(ISESRequestBuilder.SUBSCRIBE_FROM).getSpecifiedValue()):"http://www.w3.org/2005/08/addressing/role/anonymous");
            XmlCursor cur = null;
            String messageContentFilter = null;

            SESUtils.addNamespacesToEnvelope_000(env);

            cur = header.newCursor();

            cur.toFirstContentToken();
            cur.insertElementWithText(new QName(ns_addressing,"To","wsa"),sesURL);
            cur.insertElementWithText(new QName(ns_addressing,"Action","wsa"),
            "http://docs.oasis-open.org/wsn/bw-2/NotificationProducer/SubscribeRequest");
            cur.insertElementWithText(new QName(ns_addressing,"MessageID","wsa"),
                    UUID.randomUUID().toString());
            cur.beginElement(new QName(ns_addressing,"From","wsa"));
            cur.insertElementWithText(new QName(ns_addressing,"Address","wsa"),from);
            cur.dispose();

            cur = body.newCursor();

            cur.toFirstContentToken();
            cur.beginElement(new QName(ns_wsNotification,"Subscribe","wsnt"));
            cur.beginElement(new QName(ns_wsNotification,"ConsumerReference","wsnt"));
            cur.insertElementWithText(new QName(ns_addressing,"Address","wsa"),
                    (String)parameter.getParameterShellWithCommonName(ISESRequestBuilder.SUBSCRIBE_CONSUMER_REFERENCE_ADDRESS).getSpecifiedValue());
            cur.toNextToken();

            cur.beginElement(new QName(ns_wsNotification,"Filter","wsnt"));

            // we need to check which filters are defined before building the request
            //
            // TOPIC FILTERING
            //
            if(parameter.getParameterShellWithCommonName(ISESRequestBuilder.SUBSCRIBE_FILTER_TOPIC_DIALECT) != null && 
                    parameter.getParameterShellWithCommonName(ISESRequestBuilder.SUBSCRIBE_FILTER_TOPIC) != null){

                cur.beginElement(new QName(ns_wsNotification,"TopicExpression","wsnt"));
                cur.insertAttributeWithValue("Dialect",
                        (String)parameter.getParameterShellWithCommonName(ISESRequestBuilder.SUBSCRIBE_FILTER_TOPIC_DIALECT).getSpecifiedValue());
                cur.insertChars((String)parameter.getParameterShellWithCommonName(ISESRequestBuilder.SUBSCRIBE_FILTER_TOPIC).getSpecifiedValue());
                cur.toNextToken();

            }
            //
            // 消息内容用滤波
            //
            // TODO use level identifier 1,2,3 instead of dialect definition!?
            if(parameter.getParameterShellWithCommonName(ISESRequestBuilder.SUBSCRIBE_FILTER_MESSAGE_CONTENT_DIALECT) != null && 
                    parameter.getParameterShellWithCommonName(ISESRequestBuilder.SUBSCRIBE_FILTER_MESSAGE_CONTENT) != null){

                //messageContentFilter = (String)parameter.getParameterShellWithCommonName(ISESRequestBuilder.SUBSCRIBE_FILTER_MESSAGE_CONTENT).getSpecifiedValue();
               
                cur.beginElement(new QName(ns_wsNotification,"MessageContent","wsnt"));
                cur.insertAttributeWithValue("Dialect",
                        (String)parameter.getParameterShellWithCommonName(ISESRequestBuilder.SUBSCRIBE_FILTER_MESSAGE_CONTENT_DIALECT).getSpecifiedValue());
                //cur.insertChars("@MSG_CONT_FILTER@");
                //======================开始修改============================================================================================
                //下面是自己添加的
                cur.beginElement(new QName(ns_eml,"EML","eml"));    
                cur.beginElement(new QName(ns_eml,"SimplePatterns","eml")); 
                cur.toFirstChild();
                
                
                cur.beginElement(new QName(ns_eml,"SimplePattern","eml"));
                cur.insertAttributeWithValue("inputName", "Input");
                cur.insertAttributeWithValue("patternID", "In1");   
                cur.beginElement(new QName(ns_eml,"SelectFunctions","eml"));  
                cur.beginElement(new QName(ns_eml,"SelectFunction","eml"));  
                cur.insertAttributeWithValue("createCausality", "false");
                cur.insertAttributeWithValue("newEventName", "lastIn1");  
                cur.beginElement(new QName(ns_eml,"SelectEvent","eml"));   
                cur.insertAttributeWithValue("eventName", "Input");
                cur.toNextToken();
                cur.toNextToken();
                cur.toNextToken();
                cur.beginElement(new QName(ns_eml,"View","eml"));    
                cur.beginElement(new QName(ns_eml,"LengthView","eml"));   
                cur.insertElementWithText(new QName(ns_eml,"EventCount","eml"),"1");
                cur.toNextToken();
                cur.toNextToken();
                cur.beginElement(new QName(ns_eml,"Guard","eml"));  
                cur.beginElement(new QName(ns_fes,"Filter","fes")); 
                cur.beginElement(new QName(ns_fes,"PropertyIsGreaterThan","fes")); 
                cur.insertElementWithText(new QName(ns_fes,"ValueReference","fes"),"doubleValue");
                cur.beginElement(new QName(ns_fes,"Literal","fes"));
                cur.toFirstChild();
                cur.beginElement(new QName(ns_swe,"Quantity","swe"));   
                cur.insertAttributeWithValue("definition", "temperature");
                cur.insertElementWithText(new QName(ns_swe,"value","swe"),"15");
                
                cur.toNextToken();
                cur.toNextToken();
                cur.toNextToken();
                cur.toNextToken();
                cur.toNextToken();
                
                cur.beginElement(new QName(ns_eml,"PropertyRestrictions","eml")); 
                cur.beginElement(new QName(ns_eml,"PropertyRestriction","eml")); 
                cur.insertElementWithText(new QName(ns_eml,"name","eml"),"observedProperty");
                cur.insertElementWithText(new QName(ns_eml,"value","eml"),"temperature");
                
                cur.toNextToken();
                cur.toNextToken();
                cur.toNextToken();
                 
                
                cur.beginElement(new QName(ns_eml,"SimplePattern","eml"));  //第二个简单模式
                cur.insertAttributeWithValue("inputName", "Input");
                cur.insertAttributeWithValue("patternID", "In2");   
                cur.beginElement(new QName(ns_eml,"SelectFunctions","eml"));  
                cur.beginElement(new QName(ns_eml,"SelectFunction","eml"));  
                cur.insertAttributeWithValue("createCausality", "false");
                cur.insertAttributeWithValue("newEventName", "lastIn2");  
                cur.beginElement(new QName(ns_eml,"SelectEvent","eml"));   
                cur.insertAttributeWithValue("eventName", "Input");
                cur.toNextToken();
                cur.toNextToken();
                cur.toNextToken();
                cur.beginElement(new QName(ns_eml,"View","eml"));    
                cur.beginElement(new QName(ns_eml,"LengthView","eml"));   
                cur.insertElementWithText(new QName(ns_eml,"EventCount","eml"),"1");
                cur.toNextToken();
                cur.toNextToken();
                cur.beginElement(new QName(ns_eml,"Guard","eml"));  
                cur.beginElement(new QName(ns_fes,"Filter","fes")); 
                cur.beginElement(new QName(ns_fes,"PropertyIsGreaterThan","fes")); 
                cur.insertElementWithText(new QName(ns_fes,"ValueReference","fes"),"doubleValue");
                cur.beginElement(new QName(ns_fes,"Literal","fes"));
                cur.toFirstChild();
                cur.beginElement(new QName(ns_swe,"Quantity","swe"));   
                cur.insertAttributeWithValue("definition", "fume");
                cur.insertElementWithText(new QName(ns_swe,"value","swe"),"20");
                
                cur.toNextToken();
                cur.toNextToken();
                cur.toNextToken();
                cur.toNextToken();
                cur.toNextToken();
                
                cur.beginElement(new QName(ns_eml,"PropertyRestrictions","eml")); 
                cur.beginElement(new QName(ns_eml,"PropertyRestriction","eml")); 
                cur.insertElementWithText(new QName(ns_eml,"name","eml"),"observedProperty");
                cur.insertElementWithText(new QName(ns_eml,"value","eml"),"fume");
                
                cur.toNextToken();
                cur.toNextToken();
                cur.toNextToken();
                cur.toNextToken();
                 
                cur.beginElement(new QName(ns_eml,"ComplexPatterns","eml")); 
                cur.beginElement(new QName(ns_eml,"ComplexPattern","eml")); 
                cur.insertAttributeWithValue("patternID", "In3");
                cur.beginElement(new QName(ns_eml,"SelectFunctions","eml"));  
                cur.toFirstChild();
                cur.beginElement(new QName(ns_eml,"SelectFunction","eml"));  
                cur.insertAttributeWithValue("newEventName", "firealarm");
                cur.insertAttributeWithValue("outputName", "firealarm");  
                cur.insertAttributeWithValue("createCausality", "true");  
                
                cur.beginElement(new QName(ns_eml,"NotifyOnSelect","eml")); 
                cur.insertElementWithText(new QName(ns_eml,"Message","eml"), "FireAlarm");
                
                cur.toNextToken();
                cur.toNextToken();
                cur.toNextToken();
                
                cur.beginElement(new QName(ns_eml,"Logicaloperator","eml")); 
                cur.beginElement(new QName(ns_eml,"AND","eml")); 
                
                cur.toNextToken();
                cur.toNextToken();
                cur.beginElement(new QName(ns_eml,"FirstPattern","eml")); 
                cur.insertElementWithText(new QName(ns_eml,"PatternReference","eml"),"In1");
                cur.insertElementWithText(new QName(ns_eml,"SelectFunctionNumber","eml"),"0");
                
                cur.toNextToken(); 
                cur.beginElement(new QName(ns_eml,"SecondPattern","eml")); 
                cur.insertElementWithText(new QName(ns_eml,"PatternReference","eml"),"In2");
                cur.insertElementWithText(new QName(ns_eml,"SelectFunctionNumber","eml"),"0");
                
                cur.toNextToken(); 
                cur.toNextToken(); 
                cur.toNextToken();
                cur.beginElement(new QName(ns_eml,"TimerPatterns","eml")); 
                cur.toNextToken();
                
                cur.beginElement(new QName(ns_eml,"RepetitivePatterns","eml")); 
                
                /*下面添加的是RepetitivePatterns元素中的内容
                 * cur.beginElement(new QName(ns_eml,"RepetitivePattern","eml"));
                cur.insertAttributeWithValue("patternID", "ComplexCount");
                cur.beginElement(new QName(ns_eml,"SelectFunctions","eml"));  
                cur.beginElement(new QName(ns_eml,"SelectFunction","eml")); 
                cur.insertAttributeWithValue("newEventName", "FireAlarm");
                cur.insertAttributeWithValue("outputName", "OnFire");
                cur.beginElement(new QName(ns_eml,"NotifyOnSelect","eml"));  
                cur.insertElementWithText(new QName(ns_eml,"Message","eml"),"Pattern matched");
                
                cur.toNextToken();
                cur.toNextToken();
                cur.toNextToken();
                
                cur.insertElementWithText(new QName(ns_eml,"EventCount","eml"),"2");
                cur.beginElement(new QName(ns_eml,"PatternToRepeat","eml"));
                cur.insertElementWithText(new QName(ns_eml,"PatternReference","eml"),"newMax");
                cur.insertElementWithText(new QName(ns_eml,"SelectFunctionNumber","eml"),"0");
                */
                //上面是自己添加的
            
             
        cur.dispose();
        
        request = envDoc.xmlText();


        System.out.println("订阅请求是------------------------------");

        System.out.println(request); }}
         
        return request; 
       
        
    	 
    }

    @Override
    public String buildDescribeSensorRequest(ParameterContainer parameter) throws OXFException {
        String request = "";

        EnvelopeDocument envDoc = EnvelopeDocument.Factory.newInstance();
        Envelope env = envDoc.addNewEnvelope();
        Header header = env.addNewHeader();
        Body body = env.addNewBody();
        String sesURL = (String)parameter.getParameterShellWithCommonName(ISESRequestBuilder.DESCRIBE_SENSOR_SES_URL).getSpecifiedValue();
        XmlCursor cur = null;

        SESUtils.addNamespacesToEnvelope_000(env);

        cur = header.newCursor();

        cur.toFirstContentToken();
        cur.insertElementWithText(new QName(ns_addressing,"To","wsa"),
                sesURL);
        cur.insertElementWithText(new QName(ns_addressing,"Action","wsa"),
        	"http://www.opengis.net/ses/DescribeSensorRequest");
        cur.insertElementWithText(new QName(ns_addressing,"MessageID","wsa"),
                UUID.randomUUID().toString());
        cur.beginElement(new QName(ns_addressing,"From","wsa"));
        cur.insertElementWithText(new QName(ns_addressing,"Address","wsa"),
        "http://www.w3.org/2005/08/addressing/role/anonymous");
        cur.dispose();

        cur = body.newCursor();

        cur.toFirstContentToken();
        cur.beginElement(new QName(ns_ses,"DescribeSensor","ses"));
        cur.insertAttributeWithValue("service","SES");
        cur.insertAttributeWithValue("version", SESAdapter.SUPPORTED_VERSIONS[0]);
        cur.insertElementWithText(new QName(ns_ses,"SensorID","ses"), 
                (String)parameter.getParameterShellWithCommonName(ISESRequestBuilder.DESCRIBE_SENSOR_SENSOR_ID).getSpecifiedValue());
        cur.dispose();

        request = envDoc.xmlText();

        return request;
    }
    
    public String buildUnsubscribeRequest(ParameterContainer parameter){
    	String request = "";
    	
        EnvelopeDocument envDoc = EnvelopeDocument.Factory.newInstance();
        Envelope env = envDoc.addNewEnvelope();
        Header header = env.addNewHeader();
        Body body = env.addNewBody();
        String sesURL = (String)parameter.getParameterShellWithCommonName(ISESRequestBuilder.UNSUBSCRIBE_SES_URL).getSpecifiedValue();
        String resourceId = (String)parameter.getParameterShellWithCommonName(ISESRequestBuilder.UNSUBSCRIBE_REFERENCE).getSpecifiedValue();
       
        String sesSubscriptionManagerContextPath = "";
        String[] split = sesURL.split("/");
        for (int i = 0; i < split.length-1; i++) {
			sesSubscriptionManagerContextPath += split[i]+"/";
		}
        sesSubscriptionManagerContextPath +="SubscriptionManagerContextPath";
        
        XmlCursor cur = null;

        SESUtils.addNamespacesToEnvelope_000(env);

        // header
        cur = header.newCursor();

        cur.toFirstContentToken();
        cur.insertElementWithText(new QName(ns_addressing,"To","wsa"),
                sesSubscriptionManagerContextPath);
        cur.insertElementWithText(new QName(ns_addressing,"Action","wsa"),
        	"http://docs.oasis-open.org/wsrf/rlw-2/ImmediateResourceTermination/DestroyRequest");
        cur.insertElementWithText(new QName(ns_addressing,"MessageID","wsa"),
                UUID.randomUUID().toString());
        cur.beginElement(new QName(ns_addressing,"From","wsa"));
        cur.insertElementWithText(new QName(ns_addressing,"Address","wsa"),
        "http://www.w3.org/2005/08/addressing/role/anonymous");
        
        cur.toNextToken();
        cur.beginElement(new QName(ns_resourceId, "ResourceId", "muse-wsa"));
        
        cur.insertAttributeWithValue(new QName(ns_addressing, "IsReferenceParameter"), "true");
        cur.insertChars(resourceId);
        cur.dispose();
        
        // body
        cur = body.newCursor();
        cur.toFirstContentToken();
        cur.beginElement(new QName(ns_wsNotification, "Destroy", "wsnt"));
        cur.dispose();
        request = envDoc.xmlText();
        
    	return request;
    }
    public static void main(String[] args) {
    	ParameterContainer paramCon = new ParameterContainer();
		try {
			paramCon.addParameterShell(ISESRequestBuilder.UNSUBSCRIBE_SES_URL, "http://localhost:8080/ses-main-3.0-SNAPSHOT/services/SesPortType");
			paramCon.addParameterShell(ISESRequestBuilder.UNSUBSCRIBE_REFERENCE, "MuseResource-3");
			SESRequestBuilder_00 request = new SESRequestBuilder_00();
			System.out.println(request.buildUnsubscribeRequest(paramCon));
		} catch (OXFException e) {
			e.printStackTrace();
		}
	}
}
