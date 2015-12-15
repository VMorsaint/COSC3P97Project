package com.ben.cosc3p97project;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;

//main screen, used to direct user to proper task
public class MainActivity extends Activity {

    private GLSurfaceView bodyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((RecyclerView) findViewById(R.id.listview_main)).setAdapter(new MainActivityRecyclerViewAdapter());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


}
