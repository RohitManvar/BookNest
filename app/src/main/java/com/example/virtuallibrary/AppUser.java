package com.example.virtuallibrary;

public class AppUser {
    public String name;
    public String email;
    public String profileImage;

    public AppUser() {} // Required for Firebase

    public AppUser(String name, String email, String profileImage) {
        this.name = name;
        this.email = email;
        this.profileImage = profileImage;
    }
}
