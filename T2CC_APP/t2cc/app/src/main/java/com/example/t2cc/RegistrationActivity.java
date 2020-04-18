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
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegistrationActivity extends AppCompatActivity implements
    View.OnClickListener {

  private static final String TAG = "Registration";
  // Firebase Auth
  private FirebaseAuth mAuth;
  // Form Details
  private EditText mFirstNameField;
  private EditText mLastNameField;
  private EditText mEmailField;
  private EditText mPasswordField;
  private EditText mPasswordConfirmationField;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_registration);

    // registration info
    mFirstNameField = findViewById(R.id.firstNameRegField);
    mLastNameField = findViewById(R.id.lastNameRegField);
    mEmailField = findViewById(R.id.emailRegField);
    mPasswordField = findViewById(R.id.passwordRegField);
    mPasswordConfirmationField = findViewById(R.id.confirmPasswordRegField);

    // Buttons
    findViewById(R.id.registrationButton).setOnClickListener(this);

    //firebase Auth
    mAuth = FirebaseAuth.getInstance();

  }

  @Override
  public void onStart() {//check if user is signed in
    super.onStart();
    // Check if user is signed in (non-null) and update UI accordingly.
    FirebaseUser currentUser = mAuth.getCurrentUser();
    //updateUI(currentUser);
  }

  @Override
  public void onClick(View v) {
    int i = v.getId();
    if (i == R.id.registrationButton) {
      registerUser(mFirstNameField.getText().toString(), mLastNameField.getText().toString(),
          mEmailField.getText().toString(), mPasswordField.getText().toString());
    }
  }

  private void changeToLoginActivity() {
    Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
    startActivity(intent);
  }

  private void changeToHomeScreenActivity() {
    Intent intent = new Intent(RegistrationActivity.this, HomeActivity.class);
    startActivity(intent);
  }

  private void registerUser(String fName, String lName, String email, String password) {
    Log.d(TAG, "register:" + email);
    if (!validateRegistrationForm()) {
      return;
    }

    final String userFullName = String.format("%s %s", fName, lName);

    mAuth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
          @Override
          public void onComplete(@NonNull Task<AuthResult> task) {
            if (task.isSuccessful()) {
              // Sign in success, update UI with the signed-in user's information
              Log.d(TAG, "createUserWithEmail:success");
              FirebaseUser user = mAuth.getCurrentUser();
              // populate profile with name info
              updateUserProfile(user, userFullName);
              // do email verification
              sendEmailVerification();
              changeToHomeScreenActivity();
            } else {
              // If sign in fails, display a message to the user.
              Log.w(TAG, "createUserWithEmail:failure", task.getException());
              Toast.makeText(RegistrationActivity.this, "Authentication failed.",
                  Toast.LENGTH_SHORT).show();
              updateUI(null);
            }
          }
        });
  }

  private boolean validateRegistrationForm() {
    boolean valid = true;

    String firstName = mFirstNameField.getText().toString();
    if (TextUtils.isEmpty(firstName)) {
      mFirstNameField.setError("Required.");
      valid = false;
    } else {
      mFirstNameField.setError(null);
    }

    String lastName = mLastNameField.getText().toString();
    if (TextUtils.isEmpty(firstName)) {
      mLastNameField.setError("Required.");
      valid = false;
    } else {
      mLastNameField.setError(null);
    }

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

    String passwordConfirm = mPasswordConfirmationField.getText().toString();
    if (TextUtils.isEmpty(passwordConfirm)) {
      mPasswordConfirmationField.setError("Required.");
      valid = false;
    } else if (!password.equals(passwordConfirm)) {
      mPasswordConfirmationField.setError("Passwords must match.");
      valid = false;
    } else {
      mPasswordField.setError(null);
    }

    return valid;
  }

  private void updateUserProfile(FirebaseUser user, String userName) {
    if (user != null) {
      UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
          .setDisplayName(userName)
          .build();

      user.updateProfile(profileUpdates)
          .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
              if (task.isSuccessful()) {
                Log.d(TAG, "User profile updated.");
              }
            }
          });
    }
  }

  private void sendEmailVerification() {
    final FirebaseUser user = mAuth.getCurrentUser();
    user.sendEmailVerification()
        .addOnCompleteListener(this, new OnCompleteListener<Void>() {
          @Override
          public void onComplete(@NonNull Task<Void> task) {
            if (task.isSuccessful()) {
              Toast.makeText(RegistrationActivity.this,
                  "Verification email sent to " + user.getEmail(),
                  Toast.LENGTH_SHORT).show();
              changeToLoginActivity();
            } else {
              Log.e(TAG, "sendEmailVerification", task.getException());
              Toast.makeText(RegistrationActivity.this,
                  "Failed to send verification email.",
                  Toast.LENGTH_SHORT).show();
            }
          }
        });
  }

  private void updateUI(FirebaseUser user) {
    // do nothing for now
  }

}
