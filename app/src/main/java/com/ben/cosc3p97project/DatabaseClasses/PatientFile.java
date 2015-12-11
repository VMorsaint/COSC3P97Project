/**
 * Created by VMorsaint - 4864450 on 11/7/2015.
 * Cosc 3p97 Assignment 2
 */
package com.ben.cosc3p97project.DatabaseClasses;


import android.provider.BaseColumns;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PatientFile implements BaseColumns
{
    private long iPatientFileID;
    private long iPatientID;
    private String sPatientFileName;
    private String sPatientFileStart;
    private String sPatientFileEnd;
    public static final String TABLE_NAME = "tPatientFiles";
    public static final String COL_PATIENT_FILE_ID = "cPatientFilePk";
    public static final String COL_PATIENT_ID = "cPatientFk";
    public static final String COL_NAME = "cName";
    public static final String COL_DATETIME_START = "cTimeStart";
    public static final String COL_DATETIME_END = "cTimeEnd";
    private boolean bClosed = false;

    public PatientFile()
    {
        Calendar calDateNow = Calendar.getInstance();
        SimpleDateFormat dfDateNow = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        iPatientFileID = 0;
        iPatientID = 0;
        sPatientFileName = "";
        sPatientFileStart = dfDateNow.format(calDateNow.getTime());
        sPatientFileEnd = "";
    }

    public PatientFile(long iPatientFileIDParam, long iPatientIDParam, String sPatientFileNameParam, String sStartParam, String sEndParam)
    {
        iPatientFileID = iPatientFileIDParam;
        iPatientID = iPatientIDParam;
        sPatientFileName = sPatientFileNameParam;
        sPatientFileStart = sStartParam;
        sPatientFileEnd = sEndParam;
    }
    public void closeFile()
    {
        Calendar calDateNow = Calendar.getInstance();
        SimpleDateFormat dfDateNow = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sPatientFileEnd = dfDateNow.format(calDateNow.getTime());
    }
    public boolean isClosed()
    {
        return (sPatientFileEnd.length() != 0);
    }

    public long getPatientFileID ()
    {
        return iPatientFileID;
    }
    public long getPatientID()
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
