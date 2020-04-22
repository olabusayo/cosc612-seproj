package com.example.t2cc;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyClassAdapter extends RecyclerView.Adapter<MyClassViewHolder> {
  // for debugging
  private static final String TAG = "MyClassAdpater";

  private List<ClassListInformation> mdata;
  private Context mcontext;

  // constructor
  public MyClassAdapter(List<ClassListInformation> data, Context context) {
    mdata = data;
    mcontext = context;
  }

  @NonNull
  @Override
  public MyClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.myclassrow, parent, false);
    MyClassViewHolder holder = new MyClassViewHolder(view);

    return holder;
  }

  // Bind the data to the row
  @Override
  public void onBindViewHolder(@NonNull MyClassViewHolder holder, final int position) {

    // for debugging
    Log.d(TAG, "BindViewHolder: called.");

    // set the information
    holder.myClassNumberLabel.setText(mdata.get(position).classNumber);
    holder.myClassNameLabel.setText(mdata.get(position).className);
    holder.myClassSubscribeSwitchRow.setChecked(true);
    holder.myClassUnReadMsgLabel.setText((mdata.get(position).unreadMessageCount).toString());

    holder.myClassSubscribeSwitchRow.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        MyClassActivity mcaObject = (MyClassActivity) mdata.get(position).activityObject;
        String classID = mdata.get(position).classID;
        mcaObject.handleUnsubscribeToggle(mcaObject, classID);
      }
    });

  }

  @Override
  public int getItemCount() {

    return mdata.size();
  }

}

