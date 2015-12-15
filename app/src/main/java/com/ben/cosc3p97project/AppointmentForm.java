package com.ben.cosc3p97project;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.ben.cosc3p97project.DatabaseClasses.DBHelper;
import com.ben.cosc3p97project.DatabaseClasses.Patient;
import com.ben.cosc3p97project.DatabaseClasses.PatientAppointment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AppointmentForm extends AppCompatActivity {

    DBHelper db;
    PatientAppointment currentAppointment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_form);
        setTitle("Appointment Form");

        //create  db helper
        db = new DBHelper(this);

        //get passed date
        String date = getIntent().getStringExtra("date");

        //if no date passed get todays date
        if (date == null) {
            date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        }

        //get patient id
        String patientId = getIntent().getStringExtra("patient_id");

        //get passed appointment id
        int editApp = getIntent().getIntExtra("appointmentId", -1);

        //get list of all patients
        ArrayList<Patient> pats = db.getPatientList(false, false);


        //adapter for listview
        PatientAdapter adapter = new PatientAdapter(this, pats);

        //get patient spinner
        Spinner sp = (Spinner)findViewById(R.id.spinner);

        //set adapter
        sp.setAdapter(adapter);

        //get edit text views

        EditText dText = (EditText) findViewById(R.id.editText);
        EditText sText = (EditText) findViewById(R.id.editText2);
        EditText eText = (EditText) findViewById(R.id.editText3);

        //if appointment passed
        if(editApp != -1){

            //get appointment from db
            currentAppointment = db.getAppointment(editApp);

            if(currentAppointment != null) {
                //if apopintment exists get the patient the appointment belongs to
                Patient p = db.getPatient(currentAppointment.getPatientID());
                if (p != null) {
                    //if patent exists set the spinner to that patient
                    sp.setSelection(adapter.getPosition(p));

                    //set edit view with appointment values
                    dText.setText(currentAppointment.getDate());
                    sText.setText(currentAppointment.getStartTime());
                    eText.setText(currentAppointment.getEndTime());
                }
            }
        }else if(patientId != null){
            //if patient id passed get patient from db
            Patient p = db.getPatient(patientId);
            if (p != null) {

                //set spinner to patient
                sp.setSelection(adapter.getPosition(p));

                //set defaults
                dText.setText(date);
                sText.setText("08:00");
                eText.setText("09:00");
            }
        }else{
            //set defaults
            dText.setText(date);
            sText.setText("08:00");
            eText.setText("09:00");
        }

        //set listeners
        dText.setOnFocusChangeListener(new DatePickerListener());
        sText.setOnFocusChangeListener(new TimePickerListener());
        eText.setOnFocusChangeListener(new TimePickerListener());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_appointment_form, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * method that saves or creates a new appointment
     * @param v view being clicked
     */
    public void onSave(View v){


        //get values from form
        Patient p = (Patient)((Spinner)findViewById(R.id.spinner)).getSelectedItem();
        String date = ((EditText)findViewById(R.id.editText)).getText().toString();
        String sTime = ((EditText)findViewById(R.id.editText2)).getText().toString();
        String eTime = ((EditText)findViewById(R.id.editText3)).getText().toString();


        //status of db write
        boolean status = false;

        //try writing to db
        if(currentAppointment == null){
            status = db.addAppointment(Long.toString(p.getPatientID()), date, sTime, eTime);
        }else{
            status = db.updateAppointment(currentAppointment.getId(), Long.toString(p.getPatientID()), date, sTime, eTime);
        }

        //show toast with status
        Toast toaster = Toast.makeText(getApplicationContext(),status ? "Appointment Saved" : "Error occurred.\nPlease try again.", Toast.LENGTH_SHORT);
        toaster.show();

        //finish the activity if the record saved.
        if(status)
            finish();
    }

    /**
     * ListAdapter used to populate the spinner with a list of patients.
     */
    public class PatientAdapter extends ArrayAdapter<Patient> {


        public PatientAdapter(Context context, ArrayList<Patient> list) {
            super(context, android.R.layout.simple_spinner_item, list);
        }

        @Override
        public int getPosition(Patient p){

            //iterate through patient and get position of passed patient
            int index = 0;
            for(int x = 0; x < getCount(); x ++){
                if(p.getPatientID() == getItem(x).getPatientID()){
                    index = x;
                    break;
                }
            }
            return index;
        }

        @Override
        public View getDropDownView(int viewPosn, View view, ViewGroup parent) {
            //create view for patient in spinner

            //get patient
            Patient pat = getItem(viewPosn);
            // Check if an existing view is being reused, otherwise inflate the view
            if (view == null) {
                view = LayoutInflater.from(getContext()).inflate(R.layout.patient_item, parent, false);
            }

            //create view
            TextView tv = (TextView) view.findViewById(R.id.name);
            tv.setText(pat.getFirstName() + " " + pat.getLastName());

            return view;
        }
    }

    /**
     * Class that opens and listeners for the response of a DatePickerDialog
     *
     */
    public class DatePickerListener implements View.OnFocusChangeListener {

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                //text view to edit
                final EditText view = (EditText) v;

                //default values
                int year=2015, month=11, day=1;

                if (view.getText().toString().isEmpty()) {

                    //try to parse date if the date field is full
                    try {
                        Date setDate = new SimpleDateFormat("yyyy-MM-dd").parse(view.getText().toString());
                        day = setDate.getDay();
                        year = setDate.getYear();
                        month = setDate.getMonth();
                    }catch(ParseException pe){
                        Log.d("DateTimePicker", pe.getMessage());
                    }
                }else{
                    //get hte current date
                    Calendar c = Calendar.getInstance();
                    year = c.get(Calendar.YEAR);
                    month = c.get(Calendar.MONTH);
                    day = c.get(Calendar.DATE);
                }

                //launch datepicker dialog
                DatePickerDialog d = new DatePickerDialog(AppointmentForm.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker dateView, int year, int monthOfYear, int dayOfMonth) {
                        //set the view on date select
                        view.setText(year + "-" + (monthOfYear+1) + "-" + dayOfMonth);
                    }
                }, year,month , day);

                //launch dialog
                d.show();

                //unfocus the edittext
                view.clearFocus();
            }
        }
    }


    /**
     * class that opens a timepicker dialog and updates the edit text field
     */
    public class TimePickerListener implements View.OnFocusChangeListener{

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(hasFocus){
                //textview to edit
                final EditText view = (EditText)v;

                //init and launch dialog to select a time
                TimePickerDialog tp;
                tp = new TimePickerDialog(AppointmentForm.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker tpView, int hourOfDay, int minute) {
                        view.setText(String.format("%02d",hourOfDay) + ":" + String.format("%02d",minute));
                    }
                }, 0, 0, true);
                tp.show();

                view.clearFocus();
            }
        }
    }
}
