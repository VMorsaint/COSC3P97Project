
/**
 * Created by VMorsaint - 4864450 on 11/7/2015.
 * Cosc 3p97 Assignment 2
 */
package com.ben.cosc3p97project.DatabaseClasses;

import android.provider.BaseColumns;

public class Patient implements BaseColumns
{
    private int iPatientID;
    private int iAndroidID;
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
        iAndroidID = 0;
        sPatientFileFirstName = "";
        sPatientFileLastName = "";
    }

    public Patient(int iPatientIDParam, int iAndroidIDParam, String sPatientFileFirstNameParam, String sPatientFileLastNameParam)
    {
        iPatientID = iPatientIDParam;
        iAndroidID = iAndroidIDParam;
        sPatientFileFirstName = sPatientFileFirstNameParam;
        sPatientFileLastName = sPatientFileLastNameParam;
    }
    public String getFirstName()
    {
        return sPatientFileFirstName;
    }
    public String getLastName()
    {
        return sPatientFileLastName;
    }
}
