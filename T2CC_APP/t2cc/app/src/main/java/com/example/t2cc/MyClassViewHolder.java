package com.example.t2cc;

import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyClassViewHolder extends RecyclerView.ViewHolder {

    TextView myClassNumberLabel;
    TextView myClassNameLabel;
    Switch myClassSubscribeSwitchRow;
    ImageView myClassMessageImage;
    TextView myClassteacherEmailRow;

    public MyClassViewHolder(@NonNull View itemView) {
        super(itemView);

        myClassNameLabel = (TextView)itemView.findViewById(R.id.myClassNameRowLabel);
        myClassNumberLabel = (TextView)itemView.findViewById(R.id.myClassNumberRowLabel);
        myClassSubscribeSwitchRow = (Switch) itemView.findViewById(R.id.myClassSubscribeSwitchRow);
        myClassMessageImage = (ImageView)itemView.findViewById(R.id.myClassMessageImage);
        myClassteacherEmailRow = (TextView)itemView.findViewById(R.id.myClassTeacherEmailRow);

    }
}
