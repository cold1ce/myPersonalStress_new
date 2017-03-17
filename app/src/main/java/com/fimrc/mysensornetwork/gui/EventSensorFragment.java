package com.fimrc.mysensornetwork.gui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fimrc.mysensornetwork.R;

/**
 * Created by Sven on 17.03.2017.
 */

public class EventSensorFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            // restore internal values if necessary
        }
        View myView = inflater.inflate(R.layout.activity_main, container, false);
        return myView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save internal values if necessary
    }
}
