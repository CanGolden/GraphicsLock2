package com.jc.zjcan.graphicslock2.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jc.zjcan.application.Lock2Application;
import com.jc.zjcan.graphicslock2.R;
import com.jc.zjcan.graphicslock2.WelcomeActivity;
import com.jc.zjcan.graphicslock2.AlarmAcitivity;
import com.jc.zjcan.graphicslock2.view.GraphicslockView;

public class PwdViewFragment extends Fragment implements GraphicslockView.OnTouchPointsListener, View.OnClickListener {

    public static final String TYPE_SETTING = "setting";
    public static final String TYPE_CHECK = "check";
    private static final String ARG_TYPE = "type";

    private TextView titleText;
    private GraphicslockView lockView;
    private Button commit;
    private LinearLayout btnLayout;

    private String passWord;
    private Lock2Application app;

    public static PwdViewFragment newInstance(String type) {
        PwdViewFragment fragment = new PwdViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    public PwdViewFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pwd_view, container, false);
        app = (Lock2Application) Lock2Application.getInstance();
        titleText = (TextView) view.findViewById(R.id.fragment_pwd_view_title_text);
        btnLayout = (LinearLayout) view.findViewById(R.id.fragment_pwd_view_btn_layout);
        commit = (Button) view.findViewById(R.id.fragment_pwd_view_btn_commit);
        if (getArguments() != null) {
            //密码检查
            if (TYPE_CHECK.equals(getArguments().getString(ARG_TYPE)))
                titleText.setText("请绘制您的密码图案！");
                //密码设置
            else
                titleText.setText("请开始绘制您的密码图案！");
        }
        lockView = (GraphicslockView) view.findViewById(R.id.fragment_pwd_view_graphicsView);
        lockView.setOnTouchPointsListener(this);
        commit.setOnClickListener(this);
        return view;
    }

    @Override
    public void onStart(boolean is) {
        if (is) titleText.setText("绘制手势密码中。。。");
    }

    private int num = 0;
    private String firstPwd = "";

    @Override
    public void onPointFinished(String pwd) {
        passWord = pwd;
        if (TextUtils.isEmpty(passWord))
            titleText.setText("密码长度必须大于5");
        else {
            Log.v("****机密log：**** ", passWord);
            if (getArguments() != null) {
                //密码检查
                if (TYPE_CHECK.equals(getArguments().getString(ARG_TYPE))) {
                    String pwdStr = app.getSettingParams(WelcomeActivity.IsSetLock);
                    //检查成功
                    if (passWord.equals(pwdStr)) {
                        titleText.setText("匹配成功 ");
                        getActivity().startActivity(new Intent(getActivity(), AlarmAcitivity.class));
                        getActivity().finish();
                    } else
                    //检查失败
                    {
                        if (num < 4) {
                            titleText.setText("密码错误,还剩" + (4 - num) + "次机会！");
                            num++;
                            lockView.resetPoint();
                        } else if (num == 4) {
                            titleText.setText("密码错误，快跑，手机要爆炸了！");
                            getActivity().finish();
                        }
                    }
                }
                //密码设置
                else if (TYPE_SETTING.equals(getArguments().getString(ARG_TYPE))) {
                    switch (num) {
                        case 0:
                            titleText.setText("请确认您的手势密码！");
                            firstPwd = passWord;
                            lockView.resetPoint();
                            num = 1;
                            break;
                        case 1:
                            if (passWord.equals(firstPwd)) {
                                titleText.setText("两次图案一致，请确认！");
                                btnLayout.setVisibility(View.VISIBLE);
                                num = 2;
                            } else {
                                titleText.setText("两次图案不一致，请重新输入！");
                                num = 1;
                            }
                            break;
                        default:
                            break;
                    }
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_pwd_view_btn_commit:
                app.saveSettingParams(WelcomeActivity.IsSetLock, passWord);
                getActivity().startActivity(new Intent(getActivity(), AlarmAcitivity.class));
                getActivity().finish();
                break;
            default:
                break;
        }
    }
}
