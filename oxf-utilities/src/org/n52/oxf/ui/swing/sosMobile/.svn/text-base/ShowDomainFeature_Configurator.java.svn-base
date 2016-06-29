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
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import org.n52.oxf.serviceAdapters.sos.SOSAdapter;
import org.n52.oxf.ui.swing.MapCanvas;
import org.n52.oxf.ui.swing.sosMobile.ShowDomainFeature_ConfiguratorController.State;
import org.n52.oxf.ui.swing.tree.ContentTree;
import org.n52.oxf.ui.swing.wms.ConnectWMSDialogController.capabilitiesState;

/**
 * 
 * Dialog for selection of a domain feature provided by a given SOS instance.
 * 
 * @author <a href="mailto:daniel.nuest@uni-muenster.de">Daniel Nüst</a>
 * 
 */
public class ShowDomainFeature_Configurator extends JDialog {

    /**
     * 
     */
    private static final long serialVersionUID = 2586963353974465365L;
    private JPanel contentPanel;
    private JComboBox domainFeatureComboBox;
    private JPanel statusPanel;
    private JLabel statusText;
    private JProgressBar progressBar;
    private JPanel buttonPanel;
    private JButton xmlButton;
    private JButton addToMapButton;
    protected ShowDomainFeature_ConfiguratorController controller;
    private int progressLength;

    /**
     * 
     * @param owner
     * @param serviceURL
     * @param adapter
     * @param map
     * @param tree
     */
    public ShowDomainFeature_Configurator(JDialog owner,
                                          URL serviceURL,
                                          SOSAdapter adapter,
                                          MapCanvas map,
                                          ContentTree tree) {
        super(owner, "Show Domain Features.");

        this.controller = new ShowDomainFeature_ConfiguratorController(this, serviceURL, adapter, map, tree);

        initialize(owner);
    }
    
    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize(JDialog owner) {
        this.setLocation(owner.getLocation());
        this.setPreferredSize(new java.awt.Dimension(400, 120));
        this.setLayout(new BorderLayout(5, 5));
        this.add(getContentPanel(), BorderLayout.CENTER); // setContentPane(getJTabbedPane());
        this.add(getStatusPanel(), BorderLayout.SOUTH);
        this.pack();
        addWindowAdapter();
    }
    
    /**
     * 
     * @return
     */
    private JPanel getContentPanel() {
        if (contentPanel == null) {
            this.contentPanel = new JPanel();
            this.contentPanel.setLayout(new GridBagLayout());

            GridBagConstraints gridBagConstraints01 = new GridBagConstraints();
            gridBagConstraints01.gridx = 0;
            gridBagConstraints01.insets = new java.awt.Insets(4, 4, 4, 4);
            gridBagConstraints01.gridy = 1;
            JLabel sensorsLabel = new JLabel();
            sensorsLabel.setText("Domain Features:");
            gridBagConstraints01.anchor = GridBagConstraints.EAST;
            this.contentPanel.add(sensorsLabel, gridBagConstraints01);

            GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
            gridBagConstraints11.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints11.gridy = 1;
            gridBagConstraints11.weightx = 1.0;
            gridBagConstraints11.insets = new java.awt.Insets(4, 4, 4, 4);
            gridBagConstraints11.gridx = 1;
            this.contentPanel.add(getDomainFeatureComboBox(), gridBagConstraints11);

            GridBagConstraints gridBagConstraints03 = new GridBagConstraints();
            gridBagConstraints03.gridwidth = 2;
            gridBagConstraints03.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints03.weightx = 10.0D;
            gridBagConstraints03.weighty = 0.0D;
            gridBagConstraints03.gridy = 3;
            this.contentPanel.add(getButtonPanel(), gridBagConstraints03);
        }
        return contentPanel;
    }
    
    /**
     * This method initializes offeringComboBox
     * 
     * @return javax.swing.JComboBox
     */
    public JComboBox getDomainFeatureComboBox() {
        if (this.domainFeatureComboBox == null) {
            this.domainFeatureComboBox = new JComboBox();
            this.domainFeatureComboBox.setEditable(false);
            this.domainFeatureComboBox.addItemListener(new java.awt.event.ItemListener() {
                public void itemStateChanged(java.awt.event.ItemEvent e) {
                    controller.itemStateChanged_domainFeatureComboBox(e);
                }
            });
        }
        return this.domainFeatureComboBox;
    }
    
    /**
     * This method initializes buttonPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getButtonPanel() {
        if (this.buttonPanel == null) {
            GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
            gridBagConstraints6.gridy = 0;
            gridBagConstraints6.gridx = 2;
            GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
            gridBagConstraints3.insets = new java.awt.Insets(4, 4, 4, 4);
            gridBagConstraints3.gridy = 0;
            gridBagConstraints3.gridx = 1;
            GridBagConstraints gridBagConstraints0 = new GridBagConstraints();
            gridBagConstraints0.insets = new java.awt.Insets(4, 4, 4, 4);
            gridBagConstraints0.gridy = 0;
            gridBagConstraints0.gridx = 0;
            this.buttonPanel = new JPanel();
            this.buttonPanel.setLayout(new GridBagLayout());
            this.buttonPanel.add(getXmlButton(), gridBagConstraints0);
            this.buttonPanel.add(getAddToMapButton(), gridBagConstraints3);
            this.buttonPanel.add(getCancelButton(), gridBagConstraints6);
        }
        return buttonPanel;
    }
    
    /**
     * 
     * @return
     */
    private JButton getXmlButton() {
        if (this.xmlButton == null) {
            this.xmlButton = new JButton();
            this.xmlButton.setText("Xml Request");
            this.xmlButton.setEnabled(false);
            this.xmlButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    controller.actionPerformed_XmlRequestButton(e);
                }
            });
        }
        return this.xmlButton;
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
     * This method initializes addToMapButton
     * 
     * @return javax.swing.JButton
     */
    private JButton getAddToMapButton() {
        if (this.addToMapButton == null) {
            this.addToMapButton = new JButton();
            this.addToMapButton.setText("ok");
            this.addToMapButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    controller.actionPerformed_addToMapButton(e);
                }
            });
        }
        return addToMapButton;
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
     * @param string
     */
    public void setStatusText(String string) {
        this.statusText.setText(string);
    }
    
    /**
     * 
     * @param state
     */
    public void setButtonStates(org.n52.oxf.ui.swing.sosMobile.ShowDomainFeature_ConfiguratorController.State state) {
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
                    this.xmlButton.setEnabled(true);
                    this.addToMapButton.setEnabled(true);
                    this.progressBar.setIndeterminate(false);
                    this.progressBar.setValue(0);
                    break;
                case INITIALIZING_REQUEST:
                    setStatusText(capabilitiesState.INITIALIZING_REQUEST.toString());
                    this.addToMapButton.setEnabled(false);
                    this.xmlButton.setEnabled(false);
                    progressBar.setIndeterminate(true);
                    break;
                case WAITING_FOR_SERVICE:
                    setStatusText(capabilitiesState.WAITING_FOR_SERVICE.toString());
                    this.addToMapButton.setEnabled(false);
                    this.xmlButton.setEnabled(false);
                    progressBar.setIndeterminate(true);
                    break;
                case INITIALIZING_RESPONSE_DISPLAY:
                    setStatusText(capabilitiesState.INITIALIZING_RESPONSE_DISPLAY.toString());
                    this.addToMapButton.setEnabled(false);
                    this.xmlButton.setEnabled(false);
                    progressBar.setIndeterminate(true);
                    break;
                case DONE:
                    setStatusText(capabilitiesState.DONE.toString());
                    this.addToMapButton.setEnabled(true);
                    this.xmlButton.setEnabled(true);
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(this.progressLength);
                    
                    setButtonStates(State.SELECTING);
                    break;
                case SELECTING:
                    /** check drop down menues **/
                    Object selectedItem01 = getDomainFeatureComboBox().getSelectedItem();
                    if (selectedItem01 == null) {
                        this.addToMapButton.setEnabled(false);
                        this.xmlButton.setEnabled(false);
                    } else {
                        this.addToMapButton.setEnabled(true);
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
     */
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
