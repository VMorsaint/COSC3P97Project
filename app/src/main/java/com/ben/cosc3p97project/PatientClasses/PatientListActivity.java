package com.ben.cosc3p97project.PatientClasses;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.ben.cosc3p97project.DatabaseClasses.DBHelper;
import com.ben.cosc3p97project.R;

public class PatientListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_list);
        DBHelper dbHelperPatientList = new DBHelper(this);
        RecyclerView recyclerViewPatients = (RecyclerView) findViewById(R.id.listView_patient_items);
        assert recyclerViewPatients != null;
        //dbHelperPatientList.addPatient(new Patient(0, "", "Vincent2", "Morsaint2"));
        recyclerViewPatients.setAdapter(new PatientRecyclerViewAdapter(dbHelperPatientList.getPatientList()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_patient_list, menu);
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
        else if (id == R.id.action_addPatient)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
