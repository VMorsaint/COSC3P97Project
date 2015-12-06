/**
 * Created by VMorsaint - 4864450 on 11/7/2015.
 * Cosc 3p97 Assignment 2
 */
package com.ben.cosc3p97project.DatabaseClasses;

import android.provider.BaseColumns;


public class BodyLocation implements BaseColumns
{
    private int iLocationID;
    private String sLocationName;
    public static final String TABLE_NAME = "tLocations";
    public static final String COL_LOCATION_ID = "cLocationId";
    public static final String COL_NAME = "cName";

    public BodyLocation(String sName)
    {
        iLocationID = 0;
        sLocationName = sName;
    }

    public BodyLocation(int iID, String sName)
    {
        iLocationID = iID;
        sLocationName = sName;
    }
    public String getName()
    {
        return sLocationName;
    }
}
