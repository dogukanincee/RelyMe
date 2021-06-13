package com.example.safeShibaDogeMoonInu;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    EditText userPassword, userPassword2;      //User Email, Password, Name fields
    UserLocalStore userLocalStore;
    private Button regBtn, loginButton;                             //Login and register buttons
    //If all fields are filled, then register button enables
    private final TextWatcher registerTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //Initialize fields
            String passwordInput = userPassword.getText().toString().trim();
            String password2Input = userPassword2.getText().toString().trim();
            //Make register button clickable if those parameters are not empty
            regBtn.setEnabled(!passwordInput.isEmpty() && !password2Input.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    private long backPressedTime; //System's current time in milliseconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userPassword = findViewById(R.id.regPassword);
        userPassword2 = findViewById(R.id.regPassword2);
        regBtn = findViewById(R.id.regBtn);
        loginButton = findViewById(R.id.loginButton);

        userPassword.addTextChangedListener(registerTextWatcher);
        userPassword2.addTextChangedListener(registerTextWatcher);

        //If register button is clicked, creates user account
        regBtn.setOnClickListener(view -> {

            regBtn.setVisibility(View.INVISIBLE); //Hides register button

            //Creates toString methods of given fields
            final String password = userPassword.getText().toString();
            final String password2 = userPassword2.getText().toString();


            //Gives error if written passwords are not equal to each other
            if (!password.equals(password2)) {
                userPassword.setError("Passwords Must Be Same");
                userPassword2.setError("Passwords Must Be Same");
                regBtn.setVisibility(View.VISIBLE); //Shows register button
            }
            //Gives error if written passwords are smaller than 6 characters
            if (password.length() < 6 || password2.length() < 6) {
                userPassword.setError("Passwords Must Be At Least 6 Characters");
                userPassword2.setError("Passwords Must Be At Least 6 Characters");
                regBtn.setVisibility(View.VISIBLE); //Shows register button
            }
            //CreateUserAccount method runs if all bars are filled and written passwords are equal to each other and not smaller than 6 characters
            if (password.equals(password2) && !(password.length() < 6) && !(password2.length() < 6)) {
                regBtn.setVisibility(View.VISIBLE); //Shows register button
                CreateUserAccount(password);   //Create Account method runs and creates user
            }
        });

        //If login button is clicked, Login Activity starts
        loginButton.setOnClickListener(v -> {
            Intent loginActivity = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(loginActivity);
            finish();
        });

    }

    //Creates user account and starts Login Activity
    private void CreateUserAccount(String password) {
        User registeredUser = new User(password);
        Intent loginActivity = new Intent(getApplicationContext(), LoginActivity.class);
        loginActivity.putExtra("password", registeredUser.getPassword());
        startActivity(loginActivity);
        finish();
    }

    //Method to show a toast message
    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    //If user tries to go back, those operations start
    @Override
    public void onBackPressed() {
        //If User presses backPress again in 2 seconds, application will be closed
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            return;
        }
        //If User presses backPress again after 2 seconds, application will not be closed
        else {
            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        backPressedTime = System.currentTimeMillis();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}