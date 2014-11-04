package com.smartplace.cfeofficer.reports;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.smartplace.cfeofficer.R;
import com.smartplace.cfeofficer.data.Report;
import com.smartplace.cfeofficer.data.WorkerReports;
import com.smartplace.cfeofficer.services.MemoryServices;
import com.smartplace.cfeofficer.services.WebServices;
import com.smartplace.cfeofficer.utilities.Constants;

import java.util.ArrayList;


public class ReportsActivity extends Activity {

    public ArrayList<Report> mReports;
    private ReportsAdapter mReportsAdapter;
    private LinearLayout mLayoutReportsEmpty;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        //get xml references
        ListView listReports = (ListView)findViewById(R.id.list_reports);
        mLayoutReportsEmpty = (LinearLayout)findViewById(R.id.layout_reports_empty);

        mReports = new Gson().fromJson(MemoryServices.getWorkerReports(getBaseContext()),WorkerReports.class).getReports();
        mReportsAdapter = new ReportsAdapter(getBaseContext(),mReports);
        listReports.setAdapter(mReportsAdapter);

        if(mReports.size()==0){
            mLayoutReportsEmpty.setVisibility(View.VISIBLE);
        }else{
            mLayoutReportsEmpty.setVisibility(View.GONE);
        }

        listReports.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getBaseContext(),ReportDetailedActivity.class);
                intent.putExtra("report",new Gson().toJson(mReports.get(i)));
                startActivityForResult(intent, Constants.REQUEST_DETAILED_REPORT);
            }
        });

        getActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.cfe_green)));
    }


    @Override
    public void onResume(){
        super.onResume();

        WebServices.getWorkerReports(MemoryServices.getUserID(getBaseContext()),mWorkerReportsHandler);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.reports, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private Handler mWorkerReportsHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            //get response and parameter team name
            String response = bundle != null ? bundle.getString("response") : "";

            //Check response
            if ((response.equals("")) || response.equals("no connection")) {
                //No internet access
                Toast.makeText(getBaseContext(),
                        getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
            } else {

                try {
                    //get json response into object
                    WorkerReports workerReports = new Gson().fromJson(response, WorkerReports.class);
                    mReports.clear();
                    for(Report report:workerReports.getReports()){
                        mReports.add(report);
                    }
                    if(mReports.size()==0){
                        mLayoutReportsEmpty.setVisibility(View.VISIBLE);
                    }else{
                        mLayoutReportsEmpty.setVisibility(View.GONE);
                    }
                    mReportsAdapter.notifyDataSetChanged();
                    //save response
                    MemoryServices.setWorkerReports(getBaseContext(),new Gson().toJson(workerReports));

                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                    //invalid json response from the server
                    Toast.makeText(getBaseContext(), getString(R.string.server_error), Toast.LENGTH_SHORT).show();
                }
            }
        }
    };
}
