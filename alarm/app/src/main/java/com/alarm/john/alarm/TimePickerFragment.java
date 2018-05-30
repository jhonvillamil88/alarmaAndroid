package com.alarm.john.alarm;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;


public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    public static int hour   = 0;
    public static int minute = 0;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Do something with the time chosen by the user
        TextView tv1=(TextView) getActivity().findViewById(R.id.Time);
        tv1.setText(view.getHour()+":"+view.getMinute());
        this.setHour(view.getHour());
        this.setMinute(view.getMinute());

    }

    private void setHour(int _hour) {
        this.hour = _hour;
    }
    private void setMinute(int _minute){
        this.minute = _minute;
    }
    public static int getHour(){
        return hour;
    }
    public static int getMinute(){
        return minute;
    }
}