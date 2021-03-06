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
 
 Created on: 21.07.2007
 *********************************************************************************/

package org.n52.oxf.feature;

import java.util.List;

import org.n52.wv.WVStationDocument;
import org.n52.wv.WVStationType;
import org.n52.wv.WVStationType.HostedProcedure;
import org.opengis.feature.DataType;
import org.opengis.feature.FeatureAttributeDescriptor;

/**
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public class OXFWVStationType extends OXFStationType {

    public static final String HOSTED_PROCEDURES = "hostedProcedures";

    /**
     * 
     */
    public OXFWVStationType() {
        super();

        typeName = "OXFWVStationType";
        featureAttributeDescriptors = generateAttributeDescriptors();
    }

    public static OXFFeature create(WVStationDocument xb_wvStationDoc) {

        WVStationType xb_wvStation = xb_wvStationDoc.getWVStation();

        String id = xb_wvStation.getId();

        // FeatureType of the feature:
        OXFWVStationType oxf_wvStationType = new OXFWVStationType();

        OXFFeature feature = new OXFFeature(id, oxf_wvStationType);

        // initialize the feature with the attributes from the XMLBean:
        oxf_wvStationType.initializeFeature(feature, xb_wvStation);

        return feature;
    }

    /**
     * 
     * @param doc
     * @return
     */
    protected List<FeatureAttributeDescriptor> generateAttributeDescriptors() {

        List<FeatureAttributeDescriptor> attributeDescriptors = super.generateAttributeDescriptors();

        OXFFeatureAttributeDescriptor hostedProcedures = new OXFFeatureAttributeDescriptor(HOSTED_PROCEDURES,
                                                                                           DataType.OBJECT,
                                                                                           HostedProcedure[].class,
                                                                                           1,
                                                                                           Integer.MAX_VALUE,
                                                                                           "");
        attributeDescriptors.add(hostedProcedures);

        return attributeDescriptors;
    }

    /**
     * 
     */
    public void initializeFeature(OXFFeature feature,
                                  WVStationType xb_wvStation) {
        super.initializeFeature(feature, xb_wvStation);
        
        //
        // --- initialize the HOSTED_PROCEDURS-attribute:

        HostedProcedure[] xb_hostedProcArray = xb_wvStation.getHostedProcedureArray();
        feature.setAttribute(HOSTED_PROCEDURES, xb_hostedProcArray);
    }
}