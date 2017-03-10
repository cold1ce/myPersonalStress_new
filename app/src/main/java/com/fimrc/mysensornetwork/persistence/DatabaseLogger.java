package com.fimrc.mysensornetwork.persistence;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Pair;

import com.fimrc.sensorfusionframework.persistence.PersistenceLogger;
import com.fimrc.sensorfusionframework.persistence.container.SensorRecord;
import com.fimrc.sensorfusionframework.persistence.structure.Datatypes;
import com.fimrc.sensorfusionframework.persistence.structure.SensorRecordStructure;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Sven on 17.02.2017.
 */

public class DatabaseLogger extends PersistenceLogger {

    private mySQLiteHelper sqLiteHelper;
    private SensorRecordStructure structure;
    private Context context;

    @Override
    public void initialize(Object[] array){
        try{
            String sensorName = array[0].toString();
            this.structure = (SensorRecordStructure)array[1];
            this.context = (Context)array[2];
            sqLiteHelper = new mySQLiteHelper(sensorName, structure, context);
            sqLiteHelper.cleanDatabase();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public Iterator<SensorRecord> readAllRecords(SensorRecordStructure structure) {
        return null;
    }

    @Override
    public void modifyAllRecords(Iterator<SensorRecord> iterator) {

    }

    @Override
    protected void log(SensorRecord record) {
        SQLiteDatabase database = sqLiteHelper.getDatabase();
        ArrayList<Object> sensorRecordLine = convertSensorRecord(record);
        HashMap<Integer, Pair<String,Datatypes>> structureList = structure.getStructure();
        StringBuilder sb = new StringBuilder();
        StringBuilder values = new StringBuilder();
        Date timestamp = record.getTimestamp();
        Format format = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
        sb.append("INSERT INTO "+ sqLiteHelper.TableName+" ("+sqLiteHelper.COLUMN_TIMESTAMP);
        for(int i=0; i<structureList.size();i++){
            if(sensorRecordLine.get(i) != null){
                sb.append(",'"+ structure.getNameAtIndex(i)+"'");
                values.append(",'"+sensorRecordLine.get(i)+"'");
            }
        }
        sb.append(") VALUES ('" + format.format(timestamp)+"'"+values.toString()+")");
        System.out.println(sb.toString());
        database.execSQL(sb.toString());
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