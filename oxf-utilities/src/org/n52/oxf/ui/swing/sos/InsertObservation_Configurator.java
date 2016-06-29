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
public class InsertObservation_Configurator extends JDialog {

    /**
	 * 
	 */
    private static final long serialVersionUID = -6092795238845572730L;
    private JPanel jContentPane = null;
    private JLabel sensorIdLabel = null;
    private JComboBox sensorIdComboBox = null;
    private JComboBox serviceComboBox = null; 
    private JComboBox SamplingTimeComboBox=null; //����ʱ���
    private JComboBox ObservationPropertyComboBox=null; //�۲����Կ� 
    private JComboBox SamplingPointPositionComboBox=null; //������λ�ÿ�
    private JComboBox ResultComboBox=null; //����� 
    private JComboBox SamplingPointIdComboBox=null; //������ID��  
    private JComboBox TypeComboBox=null; //���Ϳ�
    private JLabel serviceLabel = null;
    private JComboBox versionComboBox = null;
    private JLabel versionLabel = null;
    private JPanel buttonPanel = null;
    private JButton okButton = null;
    private JButton cancelButton = null;

    protected InsertObservation_ConfiguratorController controller;
    private TimePeriodPanel timePP;

    /**
     * This is the default constructor
     */
    public InsertObservation_Configurator(JDialog owner, URL serviceURL, SOSAdapter adapter) {
        super(owner, "ע�ᴫ����");

        initialize(owner);
        controller = new InsertObservation_ConfiguratorController(this, serviceURL, adapter);
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
            gridBagConstraints8.gridy = 10;
            
            GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
            gridBagConstraints5.gridx = 0;
            gridBagConstraints5.gridy = 2;
 
           
            GridBagConstraints gridBagConstraints13 = new GridBagConstraints();   //��������λ��
            gridBagConstraints13.gridx = 0;
            gridBagConstraints13.gridy = 3;   
            GridBagConstraints gridBagConstraints14 = new GridBagConstraints();   //��������λ�ÿ�
            gridBagConstraints14.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints14.gridy = 3;
            gridBagConstraints14.weightx = 1.0;
            gridBagConstraints14.insets = new java.awt.Insets(5, 5, 5, 5);
            gridBagConstraints14.gridx = 1;
            
            GridBagConstraints gridBagConstraints15 = new GridBagConstraints();   //������۲�����
            gridBagConstraints15.gridx = 0;
            gridBagConstraints15.gridy = 4;  
            GridBagConstraints gridBagConstraints16 = new GridBagConstraints();   //������۲����Կ�
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
            
            GridBagConstraints gridBagConstraints19 = new GridBagConstraints();   //����������ǩ
            gridBagConstraints19.gridx = 0;
            gridBagConstraints19.gridy = 8;
            
            GridBagConstraints gridBagConstraints20 = new GridBagConstraints();   //����������
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
            
            GridBagConstraints gridBagConstraints9 = new GridBagConstraints();  //�������
            gridBagConstraints9.gridx = 0;
            gridBagConstraints9.weightx = 10.0D;
            gridBagConstraints9.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints9.gridwidth = 2;
            gridBagConstraints9.gridy = 5;
            
            GridBagConstraints gridBagConstraints10 = new GridBagConstraints();  //�������
            gridBagConstraints10.gridx = 1;
            gridBagConstraints10.weightx = 10.0D;
            gridBagConstraints10.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints10.gridwidth = 2;
            gridBagConstraints10.gridy = 5;
            
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
            
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();  //������ID��
            gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints1.gridy = 0;
            gridBagConstraints1.weightx = 1.0;
            gridBagConstraints1.insets = new java.awt.Insets(5, 5, 5, 5);
            gridBagConstraints1.gridx = 1;     
            
            GridBagConstraints gridBagConstraints = new GridBagConstraints();  //������ID��ǩ
            gridBagConstraints.gridx = 0;
            gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
            gridBagConstraints.gridy = 0;   
            
            sensorIdLabel = new JLabel();
            sensorIdLabel.setText("Sensor ID:");
            versionLabel=new JLabel();
            versionLabel.setText("version:");
            jContentPane = new JPanel();
            jContentPane.setLayout(new GridBagLayout());
            
            jContentPane.add(sensorIdLabel, gridBagConstraints);  //��Ӵ�����ID��ǩ
            jContentPane.add(getSensorIdComboBox(), gridBagConstraints1); //��Ӵ�����ID��
            jContentPane.add(getServiceComboBox(), gridBagConstraints2);
            jContentPane.add(serviceLabel, gridBagConstraints3);
            jContentPane.add(getVersionComboBox(), gridBagConstraints4);
            jContentPane.add(versionLabel, gridBagConstraints5);
            jContentPane.add(new JLabel("����ʱ��"), gridBagConstraints13);     //��ӳ���ʱ���ǩ
            jContentPane.add(getSamplingTimeComboBox(), gridBagConstraints14); //��ӳ���ʱ���
            
            jContentPane.add(new JLabel("�۲�����"), gridBagConstraints15);     //�۲����Ա�ǩ
            jContentPane.add(getObservationPropertyComboBox(), gridBagConstraints16); //�۲����Կ�
            jContentPane.add(new JLabel("������Id"), gridBagConstraints9);     //�۲������ID��ǩ
            jContentPane.add(getSamplingPointIdComboBox(), gridBagConstraints10); //������ID��
           
            jContentPane.add(new JLabel("������λ��"), gridBagConstraints17);     //��ӳ�����λ�ñ�ǩ
            jContentPane.add(getSamplingPointPositionComboBox(), gridBagConstraints18); //��ӳ�����λ�ÿ�
            
            jContentPane.add(new JLabel("���"), gridBagConstraints19);     //��ӵ�λ��ǩ
            jContentPane.add(getResultComboBox(), gridBagConstraints20); //��ӵ�λ�� 
            
            jContentPane.add(new JLabel("����"), gridBagConstraints21);     //������ͱ�ǩ
            jContentPane.add(getTypeComboBox(), gridBagConstraints22); //������Ϳ� 
         
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
    
    
    
    protected JComboBox getSamplingTimeComboBox() {  //����ʱ���
        if (SamplingTimeComboBox == null) {
        	SamplingTimeComboBox = new JComboBox();
        }
        SamplingTimeComboBox.setEditable(true);
        return SamplingTimeComboBox;
    }
    protected JComboBox getObservationPropertyComboBox() {  //�۲����Կ�
        if (ObservationPropertyComboBox == null) {
        	ObservationPropertyComboBox = new JComboBox();
        }
        ObservationPropertyComboBox.setEditable(true);
        return ObservationPropertyComboBox;
    }
    protected JComboBox getSamplingPointPositionComboBox() {  //�۲����Կ�   
        if (SamplingPointPositionComboBox == null) {
        	SamplingPointPositionComboBox = new JComboBox();
        }
        SamplingPointPositionComboBox.setEditable(true);
        return SamplingPointPositionComboBox;       
    }
    protected JComboBox getSamplingPointIdComboBox() {  //������ID��   
        if (SamplingPointIdComboBox == null) {
        	SamplingPointIdComboBox = new JComboBox();
        }
        SamplingPointIdComboBox.setEditable(true);
        return SamplingPointIdComboBox;       
    }
    protected JComboBox getResultComboBox() {  //�����
        if (ResultComboBox == null) {
        	ResultComboBox = new JComboBox();
        }
        ResultComboBox.setEditable(true);
        return ResultComboBox;
    }   
    protected JComboBox getTypeComboBox() {  //���Ϳ�
        if (TypeComboBox == null) {
        	TypeComboBox = new JComboBox();
        }
        TypeComboBox.setEditable(true);
        return TypeComboBox;
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