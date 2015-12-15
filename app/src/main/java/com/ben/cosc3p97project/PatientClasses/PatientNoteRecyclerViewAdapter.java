package com.ben.cosc3p97project.PatientClasses;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ben.cosc3p97project.DatabaseClasses.PatientNote;
import com.ben.cosc3p97project.R;

import java.util.List;

/**
 * Created by VMorsaint on 12/5/2015.
 */
public class PatientNoteRecyclerViewAdapter
        extends RecyclerView.Adapter<PatientNoteRecyclerViewAdapter.PatientNoteViewHolder>
{
    private LayoutInflater inflaterPatientNote;
    private final List<PatientNote> mValues;
    public PatientNoteRecyclerViewAdapter(List<PatientNote> items)
    {
        mValues = items;
    }

    @Override
    public PatientNoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
          View view = LayoutInflater.from(parent.getContext())
                  .inflate(R.layout.patient_notes_list_content, parent, false);
        return new PatientNoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PatientNoteViewHolder holder, int position)
    {
        long iPatientNoteId;
        holder.mItem = mValues.get(position);
        iPatientNoteId = holder.mItem.getPatientNoteID();
        if (iPatientNoteId == 0)
        {
            holder.mIdView.setText("+");
        }
        else
        {
            holder.mIdView.setText(String.valueOf(position+1));
        }
        holder.mContentView.setText( holder.mItem.getNote());

        holder.mView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Context context = v.getContext();
                Intent intent = new Intent(context, PatientNoteDetailActivity.class);
                intent.putExtra(PatientNoteDetailActivity.ARG_PATIENT_NOTE_ID, String.valueOf(holder.mItem.getPatientNoteID()));
                intent.putExtra(PatientNoteDetailActivity.ARG_PATIENT_FILE_ID, String.valueOf(holder.mItem.getPatientFileID()));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount()
    {
        return mValues.size();
    }

    public class PatientNoteViewHolder extends RecyclerView.ViewHolder
    {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public PatientNote mItem;

        public PatientNoteViewHolder(View view)
        {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.txtViewPatientNoteId);
            mContentView = (TextView) view.findViewById(R.id.txtViewPatientNoteContent);
        }

        @Override
        public String toString()
        {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
