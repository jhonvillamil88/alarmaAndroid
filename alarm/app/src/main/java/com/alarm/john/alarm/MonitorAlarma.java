package com.alarm.john.alarm;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;

public class MonitorAlarma extends AsyncTask< Void, Void, Void> {


    @Override
    public  Void doInBackground(Void... voids) {
        while(true){
            SystemClock.sleep(1000);
            Log.d("run", "running alarm");
        }
        //return null;
    }
}