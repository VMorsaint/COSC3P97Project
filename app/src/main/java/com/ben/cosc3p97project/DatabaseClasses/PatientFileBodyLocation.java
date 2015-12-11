/**
 * Created by VMorsaint - 4864450 on 11/7/2015.
 * Cosc 3p97 Assignment 2
 */
package com.ben.cosc3p97project.DatabaseClasses;


import android.provider.BaseColumns;

public class PatientFileBodyLocation implements BaseColumns
{
    private long iPatientFileBodyLocationID;
    private long iPatientFileID;
    private long iBodyLocationID;
    public static final String TABLE_NAME = "tPatientFiles";
    public static final String COL_PATIENT_FILE_BODY_LOCATION_ID = "cBodyLocationPk";
    public static final String COL_BODY_LOCATION_ID = "cBodyLocationFk";
    public static final String COL_PATIENT_FILE_ID = "cPatientFileFk";


    public PatientFileBodyLocation()
    {
        iPatientFileBodyLocationID = 0;
        iPatientFileID = 0;
        iBodyLocationID = 0;
    }

    public PatientFileBodyLocation(long iPatientFileIDParam, long iPatientFileBodyLocationIDParam, long iBodyLocationIdParam)
    {
        iPatientFileBodyLocationID = iPatientFileBodyLocationIDParam;
        iPatientFileID = iPatientFileIDParam;
        iBodyLocationID = iBodyLocationIdParam;
    }

    public long getPatientFileBodyLocationID()
    {
        return iPatientFileBodyLocationID;
    }
    public long getPatientFileID()
    {
        return iPatientFileID;
    }
    public long getBodyLocationID()
    {
        return iBodyLocationID;
    }
}
