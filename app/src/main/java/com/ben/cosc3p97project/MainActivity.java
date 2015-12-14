package com.ben.cosc3p97project;

import android.app.Activity;
import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;

import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ben.cosc3p97project.PatientClasses.PatientListActivity;
import android.view.ViewGroup;
import android.widget.TextView;


public class MainActivity extends Activity {

    private GLSurfaceView bodyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //startActivityForResult(new Intent(this, BodyActivity.class), 1);
        //startActivity(new Intent(this, PatientListActivity.class));
        //(new Intent(this, AppointmentList.class));

        ((RecyclerView) findViewById(R.id.listview_main)).setAdapter(new MainActivityRecyclerViewAdapter());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int request, int status, Intent intent)
    {
        if (request == 1) {
            if(status== Activity.RESULT_OK){
                String result=intent.getStringExtra("body_part");
                TextView newText = new TextView(this);
                newText.setText(result);
                ((ViewGroup)findViewById(android.R.id.content)).addView(newText);
            }
            if (status == Activity.RESULT_CANCELED) {
                TextView newText = new TextView(this);
                newText.setText("Canceled");
                ((ViewGroup)findViewById(android.R.id.content)).addView(newText);
            }
         }
    }


}
