package com.fimrc.mysensornetwork.persistence;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.fimrc.sensorfusionframework.persistence.PersistenceLogger;
import com.fimrc.sensorfusionframework.persistence.container.SensorRecord;
import com.fimrc.sensorfusionframework.persistence.structure.SensorRecordStructure;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

/**
 * Created by Sven on 17.02.2017.
 */

public class DatabaseLogger extends PersistenceLogger {

    private mySQLiteHelper sqLiteHelper;
    /*
    public DatabaseLogger(Context context){
        super();
        sqLiteHelper = sqLiteHelper.getInstance(context);
    }
    */
    @Override
    public void initialize(Context context){
        try{
            super.initialize(context);
            sqLiteHelper = sqLiteHelper.getInstance(context);
            sqLiteHelper.cleanDatabase();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public Iterator<SensorRecord> readAllRecords(SensorRecordStructure structure) {
        SQLiteDatabase database = sqLiteHelper.getDatabase();
        return null;
    }

    @Override
    public void modifyAllRecords(Iterator<SensorRecord> iterator) {

    }

    @Override
    protected void log(SensorRecord record) {
        SQLiteDatabase database = sqLiteHelper.getDatabase();
        ArrayList<String> line = convertSensorRecord(record);
        StringBuilder sb = new StringBuilder();
        Date timestamp = record.getTimestamp();
        Format format = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
        sb.append("INSERT INTO "+ sqLiteHelper.TABLE_SENSORDATA +" VALUES ('" + record.getID() + "', '" + format.format(timestamp)+"', 'ScreenSensor'");
        for(int i = 0; i<9;i++){
            sb.append(", '");
            if(i<line.size())
                sb.append(line.get(i));
            else
                sb.append(" ");
            sb.append("'");
        }
        sb.append(")");
        System.out.println(sb.toString());
        database.execSQL(sb.toString());
    }

    @Override
    protected void finalize() {}

    private ArrayList<String> convertSensorRecord(SensorRecord record) {
        ArrayList<String> line = new ArrayList();
        for (Object data : record) {
            line.add(data.toString());
        }
        return line;
    }

    public void print(){
        SQLiteDatabase database = sqLiteHelper.getDatabase();
        String SQL = "SELECT * FROM "+sqLiteHelper.TABLE_SENSORDATA;
        Cursor c = database.rawQuery(SQL,null);
        if(c.moveToFirst()){
            do{
                String id = c.getString(0);
                String ts = c.getString(1);
                String sensor = c.getString(2);
                String column1 = c.getString(3);
                String column2 = c.getString(4);
                String column3 = c.getString(5);
                String column4 = c.getString(6);
                String column5 = c.getString(7);
                String column6 = c.getString(8);
                String column7 = c.getString(9);
                String column8 = c.getString(10);
                String column9 = c.getString(11);
                System.out.println(id+" "+ts+" "+sensor+" "+column1+" "+column2+" "+column3+" "+column4+" "+column5+" "+column6+" "+column7+" "+column8+" "+column9);
            }while(c.moveToNext());
        }
        c.close();
    }

}