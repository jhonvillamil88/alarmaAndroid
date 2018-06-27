package com.alarm.john.alarm;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.support.v4.app.DialogFragment;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.util.Log;

import android.content.Context;
import android.os.PowerManager;
import android.widget.Toast;

import com.alarm.john.alarm.kioskMode.PrefUtils;
import com.alarm.john.alarm.service.AlarmReceiver;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {


    protected PowerManager.WakeLock mWakeLock;



    Intent serviceIntent;
    public static boolean stateAlarma = false;
    public static boolean alarmaIsActive = false;
    LoadPreferences Load = new LoadPreferences();
    private Vibrator vibrator;
    private MediaPlayer ring;
    SharedPreferences prefs;


    /*static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

        setContentView(R.layout.activity_main);

        LoadPreferences.start(getApplicationContext(),this);
        prefs =   getSharedPreferences("preferences",Context.MODE_PRIVATE);


        Bundle extras = this.getIntent().getExtras();
        Intent intent = getIntent();
        String newString;
        if(extras != null) {
            if(extras.getBoolean("alarm")){
                activeAlarm();
            }
        }
    }
    public  void scheduleAlarm(String hour){

            Log.i("Test","Service");

            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String dateNow = sdf.format(calendar.getTime());

            Log.i("alarma",dateNow+" "+hour);

            Long time = milliseconds(dateNow+" "+hour);

            Intent intentAlarm = new Intent(this, AlarmReceiver.class);

            // create the object
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

            //set the alarm for particular time
            alarmManager.set(AlarmManager.RTC_WAKEUP,time, PendingIntent.getBroadcast(this,1,  intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));

    }

    public long milliseconds(String date){
        //String date_ = date;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try{
            Date mDate = sdf.parse(date);
            long timeInMilliseconds = mDate.getTime();
            return timeInMilliseconds;
        }
        catch (ParseException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return 0;
    }


    public void activeAlarm(){


        ring = MediaPlayer.create(this,R.raw.alarma);
        ring.setLooping(true);
        ring.start();

        long[] pattern = {0, 100, 1000};
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(pattern, 0);

        PrefUtils.setKioskModeActive(true, getApplicationContext());

        alarmaIsActive = true;
        showAddItemDialog(MainActivity.this);
    }
    public void desactivedAlarma(){
        Log.i("Termino","Se cancela el alarma");
        PrefUtils.setKioskModeActive(false, getApplicationContext());
        ring.stop();
        vibrator.cancel();
    }

    private void showAddItemDialog(Context c) {
        final EditText taskEditText = new EditText(c);
        AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle("Responde para cancelar el alarma")
                .setMessage("¿Lana sube lana baja?")
                .setView(taskEditText)
                .setPositiveButton("Responder", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String task = String.valueOf(taskEditText.getText());
                        Log.i("Test",task);
                        if( task.equals("la navaja")){
                            desactivedAlarma();
                        }
                    }
                })
                .create();
        dialog.show();
    }
    public void setTime (View v){

        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public void setState (View v){

        if(alarmaIsActive){
            showAddItemDialog(MainActivity.this);
            return;
        }
        stateAlarma = !stateAlarma;
        TextView stateText =this.findViewById(R.id.State);
        stateText.setText((stateAlarma)?"ACTIVO":"INACTIVO");

        Button testButton = (Button) findViewById(R.id.setState);
        testButton.setText((stateAlarma)?"DESACTIVAR":"ACTIVAR");


        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("state", stateAlarma);
        editor.commit();


        if(stateAlarma){
            String timeAlarm = prefs.getString("time", "00:00");
            scheduleAlarm(timeAlarm);
        }

        Toast.makeText(this, "El alarma fue "+((stateAlarma)?"Activada":"Desactivada"), Toast.LENGTH_LONG).show();
    }

    public void onClickDay(View v) {
        final int id = v.getId();
        CheckBox action = (CheckBox)this.findViewById(id);
        boolean state = action.isChecked();
        int day = -1;

        switch (id) {
            case R.id.day1:
                // your code for button2 here
                Log.d("Test","Domingo");
                day = 1;
                break;
            case R.id.day2:
                // your code for button1 here
                Log.d("Test","Lunes");
                day = 2;
                break;
            case R.id.day3:
                // your code for button2 here
                Log.d("Test","Martes");
                day = 3;
                break;
            case R.id.day4:
                // your code for button2 here
                Log.d("Test","Miercoles");
                day = 4;
                break;
            case R.id.day5:
                // your code for button2 here
                Log.d("Test","Jueves");
                day = 5;
                break;
            case R.id.day6:
                // your code for button2 here
                Log.d("Test","Viernes");
                day = 6;
                break;
            case R.id.day7:
                // your code for button2 here
                Log.d("Test","Sabado");
                day = 7;
                break;

            // even more buttons here
        }


        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("day"+day, state);
        editor.commit();
        Log.d("Preferences","Saved DAY OK");
    }

    /*
    *   Funciones para el del kiosk mode
    * */
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
}
