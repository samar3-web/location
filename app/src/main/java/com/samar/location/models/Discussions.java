package com.samar.location.models;

import java.util.Date;



public class Discussions {
    private String text;
    private String friendEmail;
    private long time;
    private boolean displayed;


    //Constructor
    public Discussions(String text, String friendEmail, long time) {
        this.text = text;
        this.friendEmail = friendEmail;
        this.time = time;
        displayed = false;
    }

    public Discussions(){

    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFriendEmail() {
        return friendEmail;
    }

    public void setFriendEmail(String friendEmail) {
        this.friendEmail = friendEmail;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isDisplayed() {
        return displayed;
    }

    public void setDisplayed(boolean displayed) {
        this.displayed = displayed;
    }




}
