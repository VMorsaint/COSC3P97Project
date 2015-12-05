package com.ben.assignment2;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;

public class NewMeeting extends AppCompatActivity  implements LoaderManager.LoaderCallbacks<Cursor>  {


    private static final int PICK_CONTACT = 1;

    @SuppressLint("InlinedApi")
    private static final String[] PROJECTION =
            {
                    ContactsContract.Contacts._ID,
                    ContactsContract.Contacts.LOOKUP_KEY,
                    Build.VERSION.SDK_INT
                            >= Build.VERSION_CODES.HONEYCOMB ?
                            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY :
                            ContactsContract.Contacts.DISPLAY_NAME

            };

    private ArrayList<String> contacts = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_meeting);
        Button save = (Button)findViewById(R.id.save);

        if(savedInstanceState != null) {
            //retrieved saved contacts on rotate
            ArrayList<String> savedContacts = savedInstanceState.getStringArrayList("contacts");
            if (savedContacts != null)
                contacts = savedContacts;
            getSupportLoaderManager().initLoader(0, null, this);
        }

        //on save
        save.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                //values to be saved for meeting
                ContentValues meetingValues = new ContentValues();

                //set name
                String name = ((EditText) findViewById(R.id.name)).getText().toString();
                meetingValues.put("name", name);

                //set description
                String description = ((EditText) findViewById(R.id.description)).getText().toString();
                meetingValues.put("description", description);

                //set datetime
                DatePicker dp = (DatePicker) findViewById(R.id.date);
                String date = dp.getYear() + "-" + (dp.getMonth() + 1) + "-" + dp.getDayOfMonth();
                TimePicker tp = (TimePicker) findViewById(R.id.time);
                date += " " + tp.getCurrentHour() + ":" + tp.getCurrentMinute();
                meetingValues.put("meeting_date", date);

                //open database
                SQLiteDatabase db = openOrCreateDatabase("assignment2", MODE_PRIVATE, null);

                //create record
                long meetingId = db.insert("meetings", null, meetingValues);

                //create contact associtations
                for (String contact : contacts) {                    
                    ContentValues contactValues = new ContentValues();
                    contactValues.put("meeting_id", meetingId);
                    contactValues.put("contact_id", contact);
                    db.insert("contacts", null, contactValues);
                }

                //close activity
                finish();
            }
        });
    }
    // slelection method to build query for contact infos
    private String selection(){
        String selec = ContactsContract.Contacts.LOOKUP_KEY + "=?";
        for(int i = 1;  i < contacts.size();i++)
            selec += " OR " + selec;
        return selec;
    }


    //create cursor loader to retrieve contact names
    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {        
        // Starts the query
        return new CursorLoader(
                this,
                ContactsContract.Contacts.CONTENT_URI,
                PROJECTION,
                selection(),
                contacts.toArray(new String[0]),
                null
        );
    }

    //ctach loadder finish and display contact names
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {        
        //render contact list
        cursor.moveToFirst();
        if (cursor.getCount() > 0){
            LinearLayout list = (LinearLayout) findViewById(R.id.contact_list);
            while (!cursor.isAfterLast()) {                
                //render result from cursor
                RelativeLayout container = new RelativeLayout(this);

                //build view
                TextView contactInfo = new TextView(this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
                params.gravity = Gravity.CENTER_HORIZONTAL;
                contactInfo.setLayoutParams(params);
                contactInfo.setTextSize(13);
                //set contact name
                contactInfo.append(cursor.getString(2));

                //add view to container
                container.addView(contactInfo);

                list.addView(container);

                cursor.moveToNext();
            }
        }
        cursor.close();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {}

    //save contacts onrotate
    @Override
    public void onSaveInstanceState(Bundle bundle){
        //save contacts that have been added
        bundle.putStringArrayList("contacts", contacts);
        super.onSaveInstanceState(bundle);
    }

    //start activity to pick a contact
    public void addContact(View v){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
        startActivityForResult(intent, PICK_CONTACT);
    }

    //catch activity return and display contact
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //check codes
        if(requestCode == PICK_CONTACT){
            if(resultCode == RESULT_OK){

                //get cursor
                Uri contactData = data.getData();
                Cursor cursor =  managedQuery(contactData, null, null, null, null);
                cursor.moveToFirst();
                
                //get lookup key for contact and add to array
                String key = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.PhoneLookup.LOOKUP_KEY));                
                contacts.add(key);

                //display contact name
                String name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Identity.DISPLAY_NAME));                
                TextView contactView = new TextView(this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
                params.gravity = Gravity.CENTER_HORIZONTAL;
                contactView.setLayoutParams(params);
                contactView.setText(name);
                ((LinearLayout)findViewById(R.id.contact_list)).addView(contactView);

            }
        }
    }
}
