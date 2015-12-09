package com.ben.cosc3p97project.PatientClasses;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ben.cosc3p97project.DatabaseClasses.PatientFile;
import com.ben.cosc3p97project.R;

import java.util.List;

/**
 * Created by VMorsaint on 12/5/2015.
 */
public class PatientFileRecyclerViewAdapter
        extends RecyclerView.Adapter<PatientFileRecyclerViewAdapter.PatientFileViewHolder>
{
    private LayoutInflater inflaterPatientFile;
    private final List<PatientFile> mValues;

    public PatientFileRecyclerViewAdapter(List<PatientFile> items)
    {
        mValues = items;
    }

    @Override
    public PatientFileViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
          View view = LayoutInflater.from(parent.getContext())
                  .inflate(R.layout.patient_files_list_content, parent, false);
        return new PatientFileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PatientFileViewHolder holder, int position)
    {
        long iPatientFileId;
        holder.mItem = mValues.get(position);
        iPatientFileId = holder.mItem.getPatientFileID();
        if (iPatientFileId == 0)
        {
            holder.mIdView.setText("+");
        }
        else
        {
            holder.mIdView.setText(String.valueOf(iPatientFileId));
        }
        holder.mContentView.setText(holder.mItem.getName());

        holder.mView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Context context = v.getContext();
                Intent intent = new Intent(context, PatientFileDetailActivity.class);
                intent.putExtra(PatientFileDetailActivity.ARG_ITEM_ID, String.valueOf(holder.mItem.getPatientFileID()));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount()
    {
        return mValues.size();
    }

    public class PatientFileViewHolder extends RecyclerView.ViewHolder
    {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public PatientFile mItem;

        public PatientFileViewHolder(View view)
        {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.txtViewPatientFileId);
            mContentView = (TextView) view.findViewById(R.id.txtViewPatientFileContent);
        }

        @Override
        public String toString()
        {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
