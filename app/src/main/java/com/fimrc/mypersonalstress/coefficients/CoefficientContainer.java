package com.fimrc.mypersonalstress.coefficients;

import android.util.Log;



public class CoefficientContainer {
    private final String TAG = "CoefficientContainer";
    public Coefficient[] coefficients = new Coefficient[4];
    //public int[] intarr;
    //public int[] intarr2 = new int[2];
    public CoefficientContainer() {

        //Coefficient[] coefficients = new Coefficient[3];
        //int[] intarr = new int[2];
        //this.intarr[0] = 1;
        //this.intarr2[1] = 2;
        Log.d(TAG, "coefficientsArray erstellt mit Länge: "+coefficients.length+" -- nun: Aufruf: initialisiere Koeffizienten");
        this.coefficients[1] = new Coefficient ("TestSensor", "testvalue", "raw", "none", "max", 0.0);
        this.coefficients[2] = new Coefficient("LightSensor", "light", "raw", "none", "max", 0.0);
        this.coefficients[3] = new Coefficient("TestSensor", "testvalue", "raw", "none", "mean", 0.0);
        Log.d(TAG, "Alle Koeffizienten erstellt.");
    }


}
