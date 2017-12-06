//MainMenu.java

//Oberfläche, von der aus neue Personalisierungen gestartet werden können und
//Einstellungen oder Impressum abgerufen werden können

package com.fimrc.mypersonalstress.gui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
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

public class MainMenu extends AppCompatActivity {
    private final String TAG = "MainMenu";
    private DatabaseHelper mpsDB, msnDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainmenu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.setTitle("myPersonalStress");

        //Da diese Klasse die erste ist, die nach Installation aufgerufen wird, werden
        //alle nötigen Tabellen angelegt.
        mpsDB = new DatabaseHelper(this, "mypersonalstress.db");
        msnDB = new DatabaseHelper(this, "mySensorNetwork");
        mpsDB.createAllTables();

        //Beim ersten Start die Standardeinstellungen aus write_standard_preferences setzen
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
        final Button btn_dev = (Button) findViewById(R.id.btn_dev);

        //Deaktiviert die Personalisierungsmöglichkeit, wenn eine Abbruchbedinung eingetroffen ist.
        SharedPreferences.Editor edit = getSharedPreferences("mps_preferences", MODE_PRIVATE).edit();
        SharedPreferences pref = getSharedPreferences("mps_preferences", MODE_PRIVATE);
        boolean personalizationfinished = (pref.getBoolean("personalizationfinished", false));
        if ( personalizationfinished == true) {
            Log.d(TAG,"Personalisierungsbutton ausgeschaltet!");
            btn_personalize.setEnabled(false);
        }


        //Deklaration der Buttons auf der Oberfläche
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
                Intent myIntent = new Intent(MainMenu.this, Impress.class);
                MainMenu.this.startActivity(myIntent);
            }
        });

        btn_dev.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(MainMenu.this, DeveloperMenu.class);
                MainMenu.this.startActivity(myIntent);
            }
        });


    }

    //Einstellungen für das Titelbar-Menü
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

    //Funktion, die die Standardeinstellungen beim ersten Aufruf setzt.
    public void write_standard_preferences() {
        SharedPreferences.Editor editor = getSharedPreferences("mps_preferences", MODE_PRIVATE).edit();
        SharedPreferences prefs = getSharedPreferences("mps_preferences", MODE_PRIVATE);
        editor.putLong("alpha", Double.doubleToRawLongBits(0.0001));
        editor.putInt("maxpersonalizations", 20);
        editor.putInt("observationtimeframe", 5);
        editor.putLong("sigmatreshold", Double.doubleToRawLongBits(1.0));;
        editor.putInt("gradienttimewindow", 3);
        editor.putBoolean("personalizationfinished", false);
        editor.apply();
    }


}