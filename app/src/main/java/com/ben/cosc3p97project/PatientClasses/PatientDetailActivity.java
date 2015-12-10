package com.ben.cosc3p97project.PatientClasses;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
    private PatientFileRecyclerViewAdapter myPatientAdapter;
    private boolean bEditMode = false;
    private boolean bNewRecord = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_detail);
        if (savedInstanceState == null)
        {
            dbHelperPatientDetail = new DBHelper(this);
            sPatientID = getIntent().getStringExtra(ARG_ITEM_ID);
            bNewRecord = bEditMode = sPatientID.equals("0");
            if (bNewRecord)
            {
                mPatientItem = new Patient();
                ((TextView) findViewById(R.id.textViewPatientFilesLabel)).setVisibility(View.GONE);
            }
            else
            {
                mPatientItem = dbHelperPatientDetail.getPatient(sPatientID);
                if (mPatientItem != null)
                {
                    mPatientFileList = dbHelperPatientDetail.getPatientFileListByPatientId(sPatientID);
                    if (mPatientFileList != null)
                    {
                        mPatientFileList.add(new PatientFile(0, Integer.parseInt(sPatientID), "New File", "", ""));
                        myPatientAdapter = new PatientFileRecyclerViewAdapter(mPatientFileList);
                        ((RecyclerView) findViewById(R.id.listView_patientFile_items)).setAdapter(myPatientAdapter);
                    }
                }
            }
            setLayout();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (bEditMode)
        {
            getMenuInflater().inflate(R.menu.menu_patient_detail_edit, menu);
        }
        else
        {
            getMenuInflater().inflate(R.menu.menu_patient_detail, menu);
        }

        return true;
    }

    private void setLayout()
    {
        if (bEditMode)
        {
            ((TextView) findViewById(R.id.textViewPatientFirstNameEdit)).setText(mPatientItem.getFirstName());
            ((TextView) findViewById(R.id.textViewPatientLastNameEdit)).setText(mPatientItem.getLastName());
            ((TextView) findViewById(R.id.textViewPatientFirstNameView)).setVisibility(View.GONE);
            ((TextView) findViewById(R.id.textViewPatientLastNameView)).setVisibility(View.GONE);
            ((TextView) findViewById(R.id.textViewPatientFirstNameEdit)).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.textViewPatientLastNameEdit)).setVisibility(View.VISIBLE);
        }
        else
        {
            ((TextView) findViewById(R.id.textViewPatientFirstNameView)).setText(mPatientItem.getFirstName());
            ((TextView) findViewById(R.id.textViewPatientLastNameView)).setText(mPatientItem.getLastName());
            ((TextView) findViewById(R.id.textViewPatientFirstNameEdit)).setVisibility(View.GONE);
            ((TextView) findViewById(R.id.textViewPatientLastNameEdit)).setVisibility(View.GONE);
            ((TextView) findViewById(R.id.textViewPatientFirstNameView)).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.textViewPatientLastNameView)).setVisibility(View.VISIBLE);
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
        else if (id == R.id.action_edit)
        {
            bEditMode = true;
            invalidateOptionsMenu();
            setLayout();
            return true;
        }
        else if (id == R.id.action_cancel)
        {
            bEditMode = false;
            invalidateOptionsMenu();
            setLayout();
            return true;

        }
        else if (id == R.id.action_accept)
        {
            bEditMode = false;
            String sFirstName = ((TextView) findViewById(R.id.textViewPatientFirstNameEdit)).getText().toString();
            String sLastName = ((TextView) findViewById(R.id.textViewPatientLastNameEdit)).getText().toString();
            if (bNewRecord)
            {

                mPatientItem = new Patient(0,"",sFirstName,sLastName);
                dbHelperPatientDetail.addPatient(mPatientItem);
                sPatientID = String.valueOf(mPatientItem.getPatientID());
                mPatientFileList = new ArrayList<>();
                mPatientFileList.add(new PatientFile(0, mPatientItem.getPatientID(), "New File", "", ""));
                myPatientAdapter = new PatientFileRecyclerViewAdapter(mPatientFileList);
                ((TextView) findViewById(R.id.textViewPatientFilesLabel)).setVisibility(View.VISIBLE);
                ((RecyclerView) findViewById(R.id.listView_patientFile_items)).setAdapter(myPatientAdapter);
                bNewRecord = false;
            }
            else
            {
                mPatientItem = dbHelperPatientDetail.updatePatient(mPatientItem,mPatientItem.getAndroidId(),sFirstName,sLastName);
            }
            invalidateOptionsMenu();
            setLayout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
