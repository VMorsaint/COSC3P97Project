package com.ben.assignment2;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;


public class PickDay extends Fragment {

    public static PickDay newInstance() {
        PickDay fragment = new PickDay();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public PickDay() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_pick_day, container, false);

        //when day is selected  start activity to view meetings for that day
        ((CalendarView) rootView.findViewById(R.id.calendarView)).setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                Intent intent = new Intent(getActivity(), AllMeetings.class);
                intent.putExtra(ListMeetings.LIST_DATE, year + "-" + (month+1) + "-" + dayOfMonth);
                startActivity(intent);
            }
        });

        return rootView;
    }


}
