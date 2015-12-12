package com.ben.cosc3p97project.DatabaseClasses;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Ben on 12/7/2015.
 */
public class PatientAppointment {

    private int id;
    private String iPatientID;
    private String patientName;
    private String date;
    private String startTime;
    private String endTime;

    public static final String TABLE_NAME = "tPatients";
    public static final String COL_ID = "rowid";
    public static final String COL_PATIENT_ID = "cPatientId";
    public static final String COL_DATE = "cDate";
    public static final String COL_START_TIME = "cStartTime";
    public static final String COL_END_TIME = "cEndTime";

    public PatientAppointment(int appId, String iPatientIDParam, String iDate, String iStartTime, String iEndTime)
    {
        id = appId;
        iPatientID = iPatientIDParam;
        startTime = iStartTime;
        date = iDate;
        endTime = iEndTime;
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
    public String getStartTime()
    {
        return startTime;
    }

    public void setName(String name){
        patientName = name;
    }

    public int getId(){ return id; }

    @Override
    public String toString(){
        return patientName + "\n" + startTime +"-"+endTime;
    }
}
