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
 
 Created on: 15.06.2006
 *********************************************************************************/

package org.n52.oxf.ui.swing.sos;

import java.io.IOException;
import java.net.ConnectException;
import java.net.URL;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.n52.oxf.OXFException;
import org.n52.oxf.owsCommon.ExceptionReport;
import org.n52.oxf.owsCommon.ServiceDescriptor;
import org.n52.oxf.owsCommon.capabilities.Operation;
import org.n52.oxf.owsCommon.capabilities.Parameter;
import org.n52.oxf.serviceAdapters.OperationResult;
import org.n52.oxf.serviceAdapters.ParameterContainer;
import org.n52.oxf.serviceAdapters.ParameterShell;
import org.n52.oxf.serviceAdapters.sos.ISOSRequestBuilder;
import org.n52.oxf.serviceAdapters.sos.SOSAdapter;
import org.n52.oxf.ui.swing.ApprovalDialog;
import org.n52.oxf.ui.swing.ShowRequestDialog;
import org.n52.oxf.ui.swing.ShowXMLDocDialog;
import org.n52.oxf.util.IOHelper;
import org.n52.oxf.util.LoggingHandler;
import org.n52.oxf.valueDomains.StringValueDomain;

/**
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public class RegisterSensor_ConfiguratorController {

    private static Logger LOGGER = LoggingHandler.getLogger(DescSensor_ConfiguratorController.class);

    public static String GET_CAPABILITIES_EVENT_TIME_PARAMETER = "eventTime";

    private RegisterSensor_Configurator view;

    private URL serviceURL;
    private SOSAdapter adapter;

    private ServiceDescriptor serviceDesc;

    /**
     * 
     */
    public RegisterSensor_ConfiguratorController(RegisterSensor_Configurator view, URL serviceURL, SOSAdapter adapter) {
        this.view = view;
        this.serviceURL = serviceURL;
        this.adapter = adapter;

        try {
            this.serviceDesc = adapter.initService(serviceURL.toString());
        }
        catch (ExceptionReport e) {
            JOptionPane.showMessageDialog(view,
                                          "The request sended to the SOS produced a service sided exception. Please view the logs for details.",
                                          "Error",
                                          JOptionPane.ERROR_MESSAGE);
            LOGGER.error("error initiating service", e);
        }
        catch (OXFException e) {
            if (e.getCause() instanceof ConnectException) {
                JOptionPane.showMessageDialog(view, "Could not connect to service!", "Error", JOptionPane.ERROR_MESSAGE);
            }
            LOGGER.error("error initiating service", e);
        }
        catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(view, "The request sended to the SOS produced a service sided exception.\n"
                    + e.getMessage() + "\nPlease view the logs for details.", "Error", JOptionPane.ERROR_MESSAGE);
            LOGGER.error("error initiating service", e);
        }

        init();
    }

    /**
     * 
     * 
     */
    public void init() {

        // init versionComboBox:
        //Parameter versionParameter = serviceDesc.getOperationsMetadata().getOperationByName(SOSAdapter.REGISTER_SENSOR).getParameter(ISOSRequestBuilder.REGISTER_SENSOR_VERSION_PARAMETER);
        //StringValueDomain versionVD = (StringValueDomain) versionParameter.getValueDomain();
        //for (String version : versionVD.getPossibleValues()) {
        //    view.getVersionComboBox().addItem(version);
        //}

        // init serviceComboBox:
        //Parameter serviceParameter = serviceDesc.getOperationsMetadata().getOperationByName(SOSAdapter.REGISTER_SENSOR).getParameter(ISOSRequestBuilder.REGISTER_SENSOR_SERVICE_PARAMETER);
        //StringValueDomain serviceVD = (StringValueDomain) serviceParameter.getValueDomain();
        //for (String service : serviceVD.getPossibleValues()) {
        //    view.getServiceComboBox().addItem(service);
        //}

        // init sensorIdComboBox:传感器的ID号
        //Parameter sensorIdParameter = serviceDesc.getOperationsMetadata().getOperationByName(SOSAdapter.REGISTER_SENSOR).getParameter(ISOSRequestBuilder.REGISTER_SENSOR_ID_PARAMETER);
        //StringValueDomain sensorIdVD = (StringValueDomain) sensorIdParameter.getValueDomain();
        //for (String sensorID : sensorIdVD.getPossibleValues()) {
        //    view.getSensorIdComboBox().addItem(sensorID);
        //}
    }

    public void actionPerformed_okButton() {
        try {
            String selectedVersion = (String) view.getVersionComboBox().getSelectedItem();
            String selectedServiceType = (String) view.getServiceComboBox().getSelectedItem();
            String selectedIdentification=(String)view.getIdentificationComboBox().getSelectedItem();  //获得identification
           // String selectedStatus=(String)view.getStatusComboBox().getSelectedItem();  //获得状态
            String selectedSensorID = (String) view.getSensorIdComboBox().getSelectedItem();  //获得传感器编号
            String selectedPostition=(String)view.getPositionComboBox().getSelectedItem();  //获得位置 
            String selectedFix=(String)view.getFixComboBox().getSelectedItem();  //获得是否为固定
            String selectedLatitude=(String)view.getLatitudeComboBox().getSelectedItem();  //获得纬度 
            String selectedLatitudeUom=(String)view.getLatitudeUomComboBox().getSelectedItem();  //获得纬度 单位
            String selectedLongitude=(String)view.getLongitudeComboBox().getSelectedItem();  //获得经度
            String selectedLongitudeUom=(String)view.getLongitudeUomComboBox().getSelectedItem();  //获得经度单位
            String selectedObserveProperty=(String)view.getObservedPropertyComboBox().getSelectedItem();  //获得观测属性 
            String selectedUom=(String)view.getUomComboBox().getSelectedItem();  //获得单位
            String selectedType=(String)view.getTypeComboBox().getSelectedItem();  //获得类型
            //String selectedStatus=(String)view.getStatusComboBox().getSelectedItem();  //获得状态
            
            ParameterContainer paramCon = new ParameterContainer();
            paramCon.addParameterShell(new ParameterShell(new Parameter(ISOSRequestBuilder.REGISTER_SENSOR_VERSION_PARAMETER,
                                                                        true,
                                                                        new StringValueDomain(SOSAdapter.SUPPORTED_VERSIONS),
                                                                        ISOSRequestBuilder.REGISTER_SENSOR_VERSION_PARAMETER),
                                                          selectedVersion));
            paramCon.addParameterShell(new ParameterShell(new Parameter(ISOSRequestBuilder.REGISTER_SENSOR_SERVICE_PARAMETER,
                                                                        true,
                                                                        new StringValueDomain(selectedServiceType),
                                                                        ISOSRequestBuilder.REGISTER_SENSOR_SERVICE_PARAMETER),
                                                          selectedServiceType)); 
            paramCon.addParameterShell(new ParameterShell(new Parameter(ISOSRequestBuilder.REGISTER_SENSOR_IDENTIFICATION_TYPE,
                    true,
                    new StringValueDomain(selectedIdentification),
                    ISOSRequestBuilder.REGISTER_SENSOR_IDENTIFICATION_TYPE),
                    selectedIdentification));       //添加的identification
            
           paramCon.addParameterShell(new ParameterShell(new Parameter(ISOSRequestBuilder.REGISTER_SENSOR_POSITION_NAME_PARAMETER,
                                                                        true,
                                                                        new StringValueDomain(selectedPostition),
                                                                        ISOSRequestBuilder.REGISTER_SENSOR_POSITION_NAME_PARAMETER),
                                                                        selectedPostition));  //添加位置
           
           paramCon.addParameterShell(new ParameterShell(new Parameter(ISOSRequestBuilder.REGISTER_SENSOR_ID_PARAMETER,
                   true,
                   new StringValueDomain(selectedSensorID),
                   ISOSRequestBuilder.REGISTER_SENSOR_ID_PARAMETER),
     selectedSensorID));
            
            paramCon.addParameterShell(new ParameterShell(new Parameter(ISOSRequestBuilder.REGISTER_SENSOR_POSITION_FIXED_PARAMETER,
                    true,
                    new StringValueDomain(selectedFix),
                    ISOSRequestBuilder.REGISTER_SENSOR_POSITION_FIXED_PARAMETER),
                    selectedFix));    //-------------------------------------------添加是否为固定
            
            paramCon.addParameterShell(new ParameterShell(new Parameter(ISOSRequestBuilder.REGISTER_SENSOR_LATITUDE_UOM_PARAMETER,
                    true,
                    new StringValueDomain(selectedLatitudeUom),
                    ISOSRequestBuilder.REGISTER_SENSOR_LATITUDE_UOM_PARAMETER),
                    selectedLatitudeUom));              //-------------------------------------------添加纬度单位
                    
            paramCon.addParameterShell(new ParameterShell(new Parameter(ISOSRequestBuilder.REGISTER_SENSOR_LATITUDE_POSITION_PARAMETER,
                    true,
                    new StringValueDomain(selectedLatitude),
                    ISOSRequestBuilder.REGISTER_SENSOR_LATITUDE_POSITION_PARAMETER),
                    selectedLatitude));              //-------------------------------------------添加纬度
            paramCon.addParameterShell(new ParameterShell(new Parameter(ISOSRequestBuilder.REGISTER_SENSOR_LONGTITUDE_UOM_PARAMETER,
                    true,
                    new StringValueDomain(selectedLongitudeUom),
                    ISOSRequestBuilder.REGISTER_SENSOR_LONGTITUDE_UOM_PARAMETER),
                    selectedLongitudeUom));          //-------------------------------------------添加经度单位
            paramCon.addParameterShell(new ParameterShell(new Parameter(ISOSRequestBuilder.REGISTER_SENSOR_LONGITUDE_POSITION_PARAMETER,
                    true,
                    new StringValueDomain(selectedLongitude),
                    ISOSRequestBuilder.REGISTER_SENSOR_LONGITUDE_POSITION_PARAMETER),
                    selectedLongitude));           //-------------------------------------------添加经度
            paramCon.addParameterShell(new ParameterShell(new Parameter(ISOSRequestBuilder.REGISTER_SENSOR_OBSERVED_PROPERTY_PARAMETER,
                    true,
                    new StringValueDomain(selectedObserveProperty),
                    ISOSRequestBuilder.REGISTER_SENSOR_OBSERVED_PROPERTY_PARAMETER),
                    selectedObserveProperty));           //-------------------------------------------添加观测属性
            paramCon.addParameterShell(new ParameterShell(new Parameter(ISOSRequestBuilder.REGISTER_SENSOR_UOM_PARAMETER,
                    true,
                    new StringValueDomain(selectedUom),
                    ISOSRequestBuilder.REGISTER_SENSOR_UOM_PARAMETER),
                    selectedUom));           //-------------------------------------------添加单位
            paramCon.addParameterShell(new ParameterShell(new Parameter(ISOSRequestBuilder.REGISTER_SENSOR_OBSERVATION_TYPE,
                    true,
                    new StringValueDomain(selectedType),
                    ISOSRequestBuilder.REGISTER_SENSOR_OBSERVATION_TYPE),
                    selectedType));           //-------------------------------------------添加类型
              
            
            paramCon.addParameterShell(ISOSRequestBuilder.DESCRIBE_SENSOR_OUTPUT_FORMAT,
                                       "text/xml;subtype=\"sensorML/1.0.1\"");
                                     

            String postRequest = adapter.getRequestBuilder().buildRegisterSensor(paramCon);
          
            int returnVal = new ShowRequestDialog(view, "DescribeSensor Request", postRequest).showDialog();

            if (returnVal == ApprovalDialog.APPROVE_OPTION) {
                OperationResult opResult = adapter.doOperation(new Operation(SOSAdapter.REGISTER_SENSOR,
                                                                             serviceURL.toString() + "?",
                                                                             serviceURL.toString()), paramCon);

                String describeSensorDoc = IOHelper.readText(opResult.getIncomingResultAsStream());

                new ShowXMLDocDialog(view.getLocation(), "DescribeSensor Response", describeSensorDoc).setVisible(true);
            }
        }
        catch (OXFException e) {
            if (e.getCause() instanceof ConnectException) {
                JOptionPane.showMessageDialog(view, "Could not connect to service!", "Error", JOptionPane.ERROR_MESSAGE);
            }
            e.printStackTrace();
        }
        catch (ExceptionReport e) {
            JOptionPane.showMessageDialog(view,
                                          "The request sended to the SOS produced a service sided exception. Please view the logs for details.",
                                          "Error",
                                          JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void actionPerformed_cancelButton() {
        view.dispose();
    }
}