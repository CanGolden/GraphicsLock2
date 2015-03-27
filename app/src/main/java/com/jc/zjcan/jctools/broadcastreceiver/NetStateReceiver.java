package com.jc.zjcan.jctools.broadcastreceiver;

import java.util.ArrayList;
import java.util.List;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.jc.zjcan.jcframework.JcFrameworkApplication;

public class NetStateReceiver extends BroadcastReceiver {
	private JcFrameworkApplication application;

	public static enum NetState {
		NET_NO, NET_2G, NET_3G, NET_4G, NET_WIFI, NET_UNKNOWN
	};

	public static List<NetEventHandle> ehList = new ArrayList<NetEventHandle>();

	@Override
	public void onReceive(Context context, Intent intent) {
		application = JcFrameworkApplication.getInstance();
		// 向所有实现接口的activity传递消息
		for (NetEventHandle e : ehList) {
			e.netState(application.isConnected());
		}
	}

	/**
	 * 获取该功能，需要实现接口
	 * 
	 * @author Jcan
	 */
	public static interface NetEventHandle {
		void netState(NetState netCode);
	}
}
