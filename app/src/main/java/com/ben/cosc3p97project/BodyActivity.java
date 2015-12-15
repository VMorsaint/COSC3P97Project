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
    private GestureDetectorCompat gDetector;

    //render, displays the graphics
    private BodyRenderer bRenderer;

    //text to display which body part has been selected
    private TextView selectedBodyText;

    //current selected body part
    private String currentBodyPart;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //init the renderer
        bRenderer = new BodyRenderer(this);

        //give it the surface to draw on
        bRenderer.setSurfaceView(mSurfaceView);

        //set the renderer for the activity
        super.setRenderer(bRenderer);

        //init the gesture detector
        //used to register the touch gestures
        gDetector = new GestureDetectorCompat(this,this);
        gDetector.setOnDoubleTapListener(this);

        //remove the current layout
        ((ViewGroup) mLayout.getParent()).removeView(mLayout);

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
        selectedBodyText.setText("Select Body Part");
        selectedBodyText.setTextColor(getResources().getColor(R.color.white));
        selectedBodyText.setBackgroundColor(getResources().getColor(R.color.black));

        //add the text view to be at the top of the screen
        newLayout.addView(selectedBodyText);

        //set new layout params
        mLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1));

        //add layout
        newLayout.addView(mLayout);

        //container for the confirmation buttons
        LinearLayout buttonContainer = new LinearLayout(this);
        buttonContainer.setBackgroundColor(getResources().getColor(R.color.black));

        //create layout params for the buttons
        LinearLayout.LayoutParams buttonRules = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        buttonRules.setMargins(10,10,10,10);

        //create cancel button
        Button cancelButton = new Button(this);
        cancelButton.setText("Cancel");
        cancelButton.setLayoutParams(buttonRules);
        cancelButton.setBackgroundColor(getResources().getColor(R.color.white));
        cancelButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //when clicking cancel return to the starting activity
                setResult(Activity.RESULT_CANCELED, new Intent());
                finish();
            }
        });

        // button to confirm the body part selected

        Button confirmButton = new Button(this);
        confirmButton.setText("OK");
        confirmButton.setLayoutParams(buttonRules);
        confirmButton.setBackgroundColor(getResources().getColor(R.color.white));
        confirmButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //clicking on ok returns the selected body part to statin activity

                if(currentBodyPart != null){
                    Intent intent = new Intent();
                    intent.putExtra("body_part", currentBodyPart);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            }
        });

        //add buttons to view
        buttonContainer.addView(cancelButton);
        buttonContainer.addView(confirmButton);

        //add container view to the layout
        newLayout.addView(buttonContainer);

        //set the new layout
        setContentView(newLayout);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        //send event to the touch detector
        gDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {
        bRenderer.setRotation(distanceY, distanceX);
        return true;
    }

    
    @Override
    public boolean onSingleTapConfirmed(MotionEvent event) {
        bRenderer.getObjectAt(event.getX(), event.getY());
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

    @Override
    public void onShowPress(MotionEvent event) { }

    @Override
    public boolean onSingleTapUp(MotionEvent event) { return true; }

    @Override
    public boolean onDoubleTap(MotionEvent event) { return true; }

    @Override
    public boolean onDoubleTapEvent(MotionEvent event) { return true; }

    @Override
    public boolean onDown(MotionEvent event) { return true; }

    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2,
                           float velocityX, float velocityY) { return true; }

    @Override
    public void onLongPress(MotionEvent event) { }
}
