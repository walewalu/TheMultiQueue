package com.baekdev.themultiqueue.DataStructure;

public class LoLFavStyle {
    public boolean win_first;
    public boolean enjoyable;
    public boolean doing_role;
    public boolean practice;

    public LoLFavStyle() {
        this.win_first = false;
        this.enjoyable = false;
        this.doing_role = false;
        this.practice = false;
    }

    public void setWin_first(boolean win_first) {
        this.win_first = win_first;
    }

    public void setEnjoyable(boolean enjoyable) {
        this.enjoyable = enjoyable;
    }

    public void setDoing_role(boolean doing_role) {
        this.doing_role = doing_role;
    }

    public void setPractice(boolean practice) {
        this.practice = practice;
    }

    public boolean isWin_first() {
        return win_first;
    }

    public boolean isEnjoyable() {
        return enjoyable;
    }

    public boolean isDoing_role() {
        return doing_role;
    }

    public boolean isPractice() {
        return practice;
    }
}

