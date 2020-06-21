package com.baekdev.themultiqueue.DataStructure;

public class LoLFavPos {
    public boolean top;
    public boolean jgl;
    public boolean mid;
    public boolean adc;
    public boolean spt;

    public LoLFavPos() {
        this.top = false;
        this.jgl = false;
        this.mid = false;
        this.adc = false;
        this.spt = false;
    }

    public void setTop(boolean top) {
        this.top = top;
    }

    public void setJgl(boolean jgl) {
        this.jgl = jgl;
    }

    public void setMid(boolean mid) {
        this.mid = mid;
    }

    public void setAdc(boolean adc) {
        this.adc = adc;
    }

    public void setSpt(boolean spt) {
        this.spt = spt;
    }

    public boolean isTop() {
        return top;
    }

    public boolean isJgl() {
        return jgl;
    }

    public boolean isMid() {
        return mid;
    }

    public boolean isAdc() {
        return adc;
    }

    public boolean isSpt() {
        return spt;
    }
}

