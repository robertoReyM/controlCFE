package com.smartplace.cfeofficer.data;

import java.util.ArrayList;

/**
 * Created by Roberto on 20/10/2014.
 */
public class WorkerReports {

    public ArrayList<Report> getReports() {
        return reports;
    }

    public void setReports(ArrayList<Report> reports) {
        this.reports = reports;
    }

    private ArrayList<Report> reports;
}
