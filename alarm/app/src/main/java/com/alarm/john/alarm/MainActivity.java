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
import android.widget.RadioGroup;
import android.widget.RadioButton;
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
    public int     day     = 0;

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
                        stateAlarma = false;
                        resetAlarma();

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
        int selectDay = radioGroup.getCheckedRadioButtonId()+1;
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
