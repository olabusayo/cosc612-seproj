package com.example.t2cc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class BrowseClassActivity extends AppCompatActivity {

    BrowseClassAdpater adapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browseclass);

        // set data
        List<classData> list = new ArrayList<>();
        list.add(new classData("1", "cosc612", "unsubscribe", "None"));
        list.add(new classData("2", "cosc123", "subscribe", "Done"));
        list.add(new classData("3", "cosc546", "subscribe", "pending"));

        // get recycle view
        recyclerView = (RecyclerView)findViewById(R.id.subscribe2ClassRecycleView);
        adapter = new BrowseClassAdpater(list, getApplication());

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


