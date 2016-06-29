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


package org.n52.oxf.ui.swing.sosMobile;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import org.n52.oxf.serviceAdapters.sos.SOSAdapter;
import org.n52.oxf.ui.swing.MapCanvas;
import org.n52.oxf.ui.swing.TimePeriodPanel;
import org.n52.oxf.ui.swing.sosMobile.ShowSensorPosition_ConfiguratorController.State;
import org.n52.oxf.ui.swing.tree.ContentTree;
import org.n52.oxf.ui.swing.wms.ConnectWMSDialogController.capabilitiesState;
import org.n52.oxf.valueDomains.time.TimePeriod;

/**
 * 
 * Dialog for showing a sensor position at a time selected interatively from
 * existing points in time or a real time visualisation.
 * 
 * @author <a href="mailto:daniel.nuest@uni-muenster.de">Daniel Nüst</a>
 * 
 */
public class ShowSensorPosition_Configurator extends JDialog {

    /**
	 * 
	 */
    private static final long serialVersionUID = -4678135003161900582L;
    ShowSensorPosition_ConfiguratorController controller;
    private JButton timeInstantAddToMapButton;
    private JPanel buttonPanelTimeInstant;
    private JComboBox sensorsComboBoxTimeInstant;
    private JPanel jInstantOfTimePanel;
    private JComboBox timesComboBoxTimeInstant;
    private JTabbedPane jTabbedPane;
    private JPanel jRealTimePanel;
    private JPanel jPeriodOfTimePanel;
    private JPanel buttonPanelRealTime;
    private JComboBox sensorsComboBoxRealTime;
    private JButton timeInstantLoadTimesButton;
    private JButton realTimeAddToMapButton;
    private JPanel statusPanel;
    private JProgressBar progressBar;
    private int progressLength;
    private TimePeriodPanel timePP;
    private JButton timeInstantXmlButton;
    private JButton realTimeXmlButton;
    private JLabel statusText;
    private JComboBox intervallComboBoxRealTime;

    private int[] intervallComboBoxDefaultItems = {5, 10, 30, 60, 120, 600};
    private JPanel buttonPanelTimePeriod;
    private JButton timePeriodAddToMapButton;
    private JComboBox timesComboBoxTimePeriodStart;
    private JComboBox timesComboBoxTimePeriodEnd;
    private JComboBox sensorsComboBoxTimePeriod;
    private JButton timePeriodLoadTimesButton;

    public static final int TAB_TIME_INSTANT = 1;
    public static final int TAB_TIME_PERIOD = 2;
    public static final int TAB_REAL_TIME = 3;

    /**
     * @param tree
     * @param map
     * @param adapter
     * @param url
     * @param view
     * 
     */
    public ShowSensorPosition_Configurator(JDialog owner,
                                           URL serviceURL,
                                           SOSAdapter adapter,
                                           MapCanvas map,
                                           ContentTree tree) {
        super(owner, "Show Sensor Positions.");
        this.controller = new ShowSensorPosition_ConfiguratorController(this, serviceURL, adapter, map, tree);
        initialize(owner);
    }

    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize(JDialog owner) {
        this.setSize(500, 260);
        this.setMinimumSize(this.getSize());
        this.setLocation(owner.getLocation());
        this.setLayout(new BorderLayout(5, 5));
        this.add(getJTabbedPane(), BorderLayout.CENTER); // setContentPane(getJTabbedPane());
        this.add(getStatusPanel(), BorderLayout.SOUTH);

        addWindowAdapter();
    }

    /**
     * This method initializes jContentPane
     * 
     * @return javax.swing.JPanel
     */
    public JTabbedPane getJTabbedPane() {
        if (this.jTabbedPane == null) {

            jTabbedPane = new JTabbedPane();

            jTabbedPane.addTab("Instant of Time",
                               null,
                               getTimeInstantPanel(),
                               "Select one sensor, choose from the existing times and show the position.");
            jTabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
            
            jTabbedPane.addTab("Real Time", null, getRealTimePanel(), "Select one sensor and refresh rate");
            jTabbedPane.setMnemonicAt(1, KeyEvent.VK_2);
            

            /*
            jTabbedPane.addTab("Period of Time",
                               null,
                               getTimePeriodPanel(),
                               "Select one sensor, choose from the existing times an intervall and show the positions.");
            jTabbedPane.setMnemonicAt(3, KeyEvent.VK_3);
            */

        }
        return this.jTabbedPane;

    }

    private JPanel getStatusPanel() {
        if (this.statusPanel == null) {
            this.statusPanel = new JPanel();

            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.gridx = 0;
            gridBagConstraints1.fill = GridBagConstraints.NONE;
            gridBagConstraints1.anchor = GridBagConstraints.WEST;
            gridBagConstraints1.gridy = 0;
            gridBagConstraints1.insets = new java.awt.Insets(5, 10, 5, 10);
            gridBagConstraints1.weightx = 0.0D;
            JLabel statusLabel = new JLabel();
            statusLabel.setText("Status:");
            statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
            statusPanel.add(statusLabel, gridBagConstraints1);

            GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
            gridBagConstraints2.gridx = 1;
            gridBagConstraints2.fill = GridBagConstraints.NONE;
            gridBagConstraints2.anchor = GridBagConstraints.WEST;
            gridBagConstraints2.gridy = 0;
            gridBagConstraints2.insets = new java.awt.Insets(5, 10, 5, 10);
            gridBagConstraints2.weightx = 0.0D;
            this.statusText = new JLabel();
            this.statusText.setText(capabilitiesState.NO_ACTIVE_REQUEST.toString());
            statusPanel.add(this.statusText, gridBagConstraints2);

            GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
            gridBagConstraints4.gridx = 2;
            gridBagConstraints4.fill = GridBagConstraints.NONE;
            gridBagConstraints4.anchor = GridBagConstraints.EAST;
            gridBagConstraints4.gridy = 0;
            gridBagConstraints4.insets = new java.awt.Insets(5, 10, 5, 10);
            gridBagConstraints4.weightx = 100.0D;

            this.progressBar = new JProgressBar(SwingConstants.HORIZONTAL, 300);
            progressBar.setValue(0);
            statusPanel.add(progressBar, gridBagConstraints4);
        }
        return this.statusPanel;
    }

    /**
     * 
     * @return
     */
    private JPanel getTimeInstantPanel() {
        if (jInstantOfTimePanel == null) {
            this.jInstantOfTimePanel = new JPanel();
            this.jInstantOfTimePanel.setLayout(new GridBagLayout());

            GridBagConstraints gridBagConstraints01 = new GridBagConstraints();
            gridBagConstraints01.gridx = 0;
            gridBagConstraints01.insets = new java.awt.Insets(4, 4, 4, 4);
            gridBagConstraints01.gridy = 1;
            gridBagConstraints01.anchor = GridBagConstraints.EAST;
            this.jInstantOfTimePanel.add(new JLabel("Sensor:"), gridBagConstraints01);

            GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
            gridBagConstraints11.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints11.gridy = 1;
            gridBagConstraints11.weightx = 1.0;
            gridBagConstraints11.insets = new java.awt.Insets(4, 4, 4, 4);
            gridBagConstraints11.gridx = 1;
            this.jInstantOfTimePanel.add(getTimeInstantSensorsComboBox(), gridBagConstraints11);

            GridBagConstraints gridBagConstraints02 = new GridBagConstraints();
            gridBagConstraints02.gridx = 0;
            gridBagConstraints02.insets = new java.awt.Insets(4, 4, 4, 4);
            gridBagConstraints02.gridy = 2;
            this.jInstantOfTimePanel.add(getTimeInstantLoadTimesButton(), gridBagConstraints02);

            GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
            gridBagConstraints12.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints12.gridx = 1;
            gridBagConstraints12.weightx = 1.0;
            gridBagConstraints12.insets = new java.awt.Insets(4, 4, 4, 4);
            gridBagConstraints12.gridy = 2;
            this.jInstantOfTimePanel.add(getTimeInstantTimesComboBox(), gridBagConstraints12);

            GridBagConstraints gridBagConstraints03 = new GridBagConstraints();
            gridBagConstraints03.gridwidth = 2;
            gridBagConstraints03.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints03.weightx = 10.0D;
            gridBagConstraints03.weighty = 0.0D;
            gridBagConstraints03.gridy = 3;
            this.jInstantOfTimePanel.add(getTimeInstantButtonPanel(), gridBagConstraints03);
        }
        return jInstantOfTimePanel;
    }

    /**
     * 
     * @return
     */
    @SuppressWarnings("unused")
    private JPanel getTimePeriodPanel() {
        if (this.jPeriodOfTimePanel == null) {
            this.jPeriodOfTimePanel = new JPanel();
            this.jPeriodOfTimePanel.setLayout(new GridBagLayout());
            
            GridBagConstraints gridBagConstraints00 = new GridBagConstraints();
            gridBagConstraints00.insets = new java.awt.Insets(4, 4, 4, 4);
            gridBagConstraints00.gridx = 0;
            gridBagConstraints00.gridy = 0;
            gridBagConstraints00.anchor = GridBagConstraints.EAST;
            this.jPeriodOfTimePanel.add(new JLabel("Sensor:"), gridBagConstraints00);

            GridBagConstraints gridBagConstraints01 = new GridBagConstraints();
            gridBagConstraints01.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints01.gridx = 1;
            gridBagConstraints01.gridy = 0;
            gridBagConstraints01.weightx = 1.0;
            gridBagConstraints01.insets = new java.awt.Insets(4, 4, 4, 4);
            this.jPeriodOfTimePanel.add(getTimePeriodSensorsComboBox(), gridBagConstraints01);
            
            GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
            gridBagConstraints10.gridwidth = 2;
            gridBagConstraints10.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints10.gridy = 1;
            gridBagConstraints10.weightx = 1.0D;
            gridBagConstraints10.weighty = 0.0D;
            gridBagConstraints10.insets = new java.awt.Insets(4, 4, 4, 4);
            this.jPeriodOfTimePanel.add(getTimePeriodLoadTimesButton(), gridBagConstraints10);

            GridBagConstraints gridBagConstraints02 = new GridBagConstraints();
            gridBagConstraints02.gridx = 0;
            gridBagConstraints02.gridy = 2;
            gridBagConstraints02.insets = new Insets(4, 4, 4, 4);
            gridBagConstraints02.anchor = GridBagConstraints.EAST;
            this.jPeriodOfTimePanel.add(new JLabel("Start:"), gridBagConstraints02);
            
            GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
            gridBagConstraints12.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints12.gridx = 1;
            gridBagConstraints12.gridy = 2;
            gridBagConstraints12.weightx = 1.0;
            gridBagConstraints12.insets = new Insets(4, 4, 4, 4);
            this.jPeriodOfTimePanel.add(getTimePeriodStartComboBox(), gridBagConstraints12);
            
            GridBagConstraints gridBagConstraints03 = new GridBagConstraints();
            gridBagConstraints03.gridx = 0;
            gridBagConstraints03.gridy = 3;
            gridBagConstraints03.insets = new Insets(4, 4, 4, 4);
            gridBagConstraints03.anchor = GridBagConstraints.EAST;
            this.jPeriodOfTimePanel.add(new JLabel("End:"), gridBagConstraints03);

            GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
            gridBagConstraints14.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints14.gridx = 1;
            gridBagConstraints14.weightx = 1.0;
            gridBagConstraints14.insets = new Insets(4, 4, 4, 4);
            gridBagConstraints14.gridy = 3;
            this.jPeriodOfTimePanel.add(getTimePeriodEndComboBox(), gridBagConstraints14);

            GridBagConstraints gridBagConstraints40 = new GridBagConstraints();
            gridBagConstraints40.gridwidth = 2;
            gridBagConstraints40.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints40.weightx = 10.0D;
            gridBagConstraints40.weighty = 0.0D;
            gridBagConstraints40.gridy = 4;
            this.jPeriodOfTimePanel.add(getTimePeriodButtonPanel(), gridBagConstraints40);
        }
        return this.jPeriodOfTimePanel;
    }

    /**
     * 
     * @return
     */
    private JPanel getRealTimePanel() {
        if (this.jRealTimePanel == null) {
            this.jRealTimePanel = new JPanel();
            this.jRealTimePanel.setLayout(new GridBagLayout());

            JLabel sensorsLabel = new JLabel();
            sensorsLabel.setText("Sensor:");
            GridBagConstraints gridBagConstraints00 = new GridBagConstraints();
            gridBagConstraints00.insets = new Insets(4, 4, 4, 4);
            gridBagConstraints00.gridx = 0;
            gridBagConstraints00.gridy = 0;
            gridBagConstraints00.anchor = GridBagConstraints.EAST;
            this.jRealTimePanel.add(sensorsLabel, gridBagConstraints00);

            GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
            gridBagConstraints10.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints10.insets = new Insets(4, 4, 4, 4);
            gridBagConstraints10.gridx = 1;
            gridBagConstraints10.gridy = 0;
            gridBagConstraints10.weightx = 1.0;
            this.jRealTimePanel.add(getRealTimeSensorsComboBox(), gridBagConstraints10);

            JLabel refreshIntervallLabel = new JLabel();
            refreshIntervallLabel.setText("Intervall (secs):");
            GridBagConstraints gridBagConstraints01 = new GridBagConstraints();
            gridBagConstraints01.insets = new Insets(4, 4, 4, 4);
            gridBagConstraints01.gridx = 0;
            gridBagConstraints01.gridy = 1;
            gridBagConstraints01.anchor = GridBagConstraints.EAST;
            this.jRealTimePanel.add(refreshIntervallLabel, gridBagConstraints01);

            GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
            gridBagConstraints11.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints11.insets = new Insets(4, 4, 4, 4);
            gridBagConstraints11.gridx = 1;
            gridBagConstraints11.gridy = 1;
            gridBagConstraints11.weightx = 1.0;
            this.jRealTimePanel.add(getRealTimeRefreshIntervallComboBox(), gridBagConstraints11);

            GridBagConstraints gridBagConstraints30 = new GridBagConstraints();
            gridBagConstraints30.gridy = 2;
            gridBagConstraints30.gridwidth = 2;
            gridBagConstraints30.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints30.weightx = 10.0;
            gridBagConstraints30.weighty = 0.0;
            this.jRealTimePanel.add(getRealTimeButtonPanel(), gridBagConstraints30);
        }
        return this.jRealTimePanel;
    }

    /**
     * 
     * @return
     */
    @SuppressWarnings("unused")
    private TimePeriodPanel getPeriodInputPanel() {
        if (timePP == null) {
            // TODO use capabilities
            TimePeriod tP = new TimePeriod("2008-10-01T13:10:00/2008-11-30T11:30:00/P1D");

            this.timePP = new TimePeriodPanel(tP);
        }
        return this.timePP;
    }

    /**
     * 
     * @return javax.swing.JComboBox
     */
    public JComboBox getTimeInstantSensorsComboBox() {
        if (this.sensorsComboBoxTimeInstant == null) {
            this.sensorsComboBoxTimeInstant = new JComboBox();
            this.sensorsComboBoxTimeInstant.setEditable(false);
            this.sensorsComboBoxTimeInstant.addItemListener(new java.awt.event.ItemListener() {
                public void itemStateChanged(java.awt.event.ItemEvent e) {
                    controller.itemStateChanged_sensorsComboBox(e, TAB_TIME_INSTANT);
                }
            });
        }
        return this.sensorsComboBoxTimeInstant;
    }
    
    /**
     * 
     * @return javax.swing.JComboBox
     */
    public JComboBox getTimePeriodSensorsComboBox() {
        if (this.sensorsComboBoxTimePeriod == null) {
            this.sensorsComboBoxTimePeriod = new JComboBox();
            this.sensorsComboBoxTimePeriod.setEditable(false);
            this.sensorsComboBoxTimePeriod.addItemListener(new java.awt.event.ItemListener() {
                public void itemStateChanged(java.awt.event.ItemEvent e) {
                    controller.itemStateChanged_sensorsComboBox(e, TAB_TIME_PERIOD);
                }
            });
        }
        return this.sensorsComboBoxTimePeriod;
    }

    /**
     * 
     * @return javax.swing.JComboBox
     */
    public JComboBox getRealTimeSensorsComboBox() {
        if (this.sensorsComboBoxRealTime == null) {
            this.sensorsComboBoxRealTime = new JComboBox();
            this.sensorsComboBoxRealTime.setEditable(false);
            this.sensorsComboBoxRealTime.addItemListener(new java.awt.event.ItemListener() {
                public void itemStateChanged(java.awt.event.ItemEvent e) {
                    controller.itemStateChanged_sensorsComboBox(e, TAB_REAL_TIME);
                }
            });

        }
        return this.sensorsComboBoxRealTime;
    }

    /**
     * This method initializes offeringComboBox
     * 
     * @return javax.swing.JComboBox
     */
    public JComboBox getRealTimeRefreshIntervallComboBox() {
        if (this.intervallComboBoxRealTime == null) {
            this.intervallComboBoxRealTime = new JComboBox();
            this.intervallComboBoxRealTime.setEditable(true);
            for (int i : this.intervallComboBoxDefaultItems) {
                this.intervallComboBoxRealTime.addItem(Integer.valueOf(i));
            }
            this.intervallComboBoxRealTime.setSelectedIndex(0);
            this.intervallComboBoxRealTime.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    controller.actionPerfomed_intervallComboBox(e);
                }
            });
        }
        return this.intervallComboBoxRealTime;
    }

    /**
     * 
     * @return javax.swing.JComboBox
     */
    public JComboBox getTimeInstantTimesComboBox() {
        if (this.timesComboBoxTimeInstant == null) {
            this.timesComboBoxTimeInstant = new JComboBox();
            this.timesComboBoxTimeInstant.setEditable(false);
            this.timesComboBoxTimeInstant.addItemListener(new java.awt.event.ItemListener() {
                public void itemStateChanged(java.awt.event.ItemEvent e) {
                    controller.itemStateChanged_timesComboBox(e);
                }
            });
        }
        return this.timesComboBoxTimeInstant;
    }
    
    /**
     * 
     * @return javax.swing.JComboBox
     */
    public JComboBox getTimePeriodStartComboBox() {
        if (this.timesComboBoxTimePeriodStart == null) {
            this.timesComboBoxTimePeriodStart = new JComboBox();
            this.timesComboBoxTimePeriodStart.setEditable(false);
            this.timesComboBoxTimePeriodStart.addItemListener(new java.awt.event.ItemListener() {
                public void itemStateChanged(java.awt.event.ItemEvent e) {
                    controller.itemStateChanged_timesComboBox(e);
                }
            });
        }
        return this.timesComboBoxTimePeriodStart;
    }
    
    /**
     * 
     * @return javax.swing.JComboBox
     */
    public JComboBox getTimePeriodEndComboBox() {
        if (this.timesComboBoxTimePeriodEnd == null) {
            this.timesComboBoxTimePeriodEnd = new JComboBox();
            this.timesComboBoxTimePeriodEnd.setEditable(false);
            this.timesComboBoxTimePeriodEnd.addItemListener(new java.awt.event.ItemListener() {
                public void itemStateChanged(java.awt.event.ItemEvent e) {
                    controller.itemStateChanged_timesComboBox(e);
                }
            });
        }
        return this.timesComboBoxTimePeriodEnd;
    }
    
    /**
     * This method initializes buttonPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getTimePeriodButtonPanel() {
        if (this.buttonPanelTimePeriod == null) {
            GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
            gridBagConstraints6.insets = new Insets(4, 4, 4, 4);
            gridBagConstraints6.gridx = 2;
            gridBagConstraints6.gridy = 0;
            GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
            gridBagConstraints3.insets = new Insets(4, 4, 4, 4);
            gridBagConstraints3.gridy = 0;
            gridBagConstraints3.gridx = 1;
            this.buttonPanelTimePeriod = new JPanel();
            this.buttonPanelTimePeriod.setLayout(new GridBagLayout());
            this.buttonPanelTimePeriod.add(getTimePeriodAddToMapButton(), gridBagConstraints3);
            this.buttonPanelTimePeriod.add(getCancelButton(), gridBagConstraints6);
        }
        return buttonPanelTimePeriod;
    }

    /**
     * This method initializes buttonPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getTimeInstantButtonPanel() {
        if (this.buttonPanelTimeInstant == null) {
            GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
            gridBagConstraints6.insets = new Insets(4, 4, 4, 4);
            gridBagConstraints6.gridx = 2;
            gridBagConstraints6.gridy = 0;
            GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
            gridBagConstraints3.insets = new Insets(4, 4, 4, 4);
            gridBagConstraints3.gridy = 0;
            gridBagConstraints3.gridx = 1;
            GridBagConstraints gridBagConstraints0 = new GridBagConstraints();
            gridBagConstraints0.insets = new Insets(4, 4, 4, 4);
            gridBagConstraints0.gridy = 0;
            gridBagConstraints0.gridx = 0;
            this.buttonPanelTimeInstant = new JPanel();
            this.buttonPanelTimeInstant.setLayout(new GridBagLayout());
            this.buttonPanelTimeInstant.add(getTimeInstantXmlButton(), gridBagConstraints0);
            this.buttonPanelTimeInstant.add(getTimeInstantAddToMapButton(), gridBagConstraints3);
            this.buttonPanelTimeInstant.add(getCancelButton(), gridBagConstraints6);
        }
        return buttonPanelTimeInstant;
    }

    /**
     * This method initializes buttonPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getRealTimeButtonPanel() {
        if (this.buttonPanelRealTime == null) {
            GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
            gridBagConstraints6.gridy = 0;
            gridBagConstraints6.gridx = 2;
            GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
            gridBagConstraints3.insets = new Insets(4, 4, 4, 4);
            gridBagConstraints3.gridy = 0;
            gridBagConstraints3.gridx = 1;
            GridBagConstraints gridBagConstraints0 = new GridBagConstraints();
            gridBagConstraints0.insets = new Insets(4, 4, 4, 4);
            gridBagConstraints0.gridy = 0;
            gridBagConstraints0.gridx = 0;
            this.buttonPanelRealTime = new JPanel();
            this.buttonPanelRealTime.setLayout(new GridBagLayout());
            this.buttonPanelRealTime.add(getRealTimeXmlButton(), gridBagConstraints0);
            this.buttonPanelRealTime.add(getRealTimeAddToMapButton(), gridBagConstraints3);
            this.buttonPanelRealTime.add(getCancelButton(), gridBagConstraints6);
        }
        return buttonPanelRealTime;
    }
    
    /**
     * This method initializes addToMapButton
     * 
     * @return javax.swing.JButton
     */
    private JButton getTimePeriodAddToMapButton() {
        if (this.timePeriodAddToMapButton == null) {
            this.timePeriodAddToMapButton = new JButton();
            this.timePeriodAddToMapButton.setText("ok");
            this.timePeriodAddToMapButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    controller.actionPerformed_addToMapButton(e, TAB_TIME_PERIOD);
                }
            });
        }
        return timePeriodAddToMapButton;
    }

    /**
     * This method initializes addToMapButton
     * 
     * @return javax.swing.JButton
     */
    private JButton getTimeInstantAddToMapButton() {
        if (this.timeInstantAddToMapButton == null) {
            this.timeInstantAddToMapButton = new JButton();
            this.timeInstantAddToMapButton.setText("ok");
            this.timeInstantAddToMapButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    controller.actionPerformed_addToMapButton(e, TAB_TIME_INSTANT);
                }
            });
        }
        return timeInstantAddToMapButton;
    }

    /**
     * 
     * @return
     */
    private JButton getTimeInstantXmlButton() {
        if (this.timeInstantXmlButton == null) {
            this.timeInstantXmlButton = new JButton();
            this.timeInstantXmlButton.setText("Xml Request");
            this.timeInstantXmlButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    controller.actionPerformed_XmlRequestButton(e, TAB_TIME_INSTANT);
                }
            });
        }
        return this.timeInstantXmlButton;
    }

    /**
     * 
     * @return
     */
    private JButton getRealTimeXmlButton() {
        if (this.realTimeXmlButton == null) {
            this.realTimeXmlButton = new JButton();
            this.realTimeXmlButton.setText("Xml Request");
            this.realTimeXmlButton.setEnabled(false);
            this.realTimeXmlButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    controller.actionPerformed_XmlRequestButton(e, TAB_REAL_TIME);
                }
            });
        }
        return this.realTimeXmlButton;
    }

    /**
     * This method initializes addToMapButton
     * 
     * @return javax.swing.JButton
     */
    private JButton getRealTimeAddToMapButton() {
        if (this.realTimeAddToMapButton == null) {
            this.realTimeAddToMapButton = new JButton();
            this.realTimeAddToMapButton.setText("ok");
            this.realTimeAddToMapButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    controller.actionPerformed_addToMapButton(e, TAB_REAL_TIME);
                }
            });
        }
        return realTimeAddToMapButton;
    }

    /**
     * This method initializes addToMapButton
     * 
     * @return javax.swing.JButton
     */
    private JButton getTimeInstantLoadTimesButton() {
        if (this.timeInstantLoadTimesButton == null) {
            this.timeInstantLoadTimesButton = new JButton();
            this.timeInstantLoadTimesButton.setText("Load Times");
            this.timeInstantLoadTimesButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    controller.actionPerformed_loadTimesButton(e, TAB_TIME_INSTANT);
                }
            });
        }
        return timeInstantLoadTimesButton;
    }
    
    /**
     * This method initializes addToMapButton
     * 
     * @return javax.swing.JButton
     */
    private JButton getTimePeriodLoadTimesButton() {
        if (this.timePeriodLoadTimesButton == null) {
            this.timePeriodLoadTimesButton = new JButton();
            this.timePeriodLoadTimesButton.setText("Load Times");
            this.timePeriodLoadTimesButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    controller.actionPerformed_loadTimesButton(e, TAB_TIME_PERIOD);
                }
            });
        }
        return timePeriodLoadTimesButton;
    }

    /**
     * This method initializes cancelButton
     * 
     * @return javax.swing.JButton
     */
    private JButton getCancelButton() {
        JButton cancelButton = new JButton();
        cancelButton.setText("cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                // controller.actionPerformed_cancelButton();
                if (controller.isRequestTaskBusy()) {
                    Object[] options = {"OK", "CANCEL"};
                    String warning_message = "A request task is still busy.\n"
                            + "Clicking 'OK' will attempt to cancel all busy tasks and \nclose all dependent dialogs. \n"
                            + "Do you wish to continue?";
                    int chosenOption = JOptionPane.showOptionDialog(null,
                                                                    warning_message,
                                                                    "Warning",
                                                                    JOptionPane.DEFAULT_OPTION,
                                                                    JOptionPane.WARNING_MESSAGE,
                                                                    null,
                                                                    options,
                                                                    options[0]);
                    if (chosenOption == JOptionPane.OK_OPTION) {
                        controller.dialogWindowClosed();
                        dispose();
                    }
                }
                else {
                    controller.dialogWindowClosed();
                    dispose();
                }
            }
        });
        return cancelButton;
    }

    /**
     * Sets the enabled states of the getMap, and getCapabilities buttons and updates the status label for the
     * getCapabilities request. This is usually called from one of the background threads in the controller
     * for this dialog.
     * 
     * @param state
     * 
     */
    public void setButtonStates(State state) {
        // GUI updates should only be performed in the EventDispatch thread
        if (SwingUtilities.isEventDispatchThread()) {
            if (state != null) {
                /** switch request state **/
                switch (state) {
                case NO_ACTIVE_REQUEST:
                    if (controller.isRequestTaskBusy()) {
                        // inconsistent state
                        throw new RuntimeException("request is in an inconsistent state.");
                    }
                    setStatusText(capabilitiesState.NO_ACTIVE_REQUEST.toString());
                    this.timeInstantLoadTimesButton.setEnabled(true);
                    this.timeInstantAddToMapButton.setEnabled(true);
                    this.timeInstantXmlButton.setEnabled(true);
                    this.realTimeAddToMapButton.setEnabled(true);
                    //this.timePeriodAddToMapButton.setEnabled(true);
                    this.progressBar.setIndeterminate(false);
                    this.progressBar.setValue(0);
                    break;
                case INITIALIZING_REQUEST:
                    setStatusText(capabilitiesState.INITIALIZING_REQUEST.toString());
                    this.timeInstantLoadTimesButton.setEnabled(false);
                    this.timeInstantAddToMapButton.setEnabled(false);
                    this.timeInstantXmlButton.setEnabled(false);
                    //this.timePeriodAddToMapButton.setEnabled(false);
                    this.realTimeAddToMapButton.setEnabled(false);
                    progressBar.setIndeterminate(true);
                    break;
                case WAITING_FOR_SERVICE:
                    setStatusText(capabilitiesState.WAITING_FOR_SERVICE.toString());
                    this.timeInstantLoadTimesButton.setEnabled(false);
                    this.timeInstantAddToMapButton.setEnabled(false);
                    this.timeInstantXmlButton.setEnabled(false);
                    //this.timePeriodAddToMapButton.setEnabled(false);
                    this.realTimeAddToMapButton.setEnabled(false);
                    progressBar.setIndeterminate(true);
                    break;
                case INITIALIZING_RESPONSE_DISPLAY:
                    setStatusText(capabilitiesState.INITIALIZING_RESPONSE_DISPLAY.toString());
                    this.timeInstantLoadTimesButton.setEnabled(false);
                    this.timeInstantAddToMapButton.setEnabled(false);
                    this.timeInstantXmlButton.setEnabled(false);
                    //this.timePeriodAddToMapButton.setEnabled(false);
                    this.realTimeAddToMapButton.setEnabled(false);
                    progressBar.setIndeterminate(true);
                    break;
                case DONE:
                    setStatusText(capabilitiesState.DONE.toString());
                    this.timeInstantLoadTimesButton.setEnabled(true);
                    this.timeInstantAddToMapButton.setEnabled(true);
                    this.timeInstantXmlButton.setEnabled(true);
                    //this.timePeriodAddToMapButton.setEnabled(true);
                    this.realTimeAddToMapButton.setEnabled(true);
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(this.progressLength);
                    
                    setButtonStates(State.SELECTING);
                    break;
                case SELECTING:
                    /** check drop down menues **/
                    Object selectedItem01 = getTimeInstantSensorsComboBox().getSelectedItem();
                    Object selectedItem02 = getTimeInstantTimesComboBox().getSelectedItem();
                    if (selectedItem01 == null || selectedItem02 == null) {
                        this.timeInstantAddToMapButton.setEnabled(false);
                        this.timeInstantXmlButton.setEnabled(false);
                        this.timeInstantLoadTimesButton.setEnabled(true);
                    } else {
                        this.timeInstantAddToMapButton.setEnabled(true);
                    }

                    Object selectedItem03 = getRealTimeSensorsComboBox().getSelectedItem();
                    Object selectedItem04 = getRealTimeRefreshIntervallComboBox().getSelectedItem();
                    if (selectedItem03 == null || selectedItem04 == null) {
                        this.realTimeAddToMapButton.setEnabled(false);
                        this.realTimeXmlButton.setEnabled(false);
                    } else {
                        this.realTimeAddToMapButton.setEnabled(true);
                    }
                    
                    Object selectedItem07 = getRealTimeSensorsComboBox().getSelectedItem();
                    //Object selectedItem05 = getTimePeriodStartComboBox().getSelectedItem();
                    //Object selectedItem06 = getTimePeriodStartComboBox().getSelectedItem();
                    if (selectedItem07 == null) { // || selectedItem05 == null || selectedItem06 == null) {
                        //this.timePeriodAddToMapButton.setEnabled(false);
                        //this.timePeriodLoadTimesButton.setEnabled(true);
                    } else {
                        //this.timePeriodAddToMapButton.setEnabled(true);
                    }
                    break;
                }
            }
        }
        else {
            // if this is not the event dispatch thread, put a request on the
            // queue for that thread.
            Runnable changeButtons = new Runnable() {
                public void run() {
                    setButtonStates(controller.getRequestState());
                }
            };
            SwingUtilities.invokeLater(changeButtons);
        }
    }

    /**
     * 
     * @param string
     */
    public void setStatusText(String string) {
        this.statusText.setText(string);
    }

    private void addWindowAdapter() {
        // we want to use our own window listener when closing the window.
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        // listen for window closing and window gained focus events
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // warn if any requests are still pending
                if (controller.isRequestTaskBusy()) {
                    Object[] options = {"OK", "CANCEL"};
                    String warning_message = "A request task is still busy.\n"
                            + "Clicking 'OK' will attempt to cancel all busy tasks and \nclose all dependent dialogs. \n"
                            + "Do you wish to continue?";
                    int chosenOption = JOptionPane.showOptionDialog(null,
                                                                    warning_message,
                                                                    "Warning",
                                                                    JOptionPane.DEFAULT_OPTION,
                                                                    JOptionPane.WARNING_MESSAGE,
                                                                    null,
                                                                    options,
                                                                    options[0]);
                    if (chosenOption == JOptionPane.OK_OPTION) {
                        super.windowClosing(e);
                        controller.dialogWindowClosed();
                        (e.getWindow()).dispose();
                    }
                }
                else {
                    super.windowClosing(e);
                    controller.dialogWindowClosed();
                    (e.getWindow()).dispose();
                }
            }
        });
        addWindowFocusListener(new WindowAdapter() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
                super.windowGainedFocus(e);
                setButtonStates(controller.getRequestState());
            }
        });
    }
}
