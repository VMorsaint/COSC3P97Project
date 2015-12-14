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

        db = new DBHelper(this);

        String date = getIntent().getStringExtra("date");
        int editApp = getIntent().getIntExtra("appointmentId", -1);

        //get list of all patients
        ArrayList<Patient> pats = db.getPatientList(false, false);

        PatientAdapter adapter = new PatientAdapter(this, pats);

        Spinner sp = (Spinner)findViewById(R.id.spinner);
        sp.setAdapter(adapter);

        EditText dText = (EditText) findViewById(R.id.editText);
        EditText sText = (EditText) findViewById(R.id.editText2);
        EditText eText = (EditText) findViewById(R.id.editText3);

        if(editApp != -1){
            currentAppointment = db.getAppointment(editApp);
            if(currentAppointment != null) {
                Log.d("App Edit", "Got app");
                Patient p = db.getPatient(currentAppointment.getPatientID());
                if (p != null) {

                    sp.setSelection(adapter.getPosition(p));

                    dText.setText(currentAppointment.getDate());
                    sText.setText(currentAppointment.getStartTime());
                    eText.setText(currentAppointment.getEndTime());

                }
            }
        }

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

        //get patient id
        Patient p = (Patient)((Spinner)findViewById(R.id.spinner)).getSelectedItem();
        String date = ((EditText)findViewById(R.id.editText)).getText().toString();
        String sTime = ((EditText)findViewById(R.id.editText2)).getText().toString();
        String eTime = ((EditText)findViewById(R.id.editText3)).getText().toString();

        boolean status = false;
        if(currentAppointment == null){
            status = db.addAppointment(Long.toString(p.getPatientID()), date, sTime, eTime);
        }else{
            status = db.updateAppointment(currentAppointment.getId(), Long.toString(p.getPatientID()), date, sTime, eTime);
        }

        Toast toaster = Toast.makeText(getApplicationContext(),status ? "Appointment Saved" : "Error occurred.\nPlease try again.", Toast.LENGTH_SHORT);
        toaster.show();

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

            Patient pat = getItem(viewPosn);

            Log.d("Appointment Form Spin", pat.getFirstName());

            // Check if an existing view is being reused, otherwise inflate the view
            if (view == null) {
                view = LayoutInflater.from(getContext()).inflate(R.layout.patient_item, parent, false);
            }

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

                /*
                int year, month, day;

                if (view.getText().toString() != "") {
                    try {
                        Date setDate = new SimpleDateFormat("yyyy-MM-dd").parse(view.getText().toString());
                        day = setDate.getDay();
                    }catch(ParseException pe){
                        Log.d("DateTimePicker", pe.getMessage());
                    }
                }
                */

                //get current date
                final Calendar c = Calendar.getInstance();

                //launch datepicker dialog
                DatePickerDialog d = new DatePickerDialog(AppointmentForm.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker dateView, int year, int monthOfYear, int dayOfMonth) {
                        view.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                d.show();

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
