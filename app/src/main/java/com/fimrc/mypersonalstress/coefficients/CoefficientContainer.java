package com.fimrc.mypersonalstress.coefficients;

import android.util.Log;

//Hier können die für die Personalisierung gewünschten Koeffizienten eingegeben werden.
//Je nach Anzahl der Koeffizienten muss die Größe des Coefficient[]-Arrays angepasst werden.
//Die Nummerierung fängt hier bei 1 an.
//Sollen z.B. 5 Koeffizienten berechnet werden muss eine Array-Größe von 6 eingestellt und
//5 Koeffizienten mit der Nummerierung von 1-5 angegeben werden.

public class CoefficientContainer {
    private final String TAG = "CoefficientContainer";
    public Coefficient[] coefficients = new Coefficient[2];

    public CoefficientContainer() {
        this.coefficients[1] = new Coefficient ("TestSensor1", "testvalue", "raw", "none", "count", 0.5);
        //this.coefficients[2] = new Coefficient("LightSensor", "light", "raw", "none", "max", 0.1);
        //this.coefficients[3] = new Coefficient("TestSensor1", "testvalue", "raw", "none", "min", 0.5);
        //this.coefficients[4] = new Coefficient("TestSensor1", "testvalue", "raw", "none", "mean", 0.5);
        //this.coefficients[5] = new Coefficient("TestSensor1", "testvalue", "raw", "none", "median", 0.5);
        //this.coefficients[6] = new Coefficient("TestSensor1", "testvalue", "raw", "none", "range", 0.5);
        //this.coefficients[7] = new Coefficient("TestSensor2", "testvalue", "raw", "none", "count", 0.5);
    }


}
