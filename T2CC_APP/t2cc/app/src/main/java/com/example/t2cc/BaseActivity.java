package com.example.t2cc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class BaseActivity extends AppCompatActivity{

    private String TAG = "Base Activity:";
    private AuthUI mAuthUI;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // firebase Auth
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
    }

    private void logOut() {
        mAuthUI.signOut(getApplicationContext());
        changeToLoginActivity();
    }

    private void changeToHomeActivity(){
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
    private void changeToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
