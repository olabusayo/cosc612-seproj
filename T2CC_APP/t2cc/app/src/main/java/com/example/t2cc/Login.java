package com.example.t2cc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Login extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final Button registration_button = findViewById(R.id.registrationLoginButton);
        final Button login_button = findViewById(R.id.loginButton);

        //login
        login_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // login logic
            }
        });

        // Registration
        registration_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                change_activity();
            }
        });

    }

    private void change_activity()
    {
        Intent intent = new Intent(Login.this, Registration.class);
        startActivity(intent);

    }
}

