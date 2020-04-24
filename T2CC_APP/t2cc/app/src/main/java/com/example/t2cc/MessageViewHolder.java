package com.example.t2cc;

import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MessageViewHolder extends RecyclerView.ViewHolder {

    TextView messageNumberLabel;
    TextView classNumberLabel;
    TextView dateLabel;
    TextView messageTextField;

    public MessageViewHolder(@NonNull View itemView) {
        super(itemView);

        messageNumberLabel = (TextView)itemView.findViewById(R.id.messageNumberLabel);
        classNumberLabel = (TextView)itemView.findViewById(R.id.classNumberLabel);
        dateLabel = (TextView)itemView.findViewById(R.id.dateLabel);
        messageTextField = (TextView)itemView.findViewById(R.id.messageTextField);
    }
}