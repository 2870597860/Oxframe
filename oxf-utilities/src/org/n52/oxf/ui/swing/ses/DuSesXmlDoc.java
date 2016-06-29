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
	
	static String strbeginPosion=null; //---------------开始时间
	static String strendPosition=null; //---------------结束时间
	static String strNotificationValue=null; //---------------Notification的值
    
	
	static String strPosition1=null;   //-----------------------传感器的位置
		static String strProcedure1=null;  //------------------------要素的值
		static String strObservedProperty1=null;  //-----------------观测属性
		static String strGmlName1=null;   //------------------------传感器的名字
		static String strResult1=null;    //------------------------传感器结果的值
		
		static String strPosition2=null;   //-----------------------传感器的位置
		static String strProcedure2=null;   //-----------------------传感器元素的名字
		static String strObservedProperty2=null; //------------------观测属性
		static String strGmlName2=null;   //------------------------传感器的名字
		static String strResult2=null;    //------------------------传感器结果的值
		
    public List xmlElements(String xmlDoc) {
        //创建一个新的字符串
        StringReader read = new StringReader(xmlDoc);
        //创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
        InputSource source = new InputSource(read);
        //创建一个新的SAXBuilder
        SAXBuilder sb = new SAXBuilder();
        String a = null;
        try {
            //通过输入源构造一个Document
            Document doc = sb.build(source);            
            Element root = doc.getRootElement();   //取的根元素SensorML   
             
            
            List jiedian = root.getChildren();   
            Namespace ns = root.getNamespace();    //获得XML中的命名空间（XML中未定义可不写）     
             
            List jiedian1 = root.getChildren();   
            Namespace nsSes = root.getNamespace();    //获得XML中的命名空间（XML中未定义可不写）     
            Namespace swe=root.getNamespace("http://www.opengis.net/swe/1.0.1");
            Namespace gml=root.getNamespace("http://www.opengis.net/gml");
            Namespace om=root.getNamespace("http://www.opengis.net/om/1.0");
            Namespace eml=root.getNamespace("http://www.opengis.net/em/0.2.0");
            Namespace xlin=root.getNamespace("http://www.w3.org/1999/xlink");
            Namespace ns1=root.getNamespace("http://www.opengis.net/sampling/1.0");
            
            Element etmember = null;
            Element etAttribute2=null;
            etmember=(Element)jiedian.get(0);      //得到子元素
            
            List jiedian11 = etmember.getChildren();   
            Element etEventTime=null;
            Element etTimePeriod=null;
            Element etbeginPosition=null;
            Element endPosition=null;
            Element etNotificationValue=null;
            Element etElement1=null;
            Element etElement2=null;
             
            
            
            etEventTime=(Element)jiedian1.get(0);    //找到EventTime元素
            etAttribute2=(Element)jiedian1.get(2);  //第二个子元素
            etElement1=(Element)jiedian1.get(3); //第三个子元素：第一个传感器的消息
            etElement2=(Element)jiedian1.get(4); //第四个子元素：第二个传感器的消息
             
            etbeginPosition=etEventTime.getChild("TimePeriod", gml).getChild("beginPosition", gml); //---------
            endPosition=etEventTime.getChild("TimePeriod", gml).getChild("endPosition", gml);
            
            strbeginPosion=etbeginPosition.getText();
            strendPosition=endPosition.getText();
            
            etNotificationValue=etAttribute2.getChild("NamedValue",om).getChild("value", om);
           
            strNotificationValue=etNotificationValue.getText();  
         
       		//-------------解析第一个传感器的值---------------------------------
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
       		List ObservationElement1=etObservation1.getChildren();  //Observation的子元素
       		 
       		etlocation1=(Element) ObservationElement1.get(0);
       		etobservedProperty1=(Element) ObservationElement1.get(3);  //-----------featureOfInterest要素
       		etfeatureOfInterest1=(Element) ObservationElement1.get(4); 
       		etresult1=(Element) ObservationElement1.get(5);  
       		 
       		etposition1=etlocation1.getChild("Point",gml).getChild("pos",gml);
       		
       		etsamplingPoint1=etfeatureOfInterest1.getChild("SamplingPoint",ns1);  //SamplingPoint元素
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
       	 
       		System.out.println("第1个位置是"+strPosition1.indexOf(0));
       		
       		System.out.println(strGmlName1);
       		System.out.println(strPosition1);
       		System.out.println(strObservedProperty1);
       		System.out.println(strProcedure1);
       		System.out.println(strResult1);
       		
       		
       		
       		//--------------------解析第二个传感器的值---------------------------------------------
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
       		List ObservationElement2=etObservation2.getChildren();  //Observation的子元素
       		
       		etlocation2=(Element) ObservationElement2.get(0);
       		etobservedProperty2=(Element) ObservationElement2.get(3);  //-----------featureOfInterest要素
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
            // TODO 自动生成 catch 块
            e.printStackTrace();
        } catch (IOException e) {
            // TODO 自动生成 catch 块
            e.printStackTrace();
        }
        return null;
    }
}
