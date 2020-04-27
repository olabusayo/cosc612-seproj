package com.example.t2cc;

import android.content.Context;
import android.content.Intent;
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
    holder.myClassTeacher.setText(mdata.get(position).teacherName);

    holder.myClassSubscribeSwitchRow.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        MyClassActivity mcaObject = (MyClassActivity) mdata.get(position).activityObject;
        String classID = mdata.get(position).classID;
        mcaObject.handleUnsubscribeToggle(mcaObject, classID);
      }
    });

    holder.myClassMessageImage.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        String classID = mdata.get(position).classID;
        String className = mdata.get(position).className;
        changeToMessageActivity(classID, className);
      }
    });
  }

  private void changeToMessageActivity(String classID, String className) {
      Intent intent = new Intent(mcontext, MessageActivity.class);
      intent.putExtra("CLASS_ID", classID);
      intent.putExtra("CLASS_NAME", className);
      intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      mcontext.startActivity(intent);
  }

  @Override
  public int getItemCount() {

    return mdata.size();
  }

}

