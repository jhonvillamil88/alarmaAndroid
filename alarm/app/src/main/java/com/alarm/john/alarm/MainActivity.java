package com.alarm.john.alarm;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.support.v4.app.DialogFragment;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Button;
import android.widget.RadioGroup;
import android.util.Log;
import	java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.os.Vibrator;
import android.content.Context;
import android.os.PowerManager;

import com.alarm.john.alarm.kioskMode.PrefUtils;
import com.alarm.john.alarm.kioskMode.customViewGroup;
import com.alarm.john.alarm.service.Monitor;

public class MainActivity extends AppCompatActivity {


    protected PowerManager.WakeLock mWakeLock;


    Intent serviceIntent;
    public boolean stateAlarma = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

        setContentView(R.layout.activity_main);

        //Desactivo el kiosk mode al iniciar
        PrefUtils.setKioskModeActive(false, getApplicationContext());


        startService(new Intent(this, Monitor.class));

        /*WindowManager manager = ((WindowManager) getApplicationContext()
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
        manager.addView(view, localLayoutParams);*/



        /*RadioGroup rb = (RadioGroup) findViewById(R.id.myRadioGroup);
        rb.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Monitor.day = checkedId;
            }

        });*/

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

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        // nothing to do here
        if (!PrefUtils.isKioskModeActive(getApplicationContext())) {
            return super.dispatchKeyEvent(event);
        } else {
            return true;
        }
    }

    @Override
    public void onAttachedToWindow() {
        //this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
        super.onAttachedToWindow();
    }

    public void setTime (View v){

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

    public void onClickDay(View v) {
        final int id = v.getId();
        switch (id) {
            case R.id.day1:
                // your code for button1 here
                Log.d("Test","Lunes");
                break;
            case R.id.day2:
                // your code for button2 here
                Log.d("Test","Martes");
                break;
            // even more buttons here
        }
    }
}
