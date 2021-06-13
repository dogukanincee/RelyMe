package com.example.RelyMe;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.jaeger.library.StatusBarUtil;

public class SettingsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //Navigation Drawer fields
    DrawerLayout drawer;
    NavigationView navigationView;
    Toolbar toolbar;
    ActionBarDrawerToggle toggle;
    //Layout fields
    private Button changePassword, changeP;   //Change Email and Change Password buttons
    private EditText newPassword;//New Password field


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        StatusBarUtil.setTransparent(this);

        //Initialize fields
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_security_24);

        getWindow().setStatusBarColor(Color.TRANSPARENT);

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        changePassword = findViewById(R.id.changePassword);
        changeP = findViewById(R.id.changeP);
        newPassword = findViewById(R.id.newPassword);

        changeP.setVisibility(View.GONE);
        newPassword.setVisibility(View.GONE);

        //If Change Password button is clicked, sets "ChangeP" button's visibility
        changePassword.setOnClickListener(v -> {
            if (!(changeP.getVisibility() == View.VISIBLE)) {
                changeP.setVisibility(View.VISIBLE);
                newPassword.setVisibility(View.VISIBLE);
            } else {
                changeP.setVisibility(View.GONE);
                newPassword.setVisibility(View.GONE);
            }
        });

        //If ChangeP button is clicked, tries to update password
        changeP.setOnClickListener(v -> {
            if (!newPassword.getText().toString().trim().equals("")) {
                if (!(newPassword.getText().toString().trim().length() < 6)) {
                    if (!sameAsOldPassword(newPassword.getText().toString().trim())) {
                        signOut();
                    }
                    else{
                        newPassword.setError("Password Can't Be The Same As The Previous Password");
                    }
                } else {
                    newPassword.setError("Password Must Be Minimum 6 Characters");
                }
            } else if (newPassword.getText().toString().trim().equals("")) {
                newPassword.setError("Password Cannot Be Empty");
            }
        });
    }

    //If Navigation Drawer is open and if back button pressed
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //Handles navigation view item clicks
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_profile:          //If Profile field is clicked, starts ProfileActivity
                Intent profile = new Intent(SettingsActivity.this, ProfileActivity.class);
                startActivity(profile);
                break;
            case R.id.nav_settings:         //If Settings field is clicked, nothing happens
                break;
            case R.id.nav_about:              //If About field is clicked, starts AboutActivity
                Intent about = new Intent(SettingsActivity.this, AboutActivity.class);
                startActivity(about);
                break;
            case R.id.nav_sign_out:         //If Sign Out field is clicked, starts LoginActivity
                signOut();
                break;
        }
        drawer.closeDrawer(GravityCompat.START); //Closes Navigation Drawer
        return true;
    }

    private boolean sameAsOldPassword(String password){
        PrefManager prefManager = new PrefManager(getApplicationContext());
        String oldPassword = prefManager.getPassword();
        return oldPassword.equals(password);
    }

    //Signs out and starts login activity
    private void signOut() {
        PrefManager prefManager = new PrefManager(getApplicationContext());
        prefManager.setPassword(newPassword.getText().toString().trim());
        Intent loginActivity = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(loginActivity);
        finish();
    }

    //Shows a toast message
    private void showMessage(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
    }
}