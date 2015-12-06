/**
 * Created by VMorsaint - 4864450 on 11/7/2015.
 * Cosc 3p97 Assignment 2
 */
package com.ben.cosc3p97project.DatabaseClasses;


import android.provider.BaseColumns;

public class PatientFile implements BaseColumns
{
    private int iPatientFileID;
    private int iPatientID;
    private String sPatientFileName;
    private String sPatientFileStart;
    private String sPatientFileEnd;
    public static final String TABLE_NAME = "tPatientFiles";
    public static final String COL_PATIENT_FILE_ID = "cPatientFileId";
    public static final String COL_PATIENT_ID = "cPatientId";
    public static final String COL_NAME = "cName";
    public static final String COL_DATETIME_START = "cTimeStart";
    public static final String COL_DATETIME_END = "cTimeEnd";

    public PatientFile()
    {
        iPatientFileID = 0;
        iPatientID = 0;
        sPatientFileName = "";
        sPatientFileStart = "";
        sPatientFileEnd = "";
    }

    public PatientFile(int iPatientFileIDParam, int iPatientIDParam, String sPatientFileNameParam, String sStart, String sEnd)
    {
        iPatientFileID = iPatientFileIDParam;
        iPatientID = iPatientIDParam;
        sPatientFileName = sPatientFileNameParam;
        sPatientFileStart = sStart;
        sPatientFileEnd = sEnd;
    }

    public int getPatientFileID()
    {
        return iPatientFileID;
    }
    public int getPatientID()
    {
        return iPatientID;
    }
    public String getName()
    {
        return sPatientFileName;
    }
    public String getStart()
    {
        return sPatientFileStart;
    }
    public String getEnd()
    {
        return sPatientFileEnd;
    }

}
