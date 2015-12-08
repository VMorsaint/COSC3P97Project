package com.ben.cosc3p97project.DatabaseClasses;

/**
 * Created by Ben on 12/7/2015.
 */
public class PatientAppointment {

    private int iPatientID;
    private String startTime;
    private String endTime;
    private int noteId;
    public static final String TABLE_NAME = "tPatients";
    public static final String COL_PATIENT_ID = "cPatientId";
    public static final String COL_START_TIME = "cStartTime";
    public static final String COL_END_TIME = "cEndTime";
    public static final String COL_NOTE_ID = "cNoteId";

    public PatientAppointment()
    {
        iPatientID = 0;
        startTime = "";
        endTime = "";
        noteId = 0;
    }

    public PatientAppointment(int iPatientIDParam, String iStartTime, String iEndTime, int noteIdParam)
    {
        iPatientID = iPatientIDParam;
        startTime = iStartTime;
        endTime = iEndTime;
        noteId = noteIdParam;
    }
    public int getPatientID ()
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
}
