package com.smartplace.cfeofficer.reports;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.smartplace.cfeofficer.R;
import com.smartplace.cfeofficer.data.Comment;
import com.smartplace.cfeofficer.data.Report;

import java.util.ArrayList;

/**
 * Created by Roberto on 20/10/2014.
 */
public class ReportCommentsAdapter extends BaseAdapter {

    /**
     * Objects to reference xml
     */
    public ArrayList<Comment> mArrayData;
    private Context mContext;
    private LayoutInflater mLayoutInflater;


    public ReportCommentsAdapter(Context context, ArrayList<Comment> arrayData) {

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
            v = mLayoutInflater.inflate(R.layout.list_item_comment, null);
            holder.txtName = (TextView)v.findViewById(R.id.txt_name);
            holder.txtComment = (TextView)v.findViewById(R.id.txt_comment);
            v.setTag(holder);
        }else {
            holder = (Holder) v.getTag();
        }

        //set typeface
        Typeface font= Typeface.createFromAsset(mContext.getAssets(), "fonts/OpenSansSemibold.ttf");
        holder.txtName.setTypeface(font);
        font= Typeface.createFromAsset(mContext.getAssets(), "fonts/OpenSansLight.ttf");
        holder.txtComment.setTypeface(font);
        //set info
        holder.txtName.setText(mArrayData.get(i).getWorkerName() + " comentó:");
        holder.txtComment.setText(mArrayData.get(i).getComment());

        return v;
    }

     static class Holder{
        TextView txtName;
        TextView txtComment;
    }
}
