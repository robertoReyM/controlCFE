package com.smartplace.cfeofficer.server;

import com.smartplace.cfeofficer.data.Comment;
import com.smartplace.cfeofficer.data.Report;

import java.util.ArrayList;

/**
 * Created by Roberto on 20/10/2014.
 */
public class LoginResponse {
    public String getWorkerName() {
        return workerName;
    }

    public void setWorkerName(String workerName) {
        this.workerName = workerName;
    }

    private String workerName;

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    private String requestStatus;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public ArrayList<Report> getReports() {
        return reports;
    }

    public void setReports(ArrayList<Report> reports) {
        this.reports = reports;
    }

    private String userID;
    private ArrayList<Report> reports;
}
