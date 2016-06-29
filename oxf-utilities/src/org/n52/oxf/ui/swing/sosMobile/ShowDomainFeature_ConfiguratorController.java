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

import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.io.IOException;
import java.net.ConnectException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.n52.oxf.OXFException;
import org.n52.oxf.owsCommon.ExceptionReport;
import org.n52.oxf.owsCommon.ServiceDescriptor;
import org.n52.oxf.owsCommon.capabilities.Contents;
import org.n52.oxf.owsCommon.capabilities.Operation;
import org.n52.oxf.serviceAdapters.OperationResult;
import org.n52.oxf.serviceAdapters.ParameterContainer;
import org.n52.oxf.serviceAdapters.sos.ISOSRequestBuilder;
import org.n52.oxf.serviceAdapters.sos.SOSAdapter;
import org.n52.oxf.serviceAdapters.sosMobile.SOSmobileRequestBuilder;
import org.n52.oxf.ui.swing.ApprovalDialog;
import org.n52.oxf.ui.swing.MapCanvas;
import org.n52.oxf.ui.swing.ShowRequestDialog;
import org.n52.oxf.ui.swing.ShowXMLDocDialog;
import org.n52.oxf.ui.swing.tree.ContentTree;
import org.n52.oxf.util.IOHelper;
import org.n52.oxf.util.LoggingHandler;

/**
 * 
 * Dialog for selection of a domain feature provided by a given SOS instance.
 * 
 * @author <a href="mailto:daniel.nuest@uni-muenster.de">Daniel Nüst</a>
 * 
 */
public class ShowDomainFeature_ConfiguratorController {

    private static Logger LOGGER = LoggingHandler.getLogger(ShowDomainFeature_ConfiguratorController.class);

    /**
     * enumeration to keep track of the state of the requests initiated by the ShowSensorPosition_Configre
     * dialog
     */
    public enum State {
        NO_ACTIVE_REQUEST("No Active Request"), INITIALIZING_REQUEST("Initializing Request"), WAITING_FOR_SERVICE(
                "Waiting for Service Response"), INITIALIZING_RESPONSE_DISPLAY(
                "Response Recieved - Initializing Display"), DONE("Done"), SELECTING("Selecting Values");

        private final String message;

        State(String message) {
            this.message = message;
        }

        @Override
        public String toString() {
            return message;
        }
    }

    private static final int THREAD_POOL_SIZE = 2;

    protected ShowDomainFeature_Configurator view;
    private ContentTree tree;
    private MapCanvas map;
    protected SOSAdapter adapter;
    protected URL serviceURL;
    private ArrayList<String> domainFeatureIDList;
    private org.n52.oxf.ui.swing.sosMobile.ShowDomainFeature_ConfiguratorController.State requestState;

    protected ExecutorService threadPool;

    protected Future< ? > requestTask;

    protected ServiceDescriptor serviceDesc;

    /**
     * Inner classes representing the tasks to be done by the background thread i.e. those that involve
     * querying services
     */

    public ShowDomainFeature_ConfiguratorController(ShowDomainFeature_Configurator view,
                                                    URL serviceURL,
                                                    SOSAdapter adapter,
                                                    MapCanvas map,
                                                    ContentTree tree) {
        this.view = view;
        this.tree = tree;
        this.map = map;
        this.adapter = adapter;
        this.serviceURL = serviceURL;
        this.domainFeatureIDList = new ArrayList<String>();

        this.requestState = State.NO_ACTIVE_REQUEST;
        threadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

        requestTask = threadPool.submit(new GetServiceDescription());
    }

    /**
     * sets values in the gui according to the service description
     * 
     */
    public void initGUI() {
        // TODO operation GetDomainFeature is not contained in OperationsMetadata
        // Operation operation =
        // this.serviceDesc.getOperationsMetadata().getOperationByName(SOSmobileAdapter.GET_DOMAIN_FEATURE);
        Contents contents = this.serviceDesc.getContents();
        System.out.println(contents);

        this.domainFeatureIDList.add("eu-001");
        
        for (String s : this.domainFeatureIDList) {
            this.view.getDomainFeatureComboBox().addItem(s);
        }
    }

    /**
     * @param e2
     * 
     * 
     */
    public void actionPerformed_addToMapButton(ActionEvent event) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("addToMap action");
        }

        String domainFeatureID = (String) this.view.getDomainFeatureComboBox().getSelectedItem();
        showDomainFeature(domainFeatureID);

        // view.dispose();
    }

    /**
     * 
     */
    public void actionPerformed_XmlRequestButton(ActionEvent event) {

        String domainFeatureID = (String) this.view.getDomainFeatureComboBox().getSelectedItem();
        showDomainFeature(domainFeatureID);

        // get location

        ParameterContainer parameters;
        try {
            parameters = getBasicParameterContainer(domainFeatureID);
        }
        catch (OXFException e) {
            if (e.getCause() instanceof ConnectException) {
                JOptionPane.showMessageDialog(view,
                                              "Could not create request with given parameters! Please view the logs for details.",
                                              "Error",
                                              JOptionPane.ERROR_MESSAGE);
            }
            LOGGER.error(e);
            return;
        }

        String postRequest = adapter.getRequestBuilder().buildDescribeSensorRequest(parameters);
        int returnVal = new ShowRequestDialog(view, "DescribeSensor Request", postRequest).showDialog();

        try {
            if (returnVal == ApprovalDialog.APPROVE_OPTION) {
                OperationResult opResult = adapter.doOperation(new Operation(SOSAdapter.DESCRIBE_SENSOR,
                                                                             serviceURL.toString() + "?",
                                                                             serviceURL.toString()), parameters);

                String describeSensorDoc = IOHelper.readText(opResult.getIncomingResultAsStream());

                new ShowXMLDocDialog(view.getLocation(), "DescribeSensor Response", describeSensorDoc).setVisible(true);
            }
        }
        catch (OXFException e) {
            if (e.getCause() instanceof ConnectException) {
                JOptionPane.showMessageDialog(view, "Could not connect to service!", "Error", JOptionPane.ERROR_MESSAGE);
            }
            LOGGER.error(e);
        }
        catch (ExceptionReport e) {
            JOptionPane.showMessageDialog(view,
                                          "The request sended to the SOS produced a service sided exception. Please view the logs for details.",
                                          "Error",
                                          JOptionPane.ERROR_MESSAGE);
            LOGGER.error(e);
        }
        catch (IOException e) {
            LOGGER.error(e);
        }

    }

    /**
     * 
     * @param domainFeatureID
     */
    private void showDomainFeature(String domainFeatureID) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Show domain feature " + domainFeatureID);
        }

        /*
        ParameterContainer parameters;
        try {
            parameters = getBasicParameterContainer(domainFeatureID);
        }
        catch (OXFException e) {
            JOptionPane.showMessageDialog(view,
                                          "Could not create request parameters. Please view the logs for details.",
                                          "Error",
                                          JOptionPane.ERROR_MESSAGE);
            LOGGER.error("error creating parameter container: ", e);
            return;
        }

        // make nice id and title for the layer
        String layerID = domainFeatureID + System.currentTimeMillis();
        String layerTitle = domainFeatureID;

        */
        
        throw new UnsupportedOperationException("method not implemented!");

        //TODO create and add layer (with domain feature store etc.)
        
    }

    /**
     * @throws OXFException
     */
    private ParameterContainer getBasicParameterContainer(String domainFeatureID) throws OXFException {
        ParameterContainer parameters = new ParameterContainer();
        parameters.addParameterShell(SOSmobileRequestBuilder.GET_DOMAIN_FEATURE_SERVICE_PARAMETER,
                                     this.adapter.getServiceType());

        parameters.addParameterShell(SOSmobileRequestBuilder.GET_DOMAIN_FEATURE_VERSION_PARAMETER,
                                     this.adapter.getServiceVersion());
        parameters.addParameterShell(SOSmobileRequestBuilder.DOMAIN_FEATURE_ID, domainFeatureID);
        parameters.addParameterShell(ISOSRequestBuilder.DESCRIBE_SENSOR_OUTPUT_FORMAT,
                                     "text/xml;subtype=\"sensorML/1.0.1\"");

        return parameters;
    }

    /**
     * @return the requestState
     */
    public State getRequestState() {
        return this.requestState;
    }

    /**
     * @param requestState
     *        the requestState to set
     */
    public void setRequestState(State requestState) {
        this.requestState = requestState;
    }

    /**
     * 
     * @return
     */
    public boolean isRequestTaskBusy() {
        if (requestTask == null) {
            return false;
        }
        return !requestTask.isDone();
    }

    /**
     * 
     * @param e
     */
    public void itemStateChanged_domainFeatureComboBox(ItemEvent event) {
        System.out.println("item changed on real time tab: " + event);
        this.view.getDomainFeatureComboBox().removeAllItems();
        this.view.getDomainFeatureComboBox().setSelectedItem(null);
        this.view.setButtonStates(State.SELECTING);
    }

    /**
     * When the request window is closed, make sure that the background threads are shutdown cleanly
     */
    public void dialogWindowClosed() {
        this.threadPool.shutdownNow();
    }

    protected class GetServiceDescription implements Runnable {

        private Logger LOG = LoggingHandler.getLogger(GetServiceDescription.class);

        public void run() {
            LOG.info("starting request for service description");

            setRequestState(State.INITIALIZING_REQUEST);
            view.setButtonStates(getRequestState());

            try {
                setRequestState(State.WAITING_FOR_SERVICE);
                view.setButtonStates(getRequestState());

                serviceDesc = adapter.initService(serviceURL.toString());

                setRequestState(State.INITIALIZING_RESPONSE_DISPLAY);
                view.setButtonStates(getRequestState());

                initGUI();
            }
            catch (ExceptionReport e) {
                JOptionPane.showMessageDialog(view,
                                              "The request sended to the SOS produced a service sided exception. Please view the logs for details.",
                                              "Error",
                                              JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
            catch (OXFException e) {
                if (e.getCause() instanceof ConnectException) {
                    JOptionPane.showMessageDialog(view,
                                                  "Could not connect to service!",
                                                  "Error",
                                                  JOptionPane.ERROR_MESSAGE);
                }
                e.printStackTrace();
            }

            setRequestState(State.DONE);
            view.setButtonStates(getRequestState());
        } // end run()
    }

}
