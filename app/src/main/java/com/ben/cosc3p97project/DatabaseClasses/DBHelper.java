/**
 * Created by VMorsaint - 4864450 on 11/7/2015.
 * Cosc 3p97 Assignment 2
 */
package com.ben.cosc3p97project.DatabaseClasses;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;


public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "PatientFiles.db";
    public static final int DATABASE_VERSION = 16;

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
                    PatientFile.COL_BODYPART_KEY + " TEXT, " +
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
                    PatientAppointment.TABLE_NAME + "(" +
                    PatientAppointment.COL_PATIENT_ID + " INTEGER," +
                    PatientAppointment.COL_DATE + " TEXT," +
                    PatientAppointment.COL_START_TIME + " TEXT," +
                    PatientAppointment.COL_END_TIME + " TEXT)";
            db.execSQL(CREATE_TABLE);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onUpgrade(SQLiteDatabase dbSQL, int iOldVersion, int iNewVersion) {
        dbSQL.execSQL("DROP TABLE IF EXISTS " + Patient.TABLE_NAME);
        dbSQL.execSQL("DROP TABLE IF EXISTS " + PatientFile.TABLE_NAME);
        dbSQL.execSQL("DROP TABLE IF EXISTS " + PatientNote.TABLE_NAME);
        dbSQL.execSQL("DROP TABLE IF EXISTS tPatientFileBodyLocations");
        dbSQL.execSQL("DROP TABLE IF EXISTS tLocations");

        dbSQL.execSQL("DROP TABLE IF EXISTS " + PatientAppointment.TABLE_NAME);
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
        values.put(PatientFile.COL_BODYPART_KEY, newPatientFile.getBodyPartKey());
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

    public long addPatient(Patient newPatient) {
        boolean bFlagOk = true;
        long iPatientID = 0;
        ContentValues values = new ContentValues();
        values.put(Patient.COL_FIRST_NAME, newPatient.getFirstName());
        values.put(Patient.COL_LAST_NAME, newPatient.getLastName());
        values.put(Patient.COL_ANDROID_ID, newPatient.getAndroidId());

        SQLiteDatabase db = this.getWritableDatabase();
        try {
            iPatientID = db.insertOrThrow(Patient.TABLE_NAME, null, values);
            newPatient.setPatientID(iPatientID);
        } catch (Exception e) {
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
        try {
            iPatientNoteID = db.insertOrThrow(PatientNote.TABLE_NAME, null, values);
            newPatientNote.setPatientNoteID(iPatientNoteID);
        } catch (Exception e) {
            bFlagOk = false;
        }
        db.close();
        return bFlagOk;
    }

    public ArrayList<Patient> getPatientList(boolean bActiveOnly, boolean bSortByActivity) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  " +
                Patient.COL_PATIENT_ID + "," +
                Patient.COL_ANDROID_ID + "," +
                Patient.COL_FIRST_NAME + "," +
                Patient.COL_LAST_NAME + "," +
                Patient.COL_DATE_ADDED +
                " FROM " + Patient.TABLE_NAME;

        if (bSortByActivity) {
            selectQuery += " LEFT JOIN (SELECT " + PatientFile.COL_PATIENT_ID + ", MAX(" + PatientFile.COL_DATETIME_START + ") AS cMaxStart"
                    + " FROM " + PatientFile.TABLE_NAME
                    + " GROUP BY " + PatientFile.COL_PATIENT_ID + ") AS tFileActivity ON " + Patient.TABLE_NAME + "." + Patient.COL_PATIENT_ID
                    + " = tFileActivity." + PatientFile.COL_PATIENT_ID;
        }
        if (bActiveOnly) {
            selectQuery += " WHERE " + Patient.COL_PATIENT_ID + " IN (SELECT " + PatientFile.COL_PATIENT_ID
                    + " FROM " + PatientFile.TABLE_NAME
                    + " WHERE " + PatientFile.COL_DATETIME_END + " = '')";
        }


        ArrayList<Patient> PatientList = new ArrayList<>();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null)
        {
            if (cursor.moveToFirst())
            {
                do
                {
                    PatientList.add(new Patient(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Patient.COL_PATIENT_ID))),
                            cursor.getString(cursor.getColumnIndex(Patient.COL_ANDROID_ID)),
                            cursor.getString(cursor.getColumnIndex(Patient.COL_FIRST_NAME)),
                            cursor.getString(cursor.getColumnIndex(Patient.COL_LAST_NAME)),
                            cursor.getString(cursor.getColumnIndex(Patient.COL_DATE_ADDED))));
                }
                while (cursor.moveToNext());
            }
            if (!cursor.isClosed())
            {
                cursor.close();
            }
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
        Patient patientReturned = null;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null)
        {
            if (cursor.moveToFirst())
            {
                patientReturned = new Patient(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Patient.COL_PATIENT_ID))),
                        cursor.getString(cursor.getColumnIndex(Patient.COL_ANDROID_ID)),
                        cursor.getString(cursor.getColumnIndex(Patient.COL_FIRST_NAME)),
                        cursor.getString(cursor.getColumnIndex(Patient.COL_LAST_NAME)),
                        cursor.getString(cursor.getColumnIndex(Patient.COL_DATE_ADDED)));
            } else
            {
                patientReturned = null;
            }
            if (!cursor.isClosed())
            {
                cursor.close();
            }
        }
        db.close();
        return patientReturned;
    }

    public ArrayList<PatientFile> getPatientFileListByPatientId(String sPatientId, boolean bActiveOnly) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  " +
                PatientFile.COL_PATIENT_FILE_ID + "," +
                PatientFile.COL_PATIENT_ID + "," +
                PatientFile.COL_NAME + "," +
                PatientFile.COL_BODYPART_KEY + "," +
                PatientFile.COL_DATETIME_START + "," +
                PatientFile.COL_DATETIME_END +
                " FROM " + PatientFile.TABLE_NAME +
                " WHERE " + PatientFile.COL_PATIENT_ID + " = " + sPatientId;
        if (bActiveOnly) {
            selectQuery += " AND " + PatientFile.COL_DATETIME_END + " = ''";
        }

        ArrayList<PatientFile> PatientFileList = new ArrayList<>();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null)
        {
            if (cursor.moveToFirst())
            {
                do
                {
                    PatientFileList.add(new PatientFile(Integer.parseInt(cursor.getString(cursor.getColumnIndex(PatientFile.COL_PATIENT_FILE_ID))),
                            Integer.parseInt(cursor.getString(cursor.getColumnIndex(PatientFile.COL_PATIENT_ID))),
                            cursor.getString(cursor.getColumnIndex(PatientFile.COL_NAME)),
                            cursor.getString(cursor.getColumnIndex(PatientFile.COL_BODYPART_KEY)),
                            cursor.getString(cursor.getColumnIndex(PatientFile.COL_DATETIME_START)),
                            cursor.getString(cursor.getColumnIndex(PatientFile.COL_DATETIME_END))));
                } while (cursor.moveToNext());
            }
            if (!cursor.isClosed())
            {
                cursor.close();
            }
        }
        db.close();
        return PatientFileList;
    }

    public ArrayList<PatientNote> getPatientNoteListByPatientFileId(String sPatientFileId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT " +
                PatientNote.COL_PATIENT_NOTE_ID + "," +
                PatientNote.COL_PATIENT_FILE_ID + "," +
                PatientNote.COL_NOTE +
                " FROM " + PatientNote.TABLE_NAME +
                " WHERE " + PatientNote.COL_PATIENT_FILE_ID + " = " + sPatientFileId;

        ArrayList<PatientNote> PatientNoteList = new ArrayList<>();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null)
        {
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
            if (!cursor.isClosed())
            {
                cursor.close();
            }
        }
        db.close();
        return PatientNoteList;
    }

    public PatientFile getPatientFile(String iID) {
        String sQuery = "Select * FROM " + PatientFile.TABLE_NAME + " WHERE " + PatientFile.COL_PATIENT_FILE_ID + " = " + iID;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursorResult = db.rawQuery(sQuery, null);
        PatientFile objSelectedPatientFile = null;
        if (cursorResult != null)
        {
            if (cursorResult.moveToFirst())
            {
                objSelectedPatientFile = new PatientFile(Integer.parseInt(cursorResult.getString(0)), Integer.parseInt(cursorResult.getString(1)),
                        cursorResult.getString(2), cursorResult.getString(3), cursorResult.getString(4), cursorResult.getString(5));
            }
            if (!cursorResult.isClosed())
            {
                cursorResult.close();
            }
        }
        db.close();
        return objSelectedPatientFile;
    }


    public PatientNote getPatientNote(String iID)

    {
        String sQuery = "Select * FROM " + PatientNote.TABLE_NAME + " WHERE " + PatientNote.COL_PATIENT_NOTE_ID + " = " + iID;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursorResult = db.rawQuery(sQuery, null);
        PatientNote objSelectedPatientNote = null;
        if (cursorResult != null)
        {
            if (cursorResult.moveToFirst())
            {
                objSelectedPatientNote = new PatientNote(Integer.parseInt(cursorResult.getString(0)), Integer.parseInt(cursorResult.getString(1)),
                        cursorResult.getString(2));
            }
        }
        if (!cursorResult.isClosed())
        {
            cursorResult.close();
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

    public Patient updatePatient(Patient patientOld, String sAndroidIDParam, String sPatientFirstNameParam, String sPatientLastNameParam, String sPatientDateAddedParam) {
        ContentValues values = new ContentValues();
        values.put(Patient.COL_ANDROID_ID, sAndroidIDParam);
        values.put(Patient.COL_FIRST_NAME, sPatientFirstNameParam);
        values.put(Patient.COL_LAST_NAME, sPatientLastNameParam);
        values.put(Patient.COL_DATE_ADDED, sPatientDateAddedParam);

        SQLiteDatabase db = this.getWritableDatabase();
        db.upda
        te(Patient.TABLE_NAME, values, Patient.COL_PATIENT_ID + " = ?", new String[]{String.valueOf(patientOld.getPatientID())});
        return new Patient(patientOld.getPatientID(), sAndroidIDParam, sPatientFirstNameParam, sPatientLastNameParam, sPatientDateAddedParam);
    }

    public PatientFile updatePatientFile(PatientFile patientfileOld, long iPatientIDParam,
                     String sPatientFileNameParam,String sPatientFileBodyPartKeyParam, String sStartParam, String sEndParam) {
        ContentValues values = new ContentValues();
        values.put(PatientFile.COL_PATIENT_ID, iPatientIDParam);
        values.put(PatientFile.COL_NAME, sPatientFileNameParam);
        values.put(PatientFile.COL_BODYPART_KEY, sPatientFileBodyPartKeyParam);
        values.put(PatientFile.COL_DATETIME_START, sStartParam);
        values.put(PatientFile.COL_DATETIME_END, sEndParam);

        SQLiteDatabase db = this.getWritableDatabase();
        db.update(PatientFile.TABLE_NAME, values, PatientFile.COL_PATIENT_FILE_ID + " = ?", new String[]{String.valueOf(patientfileOld.getPatientFileID())});
        return new PatientFile(patientfileOld.getPatientFileID(), iPatientIDParam, sPatientFileNameParam, sPatientFileBodyPartKeyParam, sStartParam, sEndParam);
    }

    public PatientNote updatePatientNote(PatientNote patientnoteOld, long iPatientFileIDParam, String sPatientNoteParam) {
        ContentValues values = new ContentValues();
        values.put(PatientNote.COL_PATIENT_FILE_ID, iPatientFileIDParam);
        values.put(PatientNote.COL_NOTE, sPatientNoteParam);

        SQLiteDatabase db = this.getWritableDatabase();
        db.update(PatientNote.TABLE_NAME, values, PatientNote.COL_PATIENT_NOTE_ID + " = ?", new String[]{String.valueOf(patientnoteOld.getPatientNoteID())});
        return new PatientNote(patientnoteOld.getPatientNoteID(), iPatientFileIDParam, sPatientNoteParam);
    }

    public void closePatientFile(PatientFile patientfileToClose) {
        ContentValues values = new ContentValues();
        patientfileToClose.closeFile();
        values.put(PatientFile.COL_DATETIME_END, patientfileToClose.getEnd());

        SQLiteDatabase db = this.getWritableDatabase();
        db.update(PatientFile.TABLE_NAME, values, PatientFile.COL_PATIENT_FILE_ID + " = ?", new String[]{String.valueOf(patientfileToClose.getPatientFileID())});
    }

        /**
     * Get a list of appointments for a given day and patient
     * @param date
     * @param patientId
     * @return
     */
    public ArrayList<PatientAppointment> getAppointmentList(String date, String patientId) {
        //init variables
        ArrayList<PatientAppointment> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor;
        String query;

        if (patientId == null) {
            //get list of all appointments on day
            query = "SELECT a.rowid, a.*, p." + Patient.COL_FIRST_NAME + ", p." + Patient.COL_LAST_NAME +
                    " FROM " + PatientAppointment.TABLE_NAME + " a LEFT JOIN " +
                    Patient.TABLE_NAME + " p ON a." + PatientAppointment.COL_PATIENT_ID + " = p." + Patient.COL_PATIENT_ID +
                    " WHERE a." + PatientAppointment.COL_DATE + " = '" + date +
                    "' ORDER BY a." + PatientAppointment.COL_START_TIME + ";";

        } else {
            //get list of appointments for patient on day
            query = "SELECT a.rowid, a.*, p." + Patient.COL_FIRST_NAME + ", p." + Patient.COL_LAST_NAME +
                    " FROM " + PatientAppointment.TABLE_NAME + " a LEFT JOIN " +
                    Patient.TABLE_NAME + " p ON a." + PatientAppointment.COL_PATIENT_ID + " = p." + Patient.COL_PATIENT_ID +
                    " WHERE a." + PatientAppointment.COL_DATE + " = '" + date +
                    "' AND a." + PatientAppointment.COL_PATIENT_ID + " = " + patientId + " ORDER BY a." + PatientAppointment.COL_START_TIME + ";";
        }
        
        //get cursor from db
        cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        //build list to return 
        while(!cursor.isAfterLast()){
            list.add(new PatientAppointment(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5) + " " + cursor.getString(6)));
            cursor.moveToNext();
        }

        //return list
        return list;
    }

    /**
     * add an appointment to the db
     *  return boolean on whether the file was succesffully added
     * @param iPatientIDParam
     * @param date
     * @param iStartTime
     * @param iEndTime
     * @return
     */
    public boolean addAppointment(String iPatientIDParam, String date, String iStartTime, String iEndTime) {

        //init values
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        boolean status = true;

        //set values
        values.put(PatientAppointment.COL_PATIENT_ID, iPatientIDParam);
        values.put(PatientAppointment.COL_START_TIME, iStartTime);
        values.put(PatientAppointment.COL_END_TIME, iEndTime);
        values.put(PatientAppointment.COL_DATE, date);

        //try to add the record
        //catch any sql errors
        try {
            db.insertOrThrow(PatientAppointment.TABLE_NAME, null, values);
        } catch (SQLException e) {
            status = false;
        }

        //close connection
        db.close();

        return status;
    }

    /**
     * method to update an appointment record
     * @param appId - appointment id
     * @param iPatientIDParam - patient id
     * @param date - date of app
     * @param iStartTime - start time of app
     * @param iEndTime - end time of app
     * @return - return whether record was updated T/F
     */
    public boolean updateAppointment(int appId, String iPatientIDParam, String date, String iStartTime, String iEndTime) {
        //init variables
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        boolean status = false;
        int rows;

        //set values
        values.put(PatientAppointment.COL_PATIENT_ID, iPatientIDParam);
        values.put(PatientAppointment.COL_START_TIME, iStartTime);
        values.put(PatientAppointment.COL_END_TIME, iEndTime);
        values.put(PatientAppointment.COL_DATE, date);

        //update the app
        rows = db.update(PatientAppointment.TABLE_NAME, values, PatientAppointment.COL_ID + " = ? ", new String[]{String.valueOf(appId)});

        //if rows were updated return true
        if(rows >= 1)
            status = true;

        //close the db connection
        db.close();

        return status;
    }

    /**
     * get a single appointment fromt he given id
     * @param appId - appointment id
     * @return - appointment object
     */
    public PatientAppointment getAppointment(int appId) {
        //query for db
        String sQuery = "SELECT a.rowid, a.*, p." + Patient.COL_FIRST_NAME + ", p." + Patient.COL_LAST_NAME +
                " FROM " + PatientAppointment.TABLE_NAME + " a LEFT JOIN " +
                Patient.TABLE_NAME + " p ON a." + PatientAppointment.COL_PATIENT_ID + " = p." + Patient.COL_PATIENT_ID +
                " WHERE a.rowid = " + appId + ";";

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursorResult = db.rawQuery(sQuery, null);
        PatientAppointment app;

        //get the result if there is one
        if (cursorResult.moveToFirst()) {
            app = new PatientAppointment(cursorResult.getInt(0), cursorResult.getString(1), cursorResult.getString(2), cursorResult.getString(3), cursorResult.getString(4), cursorResult.getString(5) + " " + cursorResult.getString(6));
        } else {
            app = null;
        }

        //close db connection
        db.close();

        return app;
    }

  
    public boolean deleteAppointment(int id){
        //inti values
        SQLiteDatabase db = getWritableDatabase();
        String clause = "rowid = ?";

        //delte patient, get number of rows effected
        int effected = db.delete(PatientAppointment.TABLE_NAME, clause, new String[]{String.valueOf(id)});

        //close db connection
        db.close();

        //return boolean if rows were effected
        return (effected >= 1);
    }
}
