package com.alarm.john.alarm.kioskMode;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.alarm.john.alarm.MainActivity;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent myIntent = new Intent(context, MainActivity.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        myIntent.putExtra("txt", "the string value");
        context.sendBroadcast(myIntent);
    }
}