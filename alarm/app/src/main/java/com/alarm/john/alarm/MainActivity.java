package com.alarm.john.alarm;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.icu.text.DateFormat;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.support.v4.app.DialogFragment;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RadioButton;
import 	android.os.SystemClock;
import android.util.Log;
import	java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.os.Vibrator;
import android.content.Context;
import android.os.PowerManager;

import android.net.Uri;
public class MainActivity extends AppCompatActivity {

    public boolean stateAlarma = false;
    public String  hours   = "00";
    public String  minutes = "00";
    public int     day     = 0;
    protected PowerManager.WakeLock mWakeLock;


    private Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

        setContentView(R.layout.activity_main);

        PrefUtils.setKioskModeActive(true, getApplicationContext());

        WindowManager manager = ((WindowManager) getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE));
        WindowManager.LayoutParams localLayoutParams = new WindowManager.LayoutParams();
        localLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        localLayoutParams.gravity = Gravity.TOP;
        localLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|
                // this is to enable the notification to recieve touch events
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                // Draws over status bar
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        localLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        localLayoutParams.height = (int) (50 * getResources()
                .getDisplayMetrics().scaledDensity);
        localLayoutParams.format = PixelFormat.TRANSPARENT;
        customViewGroup view = new customViewGroup(this);
        manager.addView(view, localLayoutParams);


        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        new Thread(new Runnable(){
            public void run() {
                // do something here
                while(true){
                    SystemClock.sleep(1000);
                    if(!stateAlarma)continue;
                    Log.d("run", "running alarm");

                    hours = TimePickerFragment.getHour()+"";
                    minutes = TimePickerFragment.getMinute()+"";

                    if(comparetDate(hours+":"+minutes)){
                        Log.d("run","Se cumplio el alarma");

                        Log.v("", "Initializing sounds...");



                        MediaPlayer ring= MediaPlayer.create(MainActivity.this,R.raw.alarma);
                        ring.setLooping(true);
                        ring.start();

                        long[] pattern = {0, 100, 1000};
                        vibrator.vibrate(pattern, 0);
                        stateAlarma = false;
                        //getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
                        //resetAlarma();

                       /* new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Alarma")
                                .setMessage("Holaaa mi amor es hora de levantarte")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        dialog.cancel();
                                    }
                                }).show();*/

                    }
                }
            }
        }).start();


    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(!hasFocus) {
            // Close every kind of system dialog
            Intent closeDialog = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            sendBroadcast(closeDialog);
        }
    }

    private final List blockedKeys = new ArrayList(Arrays.asList(KeyEvent.KEYCODE_VOLUME_DOWN, KeyEvent.KEYCODE_VOLUME_UP));
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (blockedKeys.contains(event.getKeyCode())) {
            return true;
        } else {
            return super.dispatchKeyEvent(event);
        }
    }

    @Override
    public void onAttachedToWindow() {
        //this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
        super.onAttachedToWindow();
    }

    public void setTime (View v){
        /*new AlertDialog.Builder(MainActivity.this)
                .setTitle("Dialog Simple")
                .setMessage("Gracias por visitar javaheros.blogspot.com")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                }).show();*/
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public void setState (View v){

        stateAlarma = !stateAlarma;
        TextView stateText =this.findViewById(R.id.State);
        stateText.setText((stateAlarma)?"ACTIVO":"INACTIVO");

        Button testButton = (Button) findViewById(R.id.setState);
        testButton.setText((stateAlarma)?"DESACTIVAR":"ACTIVAR");
    }

    public boolean comparetDate(String timeAlarm){

        RadioGroup radioGroup = this.findViewById(R.id.myRadioGroup);
        int selectDay = radioGroup.getCheckedRadioButtonId();
        ++selectDay;
        Log.d("Test","Day select "+selectDay);

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        Log.d("Day",day+"");
        if(selectDay!=day)return false;

        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        String currentDateandTime = format.format(new Date());
        try{
            Date dateAlarm = format.parse(timeAlarm);
            Date dateSystem = format.parse(currentDateandTime);
            if (dateSystem.getTime() > dateAlarm.getTime()) {
                return true;
            }
        }catch(Exception e ){

        }
        return false;
    }

    public void setDay(View v){
        Log.d("Test",v.toString());
    }

    public void resetAlarma(){
        TextView stateText =this.findViewById(R.id.State);
        stateText.setText("INACTIVO");
        stateAlarma = false;
    }

    @Override
    public void onBackPressed() {
        // nothing to do here
    }

}
