package com.fimrc.mypersonalstress.coefficients;

import android.util.Log;



public class CoefficientContainer {
    private final String TAG = "CoefficientContainer";
    public Coefficient[] coefficients = new Coefficient[3];
    //public int[] intarr;
    //public int[] intarr2 = new int[2];
    public CoefficientContainer() {

        //Coefficient[] coefficients = new Coefficient[3];
        //int[] intarr = new int[2];
        //this.intarr[0] = 1;
        //this.intarr2[1] = 2;
        Log.d(TAG, "coefficientsArray erstellt mit Länge: "+coefficients.length+" -- nun: Aufruf: initialisiere Koeffizienten");
        this.coefficients[1] = new Coefficient ("TestSensor", "testvalue", "raw", "none", "max", 0.5);
        //this.coefficients[2] = new Coefficient("LightSensor", "light", "raw", "none", "max", -0.9);
        this.coefficients[2] = new Coefficient("TestSensor", "testvalue", "raw", "none", "mean", 0.3);
        Log.d(TAG, "Alle Koeffizienten erstellt.");
        Log.d(TAG,"Length von CoefficientContainer:"+this.coefficients.length);
    }


}
