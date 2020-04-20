package com.example.t2cc;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BrowseClassAdpater extends RecyclerView.Adapter<BrowseClassViewHolder> {
    // for debugging
    private static final String TAG = "RecyclerViewAdpater";

    private List<classData> mdata;
    private Context mcontext;

    // constructor
    public BrowseClassAdpater(List<classData> data, Context context){
        mdata = data;
        mcontext = context;
    }

    @NonNull
    @Override
    public BrowseClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.browseclassrow, parent, false);
        BrowseClassViewHolder holder  = new BrowseClassViewHolder(view);

        return holder;
    }

    // Bind the data to the row
    @Override
    public void onBindViewHolder(@NonNull BrowseClassViewHolder holder, int position) {

        // for debugging
        Log.d(TAG, "BindViewHolder: called.");

        // set the information
        holder.classNumberRowLabel.setText(mdata.get(position).classNumber);
        holder.classNameRowLabel.setText(mdata.get(position).className);
        holder.statusLabelRow.setText(mdata.get(position).status);

        if (mdata.get(position).subscription == "subscribe")
        {
            holder.subscribeSwitchRow.setChecked(true);
        }
        else {
            holder.subscribeSwitchRow.setChecked(false);
        }
    }

    @Override
    public int getItemCount() {

        return mdata.size();
    }

}
