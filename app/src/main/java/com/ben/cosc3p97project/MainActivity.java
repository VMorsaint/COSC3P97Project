package com.ben.cosc3p97project;

import android.app.Activity;
import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class MainActivity extends Activity {

    private GLSurfaceView bodyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_main);
        startActivityForResult(new Intent(this, BodyActivity.class), 1);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void viewPatient(View v){

    }

    @Override
    protected void onActivityResult(int request, int status, Intent intent) {

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
