package com.ben.cosc3p97project;

import android.app.DatePickerDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.DataSetObserver;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.ben.cosc3p97project.DatabaseClasses.DBHelper;
import com.ben.cosc3p97project.DatabaseClasses.Patient;
import com.ben.cosc3p97project.DatabaseClasses.PatientAppointment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AppointmentList extends AppCompatActivity {

    private String date;
    private String patientId;
    private DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_list);

        //helper for database
        db = new DBHelper(this);

        //get date string from intent
        date = getIntent().getStringExtra("date");

        //if no date passed get to days date
        if (date == null) {
            date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        }

        //get patient variable from intent
        patientId = getIntent().getStringExtra("patient_id");

        //set title of activity
        setActivityTitle();

        ListView lv = (ListView)findViewById(R.id.expandableListView);
        TextView tv = (TextView)findViewById(R.id.appListEmpty);
        lv.setEmptyView(tv);
    }

    /**
     * Set title of the activity based on the date and if its filtered by a patient
     */
    private void setActivityTitle(){
        if(patientId != null){
            Patient p = db.getPatient(patientId);
            if( p != null){
                setTitle(p.getLastName() + ": " + date);
            }else{
                setTitle(date);
            }
        }else
            setTitle(date);
    }

    /**
     * reload appointments on resume
     */
    @Override
    public void onResume() {
        super.onResume();
        loadAppointments();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_appointment_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        switch (id) {
            case R.id.view_day:

                //dialog to pick a new day to view
                final Calendar cal = Calendar.getInstance();
                DatePickerDialog dpd = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                Log.d("DatePicker", dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year);

                                //set the new date variable
                                date = year + "-"+ (monthOfYear + 1) + "-" + dayOfMonth;

                                //set the activity title
                                setActivityTitle();

                                //load the new appointments
                                loadAppointments();

                            }
                        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
                dpd.show();
                break;
            case R.id.new_app:

                //start the activity to create an appointment
                Intent it = new Intent(this, AppointmentForm.class);
                it.putExtra("date", date);
                if (patientId != null) {
                    it.putExtra("patient", patientId);
                }
                startActivity(it);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     *  load appointments for the current day
     */
    private void loadAppointments() {
        ArrayList<PatientAppointment> apps;
        apps = db.getAppointmentList(date, patientId);
        ExpandableListView exp = (ExpandableListView) findViewById(R.id.expandableListView);
        exp.setAdapter(new AppointmentListAdapter(this, apps));
    }

    /**
     * Lista adapter to link appointment to the expandable list
     */
    private class AppointmentListAdapter implements ExpandableListAdapter {

        private ArrayList<PatientAppointment> apps;
        private AppointmentList context;

        public AppointmentListAdapter(AppointmentList pContext, ArrayList<PatientAppointment> pApps) {
            apps = pApps;
            context = pContext;
        }

        @Override
        public void registerDataSetObserver(DataSetObserver observer) { }

        @Override
        public void unregisterDataSetObserver(DataSetObserver observer) { }

        @Override
        public int getGroupCount() {
            return apps.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return 1;
        }

        @Override
        public Object getGroup(int groupPosition) {
            return apps.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return null;
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }


        // return a view for the patient name and time of app
        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            //create text view for

            PatientAppointment pa = apps.get(groupPosition);

            //if view hasnt been created yet inflate it
            if (convertView == null)
                convertView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                        .inflate(R.layout.appointment_list_item, null);

            //add string to view
            ((TextView) convertView.findViewById(R.id.appointment_text)).setText(pa.toString());

            return convertView;
        }

        //view that contains the action button to display when an app has been clicked
        @Override
        public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

            //if view hasnt been created yet inflate it
            if (convertView == null)
                convertView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                        .inflate(R.layout.appointment_list_item_child, null);

            //edit button
            Button edit = (Button) convertView.findViewById(R.id.edit);
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, AppointmentForm.class);
                    intent.putExtra("appointmentId", apps.get(groupPosition).getId());
                    startActivity(intent);
                }
            });

            //delete button
            Button delete = (Button) convertView.findViewById(R.id.delete);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(context)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("Delete Appointment")
                            .setMessage("Are you sure you want to delete this appointment?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    
                                    //try to delete the app
                                    boolean result = db.deleteAppointment(apps.get(groupPosition).getId());

                                    if (result) {
                                        //reload list
                                        loadAppointments();
                                        Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                                    } else
                                        Toast.makeText(context, "Could not delete.\nPlease try again", Toast.LENGTH_SHORT).show();

                                }

                            })
                            .setNegativeButton("No", null)
                            .show();
                }
            });

            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }

        @Override
        public boolean areAllItemsEnabled() {
            return false;
        }

        @Override
        public boolean isEmpty() {
            return apps.isEmpty();
        }

        @Override
        public void onGroupExpanded(int groupPosition) {}

        @Override
        public void onGroupCollapsed(int groupPosition) {}

        @Override
        public long getCombinedChildId(long groupId, long childId) {
            return 0;
        }

        @Override
        public long getCombinedGroupId(long groupId) {
            return 0;
        }
    }
}
