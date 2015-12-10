package com.ben.cosc3p97project.PatientClasses;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.ben.cosc3p97project.DatabaseClasses.DBHelper;
import com.ben.cosc3p97project.DatabaseClasses.PatientNote;
import com.ben.cosc3p97project.R;

/**
 * Created by VMorsaint on 12/5/2015.
 */
public class PatientNoteDetailActivity extends AppCompatActivity
{
    public static final String ARG_PATIENT_NOTE_ID = "patient_note_id";
    public static final String ARG_PATIENT_FILE_ID = "patient_file_id";
    String sPatientNoteID = "";
    String sPatientFileID = "";
    DBHelper dbHelperPatientNoteDetail;
    PatientNote mPatientNoteItem;
    Boolean bEditMode = false;
    Boolean bNewRecord = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_note_detail);

        if (savedInstanceState == null)
        {
            sPatientNoteID = getIntent().getStringExtra(ARG_PATIENT_NOTE_ID);
            sPatientFileID = getIntent().getStringExtra(ARG_PATIENT_FILE_ID);
            dbHelperPatientNoteDetail = new DBHelper(this);
            bNewRecord = bEditMode = sPatientNoteID.equals("0");
            if (bNewRecord)
            {
                mPatientNoteItem = new PatientNote();
            }
            else
            {
                mPatientNoteItem = dbHelperPatientNoteDetail.getPatientNote(sPatientNoteID);
            }
            setLayout();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (bEditMode)
        {
            getMenuInflater().inflate(R.menu.menu_patient_note_detail_edit, menu);
        }
        else
        {
            getMenuInflater().inflate(R.menu.menu_patient_note_detail, menu);
        }

        return true;
    }

    private void setLayout()
    {
        if (bEditMode)
        {
            ((TextView) findViewById(R.id.textViewPatientNoteEdit)).setText(mPatientNoteItem.getNote());
            ((TextView) findViewById(R.id.textViewPatientNoteView)).setVisibility(View.GONE);
            ((TextView) findViewById(R.id.textViewPatientNoteEdit)).setVisibility(View.VISIBLE);
        }
        else
        {
            ((TextView) findViewById(R.id.textViewPatientNoteView)).setText(mPatientNoteItem.getNote());
            ((TextView) findViewById(R.id.textViewPatientNoteEdit)).setVisibility(View.GONE);
            ((TextView) findViewById(R.id.textViewPatientNoteView)).setVisibility(View.VISIBLE);
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
            bEditMode = false;
            invalidateOptionsMenu();
            setLayout();
            return true;

        }
        else if (id == R.id.action_accept)
        {
            bEditMode = false;
            String sNote = ((TextView) findViewById(R.id.textViewPatientNoteEdit)).getText().toString();
            if (bNewRecord)
            {
                mPatientNoteItem = new PatientNote(0,Long.valueOf(sPatientFileID),sNote);
                dbHelperPatientNoteDetail.addPatientNote(mPatientNoteItem);
                sPatientNoteID = String.valueOf(mPatientNoteItem.getPatientNoteID());
                bNewRecord = false;
            }
            else
            {
                mPatientNoteItem = dbHelperPatientNoteDetail.updatePatientNote(mPatientNoteItem, mPatientNoteItem.getPatientFileID(), sNote);
            }

            invalidateOptionsMenu();
            setLayout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
