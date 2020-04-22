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

public class BaseActivity extends AppCompatActivity {

  private String TAG = "T2CC:BaseActivity:";
  FirebaseFirestore mFBDB;
  FirebaseAuth mAuth;
  String mCurrentUserID;
  private AuthUI mAuthUI;
  FirebaseUser mCurrentUser;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // firebase Auth
    mFBDB = FirebaseFirestore.getInstance();
    mAuth = FirebaseAuth.getInstance();
    mAuthUI = AuthUI.getInstance();
    mCurrentUser = mAuth.getCurrentUser();
    mCurrentUserID = mCurrentUser.getUid();
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

    findViewById(R.id.messagesImage).setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        changeToMessageActivity();
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

  private void changeToMessageActivity(){
    Intent intent = new Intent(this, MessageActivity.class);
    startActivity(intent);
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

// define class list information data with some enriched data
class ClassListInformation {
  String className;
  String classNumber;
  Boolean subscription;
  String status;
  String classID;
  BaseActivity activityObject;
  Integer unreadMessageCount;

  ClassListInformation(BaseActivity activityObject, String classID,
      String className, String classNum, Boolean subscriptionStatus,
      String requestStatus) {
    this.className = className;
    this.classNumber = classNum;
    this.subscription = subscriptionStatus;
    this.status = requestStatus;
    this.classID = classID;
    this.activityObject = activityObject;
  }

  public ClassListInformation(String classID,
      String className, String classNum, Integer unReadMessages) {

    this.className = className;
    this.classNumber = classNum;
    this.unreadMessageCount = unReadMessages;
    this.classID = classID;
  }
}