package com.fimrc.mysensornetwork.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.fimrc.jdcf.persistence.container.SensorDataField;
import com.fimrc.jdcf.persistence.structure.SensorDataType;
import com.fimrc.jdcf.persistence.structure.SensorRecordStructure;

import java.util.HashMap;

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
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS "+TableName+" ("
                +COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_TIMESTAMP + " DATE NOT NULL");
        for(int i=0; i<structure.size(); i++){
            sb.append(", "+structure.getNameAtIndex(i));
            switch(structure.getDataTypeAtIndex(i)) {
                case STRING:
                    sb.append(" VARCHAR(30)");
                    break;
                case DOUBLE:
                    sb.append(" DOUBLE");
                    break;
                case BOOLEAN:
                    sb.append(" BOOLEAN");
                    break;
                case CHAR:
                    sb.append(" VARCHAR(1)");
                    break;
                case BYTE:
                    sb.append(" VARCHAR(1)");
                    break;
                case SHORT:
                    sb.append(" SMALLINT");
                    break;
                case INTEGER:
                    sb.append(" INTEGER");
                    break;
                case LONG:
                    sb.append(" BIGINT");
                    break;
                case FLOAT:
                    sb.append(" FLOAT");
                    break;
                case BLOB:
                    sb.append(" BLOB");
                    break;
                default:
                    sb.append(" TEXT");
                    break;
            }
        }
        sb.append(")");
        TableCreate = sb.toString();
        Log.d("Database",TableCreate);
        Log.d("Database", String.valueOf(Thread.currentThread().getId()));
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
