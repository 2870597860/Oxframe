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

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;
import java.awt.Font;
import java.awt.Color;
import java.awt.Rectangle;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JDialog;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextArea;

import org.n52.oxf.ui.swing.MapCanvas;
import org.n52.oxf.ui.swing.tree.ContentTree;
import java.awt.Point;
import java.io.IOException;

public class SesGUI extends JDialog {
	
	
	private SesGUIController controller;
    private Component owner = null;
    private String consumer = "http://111.186.105.167:8083/";
    private JDialog jDialog = null;  //  @jve:decl-index=0:visual-constraint="56,47"
	private JPanel jContentPane = null;
	private JPanel jPanelService = null;
	private JPanel jPanelSubscription = null;
	private JPanel jPanelFilter=null;//添加的滤波器条件面板
	private JLabel jLabelService = null;
	private JComboBox jComboBoxService = null;
	private JButton jButtonGetCapabilities = null;
	private JLabel jLabel1Topic = null;   
	private JLabel jLabelSecondFilter = null;  //第二个滤波器的属性标签
	private JComboBox jComboBoxTopic = null;
	private JLabel jLabelFilterType = null;
	private JComboBox jComboBoxFilterType = null;
	private JLabel jLabelFilter = null;
	private JComboBox jComboBoxobservedProperty = null;  //private JTextArea jTextAreaFilter = null;  修改
	private JComboBox jComboBoxThreshold=null;//添加的门限选项   
	private JComboBox jComboBoxSecondThreshold=null;//添加的第二个滤波器的门限选项
	private JComboBox jComboBoxobservedSecondProperty=null;//添加的第二个属性输入框
	private JTextField jTextFieldThreshold=null;//添加门限值输入框
	private JTextField jTextFieldSecondThreshold = null;////添加第二个门限值输入框
	private JButton jButtonSubscribe = null;
	private JButton jButtonUnsubscribe = null;
	private JLabel jLabelConsumerRef = null;
	private JTextField jTextFieldConsumerRef = null;     
	private JPanel jPanelUnsubscribe = null;	 
	private JLabel jLabelResource = null;
	private JTextField jTextFieldResource = null;
	private JCheckBox jCheckFire=null;
	private JCheckBox jCheckPM10=null;
	private JCheckBox jCheckCXHX=null;
	private JCheckBox jCheckCO=null;
	private JCheckBox jCheckGas=null;
	private JPanel jSystempanel=null;

	public SesGUI(JFrame owner, MapCanvas map, ContentTree tree) {
        this.owner = owner;
        getJDialog();
        controller = new SesGUIController(this, map, tree, owner);
	}

	/**
	 * This method initializes jDialog	
	 * 	
	 * @return javax.swing.JDialog	
	 */
	private JDialog getJDialog() {
		if (jDialog == null) {
			owner.getX();
			jDialog = new JDialog();
			jDialog.setSize(new Dimension(600, 500));
			//jDialog.setTitle("家庭智能安防事件订阅");
			jDialog.setTitle("年报信息事件订阅");
			jDialog.setResizable(false);
			
			//Center dialog
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			Dimension size = jDialog.getSize();
			screenSize.height = screenSize.height/2;
			screenSize.width = screenSize.width/2;
			size.height = size.height/2;
			size.width = size.width/2;
			int y = screenSize.height - size.height;
			int x = screenSize.width - size.width;
			
			jDialog.setLocation(new Point(x, y));
			jDialog.setContentPane(getJContentPane());
			 
			jDialog.setVisible(true);
		}
		return jDialog;
	}

	/**
	 * This method initializes jContentPane	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.add(getJPanelService(), null);
			jContentPane.add(getJPanelSubscription(), null);
			//jContentPane.add(getSystemPanel(), null);
			
			jContentPane.add(getJPanelFilter(),null); //添加的滤波器条件面板
			jContentPane.add(getJPanelUnsubscribe(), null);//检测管理面板，包括订阅按钮和取消订阅按钮
			 
		}
		return jContentPane;
	}

	/**
	 * This method initializes jPanelService	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelService() {
		if (jPanelService == null) {
			jLabelConsumerRef = new JLabel();
			jLabelConsumerRef.setBounds(new Rectangle(10, 44, 125, 17));
			jLabelConsumerRef.setText("用户地址:");
			jLabelService = new JLabel();
			jLabelService.setBounds(new Rectangle(10, 20, 126, 20));
			jLabelService.setText("SES地址:");
			jPanelService = new JPanel();
			jPanelService.setLayout(null);
			jPanelService.setBounds(new Rectangle(10, 13, 580, 106));
			jPanelService.setBorder(BorderFactory.createTitledBorder(null, "SES基本设置", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
			jPanelService.add(jLabelService, null);
			jPanelService.add(getJComboBoxService(), null);
			jPanelService.add(getJButtonGetCapabilities(), null);
			jPanelService.add(jLabelConsumerRef, null);
			jPanelService.add(getJTextFieldConsumerRef(), null);
		}
		return jPanelService;
	}

	/**
	 * This method initializes jPanelSubscription	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelSubscription() {
		if (jPanelSubscription == null) {
			 
			jLabelFilterType = new JLabel();
			jLabelFilterType.setBounds(new Rectangle(14, 50, 100, 20));
			jLabelFilterType.setText("监测等级:");
			jLabel1Topic = new JLabel();//
			jLabel1Topic.setBounds(new Rectangle(13, 24, 86, 20));//
			jLabel1Topic.setText("监测类型:");//
			jPanelSubscription = new JPanel();
			jPanelSubscription.setLayout(null);
			jPanelSubscription.setBounds(new Rectangle(10, 125, 580, 85));
			jPanelSubscription.setBorder(BorderFactory.createTitledBorder(null, "事件主题", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
			jPanelSubscription.add(getJComboBoxTopic(), null);
			jPanelSubscription.add(getJComboBoxTopic(), null);
			jPanelSubscription.add(jLabelFilterType, null);
			jPanelSubscription.add(getJComboBoxFilterType(), null);
			jPanelSubscription.add(jLabel1Topic, null);
	
			jPanelSubscription.add(getJComboBoxobservedProperty(), null);// 
		  
			 
			
		}
		return jPanelSubscription;
	}
	 
    private JPanel getJPanelFilter(){
    	if(jPanelFilter==null){
    		jLabelFilter = new JLabel();
			jLabelFilter.setBounds(new Rectangle(14, 20, 39, 20));
			jLabelFilter.setText("属性:");
			jLabelSecondFilter = new JLabel();
			jLabelSecondFilter.setBounds(new Rectangle(14, 50, 39, 20));
			jLabelSecondFilter.setText("属性:");
			
			jPanelFilter = new JPanel();
			jPanelFilter.setLayout(null);
			jPanelFilter.setBounds(new Rectangle(10, 220, 580, 85));
			jPanelFilter.setBorder(BorderFactory.createTitledBorder(null, "事件条件", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
			jPanelFilter.add(jLabelFilter, null);
			jPanelFilter.add(jLabelSecondFilter, null);
			jPanelFilter.add(getJComboBoxobservedProperty(), null);// 
			jPanelFilter.add(getJComboBoxThreshold(), null); //添加的"值大于"标签
			jPanelFilter.add(getJComboBoxSecondThreshold(), null); //添加的第二个"值大于"标签
			jPanelFilter.add(getJTextFieldThreshold(), null); //添加的 门限值输入框  
			jPanelFilter.add(getJTextFieldSecondThreshold(), null); //添加的 门限值输入框  
			jPanelFilter.add(getJComboBoxobservedSecondProperty(), null); //添加的第二个属性输入框      
    	}
    	return jPanelFilter;
    }
   
	
	
 
           
	/**
	 * This method initializes jComboBoxService	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	public JComboBox getJComboBoxService() {
		if (jComboBoxService == null) {
			//jComboBoxService = new JComboBox();
			
			String[] serviceURL = new String[]{"http://58.198.82.219:8080/ses-main-1.0-SNAPSHOT/services/SesPortType",
					"http://localhost:8080/ses-main-1.0-SNAPSHOT/services/SesPortType"};
			jComboBoxService = new JComboBox(serviceURL);
			jComboBoxService.setBounds(new Rectangle(80, 20, 480, 20));
			jComboBoxService.setEditable(true);
		}
		return jComboBoxService;
	}

	/**
	 * This method initializes jButtonGetCapabilities	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonGetCapabilities() {
		if (jButtonGetCapabilities == null) {
			jButtonGetCapabilities = new JButton();
			jButtonGetCapabilities.setBounds(new Rectangle(210, 70, 222, 25));
			jButtonGetCapabilities.setText("获取SES观测能力");
			jButtonGetCapabilities.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    controller.actionPerformed_GetCapabilitiesButton();
                }
            });
		}
		return jButtonGetCapabilities;
	}

	/**
	 * This method initializes jTextFieldTopic	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	public JComboBox getJComboBoxTopic() {
		if (jComboBoxTopic == null) {
			String[] topics = new String[]{"measurements", "SensorManagement", "ExpirationInformation"};
			jComboBoxTopic = new JComboBox(topics);
			jComboBoxTopic.setBounds(new Rectangle(80, 24, 160, 20));
		}
		return jComboBoxTopic;
	}

	/**
	 * This method initializes jComboBoxFilterType	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	public JComboBox getJComboBoxFilterType() {
		if (jComboBoxFilterType == null) {
			String[] filters = new String[]{"Filter Level 1", "Filter Level 2", "Filter Level 3"};
			jComboBoxFilterType = new JComboBox(filters);
			jComboBoxFilterType.setBounds(new Rectangle(80, 53, 160, 20));
		}
		return jComboBoxFilterType;
	}
	//下面是添加的门限选项
	public JComboBox getJComboBoxThreshold() {  
		if (jComboBoxThreshold == null) {
			String[] menxianxuanxiang = new String[]{"值大于", "值小于", "值等于"};
			jComboBoxThreshold = new JComboBox(menxianxuanxiang);
			jComboBoxThreshold.setBounds(new Rectangle(270, 20, 100, 20));
		}
		return jComboBoxThreshold;
	}
	public JComboBox getJComboBoxSecondThreshold() {   
		if (jComboBoxSecondThreshold == null) {
			String[] menxianxuanxiang = new String[]{"值大于", "值小于", "值等于"};
			jComboBoxSecondThreshold = new JComboBox(menxianxuanxiang);
			jComboBoxSecondThreshold.setBounds(new Rectangle(270, 50, 100, 20));
		}
		return jComboBoxSecondThreshold;
	}
	public JTextField getJTextFieldThreshold() {  
		if (jTextFieldThreshold == null) {
			jTextFieldThreshold = new JTextField();
			jTextFieldThreshold.setText("");
			jTextFieldThreshold.setBounds(new Rectangle(380, 20, 150, 20));
		}
		return jTextFieldThreshold;
	}
	public JTextField getJTextFieldSecondThreshold() {  
		if (jTextFieldSecondThreshold == null) {
			jTextFieldSecondThreshold = new JTextField();
			jTextFieldSecondThreshold.setText("");
			jTextFieldSecondThreshold.setBounds(new Rectangle(380, 50, 150, 20));
		}
		return jTextFieldSecondThreshold;
	}
	//上面是添加的
	 

	/**
	 * This method initializes jTextAreaFilter	
	 * 	
	 * @return javax.swing.JTextArea	
	 */
	public JComboBox getJComboBoxobservedProperty() {  //修改了
		if (jComboBoxobservedProperty == null) {
			String[] observedProperty = new String[]{"temperature","甘肃电投"};
			jComboBoxobservedProperty=new JComboBox(observedProperty); 
			jComboBoxobservedProperty.setEditable(true);
			jComboBoxobservedProperty.setBounds(new Rectangle(80, 20, 160, 20));
			 
		}
		return jComboBoxobservedProperty;
	}
	//下是自己添加的
	public JComboBox getJComboBoxobservedSecondProperty() {  //第二个属性输入框
		if (jComboBoxobservedSecondProperty == null) {
			String[] observedSencondProperty = new String[]{"fume"};
			jComboBoxobservedSecondProperty=new JComboBox(observedSencondProperty); 
			jComboBoxobservedSecondProperty.setEditable(true);
			jComboBoxobservedSecondProperty.setBounds(new Rectangle(80, 50, 160, 20));
			 
		}
		return jComboBoxobservedSecondProperty;
	}
	//上面是自己添加的
	 
	/**
	 * This method initializes jButtonSubscribe	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonSubscribe(){
		if (jButtonSubscribe == null) {
			jButtonSubscribe = new JButton();
			jButtonSubscribe.setBounds(new Rectangle(14, 20, 120, 25));
			jButtonSubscribe.setText("订阅事件");
			jButtonSubscribe.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    controller.actionPerformed_SubscribeButton();
                }
            });
		}
		return jButtonSubscribe;
	}
	 

	/**
	 * This method initializes jButtonUnsubscribe	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonUnsubscribe() {
		if (jButtonUnsubscribe == null) {
			jButtonUnsubscribe = new JButton();
			jButtonUnsubscribe.setText("取消订阅");
			jButtonUnsubscribe.setBounds(new Rectangle(14, 50, 120, 25));
			jButtonUnsubscribe.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    controller.actionPerformed_UnsubscribeButton();
                }
			});
		}
		return jButtonUnsubscribe;
	}

	/**
	 * This method initializes jTextFieldConsumerRef	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	public JTextField getJTextFieldConsumerRef() {
		if (jTextFieldConsumerRef == null) {
			jTextFieldConsumerRef = new JTextField();
			jTextFieldConsumerRef.setBounds(new Rectangle(80, 44, 480, 20));
			jTextFieldConsumerRef.setText(consumer);
		}
		return jTextFieldConsumerRef;
	}

	/**
	 * This method initializes jPanelUnsubscribe	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelUnsubscribe() {
		if (jPanelUnsubscribe == null) {
			jLabelResource = new JLabel();
			jLabelResource.setBounds(new Rectangle(160, 50, 100, 20));
			jLabelResource.setText("订阅编号：");
			
			jPanelUnsubscribe = new JPanel();
			jPanelUnsubscribe.setLayout(null);
			jPanelUnsubscribe.setBounds(new Rectangle(10, 320, 580, 100));
			jPanelUnsubscribe.setBorder(BorderFactory.createTitledBorder(null, "监测管理", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
			
			jPanelUnsubscribe.add(getJButtonSubscribe(), null); 
			jPanelUnsubscribe.add(getJButtonUnsubscribe(), null);
			jPanelUnsubscribe.add(jLabelResource, null);
			jPanelUnsubscribe.add(getJTextFieldResource(), null);
		}
		return jPanelUnsubscribe;
	}

	/**
	 * This method initializes jTextFieldResource	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	public JTextField getJTextFieldResource() {
		if (jTextFieldResource == null) {
			jTextFieldResource = new JTextField();
			jTextFieldResource.setBounds(new Rectangle(250, 50, 95, 20));
			jTextFieldResource.setText("2");
		}
		return jTextFieldResource;
	}
	public JPanel getSystemPanel(){
		if (jSystempanel==null){
			jCheckFire=new JCheckBox("火灾事件");
			jCheckFire.setBounds(new Rectangle(14, 20, 100, 40));
			jCheckPM10=new JCheckBox("可吸入颗粒物检测");
			jCheckPM10.setBounds(new Rectangle(160, 20, 150, 40));
			jCheckCXHX=new JCheckBox("有机气体异常检测");
			jCheckCXHX.setBounds(new Rectangle(320, 20, 150, 40));
			jCheckCO=new JCheckBox("CO中毒检测");
			jCheckCO.setBounds(new Rectangle(14, 50, 100, 40));
			jCheckGas=new JCheckBox("煤气泄漏事件");
			jCheckGas.setBounds(new Rectangle(160, 50, 150, 40));
			jSystempanel = new JPanel();
			jSystempanel.setLayout(null);
			jSystempanel.setBounds(new Rectangle(10, 220, 580, 100));
			jSystempanel.setBorder(BorderFactory.createTitledBorder(null, "订阅选项", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
			jSystempanel.add(jCheckFire,null);
			jSystempanel.add(jCheckPM10,null);
			jSystempanel.add(jCheckCXHX,null);
			jSystempanel.add(jCheckCO,null);
			jSystempanel.add(jCheckGas,null);
		}
		return jSystempanel;
	}

}
