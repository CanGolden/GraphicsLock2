package com.jc.zjcan.graphicslock2.receive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.jc.zjcan.graphicslock2.AlarmNotificeActivity;

/**
 * Created by jincan on 2015/4/2.
 */
public class AlarmReceive extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if ("jcan_alarm".equals(intent.getAction())) {
            Log.v("^^^^^^^^", "ok 闹钟收到1啊");
            Intent in = new Intent(context, AlarmNotificeActivity.class);
            in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(in);
        }
    }
}
