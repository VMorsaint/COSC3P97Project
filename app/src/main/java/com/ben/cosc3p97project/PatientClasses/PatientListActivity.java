package com.ben.cosc3p97project.PatientClasses;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.ben.cosc3p97project.DatabaseClasses.DBHelper;
import com.ben.cosc3p97project.R;

public class PatientListActivity extends AppCompatActivity {

    private boolean bShowActive = false;
    private boolean bSortByActivity = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_list);
        DBHelper dbHelperPatientList = new DBHelper(this);
        RecyclerView recyclerViewPatients = (RecyclerView) findViewById(R.id.listView_patient_items);
        assert recyclerViewPatients != null;
        recyclerViewPatients.setAdapter(new PatientRecyclerViewAdapter(dbHelperPatientList.getPatientList(false, false)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_patient_list, menu);
        ((MenuItem) menu.findItem(R.id.action_show_all)).setVisible(bShowActive);
        ((MenuItem) menu.findItem(R.id.action_show_active)).setVisible(!bShowActive);
        ((MenuItem) menu.findItem(R.id.action_sort_by_name)).setVisible(bSortByActivity);
        ((MenuItem) menu.findItem(R.id.action_sort_by_activity)).setVisible(!bSortByActivity);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        else if (id == R.id.action_addPatient)
        {
            Intent intent = new Intent(this, PatientDetailActivity.class);
            intent.putExtra(PatientDetailActivity.ARG_ITEM_ID, "0");
            this.startActivity(intent);
            return true;
        }
        else if (id == R.id.action_show_active)
        {
            bShowActive = true;
            //buildFileList();
            invalidateOptionsMenu();
        }
        else if (id == R.id.action_show_all)
        {
            bShowActive = false;
            //buildFileList();
            invalidateOptionsMenu();
        }
        else if (id == R.id.action_sort_by_activity)
        {
            bSortByActivity = true;
            //buildFileList();
            invalidateOptionsMenu();
        }
        else if (id == R.id.action_sort_by_name)
        {
            bSortByActivity = false;
            //buildFileList();
            invalidateOptionsMenu();
        }

        return super.onOptionsItemSelected(item);
    }


}
