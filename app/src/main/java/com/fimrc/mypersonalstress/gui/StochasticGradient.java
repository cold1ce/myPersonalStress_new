package com.fimrc.mypersonalstress.gui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.fimrc.mypersonalstress.coefficients.Coefficient;
import com.fimrc.mypersonalstress.coefficients.CoefficientContainer;
import com.fimrc.mypersonalstress.persistence.DatabaseHelper;
import com.fimrc.mysensornetwork.R;
import com.fimrc.mysensornetwork.gui.MainActivity;

public class StochasticGradient extends AppCompatActivity {
    private final String TAG = "StochasticGradient";
    public DatabaseHelper mpsDB, msnDB;
    int currentObservationNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "Start Stochastic Gradient Klasse");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stochastic_gradient);
        mpsDB = new DatabaseHelper(this, "mypersonalstress.db");
        msnDB = new DatabaseHelper(this, "mySensorNetwork");
        //Log.d(TAG, "Aufruf: createAllTables"); // DOPPELT=!=!=
        //mpsDB.createAllTables();                    //DOPEPLT!!!
        Log.d(TAG, "Hole den letzten PSS Score");
        double score = mpsDB.getLastPSSScore();
        //Log.d(TAG, "Gebe den Score aus");
        //Toast.makeText(getApplicationContext(),"Score: "+score, Toast.LENGTH_SHORT).show();
        Log.d(TAG, "Dieser ist: "+score);
        currentObservationNumber = (mpsDB.getAmountofObservationsDoneYet());

        Log.d(TAG, "Aufruf: initialisiere KoeffizientenKontainer");
        CoefficientContainer con = new CoefficientContainer();
        Log.d(TAG, "Aufruf des coefCalcLooooops");
        coefCalcLoop(con);
        Log.d(TAG, "EEEEEEEEEEEEEENNNNnDDDDDDDDDDDDDEEEEEEEEEE");
    }

    public void coefCalcLoop(CoefficientContainer cc) {
        //for (int i=1; i<cc.coefficients.length; i++) { ////change
            for (int i=1; i<4; i++) {
                Log.d(TAG, "____________________________________________________________________________________");
                Log.d(TAG, "Berechnung: Koeffizient Nr."+i+" mit dem Namen: "+cc.coefficients[i].name);
                calcTheCoeff(cc.coefficients[i], currentObservationNumber);
            }
    }

    public void calcTheCoeff (Coefficient c, int observNum) {
        Log.d(TAG, "Koeffizient empfangen ist: "+c.name+" mit der Beobachtungsnr.: "+observNum);
        double newaggregationvalue = 0.0;
        Log.d(TAG, "Hole Beobachtung unstandardisiert aus mSN-Datenbank...");
        if (c.transformation1 == "raw") {
            Log.d(TAG, "Transformation 1 ist: raw");
            if (c.transformation2 == "none") {
                Log.d(TAG, "Transformation 2 ist: none");
                if (c.aggregation == "max") {
                    Log.d(TAG, "Aggregation ist: max");
                    newaggregationvalue = msnDB.getAggrMax(c.sensorname, c.sensorvalue, 60);
                }
                else if (c.aggregation == "mean") {
                    newaggregationvalue = msnDB.getAggrMean(c.sensorname, c.sensorvalue, 60);
                }
            }
        }
        Log.d(TAG, "Wert "+newaggregationvalue+" gelesen für "+c.name);
        standardisizeAggrValue(c, newaggregationvalue, observNum);
    }

    public void standardisizeAggrValue(Coefficient c, double valuex, int observNumN) {
        Log.d(TAG, "Starte die Standardisierung des Aggregierten Wertes x="+valuex+" - Beob.-Nr.: "+observNumN);
        Log.d(TAG, "Wert runden...");
        valuex = Math.round(valuex * 100000.0) / 100000.0;
        Log.d(TAG, "Neuer Wert: "+valuex);
        Log.d(TAG, "Rufe die getOldMean Methode auf");
        double oldmeanbuffer = mpsDB.getOldMean(c, observNumN);
        Log.d(TAG, "Alter u-Wert von Funktion returned ist: "+oldmeanbuffer);
        Log.d(TAG, "Rufe die calcNewMean Methode auf");
        double newmeanbuffer = calcNewMean(valuex, oldmeanbuffer, observNumN);
        Log.d(TAG, "Aufruf addNewMean: "+newmeanbuffer+" in die DB");
        mpsDB.addNewMean(c, observNumN, newmeanbuffer);



        Log.d(TAG, "Rufe die getOldStdDer Methode auf");
        double oldstdderbuffer = mpsDB.getOldStdDer(c, observNumN);
        Log.d(TAG, "Alter o-Wert von Funktion returned ist: "+oldstdderbuffer);
        Log.d(TAG, "Rufe die calcNewStdDer Methode auf");
        double newstdderbuffer = calcNewStdDer(valuex, newmeanbuffer, observNumN, oldstdderbuffer);
        Log.d(TAG, "Schreibe den neue StdDer: "+newstdderbuffer+" in die DB");
        mpsDB.addNewStdDer(c, observNumN, newstdderbuffer);



        Log.d(TAG, "Standardisiere nun letzendlich den Wert");
        double zvaluebuffer = standardizise(valuex, newmeanbuffer, newstdderbuffer, observNumN);
        Log.d(TAG, "Standardisiererter Wert: "+zvaluebuffer+" - Dieser wird nun geschrieben...");
        mpsDB.addNewObservationValue(c, observNumN, zvaluebuffer);

    }

    public double calcNewMean (double x, double u, int N) {
        Log.d(TAG, "Berechne neues u:"+u+"+(("+x+"-"+u+")/"+N+")");
        double unew = u+((x-u)/N);
        Log.d(TAG, "Neues u: "+unew);
        if (Double.isNaN(unew) == true) {
            unew = 0.0;
        }
        Log.d(TAG, "Wert runden...");
        unew = Math.round(unew * 10000000000.0) / 10000000000.0;
        Log.d(TAG, "Neuer Wert: "+unew);
        return unew;
    }

    public double calcNewStdDer (double x, double u, int N, double o) {
        System.out.println("übergeben: x:"+x+" u: "+u+" N: "+N+" o: "+o);
        double zaehlerpart1 = (N+1);
        double zaehlerpart2 = Math.pow(x, 2)+N*(Math.pow(o, 2)+Math.pow(u, 2));
        System.out.println("x+n*u ist: "+(x+N*u));
        double zaehlerpart3 = Math.pow((x+(N*u)), 2);
        double zaehler = zaehlerpart1*zaehlerpart2-zaehlerpart3;
        System.out.println("o-Berechnungen||Part1: "+zaehlerpart1+" - Part2: "+zaehlerpart2+" - Part3: "+zaehlerpart3);
        double nenner = Math.pow((N+1), 2);
        System.out.println("o-Berechnungen||Zähler: "+zaehler+" Nenner: "+nenner);
        double onew = Math.sqrt(zaehler/nenner);
        Log.d(TAG, "Die neue StdDer bzw o ist: "+onew);
        if (Double.isNaN(onew) == true) {
            onew = 0.0;
        }
        Log.d(TAG, "Wert runden...");
        onew = Math.round(onew * 10000000000.0) / 10000000000.0;
        Log.d(TAG, "Neuer Wert: "+onew);
        return onew;
    }

    public double standardizise(double x, double u, double o, int N) {
        Log.d(TAG, "Standardisierung gestartet, mit x: "+x+" u: "+u+" o: "+o+" N: "+N);
        double unew = u+((x-u)/(N+1));
        Log.d(TAG, "Neues u: "+unew);
        double onew = Math.sqrt(((N+1)*(Math.pow(x, 2)+N*(Math.pow(o, 2)+Math.pow(u, 2)))-(Math.pow((x+(N*u)), 2)))/(Math.pow((N+1), 2)));
        Log.d(TAG, "Neues o: "+onew);
        double z = ((x-unew)/onew);
        Log.d(TAG, "z-Wert(Standardisierte Beobachtung): "+z);
        if (Double.isNaN(z) == true) {
            z = 0.0;
        }
        Log.d(TAG, "Wert runden...");
        z = Math.round(z * 10000000000.0) / 10000000000.0;
        Log.d(TAG, "Neuer Wert: "+z);
        return z;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.lay, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.personalization:
                Intent myIntent1 = new Intent(StochasticGradient.this, Testfield.class);
                StochasticGradient.this.startActivity(myIntent1);
                return true;
            case R.id.sensorsettings:
                Intent myIntent2 = new Intent(StochasticGradient.this, MainActivity.class);
                StochasticGradient.this.startActivity(myIntent2);
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
