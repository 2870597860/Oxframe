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
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import org.apache.log4j.Logger;
import org.n52.oxf.feature.OXFFeature;
import org.n52.oxf.feature.OXFFeatureCollection;
import org.n52.oxf.layer.IContextLayer;
import org.n52.oxf.render.sosMobile.RealTimeFeatureServiceLayer;
import org.n52.oxf.render.sosMobile.RealTimeFeatureServiceLayer.RealTimeRefresher;
import org.n52.oxf.util.EventName;
import org.n52.oxf.util.IEventListener;
import org.n52.oxf.util.LoggingHandler;
import org.n52.oxf.util.OXFEvent;
import org.n52.oxf.util.OXFEventException;

/**
 * 
 * Controller window to start and stop pseude-real time updates (i.e. refreshing a 
 * layer in a fixed time inervall). It holds a RealTimeFeatureServiceLayer that is
 * refreshed every time a LAYER_REAL_TIME_REFRESH event is caught.
 * 
 * @author <a href="mailto:daniel.nuest@uni-muenster.de">Daniel Nüst</a>
 * 
 */
public class RealTimeLayerRefresh_ConfiguratorAndController extends JDialog implements IEventListener, PropertyChangeListener {

    protected static Logger LOGGER = LoggingHandler.getLogger(RealTimeLayerRefresh_ConfiguratorAndController.class);

    /**
     * 
     */
    private static final long serialVersionUID = -5959878218030855386L;
    protected RealTimeFeatureServiceLayer layer;
    private OXFFeatureCollection featureCollection;
    private JPanel statusPanel;
    private JPanel controlButtonPanel;
    private JButton startButton;
    private JButton stopButton;
    private JComboBox intervallComboBox;
    private Integer[] intervallComboBoxDefaultItems = {new Integer(5),
                                                       new Integer(10),
                                                       new Integer(30),
                                                       new Integer(60),
                                                       new Integer(120),
                                                       new Integer(600)};
    private Set<Integer> intervallItems = new TreeSet<Integer>();

    private JPanel statusBar;

    private JLabel lastUpdateLabel;

    private JLabel nextUpdateLabel;

    public RealTimeLayerRefresh_ConfiguratorAndController(JDialog owner, RealTimeFeatureServiceLayer layer) {
        super(owner, "Real Time Controller");
        this.layer = layer;

        this.featureCollection = layer.getFeatureCollection();
        this.layer.getRefresher().addEventListener(this);
        this.layer.getRefresher().addPropertyChangeListener(this);

        initialize(owner);
    }

    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize(JDialog owner) {
        // this.setSize(400, 300);
        this.setLocation(owner.getLocation());
        this.setPreferredSize(new java.awt.Dimension(400, 200));
        this.setMinimumSize(new java.awt.Dimension(300, 200));
        BorderLayout layout = new BorderLayout(5, 5);
        this.setLayout(layout);
        this.add(getInfoPanel(), BorderLayout.NORTH);
        this.add(getControlPanel(), BorderLayout.CENTER);
        this.add(getStatusPanel(), BorderLayout.SOUTH);
        
        this.pack();
        addWindowAdapter();
    }

    /**
     * 
     * @return
     */
    private JPanel getInfoPanel() {
        StringBuilder sb = new StringBuilder();
        for (OXFFeature f : this.featureCollection) {
            sb.append(f.getID());
            sb.append("\n");
        }

        JLabel infoLabel = new JLabel(sb.toString());
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        infoPanel.setPreferredSize(new Dimension(this.getPreferredSize().width, 20));

        infoPanel.add(infoLabel);

        return infoPanel;
    }

    private JPanel getControlPanel() {
        if (this.controlButtonPanel == null) {
            this.controlButtonPanel = new JPanel();
            this.controlButtonPanel.setLayout(new GridBagLayout());
            this.controlButtonPanel.setPreferredSize(new Dimension(this.getPreferredSize().width, 60));

            GridBagConstraints gridBagConstraints0 = new GridBagConstraints();
            gridBagConstraints0.insets = new java.awt.Insets(4, 4, 4, 4);
            gridBagConstraints0.gridy = 0;
            gridBagConstraints0.gridx = 0;
            gridBagConstraints0.fill = GridBagConstraints.HORIZONTAL;
            this.controlButtonPanel.add(getStartButton(), gridBagConstraints0);

            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.insets = new java.awt.Insets(4, 4, 4, 4);
            gridBagConstraints1.gridy = 0;
            gridBagConstraints1.gridx = 1;
            gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
            this.controlButtonPanel.add(getStopButton(), gridBagConstraints1);

            GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
            gridBagConstraints2.insets = new java.awt.Insets(4, 4, 4, 4);
            gridBagConstraints2.gridy = 0;
            gridBagConstraints2.gridx = 2;
            gridBagConstraints2.fill = GridBagConstraints.HORIZONTAL;
            this.controlButtonPanel.add(getEndButton(), gridBagConstraints2);

            GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
            gridBagConstraints10.insets = new java.awt.Insets(4, 4, 4, 4);
            gridBagConstraints10.gridy = 1;
            // gridBagConstraints10.gridx = 0;
            gridBagConstraints10.gridwidth = 3;
            gridBagConstraints10.fill = GridBagConstraints.HORIZONTAL;
            this.controlButtonPanel.add(getIntervallComboBox(), gridBagConstraints10);
        }
        return controlButtonPanel;
    }

    /**
     * This method initializes offeringComboBox
     * 
     * @return javax.swing.JComboBox
     */
    public JComboBox getIntervallComboBox() {
        if (this.intervallComboBox == null) {
            this.intervallComboBox = new JComboBox();
            this.intervallComboBox.setEditable(true);
            Integer startIntervall = Integer.valueOf((int) (this.layer.getRefresher().getIntervall() / 1000));
            this.intervallItems.addAll(Arrays.asList(intervallComboBoxDefaultItems));
            this.intervallItems.add(startIntervall);

            for (Integer i : this.intervallItems) {
                this.intervallComboBox.addItem(i);
            }

            this.intervallComboBox.setSelectedItem(startIntervall);

            this.intervallComboBox.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    long l = validateIntervallInput();
                    layer.getRefresher().setIntervall(l);
                }

                private long validateIntervallInput() {
                    Object o = getIntervallComboBox().getSelectedItem();
                    if (o instanceof String) {
                        String s = (String) o;
                        if (s.length() == 0) {
                            getIntervallComboBox().setSelectedItem(null);
                        }
                        else {
                            try {
                                Integer i = new Integer(s);
                                if (i.intValue() < 1) {
                                    getIntervallComboBox().setSelectedItem("");
                                    getIntervallComboBox().setSelectedItem(null);
                                }
                                return i.longValue() * 1000;
                            }
                            catch (NumberFormatException ex) {
                                LOGGER.error("illegal input in intervall combo box, cannot parse Integer: " + s);
                                getIntervallComboBox().setSelectedItem("");
                                getIntervallComboBox().setSelectedItem(null);
                            }

                        }
                    }
                    else if (o instanceof Integer) {
                        Integer i = (Integer) o;
                        if (i.intValue() < 1) {
                            getIntervallComboBox().setSelectedItem("");
                            getIntervallComboBox().setSelectedItem(null);
                        }
                        return i.longValue() * 1000;
                    }

                    return layer.getRefresher().getIntervall();
                }
            });
        }
        return this.intervallComboBox;
    }

    /**
     * 
     * @return
     */
    private JPanel getStatusPanel() {
        if (this.statusPanel == null) {
            this.statusPanel = new JPanel(new GridBagLayout());
            this.statusPanel.setPreferredSize(new Dimension(this.getPreferredSize().width, 60));

            GridBagConstraints gridBagConstraints0 = new GridBagConstraints();
            gridBagConstraints0.insets = new java.awt.Insets(4, 4, 4, 4);
            gridBagConstraints0.gridy = 0;
            gridBagConstraints0.gridx = 0;
            gridBagConstraints0.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints0.anchor = GridBagConstraints.CENTER;
            this.statusPanel.add(getLastUpdateLabel(), gridBagConstraints0);

            GridBagConstraints gridBagConstraints00 = new GridBagConstraints();
            gridBagConstraints00.insets = new java.awt.Insets(4, 4, 4, 4);
            gridBagConstraints00.gridy = 1;
            gridBagConstraints00.gridx = 0;
            gridBagConstraints00.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints00.anchor = GridBagConstraints.CENTER;
            this.statusPanel.add(getNextUpdateLabel(), gridBagConstraints00);
            
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.insets = new java.awt.Insets(0, 0, 0, 0);
            gridBagConstraints1.gridy = 2;
            gridBagConstraints1.gridx = 0;
            gridBagConstraints1.weightx = 10.0;
            gridBagConstraints1.fill = GridBagConstraints.BOTH;
            this.statusPanel.add(getStatusBar(), gridBagConstraints1);
        }
        return statusPanel;
    }

    /**
     * 
     * @return
     */
    private JLabel getLastUpdateLabel() {
        if (this.lastUpdateLabel == null) {
            this.lastUpdateLabel = new JLabel();
            this.lastUpdateLabel.setAlignmentY(CENTER_ALIGNMENT);
            this.lastUpdateLabel.setPreferredSize(new Dimension(this.getPreferredSize().width, 20));
            resetNextAndLastUpdate();
        }
        return this.lastUpdateLabel;
    }
    
    /**
     * 
     * @return
     */
    private JLabel getNextUpdateLabel() {
        if (this.nextUpdateLabel == null) {
            this.nextUpdateLabel = new JLabel();
            this.nextUpdateLabel.setAlignmentY(CENTER_ALIGNMENT);
            this.nextUpdateLabel.setPreferredSize(new Dimension(this.getPreferredSize().width, 20));
            resetNextAndLastUpdate();
        }
        return this.nextUpdateLabel;
    }

    /**
     * 
     * @return
     */
    private JPanel getStatusBar() {
        if (this.statusBar == null) {
            this.statusBar = new JPanel(new FlowLayout());
            this.statusBar.setPreferredSize(new Dimension(Integer.MAX_VALUE, 20));
            this.statusBar.setBackground(Color.RED);
        }
        return this.statusBar;
    }

    /**
     * 
     * @param timeToNextRefresh 
     */
    protected void setStatus(long timeToNextRefresh) {
        boolean refreshing = this.layer.getRefresher().isRefreshing();
        if (refreshing) {
            getStatusBar().setBackground(Color.GREEN);
            if (timeToNextRefresh == 0) {
                getNextUpdateLabel().setText("Updating ...");
            } else {
                getNextUpdateLabel().setText("Next update in " + timeToNextRefresh + " milliseconds");
            }
        }
        else {
            getStatusBar().setBackground(Color.RED);
            resetNextAndLastUpdate();
        }
    }

    /**
     * 
     * @param lastUpdate
     */
    protected void setLastUpdate(Date lastUpdate) {
        getLastUpdateLabel().setText("Last update at " + lastUpdate);
    }

    /**
     * 
     */
    protected void resetNextAndLastUpdate() {
        getLastUpdateLabel().setText("---");
        getNextUpdateLabel().setText("---");
    }

    /**
     * This method initializes addToMapButton
     * 
     * @return javax.swing.JButton
     */
    protected JButton getStartButton() {
        if (this.startButton == null) {
            this.startButton = new JButton();
            this.startButton.setText("Start");
            this.startButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    getStartButton().setEnabled(false);
                    getStopButton().setEnabled(true);
                    layer.getRefresher().startRefreshing();
//                    setStatus();
                }
            });
        }
        return startButton;
    }

    /**
     * This method initializes addToMapButton
     * 
     * @return javax.swing.JButton
     */
    protected JButton getStopButton() {
        if (this.stopButton == null) {
            this.stopButton = new JButton();
            this.stopButton.setText("Stop");
            this.stopButton.setEnabled(false);
            this.stopButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    getStartButton().setEnabled(true);
                    getStopButton().setEnabled(false);
                    layer.getRefresher().stopRefreshing();
                    resetNextAndLastUpdate();
                }
            });
        }
        return stopButton;
    }

    /**
     * This method initializes cancelButton
     * 
     * @return javax.swing.JButton
     */
    private JButton getEndButton() {
        JButton cancelButton = new JButton();
        cancelButton.setText("End");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                // controller.actionPerformed_cancelButton();
                if (layer.getRefresher().isRefreshing()) {
                    Object[] options = {"OK", "CANCEL"};
                    String warning_message = "A visualization is still busy.\n"
                            + "Clicking 'OK' will stop real time visualization. \n" + "Do you wish to continue?";
                    int chosenOption = JOptionPane.showOptionDialog(null,
                                                                    warning_message,
                                                                    "Warning",
                                                                    JOptionPane.DEFAULT_OPTION,
                                                                    JOptionPane.WARNING_MESSAGE,
                                                                    null,
                                                                    options,
                                                                    options[0]);
                    if (chosenOption == JOptionPane.OK_OPTION) {
                        layer.endRefreshing();
                        dispose();
                    }
                }
                else {
                    layer.endRefreshing();
                    dispose();
                }
            }
        });
        return cancelButton;
    }

    /**
     * 
     */
    private void addWindowAdapter() {
        // we want to use our own window listener when closing the window.
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        // listen for window closing and window gained focus events
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // warn if any requests are still pending
                if (layer.getRefresher().isRefreshing()) {
                    Object[] options = {"OK", "CANCEL"};
                    String warning_message = "A visualization is still busy.\n"
                            + "Clicking 'OK' will stop real time visualization. \n" + "Do you wish to continue?";
                    int chosenOption = JOptionPane.showOptionDialog(null,
                                                                    warning_message,
                                                                    "Warning",
                                                                    JOptionPane.DEFAULT_OPTION,
                                                                    JOptionPane.WARNING_MESSAGE,
                                                                    null,
                                                                    options,
                                                                    options[0]);
                    if (chosenOption == JOptionPane.OK_OPTION) {
                        endThings(e);
                    }
                }
                else {
                    endThings(e);
                }
            }

            private void endThings(WindowEvent e) {
                super.windowClosing(e);
                layer.endRefreshing();
                (e.getWindow()).dispose();
            }
        });
    }

    @Override
    public void eventCaught(OXFEvent evt) throws OXFEventException {
        if (evt.getName().equals(EventName.LAYER_REAL_TIME_REFRESH)) {
            if (evt.getSource() instanceof IContextLayer) {
                IContextLayer eventLayer = (IContextLayer) evt.getSource();
                if (this.layer.equals(eventLayer)) {
                    Date updateAt = new Date(((Long)evt.getValue()).longValue());
                    setLastUpdate(updateAt);
                    setStatus(0);
                }
            }
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(RealTimeRefresher.PROPERTY_EVENT_NEXT_UPDATE_IN_MILLIS)) {
            setStatus(((Long)evt.getNewValue()).longValue());
        }
    }

}
