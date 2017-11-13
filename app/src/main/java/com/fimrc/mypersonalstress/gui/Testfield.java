package com.fimrc.mypersonalstress.gui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.fimrc.mypersonalstress.persistence.DatabaseHelper;
import com.fimrc.mysensornetwork.R;
import com.fimrc.mysensornetwork.gui.MainActivity;

import java.text.DateFormat;
import java.util.Date;

//import com.fimrc.mypersonalstress.persistence.AndroidDatabaseManager;

public class Testfield extends AppCompatActivity {
    private final String TAG = "Testfield";
    private DatabaseHelper mpsDB, msnDB;

    //Testveränderung
    //Testveränderung 2 am Desktop PC

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testfield);
        final Button button1 = (Button) findViewById(R.id.button1);
        final Button button2 = (Button) findViewById(R.id.button2);
        final Button button3 = (Button) findViewById(R.id.button3);
        final Button button4 = (Button) findViewById(R.id.button4);
        final Button button5 = (Button) findViewById(R.id.button5);
        final Button button6 = (Button) findViewById(R.id.button6);
        final Button button7 = (Button) findViewById(R.id.button7);
        final Button button8 = (Button) findViewById(R.id.button8);
        final Button button9 = (Button) findViewById(R.id.button9);
        mpsDB = new DatabaseHelper(this, "mypersonalstress.db");
        msnDB = new DatabaseHelper(this, "mySensorNetwork");

        //Intent myIntent = new Intent(MainActivity.this, StressQuestionnaire.class);
        //myIntent.putExtra("key", value); //Optional parameters
        //MainActivity.this.startActivity(myIntent);


        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(Testfield.this, Personalization.class);
                //myIntent.putExtra("key", value); //Optional parameters
                Testfield.this.startActivity(myIntent);


            }
        });

       /* button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent2 = new Intent(Testfield.this, AndroidDatabaseManager.class);
                //myIntent.putExtra("key", value); //Optional parameters
                Testfield.this.startActivity(myIntent2);
            }
        }); */

        /*button3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mpsDB.createCoefficientsTable();
                DateFormat df = DateFormat.getDateTimeInstance();
                long aktuellezeit = new Date().getTime();
                mpsDB.addNewCoefficients(aktuellezeit,  mpsDB.getAggrMin(),  getZufallszahl(-1,1));
            }
        });*/

        button4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mpsDB.createTestSensorTable();
                DateFormat df = DateFormat.getDateTimeInstance();
                long aktuellezeit = new Date().getTime();
                for (int i=0; i<10; i++) {
                    mpsDB.addNewTestSensorValues(aktuellezeit, getZufallszahl(0, 100));
                }
            }
        });

        button5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                double min = 0.0;
                min = mpsDB.getAggrMin();
                Toast.makeText(getApplicationContext(),"Score: "+min, Toast.LENGTH_SHORT).show();
            }
        });

       /* button6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                double x = 3.0;
                double ualt = 4.5;
                double oalt = 0.5;
                int N = 2;
                //double uneu = mpsDB.updateMeans(x, ualt, N);
                Snackbar mySnackbar = Snackbar.make(findViewById(android.R.id.content), "uneu: "+uneu+" o: "+mpsDB.updateStandardDeviation(x, uneu, N, oalt), Snackbar.LENGTH_SHORT);
                mySnackbar.show();
                double zval = mpsDB.standardizise(x, ualt, oalt, N);
                Snackbar mySnackbar = Snackbar.make(findViewById(android.R.id.content), "z-Value: "+zval, Snackbar.LENGTH_LONG);
                mySnackbar.show();



            }
        }); */


        button8.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
            Log.d(TAG, "Aufruf createTestSensorTable");
            msnDB.createTestSensorTable();
            DateFormat df = DateFormat.getDateTimeInstance();
            long aktuellezeit = new Date().getTime();
            Log.d(TAG, "Erstelle 10 Messwerte zwischen 0 und 100");
            for (int i=0; i<10; i++) {
                msnDB.addNewTestSensorValues(aktuellezeit, getZufallszahl(0, 100));
            }
            Log.d(TAG, "10 Testwerte erstellt.");
        }
    });


        button9.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
            Log.d(TAG, "Reset gestartet");
            msnDB.resetMSNDB();
            mpsDB.resetMPSDB();
        }
    });
}


    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.lay, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.personalization:
                Intent myIntent1 = new Intent(Testfield.this, Testfield.class);
                Testfield.this.startActivity(myIntent1);
                return true;

            case R.id.sensorsettings:
                Intent myIntent2 = new Intent(Testfield.this, MainActivity.class);
                Testfield.this.startActivity(myIntent2);
                return true;


            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }




    public double getZufallszahl(int min, int max) {
        double  random = Math.random() * (max - min) + min;
        return random;
    }
}