/**
 * Personalization
 * Hier wird der Personalisierungs-Prozess durchgeführt und schrittweise die benötigten
 * Methoden aufgerufen welche für die Personalisierung notwendig sind.
 */



package com.fimrc.mypersonalstress.gui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.fimrc.mypersonalstress.coefficients.CoefficientContainer;
import com.fimrc.mypersonalstress.methods.ObservationCalculation;
import com.fimrc.mypersonalstress.methods.StochasticGradientDescent;
import com.fimrc.mypersonalstress.persistence.DatabaseHelper;
import com.fimrc.mysensornetwork.R;

public class Personalization extends AppCompatActivity {
    private final String TAG = "Personalization";
    public DatabaseHelper mpsDB, msnDB;
    int currentObservationNumber;

    //Bei Start einer neuen Personalisierung werden die notwendigen Datenbanken angesprochen sowie
    // alle notwendige Tabellen neu erstellt(Falls sie noch nicht existieren)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "Fragebogen gestartet.");
        setContentView(R.layout.activity_personalization);
        mpsDB = new DatabaseHelper(this, "mypersonalstress.db");
        msnDB = new DatabaseHelper(this, "mySensorNetwork");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.setTitle("Personalisierung");
        startANewPersonalization();
    }

    //Wird die Personalization-Activity durch das Beenden eines neuen Stressfragebogens aufgerufen
    //startet die Activity hier, und setzt durch den Aufruf der continuePersonalization-Methode
    //die Personalisierung fort.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1)
        {
            String message=data.getStringExtra("MESSAGE");
            mpsDB = new DatabaseHelper(this, "mypersonalstress.db");
            msnDB = new DatabaseHelper(this, "mySensorNetwork");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            this.setTitle("Personalisierung");
            continuePersonalization();
        }
    }

    //Beginnt eine Personalisierung, mit dem Aufruf der Oberfläche für den Stressfragebogen
    public void startANewPersonalization() {
        StartQuestionnaire();
    }

    //Fortsetzung der Personalisierung nach dem Ausfüllen des Stressfragebogens
    public void continuePersonalization() {
        final TextView tv_title = (TextView) findViewById(R.id.tv_title);
        final TextView tv_pssscore = (TextView) findViewById(R.id.tv_pssscore);
        final TextView tv_predictedstresslevel = (TextView) findViewById(R.id.tv_predictedstresslevel);
        final TextView tv_predictionerror = (TextView) findViewById(R.id.tv_predictionerror);

        SharedPreferences prefs = getSharedPreferences("mps_preferences", MODE_PRIVATE);
        int maxobservations = prefs.getInt("maxpersonalizations", 10);


        double score = mpsDB.getLastPSSScore();
        tv_pssscore.setText((int)score+"");

        currentObservationNumber = (mpsDB.getAmountofObservationsDoneYet());
        tv_title.setText("Personalisierung "+currentObservationNumber+"/"+maxobservations+" erfolgreich durchgeführt!");
        //Initialisieren aller zu prüfenden Koeffizienten
        CoefficientContainer container = new CoefficientContainer();

        //Initialisieren einer neuen Observations-Instanz, hier werden die neuen Stress-Beobachtungen
        //verarbeitet und in den jeweiligen Hilfstabellen abgespeichert.
        ObservationCalculation observation = new ObservationCalculation(msnDB, mpsDB, currentObservationNumber);

        //Falls es die erste Beobachtung ist, werden die Koeffizienten des General-Models eingefügt
        if (currentObservationNumber == 1) {
            Log.d(TAG, "Füge erste Coeff Reihe ein");
            mpsDB.addFirstCoefficientsRow();
            observation.writeGeneralModelCoefficients(container);
        }

        //Berechnung einer neuen Stress-Beobachtung starten, aus den SensorDaten von mySensorNetwork
        observation.calculateNewObservations(container);

        //Initialisieren einer neuen StochasticGradientDescent-Instanz, hier wird mit Hilfe der
        //inzwischen standardisiert gespeicherten Beobachtungen und dem letzten Stressfragebogen
        //die eigentliche Personalisierung vorgenommen.
        StochasticGradientDescent sgd = new StochasticGradientDescent(mpsDB);

        //Den geschätzten Stress mit Hilfe des bisherigen Modells vorhersagen
        double predictedoutput = sgd.predictOutput(container, currentObservationNumber);
        tv_predictedstresslevel.setText(predictedoutput+"");

        //Mithilfe des geschätzten Stress und dem vom Benutzer angegebenen Stress den Fehler des alten
        //Modells berechnen
        double predictionerror = sgd.evaluatePredictionError(predictedoutput, currentObservationNumber);
        tv_predictionerror.setText(Math.round((Math.sqrt(predictionerror)*100.0)/100.0)+"");

        //Mithilfe des ausgerechneten Fehlers neue Koeffizienten berechnen und diese abspeichern.
        sgd.updateCoefficientValues(container, currentObservationNumber,predictionerror);

        //Überprüfe ob weiterhin Personalisierungen notwendig sind, wenn nein, dann sperre die
        //Personalisierungsaufforderungen und die Möglichkeit manuell zu personalisieren.
        boolean terminate = sgd.checkForTermination(container, 10, 2, currentObservationNumber, 10);
        if (terminate = true) {
            //Sperre Personalisierung
            Log.d(TAG, "TERMINATEEEEE.");
        }
        Log.d(TAG, "END.");
    }

    //Öffnet die Stressfragebogen-Oberfläche, und fordert eine Rückmeldung der Oberfläche.
    //Sobald der Stressfragebogen beendet ist, wird diese Aktivität wieder aufgenommen und die
    //Personalisierung fortgesetzt.
    public void StartQuestionnaire(){
        Intent intent = new Intent(Personalization.this, StressQuestionnaire.class);
        Personalization.this.startActivityForResult(intent, 1);

    }











}
