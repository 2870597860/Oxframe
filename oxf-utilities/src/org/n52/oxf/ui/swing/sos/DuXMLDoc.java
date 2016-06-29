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
        //����һ���µ��ַ���
        StringReader read = new StringReader(xmlDoc);
        //�����µ�����ԴSAX ��������ʹ�� InputSource ������ȷ����ζ�ȡ XML ����
        InputSource source = new InputSource(read);
        //����һ���µ�SAXBuilder
        SAXBuilder sb = new SAXBuilder();
        String a = null;
        try {
        	String SensorName=null;  //������������
        	String StatusValue=null;  //�������Ƿ����  
        	String OfferingId=null;  //������Ҫ�ص�ID
        	String OfferingName=null;  //������Ҫ�ص�����
        	String PositionReference=null;  //�������ο�ϵ������
        	
            //ͨ������Դ����һ��Document
            Document doc = sb.build(source);            
            Element root = doc.getRootElement();   //ȡ�ĸ�Ԫ��SensorML   
             
            
            List jiedian = root.getChildren();   
            Namespace ns = root.getNamespace();    //���XML�е������ռ䣨XML��δ����ɲ�д��     
            Namespace swe=root.getNamespace("http://www.opengis.net/swe/1.0.1");
            Namespace gml=root.getNamespace("http://www.opengis.net/gml");
            Element etmember = null;
            etmember=(Element)jiedian.get(0);      //�õ���Ԫ��
             
            
            List jiedian1 = etmember.getChildren();   
            Element etSystem=null;
            etSystem=(Element)jiedian1.get(0);    //�ҵ�sml:SystemԪ��
                      
            Element etIdentifierList=null; 
            Element etTerm=null;
            Element etvalue=null;
            Element etSimpleDataRecord=null; //SimpleDataRecordԪ��
            Element etStatusValue=null; //StatusValueԪ��
            Element etPosition=null; //PositionԪ��
            Element etVector=null; //VectorԪ��
            Element etoutput=null; //outputs�е�outputԪ��   
            Element etmetaDataProperty=null;
            Element etofferingId=null; //outputs�е�offeringԪ�� ��IDֵ
            Element etofferingName=null; //outputs�е�offeringԪ�� ������ 
             
            etIdentifierList=etSystem.getChild("identification", ns).getChild("IdentifierList", ns);  //���sml:IdentifierListԪ��
            etTerm=etIdentifierList.getChild("identifier", ns).getChild("Term", ns);  //���sml:TermԪ��
            etvalue=etTerm.getChild("value", ns); //���sml:valueԪ��
            SensorName=etvalue.getText();   //--------------------------------------------����������
             
            
            etSimpleDataRecord=etSystem.getChild("capabilities", ns).getChild("SimpleDataRecord",swe);
            etStatusValue=etSimpleDataRecord.getChild("field", swe).getChild("Boolean", swe).getChild("value", swe);
            StatusValue=etStatusValue.getText();    //-----------------------------�������Ƿ����
             
            etPosition=etSystem.getChild("position", ns).getChild("Position",swe);//PositionԪ��
            PositionReference=etPosition.getAttributeValue("referenceFrame");//------------------������λ�õĲο�ϵ
            
            
            etVector=etSystem.getChild("position", ns).getChild("Position",swe).getChild("location", swe).getChild("Vector", swe);
                      
            List listCoordinate = etVector.getChildren();   
            Element etCoordinate=null;

            String coordinate[]=new String[2];
            for(int i=0;i<listCoordinate.size();i++)
            {
            	if(i<2)
            	{
            	etCoordinate=(Element)listCoordinate.get(i);    //�ҵ�sml:SystemԪ��
            	coordinate[i]=etCoordinate.getChild("Quantity", swe).getChildText("value", swe);
            	}
            	else if(i==2)
            	{
            		etCoordinate=(Element)listCoordinate.get(i);    //�ҵ�sml:SystemԪ��
                	Element etQuantity=null;
                	etQuantity=etCoordinate.getChild("Quantity", swe); //�ҵ�QuantityԪ��
                	List listHeight = etQuantity.getChildren(); 
                	Element Height=null;
                	Height=(Element)listHeight.get(1);    
                	a=Height.getText();//---------------------�������߶�ֵ
                	 
            	}
            }
            
            etoutput=etSystem.getChild("outputs", ns).getChild("OutputList", ns).getChild("output", ns);  //���sml:IdentifierListԪ��
            etmetaDataProperty=etoutput.getChild("Text", swe).getChild("metaDataProperty", gml);//���metaDataPropertyԪ��
            etofferingId=etmetaDataProperty.getChild("offering", gml).getChild("id", gml);
            etofferingName=etmetaDataProperty.getChild("offering", gml).getChild("name", gml);
            OfferingId=etofferingId.getText();
            OfferingName=etofferingName.getText();
            
            JFrame jDescribeSensor=new JFrame();   //�½�һ������ʾ�����������Ĳ���
            jDescribeSensor.setTitle("�������Ĳ���");
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
            
            jlabel1.setText("������");
            jlabel2.setText("������״̬");
            jlabel3.setText("��γ");
            jlabel4.setText("��γ");
            jlabel5.setText("�߶�");
            jlabel6.setText("��������Ҫ������");
            jlabel7.setText("��������Ҫ��ID");
            jlabel8.setText("�ο�ϵ");
            
            
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
            // TODO �Զ����� catch ��
            e.printStackTrace();
        } catch (IOException e) {
            // TODO �Զ����� catch ��
            e.printStackTrace();
        }
        return null;
    }
}
