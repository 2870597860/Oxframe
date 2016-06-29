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
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;

import net.opengis.sensorML.x101.HistoryDocument.History;

import org.apache.log4j.Logger;
import org.jfree.util.Log;
import org.n52.oxf.OXFException;
import org.n52.oxf.feature.OXFFeature;
import org.n52.oxf.feature.OXFFeatureCollection;
import org.n52.oxf.feature.sos.OXFSensorType;
import org.n52.oxf.feature.sos.SOSSensorStore;
import org.n52.oxf.layer.FeatureLayer;
import org.n52.oxf.layer.FeatureServiceLayer;
import org.n52.oxf.owsCommon.ExceptionReport;
import org.n52.oxf.owsCommon.OWSException;
import org.n52.oxf.owsCommon.ServiceDescriptor;
import org.n52.oxf.owsCommon.capabilities.ITime;
import org.n52.oxf.owsCommon.capabilities.Operation;
import org.n52.oxf.owsCommon.capabilities.Parameter;
import org.n52.oxf.render.sosMobile.RealTimeFeatureServiceLayer;
import org.n52.oxf.render.sosMobile.SensorPositionRenderer;
import org.n52.oxf.serviceAdapters.OperationResult;
import org.n52.oxf.serviceAdapters.ParameterContainer;
import org.n52.oxf.serviceAdapters.sos.ISOSRequestBuilder;
import org.n52.oxf.serviceAdapters.sos.SOSAdapter;
import org.n52.oxf.serviceAdapters.sosMobile.AbstractTemporalOpsBuilder;
import org.n52.oxf.serviceAdapters.sosMobile.ProcedureHistoryMember;
import org.n52.oxf.ui.swing.ApprovalDialog;
import org.n52.oxf.ui.swing.MapCanvas;
import org.n52.oxf.ui.swing.ShowRequestDialog;
import org.n52.oxf.ui.swing.ShowXMLDocDialog;
import org.n52.oxf.ui.swing.tree.ContentTree;
import org.n52.oxf.ui.swing.util.LayerAdder;
import org.n52.oxf.util.IOHelper;
import org.n52.oxf.util.LoggingHandler;
import org.n52.oxf.util.OXFEventException;
import org.n52.oxf.valueDomains.StringValueDomain;
import org.n52.oxf.valueDomains.time.ITimePeriod;
import org.n52.oxf.valueDomains.time.ITimePosition;
import org.n52.oxf.valueDomains.time.ITimeResolution;
import org.n52.oxf.valueDomains.time.TimeFactory;
import org.n52.oxf.valueDomains.time.TimePeriod;

import com.vividsolutions.jts.geom.Point;

/**
 * Controller for showing a sensor position at a time selected interatively from
 * existing points in time or a real time visualisation.
 * 
 * @author <a href="mailto:daniel.nuest@uni-muenster.de">Daniel Nüst</a>
 * 
 */
public class ShowSensorPosition_ConfiguratorController {

    private static final int THREAD_POOL_SIZE = 2;

    private static Logger LOGGER = LoggingHandler.getLogger(ShowSensorPosition_ConfiguratorController.class);

    // the threadpool for executing queries
    private ExecutorService threadPool;

    protected ShowSensorPosition_Configurator view;
    private ContentTree tree;
    private MapCanvas map;
    protected SOSAdapter adapter;
    protected ServiceDescriptor serviceDesc;
    protected URL serviceURL;
    protected HashMap<String, ProcedureHistoryMember> eventMap;
    private List<String> sensorIDList;
    private State requestState;

    private Future< ? > requestTask;

    /**
     * enumeration to keep track of the state of the requests initiated by the ShowSensorPosition_Configure
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

    /**
     * 
     * @param showSensorPositions_Configurator
     * @param serviceURL
     * @param adapter
     * @param map
     * @param tree
     */
    public ShowSensorPosition_ConfiguratorController(ShowSensorPosition_Configurator view,
                                                     URL serviceURL,
                                                     SOSAdapter adapter,
                                                     MapCanvas map,
                                                     ContentTree tree) {
        this.view = view;
        this.tree = tree;
        this.map = map;
        this.adapter = adapter;
        this.serviceURL = serviceURL;
        this.sensorIDList = new ArrayList<String>();

        this.requestState = State.NO_ACTIVE_REQUEST;
        threadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

        requestTask = threadPool.submit(new GetServiceDescription());
    }

    /**
     * @param e2
     * 
     * 
     */
    public void actionPerformed_addToMapButton(ActionEvent event, int tabId) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("addToMap action");
        }

        String sensorID = (String) this.view.getTimeInstantSensorsComboBox().getSelectedItem();

        switch (tabId) {
        case ShowSensorPosition_Configurator.TAB_REAL_TIME:
            Integer integerSeconds = (Integer) this.view.getRealTimeRefreshIntervallComboBox().getSelectedItem();
            long intervall = integerSeconds.longValue() * 1000;
            showRealTime(sensorID, intervall);
            break;
        case ShowSensorPosition_Configurator.TAB_TIME_INSTANT:
            String dateString = (String) this.view.getTimeInstantTimesComboBox().getSelectedItem();

            if (dateString != null && dateString.length() < 1) {
                JOptionPane.showMessageDialog(view, "Select a time!", "Error", JOptionPane.ERROR_MESSAGE);
                LOGGER.error("no time selected, cannot show time instant");
            }
            else {
                showTimeInstant(sensorID, dateString);
            }
            break;

        case ShowSensorPosition_Configurator.TAB_TIME_PERIOD:
            LOGGER.warn("time period not implemented for add to map button!");

            String startString = (String) this.view.getTimePeriodStartComboBox().getSelectedItem();
            String endString = (String) this.view.getTimePeriodEndComboBox().getSelectedItem();

            if ( (startString != null && startString.length() < 1) || (endString != null && endString.length() < 1)) {
                JOptionPane.showMessageDialog(view, "Select a start and end time!", "Error", JOptionPane.ERROR_MESSAGE);
                LOGGER.error("no time selected, cannot show time instant");
            }
            else {
                String timePeriodString = startString + "/" + endString;
                showTimePeriod(sensorID, timePeriodString);
            }

            break;

        }

        // view.dispose();
    }

    /**
     * 
     * @param e
     * @param tab_time_instant
     */
    public void actionPerformed_loadTimesButton(ActionEvent e, int tabId) {
        String sensorID = null;
        Vector<JComboBox> boxes = new Vector<JComboBox>();
        
        switch (tabId) {
        case ShowSensorPosition_Configurator.TAB_TIME_INSTANT:
            sensorID = (String) view.getTimeInstantSensorsComboBox().getSelectedItem();
            boxes.add(view.getTimeInstantTimesComboBox());
            break;
        case ShowSensorPosition_Configurator.TAB_TIME_PERIOD:
            sensorID = (String) view.getTimePeriodSensorsComboBox().getSelectedItem();
            boxes.add(view.getTimePeriodStartComboBox());
            boxes.add(view.getTimePeriodEndComboBox());
            break;
        default:
            break;
        }
        
        requestTask = this.threadPool.submit(new GetTimesForSensor(sensorID, boxes));
    }

    /**
     * 
     */
    public void actionPerformed_XmlRequestButton(ActionEvent event, int tabId) {

        switch (tabId) {
        case ShowSensorPosition_Configurator.TAB_TIME_INSTANT:

            String sensorID = (String) view.getTimeInstantSensorsComboBox().getSelectedItem();

            String selectedTimeString = (String) view.getTimeInstantTimesComboBox().getSelectedItem();
            ITime time = TimeFactory.createTime(selectedTimeString);

            ParameterContainer parameters;
            try {
                parameters = getBasicParameterContainer(sensorID);
                parameters.addParameterShell(AbstractTemporalOpsBuilder.TM_EQUALS_PARAMETER, time);
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
                    JOptionPane.showMessageDialog(view,
                                                  "Could not connect to service!",
                                                  "Error",
                                                  JOptionPane.ERROR_MESSAGE);
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

            break;

        case ShowSensorPosition_Configurator.TAB_REAL_TIME:
            LOGGER.info("xml request for real time request ...");

            break;
        } // end switch (tabId)
    }

    /**
     * 
     * @param e
     */
    public void itemStateChanged_sensorsComboBox(ItemEvent event, int tabId) {
        switch (tabId) {
        case ShowSensorPosition_Configurator.TAB_REAL_TIME:
            //
            System.out.println("item changed on real time tab: " + event);
            break;
        case ShowSensorPosition_Configurator.TAB_TIME_INSTANT:
            this.view.getTimeInstantTimesComboBox().removeAllItems();
            this.view.getTimeInstantTimesComboBox().setSelectedItem(null);
            break;
        }
        this.view.setButtonStates(State.SELECTING);
    }

    /**
     * 
     * @param e
     */
    public void itemStateChanged_timesComboBox(ItemEvent event) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("event unused: " + event);
        }
    }

    /**
     * 
     * @param e
     */
    public void actionPerfomed_intervallComboBox(ActionEvent e) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("intervallComboBox itemStateChanged: " + e);
        }
        System.out.println("intervallComboBox itemStateChanged: " + e);

        Object o = this.view.getRealTimeRefreshIntervallComboBox().getSelectedItem();
        if (o instanceof String) {
            String s = (String) o;
            if (s.length() == 0) {
                this.view.getRealTimeRefreshIntervallComboBox().setSelectedItem(null);
            }
            else {
                try {
                    Integer i = new Integer(s);
                    if (i.intValue() < 1) {
                        this.view.getRealTimeRefreshIntervallComboBox().setSelectedItem("");
                        this.view.getRealTimeRefreshIntervallComboBox().setSelectedItem(null);
                    }
                }
                catch (NumberFormatException ex) {
                    LOGGER.error("illegal input in intervall combo box, cannot parse Integer: " + s);
                    this.view.getRealTimeRefreshIntervallComboBox().setSelectedItem("");
                    this.view.getRealTimeRefreshIntervallComboBox().setSelectedItem(null);
                }

            }
        }
        else if (o instanceof Integer) {
            Integer i = (Integer) o;
            if (i.intValue() < 1) {
                this.view.getRealTimeRefreshIntervallComboBox().setSelectedItem("");
                this.view.getRealTimeRefreshIntervallComboBox().setSelectedItem(null);
            }
        }

        this.view.setButtonStates(State.SELECTING);
    }

    /**
     * 
     * @param sensorID
     * @param dateString
     */
    private void showTimePeriod(String sensorID, String dateString) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Show time period for " + sensorID + " @ " + dateString);
        }

        /** make nice id and title for the layer **/
        String layerID = getNiceLayerId(sensorID, dateString);
        String layerTitle = getNiceLayerTitle(sensorID, dateString);

        /** do request for necessary data **/
        requestTask = threadPool.submit(new GetFeatureCollectionForSensor(sensorID,
                                                                          dateString,
                                                                          AbstractTemporalOpsBuilder.TM_DURING_PARAMETER));
        OXFFeatureCollection featureCollection;
        try {
            featureCollection = (OXFFeatureCollection) requestTask.get();
        }
        catch (InterruptedException e1) {
            /** ignored on purpose **/
            LOGGER.error("interrupted...", e1);
            return;
        }
        catch (ExecutionException e) {
            if (e.getCause() instanceof ConnectException) {
                JOptionPane.showMessageDialog(view, "Could not connect to service!", "Error", JOptionPane.ERROR_MESSAGE);
                LOGGER.error("Error fetching sensor info:\n" + e.getMessage());
            }
            if (e.getCause() instanceof ExceptionReport) {
                ExceptionReport er = (ExceptionReport) e.getCause();
                JOptionPane.showMessageDialog(view,
                                              "The request sended to the SOS produced a service sided exception. Please view the logs for details.",
                                              "Error",
                                              JOptionPane.ERROR_MESSAGE);
                LOGGER.error("Error fetching sensor info:\n" + er.countExceptions() + " exceptions in ExceptionReport",
                             e);
                Iterator<OWSException> iter = er.getExceptionsIterator();
                while (iter.hasNext()) {
                    System.out.println(Arrays.toString(iter.next().getExceptionTexts()));
                }
            }
            return;
        }

        /** create simple feature layer **/
        SensorPositionRenderer renderer = new SensorPositionRenderer();
        FeatureLayer layer = new FeatureLayer(layerID,
                                              layerTitle,
                                              this.serviceDesc.getServiceIdentification().getTitle(),
                                              this.serviceDesc.getServiceIdentification().getServiceType(),
                                              renderer,
                                              renderer,
                                              featureCollection);

        try {
            LayerAdder.addLayer(this.map, this.tree, layer);
        }
        catch (OXFException e) {
            e.printStackTrace();
        }
        catch (OXFEventException e) {
            e.printStackTrace();
        }
    }

    /**
     * 
     * @param sensorID
     * @param dateString
     */
    private void showTimeInstant(String sensorID, String dateString) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Show time instant for " + sensorID + " @ " + dateString);
        }

        /** make nice id and title for the layer **/
        String layerID = getNiceLayerId(sensorID, dateString);
        String layerTitle = getNiceLayerTitle(sensorID, dateString);

        /** do request for necessary data **/
        requestTask = threadPool.submit(new GetFeatureCollectionForSensor(sensorID,
                                                                          dateString,
                                                                          AbstractTemporalOpsBuilder.TM_EQUALS_PARAMETER));
        OXFFeatureCollection featureCollection;
        try {
            featureCollection = (OXFFeatureCollection) requestTask.get();
        }
        catch (InterruptedException e1) {
            /** ignored on purpose **/
            LOGGER.error("interrupted...", e1);
            return;
        }
        catch (ExecutionException e) {
            if (e.getCause() instanceof ConnectException) {
                JOptionPane.showMessageDialog(view, "Could not connect to service!", "Error", JOptionPane.ERROR_MESSAGE);
                LOGGER.error("Error fetching sensor info:\n" + e.getMessage());
            }
            if (e.getCause() instanceof ExceptionReport) {
                ExceptionReport er = (ExceptionReport) e.getCause();
                JOptionPane.showMessageDialog(view,
                                              "The request sended to the SOS produced a service sided exception. Please view the logs for details.",
                                              "Error",
                                              JOptionPane.ERROR_MESSAGE);
                LOGGER.error("Error fetching sensor info:\n" + er.countExceptions() + " exceptions in ExceptionReport",
                             e);
                Iterator<OWSException> iter = er.getExceptionsIterator();
                while (iter.hasNext()) {
                    System.out.println(Arrays.toString(iter.next().getExceptionTexts()));
                }
            }
            return;
        }

        /** create simple feature layer **/
        SensorPositionRenderer renderer = new SensorPositionRenderer();
        FeatureLayer layer = new FeatureLayer(layerID,
                                              layerTitle,
                                              this.serviceDesc.getServiceIdentification().getTitle(),
                                              this.serviceDesc.getServiceIdentification().getServiceType(),
                                              renderer,
                                              renderer,
                                              featureCollection);

        try {
            LayerAdder.addLayer(this.map, this.tree, layer);
        }
        catch (OXFException e) {
            e.printStackTrace();
        }
        catch (OXFEventException e) {
            e.printStackTrace();
        }
    }

    /**
     * 
     * @param sensorID
     * @param dateString
     */
    @SuppressWarnings("unused")
    private void showTimeInstantServiceLayer(String sensorID, String dateString) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Show time instant (with service layer) for " + sensorID + " @ " + dateString);
        }
        ITime time = TimeFactory.createTime(dateString);

        ParameterContainer parameters;
        try {
            parameters = getBasicParameterContainer(sensorID);
            parameters.addParameterShell(AbstractTemporalOpsBuilder.TM_EQUALS_PARAMETER, time);
        }
        catch (OXFException e) {
            JOptionPane.showMessageDialog(view,
                                          "Could not create request parameters. Please view the logs for details.",
                                          "Error",
                                          JOptionPane.ERROR_MESSAGE);
            LOGGER.error("error creating parameter container: ", e);
            return;
        }

        /** make nice id and title for the layer **/
        String layerID = getNiceLayerId(sensorID, dateString);
        String layerTitle = getNiceLayerTitle(sensorID, dateString);

        SOSSensorStore sensorStore = new SOSSensorStore();
        SensorPositionRenderer renderer = new SensorPositionRenderer();
        FeatureServiceLayer layer = new FeatureServiceLayer(adapter,
                                                            renderer,
                                                            sensorStore,
                                                            renderer,
                                                            this.serviceDesc,
                                                            parameters,
                                                            layerID,
                                                            layerTitle,
                                                            SOSAdapter.DESCRIBE_SENSOR,
                                                            true);

        try {
            LayerAdder.addLayer(this.map, this.tree, layer);
        }
        catch (OXFException e) {
            e.printStackTrace();
        }
        catch (OXFEventException e) {
            e.printStackTrace();
        }
    }

    private void showRealTime(String sensorID, long intervall) {
        LOGGER.info("Show real time visualization of " + sensorID + " every " + intervall + " seconds.");

        ParameterContainer parameters;
        try {
            parameters = getBasicParameterContainer(sensorID);
        }
        catch (OXFException e) {
            JOptionPane.showMessageDialog(view,
                                          "Could not create request parameters. Please view the logs for details.",
                                          "Error",
                                          JOptionPane.ERROR_MESSAGE);
            LOGGER.error("error creating parameter container: ", e);
            return;
        }

        /** make nice id and title for the layer **/
        String layerID = getNiceLayerId(sensorID, "real-time");
        String layerTitle = getNiceLayerTitle(sensorID, "real-time");

        SOSSensorStore sensorStore = new SOSSensorStore();
        SensorPositionRenderer renderer = new SensorPositionRenderer();
        RealTimeFeatureServiceLayer layer = new RealTimeFeatureServiceLayer(adapter,
                                                                            renderer,
                                                                            sensorStore,
                                                                            renderer,
                                                                            this.serviceDesc,
                                                                            parameters,
                                                                            layerID,
                                                                            layerTitle,
                                                                            SOSAdapter.DESCRIBE_SENSOR,
                                                                            intervall);
        RealTimeLayerRefresh_ConfiguratorAndController controller = new RealTimeLayerRefresh_ConfiguratorAndController(this.view, layer);
        controller.setVisible(true);

        try {
            LayerAdder.addLayer(this.map, this.tree, layer);
        }
        catch (OXFException e) {
            e.printStackTrace();
        }
        catch (OXFEventException e) {
            e.printStackTrace();
        }
    }

    /**
     * 
     * @param sensorID
     * @param dateString
     * @return
     */
    private String getNiceLayerTitle(String sensorID, String dateString) {
        String layerTitle;
        int maxTitleLength = 20;
        if (sensorID.length() > maxTitleLength) {
            layerTitle = "..." + sensorID.substring(sensorID.length() - maxTitleLength, sensorID.length()) + " @ "
                    + dateString;
        }
        else {
            layerTitle = sensorID + " @ " + dateString;
        }
        return layerTitle;
    }

    /**
     * 
     * @param sensorID
     * @param dateString
     * @return
     */
    private String getNiceLayerId(String sensorID, String dateString) {
        String layerID;
        int i = sensorID.lastIndexOf(":");
        if (i != -1) {
            layerID = sensorID.substring(i + 1, sensorID.length()) + "_" + (System.currentTimeMillis() / 100);
        }
        else {
            layerID = sensorID + "@" + dateString;
        }
        return layerID;
    }

    /**
     * 
     * @param sensorID
     * @return
     * @throws OXFException
     */
    protected ParameterContainer getBasicParameterContainer(String sensorID) throws OXFException {
        ParameterContainer parameters = new ParameterContainer();
        parameters.addParameterShell(ISOSRequestBuilder.DESCRIBE_SENSOR_SERVICE_PARAMETER,
                                     this.adapter.getServiceType());

        parameters.addParameterShell(ISOSRequestBuilder.DESCRIBE_SENSOR_VERSION_PARAMETER,
                                     this.adapter.getServiceVersion());
        parameters.addParameterShell(ISOSRequestBuilder.DESCRIBE_SENSOR_PROCEDURE_PARAMETER, sensorID);
        parameters.addParameterShell(ISOSRequestBuilder.DESCRIBE_SENSOR_OUTPUT_FORMAT,
                                     "text/xml;subtype=\"sensorML/1.0.1\"");

        return parameters;
    }

    /**
     * @return the sensorIDList
     */
    public List<String> getSensorIDList() {
        return this.sensorIDList;
    }

    /**
     * Inner classes representing the tasks to be done by the background thread i.e. those that involve
     * querying services
     */

    /**
     * When the request window is closed, make sure that the background threads are shutdown cleanly
     */
    public void dialogWindowClosed() {
        this.threadPool.shutdownNow();
    }

    /**
     * 
     * @author d_nues01
     * 
     */
    protected class GetFeatureCollectionForSensor implements Callable<OXFFeatureCollection> {

        private Logger LOG = LoggingHandler.getLogger(GetTimesForSensor.class);
        private String sensorID;
        private String dateString;
        private String timeParameter;

        public GetFeatureCollectionForSensor(String sensorID, String dateString, String timeParameter) {
            this.sensorID = sensorID;
            this.dateString = dateString;
            this.timeParameter = timeParameter;
        }

        @Override
        public OXFFeatureCollection call() throws OXFException, ExceptionReport {
            LOG.info("starting request for sensor times");

            setRequestState(State.INITIALIZING_REQUEST);
            view.setButtonStates(getRequestState());

            ITime time = TimeFactory.createTime(dateString);

            ParameterContainer parameters = new ParameterContainer();

            parameters = getBasicParameterContainer(sensorID);
            parameters.addParameterShell(timeParameter, time);

            setRequestState(State.WAITING_FOR_SERVICE);
            view.setButtonStates(getRequestState());
            OperationResult opResult = adapter.doOperation(new Operation(SOSAdapter.DESCRIBE_SENSOR,
                                                                         serviceURL.toString() + "?",
                                                                         serviceURL.toString()), parameters);

            setRequestState(State.INITIALIZING_RESPONSE_DISPLAY);
            view.setButtonStates(getRequestState());

            SOSSensorStore sensorStore = new SOSSensorStore();

            // TODO use this direct call to a new function that uses the history rather than the actual
            // position!
            OXFFeatureCollection featureCollection = sensorStore.unmarshalFeaturesFromHistory(opResult);

            if (featureCollection.isEmpty()) {
                LOG.error("error requesting sensor information!");
                throw new OXFException("feature collection ist empty for opResult: " + opResult);
            }
            if (featureCollection.size() > 1) {
                LOG.warn("only one element from featureCollecion is processed further!");
            }

            setRequestState(State.DONE);
            view.setButtonStates(getRequestState());

            return featureCollection;
        }
    }

    /**
     * 
     * @author d_nues01
     * 
     */
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

    /**
     * 
     * @author d_nues01
     * 
     */
    protected class GetTimesForSensor implements Runnable {

        private String sensorID;
        private Collection<JComboBox> combos;

        /**
         * 
         * @param sensorID
         */
        public GetTimesForSensor(String sensorID, Collection<JComboBox> combos) {
            this.sensorID = sensorID;
            this.combos = combos;
        }

        private Logger LOG = LoggingHandler.getLogger(GetTimesForSensor.class);

        /**
         * method also updates infotext field as a side effect.
         */
        public void run() {
            LOG.info("starting request for sensor times");

            setRequestState(State.INITIALIZING_REQUEST);
            view.setButtonStates(getRequestState());

            List<String> timesList = new ArrayList<String>();
            eventMap = new HashMap<String, ProcedureHistoryMember>();

            // String sensorID = (String) view.getTimeInstantSensorsComboBox().getSelectedItem();

            ParameterContainer parameters = new ParameterContainer();

            try {
                parameters.addParameterShell(ISOSRequestBuilder.DESCRIBE_SENSOR_SERVICE_PARAMETER,
                                             adapter.getServiceType());
                parameters.addParameterShell(ISOSRequestBuilder.DESCRIBE_SENSOR_VERSION_PARAMETER,
                                             adapter.getServiceVersion());
                parameters.addParameterShell(ISOSRequestBuilder.DESCRIBE_SENSOR_PROCEDURE_PARAMETER, sensorID);
                parameters.addParameterShell(ISOSRequestBuilder.DESCRIBE_SENSOR_OUTPUT_FORMAT,
                                             "text/xml;subtype=\"sensorML/1.0.1\"");

                Date now = new Date();
                String nowDateString = TimeFactory.ISO8601LocalFormat.format(now);
                ITime time = TimeFactory.createTime(nowDateString);

                parameters.addParameterShell(AbstractTemporalOpsBuilder.TM_BEFORE_PARAMETER, time);

                setRequestState(State.WAITING_FOR_SERVICE);
                view.setButtonStates(getRequestState());
                OperationResult opResult = adapter.doOperation(new Operation(SOSAdapter.DESCRIBE_SENSOR,
                                                                             serviceURL.toString() + "?",
                                                                             serviceURL.toString()), parameters);

                setRequestState(State.INITIALIZING_RESPONSE_DISPLAY);
                view.setButtonStates(getRequestState());

                SOSSensorStore sensorStore = new SOSSensorStore();
                OXFFeatureCollection featureCollection = sensorStore.unmarshalFeatures(opResult);

                if (featureCollection.isEmpty()) {
                    LOG.error("error requesting sensor information!");
                    throw new OXFException("feature collection ist empty for opResult: " + opResult);
                }
                if (featureCollection.size() > 1) {
                    LOG.warn("only one element from featureCollecion is processed further!");
                }

                OXFFeature sensorFeature = featureCollection.getFeature(sensorID);

                if (sensorFeature == null) {
                    throw new OXFException("no feature for that sensor id found!");
                }

                OXFSensorType sensorType = (OXFSensorType) sensorFeature.getFeatureType();

                /** add events from history **/
                if (sensorType.hasAttribute(OXFSensorType.HISTORY)) {
                    History[] history = (History[]) sensorFeature.getAttribute(OXFSensorType.HISTORY);

                    // get all event dates
                    for (History h : history) {
                        net.opengis.sensorML.x101.EventListDocument.EventList.Member[] eventListMembers = h.getEventList().getMemberArray();

                        for (net.opengis.sensorML.x101.EventListDocument.EventList.Member m : eventListMembers) {
                            ProcedureHistoryMember historyEvent = new ProcedureHistoryMember(m);
                            Date d = historyEvent.getDate();
                            String s = TimeFactory.ISO8601LocalFormat.format(d);
                            eventMap.put(s, historyEvent);
                            // boolean active = historyEvent.isActive();
                            // boolean mobile = historyEvent.isMobile();
                            timesList.add(s);
                        }
                    }
                }
                else {
                    Log.warn("No history found!");
                }

                /** create event for current position **/
                Point actualPosition = (Point) sensorFeature.getAttribute(OXFSensorType.POSITION);
                Boolean active = (Boolean) sensorFeature.getAttribute(OXFSensorType.ACTIVE);
                Boolean mobile = (Boolean) sensorFeature.getAttribute(OXFSensorType.MOBILE);

                ProcedureHistoryMember currentPosition = new ProcedureHistoryMember(sensorID,
                                                                                    actualPosition,
                                                                                    active.booleanValue(),
                                                                                    mobile.booleanValue(),
                                                                                    now);
                eventMap.put(nowDateString, currentPosition);
                timesList.add(nowDateString);

                for (JComboBox box : this.combos) {
                    for (String t : timesList) {
                        box.addItem(t);
                    }
                }

                setRequestState(State.DONE);
                view.setButtonStates(getRequestState());
            }
            catch (OXFException e) {
                if (e.getCause() instanceof ConnectException) {
                    JOptionPane.showMessageDialog(view,
                                                  "Could not connect to service!",
                                                  "Error",
                                                  JOptionPane.ERROR_MESSAGE);
                }
                LOG.error("Error fetching sensor info:\n" + e.getMessage());

            }
            catch (ExceptionReport e) {
                JOptionPane.showMessageDialog(view,
                                              "The request sended to the SOS produced a service sided exception. Please view the logs for details.",
                                              "Error",
                                              JOptionPane.ERROR_MESSAGE);
                LOG.error("Error fetching sensor info:\n" + e.countExceptions() + " exceptions in ExceptionReport", e);
                Iterator<OWSException> iter = e.getExceptionsIterator();
                while (iter.hasNext()) {
                    System.out.println(Arrays.toString(iter.next().getExceptionTexts()));
                }
            }
        } // end run()
    }

    /**
     * sets values in the gui according to the service description
     * 
     */
    public void initGUI() {
        Operation operation = this.serviceDesc.getOperationsMetadata().getOperationByName(SOSAdapter.DESCRIBE_SENSOR);
        Parameter sensorIdParameter = operation.getParameter(ISOSRequestBuilder.DESCRIBE_SENSOR_PROCEDURE_PARAMETER);
        StringValueDomain sensorIdVD = (StringValueDomain) sensorIdParameter.getValueDomain();
        this.view.setTitle("Sensor Postitions from " + this.serviceDesc.getServiceIdentification().getTitle());

        for (String sensorID : sensorIdVD.getPossibleValues()) {
            this.sensorIDList.add(sensorID);
            this.view.getTimeInstantSensorsComboBox().addItem(sensorID);
            this.view.getRealTimeSensorsComboBox().addItem(sensorID);
            this.view.getTimePeriodSensorsComboBox().addItem(sensorID);
        }
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
     * method sums up all possible extends to one period, i.e. the last of all elements will be the end and
     * the start vice versa.
     * 
     * important note: the resolution is set to the last occuring resolution: TODO fix via implementing
     * {@link Comparable} for {@link ITimeResolution}
     * 
     * @param list
     * @return
     */
    @SuppressWarnings("unused")
    private ITimePeriod aggregateTemporalExtend(List<ITime> list) {
        // TODO this method is not properly tested with all possible cases!

        ITimePeriod elementTimePeriod = null;
        ITimePosition elementTimePos = null;
        ITimePosition currentStart = null;
        ITimePosition currentEnd = null;
        ITimeResolution currentResolution = null;

        // get valid start and end ITime-elements
        ITime first = list.get(0);
        if (first instanceof ITimePeriod) {
            ITimePeriod temp = (ITimePeriod) first;
            currentStart = temp.getStart();
            currentEnd = temp.getEnd();
            temp = null;
        }
        else if (first instanceof ITimePosition) {
            ITimePosition temp = (ITimePosition) first;
            currentStart = temp;
            currentEnd = temp;
        }
        else {
            throw new IllegalArgumentException("Given list contains illegal element, only ITimePosition and ITimePeriod are allowed.");
        }
        first = null;

        // aggregate
        for (ITime element : list) {
            if (element instanceof ITimePosition) {
                elementTimePos = (ITimePosition) element;
                if (elementTimePos.before(currentStart)) {
                    currentStart = elementTimePos;
                }
                if (elementTimePos.after(currentEnd)) {
                    currentStart = elementTimePos;
                }
            }
            else if (element instanceof ITimePeriod) {
                elementTimePeriod = (ITimePeriod) element;

                currentResolution = elementTimePeriod.getResolution();

                if (currentStart.after(elementTimePeriod.getStart())) {
                    currentStart = elementTimePeriod.getStart();
                }
                if (currentEnd.before(elementTimePeriod.getEnd())) {
                    currentEnd = elementTimePeriod.getEnd();
                }
            }
        }

        ITimePeriod aggregation = new TimePeriod(currentStart, currentEnd, currentResolution);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("aggregated time to " + aggregation.toISO8601Format());
        }
        return aggregation;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ShowSensorPositions_ConfiguratorController");
        return sb.toString();
    }

}
