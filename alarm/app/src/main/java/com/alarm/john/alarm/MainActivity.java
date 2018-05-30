package com.alarm.john.alarm;

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

public class MainActivity extends AppCompatActivity {

    public boolean stateAlarma = false;
    MonitorAlarma objMonitorAlarma = new MonitorAlarma();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

        if(stateAlarma){
            objMonitorAlarma.doInBackground();
        }


    }


}
