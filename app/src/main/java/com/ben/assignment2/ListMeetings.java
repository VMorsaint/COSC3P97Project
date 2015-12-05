package com.ben.assignment2;


import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class ListMeetings extends AppCompatActivity{

    public final static String LIST_DATE= "com.ben.assignment2.DATE";

    public DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    String[] dates = new String[2];
    boolean LANDSCAPE;
    PickDay dayPicker;
    private ViewPager pageViewer;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter pageAdapter;

    public ListMeetings(){}

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_meetings);

        //create database and tables
        SQLiteDatabase db = openOrCreateDatabase("assignment2",MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS meetings( name TEXT, description TEXT, meeting_date TEXT);");
        db.execSQL("CREATE TABLE IF NOT EXISTS contacts( meeting_id INTEGER REFERENCES meetings(rowid) ON DELETE CASCADE, contact_id TEXT);");
        db.close();

        //set landscape boolean
        LANDSCAPE = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;

        //create new calendar
        Calendar tempCal = Calendar.getInstance();

        //new date of current date
        Date tempDate = new Date();

        //set current date and format into strings
        tempCal.setTime(tempDate);
        dates[0] = df.format(tempCal.getTime());
        tempCal.add(Calendar.DATE, 1);
        dates[1] = df.format(tempCal.getTime());

        // Instantiate a ViewPager and a PagerAdapter.
        pageViewer = (ViewPager) findViewById(R.id.pager);
        pageAdapter = new MeetingsPagerAdapter(getSupportFragmentManager());
        pageViewer.setAdapter(pageAdapter);
        pageViewer.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // When changing pages, reset the action bar actions since they are dependent
                // on which page is currently active. An alternative approach is to have each
                // fragment expose actions itself (rather than the activity exposing actions),
                // but for simplicity, the activity provides the actions in this sample.
                invalidateOptionsMenu();
            }
        });


        dayPicker = PickDay.newInstance();

    }

    // adapter to manager fragments
    private class MeetingsPagerAdapter extends FragmentStatePagerAdapter {
        public MeetingsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        // returna  fragment to display
        @Override
        public Fragment getItem(int position) {
            if(position == 2)
                return dayPicker;
            else
                return MeetingsFragment.newInstance(dates[position]);
        }

        //depending on oreientation only swipe between 2 pages
        // day picker will already be displayed
        @Override
        public int getCount() {
            return LANDSCAPE ? 2 : 3;
        }
    }


}
