package com.lirugo.calculate;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "dextcalc.db";
    private static final int SCHEMA = 2;
    static final String TABLE = "calculations";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_INPUT = "input";
    public static final String COLUMN_OUTPUT = "output";

    public DatabaseHelper(Context contex){
        super(contex, DATABASE_NAME, null, SCHEMA);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_INPUT + " TEXT, " +
                COLUMN_OUTPUT + " TEXT);");

        db.execSQL("INSERT INTO "+ TABLE +" (" + COLUMN_INPUT
                + ", " + COLUMN_OUTPUT  + ") VALUES ('2+2*2', '6');");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }
}
