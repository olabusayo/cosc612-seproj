package com.example.t2cc;

import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyClassViewHolder extends RecyclerView.ViewHolder {

    TextView myClassNumberLabel;
    TextView myClassNameLabel;
    TextView myClassUnReadMsgLabel;

    public MyClassViewHolder(@NonNull View itemView) {
        super(itemView);

        myClassNameLabel = (TextView)itemView.findViewById(R.id.myClassNameRowLabel);
        myClassNumberLabel = (TextView)itemView.findViewById(R.id.myClassNumberRowLabel);
        myClassUnReadMsgLabel = (TextView)itemView.findViewById(R.id.unreadMessageRowLabel);

    }
}
