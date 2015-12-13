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


public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "PatientFiles.db";
    public static final int DATABASE_VERSION = 14;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        try {
            String CREATE_TABLE = "CREATE TABLE " +
                    Patient.TABLE_NAME + " (" +
                    Patient.COL_PATIENT_ID + " INTEGER PRIMARY KEY, " +
                    Patient.COL_ANDROID_ID + " INTEGER, " +
                    Patient.COL_FIRST_NAME + " TEXT, " +
                    Patient.COL_LAST_NAME + " TEXT, " +
                    Patient.COL_DATE_ADDED + " TEXT)";
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
                    PatientNote.TABLE_NAME + " (" +
                    PatientNote.COL_PATIENT_NOTE_ID + " INTEGER PRIMARY KEY, " +
                    PatientNote.COL_PATIENT_FILE_ID + " INTEGER, " +
                    PatientNote.COL_NOTE + " TEXT)";
            db.execSQL(CREATE_TABLE);

            CREATE_TABLE = "CREATE TABLE " +
                    PatientFileBodyLocation.TABLE_NAME + " (" +
                    PatientFileBodyLocation.COL_PATIENT_FILE_BODY_LOCATION_ID + " INTEGER PRIMARY KEY, " +
                    PatientFileBodyLocation.COL_PATIENT_FILE_ID + " INTEGER, " +
                    PatientFileBodyLocation.COL_BODY_LOCATION_ID + " INTEGER)";
            db.execSQL(CREATE_TABLE);

            CREATE_TABLE = "CREATE TABLE " +
                    BodyLocation.TABLE_NAME + "(" +
                    BodyLocation.COL_LOCATION_ID + " INTEGER PRIMARY KEY, " +
                    BodyLocation.COL_NAME + " TEXT)";
            db.execSQL(CREATE_TABLE);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void onUpgrade(SQLiteDatabase dbSQL, int iOldVersion, int iNewVersion)
    {
        dbSQL.execSQL("DROP TABLE IF EXISTS " + Patient.TABLE_NAME);
        dbSQL.execSQL("DROP TABLE IF EXISTS " + PatientFile.TABLE_NAME);
        dbSQL.execSQL("DROP TABLE IF EXISTS " + PatientNote.TABLE_NAME);
        dbSQL.execSQL("DROP TABLE IF EXISTS " + PatientFileBodyLocation.TABLE_NAME);
        dbSQL.execSQL("DROP TABLE IF EXISTS " + BodyLocation.TABLE_NAME);
        onCreate(dbSQL);
    }

    public void onDowngrade(SQLiteDatabase dbSQL, int iOldVersion, int iNewVersion) {
        onUpgrade(dbSQL, iOldVersion, iNewVersion);
    }

    public boolean addPatientFile(PatientFile newPatientFile) {
        boolean bFlagOk = true;
        ContentValues values = new ContentValues();
        values.put(PatientFile.COL_PATIENT_ID, newPatientFile.getPatientID());
        values.put(PatientFile.COL_NAME, newPatientFile.getName());
        values.put(PatientFile.COL_DATETIME_START, newPatientFile.getStart());
        values.put(PatientFile.COL_DATETIME_END, newPatientFile.getEnd());

        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.insertOrThrow(PatientFile.TABLE_NAME, null, values);
        } catch (Exception e) {
            bFlagOk = false;
        }
        db.close();
        return bFlagOk;
    }

    public long addPatient(Patient newPatient)
    {
        boolean bFlagOk = true;
        long iPatientID = 0;
        ContentValues values = new ContentValues();
        values.put(Patient.COL_FIRST_NAME, newPatient.getFirstName());
        values.put(Patient.COL_LAST_NAME, newPatient.getLastName());
        values.put(Patient.COL_ANDROID_ID, newPatient.getAndroidId());

        SQLiteDatabase db = this.getWritableDatabase();
        try
        {
            iPatientID = db.insertOrThrow(Patient.TABLE_NAME, null, values);
            newPatient.setPatientID(iPatientID);
        }
        catch (Exception e)
        {
            bFlagOk = false;
        }
        db.close();
        return iPatientID;
    }

    public boolean addPatientNote(PatientNote newPatientNote) {
        boolean bFlagOk = true;
        long iPatientNoteID = 0;
        ContentValues values = new ContentValues();
        values.put(PatientNote.COL_PATIENT_FILE_ID, newPatientNote.getPatientFileID());
        values.put(PatientNote.COL_NOTE, newPatientNote.getNote());


        SQLiteDatabase db = this.getWritableDatabase();
        try
        {
            iPatientNoteID = db.insertOrThrow(PatientNote.TABLE_NAME, null, values);
            newPatientNote.setPatientNoteID(iPatientNoteID);
        }
        catch (Exception e)
        {
            bFlagOk = false;
        }
        db.close();
        return bFlagOk;
    }


    public boolean addLocation(BodyLocation newLocation) {
        boolean bFlagOk = true;
        ContentValues values = new ContentValues();
        values.put(BodyLocation.COL_NAME, newLocation.getName());

        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.insertOrThrow(BodyLocation.TABLE_NAME, null, values);
        } catch (Exception e) {
            bFlagOk = false;
        }
        db.close();
        return bFlagOk;
    }


    public ArrayList<Patient> getPatientList(boolean bActiveOnly, boolean bSortByActivity)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                Patient.COL_PATIENT_ID + "," +
                Patient.COL_ANDROID_ID + "," +
                Patient.COL_FIRST_NAME + "," +
                Patient.COL_LAST_NAME + "," +
                Patient.COL_DATE_ADDED +
                " FROM " + Patient.TABLE_NAME;

        if (bSortByActivity)
        {
            selectQuery += " LEFT JOIN (SELECT " + PatientFile.COL_PATIENT_ID + ", MAX(" + PatientFile.COL_DATETIME_START + ") AS cMaxStart"
                    + " FROM " + PatientFile.TABLE_NAME
                    + " GROUP BY " + PatientFile.COL_PATIENT_ID + ") AS tFileActivity ON " + Patient.TABLE_NAME + "." + Patient.COL_PATIENT_ID
                    + " = tFileActivity." + PatientFile.COL_PATIENT_ID;
        }
        if(bActiveOnly)
        {
            selectQuery += " WHERE " + Patient.COL_PATIENT_ID + " IN (SELECT " + PatientFile.COL_PATIENT_ID
                    + " FROM " + PatientFile.TABLE_NAME
                    + " WHERE " + PatientFile.COL_DATETIME_END + " = '')";
        }


        ArrayList<Patient> PatientList = new ArrayList<>();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                PatientList.add(new Patient(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Patient.COL_PATIENT_ID))),
                        cursor.getString(cursor.getColumnIndex(Patient.COL_ANDROID_ID)),
                        cursor.getString(cursor.getColumnIndex(Patient.COL_FIRST_NAME)),
                        cursor.getString(cursor.getColumnIndex(Patient.COL_LAST_NAME)),
                        cursor.getString(cursor.getColumnIndex(Patient.COL_DATE_ADDED))));
            }
            while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        db.close();
        return PatientList;
    }

    public Patient getPatient(String iPatientID) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  " +
                Patient.COL_PATIENT_ID + "," +
                Patient.COL_ANDROID_ID + "," +
                Patient.COL_FIRST_NAME + "," +
                Patient.COL_LAST_NAME + "," +
                Patient.COL_DATE_ADDED +
                " FROM " + Patient.TABLE_NAME +
                " WHERE " + Patient.COL_PATIENT_ID + " = " + iPatientID;

        Patient patientReturned;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            patientReturned = new Patient(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Patient.COL_PATIENT_ID))),
                        cursor.getString(cursor.getColumnIndex(Patient.COL_ANDROID_ID)),
                        cursor.getString(cursor.getColumnIndex(Patient.COL_FIRST_NAME)),
                        cursor.getString(cursor.getColumnIndex(Patient.COL_LAST_NAME)),
                        cursor.getString(cursor.getColumnIndex(Patient.COL_DATE_ADDED)));
        }
        else
        {
            patientReturned = null;
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        db.close();
        return patientReturned;
    }

    public ArrayList<PatientFile> getPatientFileListByPatientId(String sPatientId,boolean bActiveOnly)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  " +
                PatientFile.COL_PATIENT_FILE_ID + "," +
                PatientFile.COL_PATIENT_ID + "," +
                PatientFile.COL_NAME + "," +
                PatientFile.COL_DATETIME_START + "," +
                PatientFile.COL_DATETIME_END +
                " FROM " + PatientFile.TABLE_NAME +
                " WHERE " + PatientFile.COL_PATIENT_ID + " = " + sPatientId;
        if (bActiveOnly)
        {
            selectQuery += " AND " + PatientFile.COL_DATETIME_END + " = ''";
        }

        ArrayList<PatientFile> PatientFileList = new ArrayList<>();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                PatientFileList.add(new PatientFile(Integer.parseInt(cursor.getString(cursor.getColumnIndex(PatientFile.COL_PATIENT_FILE_ID))),
                        Integer.parseInt(cursor.getString(cursor.getColumnIndex(PatientFile.COL_PATIENT_ID))),
                        cursor.getString(cursor.getColumnIndex(PatientFile.COL_NAME)),
                        cursor.getString(cursor.getColumnIndex(PatientFile.COL_DATETIME_START)),
                        cursor.getString(cursor.getColumnIndex(PatientFile.COL_DATETIME_END))));
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        db.close();
        return PatientFileList;
    }

    public ArrayList<PatientNote> getPatientNoteListByPatientFileId(String sPatientFileId)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery =  "SELECT " +
                PatientNote.COL_PATIENT_NOTE_ID + "," +
                PatientNote.COL_PATIENT_FILE_ID + "," +
                PatientNote.COL_NOTE +
                " FROM " + PatientNote.TABLE_NAME +
                " WHERE " + PatientNote.COL_PATIENT_FILE_ID + " = " + sPatientFileId;

        ArrayList<PatientNote> PatientNoteList = new ArrayList<>();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst())
        {
            do
            {
                PatientNoteList.add(new PatientNote(Integer.parseInt(cursor.getString(cursor.getColumnIndex(PatientNote.COL_PATIENT_NOTE_ID))),
                        Integer.parseInt(cursor.getString(cursor.getColumnIndex(PatientNote.COL_PATIENT_FILE_ID))),
                        cursor.getString(cursor.getColumnIndex(PatientNote.COL_NOTE))
                        ));
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed())
        {
            cursor.close();
        }

        db.close();
        return PatientNoteList;
    }

    public ArrayList<String> getLocationList() {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  " +
                BodyLocation.COL_LOCATION_ID + ", " +
                BodyLocation.COL_NAME +
                " FROM " + BodyLocation.TABLE_NAME +
                " ORDER BY " + BodyLocation.COL_NAME;

        ArrayList<String> LocationList = new ArrayList<>();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                LocationList.add(cursor.getString(cursor.getColumnIndex(BodyLocation.COL_NAME)));
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        db.close();
        return LocationList;
    }

    public PatientFile getPatientFile(String iID) {
        String sQuery = "Select * FROM " + PatientFile.TABLE_NAME + " WHERE " + PatientFile.COL_PATIENT_FILE_ID + " = " + iID;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursorResult = db.rawQuery(sQuery, null);
        PatientFile objSelectedPatientFile;
        if (cursorResult.moveToFirst()) {
            objSelectedPatientFile = new PatientFile(Integer.parseInt(cursorResult.getString(0)), Integer.parseInt(cursorResult.getString(1)),
                    cursorResult.getString(2), cursorResult.getString(3), cursorResult.getString(4));
            cursorResult.close();
        } else {
            objSelectedPatientFile = null;
        }
        db.close();
        return objSelectedPatientFile;
    }


    public PatientNote getPatientNote(String iID)

    {
        String sQuery = "Select * FROM " + PatientNote.TABLE_NAME + " WHERE " + PatientNote.COL_PATIENT_NOTE_ID + " = " + iID;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursorResult = db.rawQuery(sQuery, null);
        PatientNote objSelectedPatientNote;
        if (cursorResult.moveToFirst()) {
            objSelectedPatientNote = new PatientNote(Integer.parseInt(cursorResult.getString(0)), Integer.parseInt(cursorResult.getString(1)),
                    cursorResult.getString(2));
            cursorResult.close();
        } else {
            objSelectedPatientNote = null;
        }
        db.close();
        return objSelectedPatientNote;
    }

    public boolean deletePatientFile(int iID) {

        int iReturn;
        SQLiteDatabase db = this.getWritableDatabase();
        iReturn = db.delete(PatientFile.TABLE_NAME, PatientFile.COL_PATIENT_FILE_ID + " = ?", new String[]{Integer.toString(iID)});
        db.close();
        return iReturn > 0;
    }

    public ArrayList<PatientAppointment> getAppointmentList(String date, int patientId) {
        if (patientId == -1) {
            //get list of appointments on day
        } else {
            //get list of appointments for patient on day
        }
        return new ArrayList<>();
    }

    public Patient updatePatient(Patient patientOld, String sAndroidIDParam, String sPatientFirstNameParam, String sPatientLastNameParam,String sPatientDateAddedParam )
    {
        ContentValues values = new ContentValues();
        values.put(Patient.COL_ANDROID_ID, sAndroidIDParam);
        values.put(Patient.COL_FIRST_NAME, sPatientFirstNameParam);
        values.put(Patient.COL_LAST_NAME, sPatientLastNameParam);
        values.put(Patient.COL_DATE_ADDED, sPatientDateAddedParam);

        SQLiteDatabase db = this.getWritableDatabase();
        db.update(Patient.TABLE_NAME, values, Patient.COL_PATIENT_ID + " = ?", new String[]{String.valueOf(patientOld.getPatientID())});
        return new Patient(patientOld.getPatientID(),sAndroidIDParam,sPatientFirstNameParam,sPatientLastNameParam,sPatientDateAddedParam);
    }

    public PatientFile updatePatientFile(PatientFile patientfileOld, long iPatientIDParam, String sPatientFileNameParam, String sStartParam, String sEndParam)
    {
        ContentValues values = new ContentValues();
        values.put(PatientFile.COL_PATIENT_ID, iPatientIDParam);
        values.put(PatientFile.COL_NAME, sPatientFileNameParam);
        values.put(PatientFile.COL_DATETIME_START, sStartParam);
        values.put(PatientFile.COL_DATETIME_END, sEndParam);

        SQLiteDatabase db = this.getWritableDatabase();
        db.update(PatientFile.TABLE_NAME,values,PatientFile.COL_PATIENT_FILE_ID + " = ?", new String[]{String.valueOf(patientfileOld.getPatientFileID())});
        return new PatientFile(patientfileOld.getPatientFileID(),iPatientIDParam,sPatientFileNameParam,sStartParam,sEndParam);
    }

    public PatientNote updatePatientNote(PatientNote patientnoteOld, long iPatientFileIDParam, String sPatientNoteParam)
    {
        ContentValues values = new ContentValues();
        values.put(PatientNote.COL_PATIENT_FILE_ID, iPatientFileIDParam);
        values.put(PatientNote.COL_NOTE, sPatientNoteParam);

        SQLiteDatabase db = this.getWritableDatabase();
        db.update(PatientNote.TABLE_NAME,values,PatientNote.COL_PATIENT_NOTE_ID + " = ?", new String[]{String.valueOf(patientnoteOld.getPatientNoteID())});
        return new PatientNote(patientnoteOld.getPatientNoteID(),iPatientFileIDParam,sPatientNoteParam);
    }

    public void closePatientFile(PatientFile patientfileToClose)
    {
        ContentValues values = new ContentValues();
        patientfileToClose.closeFile();
        values.put(PatientFile.COL_DATETIME_END, patientfileToClose.getEnd());

        SQLiteDatabase db = this.getWritableDatabase();
        db.update(PatientFile.TABLE_NAME,values,PatientFile.COL_PATIENT_FILE_ID + " = ?", new String[]{String.valueOf(patientfileToClose.getPatientFileID())});
    }
}
