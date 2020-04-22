package com.example.t2cc;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class BrowseClassActivity extends BaseActivity {

    BrowseClassAdapter adapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browseclass);

        // set data
        List<classData> list = new ArrayList<>();
        list.add(new classData("cosc602-02", "123456789012345678901234567890123456789012345678901234567890", "unsubscribe", "None"));
        list.add(new classData("cosc606-01", "1234567890123456789012345678901234567890", "subscribe", "Done"));
        list.add(new classData("cosc602-01", "123456789012345678901234567890", "subscribe", "pending"));
        list.add(new classData("cosc602-01", "12345678901234567890", "subscribe", "pending"));
        list.add(new classData("cosc602-01", "1234567890", "subscribe", "pending"));
        // get recycle view
        recyclerView = (RecyclerView)findViewById(R.id.subscribe2ClassRecycleView);
        adapter = new BrowseClassAdapter(list, getApplication());

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(BrowseClassActivity.this));
    }
}

// define data
class classData{
    String className;
    String classNumber;
    String subscription;
    String status;

    classData(String classNumber, String className, String subscription, String status){
        this.className = className;
        this.classNumber = classNumber;
        this.subscription = subscription;
        this.status = status;
    }
}


