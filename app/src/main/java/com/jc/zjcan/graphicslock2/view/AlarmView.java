package com.jc.zjcan.graphicslock2.view;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TimePicker;

import com.jc.zjcan.graphicslock2.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by jincan on 2015/4/1.
 */
public class AlarmView extends LinearLayout implements View.OnClickListener {
    private Button addAlarm;
    private ListView alarmList;
    private Context mContext;
    private ArrayList<String> alarmArray;
    private ArrayAdapter arrayAdapter;

    public AlarmView(Context context) {
        super(context);
        this.mContext = context;
    }

    public AlarmView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    public AlarmView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        alarmArray = new ArrayList<String>();
        arrayAdapter = new ArrayAdapter(mContext, R.layout.alarm_list_item, R.id.textView, alarmArray);
        addAlarm = (Button) findViewById(R.id.add_alarm);
        alarmList = (ListView) findViewById(R.id.alarm_list);
        addAlarm.setOnClickListener(this);
        alarmList.setAdapter(arrayAdapter);
    }

    @Override
    public void onClick(View v) {
        addAlarm();
    }

    public void addAlarm() {
        Calendar c = Calendar.getInstance();
        new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Calendar sc = Calendar.getInstance();
                AlarmManager alarmManager = (AlarmManager)
                        mContext.getSystemService(Context.ALARM_SERVICE);
                sc.set(Calendar.HOUR_OF_DAY, hourOfDay);
                sc.set(Calendar.MINUTE, minute);
                sc.set(Calendar.SECOND, 0);
                sc.set(Calendar.MILLISECOND, 0);
                Calendar currentTime = Calendar.getInstance();
                if (sc.getTimeInMillis() <= currentTime.getTimeInMillis()) {
                    sc.setTimeInMillis(sc.getTimeInMillis() + 24 * 3600 * 1000);
                }
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                String s = sdf.format(sc.getTimeInMillis());
                Log.v(">>>>>>>>>>>>>>>>>", s);
                if (!alarmArray.contains(s)) {
                    alarmArray.add(s);
                    arrayAdapter.notifyDataSetInvalidated();
                    Intent intent = new Intent();
                    intent.setAction("jcan_alarm");
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext,
                            0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                            sc.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
                    Log.v("&&&&&&", "闹着呢");
                }
            }
        }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show();
    }
}
