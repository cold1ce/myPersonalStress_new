package com.fimrc.mypersonalstress.gui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fimrc.mypersonalstress.persistence.DatabaseHelper;
import com.fimrc.mysensornetwork.R;

public class Settings extends AppCompatActivity {

    public DatabaseHelper mpsDB;
    public EditText et_timewindow, et_alpha, et_maxpers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.setTitle("Einstellungen");
        mpsDB = new DatabaseHelper(this, "mypersonalstress.db");
        //final Button btn_start = (Button) findViewById(R.id.btn_start);
        //final TextView tv_nextpersonalization = (TextView) findViewById(R.id.tv_nextpersonalization);
        EditText et_timewindow = (EditText) findViewById(R.id.et_timewindow);
        EditText et_alpha = (EditText) findViewById(R.id.et_alpha);
        EditText et_maxpers = (EditText) findViewById(R.id.et_maxpers);

        SharedPreferences prefs = getSharedPreferences("mps_preferences", MODE_PRIVATE);
        double alpha = Double.longBitsToDouble(prefs.getLong("alpha", Double.doubleToLongBits(0.13456789)));
        et_alpha.setText(alpha+"");

    }

    @Override
    //public void onBackPressed() {
      //  super.onBackPressed();
        //overridePendingTransition(R.anim.c, R.anim.comming_out);
    //}

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                //Intent i = new Intent(getApplicationContext(), MainMenu.class);
                //startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
