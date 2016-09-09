package com.example.thinkpaduser.loverunning;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

public class RemindRunningActivity extends AppCompatActivity {
    private EditText hourEdit;
    private EditText minuteEdit;
    private String hour;
    private String minute;
    private final static String LOG_TAG = "RemindRunningActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remind_running);
        DatePicker datePicker = (DatePicker) findViewById(R.id.activity_remind_running_date_picker);
        datePicker.setCalendarViewShown(false);
        hourEdit = (EditText) findViewById(R.id.activity_remind_running_et_hour);
        minuteEdit = (EditText)findViewById(R.id.activity_remind_running_et_minut);
        minuteEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                minute = s.toString();
                Log.v(LOG_TAG,"hour----->" + hour);
                SharedPreferences sp = getSharedPreferences("minute", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("minute",minute);
                editor.commit();
            }
        });
        hourEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.v(LOG_TAG,"----------------->beforeTextChanged" + s +"+++"+start + "=====" + after);

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.v("RemindRunningActivity","----------------->onTextChanged" +s +"+++"+ start +"++" + before + "++" + count);
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.v("RemindRunningActivity","----------------->afterTextChanged" + s);
                hour = s.toString();
                Log.v(LOG_TAG,"hour----->" + hour);
                SharedPreferences sp = getSharedPreferences("hour", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("hour",hour);
                editor.commit();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        SharedPreferences sp = getSharedPreferences("hour",Context.MODE_PRIVATE);
        hourEdit.setText(sp.getString("hour",""));
        SharedPreferences sp2 = getSharedPreferences("minute",Context.MODE_PRIVATE);
        minuteEdit.setText(sp2.getString("minute",""));
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
