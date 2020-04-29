package com.example.t2cc;

import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BrowseClassesViewHolder extends RecyclerView.ViewHolder {

  TextView classNumberRowLabel;
  TextView classNameRowLabel;
  TextView statusLabelRow;
  Switch subscribeSwitchRow;
  TextView subscribeTeacherRowLabel;

  public BrowseClassesViewHolder(@NonNull View itemView) {
    super(itemView);

    classNameRowLabel = (TextView) itemView.findViewById(R.id.classNameRowLabel);
    classNumberRowLabel = (TextView) itemView.findViewById(R.id.classNumberRowLabel);
    statusLabelRow = (TextView) itemView.findViewById(R.id.statusLabelRow);
    subscribeSwitchRow = (Switch) itemView.findViewById(R.id.subscribeSwitchRow);
    subscribeTeacherRowLabel = (TextView)itemView.findViewById(R.id.subscribeTeacherRowLabel);
  }
}
