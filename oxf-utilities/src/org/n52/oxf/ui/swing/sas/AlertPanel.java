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
import javax.swing.JButton;
import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;

import org.n52.oxf.serviceAdapters.sas.SASAdapter;

import java.awt.Font;
import java.awt.Color;

/**
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 *
 */
public class AlertPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private JLabel sensorIDLabel = null;
    private JTextField sensorIDTextField = null;
    private JLabel timeLabel = null;
    private JTextField timeTextField = null;
    private JButton nowButton = null;
    private JLabel dataLabel = null;
    private JTextField dataTextField = null;
    private JPanel buttonPanel = null;
    private JButton publishButton = null;
    private JButton quitButton = null;
    
    private AlertPanelController controller;
    
    /**
     * This is the default constructor
     */
    public AlertPanel(SASAdapter adapter, String serviceURL) {
        super();
        initialize();
        controller = new AlertPanelController(this,adapter,serviceURL);
    }

    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize() {
        GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
        gridBagConstraints7.gridx = 0;
        gridBagConstraints7.gridwidth = 3;
        gridBagConstraints7.gridy = 3;
        GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
        gridBagConstraints6.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints6.gridy = 2;
        gridBagConstraints6.weightx = 1.0;
        gridBagConstraints6.gridwidth = 2;
        gridBagConstraints6.gridx = 1;
        GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
        gridBagConstraints5.gridx = 0;
        gridBagConstraints5.gridy = 2;
        dataLabel = new JLabel();
        dataLabel.setText("Data:");
        GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
        gridBagConstraints4.gridx = 2;
        gridBagConstraints4.gridy = 1;
        GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints3.gridy = 1;
        gridBagConstraints3.weightx = 1.0;
        gridBagConstraints3.gridx = 1;
        GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
        gridBagConstraints2.gridx = 0;
        gridBagConstraints2.gridy = 1;
        timeLabel = new JLabel();
        timeLabel.setText("Time:");
        GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints1.gridy = 0;
        gridBagConstraints1.weightx = 1.0;
        gridBagConstraints1.gridwidth = 2;
        gridBagConstraints1.gridx = 1;
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        sensorIDLabel = new JLabel();
        sensorIDLabel.setText("Sensor ID:");
        this.setSize(300, 200);
        this.setLayout(new GridBagLayout());
        this.setBorder(BorderFactory.createTitledBorder(null, "Alert", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
        this.add(sensorIDLabel, gridBagConstraints);
        this.add(getSensorIDTextField(), gridBagConstraints1);
        this.add(timeLabel, gridBagConstraints2);
        this.add(getTimeTextField(), gridBagConstraints3);
        this.add(getNowButton(), gridBagConstraints4);
        this.add(dataLabel, gridBagConstraints5);
        this.add(getDataTextField(), gridBagConstraints6);
        this.add(getButtonPanel(), gridBagConstraints7);
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
     * This method initializes timeTextField	
     * 	
     * @return javax.swing.JTextField	
     */
    public JTextField getTimeTextField() {
        if (timeTextField == null) {
            timeTextField = new JTextField();
        }
        return timeTextField;
    }

    /**
     * This method initializes nowButton	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getNowButton() {
        if (nowButton == null) {
            nowButton = new JButton();
            nowButton.setText("Now");
            nowButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    controller.actionPerformed_NowButton();
                }
            });
        }
        return nowButton;
    }

    /**
     * This method initializes dataTextField	
     * 	
     * @return javax.swing.JTextField	
     */
    public JTextField getDataTextField() {
        if (dataTextField == null) {
            dataTextField = new JTextField();
        }
        return dataTextField;
    }

    /**
     * This method initializes buttonPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getButtonPanel() {
        if (buttonPanel == null) {
            GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
            gridBagConstraints8.fill = GridBagConstraints.NONE;
            buttonPanel = new JPanel();
            buttonPanel.setLayout(new GridBagLayout());
            buttonPanel.add(getPublishButton(), new GridBagConstraints());
            buttonPanel.add(getQuitButton(), gridBagConstraints8);
        }
        return buttonPanel;
    }

    /**
     * This method initializes publishButton	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getPublishButton() {
        if (publishButton == null) {
            publishButton = new JButton();
            publishButton.setText("Publish");
            publishButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    controller.actionPerformed_PublischButton();
                }
            });
        }
        return publishButton;
    }

    /**
     * This method initializes quitButton	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getQuitButton() {
        if (quitButton == null) {
            quitButton = new JButton();
            quitButton.setText("Quit");
        }
        return quitButton;
    }

}