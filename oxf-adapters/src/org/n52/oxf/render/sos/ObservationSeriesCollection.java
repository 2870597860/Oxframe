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
 
 Created on: 13.10.2006
 *********************************************************************************/

package org.n52.oxf.render.sos;

import java.util.*;
import org.apache.log4j.*;
import org.n52.oxf.feature.*;
import org.n52.oxf.feature.dataTypes.*;
import org.n52.oxf.util.*;
import org.n52.oxf.valueDomains.time.*;

/**
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 * 
 */
public class ObservationSeriesCollection {

    private static Logger LOGGER = LoggingHandler.getLogger(ObservationSeriesCollection.class);

    /**
     * associates a feature-id (String) with a Map<ITimePosition, ObservedValueTuple>.
     */
    private Map<String, Map<ITimePosition, ObservedValueTuple>> featureMap;

    private Set<ITimePosition> timeSet;

    private Comparable[] maximumValues;
    private Comparable[] minimumValues;

    private String[] observedPropertyArray;
    
    public ObservationSeriesCollection(OXFFeatureCollection observationCollection,
                                   Set<OXFFeature> featuresSet,
                                   String[] observedProperties,
                                   boolean onlyCompleteTuples) {
        
        String[] featureIDArray = new String[featuresSet.size()];
        Iterator iter = featuresSet.iterator();
        int i = 0;
        while (iter.hasNext()) {
            featureIDArray[i] = ((OXFFeature) iter.next()).getID();
            i++;
        }
        
        observedPropertyArray = observedProperties;

        timeSet = new HashSet<ITimePosition>();
        featureMap = findObservedValueTuples4FOI(observationCollection,
                                                 featureIDArray,
                                                 observedProperties,
                                                 onlyCompleteTuples);
    }

    /**
     * 
     */
    public ObservationSeriesCollection(OXFFeatureCollection observationCollection,
                                   String[] featureIDArray,
                                   String[] observedProperties,
                                   boolean onlyCompleteTuples) {
        
        observedPropertyArray = observedProperties;
        
        timeSet = new HashSet<ITimePosition>();
        featureMap = findObservedValueTuples4FOI(observationCollection,
                                                 featureIDArray,
                                                 observedProperties,
                                                 onlyCompleteTuples);
    }

    public ObservationSeriesCollection(OXFFeatureCollection observationCollection,
                                       String[] featureIDArray,
                                       String[] observedProperties,
                                       String[] procedureNames,
                                       boolean onlyCompleteTuples) {
        timeSet = new HashSet<ITimePosition>();
        featureMap = findObservedValueTuples4FOI(observationCollection,
                                                 featureIDArray,
                                                 observedProperties,
                                                 procedureNames,
                                                 onlyCompleteTuples);
    }
    
    /**
     * 
     * @param indexOfObservedProperty
     * @return
     */
    public Comparable getMaximum(int indexOfObservedProperty) {
        return maximumValues[indexOfObservedProperty];
    }
    public Comparable getMaximum(String observedPropertyName) {
        for (int i=0; i<observedPropertyArray.length; i++) {
            if (observedPropertyArray[i].equals(observedPropertyName)) {
                return maximumValues[i];
            }
        }
        return null;
    }
    
    /**
     * 
     * @param indexOfObservedProperty
     * @return
     */
    public Comparable getMinimum(int indexOfObservedProperty) {
        return minimumValues[indexOfObservedProperty];
    }

    public Comparable getMinimum(String observedPropertyName) {
        for (int i=0; i<observedPropertyArray.length; i++) {
            if (observedPropertyArray[i].equals(observedPropertyName)) {
                return minimumValues[i];
            }
        }
        return null;
    }
    
    /**
     * Gibt sämtliche <code>ObservedValueTuple</code> zurück. D.h. alle <code>ObservedValueTuple</code>
     * für sämtliche FOI-Zeitpunkt-Kombinationen.
     * 
     * @return
     */
    public List<ObservedValueTuple> getAllTuples() {
        List<ObservedValueTuple> res = new ArrayList<ObservedValueTuple>();

        for (String featureID : featureMap.keySet()) {
            Map<ITimePosition, ObservedValueTuple> timeMap = featureMap.get(featureID);
            res.addAll(timeMap.values());
        }

        return res;
    }

    /**
     * gibt zu dem spezifizierten OXFFeature sämtliche observedValueTuples zurück, und zwar sortiert zu den
     * zugehörigen Mess-Zeitpunkten in einer Map.
     * 
     * @param feature
     * @return
     */
    public Map<ITimePosition, ObservedValueTuple> getAllTuples(OXFFeature foi) {
        return featureMap.get(foi.getID());
    }

    /**
     * gibt zu der spezifizierten OXFFeature-ID sämtliche observedValueTuples zurück, und zwar sortiert zu den
     * zugehörigen Mess-Zeitpunkten in einer Map.
     * 
     * @param feature-ID
     * @return
     */
    public Map<ITimePosition, ObservedValueTuple> getAllTuples(String foiID) {
        return featureMap.get(foiID);
    }

    /**
     * Gibt für ein spezifiziertes FOI und einen bestimmten Zeitpunkt das Phänomen-Werte-Tupel zurück. <br>
     * Entspricht der Funktion: <br>
     * f(featureID, timePos) = phenTuple = (X1, X2, ... Xn) | mit Xi := beobachteter Phänomenwert.
     * 
     * @param feature
     * @param timePos
     * @return
     */
    public ObservedValueTuple getTuple(OXFFeature feature, ITimePosition timePos) {
        for (String featureID : featureMap.keySet()) {
            if (feature.getID().equals(featureID)) {
                Map<ITimePosition, ObservedValueTuple> timeTupleMap = featureMap.get(featureID);
                for (ITimePosition timeKey : timeTupleMap.keySet()) {
                    if (timePos.equals(timeKey)) {
                        return timeTupleMap.get(timeKey);
                    }
                }
            }
        }
        return null;
    }

    /**
     * gibt sämtliche Zeitpunkte in einem Array zurück, für die bei mindestens einem FOI Messwerte für jedes
     * Phänomen vorliegen.
     * 
     * @return
     */
    public ITimePosition[] getSortedTimeArray() {
        ITimePosition[] timeArray = new ITimePosition[timeSet.size()];
        timeSet.toArray(timeArray);
        Arrays.sort(timeArray);
        return timeArray;
    }

    /**
     * Übergeben wird eine <code>observationCollection</code>, die observations für n Phänomene enthält.
     * Die Namen dieser Phänomene werden in dem Parameter <code>observedPropertyNames</code> angegeben. Dann
     * werden für sämtliche FOI, deren ID in dem Parameter <code>featureIDArray</code> spezifiziert wurde,
     * die zugehörigen observations gefunden. Für jeden Zeitpunkt für den observations für dieses foi
     * existieren werden die zugehörigen Messwerte zu den einzelnen Phänomenen gefunden und zu Tupeln, sog.
     * <code>ObservedValueTuple</code>, zusammengefasst. <br>
     * <br>!! Falls (onlyCompleteTuples == true): Finden sich für ein FOI bei einem bestimmten Zeitpunkt
     * nicht für jedes Phänomen Messwerte, so wird das unvollständig besetzte <code>ObservedValueTuple</code>
     * nicht ins Resultat mit aufgenommen!
     * 
     * @param observationCollection
     * @param featureSet
     * @param observedPropertyNames
     */
    protected Map<String, Map<ITimePosition, ObservedValueTuple>> findObservedValueTuples4FOI(OXFFeatureCollection observationCollection,
                                                                                              String[] featureIDArray,
                                                                                              String[] observedPropertyNames,
                                                                                              boolean onlyCompleteTuples) {
        Map<String, Map<ITimePosition, ObservedValueTuple>> resultMap = new HashMap<String, Map<ITimePosition, ObservedValueTuple>>();

        //
        // firstly, initialize maximum- and minimum-arrays:
        //
        maximumValues = new Comparable[observedPropertyNames.length];
        minimumValues = new Comparable[observedPropertyNames.length];

        //
        // now iterate over features:
        //
        for (String featureID : featureIDArray) {
            Map<String, ObservedValueTuple> tupleMap = new HashMap<String, ObservedValueTuple>();

            for (OXFFeature observation : observationCollection) {

                Object foiAttribute = observation.getAttribute(OXFAbstractObservationType.FEATURE_OF_INTEREST);
                if (foiAttribute != null) {
                    OXFFeature featureOfInterest = (OXFFeature) foiAttribute;

                    // System.out.println("\"" + featureOfInterest.getID() + "\",");
                    if (featureOfInterest.getID().equals(featureID)) {

                        ITimePosition time = (ITimePosition) observation.getAttribute(OXFAbstractObservationType.SAMPLING_TIME);
                        String timeString = time.toISO8601Format();

                        ObservedValueTuple tuple = new ObservedValueTuple(observedPropertyNames.length,
                                                                          observedPropertyNames,
                                                                          time);
                        if (tupleMap.containsKey(timeString)) {
                            tuple = tupleMap.get(timeString);
                        }

                        IObservationResult measureResult = (IObservationResult) observation.getAttribute(OXFMeasurementType.RESULT);
                        Object result = measureResult.getValue();

                        OXFPhenomenonPropertyType observedProperty = (OXFPhenomenonPropertyType) observation.getAttribute(OXFAbstractObservationType.OBSERVED_PROPERTY);

                        String procedure = (String)observation.getAttribute(OXFAbstractObservationType.PROCEDURE);
                        
                        //
                        // setze den Wert an der richtigen Tupel-Position:
                        //
                        for (int i = 0; i < observedPropertyNames.length; i++) {
                            if (observedProperty.getURN().equals(observedPropertyNames[i])) {
                                tuple.setValue(i, result);
                            }
                        }
                        // füge das Tupel der tupleMap hinzu bzw. ersetze das alte Objekt:
                        tupleMap.put(timeString, tuple);

                    }
                }
            }

            //
            // nachschauen, welche tuple vollständig gesetzt sind:
            //
            Map<ITimePosition, ObservedValueTuple> tupleMap_corrected = new HashMap<ITimePosition, ObservedValueTuple>();
            for (String timeString : tupleMap.keySet()) {
                ObservedValueTuple tuple = tupleMap.get(timeString);

                boolean completeTuple = true;

                // check if all values are setted
                for (int i = 0; i < tuple.dimension(); i++) {
                    if (tuple.getValue(i) == null) {
                        completeTuple = false;
                        break;
                    }
                }

                // falls auch nicht-vollständige Tupel mit aufgenommen werden sollen, wird 'completeTuple'
                // wieder auf 'true' gesetzt:
                if (onlyCompleteTuples == false) {
                    completeTuple = true;
                }

                if (completeTuple) {
                    ITimePosition time = new TimePosition(timeString);

                    //
                    // das tuple darf rein
                    //
                    tupleMap_corrected.put(time, tuple);

                    //
                    // den Zeitpunkt der Menge der timeSet hinzufügen, falls noch nicht enthalten:
                    //
                    boolean contained = false;
                    for (ITimePosition timePos : timeSet) {
                        if (timePos.equals(time)) {
                            contained = true;
                            break;
                        }
                    }
                    if ( !contained) {
                        timeSet.add(time);
                    }

                    //
                    // falls der value vom Typ Comparable --> maximum und minimum setzen:
                    //
                    for (int i = 0; i < tuple.dimension(); i++) {
                        if (tuple.getValue(i) instanceof Comparable) {
                            Comparable c = (Comparable) tuple.getValue(i);
                            if (maximumValues[i] == null || c.compareTo(maximumValues[i]) > 0) {
                                maximumValues[i] = c;
                            }
                            if (minimumValues[i] == null || c.compareTo(minimumValues[i]) < 0) {
                                minimumValues[i] = c;
                            }
                        }
                    }
                }
            }
            if (tupleMap_corrected.size() > 0) {
                resultMap.put(featureID, tupleMap_corrected);
            }
        }

        return resultMap;
    }
    
    
    /**
     * Übergeben wird eine <code>observationCollection</code>, die observations für n Phänomene enthält.
     * Die Namen dieser Phänomene werden in dem Parameter <code>observedPropertyNames</code> angegeben. Dann
     * werden für sämtliche FOI, deren ID in dem Parameter <code>featureIDArray</code> spezifiziert wurde,
     * die zugehörigen observations gefunden. Für jeden Zeitpunkt für den observations für dieses foi
     * existieren werden die zugehörigen Messwerte zu den einzelnen Phänomenen gefunden und zu Tupeln, sog.
     * <code>ObservedValueTuple</code>, zusammengefasst. <br>
     * <br>!! Falls (onlyCompleteTuples == true): Finden sich für ein FOI bei einem bestimmten Zeitpunkt
     * nicht für jedes Phänomen Messwerte, so wird das unvollständig besetzte <code>ObservedValueTuple</code>
     * nicht ins Resultat mit aufgenommen!
     * 
     * @param observationCollection
     * @param featureSet
     * @param observedPropertyNames
     */
    protected Map<String, Map<ITimePosition, ObservedValueTuple>> findObservedValueTuples4FOI(OXFFeatureCollection observationCollection,
                                                                                              String[] featureIDArray,
                                                                                              String[] observedPropertyNames,
                                                                                              String[] procedureNames,
                                                                                              boolean onlyCompleteTuples) {
        Map<String, Map<ITimePosition, ObservedValueTuple>> resultMap = new HashMap<String, Map<ITimePosition, ObservedValueTuple>>();

        //
        // firstly, initialize maximum- and minimum-arrays:
        //
        maximumValues = new Comparable[observedPropertyNames.length];
        minimumValues = new Comparable[observedPropertyNames.length];

        //
        // now iterate over features:
        //
        for (String featureID : featureIDArray) {
            Map<String, ObservedValueTuple> tupleMap = new HashMap<String, ObservedValueTuple>();

            for (OXFFeature observation : observationCollection) {

                Object foiAttribute = observation.getAttribute(OXFAbstractObservationType.FEATURE_OF_INTEREST);
                if (foiAttribute != null) {
                    OXFFeature featureOfInterest = (OXFFeature) foiAttribute;

                    if (featureOfInterest.getID().equals(featureID)) {

                        ITimePosition time = (ITimePosition) observation.getAttribute(OXFAbstractObservationType.SAMPLING_TIME);
                        String timeString = time.toISO8601Format();

                        ObservedValueTuple tuple = new ObservedValueTuple(observedPropertyNames.length,
                                                                          observedPropertyNames,
                                                                          time);
                        if (tupleMap.containsKey(timeString)) {
                            tuple = tupleMap.get(timeString);
                        }

                        IObservationResult measureResult = (IObservationResult) observation.getAttribute(OXFMeasurementType.RESULT);
                        Object result = measureResult.getValue();

                        OXFPhenomenonPropertyType observedProperty = (OXFPhenomenonPropertyType) observation.getAttribute(OXFAbstractObservationType.OBSERVED_PROPERTY);

                        String procedure = (String)observation.getAttribute(OXFAbstractObservationType.PROCEDURE);
                        
                        //
                        // setze den Wert an der richtigen Tupel-Position:
                        //
                        for (int i = 0; i < observedPropertyNames.length; i++) {
                            if (observedProperty.getURN().equals(observedPropertyNames[i])) { 
                                if (procedure.equals(procedureNames[i])){
                                    tuple.setValue(i, result);
                                }
                            }
                        }
                        // füge das Tupel der tupleMap hinzu bzw. ersetze das alte Objekt:
                        tupleMap.put(timeString, tuple);

                    }
                }
            }

            //
            // nachschauen, welche tuple vollständig gesetzt sind:
            //
            Map<ITimePosition, ObservedValueTuple> tupleMap_corrected = new HashMap<ITimePosition, ObservedValueTuple>();
            for (String timeString : tupleMap.keySet()) {
                ObservedValueTuple tuple = tupleMap.get(timeString);

                boolean completeTuple = true;

                // check if all values are setted
                for (int i = 0; i < tuple.dimension(); i++) {
                    if (tuple.getValue(i) == null) {
                        completeTuple = false;
                        break;
                    }
                }

                // falls auch nicht-vollständige Tupel mit aufgenommen werden sollen, wird 'completeTuple'
                // wieder auf 'true' gesetzt:
                if (onlyCompleteTuples == false) {
                    completeTuple = true;
                }

                if (completeTuple) {
                    ITimePosition time = new TimePosition(timeString);

                    //
                    // das tuple darf rein
                    //
                    tupleMap_corrected.put(time, tuple);

                    //
                    // den Zeitpunkt der Menge der timeSet hinzufügen, falls noch nicht enthalten:
                    //
                    boolean contained = false;
                    for (ITimePosition timePos : timeSet) {
                        if (timePos.equals(time)) {
                            contained = true;
                            break;
                        }
                    }
                    if ( !contained) {
                        timeSet.add(time);
                    }

                    //
                    // falls der value vom Typ Comparable --> maximum und minimum setzen:
                    //
                    for (int i = 0; i < tuple.dimension(); i++) {
                        if (tuple.getValue(i) instanceof Comparable) {
                            Comparable c = (Comparable) tuple.getValue(i);
                            if (maximumValues[i] == null || c.compareTo(maximumValues[i]) > 0) {
                                maximumValues[i] = c;
                            }
                            if (minimumValues[i] == null || c.compareTo(minimumValues[i]) < 0) {
                                minimumValues[i] = c;
                            }
                        }
                    }
                }
            }
            if (tupleMap_corrected.size() > 0) {
                resultMap.put(featureID, tupleMap_corrected);
            }
        }

        return resultMap;
    }
}