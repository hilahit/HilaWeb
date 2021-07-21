package com.hit.hilaapp;

public class MessageModel {

    private int senderId;
    private String message;
    private String senderName;
    private boolean isDoctor;
    private long timeStamp;

    public MessageModel() {
    }

    public MessageModel(int senderId, String senderName, boolean isDoctor, String message, long timeStamp) {
        this.senderId = senderId;
        this.senderName = senderName;
        this.isDoctor = isDoctor;
        this.message = message;
        this.timeStamp = timeStamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public boolean isDoctor() {
        return isDoctor;
    }

    public void setDoctor(boolean doctor) {
        isDoctor = doctor;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
