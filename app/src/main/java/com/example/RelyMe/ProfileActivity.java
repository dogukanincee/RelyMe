package com.example.RelyMe;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.jaeger.library.StatusBarUtil;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class ProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //Navigation Drawer fields
    DrawerLayout drawer;
    NavigationView navigationView;
    Toolbar toolbar;
    ActionBarDrawerToggle toggle;
    String registeredPassWord;

    ListView walletListView;
    EditText walletEditText;
    EditText walletLabelEditText;
    Button encryptButton;
    Button decryptButton;
    ArrayList<String> walletArrayList;
    ArrayAdapter<String> arrayAdapter;

    String walletLabel;
    Integer index;
    String wallet;
    String AES = "AES";

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

        PrefManager prefManager = new PrefManager(getApplicationContext());
        registeredPassWord = prefManager.getPassword();

        walletListView = findViewById(R.id.walletListView);
        walletEditText = findViewById(R.id.walletText);
        walletLabelEditText = findViewById(R.id.walletLabel);
        encryptButton = findViewById(R.id.encryptButton);
        decryptButton = findViewById(R.id.decryptButton);

        walletArrayList = new ArrayList<>(prefManager.getWallets());
        Log.w("NNN", Arrays.toString(walletArrayList.toArray()));
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, walletArrayList);
        walletListView.setAdapter(arrayAdapter);

        encryptButton.setOnClickListener(v -> {
            wallet = walletEditText.getText().toString();
            walletLabel = walletLabelEditText.getText().toString();
            if ((!(wallet.length() <= 1) && !(walletLabel.length() <= 1))) {
                //TODO: Write and store the encrypted version of wallet instead of the wallet itself
                try {
                    String encryptedString = encrypt(wallet, registeredPassWord);
                    prefManager.addWallet(walletLabel, encryptedString);
                    walletArrayList.add(walletLabel + "\n" + encryptedString);
                    arrayAdapter.notifyDataSetChanged();
                    walletEditText.setText("");
                    walletLabelEditText.setText("");
                    showMessage(walletLabel + " has been encrypted");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        decryptButton.setOnClickListener(v -> {
            wallet = walletEditText.getText().toString();
            walletLabel = walletLabelEditText.getText().toString();
            if ((!(wallet.length() <= 1) && !(walletLabel.length() <= 1))) {
                //TODO: Check first if the written / copied text is stored in json. Do only if it exist.
                //TODO: Decrypt the wallet selected and update in both json and listView by directly showing the wallet
                try {
                    String decryptedString = decrypt(wallet, registeredPassWord);
                    arrayAdapter.remove(walletArrayList.get(index));
                    walletArrayList.add(walletLabel + "\n" + decryptedString);
                    arrayAdapter.notifyDataSetChanged();
                    walletEditText.setText("");
                    walletLabelEditText.setText("");
                    showMessage(walletLabel + " has been decrypted");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        walletListView.setOnItemClickListener((parent, view, position, id) -> {
            walletLabel = walletLabelEditText.getText().toString();
            index = position;
            showMessage(parent.getItemAtPosition(position).toString() + " is selected");
            runOnUiThread(() -> {
                String label = parent.getItemAtPosition(position).toString().split("\n")[0];
                String wallet = parent.getItemAtPosition(position).toString().split("\n")[1];
                walletEditText.setText(wallet);
                walletEditText.invalidate();
                walletLabelEditText.setText(label);
                walletLabelEditText.invalidate();
            });
        });

        walletListView.setOnItemLongClickListener((parent, view, position, id) -> {
            showMessage(parent.getItemAtPosition(position).toString() + " is deleted");
            index = position;
            prefManager.deleteWallet(parent.getItemAtPosition(position).toString());
            arrayAdapter.remove(parent.getItemAtPosition(position).toString());
            arrayAdapter.notifyDataSetChanged();
            return false;
        });
    }

    private String decrypt(String outputString, String password) throws Exception {
        SecretKeySpec key = generateKey(password);
        Cipher c = Cipher.getInstance(AES);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decodedValue = Base64.decode(outputString, Base64.DEFAULT);
        byte[] decValue = c.doFinal(decodedValue);
        String decryptedValue = new String(decValue);
        return decryptedValue;
    }

    private String encrypt(String data, String password) throws Exception {
        SecretKeySpec key = generateKey(password);
        Cipher c = Cipher.getInstance(AES);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = c.doFinal(data.getBytes());
        String encryptedValue = Base64.encodeToString(encVal, Base64.DEFAULT);
        return encryptedValue;
    }

    private SecretKeySpec generateKey(String password) throws Exception {
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = password.getBytes("UTF-8");
        digest.update(bytes, 0, bytes.length);
        byte[] key = digest.digest();
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        return secretKeySpec;
    }

    //If Navigation Drawer is open and if back button pressed
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            finish();
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
                startActivity(settings);
                break;
            case R.id.nav_about:              //If About field is clicked, starts AboutActivity
                Intent about = new Intent(ProfileActivity.this, AboutActivity.class);
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
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

    //Signs out and starts login activity
    private void signOut() {
        Intent loginActivity = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(loginActivity);
        finish();
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
    }
}