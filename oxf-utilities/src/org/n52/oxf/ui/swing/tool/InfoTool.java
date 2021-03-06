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
 
 Created on: 11.08.2005
 *********************************************************************************/

package org.n52.oxf.ui.swing.tool;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.util.Random;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import org.apache.log4j.Logger;
import org.n52.oxf.context.ContextBoundingBox;
import org.n52.oxf.context.LayerContext;
import org.n52.oxf.feature.OXFFeature;
import org.n52.oxf.feature.OXFFeatureCollection;
import org.n52.oxf.layer.IContextLayer;
import org.n52.oxf.layer.IFeatureLayer;
import org.n52.oxf.ui.swing.MapCanvas;
import org.n52.oxf.ui.swing.icons.IconAnchor;
import org.n52.oxf.ui.swing.ses.SesInfoFrame;
import org.n52.oxf.util.LoggingHandler;

/**
 * 
 * 
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public class InfoTool extends MapTool {

    private static Logger LOGGER = LoggingHandler.getLogger(InfoTool.class);

    /**
     * 
     */
    public InfoTool(JFrame owner, MapCanvas map) {
        super(owner, map);

        setIcon(new ImageIcon(IconAnchor.class.getResource("info.gif")));
        setToolTipText("Info");
    }

    public void activate() {
        super.activate();
    }

    public void mousePressed(MouseEvent evt) {
        createInfo(evt);
    }

    /**
     * 
     * @param evt
     */
    private void createInfo(MouseEvent event) {
        LayerContext context = map.getLayerContext();
        try {
        	Point screenPoint = new Point(event.getX(), event.getY());
            Point2D realPoint = ContextBoundingBox.screen2Realworld(context.getContextBoundingBox().getActualBBox(),
                                                                    map.getWidth(),
                                                                    map.getHeight(),
                                                                    screenPoint);

            LOGGER.info("clicked on position:  screen:(" + screenPoint.getX() + ","
                    + screenPoint.getY() + ")  realworld:(" + realPoint.getX() + ","
                    + realPoint.getY() + ")");
        }
        catch (NoninvertibleTransformException e) {
            LOGGER.error(e, e);
        }

        for (int i = 0; i < context.getLayerCount(); i++) {
            IContextLayer layer = context.get(i);

            if (layer instanceof IFeatureLayer) {
                IFeatureLayer featureLayer = (IFeatureLayer) layer;
                
                Set<OXFFeature> pickedFeatures = featureLayer.pickFeature(event.getX(),
                                                                          event.getY());

                if (pickedFeatures != null) {
                    LOGGER.info("pickedFeatures.size: " + pickedFeatures.size());

                    OXFFeatureCollection collection = null;
                    Random random = new Random();
                    int id = random.nextInt(1000000);
                    for (OXFFeature feature : pickedFeatures) {
                        LOGGER.info(feature.getID());
                        if (featureLayer.getTitle().equals("SES")) {
                        	if(collection == null){
                        		collection = new OXFFeatureCollection("id_"+id, feature.getFeatureType());
                        	}
                        	collection.add(feature);	
    					}
                    }
                    if(collection != null){
                    	 SesInfoFrame.showSesInfoFrame(collection);
                    }
                }
                else {
                    LOGGER.info("no FeaturePicker defined for the selected Layer.");
                }
            }
            
        }
    }
}