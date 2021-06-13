package com.example.safeShibaDogeMoonInu;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import java.util.concurrent.Executor;

public class LoginActivity extends AppCompatActivity {

    EditText userPassword;                                //User mail and password fields
    UserLocalStore userLocalStore;
    String registeredPassWord;
    private Button btnLogin, registerButton, fingerprintButton;   //Login, register and reset passwords buttons
    //If all fields are filled, then login button enables
    private final TextWatcher loginTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String passwordInput = userPassword.getText().toString().trim();
            btnLogin.setEnabled(!passwordInput.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private long backPressedTime;                                   //System's current time in milliseconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userPassword = findViewById(R.id.login_password);
        btnLogin = findViewById(R.id.loginBtn);
        registerButton = findViewById(R.id.registerButton);
        fingerprintButton = findViewById(R.id.fingerprintButton);

        userPassword.addTextChangedListener(loginTextWatcher);

        userLocalStore = new UserLocalStore(this);


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null)
            registeredPassWord = (String) bundle.get("password");

        BiometricManager biometricManager = BiometricManager.from(this);

        int i = biometricManager.canAuthenticate();
        if (i == BiometricManager.BIOMETRIC_SUCCESS) {
            showMessage("You can use fingerprints");
        } else if (i == BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE) {
            showMessage("You do not have fingerprint sensor");
            fingerprintButton.setVisibility(View.GONE);
        } else if (i == BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE) {
            showMessage("Sensor is unavailable");
            fingerprintButton.setVisibility(View.GONE);
        } else if (i == BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED) {
            showMessage("You do not have any saved fingerprints");
            fingerprintButton.setVisibility(View.GONE);
        }

        Executor executor = ContextCompat.getMainExecutor(this);

        BiometricPrompt biometricPrompt = new BiometricPrompt(LoginActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                if (registeredPassWord != null) {
                    super.onAuthenticationSucceeded(result);
                    showMessage("Login Success with fingerprint");
                    updateUI();
                } else {
                    showMessage("Please Sign Up first!");
                    Intent registerActivity = new Intent(getApplicationContext(), RegisterActivity.class);
                    startActivity(registerActivity);
                    finish();
                }
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        });

        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder().setTitle("Login").setDescription("Use fingerprint to log in the app").setNegativeButtonText("Cancel").build();


        //If login button is clicked, tries to log in
        btnLogin.setOnClickListener(view -> {

            //Gives error if written passwords are smaller than 6 characters
            if (userPassword.length() < 6) {
                userPassword.setError("Password Must Be At Least 6 Characters");
            }

            if (registeredPassWord != null) {
                if (registeredPassWord.equals(userPassword.getText().toString())) {
                    User user = new User(null);
                    userLocalStore.storeUserData(user);
                    userLocalStore.setUserLoggedIn(true);
                    showMessage("Successfully logged in");
                    updateUI();
                } else {
                    showMessage("Password is incorrect");
                }
            }

        });

        //If register button is clicked, RegisterActivity starts
        registerButton.setOnClickListener(v -> {
            Intent registerActivity = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(registerActivity);
            finish();
        });

        fingerprintButton.setOnClickListener(v -> {
            biometricPrompt.authenticate(promptInfo);
        });
    }

    //Starts MapActivity
    private void updateUI() {
        Intent profile = new Intent(LoginActivity.this, ProfileActivity.class);
        profile.putExtra("password", registeredPassWord);
        startActivity(profile);
    }

    //Shows a toast message
    private void showMessage(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
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
}