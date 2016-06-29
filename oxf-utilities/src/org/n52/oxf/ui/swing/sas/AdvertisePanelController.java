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

import java.io.IOException;
import java.net.ConnectException;

import javax.swing.JOptionPane;

import org.n52.oxf.OXFException;
import org.n52.oxf.owsCommon.ExceptionReport;
import org.n52.oxf.owsCommon.capabilities.Operation;
import org.n52.oxf.serviceAdapters.OperationResult;
import org.n52.oxf.serviceAdapters.ParameterContainer;
import org.n52.oxf.serviceAdapters.sas.SASAdapter;
import org.n52.oxf.serviceAdapters.sas.SASRequestBuilder;
import org.n52.oxf.serviceAdapters.sos.SOSAdapter;
import org.n52.oxf.ui.swing.ShowRequestDialog;
import org.n52.oxf.ui.swing.ShowXMLDocDialog;
import org.n52.oxf.util.IOHelper;

/**
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public class AdvertisePanelController {

    private SASAdapter adapter;
    private AdvertisePanel view;
    private String serviceURL;

    public AdvertisePanelController(AdvertisePanel view, SASAdapter adapter, String serviceURL) {
        this.adapter = adapter;
        this.view = view;
        this.serviceURL = serviceURL;
    }

    public void actionPerformed_okButton() {
        try {
            ParameterContainer paramCon = SASRequestBuilder.buildAdvertiseParamCon(SASAdapter.SERVICE_TYPE,
                                                                                   SASAdapter.SUPPORTED_VERSIONS[0],
                                                                                   view.getCompNameTextField().getText(),
                                                                                   view.getPhenTextField().getText(),
                                                                                   view.getUomTextField().getText(),
                                                                                   view.getSmlTextField().getText());

            String postRequest = SASRequestBuilder.buildAdvertiseRequest(paramCon);
            int returnVal = new ShowRequestDialog(view, "Advertise Request", postRequest).showDialog();

            if (returnVal == ShowRequestDialog.APPROVE_OPTION) {
                OperationResult opResult = adapter.doOperation(new Operation(SASAdapter.ADVERTISE_OP_NAME,
                                                                             serviceURL + "?",
                                                                             serviceURL), paramCon);

                String describeSensorDoc = IOHelper.readText(opResult.getIncomingResultAsStream());

                new ShowXMLDocDialog(view.getLocation(), "Advertise Response", describeSensorDoc).setVisible(true);
            }
        }
        catch (OXFException e) {
            if (e.getCause() instanceof ConnectException) {
                JOptionPane.showMessageDialog(view, "Could not connect to service!", "Error", JOptionPane.ERROR_MESSAGE);
            }
            e.printStackTrace();
        }
        catch (ExceptionReport e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}