package com.alarm.john.alarm.service;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.alarm.john.alarm.AlarmActivity;
import com.alarm.john.alarm.LoadPreferences;
import com.alarm.john.alarm.MainActivity;

public class AlarmReceiver extends BroadcastReceiver
{
    LoadPreferences Load = new LoadPreferences();

    @Override
    public void onReceive(Context context, Intent intent)
    {

        Intent theIntent = new Intent(context, MainActivity.class);
        theIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        theIntent.putExtra("alarm", true);
        context.startActivity(theIntent);

    }
}