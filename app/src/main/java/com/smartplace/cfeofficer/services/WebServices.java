package com.smartplace.cfeofficer.services;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Roberto on 20/10/2014.
 */
public class WebServices {

    private static final String SERVER_URL = "http://ec2-54-69-204-188.us-west-2.compute.amazonaws.com";
    private static final String PATH_LOGIN = "/worker/auth/login";
    private static final String PATH_GET_WORKER_REPORTS= "/get/worker/reports";
    private static final String PATH_GET_WORKER_NAME = "/worker/auth/get_worker_name";
    private static final String PATH_SET_PUBLIC_COMMENT = "/set/public_comment";
    private static final String PATH_SET_PRIVATE_COMMENT = "/set/private_comment";
    private static final String PATH_SET_REPORT_STATUS= "/set/report/status";

    private static final int CONNECTION_TIMEOUT = 10000;

    public static void userLogin(final String userName,final String password, final String pushToken, final Handler handler)
    {

        //Run new thread to perform users Info query
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {

                HttpParams httpParameters = new BasicHttpParams();
                // Set the timeout in milliseconds until a connection is established.
                // The default value is zero, that means the timeout is not used.
                int timeoutConnection = CONNECTION_TIMEOUT;
                HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);

                //Set post information to perform query
                HttpClient client = new DefaultHttpClient(httpParameters);
                //set post's URL
                HttpPost post = new HttpPost(SERVER_URL +PATH_LOGIN);
                //set post's header
                post.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
                //Create new NameValue pair list
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                //set parameters
                params.add(new BasicNameValuePair("userName", userName));
                params.add(new BasicNameValuePair("password", password));
                params.add(new BasicNameValuePair("pushToken", pushToken));
                try {
                    //set post value
                    post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                try {

                    //Execute post
                    HttpResponse httpResponse = client.execute(post);
                    //get post's entity
                    HttpEntity entity = httpResponse.getEntity();

                    String response = EntityUtils.toString(entity);

                    //Set message to send to the handler
                    Message msg = handler.obtainMessage();
                    Bundle bundle = new Bundle();
                    bundle.putString("response", response);
                    bundle.putString("userName", userName);
                    bundle.putString("password", password);
                    bundle.putString("pushToken", pushToken);
                    msg.setData(bundle);
                    handler.sendMessage(msg);

                } catch (IOException e) {
                    //Set message to send to the handler
                    Message msg = handler.obtainMessage();
                    Bundle bundle = new Bundle();
                    bundle.putString("response", "no connection");
                    bundle.putString("userName", userName);
                    bundle.putString("password", password);
                    bundle.putString("pushToken", pushToken);
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                }
            }
        });

        //start new thread
        thread.start();


    }

    public static void getWorkerName(final Handler handler)
    {

        //Run new thread to perform users Info query
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {

                HttpParams httpParameters = new BasicHttpParams();
                // Set the timeout in milliseconds until a connection is established.
                // The default value is zero, that means the timeout is not used.
                int timeoutConnection = CONNECTION_TIMEOUT;
                HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);

                //Set post information to perform query
                HttpClient client = new DefaultHttpClient(httpParameters);
                //set post's URL
                HttpPost post = new HttpPost(SERVER_URL +PATH_GET_WORKER_NAME);
                //set post's header
                post.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
                //Create new NameValue pair list
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                //set parameters
                try {
                    //set post value
                    post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                try {

                    //Execute post
                    HttpResponse httpResponse = client.execute(post);
                    //get post's entity
                    HttpEntity entity = httpResponse.getEntity();

                    String response = EntityUtils.toString(entity);

                    //Set message to send to the handler
                    Message msg = handler.obtainMessage();
                    Bundle bundle = new Bundle();
                    bundle.putString("response", response);
                    msg.setData(bundle);
                    handler.sendMessage(msg);

                } catch (IOException e) {
                    //Set message to send to the handler
                    Message msg = handler.obtainMessage();
                    Bundle bundle = new Bundle();
                    bundle.putString("response", "no connection");
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                }
            }
        });

        //start new thread
        thread.start();


    }
    public static void getWorkerReports(final String userID,final Handler handler)
    {

        //Run new thread to perform users Info query
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {

                HttpParams httpParameters = new BasicHttpParams();
                // Set the timeout in milliseconds until a connection is established.
                // The default value is zero, that means the timeout is not used.
                int timeoutConnection = CONNECTION_TIMEOUT;
                HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);

                //Set post information to perform query
                HttpClient client = new DefaultHttpClient(httpParameters);
                //set post's URL
                HttpPost post = new HttpPost(SERVER_URL +PATH_GET_WORKER_REPORTS);
                //set post's header
                post.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
                //Create new NameValue pair list
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                //set parameters
                params.add(new BasicNameValuePair("userID", userID));
                try {
                    //set post value
                    post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                try {

                    //Execute post
                    HttpResponse httpResponse = client.execute(post);
                    //get post's entity
                    HttpEntity entity = httpResponse.getEntity();

                    String response = EntityUtils.toString(entity);

                    //Set message to send to the handler
                    Message msg = handler.obtainMessage();
                    Bundle bundle = new Bundle();
                    bundle.putString("response", response);
                    msg.setData(bundle);
                    handler.sendMessage(msg);

                } catch (IOException e) {
                    //Set message to send to the handler
                    Message msg = handler.obtainMessage();
                    Bundle bundle = new Bundle();
                    bundle.putString("response", "no connection");
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                }
            }
        });

        //start new thread
        thread.start();


    }
    public static void setPublicComment(final String workerID,final String reportID,final String comment,final Handler handler)
    {

        //Run new thread to perform users Info query
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {

                HttpParams httpParameters = new BasicHttpParams();
                // Set the timeout in milliseconds until a connection is established.
                // The default value is zero, that means the timeout is not used.
                int timeoutConnection = CONNECTION_TIMEOUT;
                HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);

                //Set post information to perform query
                HttpClient client = new DefaultHttpClient(httpParameters);
                //set post's URL
                HttpPost post = new HttpPost(SERVER_URL +PATH_SET_PUBLIC_COMMENT);
                //set post's header
                post.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
                //Create new NameValue pair list
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                //set parameters
                params.add(new BasicNameValuePair("workerID", workerID));
                params.add(new BasicNameValuePair("reportID", reportID));
                params.add(new BasicNameValuePair("comment", comment));
                try {
                    //set post value
                    post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                try {

                    //Execute post
                    HttpResponse httpResponse = client.execute(post);
                    //get post's entity
                    HttpEntity entity = httpResponse.getEntity();

                    String response = EntityUtils.toString(entity);

                    //Set message to send to the handler
                    Message msg = handler.obtainMessage();
                    Bundle bundle = new Bundle();
                    bundle.putString("response", response);
                    bundle.putString("workerID", workerID);
                    bundle.putString("reportID", reportID);
                    bundle.putString("comment", comment);
                    msg.setData(bundle);
                    handler.sendMessage(msg);

                } catch (IOException e) {
                    //Set message to send to the handler
                    Message msg = handler.obtainMessage();
                    Bundle bundle = new Bundle();
                    bundle.putString("response", "no connection");
                    bundle.putString("workerID", workerID);
                    bundle.putString("reportID", reportID);
                    bundle.putString("comment", comment);
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                }
            }
        });

        //start new thread
        thread.start();


    }
    public static void setPrivateComment(final String workerID,final String reportID,final String comment,final Handler handler)
    {

        //Run new thread to perform users Info query
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {

                HttpParams httpParameters = new BasicHttpParams();
                // Set the timeout in milliseconds until a connection is established.
                // The default value is zero, that means the timeout is not used.
                int timeoutConnection = CONNECTION_TIMEOUT;
                HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);

                //Set post information to perform query
                HttpClient client = new DefaultHttpClient(httpParameters);
                //set post's URL
                HttpPost post = new HttpPost(SERVER_URL +PATH_SET_PRIVATE_COMMENT);
                //set post's header
                post.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
                //Create new NameValue pair list
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                //set parameters
                params.add(new BasicNameValuePair("workerID", workerID));
                params.add(new BasicNameValuePair("reportID", reportID));
                params.add(new BasicNameValuePair("comment", comment));
                try {
                    //set post value
                    post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                try {

                    //Execute post
                    HttpResponse httpResponse = client.execute(post);
                    //get post's entity
                    HttpEntity entity = httpResponse.getEntity();

                    String response = EntityUtils.toString(entity);

                    //Set message to send to the handler
                    Message msg = handler.obtainMessage();
                    Bundle bundle = new Bundle();
                    bundle.putString("response", response);
                    bundle.putString("workerID", workerID);
                    bundle.putString("reportID", reportID);
                    bundle.putString("comment", comment);
                    msg.setData(bundle);
                    handler.sendMessage(msg);

                } catch (IOException e) {
                    //Set message to send to the handler
                    Message msg = handler.obtainMessage();
                    Bundle bundle = new Bundle();
                    bundle.putString("response", "no connection");
                    bundle.putString("workerID", workerID);
                    bundle.putString("reportID", reportID);
                    bundle.putString("comment", comment);
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                }
            }
        });

        //start new thread
        thread.start();


    }
    public static void setReportStatus(final String workerID,final String reportID,final String status,final Handler handler)
    {

        //Run new thread to perform users Info query
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {

                HttpParams httpParameters = new BasicHttpParams();
                // Set the timeout in milliseconds until a connection is established.
                // The default value is zero, that means the timeout is not used.
                int timeoutConnection = CONNECTION_TIMEOUT;
                HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);

                //Set post information to perform query
                HttpClient client = new DefaultHttpClient(httpParameters);
                //set post's URL
                HttpPost post = new HttpPost(SERVER_URL +PATH_SET_REPORT_STATUS);
                //set post's header
                post.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
                //Create new NameValue pair list
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                //set parameters
                params.add(new BasicNameValuePair("workerID", workerID));
                params.add(new BasicNameValuePair("reportID", reportID));
                params.add(new BasicNameValuePair("status", status));
                try {
                    //set post value
                    post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                try {

                    //Execute post
                    HttpResponse httpResponse = client.execute(post);
                    //get post's entity
                    HttpEntity entity = httpResponse.getEntity();

                    String response = EntityUtils.toString(entity);

                    //Set message to send to the handler
                    Message msg = handler.obtainMessage();
                    Bundle bundle = new Bundle();
                    bundle.putString("response", response);
                    bundle.putString("workerID", workerID);
                    bundle.putString("reportID", reportID);
                    bundle.putString("status", status);
                    msg.setData(bundle);
                    handler.sendMessage(msg);

                } catch (IOException e) {
                    //Set message to send to the handler
                    Message msg = handler.obtainMessage();
                    Bundle bundle = new Bundle();
                    bundle.putString("response", "no connection");
                    bundle.putString("workerID", workerID);
                    bundle.putString("reportID", reportID);
                    bundle.putString("status", status);
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                }
            }
        });

        //start new thread
        thread.start();


    }


}
