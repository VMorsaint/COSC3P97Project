package com.ben.cosc3p97project.PatientClasses.OldClasses;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ben.cosc3p97project.DatabaseClasses.DBHelper;
import com.ben.cosc3p97project.DatabaseClasses.Patient;
import com.ben.cosc3p97project.DatabaseClasses.PatientFile;
import com.ben.cosc3p97project.PatientClasses.PatientFileRecyclerViewAdapter;
import com.ben.cosc3p97project.R;

import java.util.ArrayList;

public class PatientDetailFragment extends Fragment
{
    public static final String ARG_ITEM_ID = "item_id";
    private String sPatientID;
    private Patient mPatientItem;
    private ArrayList<PatientFile> mPatientFileList;
    private RecyclerView recyclerViewPatientFiles;
    private DBHelper dbHelperPatientDetail;
    private PatientFileRecyclerViewAdapter myTestAdapter;
    private LinearLayoutManager myTestLayoutManager;


    public PatientDetailFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID))
        {
            dbHelperPatientDetail = new DBHelper(getActivity());
            sPatientID = getArguments().getString(ARG_ITEM_ID);
            mPatientItem = dbHelperPatientDetail.getPatient(sPatientID);
            mPatientFileList = dbHelperPatientDetail.getPatientFileListByPatientId(sPatientID);
/*
            Calendar cDateStart = Calendar.getInstance();
            SimpleDateFormat fSQLDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            dbHelperPatientDetail.addPatientFile(new PatientFile(0, Integer.parseInt(sPatientID),
                    "testFile",fSQLDate.format(cDateStart.getTime()),""));
                    */
  //          recyclerViewPatientFiles = (RecyclerView) getView().findViewById(R.id.listView_patientFile_items);
            myTestAdapter = new PatientFileRecyclerViewAdapter(mPatientFileList);

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
            myTestLayoutManager = new LinearLayoutManager(getActivity());
            myTestLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerViewPatientFiles.setLayoutManager(myTestLayoutManager);
            recyclerViewPatientFiles.setAdapter(myTestAdapter);
            recyclerViewPatientFiles.setVisibility(View.VISIBLE);
            ((TextView) rootView.findViewById(R.id.textViewPatientFirstNameEdit)).setText(mPatientItem.getFirstName());
            ((TextView) rootView.findViewById(R.id.textViewPatientLastNameEdit)).setText(mPatientItem.getLastName());
        }

        return rootView;
    }
}
