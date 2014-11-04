package com.smartplace.cfeofficer.reports;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.smartplace.cfeofficer.R;
import com.smartplace.cfeofficer.coverflow.FancyCoverFlow;
import com.smartplace.cfeofficer.coverflow.FancyCoverFlowStatusAdapter;
import com.smartplace.cfeofficer.data.Report;
import com.smartplace.cfeofficer.server.LoginResponse;
import com.smartplace.cfeofficer.services.MemoryServices;
import com.smartplace.cfeofficer.services.WebServices;
import com.smartplace.cfeofficer.utilities.Constants;
import com.smartplace.cfeofficer.utilities.Miscellaneous;
import com.smartplace.cfeofficer.utilities.TransparentProgressDialog;

import java.io.File;

public class ReportDetailedActivity extends Activity {

    private DisplayImageOptions defaultOptions;
    private AlertDialog mStatusDialog;
    private TransparentProgressDialog mLoadingDialog;
    private Report mReport;
    private TextView mTxtStatus;
    private ImageView mImageStatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_detailed);
        TextView txtReportID = (TextView)findViewById(R.id.txt_report_id);
        TextView txtCreationDate = (TextView)findViewById(R.id.txt_creation_date);
        TextView txtCreationHour = (TextView)findViewById(R.id.txt_creation_hour);
        ImageView imageMap = (ImageView)findViewById(R.id.image_location);
        ImageView imageAttachment = (ImageView)findViewById(R.id.image_attachment);
        TextView txtComment = (TextView)findViewById(R.id.txt_comment);
        TextView txtPublicComments = (TextView)findViewById(R.id.txt_public_comments);
        TextView txtPrivateComments = (TextView)findViewById(R.id.txt_private_comments);
        ImageButton imgBtnPublicComments = (ImageButton)findViewById(R.id.img_btn_public_comments);
        ImageButton imgBtnPrivateComments = (ImageButton)findViewById(R.id.img_btn_private_comments);
        mTxtStatus = (TextView)findViewById(R.id.txt_status);
        mImageStatus = (ImageView)findViewById(R.id.image_status);
        Button btnUpdateStatus = (Button) findViewById(R.id.btn_update_status);
        ImageView imageType = (ImageView)findViewById(R.id.image_type);
        TextView txtType = (TextView) findViewById(R.id.txt_type);

        mReport = new Gson().fromJson(getIntent().getStringExtra("report"),Report.class);

        //set typeface
        Typeface font= Typeface.createFromAsset(getAssets(), "fonts/OpenSansLight.ttf");
        txtCreationDate.setTypeface(font);
        txtCreationHour.setTypeface(font);
        txtComment.setTypeface(font);
        txtPublicComments.setTypeface(font);
        txtPrivateComments.setTypeface(font);
        mTxtStatus.setTypeface(font);
        btnUpdateStatus.setTypeface(font);
        txtType.setTypeface(font);
        font= Typeface.createFromAsset(getAssets(), "fonts/OpenSansSemibold.ttf");
        txtReportID.setTypeface(font);
        ((TextView)findViewById(R.id.txt_header_creation_date)).setTypeface(font);
        ((TextView)findViewById(R.id.txt_header_location)).setTypeface(font);
        ((TextView)findViewById(R.id.txt_header_comment)).setTypeface(font);
        ((TextView)findViewById(R.id.txt_header_comments)).setTypeface(font);
        ((TextView)findViewById(R.id.txt_header_status)).setTypeface(font);
        ((TextView)findViewById(R.id.txt_header_attachment)).setTypeface(font);

        //set values
        String[]creationDateTime = mReport.getCreationDate().split(" ");
        String[]creationDate = creationDateTime[0].split("-");
        String[]creationHour = creationDateTime[1].split(":");
        String[] months = getResources().getStringArray(R.array.months_full_array);
        txtReportID.setText(getString(R.string.report_number) + mReport.getReportID());
        txtCreationDate.setText(creationDate[2] + " "+months[Integer.valueOf(creationDate[1])-1]+ " " + creationDate[0]);
        txtCreationHour.setText(creationHour[0]+":"+creationHour[1]);
        txtComment.setText(mReport.getDesc());
        txtPublicComments.setText(getString(R.string.public_comments) + " " + mReport.getPublicComments().size());
        txtPrivateComments.setText(getString(R.string.private_comments)+ " "+mReport.getPrivateComments().size());

        int[] images = {R.drawable.ic_status_pending,R.drawable.ic_status_in_progress , R.drawable.ic_status_solved,R.drawable.ic_status_closed};
        mImageStatus.setImageResource(images[Integer.valueOf(mReport.getStatus()) - 1]);

        String[] status = getResources().getStringArray(R.array.status_array);
        mTxtStatus.setText(status[Integer.valueOf(mReport.getStatus())-1]);

        if(mReport.getType().equals(String.valueOf(Constants.TYPE_FAIL))){
            int resourceID= getResources().getIdentifier(
                    "ic_fail_subtype_" + String.valueOf(mReport.getSubType()),
                    "drawable",getPackageName());
            try{
                imageType.setImageResource(resourceID);
            }catch (Resources.NotFoundException e){
                imageType.setImageResource(R.drawable.ic_fail_subtype_1);
            }
            String[] subtypeArray = getResources().getStringArray(R.array.subtype_fail_array);
            txtType.setText(subtypeArray[Integer.valueOf(mReport.getSubType())-1]);
        }else{
            int resourceID= getResources().getIdentifier(
                    "ic_issue_subtype_" + String.valueOf(mReport.getSubType()),
                    "drawable",getPackageName());
            try{
                imageType.setImageResource(resourceID);
            }catch (Resources.NotFoundException e){
                imageType.setImageResource(R.drawable.ic_issue_subtype_1);
            }
            String[] subtypeArray = getResources().getStringArray(R.array.subtype_issue_array);
            txtType.setText(subtypeArray[Integer.valueOf(mReport.getSubType())-1]);
        }
        //configure image loader
        defaultOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_default_map)
                .resetViewBeforeLoading(false).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getBaseContext())
                .defaultDisplayImageOptions(defaultOptions).build();
        ImageLoader.getInstance().init(config);

        String imagePath = Constants.MAPS_STATIC_IMAGE_PREFIX+mReport.getLat()+","+mReport.getLng();
        //Check for map in eeprom otherwise download it
        File Dir = new File (Constants.IMAGES_PATH);
        String filename = mReport.getReportID() +".png";
        File file = new File (Dir, filename);
        if(file.exists()){
           imageMap.setImageBitmap(
                    BitmapFactory.decodeFile(Constants.IMAGES_PATH + File.separator + filename));
        }else {
            ImageLoader.getInstance().displayImage(imagePath
                    ,imageMap, defaultOptions,
                    new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String s, View view) {
                        }

                        @Override
                        public void onLoadingFailed(String s, View view, FailReason failReason) {
                        }

                        @Override
                        public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                            //member id is the last string on the uri
                            Miscellaneous.saveImage(bitmap, Constants.IMAGES_PATH, mReport.getReportID());
                        }

                        @Override
                        public void onLoadingCancelled(String s, View view) {
                        }
                    });
        }

//Check for map in eeprom otherwise download it
        Dir = new File (Constants.IMAGES_PATH);
        filename = mReport.getReportID() +"attach.png";
        file = new File (Dir, filename);
        if(file.exists()){
            imageAttachment.setImageBitmap(
                    BitmapFactory.decodeFile(Constants.IMAGES_PATH + File.separator + filename));
        }else {
            String imageAttach = Constants.ATTACH_IMAGE_PREFIX + mReport.getReportID() + ".jpeg";
            ImageLoader.getInstance().displayImage(imageAttach
                    , imageAttachment, defaultOptions,
                    new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String s, View view) {
                        }

                        @Override
                        public void onLoadingFailed(String s, View view, FailReason failReason) {
                        }

                        @Override
                        public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                            //member id is the last string on the uri
                            Miscellaneous.saveImage(bitmap, Constants.IMAGES_PATH, mReport.getReportID() + "attach");
                        }

                        @Override
                        public void onLoadingCancelled(String s, View view) {
                        }
                    });
        }
        //set action bar
        getActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.cfe_green)));
        getActionBar().setDisplayHomeAsUpEnabled(true);

        //set on click listeners
        imageMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(),ReportMapActivity.class);
                intent.putExtra("report",new Gson().toJson(mReport));
                startActivity(intent);
            }
        });
        imgBtnPublicComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(),ReportCommentsActivity.class);
                intent.putExtra("report",new Gson().toJson(mReport));
                intent.putExtra("commentType",Constants.TYPE_COMMENT_PUBLIC);
                startActivity(intent);
            }
        });
        imgBtnPrivateComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(),ReportCommentsActivity.class);
                intent.putExtra("report",new Gson().toJson(mReport));
                intent.putExtra("commentType",Constants.TYPE_COMMENT_PRIVATE);
                startActivity(intent);

            }
        });
        btnUpdateStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showStatusDialog();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.report_detailed, menu);
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
    public void showStatusDialog()
    {


        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ReportDetailedActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View promptsView = inflater.inflate(R.layout.prompt_status_dialog, null);
        final FancyCoverFlow fancyCoverFlow = (FancyCoverFlow) promptsView.findViewById(R.id.fancyCoverFlow);
        final TextView txtStatusType = (TextView) promptsView.findViewById(R.id.txt_status_type);
        alertDialogBuilder.setView(promptsView);


        final String[] statusTypes= getResources().getStringArray(R.array.status_array);

        fancyCoverFlow.setAdapter(new FancyCoverFlowStatusAdapter());
        fancyCoverFlow.setUnselectedAlpha(1.0f);
        fancyCoverFlow.setUnselectedSaturation(0.0f);
        fancyCoverFlow.setUnselectedScale(0.5f);
        fancyCoverFlow.setSpacing(10);
        fancyCoverFlow.setMaxRotation(0);
        fancyCoverFlow.setSelection(1);
        fancyCoverFlow.setScaleDownGravity(1f);
        fancyCoverFlow.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                txtStatusType.setText(statusTypes[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        fancyCoverFlow.setActionDistance(FancyCoverFlow.ACTION_DISTANCE_AUTO);

        Typeface titleFont = Typeface.createFromAsset(getAssets(), "fonts/OpenSansLight.ttf");
        ((TextView)promptsView.findViewById(R.id.txt_header_status)).setTypeface(titleFont);
        ((TextView)promptsView.findViewById(R.id.txt_status_instructions )).setTypeface(titleFont);
        txtStatusType.setTypeface(titleFont);

        // set dialog message
        alertDialogBuilder
                .setNegativeButton(getString(R.string.options_cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        }
                )
                .setPositiveButton(getString(R.string.confirm),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                mLoadingDialog = new TransparentProgressDialog(ReportDetailedActivity.this, R.drawable.ic_loading);
                                mLoadingDialog.show();
                                mLoadingDialog.tv.setText(getString(R.string.updating_status));
                                WebServices.setReportStatus(MemoryServices.getUserID(getBaseContext()),
                                        mReport.getReportID(),String.valueOf(fancyCoverFlow.getSelectedItemPosition()+1),mSetReportStatusHandler);
                            }

                        }
                );
        // create alert dialog
        mStatusDialog= alertDialogBuilder.create();
        // show it
        mStatusDialog.show();
    }

    private Handler mSetReportStatusHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            String response = bundle != null ? bundle.getString("response") : "";
            String status = bundle != null ? bundle.getString("status") : "";

            if (mLoadingDialog.isShowing()) {

                mLoadingDialog.dismiss();
                //mLoadingDialog.dismiss();
                if ((response.equals("")) || response.equals("no connection")) {

                    //There is an issue with the response, no data received
                }else{
                    try{
                        LoginResponse loginResponse = new Gson().fromJson(response,LoginResponse.class);
                        if(loginResponse.getRequestStatus().equals("NOK1")) {
                            Toast.makeText(getBaseContext(), getResources().getString(R.string.invalid_parameters), Toast.LENGTH_SHORT).show();

                        }else if(loginResponse.getRequestStatus().equals("OK")){
                            mStatusDialog.dismiss();
                            String[] statusArray = getResources().getStringArray(R.array.status_array);
                            mTxtStatus.setText(statusArray[Integer.valueOf(status)-1]);
                            int[] images = {R.drawable.ic_status_pending,R.drawable.ic_status_in_progress , R.drawable.ic_status_solved,R.drawable.ic_status_closed};
                            mImageStatus.setImageResource(images[Integer.valueOf(status)-1]);
                        }
                    }catch (JsonSyntaxException w){

                    }
                }
            }
        }
    };

}
