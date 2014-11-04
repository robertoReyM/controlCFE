package com.smartplace.cfeofficer.data;

import java.util.ArrayList;

/**
 * Created by Roberto on 20/10/2014.
 */
public class Report {
    public String getReportID() {
        return reportID;
    }

    public void setReportID(String reportID) {
        this.reportID = reportID;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public ArrayList<Comment> getPublicComments() {
        return publicComments;
    }

    public void setPublicComments(ArrayList<Comment> publicComments) {
        this.publicComments = publicComments;
    }

    public ArrayList<Comment> getPrivateComments() {
        return privateComments;
    }

    public void setPrivateComments(ArrayList<Comment> privateComments) {
        this.privateComments = privateComments;
    }

    private String reportID;
    private String lat;
    private String lng;
    private String type;
    private String subType;
    private String desc;
    private String status;
    private String creationDate;
    private String lastUpdate;
    private ArrayList<Comment> publicComments;
    private ArrayList<Comment> privateComments;

}
