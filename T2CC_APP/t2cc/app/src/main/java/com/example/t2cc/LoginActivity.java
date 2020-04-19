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

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements
    View.OnClickListener {

  private final static String TAG = "T2CC:Login";
  // Firebase Auth
  private FirebaseAuth mAuth;
  // Firebase Listener
  private FirebaseAuth.AuthStateListener mAuthListener;
  // Firebase Signout
  private AuthUI mAuthUI;

  // Form Details
  private EditText mEmailField;
  private EditText mPasswordField;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);

    //firebase Auth
    mAuth = FirebaseAuth.getInstance();
    mAuthUI = AuthUI.getInstance();

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

  private void changeToHomeActivity() {
    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
    startActivity(intent);
  }

  private void logIn(final String email, String password) {
    Log.d(TAG, "logIn:" + email);
    FirebaseUser currentUser = mAuth.getCurrentUser();
    if (!validateLoginForm()) {
      return;
    } else if (currentUser != null && currentUser.isEmailVerified()) {
      changeToHomeActivity();
    }

    mAuth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
          @Override
          public void onComplete(@NonNull Task<AuthResult> task) {
            if (task.isSuccessful()) {
              FirebaseUser currentUser = mAuth.getCurrentUser();
              // Sign in success, update UI with the signed-in user's information
              if (currentUser.isEmailVerified()) {
                Log.d(TAG, "signInWithEmail:success");
                changeToHomeActivity();
              } else {
                Log.d(TAG, "userNotVerified");
                mEmailField.setError("Email not verified");
                mEmailField.requestFocus();
                mAuthUI.signOut(LoginActivity.this);
              }
            } else {
              // If sign in fails, display a message to the user
              Exception genException = task.getException();
              Log.w(TAG, "signInWithEmail:failure", genException);
              try {
                throw genException;
              } catch (FirebaseAuthInvalidCredentialsException | FirebaseAuthInvalidUserException e) {
                String m = e.getMessage();
                if (m.contains("email")) {
                  mEmailField.setError(e.getMessage());
                  mEmailField.requestFocus();
                } else {
                  mPasswordField.setError(e.getMessage());
                  mPasswordField.requestFocus();
                }
              } catch (Exception e) {
                Toast.makeText(LoginActivity.this, "Authentication failed.\n" + genException.getMessage(),
                    Toast.LENGTH_LONG).show();
              }
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

