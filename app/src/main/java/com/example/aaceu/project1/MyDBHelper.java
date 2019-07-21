package com.example.aaceu.project1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBHelper extends SQLiteOpenHelper {
    // variables for table name, id column, name column, phoneNum column, email, company and image columns
    public static final String TABLE_NAME = "contacts";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PHONE_NUM = "phoneNum";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_COMPANY = "company";
    public static final String COLUMN_IMAGE = "image";

    private static final String DATABASE_NAME = "myContact.db";
    private static final int DATABASE_VERSION = 1;
    Context context;

    // create variable for creating table in SQL commands
    private static final String CREATE_DATABASE = "create table " + TABLE_NAME
            + "(" + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_NAME + " text not null, "
            + COLUMN_PHONE_NUM + " text not null, "
            + COLUMN_EMAIL + " text not null, "
            + COLUMN_COMPANY + " text not null, "
            + COLUMN_IMAGE + " text not null);";


    // constructor for this class
    public MyDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DATABASE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // delete or remove the table if the table name is already exist
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

}
