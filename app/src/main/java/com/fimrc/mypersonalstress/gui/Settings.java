package com.fimrc.mypersonalstress.gui;

import android.app.AlarmManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Switch;

import com.fimrc.mysensornetwork.R;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        final Switch switch1 = (Switch) findViewById(R.id.switch1);


    }
}
