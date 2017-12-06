//Settings.java

//Bietet eine kleine Einstellungs-Oberfläche für die wichtigsten Parameter des Personalisierungs-
//algorithmus. In einer finalen Version ist dieses Menü evtl. nicht mehr notwendig.

package com.fimrc.mypersonalstress.gui;

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

import com.fimrc.mypersonalstress.persistence.DatabaseHelper;
import com.fimrc.mysensornetwork.R;

public class Settings extends AppCompatActivity {
    private final String TAG = "Settings";
    public DatabaseHelper mpsDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.setTitle("Einstellungen");
        mpsDB = new DatabaseHelper(this, "mypersonalstress.db");

        final EditText et_timewindow = (EditText) findViewById(R.id.et_perstimewindow);
        final EditText et_alpha = (EditText) findViewById(R.id.et_alpha);
        final EditText et_maxpers = (EditText) findViewById(R.id.et_maxpers);
        final EditText et_sigmatreshold = (EditText) findViewById(R.id.et_sigmatreshold);
        final EditText et_gradienttimewindow = (EditText) findViewById(R.id.et_gradienttimewindow);

        final SharedPreferences prefs = getSharedPreferences("mps_preferences", MODE_PRIVATE);
        double alpha = Double.longBitsToDouble(prefs.getLong("alpha", Double.doubleToLongBits(0.1)));
        double sigma = Double.longBitsToDouble(prefs.getLong("sigmatreshold", Double.doubleToLongBits(0.1)));

        et_alpha.setText(alpha+"");
        et_timewindow.setText(prefs.getInt("observationtimeframe", 1)+"");
        et_maxpers.setText(prefs.getInt("maxpersonalizations", 1)+"");
        et_sigmatreshold.setText(sigma+"");
        et_gradienttimewindow.setText(prefs.getInt("gradienttimewindow", 1)+"");
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        final EditText et_timewindow = (EditText) findViewById(R.id.et_perstimewindow);
        final EditText et_alpha = (EditText) findViewById(R.id.et_alpha);
        final EditText et_maxpers = (EditText) findViewById(R.id.et_maxpers);
        final EditText et_sigmatreshold = (EditText) findViewById(R.id.et_sigmatreshold);
        final EditText et_gradienttimewindow = (EditText) findViewById(R.id.et_gradienttimewindow);
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                //Intent i = new Intent(getApplicationContext(), MainMenu.class);
                //startActivity(i);
                return true;
            case R.id.save_settings:
                SharedPreferences.Editor editor = getSharedPreferences("mps_preferences", MODE_PRIVATE).edit();
                editor.putLong("alpha", Double.doubleToRawLongBits(Double.parseDouble(et_alpha.getText().toString())));
                editor.putInt("observationtimeframe", Integer.parseInt(et_timewindow.getText().toString()));
                editor.putInt("maxpersonalizations", Integer.parseInt(et_maxpers.getText().toString()));
                editor.putLong("sigmatreshold", Double.doubleToRawLongBits(Double.parseDouble(et_sigmatreshold.getText().toString())));
                editor.putInt("gradienttimewindow", Integer.parseInt(et_gradienttimewindow.getText().toString()));
                editor.apply();
                finish();
                startActivity(getIntent());
                Toast.makeText(getApplicationContext(), "Einstellungen gespeichert.", Toast.LENGTH_SHORT).show();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
