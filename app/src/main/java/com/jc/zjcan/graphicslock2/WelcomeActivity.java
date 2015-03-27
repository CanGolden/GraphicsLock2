package com.jc.zjcan.graphicslock2;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.jc.zjcan.application.Lock2Application;
import com.jc.zjcan.graphicslock2.fragment.PwdViewFragment;

public class WelcomeActivity extends ActionBarActivity {
    public static final String WelComeTag = "firstUser";
    public static final String IsSetLock = "setLock";
    // time in milliseconds
    private static final long SPLASHTIME = 1500;
    private Lock2Application app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        app = (Lock2Application) Lock2Application.getInstance();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String setpwd = app.getSettingParams(IsSetLock);
                if (TextUtils.isEmpty(setpwd)) {
                    //设置密码
                    startActivity(new Intent(WelcomeActivity.this, LockViewActivity.class));
                    finish();
                } else {
                    //检查密码
                    getSupportFragmentManager().beginTransaction().replace(android.R.id.content,
                            PwdViewFragment.newInstance(PwdViewFragment.TYPE_CHECK)).commit();
                }
            }
        }, SPLASHTIME);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_welcome, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
