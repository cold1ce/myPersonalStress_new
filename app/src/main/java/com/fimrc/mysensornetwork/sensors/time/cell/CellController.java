package com.fimrc.mysensornetwork.sensors.time.cell;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.provider.Settings;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;


import com.fimrc.jdcf.persistence.container.SensorRecord;
import com.fimrc.jdcf.persistence.structure.SensorDataType;
import com.fimrc.jdcf.sensors.SensorModule;
import com.fimrc.jdcf.sensors.time.SensorTimeController;

import java.util.Date;

/**
 * Created by Sven on 10.03.2017.
 */

public class CellController extends SensorTimeController {

    private TelephonyManager tm;
    private Handler mHandler;
    private Context context;

    // sensor data
    private int cellID, cellLac, mcc, signal, signal_bar, data_state, roaming = 0;
    private CellSensorListener cellHandler;
    private boolean startedData = false, startedSignal = false, startedLocation = false;
    private boolean initialized = false;


    public CellController(SensorModule module, Context context) {
        super(module);
        this.context = context;
    }

    @Override
    protected SensorRecord buildSensorRecord() {
        Date date = new Date(System.currentTimeMillis());
        SensorRecord record = new SensorRecord(module.getNextIndex(), date , structure);

        //HashMap<String, String> tmp = new HashMap<String, String>();
        HandlerThread mHandlerThread = new HandlerThread("CellSensorThread");;

        // try getting phone manager
        tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        if (tm == null){
            return record;
        }

        // if it is not a GSM phone, return right away
        if (tm.getPhoneType() != TelephonyManager.PHONE_TYPE_GSM){
            return record;
        }

        // register my CellSensorListener for getting signal strength, location
        // changes and data connection state events
        // but only if airplane mode is not enabled!
        if (Settings.System.getInt(context.getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, 0) != 0){
            return record;
        }

        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper(), new CellSensorCallBack());
        mHandler.sendEmptyMessage(0);

        while(!initialized || startedLocation || startedSignal || startedData){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        record.addData("cellID", SensorDataType.INTEGER, cellID);
        record.addData("cellLac", SensorDataType.INTEGER, cellLac);
        record.addData("roaming", SensorDataType.INTEGER, roaming);
        record.addData("signal", SensorDataType.INTEGER, signal);
        record.addData("signal_bar", SensorDataType.INTEGER, signal_bar);
        record.addData("data_state", SensorDataType.INTEGER, data_state);
        record.addData("mcc", SensorDataType.INTEGER, mcc);

        tm.listen(cellHandler, PhoneStateListener.LISTEN_NONE);
        mHandlerThread.quit();

        return record;
    }


    class CellSensorListener extends PhoneStateListener {
        /**
         * Called when the data connection state has changed (e.g., being
         * disconnected)
         *
         * @param state new state that has been detected
         * @see android.telephony.PhoneStateListener#onDataConnectionStateChanged(int)
         */
        @Override
        public void onDataConnectionStateChanged(int state) {
            if (state == TelephonyManager.DATA_CONNECTED)
                data_state = 1;
            if (state == TelephonyManager.DATA_DISCONNECTED)
                data_state = 0;

            startedData = false;
        }

        /**
         * Called when the signal strength has changed
         *
         * @param signalStrength strength of the newly detected signal. First, we get the ASU
         *                       by calling getGsmSignalStrength(), then we calculate the dBm
         *                       through dBm = -113 + 2* ASU
         * @see android.telephony.PhoneStateListener#onSignalStrengthsChanged(android.telephony.SignalStrength)
         */
        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            // signal strength in ASU
            int strength = signalStrength.getGsmSignalStrength();

            // convert ASU in dBm
            signal = -113 + 2 * strength;

            if (strength <= 2 || strength == 99)
                signal_bar = 0;
            else if (strength >= 12)
                signal_bar = 4;
            else if (strength >= 8)
                signal_bar = 3;
            else if (strength >= 5)
                signal_bar = 2;
            else
                signal_bar  = 1;

            startedSignal = false;
        }

        /**
         * Called when the cell identifier or LAC changed - read the new value and
         * release appropriate semaphore
         *
         * @param location Reference to the new {@link android.telephony.CellLocation}
         *                 that has been detected
         * @see android.telephony.PhoneStateListener#onCellLocationChanged(android.telephony.CellLocation)
         */
        @Override
        public void onCellLocationChanged(CellLocation location) {
            cellID = ((GsmCellLocation) location).getCid();
            cellLac = ((GsmCellLocation) location).getLac();

            try {
                String networkOperator = tm.getNetworkOperator();
                if (networkOperator != null)
                    mcc = Integer.parseInt(networkOperator.substring(0, 3));
                else
                    mcc = 0;
            } catch (Exception e) {
                mcc = 0;
            }

            startedLocation = false;
        }
    }

    class CellSensorCallBack implements Handler.Callback {
        @Override
        public boolean handleMessage(Message msg) {
            int events = 0;

            events |= PhoneStateListener.LISTEN_DATA_CONNECTION_STATE;
            startedData = true;

            events |= PhoneStateListener.LISTEN_SIGNAL_STRENGTHS;
            startedSignal = true;

            events |= PhoneStateListener.LISTEN_CELL_LOCATION;
            startedLocation = true;

            if(events == 0)
                return false;

            // register CellSensorListener now
            cellHandler = new CellSensorListener();
            tm.listen(cellHandler, events);
            initialized = true;
            return true;
        }
    }

}
