package com.fimrc.mypersonalstress.coefficients;

import java.util.ArrayList;
import java.util.List;

public class CoefficientsModel {

    public SingleSensorModel lightSensor;
    public void leModel(){
        //||sensorname||value||raw|outrm|none|log|posms|negms|min|max|range|median|mean|count|counth|timeframe
        lightSensor = new SingleSensorModel("lightsensor", "light", true, false,true, false, false, false, true, true, false, false, false, false, false,60);

    }

    Coefficient[] container;

    public void createCoefficientsOutOfSingleSensorModell(SingleSensorModel s){


        String sensornamebuffer;
        sensornamebuffer = s.sensorname+"."+s.sensorvalue;
        if (s.raw == true) {
            sensornamebuffer = sensornamebuffer+".raw";

        }
        else if (s.outrm == true) {
            sensornamebuffer = sensornamebuffer+".outrm";
        }
        else {
            sensornamebuffer = sensornamebuffer+".null";
        }
    }




    //public int getAmountOfCoefficients() {
       // int amount = lightSensor.length;
       // return amount;
    //}



}
