package com.ben.cosc3p97project.PatientClasses;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.ben.cosc3p97project.DatabaseClasses.DBHelper;
import com.ben.cosc3p97project.DatabaseClasses.PatientFile;
import com.ben.cosc3p97project.DatabaseClasses.PatientNote;
import com.ben.cosc3p97project.R;

import java.util.ArrayList;

/**
 * Created by VMorsaint on 12/5/2015.
 */
public class PatientFileDetailActivity extends AppCompatActivity
{
    public static final String ARG_PATIENT_FILE_ID = "patient_file_id";
    public static final String ARG_PATIENT_ID = "patient_id";
    String sPatientFileID = "";
    String sPatientID = "";
    DBHelper dbHelperPatientFileDetail;
    PatientFile mPatientFileItem;
    Boolean bEditMode = false;
    Boolean bNewRecord = false;
    private ArrayList<PatientNote> mPatientNoteList;
    private PatientNoteRecyclerViewAdapter myPatientNoteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_file_detail);

        if (savedInstanceState == null)
        {
            sPatientFileID = getIntent().getStringExtra(ARG_PATIENT_FILE_ID);
            sPatientID = getIntent().getStringExtra(ARG_PATIENT_ID);
            dbHelperPatientFileDetail = new DBHelper(this);
            bNewRecord = bEditMode = sPatientFileID.equals("0");
            if (bNewRecord)
            {
                mPatientFileItem = new PatientFile();
                ((TextView) findViewById(R.id.textViewPatientFileNotesLabel)).setVisibility(View.GONE);
            }
            else
            {
                mPatientFileItem = dbHelperPatientFileDetail.getPatientFile(sPatientFileID);
                if (mPatientFileItem != null)
                {
                    mPatientNoteList = dbHelperPatientFileDetail.getPatientNoteListByPatientFileId(sPatientFileID);
                    if (mPatientNoteList != null)
                    {
                        mPatientNoteList.add(new PatientNote(0, Integer.parseInt(sPatientFileID), "New Note"));
                        myPatientNoteAdapter = new PatientNoteRecyclerViewAdapter(mPatientNoteList);
                        ((RecyclerView) findViewById(R.id.listView_patientNote_items)).setAdapter(myPatientNoteAdapter);
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
            getMenuInflater().inflate(R.menu.menu_patient_file_detail_edit, menu);
        }
        else
        {
            getMenuInflater().inflate(R.menu.menu_patient_file_detail, menu);
        }

        return true;
    }

    private void setLayout()
    {
        if (bEditMode)
        {
            ((TextView) findViewById(R.id.textViewPatientFileNameEdit)).setText(mPatientFileItem.getName());
            ((TextView) findViewById(R.id.textViewPatientFileNameView)).setVisibility(View.GONE);
            ((TextView) findViewById(R.id.textViewPatientFileNameEdit)).setVisibility(View.VISIBLE);
        }
        else
        {
            ((TextView) findViewById(R.id.textViewPatientFileNameView)).setText(mPatientFileItem.getName());
            ((TextView) findViewById(R.id.textViewPatientFileNameEdit)).setVisibility(View.GONE);
            ((TextView) findViewById(R.id.textViewPatientFileNameView)).setVisibility(View.VISIBLE);
        }
        ((TextView) findViewById(R.id.textViewPatientFileStartView)).setText(mPatientFileItem.getStart());
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if (id == android.R.id.home)
        {
            //navigateUpTo(new Intent(this, PatientDetailActivity.class));
            this.finish();
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
            String sName = ((TextView) findViewById(R.id.textViewPatientFileNameEdit)).getText().toString();
            if (bNewRecord)
            {
                mPatientFileItem = new PatientFile(0,Long.valueOf(sPatientID),sName,mPatientFileItem.getStart(),"");
                dbHelperPatientFileDetail.addPatientFile(mPatientFileItem);
                sPatientFileID = String.valueOf(mPatientFileItem.getPatientID());
                mPatientNoteList = new ArrayList<>();
                mPatientNoteList.add(new PatientNote(0, mPatientFileItem.getPatientFileID(), "New Note"));
                myPatientNoteAdapter = new PatientNoteRecyclerViewAdapter(mPatientNoteList);
                ((TextView) findViewById(R.id.textViewPatientFilesLabel)).setVisibility(View.VISIBLE);
                ((RecyclerView) findViewById(R.id.listView_patientFile_items)).setAdapter(myPatientNoteAdapter);
                bNewRecord = false;
            }
            else
            {
                mPatientFileItem = dbHelperPatientFileDetail.updatePatientFile(mPatientFileItem, mPatientFileItem.getPatientID(), sName, mPatientFileItem.getStart(), mPatientFileItem.getEnd());
            }

            invalidateOptionsMenu();
            setLayout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
