package com.example.callum.md_coursework_v1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Locale;

/**
 * Created by Callum on 01/12/2015.
 */
public class QueryDatabase extends SQLiteOpenHelper {

    //Variables
    private static final String NEWS_TABLE = "products";
    private static final String DB_NAME = "NewsSubjects.s3db";
    private static final int DB_VERSION = 1;

    public static final String COL_TABLE_NAME = "subjects";
    public static final String COL_SUBJECT_ID = "subject_id";
    public static final String COL_SUBJECT_NAME = "subject_name";


    private SQLiteDatabase db;
    public final Context appContext;

    //Getters & Setters

    //Constructor
    public QueryDatabase(Context context, String name, SQLiteDatabase.CursorFactory
            factory, int version){
        super(context, name, factory, version);

        this.appContext = context;
    }

    //Methods
    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create book table
        String CREATE_NEWS_SUBJECT_TABLE = "CREATE TABLE IF NOT EXISTS "
                + COL_TABLE_NAME + " ("
                + COL_SUBJECT_ID + " INTEGER PRIMARY KEY,"
                + COL_SUBJECT_NAME + " TEXT, " + ") ";

        //Create Table
        db.execSQL(CREATE_NEWS_SUBJECT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion > oldVersion) {
        }
        db.execSQL("DROP TABLE IF EXISTS" + COL_TABLE_NAME);
        onCreate(db);

        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS books");
        // create fresh  table
        this.onCreate(db);
    }

    public void createDatabase(){


    }

    public boolean checkDatabaseExists(){

        SQLiteDatabase db = null;
        String path = "";

        try{
            path = appContext.getDatabasePath(DB_NAME).toString();
            db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
            db.setLocale(Locale.getDefault());
            db.setVersion(1);

        }catch(SQLiteException e){
            Log.e("SQLHelper", "Database not found");
        }

        if(db != null){
            db.close();
        }

        return db != null ? true:false;
    }




}
