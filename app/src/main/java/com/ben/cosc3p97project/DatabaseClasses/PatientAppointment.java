package com.ben.cosc3p97project.DatabaseClasses;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Ben on 12/7/2015.
 */
public class PatientAppointment {

    private String iPatientID;
    private String patientName;
    private String startTime;
    private String endTime;
    private int noteId;
    public static final String TABLE_NAME = "tPatients";
    public static final String COL_PATIENT_ID = "cPatientId";
    public static final String COL_START_TIME = "cStartTime";
    public static final String COL_END_TIME = "cEndTime";
    public static final String COL_NOTE_ID = "cNoteId";

    public PatientAppointment(String iPatientIDParam, String iStartTime, String iEndTime, int noteIdParam)
    {
        iPatientID = iPatientIDParam;
        startTime = iStartTime;
        endTime = iEndTime;
        noteId = noteIdParam;
        patientName = "";
    }
    public String getPatientID ()
    {
        return iPatientID;
    }
    public String getEndTime()
    {
        return endTime;
    }
    public int getNoteId()
    {
        return noteId;
    }
    public String getStartTime()
    {
        return startTime;
    }

    public void setName(String name){
        patientName = name;
    }
    
    @Override
    public String toString(){
        return patientName + "\n" + startTime +"-"+endTime;
    }
}
