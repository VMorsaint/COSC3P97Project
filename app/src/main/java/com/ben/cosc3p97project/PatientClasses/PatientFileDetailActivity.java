package com.ben.cosc3p97project.PatientClasses;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ben.cosc3p97project.BodyActivity;
import com.ben.cosc3p97project.DatabaseClasses.DBHelper;
import com.ben.cosc3p97project.DatabaseClasses.PatientFile;
import com.ben.cosc3p97project.DatabaseClasses.PatientNote;
import com.ben.cosc3p97project.R;

import java.util.ArrayList;

/**
 * Created by VMorsaint on 12/5/2015.
 */
public class PatientFileDetailActivity extends AppCompatActivity implements View.OnClickListener
{
    public static final String ARG_PATIENT_FILE_ID = "patient_file_id";
    public static final String ARG_PATIENT_ID = "patient_id";
    String sPatientFileID = "";
    String sPatientID = "";
    String sBodyPartKey = "";
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
        ((TextView) findViewById(R.id.textViewPatientFileBodyPartKeyEdit)).setOnClickListener(this);

        if (savedInstanceState == null && getIntent().hasExtra(ARG_PATIENT_ID) && getIntent().hasExtra(ARG_PATIENT_FILE_ID))
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
            }
             setLayout();
        }
        else
        {
            this.finish();
        }
    }
    @Override
    public void onClick(View v)
    {
        if(v.getId() == R.id.textViewPatientFileBodyPartKeyEdit)
        {
            startActivityForResult(new Intent(this, BodyActivity.class), 1);
        }
    }
    @Override
    protected void onActivityResult(int request, int status, Intent intent)
    {
        if (request == 1)
        {
            if(status== Activity.RESULT_OK)
            {
                String result=intent.getStringExtra("body_part");
                sBodyPartKey = result;
                ((TextView) findViewById(R.id.textViewPatientFileBodyPartKeyEdit)).setText(getBodyPartKeyString(sBodyPartKey));
            }
            if (status == Activity.RESULT_CANCELED){}
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
            ((MenuItem) menu.findItem(R.id.action_close_file)).setVisible(!mPatientFileItem.isClosed());
        }
        return true;
    }
    private void buildNoteList()
    {
        if (mPatientFileItem != null && !bNewRecord)
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

    private String getBodyPartKeyString(String sKey)
    {
        String sBodyPartKeyString;
        int iIdentifierBodyPartKey;
        sBodyPartKeyString = sKey;

        if (sBodyPartKeyString.length()>0)
        {
            iIdentifierBodyPartKey = getResources().getIdentifier(sBodyPartKeyString,"string",getPackageName());
            if (iIdentifierBodyPartKey > 0)
            {
                sBodyPartKeyString = getString(iIdentifierBodyPartKey);
            }
        }
        return sBodyPartKeyString;
    }

    private void setLayout()
    {
        String sPatientFileBodyPartString = getBodyPartKeyString(mPatientFileItem.getBodyPartKey());
        if (bEditMode)
        {
            ((TextView) findViewById(R.id.textViewPatientFileNameEdit)).setText(mPatientFileItem.getName());
            ((TextView) findViewById(R.id.textViewPatientFileNameView)).setVisibility(View.GONE);
            ((TextView) findViewById(R.id.textViewPatientFileNameEdit)).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.textViewPatientFileBodyPartKeyEdit)).setText(sPatientFileBodyPartString);
            ((TextView) findViewById(R.id.textViewPatientFileBodyPartKeyView)).setVisibility(View.GONE);
            ((TextView) findViewById(R.id.textViewPatientFileBodyPartKeyEdit)).setVisibility(View.VISIBLE);

            ((TextView) findViewById(R.id.textViewPatientFileNotesLabel)).setVisibility(View.GONE);
            ((RecyclerView) findViewById(R.id.listView_patientNote_items)).setVisibility(View.GONE);
        }
        else
        {
            ((TextView) findViewById(R.id.textViewPatientFileNameView)).setText(mPatientFileItem.getName());
            ((TextView) findViewById(R.id.textViewPatientFileNameEdit)).setVisibility(View.GONE);
            ((TextView) findViewById(R.id.textViewPatientFileNameView)).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.textViewPatientFileBodyPartKeyView)).setText(sPatientFileBodyPartString);
            ((TextView) findViewById(R.id.textViewPatientFileBodyPartKeyEdit)).setVisibility(View.GONE);
            ((TextView) findViewById(R.id.textViewPatientFileBodyPartKeyView)).setVisibility(View.VISIBLE);

            ((TextView) findViewById(R.id.textViewPatientFileNotesLabel)).setVisibility(View.VISIBLE);
            ((RecyclerView) findViewById(R.id.listView_patientNote_items)).setVisibility(View.VISIBLE);
        }
        ((TextView) findViewById(R.id.textViewPatientFileStartView)).setText(mPatientFileItem.getStart());
        if (mPatientFileItem.isClosed())
        {
            ((TextView) findViewById(R.id.textViewPatientFileEndView)).setText(mPatientFileItem.getEnd());
            ((TextView) findViewById(R.id.textViewPatientFileEndView)).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.textViewPatientFileEndLabel)).setVisibility(View.VISIBLE);

        }
        else
        {
            ((TextView) findViewById(R.id.textViewPatientFileEndView)).setVisibility(View.GONE);
            ((TextView) findViewById(R.id.textViewPatientFileEndLabel)).setVisibility(View.GONE);
        }

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
        else if (id == R.id.action_accept)
        {
            bEditMode = false;
            String sName = ((TextView) findViewById(R.id.textViewPatientFileNameEdit)).getText().toString();
            if (bNewRecord)
            {
                mPatientFileItem = new PatientFile(0,Long.valueOf(sPatientID),sName,sBodyPartKey,mPatientFileItem.getStart(),"");
                dbHelperPatientFileDetail.addPatientFile(mPatientFileItem);
                sPatientFileID = String.valueOf(mPatientFileItem.getPatientFileID());
                mPatientNoteList = new ArrayList<>();
                mPatientNoteList.add(new PatientNote(0, mPatientFileItem.getPatientFileID(), "New Note"));
                myPatientNoteAdapter = new PatientNoteRecyclerViewAdapter(mPatientNoteList);
                ((RecyclerView) findViewById(R.id.listView_patientNote_items)).setAdapter(myPatientNoteAdapter);
                bNewRecord = false;
            }
            else
            {
                mPatientFileItem = dbHelperPatientFileDetail.updatePatientFile(mPatientFileItem, mPatientFileItem.getPatientID(), sName,sBodyPartKey, mPatientFileItem.getStart(), mPatientFileItem.getEnd());
            }

            invalidateOptionsMenu();
            setLayout();
            return true;
        }
        else if (id == R.id.action_close_file)
        {
            dbHelperPatientFileDetail.closePatientFile(mPatientFileItem);
            Toast.makeText(getApplicationContext(), "File Closed",
                    Toast.LENGTH_SHORT).show();
            invalidateOptionsMenu();
            setLayout();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onResume()
    {
        super.onResume();
        buildNoteList();
    }
}
