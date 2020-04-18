package com.example.t2cc;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogoutActivity extends AppCompatActivity implements
    View.OnClickListener {

  private static final String TAG = "Logout";
  // Firebase Auth
  private AuthUI mAuthUI;
  private FirebaseAuth mAuth;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);

    //firebase Auth
    mAuth = FirebaseAuth.getInstance();
    mAuthUI = AuthUI.getInstance();

    // prep buttons for onClick
    findViewById(R.id.logOutButton).setOnClickListener(this);
  }

  @Override
  public void onStart() {//check if user is signed in
    super.onStart();
    // Check if user is signed in (non-null) and update UI accordingly.
    FirebaseUser currentUser = mAuth.getCurrentUser();
//    if (currentUser != null) {
//      changeToHomeScreenActivity();
//    }
  }

  @Override
  public void onClick(View v) {
    int i = v.getId();
    if (i == R.id.logOutButton) {
      logOut();
    }
  }

  private void changeToLoginActivity() {
    Intent intent = new Intent(LogoutActivity.this, LoginActivity.class);
    startActivity(intent);
  }

  private void changeToHomeScreenActivity() {
    Intent intent = new Intent(LogoutActivity.this, HomeActivity.class);
    startActivity(intent);
  }

  private void logOut() {
    mAuthUI.signOut(this)
        .addOnCompleteListener(new OnCompleteListener<Void>() {
          public void onComplete(@NonNull Task<Void> task) {
            if (task.isSuccessful()) {
              Log.d(TAG, "signOut:success");
              Toast.makeText(LogoutActivity.this, "Signing Out...",
                  Toast.LENGTH_SHORT).show();
              changeToLoginActivity();
            } else {
              // If sign in fails, display a message to the user.
              Log.w(TAG, "signInWithEmail:failure", task.getException());
              Toast.makeText(LogoutActivity.this, "Signout failed.",
                  Toast.LENGTH_LONG).show();
              changeToHomeScreenActivity();
            }
          }
        });
  }
}

