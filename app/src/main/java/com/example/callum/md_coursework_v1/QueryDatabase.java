package com.example.callum.md_coursework_v1;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Callum on 01/12/2015.
 */
public class QueryDatabase extends SQLiteOpenHelper {

    //Variables
    private static final String DB_NAME = "NewsSubjects.s3db";
    private static final int DB_VERSION = 1;
    private String path;

    public static final String COL_TABLE_NAME = "subjects";
    public static final String COL_SUBJECT_ID = "subject_id";
    public static final String COL_SUBJECT_NAME = "subject_name";


    //private SQLiteDatabase db;
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
                + COL_SUBJECT_ID + " INTEGER PRIMARY KEY, "
                + COL_SUBJECT_NAME + " TEXT)";

        //Create Table
        db.execSQL(CREATE_NEWS_SUBJECT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion > oldVersion) {
            // Drop older table if existed
            db.execSQL("DROP TABLE IF EXISTS" + COL_TABLE_NAME);
            // create fresh  table
            onCreate(db);
        }
    }

    public void createDatabase() throws  IOException{

        boolean databaseExist = checkDatabaseExists();

        if(!databaseExist){
            this.getReadableDatabase();

            try{
                copyDatabaseFromAssets();
            }catch(IOException e){
                throw new Error("Error copying database");
            }
        }

    }

    public boolean checkDatabaseExists(){

        SQLiteDatabase db = null;

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

    public void copyDatabaseFromAssets() throws  IOException{

        InputStream databaseInput = null;
        OutputStream databaseOutput = null;

        try{
            databaseInput = appContext.getAssets().open(DB_NAME);
            databaseOutput = new FileOutputStream(path);

            //transfer bytes from databaseInput to databaseOutput
            byte[] buffer = new byte[1024];
            int length;

            while((length = databaseInput.read(buffer)) > 0){
                databaseOutput.write(buffer, 0, length);
            }
            //close the streams
            databaseOutput.flush();
            databaseOutput.close();
            databaseInput.close();

        }catch (IOException e){
            throw new Error("Problems Copying DB!");
        }
    }

    public List<String> getAllSubjects() {
        List<String> subjectList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + COL_TABLE_NAME;

        try {
            createDatabase();

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                NewSubject newSubject = new NewSubject();
                //newSubject.setSubject_id(Integer.parseInt(cursor.getString(0)));
                newSubject.setSubject_name(cursor.getString(1));
                // Adding contact to list
                subjectList.add(newSubject.getSubject_name());
                } while (cursor.moveToNext());
            }
        } catch (IOException e) {
            Log.e("CursorException", "Error getting data");
        }

        // return subject list
        return subjectList;
    }

    public List<NewSubject> getAllNewSubjects() {
        List<NewSubject> subjectList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + COL_TABLE_NAME;

        try {
            createDatabase();

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    NewSubject newSubject = new NewSubject();
                    newSubject.setSubject_id(Integer.parseInt(cursor.getString(0)));
                    newSubject.setSubject_name(cursor.getString(1));
                    // Adding contact to list
                    subjectList.add(newSubject);
                } while (cursor.moveToNext());
            }
        } catch (IOException e) {
            Log.e("CursorException", "Error getting data");
        }

        // return subject list
        return subjectList;
    }
}
