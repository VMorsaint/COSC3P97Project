package com.ben.assignment2;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.provider.ContactsContract;

import java.util.ArrayList;

public class ContactsManager extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private ArrayList<String> contactIds = new ArrayList<String>();
    private String meetingId = "";
    private int PICK_CONTACT = 1;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        //get meeting id from intent
        Intent intent = getIntent();
        meetingId = intent.getStringExtra("meetingId");

        //open sql db connection
        SQLiteDatabase db = openOrCreateDatabase("assignment2", MODE_PRIVATE, null);

        //perform query
        Cursor meetingResultSet = db.rawQuery("Select rowid,* from meetings where rowid = '" + meetingId + "';", null);

        //go to first record of result set
        meetingResultSet.moveToFirst();

        //get layout
        LinearLayout layout = (LinearLayout) findViewById(R.id.list);
        layout.removeAllViews();

        //if no meetings were returned exit with a toast message
        if (meetingResultSet.getCount() <= 0) {

            //create and display toast
            Toast toast = Toast.makeText(this, "Meeting doesn't exist", Toast.LENGTH_SHORT);
            toast.show();

            //exit activity
            finish();
        } else {

            //build view
            TextView tv = (TextView) findViewById(R.id.meeting_info);

            //get record info
            String name = meetingResultSet.getString(1);
            String description = meetingResultSet.getString(2);
            String datetime = meetingResultSet.getString(3);


            //add text to view
            tv.append(name + "\n");
            tv.append(description + "\n");
            tv.append(datetime + "\n");
        }

        //close sql stream
        db.close();

        //create associtaed contact list
        initializeContactList();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contacts, menu);
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

    /*
        Build a list of contacts from db
    */
    private void initializeContactList() {

        //open sql connection
        SQLiteDatabase db = openOrCreateDatabase("assignment2", MODE_PRIVATE, null);

        //get layout
        LinearLayout layout = (LinearLayout) findViewById(R.id.list);

        //clear list and array
        layout.removeAllViews();
        contactIds.clear();

        //query for contacts
        Cursor contactsResultSet = db.rawQuery("Select rowid,* from contacts where meeting_id = '" + meetingId + "';", null);

        //go to first record of contacts result set
        contactsResultSet.moveToFirst();

        //if no meetings were returned exit with a toast message
        if (contactsResultSet.getCount() <= 0) {
            //build view
            TextView contactInfo = new TextView(this);
            contactInfo.append("No Contacts for this meeting.");

            layout.addView(contactInfo);
        } else {
            // add ids to the contact list
            while (!contactsResultSet.isAfterLast()) {


                //get record info
                contactIds.add(contactsResultSet.getString(2));
                contactsResultSet.moveToNext();
            }
            getSupportLoaderManager().restartLoader(0, null, this);

        }

        //close sql stream
        db.close();

    }

    //build intent to open contact picker
    public void addContact(View v) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
        startActivityForResult(intent, PICK_CONTACT);
    }


    /*
        Retrieve results from contact picker
    */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_CONTACT) {
            if (resultCode == RESULT_OK) {

                //get contact cursor
                Uri contactData = data.getData();
                Cursor cursor = managedQuery(contactData, null, null, null, null);
                cursor.moveToFirst();

                //get contact lookup key
                String key = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.PhoneLookup.LOOKUP_KEY));

                //add params to be saved
                ContentValues contactValues = new ContentValues();
                contactValues.put("meeting_id", meetingId);
                contactValues.put("contact_id", key);

                //open sql connection
                SQLiteDatabase db = openOrCreateDatabase("assignment2", MODE_PRIVATE, null);

                //save new contact record
                db.insert("contacts", null, contactValues);

                //close db
                db.close();
            }
        }

        //initialize contact list
        initializeContactList();
    }

    //build selection string for a list of contact
    private String selection() {
        String selec = ContactsContract.Contacts.LOOKUP_KEY + "=?";
        for (int i = 1; i < contactIds.size(); i++)
            selec += " OR " + selec;
        return selec;
    }

    //create cursorloader to retrieve a list of contacts associtaed with meeting
    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {
        // Starts the query
        return new CursorLoader(
                this,
                ContactsContract.Contacts.CONTENT_URI,
                PROJECTION,
                selection(),
                contactIds.toArray(new String[0]),
                null
        );
    }

    //catch cursor finish and display result
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        //render contact list
        cursor.moveToFirst();
        if (cursor.getCount() > 0){
            LinearLayout list = (LinearLayout) findViewById(R.id.list);
            while (!cursor.isAfterLast()) {
                //render result from cursor
                RelativeLayout container = new RelativeLayout(this);

                //build view
                TextView contactInfo = new TextView(this);

                contactInfo.setTextSize(30);
                //set contact name
                contactInfo.append(cursor.getString(2));

                //add view to container
                container.addView(contactInfo);

                //button
                Button removeButton = new Button(this);
                removeButton.setText("Remove");
                removeButton.setOnClickListener(new ContactRemoveListener(meetingId, cursor.getString(1)));

                //make layout
                RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                rl.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

                removeButton.setLayoutParams(rl);

                container.addView(removeButton);
                list.addView(container);

                cursor.moveToNext();
            }
        }
        cursor.close();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    /* Contact remove click listener
        Stores id of contact to remove
        Handles click
    */
    class ContactRemoveListener implements View.OnClickListener {
        private String CONTACT_ID, MEETING_ID;

        public ContactRemoveListener(String meeting, String contact) {
            CONTACT_ID = contact;
            MEETING_ID = meeting;
        }

        @Override
        public void onClick(View v) {
            //open db and delete contact association record
            SQLiteDatabase db = openOrCreateDatabase("assignment2", MODE_PRIVATE, null);
            db.execSQL("DELETE FROM contacts WHERE meeting_id = '" + MEETING_ID + "' AND contact_id = '" + CONTACT_ID + "';");
            db.close();
            initializeContactList();
        }
    }
}


