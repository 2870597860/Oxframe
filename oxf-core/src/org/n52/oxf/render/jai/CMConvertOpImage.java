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
 
 Created on: 15.8.2005
 *********************************************************************************/

package org.n52.oxf.render.jai;

import java.awt.*;
import java.awt.image.*;
import java.util.*;
import javax.media.jai.*;
import org.apache.log4j.*;
import org.n52.oxf.util.*;

/**
 * 
 * @author <a href="mailto:jaeger@52north.org">Andreas Jaeger</a>
 */
class CMConvertOpImage extends PointOpImage {

    private final static Logger LOGGER = LoggingHandler.getLogger(CMConvertOpImage.class);
    
	private Color transColor;
	
    public CMConvertOpImage(ColorModel targetCM, Vector sources, RenderingHints rh) {
        super(
            (RenderedImage) sources.get(0),
            createImageLayout((RenderedImage) sources.get(0), targetCM),
            createConfiguration((RenderedImage) sources.get(0)),
            true
        );
    }

    private static ImageLayout createImageLayout(RenderedImage source, ColorModel targetCM) {
        return new ImageLayout(
            source.getMinX(),
            source.getMinY(),
            source.getWidth(),
            source.getHeight(),
            source.getTileGridXOffset(),
            source.getTileGridYOffset(),
            source.getTileWidth(),
            source.getTileHeight(),
            targetCM.createCompatibleSampleModel(source.getWidth(), source.getHeight()),
            targetCM
        );
    }

    private static Map createConfiguration(RenderedImage source) {
        return null;
    }

    /*########################################################################*/
    /*##########                  Computing pixels                 ###########*/
    /*########################################################################*/

    public void computeRect(Raster[] sources, WritableRaster dest, Rectangle destRect) {
        LOGGER.debug("Start computing region: " + destRect);
        ColorModel srcCM = getSourceImage(0).getColorModel();
        ColorModel destCM = getColorModel();
        Object srcData = null;
        Object destData = null;
        		
        for (int i = destRect.x + destRect.width; --i >= destRect.x;) {
            for (int j = destRect.y + destRect.height; --j >= destRect.y;) {
                srcData = sources[0].getDataElements(i, j, srcData);
                destData = destCM.getDataElements(srcCM.getRGB(srcData), destData);
      			dest.setDataElements(i, j, destData);
            }
        }
        LOGGER.debug("Region computed: " + destRect);
    }

}