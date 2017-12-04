package com.fimrc.mypersonalstress.gui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.fimrc.mypersonalstress.persistence.DatabaseHelper;
import com.fimrc.mysensornetwork.R;
import com.fimrc.mysensornetwork.gui.MainActivity;

import java.text.DateFormat;
import java.util.Date;

public class MainMenu extends AppCompatActivity {
    private final String TAG = "MainMenu";
    private DatabaseHelper mpsDB, msnDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mpsDB = new DatabaseHelper(this, "mypersonalstress.db");
        msnDB = new DatabaseHelper(this, "mySensorNetwork");
        mpsDB.createAllTables();

        setContentView(R.layout.activity_mainmenu);


        SharedPreferences.Editor editor = getSharedPreferences("mps_preferences", MODE_PRIVATE).edit();
        SharedPreferences prefs = getSharedPreferences("mps_preferences", MODE_PRIVATE);
        boolean firststart = prefs.getBoolean("firstStart", true);


        if (firststart == true) {
            editor.putBoolean("firstStart", false);
            editor.apply();
            write_standard_preferences();
        }
        final Button btn_personalize = (Button) findViewById(R.id.btn_personalize);
        final Button btn_settings = (Button) findViewById(R.id.btn_settings);
        final Button btn_impress = (Button) findViewById(R.id.btn_impress);
        final Button btn_reset = (Button) findViewById(R.id.btn_reset);
        final Button btn_add_low = (Button) findViewById(R.id.btn_add_low);
        final Button btn_add_high = (Button) findViewById(R.id.btn_add_high);
        final Button btn_add_random = (Button) findViewById(R.id.btn_add_random);
        final Button btn_add_x= (Button) findViewById(R.id.btn_add_x);
        final Button btn_add_3600= (Button) findViewById(R.id.btn_add_3600);

        final EditText et_valuex = (EditText) findViewById(R.id.et_valuex);

        mpsDB = new DatabaseHelper(this, "mypersonalstress.db");
        msnDB = new DatabaseHelper(this, "mySensorNetwork");
        mpsDB.createAllTables();


        btn_personalize.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(MainMenu.this, PersonalizationMenu.class);
                MainMenu.this.startActivity(myIntent);
            }
        });

        btn_settings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(MainMenu.this, Settings.class);
                MainMenu.this.startActivity(myIntent);
            }
        });

        btn_impress.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(MainMenu.this, PersonalizationMenu.class);
                MainMenu.this.startActivity(myIntent);
            }
        });

        btn_add_random.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                msnDB.createTestSensorTable();
                DateFormat df = DateFormat.getDateTimeInstance();
                long aktuellezeit = new Date().getTime();
                for (int i=0; i<10; i++) {
                    msnDB.addNewTimeTestSensorValues("TestSensor1", "testvalue", getZufallszahl(0, 100));
                    msnDB.addNewTimeTestSensorValues("TestSensor3", "testvalue", getZufallszahl(0, 100));

                }
                for (int j=0; j<(int)(getZufallszahl(0, 20)); j++) {
                    msnDB.addNewEventTestSensorValues("TestSensor2", "testvalue", "event ist passiert");
                }

            }
        });

        btn_add_high.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                msnDB.createTestSensorTable();
                DateFormat df = DateFormat.getDateTimeInstance();
                long aktuellezeit = new Date().getTime();
                for (int i=0; i<10; i++) {
                    msnDB.addNewTimeTestSensorValues("TestSensor1", "testvalue", getZufallszahl(15, 16));
                    //msnDB.addNewTimeTestSensorValues("TestSensor3", "testvalue", getZufallszahl(0, 5));

                }
                for (int j=0; j<(int)(getZufallszahl(17, 20)); j++) {
                    //msnDB.addNewEventTestSensorValues("TestSensor2", "testvalue", "event ist passiert");
                }

            }
        });

        btn_add_low.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                msnDB.createTestSensorTable();
                DateFormat df = DateFormat.getDateTimeInstance();
                long aktuellezeit = new Date().getTime();
                    msnDB.addNewTimeTestSensorValues("TestSensor1", "testvalue", getZufallszahl(0, 1));
                    //msnDB.addNewTimeTestSensorValues("TestSensor3", "testvalue", getZufallszahl(95, 100));

                for (int j=0; j<(int)(getZufallszahl(0, 3)); j++) {
                    //msnDB.addNewEventTestSensorValues("TestSensor2", "testvalue", "event ist passiert");
                }

            }
        });

        btn_add_3600.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                msnDB.createTestSensorTable();
                DateFormat df = DateFormat.getDateTimeInstance();
                long aktuellezeit = new Date().getTime();
                for (int i=0; i<3600; i++) {
                    msnDB.addNewTimeTestSensorValues("TestSensor1", "testvalue", getZufallszahl(1, 500));
                    Log.d(TAG, "Wert "+i+" angelegt.");
                }
            }
        });

        btn_add_x.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                msnDB.createTestSensorTable();
                DateFormat df = DateFormat.getDateTimeInstance();
                long aktuellezeit = new Date().getTime();
                double value = Double.parseDouble(et_valuex.getText().toString());
                //for (int i=0; i<10; i++) {
                    msnDB.addNewTimeTestSensorValues("TestSensor1", "testvalue", value);


                //}
                //for (int j=0; j<(int)(getZufallszahl(0, 3)); j++) {
                    //msnDB.addNewEventTestSensorValues("TestSensor2", "testvalue", "event ist passiert");
                //}

            }
        });

        btn_reset.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                msnDB.resetMSNDB();
                mpsDB.resetMPSDB();
                mpsDB.createAllTables();
                Log.d(TAG, "Tabellen in beiden Datenbanken gelöscht.");
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
                Intent myIntent1 = new Intent(MainMenu.this, MainMenu.class);
                MainMenu.this.startActivity(myIntent1);
                return true;

            case R.id.sensorsettings:
                Intent myIntent2 = new Intent(MainMenu.this, MainActivity.class);
                MainMenu.this.startActivity(myIntent2);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    public double getZufallszahl(int min, int max) {
        double  random = Math.random() * (max - min) + min;
        random = Math.round(random * 100.0) / 100.0;
        return random;
    }

    public void write_standard_preferences() {
        SharedPreferences.Editor editor = getSharedPreferences("mps_preferences", MODE_PRIVATE).edit();
        SharedPreferences prefs = getSharedPreferences("mps_preferences", MODE_PRIVATE);
        editor.putLong("alpha", Double.doubleToRawLongBits(0.0001));
        editor.putInt("maxpersonalizations", 7);
        editor.putInt("observationtimeframe", 2);
        editor.putLong("sigmatreshold", Double.doubleToRawLongBits(1.0));;
        editor.putInt("gradienttimewindow", 10);
        editor.apply();
    }


}