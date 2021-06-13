package com.example.RelyMe;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;

public class PrefManager {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    public PrefManager(Context context) {
        pref = context.getSharedPreferences("PROJECT_NAME", Context.MODE_PRIVATE);
        editor = pref.edit();
        editor.apply();
    }

    public void addWallet(String label, String wallet) {
        String labels = pref.getString("labels", null);
        if (labels != null) {
            Log.w("NNN", "not null, label: " + label + "\nwallet: " + wallet);
            labels = labels + label + "\n";
            editor.putString("labels", labels);
            editor.putString(label, wallet);
            editor.apply();
        } else {
            Log.w("NNN", "null asf");
            editor.putString("labels", label + "\n");
            editor.putString(label, wallet);
            editor.apply();
        }
    }

    public void deleteWallet(String data) {
        String label = data.split("\n")[0];
        editor.remove(label);
        editor.apply();
    }

    public ArrayList<String> getWallets() {
        ArrayList<String> walletsList = new ArrayList<>();
        String labels = pref.getString("labels", null);
        if (labels != null) {
            Log.w("NNN get", "not null");
            String[] labelsArray = labels.split("\n");
            for (int i = 0; i < labelsArray.length; i++) {
                String wallet = pref.getString(labelsArray[i], null);
                if (wallet != null) {
                    Log.w("NNN get", "label:" + labelsArray[i] + "\nwallet:" + wallet);
                    walletsList.add(labelsArray[i] + "\n" + wallet);
                }
            }
        } else {
            Log.w("NNN", "null afff");
        }
        return walletsList;
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

    public boolean isRegistered() {
        return pref.getBoolean("registered", false);
    }
}