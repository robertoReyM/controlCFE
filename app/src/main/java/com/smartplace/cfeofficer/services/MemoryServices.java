package com.smartplace.cfeofficer.services;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Roberto on 20/10/2014.
 */
public class MemoryServices {
    public static void setUserID(Context context,String teamID)
    {
        SharedPreferences mySharedPreferences =
                context.getSharedPreferences("USER",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString("ID", teamID);
        editor.apply();
    }
    public static String getUserID(Context context)
    {
        SharedPreferences mySharedPreferences = context.getSharedPreferences("USER", Context.MODE_PRIVATE);
        return mySharedPreferences.getString("ID", "");
    }
    public static void setWorkerName(Context context,String teamName)
    {
        SharedPreferences mySharedPreferences =
                context.getSharedPreferences("USER",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString("NAME", teamName);
        editor.apply();
    }
    public static String getWorkerName(Context context)
    {
        SharedPreferences mySharedPreferences = context.getSharedPreferences("USER", Context.MODE_PRIVATE);
        return mySharedPreferences.getString("NAME", "");
    }
    public static void setWorkerReports(Context context,String reports)
    {
        SharedPreferences mySharedPreferences =
                context.getSharedPreferences("USER",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString("REPORTS", reports);
        editor.apply();
    }
    public static String getWorkerReports(Context context)
    {
        SharedPreferences mySharedPreferences = context.getSharedPreferences("USER", Context.MODE_PRIVATE);
        return mySharedPreferences.getString("REPORTS", "{reports:[]}");
    }
    public static boolean isFirstTime(Context context)
    {
        SharedPreferences mySharedPreferences = context.getSharedPreferences("ADMIN", Context.MODE_PRIVATE);
        return mySharedPreferences.getBoolean("FIRST_TIME",true);
    }
    public static void setFirstTime(Context context,boolean firstTime)
    {
        SharedPreferences mySharedPreferences =
                context.getSharedPreferences("ADMIN",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putBoolean("FIRST_TIME", firstTime);
        editor.apply();
    }
    public static String getPushToken(Context context)
    {
        SharedPreferences mySharedPreferences = context.getSharedPreferences("ADMIN", Context.MODE_PRIVATE);
        return mySharedPreferences.getString("PUSH_TOKEN", "");
    }
    public static void setPushToken(Context context,String pushToken)
    {
        SharedPreferences mySharedPreferences =
                context.getSharedPreferences("ADMIN",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString("PUSH_TOKEN", pushToken);
        editor.apply();
    }
}
