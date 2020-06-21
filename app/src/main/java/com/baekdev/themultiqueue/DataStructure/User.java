package com.baekdev.themultiqueue.DataStructure;

import java.util.Map;

public class User {
    public String name;
    public String gender;
    public String email;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String name, String gender, String email) {
        this.name = name;
        this.gender = gender;
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    };

    public String getEmail() {
        return email;
    }
}
