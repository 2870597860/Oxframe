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
 
 Created on: 29.01.2006
 *********************************************************************************/

package org.n52.oxf.feature;

import com.vividsolutions.jts.geom.*;
import java.util.*;
import net.opengis.swe.x00.*;
import org.apache.log4j.*;
import org.n52.oxf.*;
import org.n52.oxf.feature.dataTypes.*;
import org.n52.oxf.owsCommon.capabilities.*;
import org.n52.oxf.util.*;
import org.opengis.feature.*;

/**
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public class OXFTruthObservationType extends OXFAbstractObservationType {

    private static Logger LOGGER = LoggingHandler.getLogger(OXFTruthObservationType.class);

    public static final String RESULT = "result";
    
    /**
     * 
     */
    public OXFTruthObservationType() {
        super();

        typeName = "OXFTruthObservationType";
        featureAttributeDescriptors = generateAttributeDescriptors();
    }

    /**
     * 
     */
    protected List<FeatureAttributeDescriptor> generateAttributeDescriptors() {

        List<FeatureAttributeDescriptor> attributeDescriptors = super.generateAttributeDescriptors();

        OXFFeatureAttributeDescriptor result = new OXFFeatureAttributeDescriptor(RESULT,
                                                                                 DataType.OBJECT,
                                                                                 Boolean.class,
                                                                                 1,
                                                                                 1,
                                                                                 "");
        attributeDescriptors.add(result);

        return attributeDescriptors;
    }

    /**
     * 
     * @param feature
     * @param nameValue
     * @param descriptionValue
     * @param locationValue
     * @param timeValue
     * @param procedureValue
     * @param observedPropertyValue
     * @param featureOfInterestValue
     * @param resultValue
     */
    public void initializeFeature(OXFFeature feature,
                                  String[] nameValue,
                                  String descriptionValue,
                                  Geometry locationValue,
                                  ITime timeValue,
                                  String procedureValue,
                                  OXFPhenomenonPropertyType observedPropertyValue,
                                  OXFFeature featureOfInterestValue,
                                  Boolean resultValue) {
        super.initializeFeature(feature,
                                nameValue,
                                descriptionValue,
                                locationValue,
                                timeValue,
                                procedureValue,
                                observedPropertyValue,
                                featureOfInterestValue);

        feature.setAttribute(RESULT, resultValue);

    }
}