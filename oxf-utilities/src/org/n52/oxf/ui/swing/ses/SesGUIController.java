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
package org.n52.oxf.ui.swing.ses;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.n52.oxf.OXFException;
import org.n52.oxf.context.LayerContext;
import org.n52.oxf.owsCommon.ExceptionReport;
import org.n52.oxf.owsCommon.ServiceDescriptor;
import org.n52.oxf.owsCommon.capabilities.Operation;
import org.n52.oxf.render.sos.FeatureGeometryRenderer;
import org.n52.oxf.serviceAdapters.IServiceAdapter;
import org.n52.oxf.serviceAdapters.OperationResult;
import org.n52.oxf.serviceAdapters.ParameterContainer;
import org.n52.oxf.serviceAdapters.ses.ISESRequestBuilder;
import org.n52.oxf.serviceAdapters.ses.SESAdapter;
import org.n52.oxf.serviceAdapters.ses.SESRequestBuilderFactory;
import org.n52.oxf.serviceAdapters.ses.TestSESAdapter;
import org.n52.oxf.serviceAdapters.sos.SOSAdapter;
import org.n52.oxf.ui.swing.ApprovalDialog;
import org.n52.oxf.ui.swing.MapCanvas;
import org.n52.oxf.ui.swing.ShowRequestDialog;
import org.n52.oxf.ui.swing.ShowXMLDocDialog;
import org.n52.oxf.ui.swing.tree.ContentTree;
import org.n52.oxf.util.IOHelper;


/**
 * 
 * @author Artur Osmanov <artur.osmanov@uni-muenster.de>
 *
 */
public class SesGUIController {
	private static final Logger LOGGER = Logger.getLogger(SesGUIController.class);
	public static String a=null;
	private SesGUI view;
    private MapCanvas map = null;
    private ContentTree tree = null;
    private Component owner = null;
    private String serviceURL;
    private String consumerReference;
    private FeatureGeometryRenderer renderer;
    
    /**
     * 
     * @param gui
     * @param map
     * @param tree
     * @param owner
     */
    public SesGUIController(SesGUI gui, MapCanvas map, ContentTree tree, Component owner) {
        this.view = gui;
        this.map = map;
        this.tree = tree;
        this.owner = owner;
        this.renderer = new FeatureGeometryRenderer();
        this.loadURLs();
    }
    
    
    @SuppressWarnings("unchecked")
	private void loadURLs() {
        Properties properties = new Properties();
        try {
            properties.load(IServiceAdapter.class.getResourceAsStream("/serviceURLs.properties"));

            Enumeration propKeys = properties.keys();
            while (propKeys.hasMoreElements()) {
                String key = (String) propKeys.nextElement();

                if (key.substring(0, 3) != null && key.substring(0, 3).equals("SES")) {
                    String url = (String) properties.get(key);
                    view.getJComboBoxService().addItem(url);
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }


	public void actionPerformed_GetCapabilitiesButton() {
		try {
			serviceURL = view.getJComboBoxService().getSelectedItem().toString();
			
			ParameterContainer paramCon = new ParameterContainer();
			paramCon.addParameterShell(ISESRequestBuilder.GET_CAPABILITIES_SES_URL, serviceURL);
			SESAdapter adapter = new SESAdapter(paramCon);
		
			OperationResult opResult = adapter.doOperation(new Operation(SESAdapter.GET_CAPABILITIES, 
	                serviceURL, serviceURL), paramCon);
			String getObsDoc = IOHelper.readText(opResult.getIncomingResultAsStream());
			
			// center dialog
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			Dimension size = new Dimension(800, 355);
			screenSize.height = screenSize.height/2;
			screenSize.width = screenSize.width/2;
			size.height = size.height/2;
			size.width = size.width/2;
			int y = screenSize.height - size.height;
			int x = screenSize.width - size.width;
	        new ShowXMLDocDialog(new Point(x,y), "GetCapabilities", getObsDoc).setVisible(true);
	        
		} catch (ExceptionReport e) {
			e.printStackTrace();
		} catch (OXFException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public void actionPerformed_SubscribeButton() {
		String topic = null;
		String observedProperty = null;
		String observedSecondProperty=null; //第二个属性输入值
		String filterLevel = null;
		String propertyThreshold=null;//门限
		String propertyThresholdValue=null;//第一个门限值
		String propertySecondThreshold=null;//第二个门限
		String propertySecondThresholdValue=null;//第二个门限值
		

		LayerContext context = map.getLayerContext();
		if (context.getLayerCount() !=0) {
		//if (true) {

			if (!view.getJComboBoxTopic().getSelectedItem().toString().equals("")&& !view.getJComboBoxobservedProperty().getSelectedItem().toString().equals("")) 
			{
			     
				topic = view.getJComboBoxTopic().getSelectedItem().toString();
				observedProperty = view.getJComboBoxobservedProperty().getSelectedItem().toString();
				filterLevel = view.getJComboBoxFilterType().getSelectedItem().toString();
				a=filterLevel;
				serviceURL = view.getJComboBoxService().getSelectedItem().toString();
				consumerReference = view.getJTextFieldConsumerRef().getText().trim();
				propertyThreshold=view.getJComboBoxThreshold().getSelectedItem().toString();//添加的
				propertyThresholdValue=view.getJTextFieldThreshold().getText().toString();//添加的
                 
			    	
                	try {
					ParameterContainer paramCon = new ParameterContainer();
					paramCon.addParameterShell(ISESRequestBuilder.SUBSCRIBE_SES_URL, serviceURL);
					paramCon.addParameterShell(ISESRequestBuilder.SUBSCRIBE_CONSUMER_REFERENCE_ADDRESS,consumerReference);
					//paramCon.addParameterShell(ISESRequestBuilder.SUBSCRIBE_FILTER_MESSAGE_CONTENT_DIALECT,"http://www.w3.org/TR/1999/REC-xpath-19991116");
					paramCon.addParameterShell(ISESRequestBuilder.SUBSCRIBE_FILTER_TOPIC, topic);
					paramCon.addParameterShell(ISESRequestBuilder.SUBSCRIBE_FILTER_MESSAGE_CONTENT,observedProperty); //修改的
					
					paramCon.addParameterShell(ISESRequestBuilder.SUBSCRIBE_FILTER_MESSAGE_CONTENT_THRESHOLD,propertyThreshold); //修改的
					paramCon.addParameterShell(ISESRequestBuilder.SUBSCRIBE_FILTER_MESSAGE_CONTENT_THRESHOLD_VALUE,propertyThresholdValue);//添加的
					LOGGER.info("检测等级："+filterLevel);
					//System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"+filterLevel);
					if(filterLevel.equals("Filter Level 1")){
						//TODO Filterlevelunterscheidung
						paramCon.addParameterShell(ISESRequestBuilder.SUBSCRIBE_FILTER_MESSAGE_CONTENT_DIALECT,"http://www.w3.org/TR/1999/REC-xpath-19991116");
					}
					else if(filterLevel.equals("Filter Level 2")){
						paramCon.addParameterShell(ISESRequestBuilder.SUBSCRIBE_FILTER_MESSAGE_CONTENT_DIALECT,"http://www.opengis.net/ses/filter/level2");
						
					}
					else if(filterLevel.equals("Filter Level 3")){
						System.out.println("=====================level3zaici");
						paramCon.addParameterShell(ISESRequestBuilder.SUBSCRIBE_FILTER_MESSAGE_CONTENT_DIALECT,"http://www.opengis.net/ses/filter/level3");
		      			observedSecondProperty = view.getJComboBoxobservedSecondProperty().getSelectedItem().toString();//第二个属性输入框			 
	    				propertySecondThreshold=view.getJComboBoxSecondThreshold().getSelectedItem().toString();//第二个门限
	    				propertySecondThresholdValue=view.getJTextFieldSecondThreshold().getText().toString();//第二个门限值输入框
	    				paramCon.addParameterShell(ISESRequestBuilder.SUBSCRIBE_FILTER_MESSAGE_SECOND_CONTENT,observedSecondProperty); //第二个属性
	    				paramCon.addParameterShell(ISESRequestBuilder.SUBSCRIBE_FILTER_MESSAGE_CONTENT_SECOND_THRESHOLD,propertySecondThreshold); //添加的第二个门限    		 
	    				paramCon.addParameterShell(ISESRequestBuilder.SUBSCRIBE_FILTER_MESSAGE_CONTENT_SECOND_THRESHOLD_VALUE,propertySecondThresholdValue);//添加的第二个门限值
					}
					 
					paramCon.addParameterShell("FilterLevel", filterLevel);
					
					LOGGER.info("paramCom:     "+paramCon);
					//System.out.println("paramCom是==================================================");
					//System.out.println(paramCon);

					SESAdapter adapter = new SESAdapter(paramCon);

					ServiceDescriptor descriptor = adapter.initService(serviceURL);
					// Subscribe
					//LOGGER.info("Subscribe ist auskommentiert");
					System.out.println("Subscribe ist auskommentiert");
					 
					OperationResult opResult = adapter.doOperation(new Operation(SESAdapter.SUBSCRIBE, serviceURL,serviceURL), paramCon);
					
					 //String describeSensorDoc = IOHelper.readText(opResult.getIncomingResultAsStream());//添加的
					 //System.out.println("------------------------SES响应文档-------------------");
					 //System.out.println(describeSensorDoc);
					 
					SesLayerAdder adder = new SesLayerAdder();

					adder.addSesLayer(this.owner, this.map, this.tree,"Notifications00", "SES", adapter, descriptor,renderer, null, paramCon);}
                	  catch (ExceptionReport e) {
					e.printStackTrace();
				} catch (OXFException e) {
					e.printStackTrace();
				}
			} 
                
            else {
				JOptionPane.showMessageDialog(null,"One or more inputs are missing or invalid", "Warning",JOptionPane.WARNING_MESSAGE);
			}
		}
			 
			else{
			JOptionPane.showMessageDialog(null,"The extent of the ContextBoundingBox has to be set.", "Warning",JOptionPane.WARNING_MESSAGE);
		}   	
			    }
                 	
	
	
	 


	public void actionPerformed_UnsubscribeButton() {
		try {
			String serviceURL = view.getJComboBoxService().getSelectedItem().toString();
			String resourceInstance = "MuseResource-"+ view.getJTextFieldResource().getText();
			
			ParameterContainer paramCon = new ParameterContainer();
			paramCon.addParameterShell(ISESRequestBuilder.UNSUBSCRIBE_SES_URL, serviceURL);
			paramCon.addParameterShell(ISESRequestBuilder.UNSUBSCRIBE_REFERENCE, resourceInstance);
			
			SESAdapter adapter = new SESAdapter(paramCon);
			ServiceDescriptor descriptor = adapter.initService(serviceURL);
			OperationResult opResult = adapter.doOperation(new Operation(SESAdapter.UNSUBSCRIBE, serviceURL,serviceURL), paramCon);
			
			if (!opResult.toString().contains("soap:Fault")) {
				JOptionPane.showMessageDialog(null,"Unsubscribe successful!", resourceInstance,JOptionPane.INFORMATION_MESSAGE);
			}else{
				JOptionPane.showMessageDialog(null,"Unsubscribe failed!", resourceInstance,JOptionPane.WARNING_MESSAGE);
			}
		} catch (OXFException e) {
			e.printStackTrace();
		} catch (ExceptionReport e) {
			e.printStackTrace();
		}
	}

}
