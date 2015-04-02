package com.jc.zjcan.graphicslock2.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jc.zjcan.graphicslock2.R;

import java.util.Calendar;

/**
 * Created by jincan on 2015/3/31.
 */
public class TimeView extends LinearLayout {

    private TextView textView;

    public TimeView(Context context) {
        super(context);
    }

    public TimeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TimeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        textView = (TextView) findViewById(R.id.timeView);
        textView.setText("helloWorld");
        timeHandler.sendEmptyMessage(0);
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == View.VISIBLE)
            timeHandler.sendEmptyMessage(0);
        else
            timeHandler.removeMessages(0);
    }

    private Handler timeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            refreshTime();
            if (getVisibility() == View.VISIBLE)
                timeHandler.sendEmptyMessageDelayed(0, 1000);
        }
    };

    public void refreshTime() {
        Log.v(">>>>>>>>>>>.", "ok");
        Calendar calendar = Calendar.getInstance();
        textView.setText(String.format("%d:%d:%d", calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND)));
    }
}
