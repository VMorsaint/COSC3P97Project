/**
 * Created by VMorsaint - 4864450 on 11/7/2015.
 * Cosc 3p97 Assignment 2
 */

package com.ben.cosc3p97project.DatabaseClasses;

import android.provider.BaseColumns;

public class PatientNote implements BaseColumns
{
    private long iPatientNoteID;
    private long iPatientFileID;
    private String sPatientNote;

    public static final String TABLE_NAME = "tPatientNotes";
    public static final String COL_PATIENT_NOTE_ID = "cPatientNoteId";
    public static final String COL_PATIENT_FILE_ID = "cPatientFileId";
    public static final String COL_NOTE = "cNotes";

    public PatientNote()
    {
        iPatientNoteID = 0;
        iPatientFileID = 0;
        sPatientNote = "";
    }

    public PatientNote(long iPatientNoteIDParam, long iPatientFileIDParam, String sPatientNoteParam)
    {
        iPatientNoteID = iPatientNoteIDParam;
        iPatientFileID = iPatientFileIDParam;
        sPatientNote = sPatientNoteParam;
    }

    public long getPatientNoteID()
    {
        return iPatientNoteID;
    }
    public void setPatientNoteID(long iPatientNoteIDParam)
    {
        iPatientNoteID = iPatientNoteIDParam;
    }

    public long getPatientFileID()
    {
        return iPatientFileID;
    }

    public String getNote()
    {
        return sPatientNote;
    }

}
