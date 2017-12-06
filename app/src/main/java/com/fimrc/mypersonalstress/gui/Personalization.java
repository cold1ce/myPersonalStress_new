//Personalization.Java

//Activity, in welcher die einzelnen Methoden des Personalisierungsalgorithmus in der richtigen
//Reihenfolge nacheinander aufgerufen werden. Stellt auch eine Oberfläche bereit, welche die
//Ergebnisse der Personalisierung anzeigt.

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

    //Beim Start einer neuen Personalisierung werden die notwendigen Datenbanken angesprochen, sowie
    //alle notwendigen Tabellen neu erstellt (Falls diese noch nicht existieren)
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

    //Beginnt eine Personalisierung, mit dem Aufruf der Oberfläche für den Stressfragebogen
    public void startANewPersonalization() {
        StartQuestionnaire();
    }

    //Öffnet die Stressfragebogen-Oberfläche, und fordert eine Rückmeldung der Oberfläche.
    //Sobald der Stressfragebogen beendet ist, wird diese Aktivität (Personalization) wieder
    //aufgenommen und die Personalisierung fortgesetzt.
    public void StartQuestionnaire(){
        Intent intent = new Intent(Personalization.this, StressQuestionnaire.class);
        Personalization.this.startActivityForResult(intent, 1);

    }

    //Wird die Personalization-Activity durch das Beenden eines neuen Stressfragebogens erneut
    // aufgerufen, startet die Activity hier, und setzt durch den Aufruf der continuePersonalization
    // -Methode die Personalisierung fort.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1)
        {
            String message=data.getStringExtra("continue");
            mpsDB = new DatabaseHelper(this, "mypersonalstress.db");
            msnDB = new DatabaseHelper(this, "mySensorNetwork");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            this.setTitle("Personalisierung");
            continuePersonalization();
        }
    }

    //Fortsetzung der Personalisierung nach dem Ausfüllen des Stressfragebogens
    public void continuePersonalization() {
        final TextView tv_title = (TextView) findViewById(R.id.tv_title);
        final TextView tv_pssscore = (TextView) findViewById(R.id.tv_pssscore);
        final TextView tv_predictedstresslevel = (TextView) findViewById(R.id.tv_predictedstresslevel);
        final TextView tv_predictionerror = (TextView) findViewById(R.id.tv_predictionerror);
        final TextView tv_info = (TextView) findViewById(R.id.tv_info);

        SharedPreferences prefs = getSharedPreferences("mps_preferences", MODE_PRIVATE);
        int maxpersonalizations = prefs.getInt("maxpersonalizations", 10);
        int gradienttimewindow = prefs.getInt("gradienttimewindow", 10);
        double alpha = Double.longBitsToDouble(prefs.getLong("alpha", Double.doubleToLongBits(0.1)));
        double sigmatreshold = Double.longBitsToDouble(prefs.getLong("sigmatreshold", Double.doubleToLongBits(0.1)));
        int observationtimeframe = prefs.getInt("observationtimeframe", 10);

        double score = mpsDB.getLastPSSScore();
        tv_pssscore.setText((int)score+"");

        currentObservationNumber = (mpsDB.getAmountofObservationsDoneYet());
        tv_title.setText("Personalisierung "+currentObservationNumber+"/"+maxpersonalizations+" erfolgreich durchgeführt!");

        //Initialisieren aller zu prüfenden Koeffizienten
        CoefficientContainer container = new CoefficientContainer();

        //Initialisieren einer neuen Observations-Instanz, hier werden die neuen Stress-Beobachtungen
        //verarbeitet und in den jeweiligen Hilfstabellen abgespeichert.
        ObservationCalculation observation = new ObservationCalculation(msnDB, mpsDB, currentObservationNumber);

        //Falls es die erste Beobachtung ist, werden die Koeffizienten des General-Models eingefügt
        if (currentObservationNumber == 1) {
            Log.d(TAG, "Füge erste Coefficients Reihe ein(Werte des allgemeinen Stressmodells)");
            mpsDB.addFirstCoefficientsRow();
            observation.writeGeneralModelCoefficients(container);
        }

        //Berechnung einer neuen Stress-Beobachtung starten, aus den SensorDaten von mySensorNetwork
        observation.calculateNewObservations(container, observationtimeframe);

        //Initialisieren einer neuen StochasticGradientDescent-Instanz, hier wird mit Hilfe der
        //inzwischen standardisiert gespeicherten Beobachtungen und dem letzten Stressfragebogen
        //die eigentliche Personalisierung vorgenommen.
        StochasticGradientDescent sgd = new StochasticGradientDescent(mpsDB);

        //Den geschätzten Stress mit Hilfe des bisherigen Modells vorhersagen
        double predictedoutput = sgd.predictOutput(container, currentObservationNumber);
        tv_predictedstresslevel.setText(predictedoutput+"");

        //Mithilfe des vorhergesagten Stress und dem vom Benutzer angegebenen Stress den Fehler des alten
        //Modells berechnen
        double predictionerror = sgd.evaluatePredictionError(predictedoutput, currentObservationNumber);
        tv_predictionerror.setText(predictionerror+"");

        //Mithilfe des ausgerechneten Fehlers neue Koeffizienten berechnen und diese abspeichern.
        sgd.updateCoefficientValues(container, alpha, currentObservationNumber,predictionerror);

        //Überprüfe ob weiterhin Personalisierungen notwendig sind. Wenn nein, dann sperre die
        //Personalisierungsmöglichkeit.
        boolean terminate = sgd.checkForTermination(container, gradienttimewindow, sigmatreshold, currentObservationNumber, maxpersonalizations);
        if (terminate == true) {
            //Sperre Personalisierung
            Log.d(TAG, "Sperre Personalisierung.");
            SharedPreferences.Editor editor = getSharedPreferences("mps_preferences", MODE_PRIVATE).edit();
            editor.putBoolean("personalizationfinished", true);
            editor.apply();
        }
        tv_info.setText("Um ausreichend neue Sensorwerte zu sammeln ist eine erneute Personalisierung des Stressmodels erst in "+observationtimeframe+" Minuten wieder möglich.");

    }













}
