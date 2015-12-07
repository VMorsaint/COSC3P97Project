package com.ben.cosc3p97project.PatientClasses.OldClasses;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ben.cosc3p97project.DatabaseClasses.DBHelper;
import com.ben.cosc3p97project.DatabaseClasses.PatientFile;
import com.ben.cosc3p97project.DatabaseClasses.PatientNote;
import com.ben.cosc3p97project.R;

import java.util.ArrayList;

public class PatientFileDetailFragment extends Fragment
{
    public static final String ARG_ITEM_ID = "item_id";
    private String sPatientFileID;
    private PatientFile mPatientFileItem;
    private ArrayList<PatientNote> mPatientNoteList;
    private RecyclerView recyclerViewPatientNotes;
    private DBHelper dbHelperPatientFileDetail;
    public PatientFileDetailFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID))
        {
            dbHelperPatientFileDetail = new DBHelper(getActivity());
            sPatientFileID = getArguments().getString(ARG_ITEM_ID);
            mPatientFileItem = dbHelperPatientFileDetail.getPatientFile(sPatientFileID);

            dbHelperPatientFileDetail.addPatientNote(new PatientNote(0, Integer.parseInt(sPatientFileID),
                    "test note"));
            //mPatientNoteList = dbHelperPatientFileDetail.getPatientNoteListByPatientFileId(sPatientFileID);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.patient_detail, container, false);
        if (mPatientFileItem != null)
        {
    //          ((TextView) rootView.findViewById(R.id.textViewPatientFirstNameEdit)).setText(mPatientFileItem.getNote());
    //          recyclerViewPatientNotess = (RecyclerView) rootView.findViewById(R.id.listView_patientFile_items);
    //          assert recyclerViewPatientNotess != null;
                //dbHelperPatientList.addPatient(new Patient(0, "", "Vincent2", "Morsaint2"));
    //          recyclerViewPatientNotess.setAdapter(new PatientFileRecyclerViewAdapter(mPatientFileList));

        }

        return rootView;
    }
}
