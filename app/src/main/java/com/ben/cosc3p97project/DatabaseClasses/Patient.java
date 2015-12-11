
/**
 * Created by VMorsaint - 4864450 on 11/7/2015.
 * Cosc 3p97 Assignment 2
 */
package com.ben.cosc3p97project.DatabaseClasses;

import android.provider.BaseColumns;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Patient implements BaseColumns
{
    private long iPatientID;
    private String sAndroidID;
    private String sPatientFileFirstName;
    private String sPatientFileLastName;
    private String sDateAdded;
    public static final String TABLE_NAME = "tPatients";
    public static final String COL_PATIENT_ID = "cPatientPk";
    public static final String COL_ANDROID_ID = "cAndroidId";
    public static final String COL_FIRST_NAME = "cFirstName";
    public static final String COL_LAST_NAME = "cLastName";
    public static final String COL_DATE_ADDED = "cDateAdded";

    public Patient()
    {
        iPatientID = 0;
        sAndroidID = "";
        sPatientFileFirstName = "";
        sPatientFileLastName = "";
        Calendar calDateNow = Calendar.getInstance();
        SimpleDateFormat dfDateNow = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sDateAdded = dfDateNow.format(calDateNow.getTime());
    }

    public Patient(long iPatientIDParam, String sAndroidIDParam, String sPatientFileFirstNameParam, String sPatientFileLastNameParam, String sDateAddedParam)
    {
        iPatientID = iPatientIDParam;
        sAndroidID = sAndroidIDParam;
        sPatientFileFirstName = sPatientFileFirstNameParam;
        sPatientFileLastName = sPatientFileLastNameParam;
        sDateAdded = sDateAddedParam;
    }
    public long getPatientID ()
    {
        return iPatientID;
    }
    public void setPatientID (long iPatientIDParam)
    {
        iPatientID = iPatientIDParam;
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
    public String getsDateAdded()
    {
        return sDateAdded;
    }
}
