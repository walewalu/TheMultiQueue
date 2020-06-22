package com.baekdev.themultiqueue.DataStructure;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChatData {
    private String chat;
    private String name;
    private String uid;
    private String time;

    public ChatData() {}

    public ChatData(String chat, String name, String uid, String time){
        this.chat = chat;
        this.name = name;
        this.uid = uid;
        this.time = time;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setChat(String chat) {
        this.chat = chat;
    }

    public String getName() {
        return name;
    }

    public String getChat() {
        return chat;
    }

    public String getUid() { return uid; }

    public String getTime() {
        return time;
    }
}
