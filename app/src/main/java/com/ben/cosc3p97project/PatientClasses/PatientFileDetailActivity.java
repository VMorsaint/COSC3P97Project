package com.ben.cosc3p97project.PatientClasses;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.ben.cosc3p97project.DatabaseClasses.DBHelper;
import com.ben.cosc3p97project.DatabaseClasses.PatientFile;
import com.ben.cosc3p97project.R;

/**
 * Created by VMorsaint on 12/5/2015.
 */
public class PatientFileDetailActivity extends AppCompatActivity
{
    public static final String ARG_ITEM_ID = "patient_file_id";
    String sPatientFileID = "";
    DBHelper dbHelperPatientFileDetail;
    PatientFile mPatientFileItem;
    Boolean bEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_file_detail);

        if (savedInstanceState == null)
        {
            sPatientFileID = getIntent().getStringExtra(ARG_ITEM_ID);
            dbHelperPatientFileDetail = new DBHelper(this);
            mPatientFileItem = dbHelperPatientFileDetail.getPatientFile(sPatientFileID);
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
            if (mPatientFileItem.getPatientFileID() == 0)
            {
                mPatientFileItem = dbHelperPatientFileDetail.updatePatientFile(mPatientFileItem, mPatientFileItem.getPatientID(), sName, mPatientFileItem.getStart(), mPatientFileItem.getEnd());
            }
            else
            {

            }

            invalidateOptionsMenu();
            setLayout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
