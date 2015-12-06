package com.ben.cosc3p97project.PatientClasses;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ben.cosc3p97project.DatabaseClasses.DBHelper;
import com.ben.cosc3p97project.DatabaseClasses.Patient;
import com.ben.cosc3p97project.DatabaseClasses.PatientFile;
import com.ben.cosc3p97project.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class PatientDetailFragment extends Fragment
{
    public static final String ARG_ITEM_ID = "item_id";
    private String sPatientID;
    private Patient mPatientItem;
    private ArrayList<PatientFile> mPatientFileList;
    private RecyclerView recyclerViewPatientFiles;


    public PatientDetailFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID))
        {
            DBHelper dbHelperPatientDetail = new DBHelper(getActivity());
            sPatientID = getArguments().getString(ARG_ITEM_ID);
            mPatientItem = dbHelperPatientDetail.getPatient(sPatientID);

            Calendar cDateStart = Calendar.getInstance();
            SimpleDateFormat fSQLDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            dbHelperPatientDetail.addPatientFile(new PatientFile(0, Integer.parseInt(sPatientID),
                    "testFile",fSQLDate.format(cDateStart.getTime()),""));
            mPatientFileList = dbHelperPatientDetail.getPatientFileListByPatientId(sPatientID);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.patient_detail, container, false);
        if (mPatientItem != null)
        {

            recyclerViewPatientFiles = (RecyclerView) rootView.findViewById(R.id.listView_patientFile_items);
            assert recyclerViewPatientFiles != null;
            //dbHelperPatientList.addPatient(new Patient(0, "", "Vincent2", "Morsaint2"));
            recyclerViewPatientFiles.setAdapter(new PatientFileRecyclerViewAdapter(mPatientFileList));
            ((TextView) rootView.findViewById(R.id.textViewPatientFirstNameEdit)).setText(mPatientItem.getFirstName());
            ((TextView) rootView.findViewById(R.id.textViewPatientLastNameEdit)).setText(mPatientItem.getLastName());
        }

        return rootView;
    }
}
