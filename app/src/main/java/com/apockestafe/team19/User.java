package com.apockestafe.team19;

/**
 * Created by nsallaire on 11/5/16.
 */

public class User {
    private String userName;
    private String address;

    public String getUserName() {
        return this.userName;
    }

    public String getAddress() {
        return this.address;
    }

    public void setUserName(String un) {
        this.userName = un;
    }

    public void setAddress(String add) {
        this.address = add;
    }
}
