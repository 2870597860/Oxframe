package org.n52.oxf.ui.swing.sos;
import java.awt.GridLayout;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.xml.sax.InputSource;

public class DuXMLDoc {
    public List xmlElements(String xmlDoc) {
        //创建一个新的字符串
        StringReader read = new StringReader(xmlDoc);
        //创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
        InputSource source = new InputSource(read);
        //创建一个新的SAXBuilder
        SAXBuilder sb = new SAXBuilder();
        String a = null;
        try {
        	String SensorName=null;  //传感器的名字
        	String StatusValue=null;  //传感器是否可用  
        	String OfferingId=null;  //传感器要素的ID
        	String OfferingName=null;  //传感器要素的名字
        	String PositionReference=null;  //传感器参考系的名字
        	
            //通过输入源构造一个Document
            Document doc = sb.build(source);            
            Element root = doc.getRootElement();   //取的根元素SensorML   
             
            
            List jiedian = root.getChildren();   
            Namespace ns = root.getNamespace();    //获得XML中的命名空间（XML中未定义可不写）     
            Namespace swe=root.getNamespace("http://www.opengis.net/swe/1.0.1");
            Namespace gml=root.getNamespace("http://www.opengis.net/gml");
            Element etmember = null;
            etmember=(Element)jiedian.get(0);      //得到子元素
             
            
            List jiedian1 = etmember.getChildren();   
            Element etSystem=null;
            etSystem=(Element)jiedian1.get(0);    //找到sml:System元素
                      
            Element etIdentifierList=null; 
            Element etTerm=null;
            Element etvalue=null;
            Element etSimpleDataRecord=null; //SimpleDataRecord元素
            Element etStatusValue=null; //StatusValue元素
            Element etPosition=null; //Position元素
            Element etVector=null; //Vector元素
            Element etoutput=null; //outputs中的output元素   
            Element etmetaDataProperty=null;
            Element etofferingId=null; //outputs中的offering元素 的ID值
            Element etofferingName=null; //outputs中的offering元素 的名字 
             
            etIdentifierList=etSystem.getChild("identification", ns).getChild("IdentifierList", ns);  //获得sml:IdentifierList元素
            etTerm=etIdentifierList.getChild("identifier", ns).getChild("Term", ns);  //获得sml:Term元素
            etvalue=etTerm.getChild("value", ns); //获得sml:value元素
            SensorName=etvalue.getText();   //--------------------------------------------传感器名字
             
            
            etSimpleDataRecord=etSystem.getChild("capabilities", ns).getChild("SimpleDataRecord",swe);
            etStatusValue=etSimpleDataRecord.getChild("field", swe).getChild("Boolean", swe).getChild("value", swe);
            StatusValue=etStatusValue.getText();    //-----------------------------传感器是否可用
             
            etPosition=etSystem.getChild("position", ns).getChild("Position",swe);//Position元素
            PositionReference=etPosition.getAttributeValue("referenceFrame");//------------------传感器位置的参考系
            
            
            etVector=etSystem.getChild("position", ns).getChild("Position",swe).getChild("location", swe).getChild("Vector", swe);
                      
            List listCoordinate = etVector.getChildren();   
            Element etCoordinate=null;

            String coordinate[]=new String[2];
            for(int i=0;i<listCoordinate.size();i++)
            {
            	if(i<2)
            	{
            	etCoordinate=(Element)listCoordinate.get(i);    //找到sml:System元素
            	coordinate[i]=etCoordinate.getChild("Quantity", swe).getChildText("value", swe);
            	}
            	else if(i==2)
            	{
            		etCoordinate=(Element)listCoordinate.get(i);    //找到sml:System元素
                	Element etQuantity=null;
                	etQuantity=etCoordinate.getChild("Quantity", swe); //找到Quantity元素
                	List listHeight = etQuantity.getChildren(); 
                	Element Height=null;
                	Height=(Element)listHeight.get(1);    
                	a=Height.getText();//---------------------传感器高度值
                	 
            	}
            }
            
            etoutput=etSystem.getChild("outputs", ns).getChild("OutputList", ns).getChild("output", ns);  //获得sml:IdentifierList元素
            etmetaDataProperty=etoutput.getChild("Text", swe).getChild("metaDataProperty", gml);//获得metaDataProperty元素
            etofferingId=etmetaDataProperty.getChild("offering", gml).getChild("id", gml);
            etofferingName=etmetaDataProperty.getChild("offering", gml).getChild("name", gml);
            OfferingId=etofferingId.getText();
            OfferingName=etofferingName.getText();
            
            JFrame jDescribeSensor=new JFrame();   //新建一个框显示描述传感器的参数
            jDescribeSensor.setTitle("传感器的参数");
            jDescribeSensor.setLayout(new GridLayout(2,1));
            JLabel jlabel1=new JLabel();
            JLabel jlabel2=new JLabel();
            JLabel jlabel3=new JLabel();
            JLabel jlabel4=new JLabel();
            JLabel jlabel5=new JLabel();
            JLabel jlabel6=new JLabel();
            JLabel jlabel7=new JLabel();
            JLabel jlabel8=new JLabel();
            
            JTextField jtext1=new JTextField();
            JTextField jtext2=new JTextField();
            JTextField jtext3=new JTextField();
            JTextField jtext4=new JTextField();
            JTextField jtext5=new JTextField();
            JTextField jtext6=new JTextField();
            JTextField jtext7=new JTextField(); 
            JTextField jtext8=new JTextField();      
            
            jlabel1.setText("传感器");
            jlabel2.setText("传感器状态");
            jlabel3.setText("北纬");
            jlabel4.setText("南纬");
            jlabel5.setText("高度");
            jlabel6.setText("传感器的要素名称");
            jlabel7.setText("传感器的要素ID");
            jlabel8.setText("参考系");
            
            
            jtext1.setText(SensorName);
            jtext2.setText(StatusValue);
            jtext3.setText(coordinate[1]);
            jtext4.setText(coordinate[0]);
            jtext5.setText(a);
            jtext6.setText(OfferingName);
            jtext7.setText(OfferingId);
            jtext8.setText(PositionReference);            
            
            JPanel jp1=new JPanel();
            JPanel jp2=new JPanel(); 
            jp1.setLayout(new GridLayout(4,2));
            jp2.setLayout(new GridLayout(4,2));
             
            
            jp1.add(jlabel1);
            jp1.add(jtext1);
            jp1.add(jlabel2);
            jp1.add(jtext2);
            jp1.add(jlabel6);
            jp1.add(jtext6);
            jp1.add(jlabel7);
            jp1.add(jtext7); 
            
            jp2.add(jlabel8);
            jp2.add(jtext8); 
            jp2.add(jlabel3);
            jp2.add(jtext3);
            jp2.add(jlabel4);
            jp2.add(jtext4);
            jp2.add(jlabel5);
            jp2.add(jtext5);
             
            jDescribeSensor.add(jp1);
            jDescribeSensor.add(jp2);
            
            jDescribeSensor.pack();
            jDescribeSensor.setVisible(true);
           
          
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
