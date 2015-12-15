package com.ben.cosc3p97project;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import rajawali.BaseObject3D;
import rajawali.RajawaliActivity;
import rajawali.util.OnObjectPickedListener;

public class BodyActivity extends RajawaliActivity implements
        GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener, OnObjectPickedListener {

    //gesture detector to detect the different types of gestures
    private GestureDetectorCompat mDetector;

    //render, displays the graphics
    private BodyRenderer mRenderer;

    //text to display which body part has been selected
    private TextView selectedBodyText;

    private String currentBodyPart;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        //init the renderer
        mRenderer = new BodyRenderer(this);

        //give it the surface to draw on
        mRenderer.setSurfaceView(mSurfaceView);

        //set the renderer for the activity
        super.setRenderer(mRenderer);

        //init the gesture detector
        mDetector = new GestureDetectorCompat(this,this);
        mDetector.setOnDoubleTapListener(this);

        //make a new layout
        LinearLayout newLayout = new LinearLayout(this);
        newLayout.setOrientation(LinearLayout.VERTICAL);

        //init the responsive text
        selectedBodyText = new TextView(this);

        selectedBodyText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        float DENSITY_SCALE = getResources().getDisplayMetrics().density;

        selectedBodyText.setGravity(Gravity.CENTER_HORIZONTAL);
        selectedBodyText.setPadding(0, (int) (20 * DENSITY_SCALE + 0.5f), 0, 0);
        //for testing
        selectedBodyText.setText("Selected Body Part");
        selectedBodyText.setTextColor(getResources().getColor(R.color.white));
        selectedBodyText.setBackgroundColor(getResources().getColor(R.color.black));

        //add the text view to be at the top of the screen
        newLayout.addView(selectedBodyText);

        //remove the current layout
        ((ViewGroup) mLayout.getParent()).removeView(mLayout);

        mLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1));

        //add layout
        newLayout.addView(mLayout);

        LinearLayout buttonContainer = new LinearLayout(this);
        buttonContainer.setBackgroundColor(getResources().getColor(R.color.black));

        LinearLayout.LayoutParams buttonRules = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        buttonRules.setMargins(10,10,10,10);

        Button cancelButton = new Button(this);
        cancelButton.setText("Cancel");
        cancelButton.setLayoutParams(buttonRules);
        cancelButton.setBackgroundColor(getResources().getColor(R.color.white));
        cancelButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                setResult(Activity.RESULT_CANCELED, new Intent());
                finish();
            }
        });

        Button confirmButton = new Button(this);
        confirmButton.setText("OK");
        confirmButton.setLayoutParams(buttonRules);
        confirmButton.setBackgroundColor(getResources().getColor(R.color.white));
        confirmButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(currentBodyPart != null){
                    Intent intent = new Intent();
                    intent.putExtra("body_part", currentBodyPart);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            }
        });
        
        buttonContainer.addView(cancelButton);
        buttonContainer.addView(confirmButton);

        newLayout.addView(buttonContainer);

        setContentView(newLayout);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_rajawali, menu);
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.mDetector.onTouchEvent(event);
        // Be sure to call the superclass implementation
        return super.onTouchEvent(event);
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

    @Override
    public boolean onDown(MotionEvent event) {
        Log.d("BodyActivity Gesture","onDown: " + event.toString());
        return true;
    }

    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2,
                           float velocityX, float velocityY) {
        Log.d("BodyActivity Gesture", "onFling: " + event1.toString()+event2.toString());
        return true;
    }

    @Override
    public void onLongPress(MotionEvent event) {
        Log.d("BodyActivity Gesture", "onLongPress: " + event.toString());
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {
        Log.d("BodyActivity Gesture", "onScroll: " + distanceX +" " +distanceY);
        mRenderer.setRotation(distanceY, distanceX);
        return true;
    }

    @Override
    public void onShowPress(MotionEvent event) {
        Log.d("BodyActivity Gesture", "onShowPress: " + event.toString());
    }

    @Override
    public boolean onSingleTapUp(MotionEvent event) {
        Log.d("BodyActivity Gesture", "onSingleTapUp: " + event.toString());

        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent event) {
        Log.d("BodyActivity Gesture", "onDoubleTap: " + event.toString());
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent event) {
        Log.d("BodyActivity Gesture", "onDoubleTapEvent: " + event.toString());
        return true;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent event) {
        Log.d("BodyActivity Gesture", "onSingleTapConfirmed: " + event.toString());
        mRenderer.getObjectAt(event.getX(), event.getY());
        return true;
    }

    /**
     * Listener for the object picker callback.
     * Updates label on screen and the class bodypart variable
     * @param object - object that has been selected.
     */
    @Override
    public void onObjectPicked(final BaseObject3D object) {

        //change text on screen
        this.runOnUiThread(new Runnable() {
            public void run() {
                selectedBodyText.setText(object.getName());
            }
        });

        //update class variable
        currentBodyPart = object.getName();
    }
}
