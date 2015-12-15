package com.ben.cosc3p97project.PatientClasses;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


import com.ben.cosc3p97project.AppointmentForm;
import com.ben.cosc3p97project.AppointmentList;

import com.ben.cosc3p97project.DatabaseClasses.DBHelper;
import com.ben.cosc3p97project.DatabaseClasses.Patient;
import com.ben.cosc3p97project.DatabaseClasses.PatientFile;
import com.ben.cosc3p97project.R;

import java.util.ArrayList;

/**
 * Created by VMorsaint/BenMannell on 12/5/2015.
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
    private boolean bShowActive = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_detail);

        if (savedInstanceState == null && getIntent().hasExtra(ARG_ITEM_ID))
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
            }
        }
        else
        {
            this.finish();
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
            ((MenuItem) menu.findItem(R.id.action_show_all)).setVisible(bShowActive);
            ((MenuItem) menu.findItem(R.id.action_show_active)).setVisible(!bShowActive);
        }
        return true;
    }

    //build gui based on state
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
            ((TextView) findViewById(R.id.textViewPatientFilesLabel)).setVisibility(View.GONE);
            ((RecyclerView) findViewById(R.id.listView_patientFile_items)).setVisibility(View.GONE);
        }
        else
        {
            ((TextView) findViewById(R.id.textViewPatientFirstNameView)).setText(mPatientItem.getFirstName());
            ((TextView) findViewById(R.id.textViewPatientLastNameView)).setText(mPatientItem.getLastName());
            ((TextView) findViewById(R.id.textViewPatientFirstNameEdit)).setVisibility(View.GONE);
            ((TextView) findViewById(R.id.textViewPatientLastNameEdit)).setVisibility(View.GONE);
            ((TextView) findViewById(R.id.textViewPatientFirstNameView)).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.textViewPatientLastNameView)).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.textViewPatientFilesLabel)).setVisibility(View.VISIBLE);
            ((RecyclerView) findViewById(R.id.listView_patientFile_items)).setVisibility(View.VISIBLE);
        }

    }

    //handle menu actions
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if (id == android.R.id.home)//back to last screen
        {
            this.finish();
            return true;
        }
        else if (id == R.id.action_edit) //starts edit
        {
            bEditMode = true;
            invalidateOptionsMenu();
            setLayout();
            return true;
        }
        else if (id == R.id.action_cancel) //cancels edit, edits form if in addnew mode
        {
            if (bNewRecord)
            {
                this.finish();
            }
            else
            {
                bEditMode = false;
                invalidateOptionsMenu();
                setLayout();
            }
            return true;
        }
        else if (id == R.id.action_accept) //accepts edit and updates patient
        {
            bEditMode = false;
            String sFirstName = ((TextView) findViewById(R.id.textViewPatientFirstNameEdit)).getText().toString();
            String sLastName = ((TextView) findViewById(R.id.textViewPatientLastNameEdit)).getText().toString();
            if (bNewRecord)
            {

                mPatientItem = new Patient(0,"",sFirstName,sLastName,mPatientItem.getsDateAdded());
                dbHelperPatientDetail.addPatient(mPatientItem);
                sPatientID = String.valueOf(mPatientItem.getPatientID());
                mPatientFileList = new ArrayList<>();
                mPatientFileList.add(new PatientFile(0, mPatientItem.getPatientID(), "New File","", "", ""));
                myPatientAdapter = new PatientFileRecyclerViewAdapter(mPatientFileList);
                ((RecyclerView) findViewById(R.id.listView_patientFile_items)).setAdapter(myPatientAdapter);
                bNewRecord = false;
            }
            else
            {
                mPatientItem = dbHelperPatientDetail.updatePatient(mPatientItem,mPatientItem.getAndroidId(),sFirstName,sLastName,mPatientItem.getsDateAdded());
            }
            invalidateOptionsMenu();
            setLayout();
            return true;
        }
        else if (id == R.id.action_show_active) //changes filter to active patients only
        {
            bShowActive = true;
            buildFileList();
            invalidateOptionsMenu();
        }
        else if (id == R.id.action_show_all) //changes filter to show all
        {
            bShowActive = false;
            buildFileList();
            invalidateOptionsMenu();
        }
        else if(id == R.id.action_view_apps) //link appointment to patient
        {
            Intent intent = new Intent(this, AppointmentList.class);
            intent.putExtra("patient_id", sPatientID);
            startActivity(intent);
        }else if(id == R.id.action_new_app)//link appointment to patient
        {
            Intent intent = new Intent(this, AppointmentForm.class);
            intent.putExtra("patient_id", sPatientID);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    //calls sql query and assigns adapter
    private void buildFileList()
    {
        if (mPatientItem != null && !bNewRecord)
        {
            mPatientFileList = dbHelperPatientDetail.getPatientFileListByPatientId(sPatientID, bShowActive);
            if (mPatientFileList != null)
            {

                mPatientFileList.add(new PatientFile(0, Integer.parseInt(sPatientID), "New File","", "", ""));

                myPatientAdapter = new PatientFileRecyclerViewAdapter(mPatientFileList);
                ((RecyclerView) findViewById(R.id.listView_patientFile_items)).setAdapter(myPatientAdapter);
            }
        }
    }

    //rebuilds gui
    @Override
    public void onResume()
    {
        super.onResume();
        buildFileList();
        setLayout();
    }
}
