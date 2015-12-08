package com.ben.cosc3p97project.PatientClasses;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
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

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_detail);

        if (savedInstanceState == null)
        {
            sPatientFileID = getIntent().getStringExtra(ARG_ITEM_ID);
            dbHelperPatientFileDetail = new DBHelper(this);
            mPatientFileItem = dbHelperPatientFileDetail.getPatientFile(sPatientFileID);
            ((TextView) findViewById(R.id.textViewPatientFirstNameEdit)).setText(mPatientFileItem.getName());
            ((TextView) findViewById(R.id.textViewPatientLastNameEdit)).setText(mPatientFileItem.getStart());
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
