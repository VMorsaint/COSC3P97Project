/**
 * Created by VMorsaint - 4864450 on 11/7/2015.
 * Cosc 3p97 Assignment 2
 */
package com.ben.cosc3p97project.DatabaseClasses;


import android.provider.BaseColumns;

public class PatientFileBodyLocation implements BaseColumns
{
    private int iPatientFileBodyLocationID;
    private int iPatientFileID;
    private int iBodyLocationID;
    public static final String TABLE_NAME = "tPatientFiles";
    public static final String COL_PATIENT_FILE_BODY_LOCATION_ID = "cBodyLocationId";
    public static final String COL_BODY_LOCATION_ID = "cBodyLocationId";
    public static final String COL_PATIENT_FILE_ID = "cPatientFileId";


    public PatientFileBodyLocation()
    {
        iPatientFileBodyLocationID = 0;
        iPatientFileID = 0;
        iBodyLocationID = 0;
    }

    public PatientFileBodyLocation(int iPatientFileIDParam, int iPatientFileBodyLocationIDParam, int iBodyLocationIdParam)
    {
        iPatientFileBodyLocationID = iPatientFileBodyLocationIDParam;
        iPatientFileID = iPatientFileIDParam;
        iBodyLocationID = iBodyLocationIdParam;
    }

    public int getID()
    {
        return iPatientFileBodyLocationID;
    }
    public int getPatientFileID()
    {
        return iPatientFileID;
    }
    public int getBodyLocationID()
    {
        return iBodyLocationID;
    }
}
