/**
 * StochasticGradientDescent
 * Hier wird die Berechnung des Vorhersagefehlers, der daraus resultierenden Gradienten und der
 * neuen Koeffizienten durchgeführt.
 */


package com.fimrc.mypersonalstress.gui;

import android.content.Context;
import android.util.Log;

import com.fimrc.mypersonalstress.coefficients.Coefficient;
import com.fimrc.mypersonalstress.coefficients.CoefficientContainer;
import com.fimrc.mypersonalstress.persistence.DatabaseHelper;

public class StochasticGradientDescent {
    private final String TAG = "SGD";
    protected Context context;
    public DatabaseHelper mpsDB;

    public StochasticGradientDescent(DatabaseHelper mpsDB) {
        this.context = context;
        this.mpsDB = mpsDB;
    }
    public double predictOutput(CoefficientContainer cc, int observNumN) {
        //for (int i=1; i<cc.coefficients.length; i++) { ////change
        double predictionbuffer = 0;
        for (int i=1; i<3; i++) {

            Log.d(TAG, "Rechne Ergebnis für: "+cc.coefficients[i].name);
            predictionbuffer = predictionbuffer + skalarMe(cc.coefficients[i], observNumN);
            Log.d(TAG,"Zwischenergebnis: "+predictionbuffer);
        }
        mpsDB.addNewPrediction(observNumN, predictionbuffer);
        return predictionbuffer;
    }

    public double skalarMe(Coefficient c, int observNumN) {
        double part1 = mpsDB.getObservation(c, observNumN);
        Log.d(TAG, "Observationswert: "+part1);
        double part2 = mpsDB.getOldCoefficient(c, observNumN);
        Log.d(TAG, "Koeffizientenwert: "+part2);
        double product = part1*part2;
        product = Math.round(product * 10000000000.0) / 10000000000.0;
        Log.d(TAG, "Ergebnis: "+product);
        return product;
    }

    public double evaluatePredictionError(double prediction, int observNumN) {
        double y = mpsDB.getPSSScoreofObservationNumber(observNumN);
        double e = prediction-y;
        double e2 = Math.pow(e, 2);
        Log.d(TAG, "Prediction Error e² = "+e2);
        mpsDB.addNewPredictionError(observNumN, e2);
        return e2;
    }

    public double calculateGradient(Coefficient c, int observnumn, double prederr) {
        double gradient = 2*(Math.sqrt(prederr))*mpsDB.getObservation(c, observnumn);
        return gradient;
    }

    public void updateCoefficientValues(CoefficientContainer cc, int observnumn, double prederr) {
        for (int i=1; i<3; i++) {
            double gradient = calculateGradient(cc.coefficients[i], observnumn, prederr);
            double oldcoefficient = mpsDB.getOldCoefficient(cc.coefficients[i], observnumn);
            double alpha = 0.01;
            double newcoefficient = oldcoefficient - (alpha*gradient);
            Log.d(TAG, "Schreibe den neu berechneten Koeffizienten: "+newcoefficient);
            mpsDB.addNewGradient(cc.coefficients[i], observnumn, gradient);
            mpsDB.addNewCoefficientValues(cc.coefficients[i], observnumn, newcoefficient);
        }
    }

    public boolean checkForTermination(CoefficientContainer cc, int timewindow, double treshold, int observNumN, int maxobservations) {
        double averagebuffer = 0.0;
        for (int i=1; i<cc.coefficients.length; i++) {
            averagebuffer = averagebuffer + mpsDB.getGradientAverage(cc.coefficients[i], timewindow, observNumN);
        }
        double average = averagebuffer/((cc.coefficients.length)-1);


        if ((average < treshold) && (observNumN >= maxobservations)) {
            return true;
        }
        else {
            return false;
        }
    }

}
