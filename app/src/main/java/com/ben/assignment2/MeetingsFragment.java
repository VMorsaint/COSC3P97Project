package com.ben.assignment2;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MeetingsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String DATE = "queryDate";


    //date of listed meetings
    private String currentDateString;


    public static MeetingsFragment newInstance(String date) {
        MeetingsFragment fragment = new MeetingsFragment();
        Bundle args = new Bundle();
        args.putString(DATE, date);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MeetingsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (getArguments() != null) {
            currentDateString = getArguments().getString(DATE);
        }
        //create db object      
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle) {
        super.onCreateView(inflater, viewGroup, bundle);
        return inflater.inflate(R.layout.fragment_meetings_list, viewGroup, false);
    }

    @Override
    public void onResume() {
        buildMeetingList();
        super.onResume();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_meetings_fragment, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    //hanlde menu options
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_new:
                //start activity for a new meeting
                Intent intent = new Intent(getActivity(), NewMeeting.class);
                intent.putExtra(ListMeetings.LIST_DATE, currentDateString);
                startActivity(intent);
                break;
            case R.id.clear_meetings:
                //delete all meetings for current fragment date
                //open db connection
                SQLiteDatabase db = getActivity().openOrCreateDatabase("assignment2", getActivity().MODE_PRIVATE, null);
                String[] dateRange = new String[]{currentDateString + " 00:00", currentDateString + "23:99"};
                db.delete("meetings", "meeting_date BETWEEN ? AND ?", dateRange);
                db.close();
                break;
            case R.id.defer_meetings:
                //didnt get this working, stupid sqlite
                //deferMeetings();
                Toast toast = Toast.makeText(getActivity(), "Action doesn't work.", Toast.LENGTH_SHORT);
                toast.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /*
    private void deferMeetings() {
        SQLiteDatabase db = getActivity().openOrCreateDatabase("assignment2", getActivity().MODE_PRIVATE, null);
        db.execSQL("UPDATE meetings SET meeting_date = DATETIME(meeting_date, '+1 day') where meeting_date BETWEEN '" + currentDateString + " 00:00' AND '" + currentDateString + " 23:99';");
        db.close();
        buildMeetingList();
    }*/

    // queries db and display a list of meetings for the given date
    private void buildMeetingList() {
        ViewGroup rootView = (ViewGroup) getView();

        //create db object      
        SQLiteDatabase db = getActivity().openOrCreateDatabase("assignment2", getActivity().MODE_PRIVATE, null);
        
        //get meetings for today        
        Cursor resultSet;
        resultSet = db.rawQuery("Select rowid,* from meetings where meeting_date BETWEEN '" + currentDateString + " 00:00' AND '" + currentDateString + " 23:99';", null);
        resultSet.moveToFirst();

        //get layout
        LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.list);

        //clear previous meetings
        layout.removeAllViews();


        if (resultSet.getCount() <= 0) {
            //display message if no meetings
            TextView tv = new TextView(getActivity());
            tv.append("No meetings for " + currentDateString);
            layout.addView(tv);
        } else {
            while (!resultSet.isAfterLast()) {

                //container for meeting
                RelativeLayout container = new RelativeLayout(getActivity());

                //build view
                TextView tv = new TextView(getActivity());

                //get record info
                String name = resultSet.getString(1);
                String description = resultSet.getString(2);
                String datetime = resultSet.getString(3);

                //add text to view
                tv.append(name + "\n");
                tv.append(description + "\n");
                tv.append(datetime + "\n");

                tv.setOnClickListener(new MeetingClickListener(resultSet.getString(0)));
                //add view to list
                container.addView(tv);

                //button  to delete meeting
                Button removeButton = new Button(getActivity());
                removeButton.setText("Delete");
                removeButton.setOnClickListener(new DeleteMeetingClickListener(resultSet.getString(0)));

                //make layout
                RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                rl.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

                //set layout params for button
                removeButton.setLayoutParams(rl);

                //add to list
                container.addView(removeButton);
                layout.addView(container);

                //next record
                resultSet.moveToNext();
            }
        }

        //close db stream
        db.close();
    }

    //listener to go to meeting info page
    class MeetingClickListener implements View.OnClickListener {
        private String MEETING_ID;

        public MeetingClickListener(String id) {
            MEETING_ID = id;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MeetingsFragment.this.getActivity(), ContactsManager.class);
            intent.putExtra("meetingId", MEETING_ID);
            startActivity(intent);
        }
    }

    //listener to delete single meeting
    class DeleteMeetingClickListener implements View.OnClickListener {
        private String[] MEETING_ID = new String[1];

        public DeleteMeetingClickListener(String id) {
            MEETING_ID[0] = id;
        }

        @Override
        public void onClick(View v) {
            SQLiteDatabase db = getActivity().openOrCreateDatabase("assignment2", getActivity().MODE_PRIVATE, null);
            db.delete("meetings", "rowid = ?", MEETING_ID);
            db.delete("contacts", "meeting_id = ?", MEETING_ID);
            db.close();
            buildMeetingList();
        }
    }

}
