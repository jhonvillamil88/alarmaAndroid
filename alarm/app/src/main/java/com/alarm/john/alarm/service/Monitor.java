package com.alarm.john.alarm.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.Vibrator;
import android.util.Log;
import android.widget.RadioGroup;

import com.alarm.john.alarm.MainActivity;
import com.alarm.john.alarm.R;
import com.alarm.john.alarm.TimePickerFragment;
import com.alarm.john.alarm.kioskMode.PrefUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Monitor extends Service {

    private Thread t = null;
    private static final long INTERVAL = TimeUnit.SECONDS.toMillis(2);
    public static boolean running = true;
    public static String  hours   = "00";
    public static String  minutes = "00";
    public static int     day     = 0;
    private Vibrator vibrator;




    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("Monitor", "Starting service 'Monitor'");

        // start a thread that periodically checks if your app is in the foreground
        t = new Thread(new Runnable() {
            @Override
            public void run() {
                do {
                    Log.d("Service","Running");
                    try {
                        Thread.sleep(INTERVAL);
                        Log.d("Try",hours+":"+minutes+ " => "+day);
                        if(comparedDate()){
                            activeAlarma();
                        }

                    } catch (InterruptedException e) {
                        Log.i("Service", "Thread interrupted: 'Monitor'");
                    }
                }while(running);
                stopSelf();
            }
        });

        t.start();
        return Service.START_NOT_STICKY;
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    private Boolean comparedDate(){
        Calendar calendar = Calendar.getInstance();
        int dayCurrent = calendar.get(Calendar.DAY_OF_WEEK);
        Log.d("Day",dayCurrent+"");
        if(day!=dayCurrent)return false;

        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        String currentDateandTime = format.format(new Date());
        try{
            Date dateAlarm = format.parse(hours+":"+minutes);
            Date dateSystem = format.parse(currentDateandTime);
            if (dateSystem.getTime() > dateAlarm.getTime()) {
                return true;
            }
        }catch(Exception e ){

        }
        return false;
    }
    private void activeAlarma(){
        Log.d("Test","Se cumplio el alarma");

        MediaPlayer ring= MediaPlayer.create(this,R.raw.alarma);
        ring.setLooping(true);
        ring.start();

        long[] pattern = {0, 100, 1000};
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(pattern, 0);

        PrefUtils.setKioskModeActive(true, getApplicationContext());
        running = false;
    }
}
