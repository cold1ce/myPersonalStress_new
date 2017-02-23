package com.fimrc.mysensornetwork.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Sven on 17.02.2017.
 */

public final class mySQLiteHelper extends SQLiteOpenHelper {

    private static mySQLiteHelper instance = null;
    private SQLiteDatabase database = null;

    public static final String TABLE_SENSORDATA = "SensorData";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TIMESTAMP = "Timestamp";
    public static final String COLUMN_SENSOR = "Sensor";
    public static final String COLUMN_DATA1 = "Data1";
    public static final String COLUMN_DATA2 = "Data2";
    public static final String COLUMN_DATA3 = "Data3";
    public static final String COLUMN_DATA4 = "Data4";
    public static final String COLUMN_DATA5 = "Data5";
    public static final String COLUMN_DATA6 = "Data6";
    public static final String COLUMN_DATA7 = "Data7";
    public static final String COLUMN_DATA8 = "Data8";
    public static final String COLUMN_DATA9 = "Data9";
    public static final String DATABASE_NAME = "mySensorNetwork";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE = "CREATE TABLE IF NOT EXISTS " + TABLE_SENSORDATA + " ("
            + COLUMN_ID + " INTEGER NOT NULL,"
            + COLUMN_TIMESTAMP + " TEXT NOT NULL,"
            + COLUMN_SENSOR + " TEXT,"
            + COLUMN_DATA1 + " TEXT,"
            + COLUMN_DATA2 + " TEXT,"
            + COLUMN_DATA3 + " TEXT,"
            + COLUMN_DATA4 + " TEXT,"
            + COLUMN_DATA5 + " TEXT,"
            + COLUMN_DATA6 + " TEXT,"
            + COLUMN_DATA7 + " TEXT,"
            + COLUMN_DATA8 + " TEXT,"
            + COLUMN_DATA9 + " TEXT)";


    private mySQLiteHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        database = getWritableDatabase();
    }

    public static mySQLiteHelper getInstance(Context context){
        if(instance == null)
            instance = new mySQLiteHelper(context);
        return instance;
    }

    public SQLiteDatabase getDatabase(){
        return database;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void cleanDatabase(){
        database.delete(TABLE_SENSORDATA, null, null);
    }

}
