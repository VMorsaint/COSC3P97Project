package com.ben.cosc3p97project;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;

import com.ben.cosc3p97project.DatabaseClasses.DBHelper;
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
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadAppointments(){
        ArrayList<PatientAppointment> apps;
        apps = db.getAppointmentList(date, patientId);

        for(PatientAppointment pa: apps){
            //display list
        }

    }
}
