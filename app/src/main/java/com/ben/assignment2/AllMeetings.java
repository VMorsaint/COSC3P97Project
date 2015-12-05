package com.ben.assignment2;

import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AllMeetings extends AppCompatActivity {

    private String date = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_meetings);

        //get the intent
        Intent intent = getIntent();
        date = intent.getStringExtra(ListMeetings.LIST_DATE);
        if(savedInstanceState != null)
            date = savedInstanceState.getString("date", date);

        //create the list fragment from the given day
        Fragment newFragment = MeetingsFragment.newInstance(date);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(android.R.id.content, newFragment).commit();
    }

    @Override
    public void onSaveInstanceState(Bundle bundle){
        bundle.putString("date", date);
        super.onSaveInstanceState(bundle);
    }

}
