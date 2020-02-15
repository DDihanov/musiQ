package com.dihanov.musiq.models;

public class AuthenticateUserModel {
    private String username;
    private String password;
    private boolean shouldRemember;

    public AuthenticateUserModel(String username, String password, boolean shouldRemember) {
        this.username = username;
        this.password = password;
        this.shouldRemember = shouldRemember;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isShouldRemember() {
        return shouldRemember;
    }

    public void setShouldRemember(boolean shouldRemember) {
        this.shouldRemember = shouldRemember;
    }
}
