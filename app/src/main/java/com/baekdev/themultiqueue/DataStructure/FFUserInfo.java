package com.baekdev.themultiqueue.DataStructure;

public class FFUserInfo {
    public String name;
    public String tribe;

    public FFUserInfo() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public FFUserInfo(String name, String tribe) {
        this.name = name;
        this.tribe = tribe;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTribe(String tribe) {
        this.tribe = tribe;
    }

    public String getName() {
        return name;
    }

    public String getTribe() {
        return tribe;
    }
}

