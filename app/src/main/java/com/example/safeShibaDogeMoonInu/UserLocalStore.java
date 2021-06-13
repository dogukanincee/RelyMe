package com.example.safeShibaDogeMoonInu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

public class UserLocalStore {

    public static final String SP_NAME = "userDetails";
    SharedPreferences userLocalDatabase;

    public UserLocalStore(Context context) {
        userLocalDatabase = context.getSharedPreferences(SP_NAME, 0);
    }

    public void storeUserData(User user) {
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putString("password", user.getPassword());
        spEditor.putStringSet("wallets", (Set<String>) user.getWalletArrayList());
        spEditor.apply();
    }

    public User getLoggedInUser() {
        String password = userLocalDatabase.getString("password", "");
        Set<String> wallets = userLocalDatabase.getStringSet("wallets", null);

        User storedUser = new User(password);

        return storedUser;
    }

    public void clearUserData() {
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.apply();
        spEditor.clear();
    }

    public boolean getUserLoggedIn() {
        return userLocalDatabase.getBoolean("loggedIn", false);
    }

    public void setUserLoggedIn(boolean loggedIn) {
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putBoolean("LoggedIn", loggedIn);
        spEditor.apply();
    }
}
