package com.fimrc.mypersonalstress.gui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.fimrc.mypersonalstress.coefficients.CoefficientContainer;
import com.fimrc.mypersonalstress.persistence.DatabaseHelper;
import com.fimrc.mysensornetwork.R;

public class Personalization {

    Context context;
    private final String TAG = "Personalization"; //Tag-String für die Debug-Ausgabe
    public DatabaseHelper mpsDB, msnDB; //Datenbankhilfsklassen für beide Datenbanken
    int currentObservationNumber; //Integer, repräsentiert die Anzahl der Abgerufenen Beobachtungen



    public Personalization(Context c) {
        this.context = c;
        mpsDB = new DatabaseHelper(c, "mypersonalstress.db");
        msnDB = new DatabaseHelper(c, "mySensorNetwork");
    }


    public void doANewPersonalization() {
        StartQuestionnaire();
        double score = mpsDB.getLastPSSScore();
        currentObservationNumber = (mpsDB.getAmountofObservationsDoneYet());
        CoefficientContainer con = new CoefficientContainer();
        //observationCalculationLoop(con);
        Log.d(TAG, "Habe alle neuen Oservation Werte geschrieben");
        Log.d(TAG, "________________________________________________");
        if (currentObservationNumber == 1) {
            mpsDB.addFirstCoefficientsRow();
            //writeGeneralModelCoefficients(con);
        }
        StochasticGradientDescent sgd = new StochasticGradientDescent(mpsDB);
        double predictedoutput = sgd.predictOutput(con, currentObservationNumber);
        Log.d(TAG, "Predicted Output ist: "+predictedoutput);
        double predictionerror = sgd.evaluatePredictionError(predictedoutput, currentObservationNumber);
        sgd.updateCoefficientValues(con, currentObservationNumber,predictionerror);
    }
    public void StartQuestionnaire(){
        System.out.println("STARTING ACTIVITY B");
        Intent i = new Intent (context, StressQuestionnaire.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }










}
