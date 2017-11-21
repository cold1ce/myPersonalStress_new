/**
 * StochasticGradientDescent
 * Hier wird die Berechnung des Vorhersagefehlers, der daraus resultierenden Gradienten und der
 * neuen Koeffizienten durchgeführt.
 */


package com.fimrc.mypersonalstress.methods;

import android.content.Context;
import android.content.SharedPreferences;
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
        for (int i=1; i<cc.coefficients.length; i++) {

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
        double e = (prediction-y);
        mpsDB.addNewPredictionError(observNumN, e);
        Log.d(TAG, "Prediction Error e = "+e);
        double e2 = Math.pow((prediction-y), 2);
        mpsDB.addNewPredictionErrorSquared(observNumN, e2);
        Log.d(TAG, "Prediction Error e² = "+e2);
        return e;
    }

    public double calculateGradient(Coefficient c, int observnumn, double prederr) {
        double gradient = 2*prederr*mpsDB.getObservation(c, observnumn);
        return gradient;
    }

    public void updateCoefficientValues(CoefficientContainer cc, double alpha, int observnumn, double prederr) {
        double gradientsum = 0.0;
        for (int i=1; i<cc.coefficients.length; i++) {
            double gradient = calculateGradient(cc.coefficients[i], observnumn, prederr);
            double oldcoefficient = mpsDB.getOldCoefficient(cc.coefficients[i], observnumn);
            double newcoefficient = oldcoefficient - (alpha*gradient);
            Log.d(TAG, "Berechne neuen Koeffizienten: Alter Koeffizient-(Alpha*Gradient): "+oldcoefficient+"-("+alpha+"*"+gradient+")="+newcoefficient);
            gradientsum = gradientsum + gradient;
            Log.d(TAG, "Schreibe den neu berechneten Koeffizienten: "+newcoefficient);
            mpsDB.addNewGradient(cc.coefficients[i], observnumn, gradient);
            mpsDB.addNewCoefficientValues(cc.coefficients[i], observnumn, newcoefficient);
        }
        double gradientaverage = gradientsum/((cc.coefficients.length)-1);
        Log.d(TAG, "Der Gradienten-Average ist: "+gradientaverage);
        mpsDB.addNewGradientsAverage(gradientaverage, observnumn);

    }

    public boolean checkForTermination(CoefficientContainer cc, int gradienttimewindow, double sigmatreshold, int observNumN, int maxpersonalizations) {
        double gradientsmaximum = mpsDB.checkGradientsAverages(gradienttimewindow, observNumN);
        if ((gradientsmaximum < sigmatreshold) || (observNumN >= maxpersonalizations)) {
            return true;
        }
        else {
            return false;
        }
    }

}
