package com.example.t2cc;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class HomeActivity extends BaseActivity implements View.OnClickListener {
  private final static String TAG = "T2CC:Home";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);

    // prep buttons for onClick
    findViewById(R.id.browseClassesButton).setOnClickListener(this);
    findViewById(R.id.myClassButton).setOnClickListener(this);

  }

  @Override
  public void onClick(View v) {
    int i = v.getId();

    if (i == R.id.browseClassesButton) {
      changeToBrowseActivity();
    } else if (i == R.id.myClassButton) {
      changeToMyClassActivity();
    }

  }

  private void changeToMyClassActivity() {
    Intent intent = new Intent(HomeActivity.this, MyClassActivity.class);
    startActivity(intent);
  }

  private void changeToBrowseActivity() {
    Intent intent = new Intent(HomeActivity.this, BrowseClassesActivity.class);
    startActivity(intent);
  }

}
