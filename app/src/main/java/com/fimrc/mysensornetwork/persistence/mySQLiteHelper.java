package com.fimrc.mysensornetwork.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.fimrc.sensorfusionframework.persistence.structure.SensorRecordStructure;

import java.util.List;

/**
 * Created by Sven on 17.02.2017.
 */

public final class mySQLiteHelper extends SQLiteOpenHelper {

    private SQLiteDatabase database = null;

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TIMESTAMP = "Timestamp";
    public static final String DATABASE_NAME = "mySensorNetwork";
    public final String TABLE_NAME;
    private static final int DATABASE_VERSION = 1;
    private final String TABLE_CREATE;

    public mySQLiteHelper(String TABLE_NAME, SensorRecordStructure structure, Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.TABLE_NAME = TABLE_NAME;
        List<String> structureList = structure.getStructure();
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" ("
                +COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_TIMESTAMP + " DATE NOT NULL");
        for(String s : structureList)
            sb.append(", "+s+ " TEXT");

        sb.append(")");
        TABLE_CREATE = sb.toString();
        System.out.println(TABLE_CREATE);
        database = getWritableDatabase();
        createTable(database);
    }

    public SQLiteDatabase getDatabase(){
        return database;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void createTable(SQLiteDatabase db){ db.execSQL(TABLE_CREATE); }

    public void cleanDatabase(){
        database.delete(TABLE_NAME, null, null);
    }

}
