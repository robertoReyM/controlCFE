package com.smartplace.cfeofficer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.smartplace.cfeofficer.data.WorkerReports;
import com.smartplace.cfeofficer.reports.ReportsActivity;
import com.smartplace.cfeofficer.server.LoginResponse;
import com.smartplace.cfeofficer.services.MemoryServices;
import com.smartplace.cfeofficer.services.WebServices;
import com.smartplace.cfeofficer.utilities.TransparentProgressDialog;


public class LoginActivity extends Activity {

    private EditText mEditUsername;
    private EditText mEditPassword;
    private Button mBtnLogin;
    private TransparentProgressDialog mLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //get xml references
        mEditUsername = (EditText)findViewById(R.id.edit_username);
        mEditPassword = (EditText)findViewById(R.id.edit_password);
        mBtnLogin = (Button)findViewById(R.id.btn_login);


        //set typeface
        Typeface font= Typeface.createFromAsset(getAssets(), "fonts/OpenSansLight.ttf");
        mEditUsername.setTypeface(font);
        mEditPassword.setTypeface(font);
        mBtnLogin.setTypeface(font);
        ((TextView)findViewById(R.id.txt_app_name_1)).setTypeface(font);
        ((TextView)findViewById(R.id.txt_app_name_2)).setTypeface(font);

        YoYo.with(Techniques.FadeInRight).duration(1000).playOn(findViewById(R.id.txt_app_name_1));
        YoYo.with(Techniques.FadeInLeft).duration(1000).playOn(findViewById(R.id.txt_app_name_2));


        mEditUsername.setVisibility(View.INVISIBLE);
        mEditPassword.setVisibility(View.INVISIBLE);
        mBtnLogin.setVisibility(View.INVISIBLE);


        //set loading dialog to be ready when needed
        mLoadingDialog = new TransparentProgressDialog(LoginActivity.this, R.drawable.ic_loading);

        Handler handler2 = new Handler();
        handler2.postDelayed(new Runnable() {
            @Override
            public void run() {
                //set animations
                mEditUsername.setVisibility(View.VISIBLE);
                YoYo.with(Techniques.SlideInLeft).duration(300).playOn(mEditUsername);
            }
        }, 400);
        Handler handler3 = new Handler();
        handler3.postDelayed(new Runnable() {
            @Override
            public void run() {
                //set animations
                mEditPassword.setVisibility(View.VISIBLE);
                YoYo.with(Techniques.SlideInLeft).duration(300).playOn(mEditPassword);
            }
        }, 700);
        Handler handler4 = new Handler();
        handler4.postDelayed(new Runnable() {
            @Override
            public void run() {
                //set animations
                mBtnLogin.setVisibility(View.VISIBLE);
                YoYo.with(Techniques.SlideInLeft).duration(300).playOn(mBtnLogin);
            }
        }, 1000);
        //set text watcher
        mEditUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {}

            @Override
            public void afterTextChanged(Editable editable) {
                enableLoginIfReady();
            }
        });
        mEditPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {}

            @Override
            public void afterTextChanged(Editable editable) {
                enableLoginIfReady();
            }
        });

        //set on click listeners
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLoadingDialog.show();
                mLoadingDialog.tv.setText(getString(R.string.logging_in));
                WebServices.userLogin(mEditUsername.getText().toString(),
                        mEditPassword.getText().toString(),MemoryServices.getPushToken(getBaseContext()),mLoginHandler);
                //WebServices.getWorkerName(new Handler());
            }
        });
        enableLoginIfReady();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
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

    public void enableLoginIfReady() {

        boolean isReady =mEditUsername.getText().toString().length()>0 &&
                mEditPassword.getText().toString().length()>0;

        if (isReady) {
            mBtnLogin.setEnabled(true);
        } else {
            mBtnLogin.setEnabled(false);
        }
    }
    private Handler mLoginHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            //get response and parameter team name
            String response = bundle != null ? bundle.getString("response") : "";

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

                            //save info into memory
                            MemoryServices.setUserID(getBaseContext(),loginResponse.getUserID());
                            MemoryServices.setWorkerName(getBaseContext(),loginResponse.getWorkerName());
                            WorkerReports workerReports = new WorkerReports();
                            workerReports.setReports(loginResponse.getReports());
                            MemoryServices.setWorkerReports(getBaseContext(),new Gson().toJson(workerReports));

                            //go to reports activity
                            Intent intent = new Intent(getBaseContext(),ReportsActivity.class);
                            //delete activity from back stack
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
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
