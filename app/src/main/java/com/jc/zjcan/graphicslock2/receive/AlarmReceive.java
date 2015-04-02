package com.jc.zjcan.graphicslock2.receive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by jincan on 2015/4/2.
 */
public class AlarmReceive extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        if("jcan_alarm".equals(intent.getAction())){
            
        }
    }
}
