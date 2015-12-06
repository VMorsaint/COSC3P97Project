
/**
 * Created by VMorsaint - 4864450 on 11/7/2015.
 * Cosc 3p97 Assignment 2
 */
package com.ben.cosc3p97project.DatabaseClasses;

import android.provider.BaseColumns;

public class Patient implements BaseColumns
{
    private int iPatientID;
    private String sAndroidID;
    private String sPatientFileFirstName;
    private String sPatientFileLastName;
    public static final String TABLE_NAME = "tPatients";
    public static final String COL_PATIENT_ID = "cPatientId";
    public static final String COL_ANDROID_ID = "cAndroidId";
    public static final String COL_FIRST_NAME = "cFirstName";
    public static final String COL_LAST_NAME = "cLastName";

    public Patient()
    {
        iPatientID = 0;
        sAndroidID = "";
        sPatientFileFirstName = "";
        sPatientFileLastName = "";
    }

    public Patient(int iPatientIDParam, String sAndroidIDParam, String sPatientFileFirstNameParam, String sPatientFileLastNameParam)
    {
        iPatientID = iPatientIDParam;
        sAndroidID = sAndroidIDParam;
        sPatientFileFirstName = sPatientFileFirstNameParam;
        sPatientFileLastName = sPatientFileLastNameParam;
    }
    public int getPatientID ()
    {
        return iPatientID;
    }
    public String getFirstName()
    {
        return sPatientFileFirstName;
    }
    public String getLastName()
    {
        return sPatientFileLastName;
    }
    public String getAndroidId()
    {
        return sAndroidID;
    }
}
