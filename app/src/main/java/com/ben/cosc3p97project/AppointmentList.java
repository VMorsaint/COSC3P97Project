package com.ben.cosc3p97project;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import com.ben.cosc3p97project.DatabaseClasses.DBHelper;
import com.ben.cosc3p97project.DatabaseClasses.Patient;
import com.ben.cosc3p97project.DatabaseClasses.PatientAppointment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AppointmentList extends AppCompatActivity {

    String date;
    private int patientId;
    private DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_list);

        db = new DBHelper(this);

        date = getIntent().getStringExtra("date");
        if(date!=null){
            //get todays date
            date = new Date().toString();
        }
        patientId = getIntent().getIntExtra("patient_id", -1);
    }

    @Override
    public void onResume(){
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
                final Calendar cal = Calendar.getInstance();
                DatePickerDialog dpd = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            Log.d("DatePicker", dayOfMonth + "-"
                                    + (monthOfYear + 1) + "-" + year);

                        }
                    }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
                dpd.show();
                break;
            case R.id.new_app:
                Intent it = new Intent(this, AppointmentForm.class);
                it.putExtra("date", date);
                if(patientId > 0) {
                    it.putExtra("patient", patientId);
                }
                startActivity(it);
                break;
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //load appointments for the current day
    private void loadAppointments(){
        ArrayList<PatientAppointment> apps;
        apps = db.getAppointmentList(date, patientId);
        ViewGroup root = (ViewGroup)findViewById(android.R.id.content);
        root.removeAllViews();
        for(PatientAppointment pa: apps){
            //display list
            Patient p = db.getPatient(pa.getPatientID());
            TextView tv = new TextView(this);
            tv.append(p.getFirstName() + " " + p.getLastName() + "\n");
            tv.append(pa.getStartTime() + "-" + pa.getEndTime());
            root.addView(tv);
        }
    }
}
