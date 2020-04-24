package com.example.t2cc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MessageActivity extends BaseActivity {

    MessageAdapter adapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        // set data
        List<messageData> list = new ArrayList<>();
        list.add(new messageData("12345678901234567890123456789012345678901234567890", "November 22",
                "This is an example message from professor 1"));

        list.add(new messageData("1234567890123456789012345678901234567890", "September 22",
                "This is an example message from professor 2"));;

        list.add(new messageData("12345678901234567890", "September 29",
                "This is an example message from professor 3"));;

        // get recycle view
        recyclerView = (RecyclerView)findViewById(R.id.messageRecycleView);
        adapter = new MessageAdapter(list, getApplication());

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MessageActivity.this));
    }
}

// define data
class messageData{
    String classNameLabel;
    String dateLabel;
    String messageTextField;

    messageData(String classNameLabel, String dateLabel, String messageTextField){
        this.classNameLabel = classNameLabel;
        this.dateLabel = dateLabel;
        this.messageTextField = messageTextField;
    }
}
