/*
 *  Copyright (c) 2013 The CCP project authors. All Rights Reserved.
 *
 *  Use of this source code is governed by a Beijing Speedtong Information Technology Co.,Ltd license
 *  that can be found in the LICENSE file in the root of the web site.
 *
 *   http://www.cloopen.com
 *
 *  An additional intellectual property rights grant can be found
 *  in the file PATENTS.  All contributing project authors may
 *  be found in the AUTHORS file in the root of the source tree.
 */
package com.jc.zjcan.jctools;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.os.Environment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.jc.zjcan.jcframework.JcFrameworkApplication;

/**
 * Simple tools.
 *
 * @version 1.0.0
 */
@SuppressLint("SimpleDateFormat")
public final class JcUtil {

    public static final String Ou_ExternalStorage = "our_external_storage";

    private static float density = -1.0F;

    /**
     * 是否有外存卡
     *
     * @return
     */
    public static boolean isExistExternalStore() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 通话时间 格式00:00:00
     *
     * @param duration
     * @return
     */
    public static String getCallDurationShow(long duration) {
        if (duration / 60 == 0) {
            String second = "00";

            if (duration < 10) {
                second = "0" + duration;
            } else {
                second = duration + "";
            }
            return "00:" + second;
        } else {
            String minute = "00";
            String second = "00";
            String hour = "00";
            if ((duration / 60) < 10) {
                minute = "0" + (duration / 60);
            } else {
                if ((duration / 60) > 59) {
                    if ((duration / 3600) < 10) {
                        hour = "0" + (duration / 3600);
                    } else {
                        hour = (duration / 3600) + "";
                    }

                    if ((duration / 60) % 60 < 10) {
                        minute = "0" + (duration / 60) % 60;
                    } else {
                        minute = (duration / 60) % 60 + "";
                    }

                } else {
                    minute = (duration / 60) + "";
                }
            }
            if ((duration % 60) < 10) {
                second = "0" + (duration % 60);
            } else {
                second = (duration % 60) + "";
            }
            if (hour.equals("00")) {
                return minute + ":" + second;
            } else {
                return hour + ":" + minute + ":" + second;
            }
        }
    }

    static MediaPlayer mediaPlayer = null;

    public static void playNotifycationMusic(Context context, String voicePath)
            throws IOException {
        // paly music ...
        AssetFileDescriptor fileDescriptor = context.getAssets().openFd(
                voicePath);
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        }
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        mediaPlayer.reset();
        mediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(),
                fileDescriptor.getStartOffset(), fileDescriptor.getLength());
        mediaPlayer.prepare();
        mediaPlayer.setLooping(false);
        mediaPlayer.start();
    }

    public static ArrayList<String> removeString(List<String> strArr, String str) {
        ArrayList<String> newArr = null;
        if (strArr != null && str != null) {
            newArr = new ArrayList<String>();
            for (String string : strArr) {
                if (!str.equals(string)) {
                    newArr.add(string);
                }
            }
        }
        return newArr;
    }

    public static void clearActivityTask(final Context context) {
        Intent i = context.getPackageManager().getLaunchIntentForPackage(
                context.getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(i);
        ((Activity) context).finish();

    }

    public static String getDateCreate() {
        SimpleDateFormat dateformat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        return dateformat.format(new Date());
    }

    public static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");

    public static final SimpleDateFormat sequenceFormat = new SimpleDateFormat(
            "yyyyMMddHHmmss");
    public static final String HOLDPLACE = nextHexString(49);

    /**
     * The random seed
     */
    static final long seed = System.currentTimeMillis();
    // static final long seed=0;

    public static int K = 1;

    public static String getSequenceFormat(long t) {
        // long t = System.currentTimeMillis();
        Date d = new Date(t);
        String date = sequenceFormat.format(d);

        return date + "$" + (K++) + "%" + HOLDPLACE + "@" + t;
    }

    /**
     * Returns a random hexadecimal String
     */
    public static String nextHexString(int len) {
        byte[] buff = new byte[len];
        for (int i = 0; i < len; i++) {
            int n = nextInt(16);
            buff[i] = (byte) ((n < 10) ? 48 + n : 87 + n);
        }
        return new String(buff);
    }

    /**
     * Returns a random integer between 0 and n-1
     */
    public static int nextInt(int n) {
        Random rand = new Random(seed);
        return Math.abs(rand.nextInt()) % n;
    }

    public static boolean hasFullSize(String inStr) {
        if (inStr.getBytes().length != inStr.length()) {
            return true;
        }
        return false;
    }

    public static final String TACK_PIC_PATH = getExternalStorePath() + "/"
            + Ou_ExternalStorage + "/.chatTemp";

    public static File TackPicFilePath() {
        File localFile = new File(TACK_PIC_PATH, createJcFileName() + ".jpg");
        if ((!localFile.getParentFile().exists())
                && (!localFile.getParentFile().mkdirs())) {
            localFile = null;
        }
        return localFile;
    }

    public static final String TACK_VIDEO_PATH = getExternalStorePath() + "/"
            + Ou_ExternalStorage + "/.videoTemp";

    public static File TackVideoFilePath() {
        File localFile = new File(JcUtil.TACK_VIDEO_PATH,
                JcUtil.createJcFileName() + ".mp4");
        if ((!localFile.getParentFile().exists())
                && (!localFile.getParentFile().mkdirs())) {
            localFile = null;
        }
        return localFile;
    }

    /**
     * /sdcard
     *
     * @return
     */
    public static String getExternalStorePath() {
        if (isExistExternalStore()) {
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        }
        return null;
    }

    /**
     * dip转化像素
     *
     * @param context
     * @param dipValue
     * @return
     */
    public static int dip2px(Context context, float dipValue) {

        final float scale = context.getResources().getDisplayMetrics().density;

        return (int) (dipValue * scale + 0.5f);

    }

    /**
     * @param context
     * @return
     */
    public static float getDensity(Context context) {
        if (context == null)
            context = JcFrameworkApplication.getInstance()
                    .getApplicationContext();
        if (density < 0.0F)
            density = context.getResources().getDisplayMetrics().density;
        return density;
    }

    public static int round(Context context, int paramInt) {
        return Math.round(paramInt / getDensity(context));
    }

    public static int fromDPToPix(Context context, int dp) {
        return Math.round(getDensity(context) * dp);
    }

    private static long lastClickTime;

    public static boolean isInvalidClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 1000) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    public static boolean delFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return true;
        }

        return file.delete();
    }

    public static void delFolder(String folderPath) {
        try {
            delAllFile(folderPath); // 删除完里面所有内容
            String filePath = folderPath;
            filePath = filePath.toString();
            File myFilePath = new File(filePath);
            myFilePath.delete(); // 删除空文件夹
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * delete all file
     *
     * @param path
     * @return
     */
    public static boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
                delFolder(path + "/" + tempList[i]);// 再删除空文件夹
                flag = true;
            }
        }
        return flag;
    }

    public static int getMetricsDensity(Context context, float height) {
        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay().getMetrics(localDisplayMetrics);
        return Math.round(height * localDisplayMetrics.densityDpi / 160.0F);
    }

    public static String createJcFileName() {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());

        int y = c.get(Calendar.YEAR);
        int m = c.get(Calendar.MONTH);
        int d = c.get(Calendar.DAY_OF_MONTH);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        int second = c.get(Calendar.SECOND);
        return y + "-" + m + "-" + d + "-" + hour + "-" + minute + "-" + second;
    }

    public static String interceptStringOfIndex(String str, int index) {
        String intercept = str;

        if (TextUtils.isEmpty(str)) {
            return str;
        }

        if (str.length() > index) {
            intercept = str.substring(str.length() - index, str.length());

        }

        return intercept;
    }

    /**
     * 将文件生成位图
     *
     * @param path
     * @return
     * @throws java.io.IOException
     */
    public static BitmapDrawable getImageDrawable(String path)
            throws IOException {
        // 打开文件
        File file = new File(path);
        if (!file.exists()) {
            return null;
        }

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] bt = new byte[1024];

        // 得到文件的输入流
        @SuppressWarnings("resource")
        InputStream in = new FileInputStream(file);

        // 将文件读出到输出流中
        int readLength = in.read(bt);
        while (readLength != -1) {
            outStream.write(bt, 0, readLength);
            readLength = in.read(bt);
        }

        // 转换成byte 后 再格式化成位图
        byte[] data = outStream.toByteArray();
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);// 生成位图
        @SuppressWarnings("deprecation")
        BitmapDrawable bd = new BitmapDrawable(bitmap);

        return bd;
    }

    public static final int RANDOM_STRING_NONE = 0;
    public static final int RANDOM_STRING_CHAR = 1;
    public static final int RANDOM_STRING_NUM = 2;

    // Get 6 numbers and the composition string.
    public static String getCharAndNumr(int length, int type) {
        String val = "";
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num"; // 输出字母还是数字
            if (type == RANDOM_STRING_CHAR) {
                charOrNum = "char";
            } else if (type == RANDOM_STRING_NUM) {
                charOrNum = "num";
            }

            if ("char".equalsIgnoreCase(charOrNum)) // 字符串
            {
                int choice = /* random.nextInt(2) % 2 == 0 ? 65 : 97 */97; // 取得大写字母还是小写字母
                val += (char) (choice + random.nextInt(26));
            } else if ("num".equalsIgnoreCase(charOrNum)) // 数字
            {
                val += String.valueOf(random.nextInt(10));
            }
        }

        return val;
    }

    public static String CALLS_RECORD_TEMP_PATH = JcUtil.getExternalStorePath()
            + "/" + JcUtil.Ou_ExternalStorage + "/callsRecordTemp";

    /**
     * @param fileName
     * @param ext
     * @return
     */
    public static File createCallRecordFilePath(String fileName, String ext) {
        File localFile = new File(CALLS_RECORD_TEMP_PATH, fileName + "." + ext);
        if ((!localFile.getParentFile().exists())
                && (!localFile.getParentFile().mkdirs())) {
            localFile = null;
        }

        return localFile;
    }

    public static String remove86(String phone) {
        if (TextUtils.isEmpty(phone)) {
            return phone;
        }
        String str = phone;
        if (phone.startsWith("86"))
            str = phone.substring(2);
        else if (phone.startsWith("+86"))
            str = phone.substring(3);
        return str;
    }

}
