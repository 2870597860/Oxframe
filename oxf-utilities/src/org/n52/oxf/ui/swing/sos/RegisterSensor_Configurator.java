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
 
 Created on: 12.03.2006
 *********************************************************************************/

package org.n52.oxf.ui.swing.sos;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.n52.oxf.serviceAdapters.sos.SOSAdapter;
import org.n52.oxf.serviceAdapters.sosMobile.DescribeSensorTimePair;
import org.n52.oxf.ui.swing.TimePeriodPanel;

/**
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public class RegisterSensor_Configurator extends JDialog {

    /**
	 * 
	 */
    private static final long serialVersionUID = -6092795238845572730L;
    private JPanel jContentPane = null;
    private JLabel sensorIdLabel = null;
    private JComboBox sensorIdComboBox = null;
    private JComboBox serviceComboBox = null;
    private JComboBox LatitudeComboBox=null; //γ�ȿ�
    private JComboBox LongitudeComboBox=null; //���ȿ�
    private JComboBox PositionComboBox=null; //λ�ÿ�
    private JComboBox FixComboBox=null; //�Ƿ�Ϊ�̶��Ŀ� 
    private JComboBox ObservedPropertyComboBox=null; //�۲����Կ�
    private JComboBox UomComboBox=null; //��λ��
    private JPanel LatitudePanel=null; //γ�����
    private JPanel LongitudePanel=null; //�������
    private JComboBox LatitudeUomComboBox=null;//γ�ȵ�λ�� 
    private JComboBox TypeComboBox=null;//���Ϳ�
    private JComboBox IdentificationComboBox=null; //identification��
    private JComboBox StatusComboBox=null;//״̬��
    private JComboBox LongitudeUomComboBox=null;
    private JLabel serviceLabel = null;
    private JComboBox versionComboBox = null;
    private JLabel versionLabel = null;
    private JPanel buttonPanel = null;
    private JButton okButton = null;
    private JButton cancelButton = null;

    protected RegisterSensor_ConfiguratorController controller;
    private TimePeriodPanel timePP;

    /**
     * This is the default constructor
     */
    public RegisterSensor_Configurator(JDialog owner, URL serviceURL, SOSAdapter adapter) {
        super(owner, "ע�ᴫ����");

        initialize(owner);
        controller = new RegisterSensor_ConfiguratorController(this, serviceURL, adapter);
    }
    
    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize(JDialog owner) {
        this.setSize(420, 180);
        this.setLocation(owner.getLocation());
        this.setPreferredSize(new java.awt.Dimension(420, 180));
        this.setMinimumSize(new java.awt.Dimension(300, 180));
        this.setContentPane(getJContentPane());
    }

    /**
     * This method initializes jContentPane
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
            gridBagConstraints8.gridx = 0;
            gridBagConstraints8.weightx = 10.0D;
            gridBagConstraints8.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints8.gridwidth = 2;
            gridBagConstraints8.gridy = 12;
            
            GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
            gridBagConstraints5.gridx = 0;
            gridBagConstraints5.gridy = 2;
 
            GridBagConstraints gridBagConstraints25 = new GridBagConstraints();   //״̬�Ƿ����		
            gridBagConstraints25.gridx = 0;
            gridBagConstraints25.gridy = 11;   
            GridBagConstraints gridBagConstraints26 = new GridBagConstraints();   //״̬�Ƿ����
            gridBagConstraints26.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints26.gridy = 11;
            gridBagConstraints26.weightx = 1.0;
            gridBagConstraints26.insets = new java.awt.Insets(5, 5, 5, 5);
            gridBagConstraints26.gridx = 1; 
            
            GridBagConstraints gridBagConstraints23 = new GridBagConstraints();   //identification		
            gridBagConstraints23.gridx = 0;
            gridBagConstraints23.gridy = 10;   
            GridBagConstraints gridBagConstraints24 = new GridBagConstraints();   //identification
            gridBagConstraints24.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints24.gridy = 10;
            gridBagConstraints24.weightx = 1.0;
            gridBagConstraints24.insets = new java.awt.Insets(5, 5, 5, 5);
            gridBagConstraints24.gridx = 1;               
            
            GridBagConstraints gridBagConstraints13 = new GridBagConstraints();   //��������λ��
            gridBagConstraints13.gridx = 0;
            gridBagConstraints13.gridy = 3;   
            GridBagConstraints gridBagConstraints14 = new GridBagConstraints();   //��������λ�ÿ�
            gridBagConstraints14.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints14.gridy = 3;
            gridBagConstraints14.weightx = 1.0;
            gridBagConstraints14.insets = new java.awt.Insets(5, 5, 5, 5);
            gridBagConstraints14.gridx = 1;
            
            GridBagConstraints gridBagConstraints15 = new GridBagConstraints();   //�������Ƿ�Ϊ�̶���
            gridBagConstraints15.gridx = 0;
            gridBagConstraints15.gridy = 4;  
            GridBagConstraints gridBagConstraints16 = new GridBagConstraints();   //�������Ƿ�Ϊ�̶���
            gridBagConstraints16.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints16.gridy = 4;
            gridBagConstraints16.weightx = 1.0;
            gridBagConstraints16.insets = new java.awt.Insets(5, 5, 5, 5);
            gridBagConstraints16.gridx = 1;
            
          
            GridBagConstraints gridBagConstraints17 = new GridBagConstraints();   //�۲����Ա�ǩ
            gridBagConstraints17.gridx = 0;
            gridBagConstraints17.gridy = 7;
            
            GridBagConstraints gridBagConstraints18 = new GridBagConstraints();   //�۲����Կ�
            gridBagConstraints18.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints18.gridy = 7;
            gridBagConstraints18.weightx = 1.0;
            gridBagConstraints18.insets = new java.awt.Insets(5, 5, 5, 5);
            gridBagConstraints18.gridx = 1;
            
            GridBagConstraints gridBagConstraints19 = new GridBagConstraints();   //��λ��ǩ
            gridBagConstraints19.gridx = 0;
            gridBagConstraints19.gridy = 8;
            
            GridBagConstraints gridBagConstraints20 = new GridBagConstraints();   //��λ��
            gridBagConstraints20.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints20.gridy = 8;
            gridBagConstraints20.weightx = 1.0;
            gridBagConstraints20.insets = new java.awt.Insets(5, 5, 5, 5);
            gridBagConstraints20.gridx = 1;
            
            GridBagConstraints gridBagConstraints21 = new GridBagConstraints();   //���ͱ�ǩ
            gridBagConstraints21.gridx = 0;
            gridBagConstraints21.gridy = 9;
            
            GridBagConstraints gridBagConstraints22 = new GridBagConstraints();   //���Ϳ�
            gridBagConstraints22.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints22.gridy = 9;
            gridBagConstraints22.weightx = 1.0;
            gridBagConstraints22.insets = new java.awt.Insets(5, 5, 5, 5);
            gridBagConstraints22.gridx = 1;
          
            GridBagConstraints gridBagConstraints9 = new GridBagConstraints();  //γ�����
            gridBagConstraints9.gridx = 0;
            gridBagConstraints9.weightx = 10.0D;
            gridBagConstraints9.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints9.gridwidth = 2;
            gridBagConstraints9.gridy = 5;
            
            GridBagConstraints gridBagConstraints10 = new GridBagConstraints();  //�������
            gridBagConstraints10.gridx = 0;
            gridBagConstraints10.weightx = 10.0D;
            gridBagConstraints10.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints10.gridwidth = 2;
            gridBagConstraints10.gridy = 6;
            
            GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
            gridBagConstraints4.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints4.gridy = 2;
            gridBagConstraints4.weightx = 1.0;
            gridBagConstraints4.insets = new java.awt.Insets(5, 5, 5, 5);
            gridBagConstraints4.gridx = 1;   
            
            GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
            gridBagConstraints3.gridx = 0;
            gridBagConstraints3.gridy = 1;   
            
            serviceLabel = new JLabel();
            serviceLabel.setText("Service:");
            GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
            gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints2.gridy = 1;
            gridBagConstraints2.weightx = 1.0;
            gridBagConstraints2.insets = new java.awt.Insets(5, 5, 5, 5);
            gridBagConstraints2.gridx = 1;            
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints1.gridy = 0;
            gridBagConstraints1.weightx = 1.0;
            gridBagConstraints1.insets = new java.awt.Insets(5, 5, 5, 5);
            gridBagConstraints1.gridx = 1;            
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
            gridBagConstraints.gridy = 0;            
            sensorIdLabel = new JLabel();
            sensorIdLabel.setText("Sensor ID:");
            versionLabel=new JLabel();
            versionLabel.setText("version:");
            jContentPane = new JPanel();
            jContentPane.setLayout(new GridBagLayout());
            jContentPane.add(sensorIdLabel, gridBagConstraints);  
            jContentPane.add(getSensorIdComboBox(), gridBagConstraints1);
            jContentPane.add(getServiceComboBox(), gridBagConstraints2);
            jContentPane.add(serviceLabel, gridBagConstraints3);
            jContentPane.add(getVersionComboBox(), gridBagConstraints4);
            jContentPane.add(versionLabel, gridBagConstraints5);
            jContentPane.add(new JLabel("��������λ��"), gridBagConstraints13);     //���λ�����Ʊ�ǩ
            jContentPane.add(getPositionComboBox(), gridBagConstraints14); //���λ�ÿ�
            jContentPane.add(new JLabel("fixed"), gridBagConstraints15);     //fix��ǩ
            jContentPane.add(getFixComboBox(), gridBagConstraints16); //fix��
       
            jContentPane.add(getLatitudePanel(), gridBagConstraints9); //���γ�����
            jContentPane.add(getLongitudePanel(), gridBagConstraints10); //��Ӿ������
           
            jContentPane.add(new JLabel("�۲�����"), gridBagConstraints17);     //��ӹ۲����Ա�ǩ
            jContentPane.add(getObservedPropertyComboBox(), gridBagConstraints18); //��ӹ۲�����
            jContentPane.add(new JLabel("��λ"), gridBagConstraints19);     //��ӵ�λ��ǩ
            jContentPane.add(getUomComboBox(), gridBagConstraints20); //��ӵ�λ�� 
            jContentPane.add(new JLabel("����"), gridBagConstraints21);     //��ӵ�λ��ǩ
            jContentPane.add(getTypeComboBox(), gridBagConstraints22); //������Ϳ�  
            jContentPane.add(new JLabel("identification"), gridBagConstraints23);     //���identification��ǩ
            jContentPane.add(getIdentificationComboBox(), gridBagConstraints24); //���identification���Ϳ�  
            jContentPane.add(new JLabel("״̬"), gridBagConstraints25);     //���״̬��ǩ
            jContentPane.add(getStatusComboBox(), gridBagConstraints26); //���״̬��  
            jContentPane.add(getButtonPanel(), gridBagConstraints8);
        }
        return jContentPane;
    }

    /**
     * This method initializes sensorIdComboBox
     * 
     * @return javax.swing.JComboBox
     */
    protected JComboBox getSensorIdComboBox() {
        if (sensorIdComboBox == null) {
            sensorIdComboBox = new JComboBox();
        }
        sensorIdComboBox.setEditable(true);
        return sensorIdComboBox;
    }

    /**
     * This method initializes serviceComboBox
     * 
     * @return javax.swing.JComboBox
     */
    protected JComboBox getServiceComboBox() {
        if (serviceComboBox == null) {
            serviceComboBox = new JComboBox();
        }
        serviceComboBox.setEditable(true);
        return serviceComboBox;
    }

    /**
     * This method initializes versionComboBox
     * 
     * @return javax.swing.JComboBox
     */
    protected JComboBox getVersionComboBox() {
        if (versionComboBox == null) {
            versionComboBox = new JComboBox();
        }
        versionComboBox.setEditable(true);
        return versionComboBox;
    }
    //��������ӵ�
    
    
   
    protected JComboBox getLongitudeComboBox() {          //���ȿ�
        if (LongitudeComboBox == null) {
        	LongitudeComboBox = new JComboBox();
        }
        LongitudeComboBox.setEditable(true);
        return LongitudeComboBox;
    }
    protected JComboBox getLatitudeComboBox() {           //γ�ȿ�
        if (LatitudeComboBox == null) {
        	LatitudeComboBox = new JComboBox();
        }
        LatitudeComboBox.setEditable(true);
        return LatitudeComboBox;
    }
    protected JComboBox getPositionComboBox() {  //λ�ÿ�
        if (PositionComboBox == null) {
        	PositionComboBox = new JComboBox();
        }
        PositionComboBox.setEditable(true);
        return PositionComboBox;
    }
    protected JComboBox getFixComboBox() {  //�Ƿ�̶���
        if (FixComboBox == null) {
        	FixComboBox = new JComboBox();
        }
        FixComboBox.setEditable(true);
        return FixComboBox;
    }
    protected JComboBox getObservedPropertyComboBox() {  //�۲����Կ�   
        if (ObservedPropertyComboBox == null) {
        	ObservedPropertyComboBox = new JComboBox();
        }
        ObservedPropertyComboBox.setEditable(true);
        return ObservedPropertyComboBox;
    }
    protected JComboBox getUomComboBox() {  //��λ��
        if (UomComboBox == null) {
        	UomComboBox = new JComboBox();
        }
        UomComboBox.setEditable(true);
        return UomComboBox;
    }
    protected JComboBox getLatitudeUomComboBox() {  //γ�ȵ�λ��  getLongitudeUomComboBox
        if (LatitudeUomComboBox == null) {
        	LatitudeUomComboBox = new JComboBox();
        }
        LatitudeUomComboBox.setEditable(true);
        return LatitudeUomComboBox;
    }
    protected JComboBox getLongitudeUomComboBox() {  //���ȵ�λ��     
        if (LongitudeUomComboBox == null) {
        	LongitudeUomComboBox = new JComboBox();
        }
        LongitudeUomComboBox.setEditable(true);
        return LongitudeUomComboBox;
    }
    protected JComboBox getTypeComboBox() {  //���Ϳ�
        if (TypeComboBox == null) {
        	TypeComboBox = new JComboBox();
        }
        TypeComboBox.setEditable(true);
        return TypeComboBox;
    }
    protected JComboBox getIdentificationComboBox() {  //identification��  getStatusComboBox
        if (IdentificationComboBox == null) {
        	IdentificationComboBox = new JComboBox();
        }
        IdentificationComboBox.setEditable(true);
        return IdentificationComboBox;
    }
    protected JComboBox getStatusComboBox() {  //identification��  getStatusComboBox
        if (StatusComboBox == null) {
        	StatusComboBox = new JComboBox();
        }
        StatusComboBox.setEditable(true);
        return StatusComboBox;
    }
    
    //��������ӵ�

    /**
     * 
     */
    protected Collection<DescribeSensorTimePair> getTemporalParameters() {
        ArrayList<DescribeSensorTimePair> list = new ArrayList<DescribeSensorTimePair>();

        DescribeSensorTimePair onlySimple = new DescribeSensorTimePair(this.timePP.getChosenTime(),
                                                                       DescribeSensorTimePair.SOSmobileDescSensorTimeOps.EQUALS);
        list.add(onlySimple);

        return list;
    }

    /**
     * This method initializes buttonPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getButtonPanel() {
        if (buttonPanel == null) {
            GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
            gridBagConstraints8.insets = new java.awt.Insets(4, 4, 4, 4);
            gridBagConstraints8.gridy = 0;
            gridBagConstraints8.gridx = 1;
            GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
            gridBagConstraints7.insets = new java.awt.Insets(4, 4, 4, 4);
            gridBagConstraints7.gridy = 0;
            gridBagConstraints7.gridx = 0;
            buttonPanel = new JPanel();
            buttonPanel.setLayout(new GridBagLayout());
            buttonPanel.add(getOkButton(), gridBagConstraints7);
            buttonPanel.add(getCancelButton(), gridBagConstraints8);
        }
        return buttonPanel;
    }
    //��������ӵ�
    private JPanel getLatitudePanel() {
        if (LatitudePanel == null) {
            GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
            gridBagConstraints8.insets = new java.awt.Insets(4, 4, 4, 4);
            gridBagConstraints8.gridy = 0;
            gridBagConstraints8.gridx = 1;
            GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
            gridBagConstraints7.insets = new java.awt.Insets(4, 4, 4, 4);
            gridBagConstraints7.gridy = 0;
            gridBagConstraints7.gridx = 0;
            GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
            gridBagConstraints9.insets = new java.awt.Insets(4, 4, 4, 4);
            gridBagConstraints9.gridy = 0;
            gridBagConstraints9.gridx = 2;
            GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
            gridBagConstraints10.insets = new java.awt.Insets(4, 4, 4, 4);
            gridBagConstraints10.gridy = 0;
            gridBagConstraints10.gridx = 3;
            LatitudePanel = new JPanel();
            LatitudePanel.setLayout(new GridBagLayout());
            LatitudePanel.add(new JLabel("γ�ȣ�"), gridBagConstraints7);
            LatitudePanel.add(getLatitudeComboBox(), gridBagConstraints8);
            LatitudePanel.add(new JLabel("��λ��"), gridBagConstraints9);
            LatitudePanel.add(getLatitudeUomComboBox(), gridBagConstraints10);
        }
        return LatitudePanel;
    }
   
    
    private JPanel getLongitudePanel() {
        if (LongitudePanel == null) {
            GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
            gridBagConstraints8.insets = new java.awt.Insets(4, 4, 4, 4);
            gridBagConstraints8.gridy = 0;
            gridBagConstraints8.gridx = 1;
            GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
            gridBagConstraints7.insets = new java.awt.Insets(4, 4, 4, 4);
            gridBagConstraints7.gridy = 0;
            gridBagConstraints7.gridx = 0;
            GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
            gridBagConstraints9.insets = new java.awt.Insets(4, 4, 4, 4);
            gridBagConstraints9.gridy = 0;
            gridBagConstraints9.gridx = 2;
            GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
            gridBagConstraints10.insets = new java.awt.Insets(4, 4, 4, 4);
            gridBagConstraints10.gridy = 0;
            gridBagConstraints10.gridx = 3;
            LongitudePanel = new JPanel();
            LongitudePanel.setLayout(new GridBagLayout());
            LongitudePanel.add(new JLabel("���ȣ�"), gridBagConstraints7);
            LongitudePanel.add(getLongitudeComboBox(), gridBagConstraints8);
            LongitudePanel.add(new JLabel("��λ��"), gridBagConstraints9);
            LongitudePanel.add(getLongitudeUomComboBox(), gridBagConstraints10);
        }
        return LongitudePanel;
    }
    //��������ӵ�

    /**
     * This method initializes okButton
     * 
     * @return javax.swing.JButton
     */
    private JButton getOkButton() {
        if (okButton == null) {
            okButton = new JButton();
            okButton.setText("ok");
            okButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    controller.actionPerformed_okButton();
                }
            });
        }
        return okButton;
    }

    /**
     * This method initializes cancelButton
     * 
     * @return javax.swing.JButton
     */
    private JButton getCancelButton() {
        if (cancelButton == null) {
            cancelButton = new JButton();
            cancelButton.setText("cancel");
            cancelButton.setMnemonic(java.awt.event.KeyEvent.VK_ESCAPE);
            cancelButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    controller.actionPerformed_cancelButton();
                }
            });
        }
        return cancelButton;
    }

} // @jve:decl-index=0:visual-constraint="10,10"