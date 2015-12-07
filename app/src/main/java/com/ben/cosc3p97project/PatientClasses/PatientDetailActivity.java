package com.ben.cosc3p97project.PatientClasses;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.TextView;

import com.ben.cosc3p97project.DatabaseClasses.DBHelper;
import com.ben.cosc3p97project.DatabaseClasses.Patient;
import com.ben.cosc3p97project.DatabaseClasses.PatientFile;
import com.ben.cosc3p97project.R;

import java.util.ArrayList;

/**
 * Created by VMorsaint on 12/5/2015.
 */
public class PatientDetailActivity extends AppCompatActivity
{
    public static final String ARG_ITEM_ID = "patient_id";
    private String sPatientID = "";
    private Patient mPatientItem;
    private ArrayList<PatientFile> mPatientFileList;
    private DBHelper dbHelperPatientDetail;
    private PatientFileRecyclerViewAdapter myTestAdapter;
    private LinearLayoutManager myTestLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_detail);
        if (savedInstanceState == null)
        {
            sPatientID = getIntent().getStringExtra(ARG_ITEM_ID);
            dbHelperPatientDetail = new DBHelper(this);
            mPatientItem = dbHelperPatientDetail.getPatient(sPatientID);
            if (mPatientItem != null)
            {
                ((TextView) findViewById(R.id.textViewPatientFirstNameEdit)).setText(mPatientItem.getFirstName());
                ((TextView) findViewById(R.id.textViewPatientLastNameEdit)).setText(mPatientItem.getLastName());
                mPatientFileList = dbHelperPatientDetail.getPatientFileListByPatientId(sPatientID);
                if (mPatientFileList != null)
                {
                    myTestAdapter = new PatientFileRecyclerViewAdapter(mPatientFileList);
                    ((RecyclerView) findViewById(R.id.listView_patientFile_items)).setAdapter(myTestAdapter);
                }
            }

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if (id == android.R.id.home)
        {
            navigateUpTo(new Intent(this, PatientListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
