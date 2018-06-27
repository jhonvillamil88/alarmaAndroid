package com.alarm.john.alarm;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class LoadPreferences {


    public static void start(Context context, MainActivity el){
        SharedPreferences prefsT =   context.getSharedPreferences("preferences",Context.MODE_PRIVATE);
        boolean isAlarmaActivate = prefsT.getBoolean("state", false);

        //boton de activar o desactivar alarma
        TextView stateText = el.findViewById(R.id.State);
        stateText.setText((isAlarmaActivate)?"ACTIVO":"INACTIVO");
        MainActivity.stateAlarma = isAlarmaActivate;
        Button testButton = (Button) el.findViewById(R.id.setState);
        testButton.setText((isAlarmaActivate)?"DESACTIVAR":"ACTIVAR");
       // el.stateAlarma = isAlarmaActivate;

        //CheckBox para los dias
        CheckBox checkCmp = null;
        Boolean  dayValue = false;
        for(int i=1; i<8; i++){
            checkCmp =  el.findViewById(el.getResources().getIdentifier("day"+i, "id", el.getPackageName()));
            dayValue = prefsT.getBoolean("day"+i, false);
            checkCmp.setChecked(dayValue);

        }

        //Hora
        TextView timeText = el.findViewById(R.id.Time);
        String isTime = prefsT.getString("time", "00:00");
        timeText.setText(isTime);


    }
}
