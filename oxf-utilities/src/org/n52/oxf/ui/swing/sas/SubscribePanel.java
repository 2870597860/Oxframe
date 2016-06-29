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
 
 Created on: 24.01.2008
 *********************************************************************************/

package org.n52.oxf.ui.swing.sas;

import java.awt.GridBagLayout;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;

import org.n52.oxf.serviceAdapters.sas.SASAdapter;
import org.n52.oxf.serviceAdapters.sas.SASRequestBuilder.Criteria;

import java.awt.Font;
import java.awt.Color;
import java.awt.Insets;

/**
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 *
 */
public class SubscribePanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private JLabel sensorIDLabel = null;
    private JTextField sensorIDTextField = null;
    private JLabel phenLabel = null;
    private JLabel filterLabel = null;
    private JComboBox filterComboBox = null;
    private JLabel notifyLabel = null;
    private JLabel filterValueLabel = null;
    private JTextField filterValueTextField = null;
    private JLabel filterValue2Label = null;
    private JTextField filterValue2TextField = null;
    private JLabel emailLabel = null;
    private JLabel smsLabel = null;
    private JTextField emailTextField = null;
    private JTextField smsTextField = null;
    private JButton okButton = null;
    private JTextField phenTextField = null;
    
    
    private SubscribePanelController controller;
    /**
     * This is the default constructor
     */
    public SubscribePanel(SASAdapter adapter, String serviceURL) {
        super();
        initialize();
        
        controller = new SubscribePanelController(this, adapter, serviceURL);
    }

    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize() {
        GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints3.gridy = 1;
        gridBagConstraints3.weightx = 1.0;
        gridBagConstraints3.gridwidth = 5;
        gridBagConstraints3.gridx = 1;
        GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
        gridBagConstraints13.gridx = 0;
        gridBagConstraints13.gridwidth = 6;
        gridBagConstraints13.gridy = 6;
        GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
        gridBagConstraints12.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints12.gridy = 5;
        gridBagConstraints12.weightx = 1.0;
        gridBagConstraints12.gridwidth = 5;
        gridBagConstraints12.gridx = 1;
        GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
        gridBagConstraints11.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints11.gridy = 4;
        gridBagConstraints11.weightx = 1.0;
        gridBagConstraints11.gridwidth = 5;
        gridBagConstraints11.gridx = 1;
        GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
        gridBagConstraints10.gridx = 0;
        gridBagConstraints10.gridy = 5;
        smsLabel = new JLabel();
        smsLabel.setText("or SMS:");
        GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
        gridBagConstraints9.gridx = 0;
        gridBagConstraints9.gridy = 4;
        emailLabel = new JLabel();
        emailLabel.setText("E-Mail:");
        GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
        gridBagConstraints8.gridx = 0;
        gridBagConstraints8.gridy = 3;
        notifyLabel = new JLabel();
        notifyLabel.setText("Notify:");
        GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
        gridBagConstraints7.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints7.gridy = 2;
        gridBagConstraints7.weightx = 1.0;
        gridBagConstraints7.gridx = 3;
        GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
        gridBagConstraints6.gridx = 2;
        gridBagConstraints6.gridy = 2;
        filterValueLabel = new JLabel();
        filterValueLabel.setText("Value:");
        
        
        GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
        gridBagConstraints15.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints15.gridy = 2;
        gridBagConstraints15.weightx = 1.0;
        gridBagConstraints15.gridx = 5;
        GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
        gridBagConstraints14.gridx = 4;
        gridBagConstraints14.gridy = 2;
        filterValue2Label = new JLabel();
        filterValue2Label.setText("Value2:");
        
        GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
        gridBagConstraints5.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints5.gridy = 2;
        gridBagConstraints5.weightx = 1.0;
        gridBagConstraints5.gridx = 1;
        GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
        gridBagConstraints4.gridx = 0;
        gridBagConstraints4.insets = new Insets(0, 0, 0, 0);
        gridBagConstraints4.gridy = 2;
        filterLabel = new JLabel();
        filterLabel.setText("Filter:");
        GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
        gridBagConstraints2.gridx = 0;
        gridBagConstraints2.gridy = 1;
        phenLabel = new JLabel();
        phenLabel.setText("Phenomenon:");
        GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints1.gridy = 0;
        gridBagConstraints1.weightx = 1.0;
        gridBagConstraints1.gridwidth = 5;
        gridBagConstraints1.gridx = 1;
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        sensorIDLabel = new JLabel();
        sensorIDLabel.setText("Sensor ID:");
        this.setSize(300, 200);
        this.setLayout(new GridBagLayout());
        this.setBorder(BorderFactory.createTitledBorder(null, "Subscribe", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
        this.add(sensorIDLabel, gridBagConstraints);
        this.add(getSensorIDTextField(), gridBagConstraints1);
        this.add(phenLabel, gridBagConstraints2);
        this.add(filterLabel, gridBagConstraints4);
        this.add(getFilterComboBox(), gridBagConstraints5);
        this.add(filterValueLabel, gridBagConstraints6);
        this.add(getFilterValueTextField(), gridBagConstraints7);
        
        this.add(filterValue2Label, gridBagConstraints14);
        this.add(getFilterValue2TextField(), gridBagConstraints15);
        
        this.add(notifyLabel, gridBagConstraints8);
        this.add(emailLabel, gridBagConstraints9);
        this.add(smsLabel, gridBagConstraints10);
        this.add(getEmailTextField(), gridBagConstraints11);
        this.add(getSmsTextField(), gridBagConstraints12);
        this.add(getOkButton(), gridBagConstraints13);
        this.add(getPhenTextField(), gridBagConstraints3);
    }

    /**
     * This method initializes sensorIDTextField	
     * 	
     * @return javax.swing.JTextField	
     */
    public JTextField getSensorIDTextField() {
        if (sensorIDTextField == null) {
            sensorIDTextField = new JTextField();
        }
        return sensorIDTextField;
    }

    /**
     * This method initializes filterComboBox	
     * 	
     * @return javax.swing.JComboBox	
     */
    public JComboBox getFilterComboBox() {
        if (filterComboBox == null) {
            filterComboBox = new JComboBox();
            for(String s:Criteria.operationNames){
            	filterComboBox.addItem(s);
            }
        }
        return filterComboBox;
    }

    /**
     * This method initializes filterValueTextField	
     * 	
     * @return javax.swing.JTextField	
     */
    public JTextField getFilterValue2TextField() {
    	if (filterValue2TextField == null) {
    		filterValue2TextField = new JTextField();
    	}
    	return filterValue2TextField;
    }
    /**
     * This method initializes filterValueTextField	
     * 	
     * @return javax.swing.JTextField	
     */
    public JTextField getFilterValueTextField() {
        if (filterValueTextField == null) {
            filterValueTextField = new JTextField();
        }
        return filterValueTextField;
    }

    /**
     * This method initializes emailTextField	
     * 	
     * @return javax.swing.JTextField	
     */
    public JTextField getEmailTextField() {
        if (emailTextField == null) {
            emailTextField = new JTextField();
        }
        return emailTextField;
    }

    /**
     * This method initializes smsTextField	
     * 	
     * @return javax.swing.JTextField	
     */
    public JTextField getSmsTextField() {
        if (smsTextField == null) {
            smsTextField = new JTextField();
        }
        return smsTextField;
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
                    controller.actionPerformed_SubscribeButton();
                }
            });
        }
        return okButton;
    }

    /**
     * This method initializes phenTextField	
     * 	
     * @return javax.swing.JTextField	
     */
    public JTextField getPhenTextField() {
        if (phenTextField == null) {
            phenTextField = new JTextField();
        }
        return phenTextField;
    }

}