package com.baekdev.themultiqueue.DataStructure;

public class FFFavMode {
    public boolean story;
    public boolean level;
    public boolean pvp;
    public boolean hard_contents;
    public boolean collect;
    public boolean gold_saucer;
    public boolean screenshot;
    public boolean random;

    public FFFavMode() {
        this.story = false;
        this.level = false;
        this.pvp = false;
        this.hard_contents = false;
        this.collect = false;
        this.gold_saucer = false;
        this.screenshot = false;
        this.random = false;
    }

    public void setCollect(boolean collect) {
        this.collect = collect;
    }

    public void setGold_saucer(boolean gold_saucer) {
        this.gold_saucer = gold_saucer;
    }

    public void setHard_contents(boolean hard_contents) {
        this.hard_contents = hard_contents;
    }

    public void setLevel(boolean level) {
        this.level = level;
    }

    public void setPvp(boolean pvp) {
        this.pvp = pvp;
    }

    public void setRandom(boolean random) {
        this.random = random;
    }

    public void setScreenshot(boolean screenshot) {
        this.screenshot = screenshot;
    }

    public void setStory(boolean story) {
        this.story = story;
    }

    public boolean isStory() {
        return story;
    }

    public boolean isCollect() {
        return collect;
    }

    public boolean isGold_saucer() {
        return gold_saucer;
    }

    public boolean isHard_contents() {
        return hard_contents;
    }

    public boolean isLevel() {
        return level;
    }

    public boolean isPvp() {
        return pvp;
    }

    public boolean isRandom() {
        return random;
    }

    public boolean isScreenshot() {
        return screenshot;
    }
}

