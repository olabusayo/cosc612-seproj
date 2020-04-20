package com.example.t2cc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MyClassActivity extends AppCompatActivity {

    MyClassAdapter adapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myclass);

        // set data
        List<myClassData> list = new ArrayList<>();
        list.add(new myClassData("cosc602-02", "123456789012345678901234567890123456789012345678901234567890", "2"));

        // get recycle view
        recyclerView = (RecyclerView)findViewById(R.id.myClassRecycleView);
        adapter = new MyClassAdapter(list, getApplication());

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MyClassActivity.this));
    }
}


class myClassData{
    String className;
    String classNumber;
    String unReadMsg;

    myClassData(String classNumber, String className, String unReadMsg){
        this.className = className;
        this.classNumber = classNumber;
        this.unReadMsg = unReadMsg;
    }
}