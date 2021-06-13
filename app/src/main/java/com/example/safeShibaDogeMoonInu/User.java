package com.example.safeShibaDogeMoonInu;

import java.util.ArrayList;

public class User {

    private String password;
    private ArrayList<String> walletArrayList;

    //Constructor Method
    public User(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<String> getWalletArrayList() {
        return walletArrayList;
    }

    public void addWallet(String wallet) {
        walletArrayList.add(wallet);
    }

}