package com.smartplace.cfeofficer.reports;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.smartplace.cfeofficer.R;
import com.smartplace.cfeofficer.data.Comment;
import com.smartplace.cfeofficer.data.Report;
import com.smartplace.cfeofficer.data.WorkerReports;
import com.smartplace.cfeofficer.server.LoginResponse;
import com.smartplace.cfeofficer.services.MemoryServices;
import com.smartplace.cfeofficer.services.WebServices;
import com.smartplace.cfeofficer.utilities.Constants;
import com.smartplace.cfeofficer.utilities.TransparentProgressDialog;

import java.util.ArrayList;

public class ReportCommentsActivity extends Activity {

    private int mCommentType;
    private ArrayList<Comment> mComments;
    private ReportCommentsAdapter mReportCommentsAdapter;
    private TransparentProgressDialog mLoadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_comments);

        ListView listComments = (ListView)findViewById(R.id.list_comments);
        final EditText editComment = (EditText)findViewById(R.id.edit_comment);
        ImageButton imgBtnPublish = (ImageButton)findViewById(R.id.img_btn_publish);


        mCommentType = getIntent().getIntExtra("commentType", Constants.TYPE_COMMENT_PUBLIC);
        final Report report = new Gson().fromJson(getIntent().getStringExtra("report"),Report.class);

        if(mCommentType == Constants.TYPE_COMMENT_PUBLIC){
            getActionBar().setTitle(getString(R.string.header_public_comments));
            mComments = report.getPublicComments();

        }else if(mCommentType == Constants.TYPE_COMMENT_PRIVATE){
            getActionBar().setTitle(getString(R.string.header_private_comments));
            mComments = report.getPrivateComments();
        }

        //set loading dialog to be ready when needed
        mLoadingDialog = new TransparentProgressDialog(ReportCommentsActivity.this, R.drawable.ic_loading);

        //set typeface
        Typeface font= Typeface.createFromAsset(getAssets(), "fonts/OpenSansLight.ttf");
        editComment.setTypeface(font);

        mReportCommentsAdapter = new ReportCommentsAdapter(getBaseContext(),mComments);
        listComments.setAdapter(mReportCommentsAdapter);

        getActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.cfe_green)));
        getActionBar().setDisplayHomeAsUpEnabled(true);

        //set on click listeners
        imgBtnPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mCommentType == Constants.TYPE_COMMENT_PUBLIC){

                    mLoadingDialog.show();
                    mLoadingDialog.tv.setText(getString(R.string.sending_public_comment));
                    WebServices.setPublicComment(MemoryServices.getUserID(getBaseContext()),
                            report.getReportID(),editComment.getText().toString(),mSetCommentHandler);
                    editComment.setText("");
                }else if(mCommentType == Constants.TYPE_COMMENT_PRIVATE){

                    mLoadingDialog.show();
                    mLoadingDialog.tv.setText(getString(R.string.sending_private_comment));
                    WebServices.setPrivateComment(MemoryServices.getUserID(getBaseContext()),
                            report.getReportID(), editComment.getText().toString(), mSetCommentHandler);
                    editComment.setText("");
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.report_comments, menu);
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
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private Handler mSetCommentHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            //get response and parameter team name
            String response = bundle != null ? bundle.getString("response") : "";
            String commentText = bundle != null ? bundle.getString("comment") : "";

            //Check if dialog is active
            if (mLoadingDialog.isShowing()) {
                mLoadingDialog.dismiss();
                //Check response
                if ((response.equals("")) || response.equals("no connection")) {
                    //No internet access
                    Toast.makeText(getBaseContext(),
                            getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
                } else {

                    try {
                        //get json response into object
                        LoginResponse loginResponse = new Gson().fromJson(response, LoginResponse.class);
                        //check for different status values
                        if (loginResponse.getRequestStatus().equals("NOK1")) {
                            //invalid parameters
                            Toast.makeText(getBaseContext(),
                                    getString(R.string.invalid_parameters), Toast.LENGTH_SHORT).show();
                        }else if (loginResponse.getRequestStatus().equals("NOK2")) {
                            //wrong username/password combination
                            Toast.makeText(getBaseContext(),
                                    getString(R.string.authentication_failed), Toast.LENGTH_SHORT).show();
                        } else if (loginResponse.getRequestStatus().equals("OK")) {
                            Comment comment = new Comment();
                            comment.setWorkerName(MemoryServices.getWorkerName(getBaseContext()));
                            comment.setComment(commentText);
                            mComments.add(comment);
                            mReportCommentsAdapter.notifyDataSetChanged();


                        }
                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                        //invalid json response from the server
                        Toast.makeText(getBaseContext(), getString(R.string.server_error), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    };
}
