package com.example.RelyMe;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    public PrefManager(Context context) {
        pref = context.getSharedPreferences("PROJECT_NAME", Context.MODE_PRIVATE);
        editor = pref.edit();
        editor.apply();
    }
    public String getPassword() {
        String password = pref.getString("password", null);
        if (password != null) {
            int len = password.length();
            len /= 2;
            StringBuilder b1 = new StringBuilder(password.substring(0, len));
            StringBuilder b2 = new StringBuilder(password.substring(len));
            password = b1.reverse().toString() + b2.reverse().toString();
            return password;
        }
        return null;
    }

    public void setPassword(String password) {
        int len = password.length();
        len /= 2;
        StringBuilder b1 = new StringBuilder(password.substring(0, len));
        StringBuilder b2 = new StringBuilder(password.substring(len));
        b1.reverse();
        b2.reverse();
        password = b1.toString() + b2.toString();
        editor.putString("password", password);
        editor.putBoolean("registered", true);
        editor.apply();
    }

    public boolean isRegistered(){
        return pref.getBoolean("registered", false);
    }
}