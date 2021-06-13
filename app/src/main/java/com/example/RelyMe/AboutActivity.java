package com.example.RelyMe;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.jaeger.library.StatusBarUtil;


public class AboutActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //Navigation Drawer fields
    DrawerLayout drawer;
    NavigationView navigationView;
    Toolbar toolbar;
    ActionBarDrawerToggle toggle;
    //About fields
    TextView about_title, about_text;
    ImageView uni_logo, app_logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

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

        about_title = findViewById(R.id.about_title);
        about_text = findViewById(R.id.about_text);

        uni_logo = findViewById(R.id.uni_logo);
        app_logo = findViewById(R.id.app_logo);

        //Checks phone permissions
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PackageManager.PERMISSION_GRANTED);
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
                Intent profile = new Intent(AboutActivity.this, ProfileActivity.class);
                startActivity(profile);
                break;
            case R.id.nav_settings:  //If Settings field is clicked, starts SettingsActivity
                Intent settings = new Intent(AboutActivity.this, SettingsActivity.class);
                startActivity(settings);
                break;
            case R.id.nav_about:             //If About field is clicked, nothing happens
                break;
            case R.id.nav_sign_out: //If Sign Out field is clicked, starts LoginActivity
                signOut();
        }
        drawer.closeDrawer(GravityCompat.START);  //Closes Navigation Drawer
        return true;
    }

    //Signs out and starts login activity
    private void signOut() {
        /*
        userLocalStore.clearUserData();
        userLocalStore.setUserLoggedIn(false);
         */
        Intent loginActivity = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(loginActivity);
        finish();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
    }
}