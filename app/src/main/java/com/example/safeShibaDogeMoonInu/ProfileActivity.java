package com.example.safeShibaDogeMoonInu;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.jaeger.library.StatusBarUtil;

import java.util.ArrayList;
import java.util.Objects;

public class ProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //Navigation Drawer fields
    DrawerLayout drawer;
    NavigationView navigationView;
    Toolbar toolbar;
    ActionBarDrawerToggle toggle;
    UserLocalStore userLocalStore;
    String registeredPassWord;

    ListView walletListView;
    EditText walletEditText;
    Button encryptButton;
    Button decryptButton;
    ArrayList<String> walletArrayList = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;

    Integer index;
    String wallet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        StatusBarUtil.setTransparent(this);

        //Initialize fields
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_security_24);

        getWindow().setStatusBarColor(Color.TRANSPARENT);

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        userLocalStore = new UserLocalStore(this);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null)
            registeredPassWord = (String) bundle.get("password");

        walletListView = findViewById(R.id.walletListView);
        walletEditText = findViewById(R.id.walletText);
        encryptButton = findViewById(R.id.encryptButton);
        decryptButton = findViewById(R.id.decryptButton);

        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, walletArrayList);
        walletListView.setAdapter(arrayAdapter);

        encryptButton.setOnClickListener(v -> {
            if (!(walletEditText.getText().length() <= 1)) {
                wallet = walletEditText.getText().toString();
                //TODO: Write and store the encrypted version of wallet instead of the wallet itself
                walletArrayList.add(wallet);
                arrayAdapter.notifyDataSetChanged();
                walletEditText.setText("");
            }
        });

        decryptButton.setOnClickListener(v -> {
            if (!(walletEditText.getText().length() <= 1)) {
                //TODO: Check first if the written / copied text is stored in json. Do only if it exist.
                //TODO: Decrypt the wallet selected and update in both json and listView by directly showing the wallet
                /*

                 */
            }
        });

        walletListView.setOnItemClickListener((parent, view, position, id) -> {
            index = position;
            showMessage(parent.getItemAtPosition(position).toString() + " has selected");

            runOnUiThread(() -> {
                walletEditText.setText(parent.getItemAtPosition(position).toString());
                walletEditText.invalidate();
            });
        });

        walletListView.setOnItemLongClickListener((parent, view, position, id) -> {
            index = position;
            arrayAdapter.remove(parent.getItemAtPosition(position).toString());
            arrayAdapter.notifyDataSetChanged();
            showMessage(parent.getItemAtPosition(position).toString() + " has deleted");
            return false;
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        /*
        if (authenticate()) displayUserDetails();
        else signOut();
        */

    }

    private boolean authenticate() {
        return userLocalStore.getUserLoggedIn();
    }

    private void displayUserDetails() {
        User user = userLocalStore.getLoggedInUser();
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
            case R.id.nav_profile:  //If Profile field is clicked, nothing happens
                break;
            case R.id.nav_settings:  //If Settings field is clicked, starts SettingsActivity
                Intent settings = new Intent(ProfileActivity.this, SettingsActivity.class);
                settings.putExtra("password", registeredPassWord);
                startActivity(settings);
                break;
            case R.id.nav_about:              //If About field is clicked, starts AboutActivity
                Intent about = new Intent(ProfileActivity.this, AboutActivity.class);
                about.putExtra("password", registeredPassWord);
                startActivity(about);
                break;
            case R.id.nav_sign_out: //If Sign Out field is clicked, starts LoginActivity
                signOut();
        }
        drawer.closeDrawer(GravityCompat.START);  //Closes Navigation Drawer
        return true;
    }

    //Shows a toast message
    private void showMessage(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
    }

    //Signs out and starts login activity
    private void signOut() {
        /*
        userLocalStore.clearUserData();
        userLocalStore.setUserLoggedIn(false);
         */
        Intent loginActivity = new Intent(getApplicationContext(), LoginActivity.class);
        loginActivity.putExtra("password", registeredPassWord);
        startActivity(loginActivity);
        finish();
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
    }
}