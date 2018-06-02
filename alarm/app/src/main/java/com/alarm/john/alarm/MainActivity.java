package com.alarm.john.alarm;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.icu.text.DateFormat;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import java.util.Calendar;
import java.util.Date;
import android.os.Vibrator;
import android.content.Context;

import android.net.Uri;
public class MainActivity extends AppCompatActivity {

    public boolean stateAlarma = false;
    public String  hours   = "00";
    public String  minutes = "00";
    public int     day     = 0;
    public MainActivity el = this;

    private Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        BroadcastReceiver mReceiver = new ReceiverScreen();
        registerReceiver(mReceiver, filter);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
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

        /*RadioGroup radioGroup = this.findViewById(R.id.State);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                RadioButton rb=(RadioButton)findViewById(checkedId);
                //textViewChoice.setText("You Selected " + rb.getText());
                //Toast.makeText(getApplicationContext(), rb.getText(), Toast.LENGTH_SHORT).show();
                Log.d("Test",rb.getText()+"");
            }
        });*/

    }
/*
    @Override
    protected void onPause() {
        // when the screen is about to turn off
        if (ScreenReceiver.wasScreenOn) {
            // this is the case when onPause() is called by the system due to a screen state change
            System.out.println("SCREEN TURNED OFF");

        } else {
            // this is when onPause() is called when the screen state has not changed
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        // only when screen turns on
        if (!ScreenReceiver.wasScreenOn) {
            // this is when onResume() is called due to a screen state change
            System.out.println("SCREEN TURNED ON");
        } else {
            // this is when onResume() is called when the screen state has not changed
        }
        super.onResume();
    }
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_POWER) {
            Log.i("", "Dispath event power");
            Intent closeDialog = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            sendBroadcast(closeDialog);
            return true;
        }

        return super.dispatchKeyEvent(event);
    }*/
public boolean dispatchKeyEvent(KeyEvent event) {
    if (event.getKeyCode() == KeyEvent.KEYCODE_POWER) {
        Log.i("", "Dispath event power");
        Intent closeDialog = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        sendBroadcast(closeDialog);
        return true;
    }

    return super.dispatchKeyEvent(event);
}
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //if (keyCode == KeyEvent.KEYCODE_BACK) {
         //   return true;
        //}
        //return super.onKeyDown(keyCode, event);
        Log.d("Test",keyCode+"");
        return true;
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

}
