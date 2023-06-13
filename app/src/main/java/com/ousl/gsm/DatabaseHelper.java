package com.ousl.gsm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Declare Objects
    public static final String DATABASE_NAME = "gsm.db";

    // user table
    public static final String USER_TABLE_NAME = "USERS";
    public static final String USER_TABLE_NAME_COL_1 = "ID";
    public static final String USER_TABLE_NAME_COL_2 = "USERNAME";
    public static final String USER_TABLE_NAME_COL_3 = "PASSWORD";

    // Sligting table
    public static final String SLIGTING_TABLE_NAME = "SLIGTING";
    public static final String SLIGTING_TABLE_NAME_COL_1 = "ID";
    public static final String SLIGTING_TABLE_NAME_COL_2 = "LOCATION";
    public static final String SLIGTING_TABLE_NAME_COL_3 = "DATE";
    public static final String SLIGTING_TABLE_NAME_COL_4 = "TIME";
    public static final String SLIGTING_TABLE_NAME_COL_5 = "STRENGTH";
    public static final String SLIGTING_TABLE_NAME_COL_6 = "USER_ID";

    // ImmenseRecordsMain table
    public static final String IMMENSE_MAIN_TABLE_NAME = "IMMENSE_MAIN";
    public static final String IMMENSE_MAIN_TABLE_NAME_COL_1 = "ID";
    public static final String IMMENSE_MAIN_TABLE_NAME_COL_2 = "USER_ID";

    public static final String IMMENSE_MAIN_TABLE_NAME_COL_3 = "DATE";
    public static final String IMMENSE_MAIN_TABLE_NAME_COL_4 = "TIME";

    public static final String IMMENSE_MAIN_TABLE_NAME_COL_5 = "TITLE";


    // ImmenseRecordDescription table
    public static final String IMMENSE_DESCRIPTION_TABLE_NAME = "IMMENSE_DESCRIPTION";
    public static final String IMMENSE_DESCRIPTION_TABLE_NAME_COL_1 = "ID";
    public static final String IMMENSE_DESCRIPTION_TABLE_NAME_COL_2 = "IMMENSE_MAIN_ID";
    public static final String IMMENSE_DESCRIPTION_TABLE_NAME_COL_3 = "LATITUDE";
    public static final String IMMENSE_DESCRIPTION_TABLE_NAME_COL_4 = "LONGITUDE";
    public static final String IMMENSE_DESCRIPTION_TABLE_NAME_COL_5 = "SIGNAL_STRENGTH";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create user table
        db.execSQL("CREATE TABLE " + USER_TABLE_NAME +" (ID INTEGER PRIMARY KEY AUTOINCREMENT, USERNAME TEXT, PASSWORD TEXT)" );

        // create sligting table
        db.execSQL("CREATE TABLE " + SLIGTING_TABLE_NAME +" (ID INTEGER PRIMARY KEY AUTOINCREMENT, LOCATION TEXT, DATE TEXT, TIME TEXT, STRENGTH TEXT, USER_ID TEXT)" );

        // create immense main table
        db.execSQL("CREATE TABLE " + IMMENSE_MAIN_TABLE_NAME +" (ID INTEGER PRIMARY KEY AUTOINCREMENT, USER_ID TEXT,TITLE TXT ,DATE TEXT, TIME TEXT)" );

        // create immense description table
        db.execSQL("CREATE TABLE " + IMMENSE_DESCRIPTION_TABLE_NAME +" (ID INTEGER PRIMARY KEY AUTOINCREMENT, IMMENSE_MAIN_ID TEXT, LATITUDE TEXT, LONGITUDE TEXT, SIGNAL_STRENGTH TEXT)" );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + SLIGTING_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + IMMENSE_MAIN_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + IMMENSE_DESCRIPTION_TABLE_NAME);
        onCreate(db);
    }

    // USER TABLE

    public boolean insertDataToUser(String username, String passwordOne){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_TABLE_NAME_COL_2, username);
        contentValues.put(USER_TABLE_NAME_COL_3, passwordOne);

        long results = db.insert(USER_TABLE_NAME, null, contentValues);
        if(results == -1){
            return false;
        }else {
            return true;
        }
    }

    public Cursor getAllDataFromUser(String username){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor results =  db.rawQuery("select * from " + USER_TABLE_NAME + " WHERE " + USER_TABLE_NAME_COL_2 + " = " + "\""+username+"\"" , null);
        return results;
    }

    public Cursor getAllRecordForUser(String id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor results =  db.rawQuery("select * from " + SLIGTING_TABLE_NAME + " WHERE " + SLIGTING_TABLE_NAME_COL_6 + " = " + "\""+id+"\"" , null);
        return results;
    }


    // SLIGTING TABLE

    // insert data to sligting table
    public boolean insertDataToSligting(String location, String date, String time, String strength, String user_id){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SLIGTING_TABLE_NAME_COL_2, location);
        contentValues.put(SLIGTING_TABLE_NAME_COL_3, date);
        contentValues.put(SLIGTING_TABLE_NAME_COL_4, time);
        contentValues.put(SLIGTING_TABLE_NAME_COL_5, strength);
        contentValues.put(SLIGTING_TABLE_NAME_COL_6, user_id);

        long results = db.insert(SLIGTING_TABLE_NAME, null, contentValues);
        if(results == -1){
            return false;
        }else {
            return true;
        }
    }

    // Retrieve data from sligting table
    public String[][] retrieveDataFromSligtingTable(int user_id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor results =  db.rawQuery("select * from " + SLIGTING_TABLE_NAME + " WHERE " + SLIGTING_TABLE_NAME_COL_6 + " = " + "\""+user_id+"\"" , null);

        String[][] data = new String[results.getCount()][6];
        int i = 0;
        while(results.moveToNext()){
            data[i][0] = results.getString(0);
            data[i][1] = results.getString(1);
            data[i][2] = results.getString(2);
            data[i][3] = results.getString(3);
            data[i][4] = results.getString(4);
            data[i][5] = results.getString(5);
            i++;
        }
        return data;
    }

    // delete record from sligting table using unique table ID
    public boolean deleteDataFromSligtingTable(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(SLIGTING_TABLE_NAME, SLIGTING_TABLE_NAME_COL_1 + "=" + id, null) > 0;
    }


    // IMMENSE MODE
    // insert data to immense main table
    public int insertDataToImmenseMain(String user_id, String title, String date, String time){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(IMMENSE_MAIN_TABLE_NAME_COL_2, user_id);
        contentValues.put(IMMENSE_MAIN_TABLE_NAME_COL_5, title);
        contentValues.put(IMMENSE_MAIN_TABLE_NAME_COL_3, date);
        contentValues.put(IMMENSE_MAIN_TABLE_NAME_COL_4, time);

        long results = db.insert(IMMENSE_MAIN_TABLE_NAME, null, contentValues);
        if(results == -1){
            return -1;
        }else {
            return (int) results;
        }
    }

    // Retrieve data from immense main table
    public String[][] retrieveDataFromImmenseMain(int user_id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor results =  db.rawQuery("select * from " + IMMENSE_MAIN_TABLE_NAME + " WHERE " + IMMENSE_MAIN_TABLE_NAME_COL_2 + " = " + "\""+user_id+"\"" , null);

        String[][] data = new String[results.getCount()][5];
        int i = 0;
        while(results.moveToNext()){
            data[i][0] = results.getString(0);
            data[i][1] = results.getString(1);
            data[i][2] = results.getString(2);
            data[i][3] = results.getString(3);
            data[i][4] = results.getString(4);
            i++;
        }
        return data;
    }


    // insert data to immense description table
    public boolean insertDataToImmenseDescription(String immense_main_id, String latitude, String longitude, String signal_strength){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(IMMENSE_DESCRIPTION_TABLE_NAME_COL_2, immense_main_id);
        contentValues.put(IMMENSE_DESCRIPTION_TABLE_NAME_COL_3, latitude);
        contentValues.put(IMMENSE_DESCRIPTION_TABLE_NAME_COL_4, longitude);
        contentValues.put(IMMENSE_DESCRIPTION_TABLE_NAME_COL_5, signal_strength);

        long results = db.insert(IMMENSE_DESCRIPTION_TABLE_NAME, null, contentValues);
        Log.i("resultsB", String.valueOf(results));
        if(results == -1){
            return false;
        }else {
            return true;
        }
    }

    // Retrieve data from immense description table using id
    public String[][] retrieveDataFromImmenseDescription(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor results =  db.rawQuery("select * from " + IMMENSE_DESCRIPTION_TABLE_NAME + " WHERE " + IMMENSE_DESCRIPTION_TABLE_NAME_COL_2 + " = " + "\""+id+"\"" , null);

        String[][] data = new String[results.getCount()][5];
        int i = 0;
        while(results.moveToNext()){
            data[i][0] = results.getString(0);
            data[i][1] = results.getString(1);
            data[i][2] = results.getString(2);
            data[i][3] = results.getString(3);
            data[i][4] = results.getString(4);
            i++;
        }
        return data;
    }
}



