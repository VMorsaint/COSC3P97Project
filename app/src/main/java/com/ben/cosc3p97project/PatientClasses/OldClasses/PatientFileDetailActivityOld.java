package com.ben.cosc3p97project.PatientClasses.OldClasses;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.ben.cosc3p97project.PatientClasses.OldClasses.PatientDetailFragment;
import com.ben.cosc3p97project.PatientClasses.PatientListActivity;
import com.ben.cosc3p97project.R;

/**
 * Created by VMorsaint on 12/5/2015.
 */
public class PatientFileDetailActivityOld extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_detail);
        if (savedInstanceState == null)
        {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(PatientDetailFragment.ARG_ITEM_ID,
                    getIntent().getStringExtra(PatientDetailFragment.ARG_ITEM_ID));
            PatientDetailFragment fragment = new PatientDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.patient_detail_container, fragment)
                    .commit();
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
