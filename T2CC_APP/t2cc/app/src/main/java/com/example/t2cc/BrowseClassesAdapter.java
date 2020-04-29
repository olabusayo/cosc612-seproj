package com.example.t2cc;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BrowseClassesAdapter extends RecyclerView.Adapter<BrowseClassesViewHolder> {
  // for debugging
  private static final String TAG = "T2CC:RecyclerViewAdapter";

  private List<ClassListInformation> mdata;
  private Context mcontext;

  // constructor
  public BrowseClassesAdapter(List<ClassListInformation> data, Context context) {
    this.mdata = data;
    this.mcontext = context;
  }

  @NonNull
  @Override
  public BrowseClassesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.browseclassrow, parent, false);
    BrowseClassesViewHolder holder = new BrowseClassesViewHolder(view);

    return holder;
  }

  // Bind the data to the row
  @Override
  public void onBindViewHolder(@NonNull final BrowseClassesViewHolder holder, final int position) {

    // for debugging
    Log.d(TAG, "BindViewHolder: called.");

    // set the information
    holder.classNumberRowLabel.setText(mdata.get(position).classNumber);
    holder.classNameRowLabel.setText(mdata.get(position).className);
    holder.statusLabelRow.setText(mdata.get(position).status);
    holder.subscribeTeacherRowLabel.setText(mdata.get(position).teacherName);

    if (mdata.get(position).status.equals("pending")) {
      holder.subscribeSwitchRow.setChecked(true);
      holder.subscribeSwitchRow.setEnabled(false);
    } else {
      holder.subscribeSwitchRow.setChecked(false);
      holder.subscribeSwitchRow.setEnabled(true);
    }

    holder.subscribeSwitchRow.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        BrowseClassesActivity bcaObject = (BrowseClassesActivity) mdata.get(position).activityObject;
        String classID = mdata.get(position).classID;
        bcaObject.handleSubscriptionToggle(bcaObject, classID, holder.subscribeSwitchRow, holder.statusLabelRow);
      }
    });

  }

  @Override
  public int getItemCount() {

    return mdata.size();
  }

}
