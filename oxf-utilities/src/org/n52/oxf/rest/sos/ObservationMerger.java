/**********************************************************************************
 Copyright (C) 2010
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
 
 21.02.2011
 *********************************************************************************/
package org.n52.oxf.rest.sos;

import java.util.Iterator;

import org.n52.oxf.feature.OXFFeature;
import org.n52.oxf.feature.OXFFeatureCollection;
import org.n52.oxf.feature.OXFMeasurementType;

/**
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 *
 */
public class ObservationMerger {

	public static OXFFeatureCollection mergeObservations(OXFFeatureCollection[] observationCollectionArray) {
		
		OXFFeatureCollection observations = new OXFFeatureCollection("merged_coll", new OXFMeasurementType());
		
		for (int i = 0; i < observationCollectionArray.length; i++) {
			for (Iterator iterator = observationCollectionArray[i].iterator(); iterator.hasNext();) {
				OXFFeature observation = (OXFFeature) iterator.next();

				observations.add(observation);
			}
		}
		
		return observations;
	}
}
