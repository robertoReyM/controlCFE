package com.smartplace.cfeofficer.data;

/**
 * Created by Roberto on 20/10/2014.
 */
public class Comment {
    public String getWorkerName() {
        return workerName;
    }

    public void setWorkerName(String name) {
        this.workerName = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String commentText) {
        this.comment = commentText;
    }

    private String workerName;
    private String comment;
}
