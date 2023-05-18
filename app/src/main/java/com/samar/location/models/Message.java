package com.samar.location.models;

import java.util.Date;


public class Message {
    private String text;
    private String senderEmail;
    private String receiverEmail;

    private long time;

    //Constructor
    public Message(String text, String senderEmail, String receiverEmail) {
        this.text = text;
        this.senderEmail = senderEmail;
        this.receiverEmail = receiverEmail;
        // Initialize to current time
        time = new Date().getTime();

    }

    public Message() {

    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getReceiverEmail() {
        return receiverEmail;
    }

    public void setReceiverEmail(String receiverEmail) {
        this.receiverEmail = receiverEmail;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
