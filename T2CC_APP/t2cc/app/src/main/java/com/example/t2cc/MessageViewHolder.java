package com.example.t2cc;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MessageViewHolder extends RecyclerView.ViewHolder {

    TextView classNameLabel;
    TextView dateLabel;
    TextView messageTextField;

    public MessageViewHolder(@NonNull View itemView) {
        super(itemView);

        classNameLabel = (TextView)itemView.findViewById(R.id.classNameLabel);
        dateLabel = (TextView)itemView.findViewById(R.id.dateLabel);
        messageTextField = (TextView)itemView.findViewById(R.id.messageTextField);
    }
}