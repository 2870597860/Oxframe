package org.n52.oxf.ui.swing.ses;
import java.awt.GridLayout;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.xml.sax.InputSource;

public class DuSesXmlDoc {
	
	static String strbeginPosion=null; //---------------��ʼʱ��
	static String strendPosition=null; //---------------����ʱ��
	static String strNotificationValue=null; //---------------Notification��ֵ
    
	
	static String strPosition1=null;   //-----------------------��������λ��
		static String strProcedure1=null;  //------------------------Ҫ�ص�ֵ
		static String strObservedProperty1=null;  //-----------------�۲�����
		static String strGmlName1=null;   //------------------------������������
		static String strResult1=null;    //------------------------�����������ֵ
		
		static String strPosition2=null;   //-----------------------��������λ��
		static String strProcedure2=null;   //-----------------------������Ԫ�ص�����
		static String strObservedProperty2=null; //------------------�۲�����
		static String strGmlName2=null;   //------------------------������������
		static String strResult2=null;    //------------------------�����������ֵ
		
    public List xmlElements(String xmlDoc) {
        //����һ���µ��ַ���
        StringReader read = new StringReader(xmlDoc);
        //�����µ�����ԴSAX ��������ʹ�� InputSource ������ȷ����ζ�ȡ XML ����
        InputSource source = new InputSource(read);
        //����һ���µ�SAXBuilder
        SAXBuilder sb = new SAXBuilder();
        String a = null;
        try {
            //ͨ������Դ����һ��Document
            Document doc = sb.build(source);            
            Element root = doc.getRootElement();   //ȡ�ĸ�Ԫ��SensorML   
             
            
            List jiedian = root.getChildren();   
            Namespace ns = root.getNamespace();    //���XML�е������ռ䣨XML��δ����ɲ�д��     
             
            List jiedian1 = root.getChildren();   
            Namespace nsSes = root.getNamespace();    //���XML�е������ռ䣨XML��δ����ɲ�д��     
            Namespace swe=root.getNamespace("http://www.opengis.net/swe/1.0.1");
            Namespace gml=root.getNamespace("http://www.opengis.net/gml");
            Namespace om=root.getNamespace("http://www.opengis.net/om/1.0");
            Namespace eml=root.getNamespace("http://www.opengis.net/em/0.2.0");
            Namespace xlin=root.getNamespace("http://www.w3.org/1999/xlink");
            Namespace ns1=root.getNamespace("http://www.opengis.net/sampling/1.0");
            
            Element etmember = null;
            Element etAttribute2=null;
            etmember=(Element)jiedian.get(0);      //�õ���Ԫ��
            
            List jiedian11 = etmember.getChildren();   
            Element etEventTime=null;
            Element etTimePeriod=null;
            Element etbeginPosition=null;
            Element endPosition=null;
            Element etNotificationValue=null;
            Element etElement1=null;
            Element etElement2=null;
             
            
            
            etEventTime=(Element)jiedian1.get(0);    //�ҵ�EventTimeԪ��
            etAttribute2=(Element)jiedian1.get(2);  //�ڶ�����Ԫ��
            etElement1=(Element)jiedian1.get(3); //��������Ԫ�أ���һ������������Ϣ
            etElement2=(Element)jiedian1.get(4); //���ĸ���Ԫ�أ��ڶ�������������Ϣ
             
            etbeginPosition=etEventTime.getChild("TimePeriod", gml).getChild("beginPosition", gml); //---------
            endPosition=etEventTime.getChild("TimePeriod", gml).getChild("endPosition", gml);
            
            strbeginPosion=etbeginPosition.getText();
            strendPosition=endPosition.getText();
            
            etNotificationValue=etAttribute2.getChild("NamedValue",om).getChild("value", om);
           
            strNotificationValue=etNotificationValue.getText();  
         
       		//-------------������һ����������ֵ---------------------------------
            Element etEventRelationship1=null;
       		Element ettarget1=null;
       		Element etObservation1=null;
       		Element etlocation1=null;
       		Element etposition1=null;  
       		Element etobservedProperty1=null;
       		Element etfeatureOfInterest1=null;
       		Element etresult1=null;
       		Element etsamplingPoint1=null;
       		Attribute attObservedProperty1=null;
       		Attribute etgmlId1=null;
       		Element etgmlName1=null;
       		
       		
       		
       		etEventRelationship1=etElement1.getChild("EventEventRelationship",eml);
       		 
       		List EventRelationship1=etEventRelationship1.getChildren();
       	    ettarget1=(Element) EventRelationship1.get(1);    
       	    
       		etObservation1=ettarget1.getChild("Observation",om);
       		List ObservationElement1=etObservation1.getChildren();  //Observation����Ԫ��
       		 
       		etlocation1=(Element) ObservationElement1.get(0);
       		etobservedProperty1=(Element) ObservationElement1.get(3);  //-----------featureOfInterestҪ��
       		etfeatureOfInterest1=(Element) ObservationElement1.get(4); 
       		etresult1=(Element) ObservationElement1.get(5);  
       		 
       		etposition1=etlocation1.getChild("Point",gml).getChild("pos",gml);
       		
       		etsamplingPoint1=etfeatureOfInterest1.getChild("SamplingPoint",ns1);  //SamplingPointԪ��
       		etgmlName1=etsamplingPoint1.getChild("name",gml);
       		
       		List listObservedProperty=etobservedProperty1.getAttributes(); 
       		attObservedProperty1=(Attribute) listObservedProperty.get(0);      		  
       		strObservedProperty1=attObservedProperty1.getValue();
       		 
       		List sp=etsamplingPoint1.getAttributes();  
       		etgmlId1=(Attribute) sp.get(0);      		  
       		strProcedure1=etgmlId1.getValue();
        	
       		strPosition1=etposition1.getText();
       		
       		strGmlName1=etgmlName1.getText();
       		strResult1=etresult1.getText();
       	 
       		System.out.println("��1��λ����"+strPosition1.indexOf(0));
       		
       		System.out.println(strGmlName1);
       		System.out.println(strPosition1);
       		System.out.println(strObservedProperty1);
       		System.out.println(strProcedure1);
       		System.out.println(strResult1);
       		
       		
       		
       		//--------------------�����ڶ�����������ֵ---------------------------------------------
       		Element etEventRelationship2=null;
       		Element ettarget2=null;
       		Element etObservation2=null;
       		Element etlocation2=null;
       		Element etposition2=null;  
       		Element etobservedProperty2=null;
       		Element etfeatureOfInterest2=null;
       		Element etresult2=null;
       		Element etsamplingPoint2=null;
       		Attribute attObservedProperty2=null;
       		Attribute etgmlId2=null;
       		Element etgmlName2=null;
       		
       		
       		
       		etEventRelationship2=etElement2.getChild("EventEventRelationship",eml);
       		 
       		List EventRelationship2=etEventRelationship2.getChildren();
       	    ettarget2=(Element) EventRelationship2.get(1);    
       	    
       		etObservation2=ettarget2.getChild("Observation",om);
       		List ObservationElement2=etObservation2.getChildren();  //Observation����Ԫ��
       		
       		etlocation2=(Element) ObservationElement2.get(0);
       		etobservedProperty2=(Element) ObservationElement2.get(3);  //-----------featureOfInterestҪ��
       		etfeatureOfInterest2=(Element) ObservationElement2.get(4); 
       		etresult2=(Element) ObservationElement2.get(5);  
       		 
       		etposition2=etlocation2.getChild("Point",gml).getChild("pos",gml);
       		etsamplingPoint2=etfeatureOfInterest2.getChild("SamplingPoint",ns1);
       		etgmlName2=etsamplingPoint2.getChild("name",gml);
       		
       		List listObservedProperty2=etobservedProperty2.getAttributes(); 
       		attObservedProperty2=(Attribute) listObservedProperty2.get(0);      		  
       		strObservedProperty2=attObservedProperty2.getValue();
       		
       		List sp2=etsamplingPoint2.getAttributes(); 
       		etgmlId2=(Attribute) sp2.get(0);      		  
       		strProcedure2=etgmlId2.getValue();  
       		
       		strPosition2=etposition2.getText();
       		strGmlName2=etgmlName2.getText();
       		strResult2=etresult2.getText();
       		
       		System.out.println(strGmlName2);
       		System.out.println(strPosition2);
       		System.out.println(strObservedProperty2);
       		System.out.println(strProcedure2);
       		System.out.println(strResult2);
       		
       		 
          
        } catch (JDOMException e) {
            // TODO �Զ����� catch ��
            e.printStackTrace();
        } catch (IOException e) {
            // TODO �Զ����� catch ��
            e.printStackTrace();
        }
        return null;
    }
}
