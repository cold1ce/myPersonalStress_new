package com.fimrc.mysensornetwork.gui;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.fimrc.mysensornetwork.R;
import com.fimrc.mysensornetwork.SensorService;
import com.fimrc.mysensornetwork.sensors.SensorContainer;
import com.fimrc.sensorfusionframework.sensors.SensorManager;

import java.util.ArrayList;


/**
 * Created by Sven on 17.03.2017.
 */

public class EventSensorFragment extends Fragment {

    private View myView;
    private ListView listView;
    private String[] eventSensorList;
    private String[] eventSensorDescriptionList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("Fragment", "on CreateView - EventSensorFragment");

        myView = inflater.inflate(R.layout.event_sensor_fragment, container, false);

        eventSensorList = new String[SensorContainer.eventSensorCount()];
        for(int i = 0; i< SensorContainer.eventSensorCount();i++){
            eventSensorList[i] = SensorContainer.getSensor(i).eventSensor.toString();
        }

        listView = (ListView)myView.findViewById(R.id.event_listView);
        eventSensorDescriptionList = getResources().getStringArray(R.array.eventSensorDescription);
        myAdapter adapter = new myAdapter(container.getContext());
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(!SensorManager.instance().getSensor(position).isActive()){
                    ((MainActivity)getActivity()).sendSensorActionToService(SensorService.ACTIVATE_SENSOR, position);
                    ((MainActivity)getActivity()).sendSensorActionToService(SensorService.START_LOGGING, position);
                    ((ImageView)(view.findViewById(R.id.single_row_event_imageView))).setImageResource(R.drawable.sensor_on);
                }else{
                    ((MainActivity)getActivity()).sendSensorActionToService(SensorService.STOP_LOGGING, position);
                    ((MainActivity)getActivity()).sendSensorActionToService(SensorService.DEACTIVATE_SENSOR, position);
                    ((ImageView)(view.findViewById(R.id.single_row_event_imageView))).setImageResource(R.drawable.sensor_off);
                }
            }
        });

        return myView;
    }

    class myAdapter extends ArrayAdapter<String>{

        Context context;
        MyViewHolder holder;

        public myAdapter(Context context) {
            super(context, R.layout.single_row_event, R.id.single_row_event_sensorName, eventSensorList);
            this.context = context;
        }

        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View row = convertView;
            holder = null;
            if(row == null) { //erster Aufruf
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.single_row_event, parent, false);
                holder = new MyViewHolder(row);
                row.setTag(holder);
            }else{ //recycling
                holder = (MyViewHolder)row.getTag();
            }

            if(SensorManager.instance().getSensor(position).isActive())
                holder.myImage.setImageResource(R.drawable.sensor_on);
            else
                holder.myImage.setImageResource(R.drawable.sensor_off);

            holder.mySensorName.setText(SensorContainer.getSensor(position).eventSensor.toString());
            holder.mySensorDescription.setText(eventSensorDescriptionList[position]);

            return row;
        }
    }
    class MyViewHolder {
        ImageView myImage;
        TextView mySensorName;
        TextView mySensorDescription;

        MyViewHolder(View v){
            myImage=(ImageView)v.findViewById(R.id.single_row_event_imageView);
            mySensorName = (TextView)v.findViewById(R.id.single_row_event_sensorName);
            mySensorDescription = (TextView)v.findViewById(R.id.single_row_event_sensorDescription);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save internal values if necessary
    }

}
