package com.baekdev.themultiqueue.DataStructure;

public class ChatRoom {
    private String name;
    private String id;
    private String description;
    private int current_Members;
    private int maximum_Members;
    private int game;

    public ChatRoom() {};

    public ChatRoom(String name, String id, String description, int current_Members, int maximum_Members, int game){
        this.name = name;
        this.id = id;
        this.description = description;
        this.current_Members = current_Members;
        this.maximum_Members = maximum_Members;
        this.game = game;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCurrent_Members(int current_Members) {
        this.current_Members = current_Members;
    }

    public void setMaximum_Members(int maximum_Members) {
        this.maximum_Members = maximum_Members;
    }

    public void setGame(int game) {
        this.game = game;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public int getCurrent_Members() {
        return current_Members;
    }

    public int getMaximum_Members() {
        return maximum_Members;
    }

    public int getGame() {
        return game;
    }
}
