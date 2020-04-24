package com.example.t2cc;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageViewHolder> {
    // for debugging
    private static final String TAG = "MessageAdapter: ";

    private List<messageData> mdata;
    private Context mcontext;

    // constructor
    public MessageAdapter (List<messageData> data, Context context){
        mdata = data;
        mcontext = context;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.messagerow, parent, false);
        MessageViewHolder holder  = new MessageViewHolder(view);

        return holder;
    }

    // Bind the data to the row
    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {

        // for debugging
        Log.d(TAG, "BindViewHolder: called.");

        // set the information
        holder.classNameLabel.setText(mdata.get(position).classNameLabel);
        holder.dateLabel.setText(mdata.get(position).dateLabel);
        holder.messageTextField.setText(mdata.get(position).messageTextField);

    }

    @Override
    public int getItemCount() {

        return mdata.size();
    }

}