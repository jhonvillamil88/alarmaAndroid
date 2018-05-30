package com.alarm.john.alarm;

import android.icu.text.DateFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.support.v4.app.DialogFragment;
import android.widget.TextView;
import android.widget.Button;
import 	android.os.SystemClock;
import android.util.Log;
import	java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import android.os.Vibrator;
import android.content.Context;


public class MainActivity extends AppCompatActivity {

    public boolean stateAlarma = false;
    public String  hours   = "00";
    public String  minutes = "00";

    private Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);


        new Thread(new Runnable(){
            public void run() {
                // do something here
                while(true){
                    SystemClock.sleep(1000);
                    if(!stateAlarma)continue;
                    Log.d("run", "running alarm");

                    if(comparetDate(TimePickerFragment.getHour()+":"+TimePickerFragment.getMinute())){
                        Log.d("run","Se cumplio el alarma");

                        vibrator.vibrate(1000000);
                    }
                }
            }
        }).start();
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

}
