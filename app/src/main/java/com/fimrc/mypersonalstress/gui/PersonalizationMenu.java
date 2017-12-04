package com.fimrc.mypersonalstress.gui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fimrc.mypersonalstress.persistence.DatabaseHelper;
import com.fimrc.mysensornetwork.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class PersonalizationMenu extends AppCompatActivity {
    private final String TAG = "PersonalizationMenu";

    public boolean dummy=false;
    //public Button btn_start;
    public Handler mHandler;
    public DatabaseHelper mpsDB;
    public Thread t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personalization_menu);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.setTitle("Personalisierung");

        mpsDB = new DatabaseHelper(this, "mypersonalstress.db");
        final Button btn_start = (Button) findViewById(R.id.btn_start);
        btn_start.setEnabled(false);
        final TextView tv_nextpersonalization = (TextView) findViewById(R.id.tv_nextpersonalization);
        tv_nextpersonalization.setText("Berechne Zeitpunkt für nächstmögliche Personalisierung...");




        t = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(3000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                SharedPreferences prefs = getSharedPreferences("mps_preferences", MODE_PRIVATE);
                                int observationtimeframe = prefs.getInt("observationtimeframe", 10);
                                if (mpsDB.getAmountofObservationsDoneYet() > 0) {
                                    DateFormat df = DateFormat.getDateTimeInstance();
                                    long aktuellezeit = new Date().getTime();
                                    long ts = mpsDB.getTimeOfLastPersonalization();
                                    long timespan = aktuellezeit - ts;

                                    int observationtimeframeinmilliseconds = observationtimeframe * 60000;
                                    if (timespan < observationtimeframeinmilliseconds) {
                                        long nxtpossibility = ((observationtimeframeinmilliseconds - timespan) / 60000);
                                        tv_nextpersonalization.setText("Nächste Personalisierung möglich in " + nxtpossibility + " Minute(n).");
                                        btn_start.setEnabled(false);
                                    }
                                    else {
                                        tv_nextpersonalization.setText("Nächste Personalisierung möglich.");
                                        btn_start.setEnabled(true);
                                    }
                                }
                                else {
                                    btn_start.setEnabled(true);
                                    tv_nextpersonalization.setText("Noch keine Personalisierung durchgeführt. Bitte warten Sie mindestens "+observationtimeframe+" Minuten, bevor Sie die erste Personalisierung durchführen.");
                                }
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };
        t.start();

        btn_start.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(PersonalizationMenu.this, Personalization.class);
                PersonalizationMenu.this.startActivity(myIntent);
            }
        });


    }
    public void onStop () {
        t.interrupt();
        super.onStop();
    }
}