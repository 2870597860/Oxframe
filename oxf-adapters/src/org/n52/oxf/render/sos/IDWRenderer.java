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
 
 Created on: 01.07.2006
 *********************************************************************************/

package org.n52.oxf.render.sos;

import com.vividsolutions.jts.geom.*;
import java.util.*;
import org.n52.oxf.feature.*;
import org.n52.oxf.feature.dataTypes.*;

/**
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public class IDWRenderer extends InterpolationRenderer {

	//In case of not setting maxDistance use all
	private double maxInvDistance = 0;
	
    public Double computeInterpolatedValue(Coordinate coordToInterpolate,
                                           List<OXFFeature> observationList) {
        double result = 0;
        double distanceSum = 0;

        for (OXFFeature observation : observationList) {
            // TODO: the location of the FOI (associated with the observation) will be taken and not the
            // Location of the observation itself.
            if (observation.getAttribute(OXFAbstractObservationType.FEATURE_OF_INTEREST) != null) {
                OXFFeature featureOfInterest = (OXFFeature) observation.getAttribute(OXFAbstractObservationType.FEATURE_OF_INTEREST);

                // TODO Spec-Too-Flexible-Problem --> various GeometryTypes are possible:
                if (featureOfInterest.getGeometry() instanceof Point) {
                    Point foiPoint = (Point) featureOfInterest.getGeometry();

                    Coordinate coordinateOfFOI = foiPoint.getCoordinate();

                    double tmpInvDistance = 1 / coordToInterpolate.distance(coordinateOfFOI);
                    //Only use Points with distance < MaxDistance
                    if(maxInvDistance<tmpInvDistance){
	                    double tmpValue = Math.pow(tmpInvDistance, 3);
	                    distanceSum += tmpValue;
	
	                    // TODO Spec-Too-Flexible-Problem --> various FeatureTypes are possible:
	                    if (observation.getFeatureType() instanceof org.n52.oxf.feature.OXFMeasurementType) {
	                        OXFMeasureType measureResult = (OXFMeasureType) observation.getAttribute(OXFMeasurementType.RESULT);
	                        double measurement = measureResult.getValue();
	
	                        result += tmpValue * measurement;
	                    }
                    }
                }
            }
        }

        return result / distanceSum;
    }
    
    /**
     * @return a plain text description of this Renderer.
     */
    public String getDescription() {
        return "IDWRenderer - visualizes an isoline map for the selected phenomenon";
    }
    
    public String toString() {
        return getDescription();
    }
    /**
     * 
     * @param maxDistance Maximum distance between interpolatedpoint and datapoint to use it in interpolation 
     */
    public void setMaxDistance(double maxDistance){
    	maxInvDistance = 1/maxDistance;
    }
    
    /**
     * @return the type of the service whose data can be rendered with this ServiceRenderer. In this case
     *         "OGC:SOS" will be returned.
     */
    public String getServiceType() {
        return "OGC:SOS";
    }

    /**
     * @return the versions of the services whose data can be rendered with this ServiceRenderer. In this case
     *         {"1.0.0"} will be returned.
     */
    public String[] getSupportedVersions() {
        return new String[] {"0.0.0"};
    }
    
}