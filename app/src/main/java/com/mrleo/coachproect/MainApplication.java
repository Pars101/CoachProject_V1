package com.mrleo.coachproect;

import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;

public class MainApplication extends Application
{
    private static Context context;
    private static ContentResolver contentResolver;

    @Override
    public void onCreate()
    {
        super.onCreate();
        context = getApplicationContext();
        contentResolver = getContentResolver();
    }

    public static Context getAppContext()
    {
        return context;
    }

    public static ContentResolver getAppContentResolver()
    {
        return contentResolver;
    }
}
