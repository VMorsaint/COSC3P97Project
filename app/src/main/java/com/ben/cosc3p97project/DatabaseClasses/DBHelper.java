/**
 * Created by VMorsaint - 4864450 on 11/7/2015.
 * Cosc 3p97 Assignment 2
 */
package com.ben.cosc3p97project.DatabaseClasses;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;


public class DBHelper extends SQLiteOpenHelper
{
    public static final String DATABASE_NAME = "PatientFiles.db";
    public static final int DATABASE_VERSION = 10;

    public DBHelper(Context context)
    {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db)
    {
        try
        {
            String CREATE_TABLE = "CREATE TABLE " +
                    Patient.TABLE_NAME + " (" +
                    Patient.COL_PATIENT_ID + " INTEGER PRIMARY KEY, " +
                    Patient.COL_ANDROID_ID + " INTEGER, " +
                    Patient.COL_FIRST_NAME + " TEXT, " +
                    Patient.COL_LAST_NAME + " TEXT)";
            db.execSQL(CREATE_TABLE);

            CREATE_TABLE = "CREATE TABLE " +
                    PatientFile.TABLE_NAME + " (" +
                    PatientFile.COL_PATIENT_FILE_ID + " INTEGER PRIMARY KEY, " +
                    PatientFile.COL_PATIENT_ID + " INTEGER, " +
                    PatientFile.COL_NAME + " TEXT, " +
                    PatientFile.COL_DATETIME_START + " TEXT, " +
                    PatientFile.COL_DATETIME_END + " TEXT)";
            db.execSQL(CREATE_TABLE);



            CREATE_TABLE = "CREATE TABLE " +
                    BodyLocation.TABLE_NAME + "(" +
                    BodyLocation.COL_LOCATION_ID + " INTEGER PRIMARY KEY, " +
                    BodyLocation.COL_NAME + " TEXT)";
            db.execSQL(CREATE_TABLE);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public void onUpgrade(SQLiteDatabase dbSQL, int iOldVersion, int iNewVersion)
    {
        dbSQL.execSQL("DROP TABLE IF EXISTS " + PatientFile.TABLE_NAME);
        dbSQL.execSQL("DROP TABLE IF EXISTS " + BodyLocation.TABLE_NAME);
        dbSQL.execSQL("DROP TABLE IF EXISTS " + PatientNote.TABLE_NAME);
        dbSQL.execSQL("DROP TABLE IF EXISTS " + Patient.TABLE_NAME);
        onCreate(dbSQL);
    }
    public void onDowngrade(SQLiteDatabase dbSQL, int iOldVersion, int iNewVersion)
    {
        onUpgrade(dbSQL, iOldVersion, iNewVersion);
    }

    public boolean addPatientFile(PatientFile newPatientFile)
    {
        boolean bFlagOk = true;
        ContentValues values = new ContentValues();
        values.put(PatientFile.COL_PATIENT_ID, newPatientFile.getPatientID());
        values.put(PatientFile.COL_NAME, newPatientFile.getName());
        values.put(PatientFile.COL_DATETIME_START, newPatientFile.getStart());
        values.put(PatientFile.COL_DATETIME_END, newPatientFile.getEnd());

        SQLiteDatabase db = this.getWritableDatabase();
        try
        {
            db.insertOrThrow(PatientFile.TABLE_NAME, null, values);
        }
        catch (Exception e)
        {
            bFlagOk = false;
        }
        db.close();
        return bFlagOk;
    }

    public boolean addPatient(Patient newPatient)
    {
        boolean bFlagOk = true;
        ContentValues values = new ContentValues();
        values.put(Patient.COL_FIRST_NAME, newPatient.getFirstName());
        values.put(Patient.COL_LAST_NAME, newPatient.getLastName());
        values.put(Patient.COL_ANDROID_ID, newPatient.getAndroidId());

        SQLiteDatabase db = this.getWritableDatabase();
        try
        {
            db.insertOrThrow(Patient.TABLE_NAME, null, values);
        }
        catch (Exception e)
        {
            bFlagOk = false;
        }
        db.close();
        return bFlagOk;
    }

    public boolean addPatientNote(PatientNote newPatientNote)
    {
        boolean bFlagOk = true;
        ContentValues values = new ContentValues();
        values.put(PatientNote.COL_PATIENT_FILE_ID, newPatientNote.getPatientFileID());
        values.put(PatientNote.COL_NOTE, newPatientNote.getNote());


        SQLiteDatabase db = this.getWritableDatabase();
        try
        {
            db.insertOrThrow(Patient.TABLE_NAME, null, values);
        }
        catch (Exception e)
        {
            bFlagOk = false;
        }
        db.close();
        return bFlagOk;
    }


    public boolean addLocation(BodyLocation newLocation)
    {
        boolean bFlagOk = true;
        ContentValues values = new ContentValues();
        values.put(BodyLocation.COL_NAME, newLocation.getName());

        SQLiteDatabase db = this.getWritableDatabase();
        try
        {
            db.insertOrThrow(BodyLocation.TABLE_NAME, null, values);
        }
        catch (Exception e)
        {
            bFlagOk = false;
        }
        db.close();
        return bFlagOk;
    }


    public ArrayList<String> getPatientFileList()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                PatientFile.COL_PATIENT_FILE_ID + "," +
                PatientFile.COL_NAME + "," +
                PatientFile.COL_DATETIME_START + "," +
                PatientFile.COL_DATETIME_END +
                " FROM " + PatientFile.TABLE_NAME;

        ArrayList<String> PatientFileList = new ArrayList<>();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst())
        {
            do
            {
                PatientFileList.add(cursor.getString(cursor.getColumnIndex(PatientFile.COL_NAME)) + " (" +
                        cursor.getString(cursor.getColumnIndex(PatientFile.COL_DATETIME_START)) + " - " +
                        cursor.getString(cursor.getColumnIndex(PatientFile.COL_DATETIME_END)) + ")");
            }
            while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed())
        {
            cursor.close();
        }

        db.close();
        return PatientFileList;
    }

    public ArrayList<Patient> getPatientList()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                Patient.COL_PATIENT_ID + "," +
                Patient.COL_ANDROID_ID + "," +
                Patient.COL_FIRST_NAME + "," +
                Patient.COL_LAST_NAME +
                " FROM " + Patient.TABLE_NAME;

        ArrayList<Patient> PatientList = new ArrayList<>();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst())
        {
            do
            {
                PatientList.add(new Patient(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Patient.COL_PATIENT_ID))),
                        cursor.getString(cursor.getColumnIndex(Patient.COL_ANDROID_ID)),
                        cursor.getString(cursor.getColumnIndex(Patient.COL_FIRST_NAME)),
                        cursor.getString(cursor.getColumnIndex(Patient.COL_LAST_NAME))));
            }
            while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed())
        {
            cursor.close();
        }

        db.close();
        return PatientList;
    }

    public Patient getPatient(String iPatientID)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                Patient.COL_PATIENT_ID + "," +
                Patient.COL_ANDROID_ID + "," +
                Patient.COL_FIRST_NAME + "," +
                Patient.COL_LAST_NAME +
                " FROM " + Patient.TABLE_NAME +
                " WHERE " + Patient.COL_PATIENT_ID + " = " + iPatientID;

        Patient patientReturned;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst())
        {
            patientReturned = new Patient(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Patient.COL_PATIENT_ID))),
                        cursor.getString(cursor.getColumnIndex(Patient.COL_ANDROID_ID)),
                        cursor.getString(cursor.getColumnIndex(Patient.COL_FIRST_NAME)),
                        cursor.getString(cursor.getColumnIndex(Patient.COL_LAST_NAME)));
        }
        else
        {
            patientReturned = null;
        }
        if (cursor != null && !cursor.isClosed())
        {
            cursor.close();
        }

        db.close();
        return patientReturned;
    }

    public ArrayList<PatientFile> getPatientFileListByPatientId(String sPatientId)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                PatientFile.COL_PATIENT_FILE_ID + "," +
                PatientFile.COL_PATIENT_ID + "," +
                PatientFile.COL_NAME + "," +
                PatientFile.COL_DATETIME_START + "," +
                PatientFile.COL_DATETIME_END +
                " FROM " + PatientFile.TABLE_NAME +
                " WHERE " + PatientFile.COL_PATIENT_ID + " = " + sPatientId;

        ArrayList<PatientFile> PatientFileList = new ArrayList<>();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst())
        {
            do
            {
                PatientFileList.add(new PatientFile(Integer.parseInt(cursor.getString(cursor.getColumnIndex(PatientFile.COL_PATIENT_FILE_ID))),
                        Integer.parseInt(cursor.getString(cursor.getColumnIndex(PatientFile.COL_PATIENT_ID))),
                        cursor.getString(cursor.getColumnIndex(PatientFile.COL_NAME)),
                        cursor.getString(cursor.getColumnIndex(PatientFile.COL_DATETIME_START)),
                        cursor.getString(cursor.getColumnIndex(PatientFile.COL_DATETIME_END))));
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed())
        {
            cursor.close();
        }

        db.close();
        return PatientFileList;
    }



    public ArrayList<String> getLocationList()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                BodyLocation.COL_LOCATION_ID + ", " +
                BodyLocation.COL_NAME +
                " FROM " + BodyLocation.TABLE_NAME +
                " ORDER BY " + BodyLocation.COL_NAME;

        ArrayList<String> LocationList = new ArrayList<>();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst())
        {
            do
            {
                LocationList.add(cursor.getString(cursor.getColumnIndex(BodyLocation.COL_NAME)));
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed())
        {
            cursor.close();
        }

        db.close();
        return LocationList;
    }

    public PatientFile getPatientFile(String iID)
    {
        String sQuery = "Select * FROM " + PatientFile.TABLE_NAME + " WHERE " + PatientFile.COL_PATIENT_FILE_ID + " = " + iID ;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursorResult = db.rawQuery(sQuery, null);
        PatientFile objSelectedPatientFile;
        if (cursorResult.moveToFirst())
        {
            objSelectedPatientFile = new PatientFile(Integer.parseInt(cursorResult.getString(0)), Integer.parseInt(cursorResult.getString(1)),
                    cursorResult.getString(2), cursorResult.getString(3), cursorResult.getString(4));
            cursorResult.close();
        }
        else
        {
            objSelectedPatientFile = null;
        }
        db.close();
        return objSelectedPatientFile;
    }


    public PatientNote getPatientNote(String iID)

    {
        String sQuery = "Select * FROM " + PatientNote.TABLE_NAME + " WHERE " + PatientNote.COL_PATIENT_NOTE_ID + " = " + iID ;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursorResult = db.rawQuery(sQuery, null);
        PatientNote objSelectedPatientNote;
        if (cursorResult.moveToFirst())
        {
            objSelectedPatientNote = new PatientNote(Integer.parseInt(cursorResult.getString(0)), Integer.parseInt(cursorResult.getString(1)),
                    cursorResult.getString(2));
            cursorResult.close();
        }
        else
        {
            objSelectedPatientNote = null;
        }
        db.close();
        return objSelectedPatientNote;
    }

    public boolean deletePatientFile(int iID)
    {

        int iReturn;
        SQLiteDatabase db = this.getWritableDatabase();
        iReturn = db.delete(PatientFile.TABLE_NAME, PatientFile.COL_PATIENT_FILE_ID + " = ?", new String[]{Integer.toString(iID)});
        db.close();
        return iReturn > 0;
    }

}
