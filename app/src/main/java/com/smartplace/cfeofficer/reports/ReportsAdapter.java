package com.smartplace.cfeofficer.reports;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartplace.cfeofficer.R;
import com.smartplace.cfeofficer.data.Report;
import com.smartplace.cfeofficer.utilities.Constants;

import java.util.ArrayList;

/**
 * Created by Roberto on 20/10/2014.
 */
public class ReportsAdapter extends BaseAdapter {

    /**
     * Objects to reference xml
     */
    public ArrayList<Report> mArrayData;
    private Context mContext;
    private LayoutInflater mLayoutInflater;


    public ReportsAdapter(Context context, ArrayList<Report> arrayData) {

        //save references to local variables
        mArrayData = arrayData;
        mLayoutInflater = LayoutInflater.from(context);
        mContext =context;
    }
    @Override
    public int getCount() {
        return mArrayData.size();
    }

    @Override
    public Object getItem(int i) {
        return mArrayData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Holder holder;
        View v = view;
        if (v == null) {
            holder = new Holder();
            v = mLayoutInflater.inflate(R.layout.list_item_report, null);
            holder.imgType = (ImageView) v.findViewById(R.id.image_type);
            holder.txtType = (TextView) v.findViewById(R.id.txt_type);
            holder.txtID = (TextView)v.findViewById(R.id.txt_id);
            holder.txtCreationDate = (TextView)v.findViewById(R.id.txt_creation_date);
            holder.txtCreationHour = (TextView)v.findViewById(R.id.txt_creation_hour);
            holder.txtStatus = (TextView)v.findViewById(R.id.txt_status);
            holder.imgStatus = (ImageView)v.findViewById(R.id.image_status);
            v.setTag(holder);
        }else {
            holder = (Holder) v.getTag();
        }

        //set typeface
        Typeface font= Typeface.createFromAsset(mContext.getAssets(), "fonts/OpenSansLight.ttf");
        holder.txtID.setTypeface(font);
        holder.txtCreationDate.setTypeface(font);
        holder.txtCreationHour.setTypeface(font);
        holder.txtStatus.setTypeface(font);
        holder.txtType.setTypeface(font);

        //set info
        String[]creationDateTime = mArrayData.get(i).getCreationDate().split(" ");
        String[]creationDate = creationDateTime[0].split("-");
        String[]creationHour = creationDateTime[1].split(":");
        String[] months = mContext.getResources().getStringArray(R.array.months_array);
        holder.txtID.setText(mContext.getString(R.string.report_number)+mArrayData.get(i).getReportID());
        holder.txtCreationDate.setText(creationDate[2] + " "+months[Integer.valueOf(creationDate[1])-1]+ " " + creationDate[0]);
        holder.txtCreationHour.setText(creationHour[0]+":"+creationHour[1]);
        String[] statusArray = mContext.getResources().getStringArray(R.array.status_array);
        holder.txtStatus.setText(statusArray[Integer.valueOf(mArrayData.get(i).getStatus())-1]);
        int[] images = {R.drawable.ic_status_pending,R.drawable.ic_status_in_progress , R.drawable.ic_status_solved,R.drawable.ic_status_closed};
        holder.imgStatus.setImageResource(images[Integer.valueOf(mArrayData.get(i).getStatus())-1]);
        if(mArrayData.get(i).getType().equals(String.valueOf(Constants.TYPE_FAIL))){
            int resourceID= mContext.getResources().getIdentifier(
                    "ic_fail_subtype_" + String.valueOf(mArrayData.get(i).getSubType()),
                    "drawable",mContext.getPackageName());
            try{
                holder.imgType.setImageResource(resourceID);
            }catch (Resources.NotFoundException e){
                holder.imgType.setImageResource(R.drawable.ic_fail_subtype_1);
            }
            String[] subtypeArray = mContext.getResources().getStringArray(R.array.subtype_fail_array);
            holder.txtType.setText(subtypeArray[Integer.valueOf(mArrayData.get(i).getSubType())-1]);
        }else{
            int resourceID= mContext.getResources().getIdentifier(
                    "ic_issue_subtype_" + String.valueOf(mArrayData.get(i).getSubType()),
                    "drawable",mContext.getPackageName());
            try{
                holder.imgType.setImageResource(resourceID);
            }catch (Resources.NotFoundException e){
                holder.imgType.setImageResource(R.drawable.ic_issue_subtype_1);
            }
            String[] subtypeArray = mContext.getResources().getStringArray(R.array.subtype_issue_array);
            holder.txtType.setText(subtypeArray[Integer.valueOf(mArrayData.get(i).getSubType())-1]);
        }
        return v;
    }

     static class Holder{
         ImageView imgType;
         TextView txtType;
        TextView txtID;
        TextView txtCreationDate;
        TextView txtCreationHour;
         TextView txtStatus;
         ImageView imgStatus;

    }
}
