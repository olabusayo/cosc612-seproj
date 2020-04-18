package com.example.t2cc;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
  private final static String TAG = "T2CC:Home";
  // Firebase Auth
  private AuthUI mAuthUI;
  private FirebaseAuth mAuth;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);

    // firebase Auth
    mAuth = FirebaseAuth.getInstance();
    mAuthUI = AuthUI.getInstance();

    // prep buttons for onClick
    findViewById(R.id.logoutImage).setOnClickListener(this);
    findViewById(R.id.homeLogo).setOnClickListener(this);
    findViewById(R.id.browseClassesButton).setOnClickListener(this);
  }

  @Override
  public void onStart() {//check if user is signed in
    super.onStart();
    // Check if user is signed in (non-null) and update UI accordingly.
    FirebaseUser currentUser = mAuth.getCurrentUser();
    if (currentUser != null) {
      currentUser.reload();
      if (!currentUser.isEmailVerified()) {
        Log.w(TAG, "On Home Screen without Verification");
        Toast.makeText(HomeActivity.this, "Email not verified.",
            Toast.LENGTH_LONG).show();
        logOut();
      }
    } else {
      changeToLoginActivity();
    }
  }

  @Override
  public void onClick(View v) {
    int i = v.getId();
    if (i == R.id.browseClassesButton) {
      changeToBrowseActivity();
    } else if (i == R.id.logoutImage) {
      Toast.makeText(HomeActivity.this, "Goodbye.",
          Toast.LENGTH_SHORT).show();
      logOut();
    }
  }

  private void changeToBrowseActivity() {
    Intent intent = new Intent(HomeActivity.this, BrowseClassActivity.class);
    startActivity(intent);
  }

  private void changeToLoginActivity() {
    Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
    startActivity(intent);
  }

  private void logOut() {
    mAuthUI.signOut(this);
    changeToLoginActivity();
  }
}
