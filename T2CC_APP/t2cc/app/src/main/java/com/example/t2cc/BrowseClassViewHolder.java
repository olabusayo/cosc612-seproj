package com.example.t2cc;

import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BrowseClassViewHolder extends RecyclerView.ViewHolder {

    TextView classNumberRowLabel;
    TextView classNameRowLabel;
    TextView subUnsubLabel;
    TextView statusLabelRow;
    Switch subscribeSwitchRow;

    public BrowseClassViewHolder(@NonNull View itemView) {
        super(itemView);

        classNameRowLabel = (TextView)itemView.findViewById(R.id.classNameRowLabel);
        classNumberRowLabel = (TextView)itemView.findViewById(R.id.classNumberRowLabel);
        subUnsubLabel = (TextView)itemView.findViewById(R.id.subUnsubLabel);
        statusLabelRow = (TextView)itemView.findViewById(R.id.statusLabelRow);
        subscribeSwitchRow = (Switch)itemView.findViewById(R.id.subscribeSwitchRow);
    }
}
