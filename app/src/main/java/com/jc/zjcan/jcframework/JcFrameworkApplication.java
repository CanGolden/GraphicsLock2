package com.jc.zjcan.jcframework;

import java.io.File;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.widget.Toast;
import com.jc.zjcan.graphicslock2.R;
import com.jc.zjcan.jctools.JcUtil;
import com.jc.zjcan.jctools.broadcastreceiver.NetStateReceiver.NetState;

public class JcFrameworkApplication extends Application {

	private static JcFrameworkApplication instance;
	public static JcFrameworkApplication getInstance() {
		return instance;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
	}

	public void showToast(String s) {
		Toast.makeText(this, s, Toast.LENGTH_LONG).show();
	}

	private File vStore;

	private void initFileStore() {
		if (!JcUtil.isExistExternalStore()) {
			Toast.makeText(getApplicationContext(), R.string.media_ejected,
					Toast.LENGTH_LONG).show();
			return;
		}
		File directory = new File(Environment.getExternalStorageDirectory(),
				JcUtil.Ou_ExternalStorage);
		if (!directory.exists() && !directory.mkdirs()) {
			Toast.makeText(getApplicationContext(),
					"Path to file could not be created", Toast.LENGTH_SHORT)
					.show();
			return;
		}
		vStore = directory;
	}

	public File getOuExternalStorage() {
		if (vStore == null || vStore.exists()) {
			initFileStore();
		}
		return vStore;
	}

	/**
	 * 判断当前网络状态
	 * 
	 * @return
	 */
	public NetState isConnected() {
		NetState stateCode = NetState.NET_NO;
		ConnectivityManager cm = (ConnectivityManager) this
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni != null && ni.isConnectedOrConnecting()) {
			switch (ni.getType()) {
			case ConnectivityManager.TYPE_WIFI:
				stateCode = NetState.NET_WIFI;
				break;
			case ConnectivityManager.TYPE_MOBILE:
				switch (ni.getSubtype()) {
				case TelephonyManager.NETWORK_TYPE_GPRS: // 联通2g
				case TelephonyManager.NETWORK_TYPE_CDMA: // 电信2g
				case TelephonyManager.NETWORK_TYPE_EDGE: // 移动2g
				case TelephonyManager.NETWORK_TYPE_1xRTT:
				case TelephonyManager.NETWORK_TYPE_IDEN:
					stateCode = NetState.NET_2G;
					break;
				case TelephonyManager.NETWORK_TYPE_EVDO_A: // 电信3g
				case TelephonyManager.NETWORK_TYPE_UMTS:
				case TelephonyManager.NETWORK_TYPE_EVDO_0:
				case TelephonyManager.NETWORK_TYPE_HSDPA:
				case TelephonyManager.NETWORK_TYPE_HSUPA:
				case TelephonyManager.NETWORK_TYPE_HSPA:
				case TelephonyManager.NETWORK_TYPE_EVDO_B:
				case TelephonyManager.NETWORK_TYPE_EHRPD:
				case TelephonyManager.NETWORK_TYPE_HSPAP:

					stateCode = NetState.NET_3G;
					break;

				case TelephonyManager.NETWORK_TYPE_LTE:
					stateCode = NetState.NET_4G;
					break;
				default:
					stateCode = NetState.NET_UNKNOWN;
				}
				break;
			default:
				stateCode = NetState.NET_UNKNOWN;
			}

		}
		return stateCode;
	}

	/**
	 * Retrieves application's version code from the manifest
	 * 
	 * @return versionCode
	 */
	public int getVersionCode() {
		int code = 1;
		try {
			PackageInfo packageInfo = getPackageManager().getPackageInfo(
					getPackageName(), 0);
			code = packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		return code;
	}

	/**
	 * 
	 * @param mode
	 */
	public void setAudioMode(int mode) {
		AudioManager audioManager = (AudioManager) getApplicationContext()
				.getSystemService(Context.AUDIO_SERVICE);
		if (audioManager != null) {
			audioManager.setMode(mode);
		}
	}

	public String getSettingParams(String key) {
		SharedPreferences settings = getSharedPreferences();
		return settings.getString(key, "");
	}

	public void saveSettingParams(String key, String value) {
		SharedPreferences settings = getSharedPreferences();
		settings.edit().putString(key, value).commit();
	}

	public static final String JcFrameWork = "JcApplication";

	public SharedPreferences getSharedPreferences() {
		return getSharedPreferences(JcFrameWork, MODE_PRIVATE);
	}
}