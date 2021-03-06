//ObservationCalculation.java

//Hier wird das Abrufen neuer aggregierter Sensorwerte durchgeführt, welche standardisiert abgespeichert
//werden.

package com.fimrc.mypersonalstress.methods;

import android.content.Context;
import android.util.Log;

import com.fimrc.mypersonalstress.coefficients.Coefficient;
import com.fimrc.mypersonalstress.coefficients.CoefficientContainer;
import com.fimrc.mypersonalstress.persistence.DatabaseHelper;

public class ObservationCalculation {
    private final String TAG = "ObservationCalculation"; //Tag-String für die Debug-Ausgabe
    public DatabaseHelper mpsDB, msnDB; //Datenbankhilfsklassen für beide Datenbanken
    int currentObservationNumber; //Integer, repräsentiert die Anzahl der Abgerufenen Beobachtungen
    protected Context context;


    public ObservationCalculation(DatabaseHelper msnDB, DatabaseHelper mpsDB, int currentObservationNumber) {
        this.context = context;
        this.msnDB = msnDB;
        this.mpsDB = mpsDB;
        this.currentObservationNumber = currentObservationNumber;
    }

    //Schleife, welche Schritt für Schritt jede im CoefficientContainer angegebene Sensor-Beobachtung
    //ausrechnet.
    public void calculateNewObservations(CoefficientContainer cc, int timeframe) {
        //for (int i=1; i<cc.coefficients.length; i++) { ////change
            for (int i=1; i<cc.coefficients.length; i++) {
                Log.d(TAG, "____________________________________________________________________________________");
                Log.d(TAG, "Berechnung: Beobachtung Nr."+i+" mit dem Namen: "+cc.coefficients[i].name);
                calculateSingleObservation(cc.coefficients[i], this.currentObservationNumber, timeframe);

            }
    }

    //Methode welche vor der ersten Personalisierung die Koeffizienten des General-Models in der
    //Datenbank abspeichert.
    public void writeGeneralModelCoefficients(CoefficientContainer cc) {
        for (int i=1; i<cc.coefficients.length; i++) {
            mpsDB.addNewCoefficientValues(cc.coefficients[i], 0, cc.coefficients[i].generalmodelvalue);
        }
    }

    //Ausrechnen einer einzelnen Sensor-Beobachtung. Durch den Namen wird die jeweils gewünschte
    //Aggregation ausgelesen und durchgeführt.
    public void calculateSingleObservation (Coefficient c, int observNum, int timeframe) {
        //Log.d(TAG, "Koeffizient empfangen ist: "+c.name+" mit der Beobachtungsnr.: "+observNum);
        double newaggregationvalue = 0.0; // Zur Sicherheit 0 setzen

        //Log.d(TAG, "Hole Beobachtung unstandardisiert aus mSN-Datenbank...");
        if (c.transformation1 == "raw") {
            //Log.d(TAG, "Transformation 1 ist: raw");
            if (c.transformation2 == "none") {
                //Log.d(TAG, "Transformation 2 ist: none");
                if (c.aggregation == "min") {
                    newaggregationvalue = msnDB.getAggrMin(c.sensorname, c.sensorvalue, timeframe);
                }
                else if (c.aggregation == "max") {
                    newaggregationvalue = msnDB.getAggrMax(c.sensorname, c.sensorvalue, timeframe);
                }
                else if (c.aggregation == "range") {
                    newaggregationvalue = msnDB.getAggrRange(c.sensorname, c.sensorvalue, timeframe);
                }
                else if (c.aggregation == "median") {
                    newaggregationvalue = msnDB.getAggrMedian(c.sensorname, c.sensorvalue, timeframe);
                }
                else if (c.aggregation == "mean") {
                    newaggregationvalue = msnDB.getAggrMean(c.sensorname, c.sensorvalue, timeframe);
                }
                else if (c.aggregation == "count") {
                    newaggregationvalue = msnDB.getAggrCount(c.sensorname, c.sensorvalue, timeframe);
                }
            }
        }
        Log.d(TAG, "Wert "+newaggregationvalue+" wurde gelesen für "+c.name);
        standardisizeAggrValue(c, newaggregationvalue, observNum);
       // mpsDB.addNewCoefficientValues(c,)
    }

    //Standardisierungsprozess einer Sensor-Beobachtung, sowie Schreiben der neuen standardisierten
    //Beobachtung in die Datenbank
    public void standardisizeAggrValue(Coefficient c, double valuex, int observNumN) {
        //Log.d(TAG, "Starte die Standardisierung des Aggregierten Wertes x="+valuex+" - Beob.-Nr.: "+observNumN);
        //Log.d(TAG, "Wert runden...");
        valuex = Math.round(valuex * 10000000000.0) / 10000000000.0;
        //Log.d(TAG, "Neuer Wert: "+valuex);
        //Log.d(TAG, "Rufe die getOldMean Methode auf");
        double oldmeanbuffer = mpsDB.getOldMean(c, observNumN);
        //Log.d(TAG, "Alter u-Wert von Funktion returned ist: "+oldmeanbuffer);
        // Log.d(TAG, "Rufe die calcNewMean Methode auf");
        double newmeanbuffer = calcNewMean(valuex, oldmeanbuffer, observNumN);
        Log.d(TAG, "Schreibe den neuen Mean: "+newmeanbuffer+" in die DB");
        mpsDB.addNewMean(c, observNumN, newmeanbuffer);
        //Log.d(TAG, "Rufe die getOldStdDev Methode auf");
        double oldstddevbuffer = mpsDB.getOldStdDev(c, observNumN);
        //Log.d(TAG, "Alter o-Wert von Funktion returned ist: "+oldstddevbuffer);
        //Log.d(TAG, "Rufe die calcNewStdDev Methode auf");
        double newstddevbuffer = calcNewStdDev(valuex, oldmeanbuffer, newmeanbuffer, observNumN, oldstddevbuffer);
        Log.d(TAG, "Schreibe die neue StdDev: "+newstddevbuffer+" in die DB");
        mpsDB.addNewStdDev(c, observNumN, newstddevbuffer);
        //Log.d(TAG, "Standardisiere nun letzendlich den Wert");
        double zvaluebuffer = standardizise(valuex, newmeanbuffer, newstddevbuffer, observNumN);
        Log.d(TAG, "Schreibe den standardisierten Wert: "+zvaluebuffer+" in die DB");
        mpsDB.addNewObservationValue(c, observNumN, zvaluebuffer);

    }

    //Berechnen eines neuen Durchschnitts der jeweiligen Sensor-Beobachtung
    //Erforderlich für die durchzuführende Standardisierung.
    public double calcNewMean (double x, double u, int N) {
        double unew = u+((x-u)/N);
        if (Double.isNaN(unew) == true) {
            unew = 0.0;
        }
        unew = Math.round(unew * 10000000000.0) / 10000000000.0;
        return unew;
    }

    //Berechnen einer neuen Standard-Abweichung der jeweiligen Sensor-Beobachtung
    //Erforderlich für die durchzuführende Standardisierung.
    public double calcNewStdDev (double x, double ualt, double uneu, int N, double oalt) {
        Log.d(TAG, "Berechne neue StdDev mit den übergebenen Werten Aktuelles x: "+x+" Alter Mean u: "+ualt+" Neuer Mean u: "+uneu+" Durchgang N: "+N+" Alte StdDev o: "+oalt);
        double oneu, o2neu, o2alt, delta1, delta2, m2neu, m2alt;
        if (N <= 1) {
            oneu = 0.0;
            Log.d(TAG, "Fall darf eig nicht auftreten.(?) Die neue StdDev ist: "+oneu);
            return oneu;
        }
        else if (N == 2) {
            oneu = (x-ualt)/2;
            oneu = Math.abs(oneu);
            Log.d(TAG, "Die neue StdDev ist: "+oneu);
            return oneu;
        }
        else {
            o2alt = Math.pow(oalt, 2);
            m2alt =  o2alt*(N-1);
            //int n = N+1;
            delta1 = x - ualt;
            delta2 = x - uneu;
            m2neu =  m2alt +(delta1*delta2);
            o2neu = m2neu/N;
            oneu = Math.sqrt(o2neu);
            Log.d(TAG, "Die neue StdDev ist: "+oneu);
            return oneu;
        }
    }

    //Mathematische Standardisierungsmethode
    public double standardizise(double x, double u, double o, int N) {
        double z;
        Log.d(TAG, "Standardisierung gestartet, mit x: "+x+" u: "+u+" o: "+o+" N: "+N);
        z = (x-u)/o;
        if (Double.isNaN(z) == true) {
            z = 0.0;
        }
        z = Math.round(z * 10000000000.0) / 10000000000.0;
        Log.d(TAG, "Neuer z-Wert: "+z);
        return z;
    }

}
