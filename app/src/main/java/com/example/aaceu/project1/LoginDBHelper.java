package com.example.aaceu.project1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LoginDBHelper extends SQLiteOpenHelper {
    // variable for table name, columns = id, username, and password
    public static final String TABLE_NAME = "login";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";

    // database name is security.db
    private static final String DATABASE_NAME = "security.db";
    private static final int DATABASE_VERSION = 1;  // database version
    Context context;

    // create variable for creating table in SQL commands
    // id is set to increment automatically
    // username and password are set to be not null text
    private static final String CREATE_DATABASE = "create table " + TABLE_NAME
            + "(" + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_USERNAME + " text not null, "
            + COLUMN_PASSWORD + " text not null);";

    public LoginDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // when call onCreate method create database using CREATE_DATABASE variable
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DATABASE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

}
