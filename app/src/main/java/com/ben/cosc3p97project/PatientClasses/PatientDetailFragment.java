package com.ben.cosc3p97project.PatientClasses;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ben.cosc3p97project.DatabaseClasses.DBHelper;
import com.ben.cosc3p97project.DatabaseClasses.Patient;
import com.ben.cosc3p97project.R;

public class PatientDetailFragment extends Fragment
{
    public static final String ARG_ITEM_ID = "item_id";
    private Patient mItem;
    public PatientDetailFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID))
        {
            DBHelper dbHelperPatientDetail = new DBHelper(getActivity());
            mItem =dbHelperPatientDetail.getPatient(getArguments().getString(ARG_ITEM_ID));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.patient_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null)
        {
            ((TextView) rootView.findViewById(R.id.textViewPatientFirstNameEdit)).setText(mItem.getFirstName());
            ((TextView) rootView.findViewById(R.id.textViewPatientLastNameEdit)).setText(mItem.getLastName());
        }

        return rootView;
    }
}
