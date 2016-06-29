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
 
 Created on: 30.01.2006
 *********************************************************************************/

package org.n52.oxf.feature;

import java.util.List;

import net.opengis.gml.DirectPositionType;
import net.opengis.gml.PointType;
import net.opengis.sampling.x00.StationDocument;
import net.opengis.sampling.x00.StationType;


import org.apache.log4j.Logger;
import org.n52.oxf.util.LoggingHandler;
import org.opengis.feature.DataType;
import org.opengis.feature.FeatureAttributeDescriptor;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

/**
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public class OXFStationType extends OXFAbstractFeatureType {

    private static Logger LOGGER = LoggingHandler.getLogger(OXFStationType.class);

    public static final String POSITION = "position";

    /**
     * 
     */
    public OXFStationType() {
        super();

        typeName = "OXFStationType";
        featureAttributeDescriptors = generateAttributeDescriptors();
    }

    
    public static OXFFeature create(StationDocument xb_stationDoc) {

        StationType xb_station = xb_stationDoc.getStation();

        String id = xb_station.getId();

        // FeatureType of the feature:
        OXFStationType oxf_stationType = new OXFStationType();

        OXFFeature feature = new OXFFeature(id, oxf_stationType);

        // initialize the feature with the attributes from the XMLBean:
        oxf_stationType.initializeFeature(feature, xb_station);

        return feature;
    }

    /**
     * 
     * @param doc
     * @return
     */
    protected List<FeatureAttributeDescriptor> generateAttributeDescriptors() {

        List<FeatureAttributeDescriptor> attributeDescriptors = super.generateAttributeDescriptors();

        OXFFeatureAttributeDescriptor position = new OXFFeatureAttributeDescriptor(POSITION,
                                                                                   DataType.OBJECT,
                                                                                   Point.class,
                                                                                   1,
                                                                                   1,
                                                                                   "");
        attributeDescriptors.add(position);

        return attributeDescriptors;
    }

    /**
     * 
     * @param xb_event
     *        shall be an instance of <code>OXFStationType</code>, taken form a concrete
     *        <code>StationDocument</code>.
     * @return
     */
    public void initializeFeature(OXFFeature feature,
                                  StationType xb_station) {
        super.initializeFeature(feature, xb_station);

        //
        // --- initialize the POSITION-attribute:

        // TODO Spec-Too-Flexible-Problem --> various geometry representations are possible:

        // until now just the "PointType"-geometry is supported:
        if (xb_station.getPosition() != null && xb_station.getPosition().getPoint() != null
                && ((PointType)xb_station.getPosition().getPoint()).getPos() != null) {

            PointType xb_point = xb_station.getPosition().getPoint();

            DirectPositionType xb_pos = xb_point.getPos();

            List coordsList = xb_pos.getListValue();

            double x = (Double) coordsList.get(0);
            double y = (Double) coordsList.get(1);
            double z = Double.NaN;

            if (coordsList.size() > 2) {
                z = (Double) coordsList.get(2);
            }

            Point point = new GeometryFactory().createPoint(new Coordinate(x, y, z));

            feature.setAttribute(POSITION, point);
            initializeFeaturesGeometry(feature, point);
        }

        // check whether the geometry-attribute was set: (could be set in this or the super class)
        if (feature.getGeometry() == null) {
            throw new IllegalArgumentException("The geometry attribute could not be initialized.");
        }
    }

}