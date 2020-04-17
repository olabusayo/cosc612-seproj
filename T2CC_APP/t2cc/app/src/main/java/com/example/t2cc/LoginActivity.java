package com.example.t2cc;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements
    View.OnClickListener {

  private static final String TAG = "Login";
  // Firebase Auth
  private FirebaseAuth mAuth;

  // Form Details
  private EditText mEmailField;
  private EditText mPasswordField;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);

    //firebase Auth
    mAuth = FirebaseAuth.getInstance();

    // login info
    mEmailField = findViewById(R.id.usernameLoginField);
    mPasswordField = findViewById(R.id.passwordLoginField);

    // prep buttons for onClick
    findViewById(R.id.registrationLoginButton).setOnClickListener(this);
    findViewById(R.id.loginButton).setOnClickListener(this);
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
    if (i == R.id.loginButton) {
      logIn(mEmailField.getText().toString(), mPasswordField.getText().toString());
    } else if (i == R.id.registrationLoginButton) {
      changeToRegistrationActivity();
    }
  }

  private void changeToRegistrationActivity() {
    Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
    startActivity(intent);
  }

  private void changeToHomeScreenActivity() {
    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
    startActivity(intent);
  }

  private void logIn(String email, String password) {
    Log.d(TAG, "logIn:" + email);
    if (!validateLoginForm()) {
      return;
    }

    mAuth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
          @Override
          public void onComplete(@NonNull Task<AuthResult> task) {
            if (task.isSuccessful()) {
              // Sign in success, update UI with the signed-in user's information
              Log.d(TAG, "signInWithEmail:success");
              FirebaseUser user = mAuth.getCurrentUser();
              changeToHomeScreenActivity();
            } else {
              // If sign in fails, display a message to the user.
              Log.w(TAG, "signInWithEmail:failure", task.getException());
              Toast.makeText(LoginActivity.this, "Authentication failed.",
                  Toast.LENGTH_SHORT).show();
              updateUI(null);
            }
          }
        });
  }

  private boolean validateLoginForm() {
    boolean valid = true;

    String email = mEmailField.getText().toString();
    if (TextUtils.isEmpty(email)) {
      mEmailField.setError("Required.");
      valid = false;
    } else {
      mEmailField.setError(null);
    }

    String password = mPasswordField.getText().toString();
    if (TextUtils.isEmpty(password)) {
      mPasswordField.setError("Required.");
      valid = false;
    } else {
      mPasswordField.setError(null);
    }
    return valid;
  }

  private void updateUI(FirebaseUser user) {
    // do nothing for now
  }
}

