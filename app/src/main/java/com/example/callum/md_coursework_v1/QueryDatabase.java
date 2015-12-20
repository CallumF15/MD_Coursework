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

    public final Context appContext;

    //Getters & Setters

    //Constructor
    public QueryDatabase(Context context, String name, SQLiteDatabase.CursorFactory
            factory, int version){
        super(context, name, factory, version);

        //assign context
        this.appContext = context;
    }


    //Methods
    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create subject table
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

    //checks whether database exists and creates one if it doesn't
    public void createDatabase() throws  IOException{

        //check if the database exists
        boolean databaseExist = checkDatabaseExists();

        if(!databaseExist){ //if it doesn't exist
            //Create and/or open a database.
            this.getReadableDatabase();

            try{
                copyDatabaseFromAssets();
            }catch(IOException e){
                throw new Error("Error copying database");
            }
        }

    }

    //checks if the database exists
    public boolean checkDatabaseExists(){

        SQLiteDatabase db = null;

        try{
            //get the path of the database
            path = appContext.getDatabasePath(DB_NAME).toString();
            //open the database and assign to variable
            db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
            //sets the locale of this  database
            db.setLocale(Locale.getDefault());
            //sets the database version
            db.setVersion(1);

        }catch(SQLiteException e){
            Log.e("SQLHelper", "Database not found");
        }

        if(db != null){
            //closes the database
            db.close();
        }

        return db != null ? true:false;
    }

    //Copies the data from the database
    public void copyDatabaseFromAssets() throws  IOException{

        InputStream databaseInput = null;
        OutputStream databaseOutput = null;

        try{
            //get the assets from the database
            databaseInput = appContext.getAssets().open(DB_NAME);
            //create new file output stream by specified path
            databaseOutput = new FileOutputStream(path);

            //transfer bytes from databaseInput to databaseOutput
            byte[] buffer = new byte[1024];
            int length;

            while((length = databaseInput.read(buffer)) > 0){
                //writes the specified byte to this file output stream.
                databaseOutput.write(buffer, 0, length);
            }
            //flush the streams
            databaseOutput.flush();
            //close database output
            databaseOutput.close();
            //close database input
            databaseInput.close();

        }catch (IOException e){
            throw new Error("Problems Copying DB!");
        }
    }

    public List<NewSubject> getAllNewSubjects() {
        List<NewSubject> subjectList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + COL_TABLE_NAME;

        try {
            //create the database
            createDatabase();
            //Allow Create and/or open a database.
            SQLiteDatabase db = this.getReadableDatabase();
            //Runs the provided SQL and returns a Cursor over the result set.
            Cursor cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    NewSubject newSubject = new NewSubject();
                    //set subject id
                    newSubject.setSubject_id(Integer.parseInt(cursor.getString(0)));
                    //set subject name
                    newSubject.setSubject_name(cursor.getString(1));
                    // Adding contact to list
                    subjectList.add(newSubject);
                } while (cursor.moveToNext());
            }
        } catch (IOException e) { //move to next item
            Log.e("CursorException", "Error getting data");
        }

        // return subject list
        return subjectList;
    }
}
