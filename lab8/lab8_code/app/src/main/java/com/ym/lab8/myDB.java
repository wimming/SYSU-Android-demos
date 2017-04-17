package com.ym.lab8;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by ym on 16-11-22.
 */

public class myDB extends SQLiteOpenHelper {
    public static final String DB_NAME = "database1.db";
    public static final String TABLE_NAME = "tb_birthday";
    public static final int DB_VERSION = 1;

    public myDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public myDB(Context context) {
        this(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE = "CREATE TABLE if not exists " + TABLE_NAME
                + "(name TEXT PRIMARY KEY," + "birthday TEXT," + "gift TEXT)";
        try {
            sqLiteDatabase.execSQL(CREATE_TABLE);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
