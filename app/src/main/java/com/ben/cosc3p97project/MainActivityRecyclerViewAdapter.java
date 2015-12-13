package com.ben.cosc3p97project;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ben.cosc3p97project.PatientClasses.PatientListActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by VMorsaint on 12/5/2015.
 */
public class MainActivityRecyclerViewAdapter
        extends RecyclerView.Adapter<MainActivityRecyclerViewAdapter.MainActivityViewHolder>
{

    private final List<MainActivityItem> mValues;

    public MainActivityRecyclerViewAdapter()
    {
        mValues = new ArrayList<MainActivityItem>();
        mValues.add(new MainActivityItem(1,"Patients"));
        mValues.add(new MainActivityItem(2,"Appointments"));
        mValues.add(new MainActivityItem(3,"About"));

    }


    @Override
    public MainActivityViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_activity_list_content, parent, false);
        return new MainActivityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MainActivityViewHolder holder, int position)
    {
        holder.mItem = mValues.get(position);
            holder.mContentView.setText(holder.mItem.getLabel());

        holder.mView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                Context context = v.getContext();
                Intent intent;
                switch (holder.mItem.getID())
                {
                    case 1:
                        intent = new Intent(context, PatientListActivity.class);
                        context.startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(context, AppointmentList.class);
                        context.startActivity(intent);
                        break;
                    case 3:
                        intent = new Intent(context, PatientListActivity.class);
                        context.startActivity(intent);
                        break;
                    default:
                }
                //startActivityForResult(new Intent(context, BodyActivity.class), 1);

            }
        });
    }

    @Override
    public int getItemCount()
    {
        return mValues.size();
    }

    public class MainActivityViewHolder extends RecyclerView.ViewHolder
    {
        public final View mView;
        public final TextView mContentView;
        public MainActivityItem mItem;

        public MainActivityViewHolder(View view)
        {
            super(view);
            mView = view;
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString()
        {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
    private class MainActivityItem
    {
        int iID;
        String sLabel;
        MainActivityItem(int iIDParam, String sLabelParam)
        {
            iID=iIDParam;
            sLabel=sLabelParam;
        }
        int getID()
        {
            return iID;
        }
        String getLabel()
        {
            return sLabel;
        }
    }
}
