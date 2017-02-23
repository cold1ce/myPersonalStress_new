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
    private static final int DATABASE_VERSION = 1;
    public final String TableName;
    private final String TableCreate;

    public mySQLiteHelper(String TableName, SensorRecordStructure structure, Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.TableName = TableName;
        List<String> structureList = structure.getStructure();
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS "+TableName+" ("
                +COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_TIMESTAMP + " DATE NOT NULL");
        for(String s : structureList)
            sb.append(", "+s+ " TEXT");

        sb.append(")");
        TableCreate = sb.toString();
        System.out.println(TableCreate);
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

    public void createTable(SQLiteDatabase db){ db.execSQL(TableCreate); }

    public void cleanDatabase(){
        database.delete(TableName, null, null);
    }

}
