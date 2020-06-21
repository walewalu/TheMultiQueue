package com.baekdev.themultiqueue.DataStructure;

public class FFFavStyle {
    public boolean story;
    public boolean fashion;
    public boolean doing_role;
    public boolean practice;
    public boolean housing;
    public boolean helper;
    public boolean adventure;
    public boolean talk;

    public FFFavStyle() {
        this.story = false;
        this.fashion = false;
        this.doing_role = false;
        this.practice = false;
        this.housing = false;
        this.helper = false;
        this.adventure = false;
        this.talk = false;
    }

    public void setStory(boolean story) {
        this.story = story;
    }

    public void setDoing_role(boolean doing_role) {
        this.doing_role = doing_role;
    }

    public void setPractice(boolean practice) {
        this.practice = practice;
    }

    public void setAdventure(boolean adventure) {
        this.adventure = adventure;
    }

    public void setFashion(boolean fashion) {
        this.fashion = fashion;
    }

    public void setHelper(boolean helper) {
        this.helper = helper;
    }

    public void setHousing(boolean housing) {
        this.housing = housing;
    }

    public void setTalk(boolean talk) {
        this.talk = talk;
    }

    public boolean isDoing_role() {
        return doing_role;
    }

    public boolean isPractice() {
        return practice;
    }

    public boolean isFashion() {
        return fashion;
    }

    public boolean isAdventure() {
        return adventure;
    }

    public boolean isHelper() {
        return helper;
    }

    public boolean isStory() {
        return story;
    }

    public boolean isHousing() {
        return housing;
    }

    public boolean isTalk() {
        return talk;
    }
}

