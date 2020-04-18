package com.example.t2cc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);

    // Buttons
    findViewById(R.id.browseClassesButton).setOnClickListener(this);

  }

  @Override
  public void onClick(View v) {
    int i = v.getId();

    if (i == R.id.browseClassesButton) {
      changeToBrowseActivity();

    }
  }

  private void changeToBrowseActivity() {
    Intent intent = new Intent(HomeActivity.this, BrowseClassActivity.class);
    startActivity(intent);
  }

}
