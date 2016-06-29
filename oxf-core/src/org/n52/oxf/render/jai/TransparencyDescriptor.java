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
import java.awt.image.renderable.*;
import javax.media.jai.*;
import javax.media.jai.registry.*;
import org.apache.log4j.*;
import org.n52.oxf.util.*;

/**
 * 
 * @author <a href="mailto:jaeger@52north.org">Andreas Jaeger</a>
 */
public class TransparencyDescriptor extends OperationDescriptorImpl implements RenderedImageFactory {

    private final static Logger LOGGER = LoggingHandler.getLogger(TransparencyDescriptor.class);
    
    private final static String PRODUCT = "52north OX-Framework";

    private final static String[][] resources = {
        {"GlobalName",  "52nTransparency"},
        {"LocalName",   "52nTransparency"},
        {"Vendor",	  	"org.52n"},
        {"Description", "An operation that adds transparency to images with opaque color models"},
        {"DocURL",	  	"http://www.52north.org"},
        {"Version",	 	"1.0"},
        {"arg0Desc",	"Implied transparent color"}
    };

    private final static String[] supportedModes = {
        "rendered"
    };

    private final static String[] paramNames = {
        "srcTransColor"
    };

    private final static Class[] paramClasses = {
        Color.class
    };

    private final static Object[] paramDefaults = {
        Color.white
    };

    private final static Object[] validParamValues = {
        null
    };

    private static TransparencyDescriptor descriptor;

    public synchronized static void register() {
        try {
            if (descriptor != null) {
                return;
            }
            descriptor = new TransparencyDescriptor();
            OperationRegistry or = JAI.getDefaultInstance().getOperationRegistry();
            or.registerDescriptor(descriptor);
            RIFRegistry.register(or, resources[0][1], PRODUCT, descriptor);
        } catch (IllegalArgumentException e) {
            LOGGER.warn("Unable to register descriptor");
        }
    }

    public synchronized static void unregister() {
        try {
            if (descriptor == null) {
                return;
            }
            OperationRegistry or = JAI.getDefaultInstance().getOperationRegistry();
            or.unregisterDescriptor(descriptor);
            RIFRegistry.unregister(or, resources[0][1], PRODUCT, descriptor);
            descriptor = null;
        } catch (IllegalArgumentException e) {
            LOGGER.warn("Unable to register descriptor");
        }
    }

    private TransparencyDescriptor() {
        super(resources, supportedModes, 1, paramNames, paramClasses, paramDefaults, validParamValues);
    }

    public RenderedImage create(ParameterBlock args, RenderingHints rh) {
        StringBuffer msg = new StringBuffer(100);
        if (! validateArguments("rendered", args, msg)) {
            return null;
        }
        return new TransparencyOpImage((Color) args.getObjectParameter(0), args.getSources(), rh);
    }

}