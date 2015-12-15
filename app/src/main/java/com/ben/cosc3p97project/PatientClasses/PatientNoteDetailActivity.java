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

//this activity is used to display the note details
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

    //sets default variables and builds gui
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_note_detail);
        //checks for key and parent key param, and builds gui
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

    //sets menu depending on state(edit or read)
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

    //sets layout for gui called from create and onresume
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
        else if (id == R.id.action_accept) //accept changes, will add record if new otherwise update
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
            //rebuild menu/layout
            invalidateOptionsMenu();
            setLayout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
