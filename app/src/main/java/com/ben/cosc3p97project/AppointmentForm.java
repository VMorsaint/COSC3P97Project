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

import com.ben.cosc3p97project.DatabaseClasses.DBHelper;
import com.ben.cosc3p97project.DatabaseClasses.Patient;
import com.ben.cosc3p97project.DatabaseClasses.PatientAppointment;

import java.util.ArrayList;
import java.util.Calendar;

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
        String editApp = getIntent().getStringExtra("appointmentId");

        ArrayList<Patient> pats = db.getPatientList(true, false);

        PatientAdapter adapter = new PatientAdapter(this, pats);



        Spinner sp = (Spinner)findViewById(R.id.spinner);
        if(editApp != null){
            currentAppointment = db.getAppointment(editApp);
            Patient p = db.getPatient(currentAppointment.getPatientID());
            if(p != null)
                sp.setSelection(adapter.getPosition(p));
        }
        //sp.setAdapter(adapter);

        TextView dateText = (TextView) findViewById(R.id.editText);
        dateText.setOnFocusChangeListener(new DatePickerListener());

        TextView timeText = (TextView) findViewById(R.id.editText2);
        timeText.setOnFocusChangeListener(new TimePickerListener());

        TextView timeText2 = (TextView) findViewById(R.id.editText3);
        timeText2.setOnFocusChangeListener(new TimePickerListener());
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

        if(currentAppointment == null){
            db.addAppointment(Long.toString(p.getPatientID()), date, sTime, eTime);
        }else{
            db.updateAppointment(currentAppointment.getId(), Long.toString(p.getPatientID()), date, sTime, eTime);
        }
    }

    /**
     * ListAdapter used to populate the spinner with a list of patients.
     */
    public class PatientAdapter extends ArrayAdapter<Patient> {

        public PatientAdapter(Context context, ArrayList<Patient> list) {
            super(context, 0, list);
        }

        public View getView(int viewPosn, View view, ViewGroup parent) {

            Patient pat = getItem(viewPosn);
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
                        view.setText(String.format("%01d",hourOfDay) + ":" + String.format("%01d",minute));
                    }
                }, 0, 0, true);
                tp.show();
            }
        }
    }
}
