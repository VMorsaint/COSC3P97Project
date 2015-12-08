package com.ben.cosc3p97project.PatientClasses;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ben.cosc3p97project.DatabaseClasses.Patient;
import com.ben.cosc3p97project.R;

import java.util.List;

/**
 * Created by VMorsaint on 12/5/2015.
 */
public class PatientRecyclerViewAdapter
        extends RecyclerView.Adapter<PatientRecyclerViewAdapter.PatientViewHolder>
{

    private final List<Patient> mValues;

    public PatientRecyclerViewAdapter(List<Patient> items)
    {
        mValues = items;
    }

    @Override
    public PatientViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.patient_list_content, parent, false);
        return new PatientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PatientViewHolder holder, int position)
    {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(String.valueOf(holder.mItem.getPatientID()));
        holder.mContentView.setText(holder.mItem.getFirstName());

        holder.mView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                Context context = v.getContext();
                Intent intent = new Intent(context, PatientDetailActivity.class);
                intent.putExtra(PatientDetailActivity.ARG_ITEM_ID, String.valueOf(holder.mItem.getPatientID()));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return mValues.size();
    }

    public class PatientViewHolder extends RecyclerView.ViewHolder
    {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public Patient mItem;

        public PatientViewHolder(View view)
        {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString()
        {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
