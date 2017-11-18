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
        final Button btn_printstress = (Button) findViewById(R.id.btn_printstress);
        final Button btn_settings = (Button) findViewById(R.id.btn_settings);
        final Button btn_impress = (Button) findViewById(R.id.btn_impress);
        final Button btn_reset = (Button) findViewById(R.id.btn_reset);
        final Button btn_testsensor_1 = (Button) findViewById(R.id.btn_testsensor_1);

        mpsDB = new DatabaseHelper(this, "mypersonalstress.db");
        msnDB = new DatabaseHelper(this, "mySensorNetwork");
        mpsDB.createAllTables();

        btn_printstress.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(MainMenu.this, StressPrediction.class);
                MainMenu.this.startActivity(myIntent);
            }
        });

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

        btn_testsensor_1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                msnDB.createTestSensorTable();
                DateFormat df = DateFormat.getDateTimeInstance();
                long aktuellezeit = new Date().getTime();
                for (int i=0; i<10; i++) {
                    msnDB.addNewTestSensorValues(aktuellezeit, getZufallszahl(0, 100));
                }
                Log.d(TAG, "Dummy-Werte für den Testsensor erstellt.");
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
        return random;
    }

    public void write_standard_preferences() {
        SharedPreferences.Editor editor = getSharedPreferences("mps_preferences", MODE_PRIVATE).edit();
        SharedPreferences prefs = getSharedPreferences("mps_preferences", MODE_PRIVATE);
        editor.putLong("alpha", Double.doubleToRawLongBits(0.00001));
        editor.putInt("maxpersonalizations", 10);
        editor.putInt("observationtimeframe", 60);
        editor.apply();
    }


}