package com.baekdev.themultiqueue.DataStructure;

public class LoLFavMode {
    public boolean normal;
    public boolean rank;
    public boolean abyss;
    public boolean tft;

    public LoLFavMode() {
        this.normal = false;
        this.rank = false;
        this.abyss = false;
        this.tft = false;
    }

    public void setNormal(boolean normal) {
        this.normal = normal;
    }

    public void setRank(boolean rank) {
        this.rank = rank;
    }

    public void setAbyss(boolean abyss) {
        this.abyss = abyss;
    }

    public void setTft(boolean tft) {
        this.tft = tft;
    }

    public boolean isNormal() {
        return normal;
    }

    public boolean isRank() {
        return rank;
    }

    public boolean isAbyss() {
        return abyss;
    }

    public boolean isTft() {
        return tft;
    }
}
