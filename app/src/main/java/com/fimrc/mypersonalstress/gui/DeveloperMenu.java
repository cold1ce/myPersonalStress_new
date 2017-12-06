package com.fimrc.mypersonalstress.gui;

import android.content.Intent;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.fimrc.mypersonalstress.persistence.DatabaseHelper;
import com.fimrc.mysensornetwork.R;
import com.fimrc.mysensornetwork.gui.MainActivity;

import java.text.DateFormat;
import java.util.Date;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class DeveloperMenu extends AppCompatActivity {
    private final String TAG = "DevMenu";
    private DatabaseHelper mpsDB, msnDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer_menu);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.setTitle("Entwickler-Menü");
        mpsDB = new DatabaseHelper(this, "mypersonalstress.db");
        msnDB = new DatabaseHelper(this, "mySensorNetwork");



        final Button btn_reset = (Button) findViewById(R.id.btn_reset);
        final Button btn_add_low = (Button) findViewById(R.id.btn_add_low);
        final Button btn_add_high = (Button) findViewById(R.id.btn_add_high);
        final Button btn_add_random = (Button) findViewById(R.id.btn_add_random);
        final Button btn_add_x= (Button) findViewById(R.id.btn_add_x);
        final Button btn_add_3600= (Button) findViewById(R.id.btn_add_3600);
        final EditText et_valuex = (EditText) findViewById(R.id.et_valuex);
        final CompoundButton sw_1 = (CompoundButton) findViewById(R.id.sw_1);


        SharedPreferences prefs = getSharedPreferences("mps_preferences", MODE_PRIVATE);
        boolean deactivated = prefs.getBoolean("personalizationfinished", false);
        if (deactivated == true) {
            sw_1.isChecked();
        }
        if (deactivated == false) {
            //
        }





        btn_add_random.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                msnDB.createTestSensorTable();
                DateFormat df = DateFormat.getDateTimeInstance();
                long aktuellezeit = new Date().getTime();
                for (int i=0; i<10; i++) {
                    msnDB.addNewTimeTestSensorValues("TestSensor1", "testvalue", getZufallszahl(0, 100));
                    //msnDB.addNewTimeTestSensorValues("TestSensor3", "testvalue", getZufallszahl(0, 100));

                }
                for (int j=0; j<(int)(getZufallszahl(0, 20)); j++) {
                    //msnDB.addNewEventTestSensorValues("TestSensor2", "testvalue", "event ist passiert");
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
                SharedPreferences.Editor editor = getSharedPreferences("mps_preferences", MODE_PRIVATE).edit();
                SharedPreferences prefs = getSharedPreferences("mps_preferences", MODE_PRIVATE);
                editor.putLong("alpha", Double.doubleToRawLongBits(0.0001));
                editor.putInt("maxpersonalizations", 7);
                editor.putInt("observationtimeframe", 5);
                editor.putLong("sigmatreshold", Double.doubleToRawLongBits(1.0));;
                editor.putInt("gradienttimewindow", 10);
                editor.putBoolean("personalizationfinished", false);
                editor.apply();
            }
        });

        sw_1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = getSharedPreferences("mps_preferences", MODE_PRIVATE).edit();
                SharedPreferences prefs = getSharedPreferences("mps_preferences", MODE_PRIVATE);
                if (isChecked == true) {
                    Log.d(TAG, "Personalisierung gesperrt.");
                    editor.putBoolean("personalizationfinished", true);
                    editor.apply();
                }
                if (isChecked == false) {
                    Log.d(TAG, "Personalisierung frei.");
                    editor.putBoolean("personalizationfinished", false);
                    editor.apply();
                }
            }
        });

    }



    public double getZufallszahl(int min, int max) {
        double  random = Math.random() * (max - min) + min;
        random = Math.round(random * 100.0) / 100.0;
        return random;
    }



}