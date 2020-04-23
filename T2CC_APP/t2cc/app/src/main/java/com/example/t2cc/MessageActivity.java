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
        list.add(new messageData("message #1", "COSC602-02", "April 10",
                "This is an example message from professor 1"));

        list.add(new messageData("message #2", "COSC602-01", "November 20",
                "This is an example message from professor 2"));

        // get recycle view
        recyclerView = (RecyclerView)findViewById(R.id.messageRecycleView);
        adapter = new MessageAdapter(list, getApplication());

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MessageActivity.this));
    }
}

// define data
class messageData{
    String messageNumberLabel;
    String classNumberLabel;
    String dateLabel;
    String messageTextField;

    messageData(String messageNumberLabel, String classNumberLabel, String dateLabel, String messageTextField){
        this.messageNumberLabel = messageNumberLabel;
        this.classNumberLabel = classNumberLabel;
        this.dateLabel = dateLabel;
        this.messageTextField = messageTextField;
    }
}
