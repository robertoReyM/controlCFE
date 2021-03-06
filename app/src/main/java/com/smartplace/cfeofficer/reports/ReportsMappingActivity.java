package com.smartplace.cfeofficer.reports;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.smartplace.cfeofficer.R;
import com.smartplace.cfeofficer.data.Report;
import com.smartplace.cfeofficer.data.WorkerReports;
import com.smartplace.cfeofficer.services.MemoryServices;

import java.util.ArrayList;

public class ReportsMappingActivity extends Activity {
    private GoogleMap googleMap;
    MapView mMapView;
    public ArrayList<Report> mReports;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports_mapping);

        mMapView = (MapView) findViewById(R.id.mapView);

        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();// needed to get the map to display immediately
        try {
            MapsInitializer.initialize(getBaseContext().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        googleMap = mMapView.getMap();
        mReports = new Gson().fromJson(MemoryServices.getWorkerReports(getBaseContext()),WorkerReports.class).getReports();
        for(Report report:mReports) {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Float.valueOf(report.getLat()), Float.valueOf(report.getLng())), 12));
            googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(Float.valueOf(report.getLat()), Float.valueOf(report.getLng()))));
        }
        getActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.cfe_green)));
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_reports_mapping, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

}
