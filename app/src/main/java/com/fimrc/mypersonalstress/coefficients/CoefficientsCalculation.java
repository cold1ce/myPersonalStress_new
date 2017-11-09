package com.fimrc.mypersonalstress.coefficients;


import com.fimrc.mypersonalstress.persistence.DatabaseHelper;

public class CoefficientsCalculation {

    public DatabaseHelper myDB, myDB2;




    public void getCoeff(SingleSensorModel s) {
        doAggregation(s);
    }

    public void doCoeffThings (Coefficient c) {

        if (c.transformation1 == "raw") {
            if (c.transformation2 == "none") {
                if (c.aggregation == "max") {


                }
            }
        }
     }

    public void doAggregation(SingleSensorModel s) {

    }
}
