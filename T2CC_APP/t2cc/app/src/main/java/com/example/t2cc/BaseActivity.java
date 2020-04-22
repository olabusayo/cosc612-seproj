package com.example.t2cc;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

public class BaseActivity extends AppCompatActivity {

  FirebaseFirestore mFBDB;
  private String TAG = "Base Activity:";
  private FirebaseAuth mAuth;
  private AuthUI mAuthUI;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // firebase Auth
    mFBDB = FirebaseFirestore.getInstance();
    mAuth = FirebaseAuth.getInstance();
    mAuthUI = AuthUI.getInstance();
  }

  public void onStart() {//check if user is signed in
    super.onStart();
    // Check if user is signed in (non-null) and update UI accordingly.
    FirebaseUser currentUser = mAuth.getCurrentUser();
    if (currentUser != null) {
      currentUser.reload();
      if (!currentUser.isEmailVerified()) {
        Log.w(TAG, "On Home Screen without Verification");
        Toast.makeText(getApplicationContext(), "Email not verified.",
            Toast.LENGTH_LONG).show();
        logOut();
      } else {
        setStudentsInitials(currentUser);
      }
    } else {
      changeToLoginActivity();
    }
  }

  @Override
  public void setContentView(int layoutResID) {
    super.setContentView(layoutResID);

    // initialize tool bar
    Toolbar myToolbar = findViewById(R.id.appBar);
    setSupportActionBar(myToolbar);

    findViewById(R.id.logoutImage).setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        Toast.makeText(getApplicationContext(), "Goodbye.",
            Toast.LENGTH_SHORT).show();
        logOut();
      }
    });

    findViewById(R.id.homeLogo).setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        changeToHomeActivity();
      }
    });

    findViewById(R.id.userInitial).setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Toast toast = Toast.makeText(BaseActivity.this, currentUser.getEmail(), Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP | Gravity.RIGHT, 0, 200);
        toast.show();
      }
    });
  }

  private void setStudentsInitials(FirebaseUser currentUser) {
    Log.d(TAG, "studentInitialsSet:success");
    String[] userName = currentUser.getDisplayName().toUpperCase().split(" ");
    String initials = String.format("%c.%c.", userName[0].charAt(0), userName[1].charAt(0));
    ((TextView) findViewById(R.id.userInitial)).setText(initials);
  }

  private void logOut() {
    mAuthUI.signOut(getApplicationContext());
    changeToLoginActivity();
  }

  private void changeToHomeActivity() {
    Intent intent = new Intent(this, HomeActivity.class);
    startActivity(intent);
  }

  private void changeToLoginActivity() {
    Intent intent = new Intent(this, LoginActivity.class);
    startActivity(intent);
  }
}
