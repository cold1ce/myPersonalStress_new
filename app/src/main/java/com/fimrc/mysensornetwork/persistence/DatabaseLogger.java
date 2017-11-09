package com.fimrc.mysensornetwork.persistence;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.fimrc.jdcf.persistence.PersistenceLogger;
import com.fimrc.jdcf.persistence.container.SensorDataField;
import com.fimrc.jdcf.persistence.container.SensorRecord;
import com.fimrc.jdcf.persistence.structure.SensorRecordStructure;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Sven on 17.02.2017.
 */

public class DatabaseLogger extends PersistenceLogger {

    private mySQLiteHelper sqLiteHelper;
    private SensorRecordStructure structure;
    private Context context;

    public DatabaseLogger(String sensorName, SensorRecordStructure structure, Context context){
        try{
            this.structure = structure;
            this.context = context;
            sqLiteHelper = new mySQLiteHelper(sensorName, structure, context);
            sqLiteHelper.cleanDatabase();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void modifyAllRecords(Iterator<SensorRecord> iterator) {

    }

    @Override
    protected void writeSensorRecord(SensorRecord record) {
        SQLiteDatabase database = sqLiteHelper.getDatabase();
        ArrayList<Object> sensorRecordLine = convertSensorRecord(record);
        StringBuilder sb = new StringBuilder();
        StringBuilder values = new StringBuilder();
        Date timestamp = record.getTimestamp();
        Format format = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
        sb.append("INSERT INTO "+ sqLiteHelper.TableName+" ("+sqLiteHelper.COLUMN_TIMESTAMP);
        for(int i=0; i<structure.size();i++){
            if(sensorRecordLine.get(i) != null){
                sb.append(",'"+ structure.getNameAtIndex(i)+"'");
                values.append(",'"+sensorRecordLine.get(i)+"'");
            }
        }
        sb.append(") VALUES ('" + format.format(timestamp)+"'"+values.toString()+")");
        Log.d("Database",sb.toString());
        Log.d("Database", String.valueOf(Thread.currentThread().getId()));
        database.execSQL(sb.toString());
    }

    @Override
    protected void finalizeLogging() {

    }

    @Override
    protected void finalize() {}

    private ArrayList<Object> convertSensorRecord(SensorRecord record) {
        ArrayList<Object> line = new ArrayList<>();
        for (Object data : record) {
            line.add(data);
        }
        return line;
    }
    /*
    public void print(){
        SQLiteDatabase database = sqLiteHelper.getDatabase();
        String SQL = "SELECT * FROM "+sqLiteHelper.TableName;
        List<String> structureList = structure.getStructure();
        Cursor c = database.rawQuery(SQL,null);
        if(c.moveToFirst()){
            do{
                StringBuilder line = new StringBuilder();
                line.append(c.getString(0));
                line.append(" | "+c.getString(1));
                int i = 2;
                for(String s : structureList){
                    line.append(" | "+c.getString(i));
                    i++;
                }
                System.out.println(line.toString());
            }while(c.moveToNext());
        }
        c.close();
    }
    */

}