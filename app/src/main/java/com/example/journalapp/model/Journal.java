package com.example.journalapp.model;

import com.google.firebase.Timestamp;

public class Journal {

    private String title, thoughts, imageUri, userId, userName;
    private Timestamp timeAdd;

    public Journal() {
    }

    public Journal(String title, String thoughts, String imageUri, String userId, String userName, Timestamp timeAdd) {
        this.title = title;
        this.thoughts = thoughts;
        this.imageUri = imageUri;
        this.userId = userId;
        this.userName = userName;
        this.timeAdd = timeAdd;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThoughts() {
        return thoughts;
    }

    public void setThoughts(String thoughts) {
        this.thoughts = thoughts;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Timestamp getTimeAdd() {
        return timeAdd;
    }

    public void setTimeAdd(Timestamp timeAdd) {
        this.timeAdd = timeAdd;
    }
}
