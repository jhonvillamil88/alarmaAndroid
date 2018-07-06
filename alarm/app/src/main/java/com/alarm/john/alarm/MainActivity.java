package com.alarm.john.alarm;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
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

public  class MainActivity extends AppCompatActivity {


    protected PowerManager.WakeLock mWakeLock;



    Intent serviceIntent;
    public static boolean stateAlarma = false;
    public static boolean alarmaIsActive = false;
    LoadPreferences Load = new LoadPreferences();
    public static Vibrator vibrator;
    public static MediaPlayer ring;
    public static SharedPreferences prefs;
    public static SimpleDateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    public static SimpleDateFormat naturalFormat = new SimpleDateFormat("EEEE, 28 MMMM yyyy HH:mm");
    public static String textFormatNaturalNextAlarma = " -- ";
    public TextView textNextAlarm;

    /*static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }*/
    private int streamType = AudioManager.STREAM_MUSIC;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

        setContentView(R.layout.activity_main);

        LoadPreferences.start(getApplicationContext(),this);
        prefs =   getSharedPreferences("preferences",Context.MODE_PRIVATE);
        textNextAlarm = this.findViewById(R.id.textNextAlarma);


        Bundle extras = this.getIntent().getExtras();
        Intent intent = getIntent();
        String newString;
        if(extras != null) {
            if(extras.getBoolean("alarm")){
                activeAlarm();
                setContentView(R.layout.activity_alarm);
            }
        }
    }
    public TextView getTextView(){

        TextView txtView = (TextView)findViewById(R.id.textNextAlarma);
        return txtView;
    }

    public void setTextNextAlarm(Context context){
        TextView textNextAlarm =this.findViewById(R.id.textNextAlarma);
        textNextAlarm.setText("Hola");
    }
    public static void scheduleAlarm(Context context,long nextAlarm){
            Intent intentAlarm = new Intent(context, AlarmReceiver.class);
            // create the object
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            //set the alarm for particular time
            alarmManager.set(AlarmManager.RTC_WAKEUP,nextAlarm, PendingIntent.getBroadcast(context,1,  intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));
    }

    public void cancelAlarm(){
        Intent intentAlarm = new Intent(this, AlarmReceiver.class);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        PendingIntent pi = PendingIntent.getBroadcast(this, 1, intentAlarm, 0);
        alarmManager.cancel(pi);
        Toast.makeText(this, "Alarma DESACTIVADA", Toast.LENGTH_LONG).show();
    }

    public static long milliseconds(String date){
        //String date_ = date;

        try{
            Date mDate = timestampFormat.parse(date);
            Log.i("Alarm date",date);
            long timeInMilliseconds = mDate.getTime();
            return timeInMilliseconds;
        }
        catch (ParseException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return 0;
    }
     private PowerManager mPowerManager;

    public void activeAlarm(){
        ring = MediaPlayer.create(this,R.raw.alarma);
        ring.setLooping(true);
        ring.start();

        long[] pattern = {0, 100, 1000};
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(pattern, 0);

        PrefUtils.setKioskModeActive(true, getApplicationContext());

        alarmaIsActive = true;
        //showAddItemDialog(MainActivity.this);

        getWindow().addFlags( WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON );
        AudioManager audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);


  //later
    }
    public void desactivedAlarma(){
        Log.i("Termino","Se cancela el alarma");
        PrefUtils.setKioskModeActive(false, getApplicationContext());
        ring.stop();
        vibrator.cancel();
        alarmaIsActive = false;
        TextView textNextAlarm =this.findViewById(R.id.textNextAlarma);
        scheduleNextAlarm(getApplicationContext());
        textNextAlarm.setText("Proxima alarma: "+textFormatNaturalNextAlarma);
        setContentView(R.layout.activity_main);

    }


    public void response(View v){
        TextView response = this.findViewById(R.id.response);
        if( String.valueOf(response.getText()).equals("la navaja") ){
            desactivedAlarma();

        }
    }
    public void setTime (View v){

        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public void setState (View v){

  
        stateAlarma = !stateAlarma;
       // TextView stateText =this.findViewById(R.id.State);
        //stateText.setText((stateAlarma)?"ACTIVO":"INACTIVO");

        Button testButton = (Button) findViewById(R.id.setState);
        testButton.setText((stateAlarma)?"DESACTIVAR":"ACTIVAR");


        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("state", stateAlarma);
        editor.commit();

        TextView textNextAlarm =this.findViewById(R.id.textNextAlarma);
        if(stateAlarma){
            scheduleNextAlarm(getApplicationContext());
            textNextAlarm.setText("Proxima alarma: "+textFormatNaturalNextAlarma);

        }else{
            cancelAlarm();
            textNextAlarm.setText("Proxima alarma: -- ");

        }
    }

    public static void scheduleNextAlarm(Context context){
        String timeAlarm = prefs.getString("time", "00:00");
        Date dateCurrent = new Date();
        String dateCurrentFormat = dateFormat.format(dateCurrent);
        long dateAlarma = milliseconds(dateCurrentFormat+" "+timeAlarm+":00");
        long millisStart = Calendar.getInstance().getTimeInMillis();
        long nextAlarma = dateAlarma;


        if(!validateTime(nextAlarma)){
            Log.i("VAlidate","Pasoooo");
            nextAlarma = dateAlarma + makeTimeNextAlarm();
        }
        scheduleAlarm(context,nextAlarma);
        Log.i("Next Alarm",timeToString(nextAlarma));
        textFormatNaturalNextAlarma = timeToString(nextAlarma);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("textNextAlarm", textFormatNaturalNextAlarma);
        editor.commit();
        Toast.makeText(context, "Su alarma sonara el proximo "+textFormatNaturalNextAlarma, Toast.LENGTH_LONG).show();



    }
    public static Boolean validateTime(long dateAlarma){
        long millisStart = Calendar.getInstance().getTimeInMillis();
        if( dateAlarma <= millisStart ){
            //return dateAlarma + (1000 * 60 * 60 * 24);
            return false;
        }
        return true;
    }
    public static long makeTimeNextAlarm(){

        Calendar c = Calendar.getInstance();
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK) + 1;
        Log.i("Day week",dayOfWeek+"");
        Boolean dayAlarma = false;
        Boolean fl = true;
        int factorMulti = 1;
        do{
            dayAlarma = prefs.getBoolean("day"+dayOfWeek, false);
            Log.i("TAG","day"+dayOfWeek);
            if(!dayAlarma){
                ++factorMulti;
                ++dayOfWeek;
                if(dayOfWeek==8)
                    dayOfWeek=1;
            }
        }while(!dayAlarma);
        return (1000 * 60 * 60 * 24) * factorMulti;

    }
    public static String timeToString(long millis){
        Date date = new Date(millis);
        return naturalFormat.format(date);
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

        TextView textNextAlarm =this.findViewById(R.id.textNextAlarma);
        scheduleNextAlarm(getApplicationContext());
        textNextAlarm.setText("Proxima alarma: "+textFormatNaturalNextAlarma);


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
