package com.ali.lantool.com.ali.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Process;
import android.provider.Settings;
import androidx.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.ViewConfiguration;
import android.view.WindowManager;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
/**
 * Created by su on 17-2-8.
 */

public class GeneralInfoHelper {

    private static final String TAG = GeneralInfoHelper.class.getSimpleName();

    public static final String LIB_PACKAGE_NAME = "com.su.workbox";
    //application context
    @SuppressLint("StaticFieldLeak")
    private static Context sContext;
    private static String sAndroidId;
    private static int sScreenWidth;
    private static int sScreenHeight;
    private static double sAspectRatio;

    private static int sAvailableWidth;
    private static int sAvailableHeight; // = sScreenHeight - sStatusBarHeight

    private static String sVersionName = "";
    private static int sVersionCode;
    private static String sPackageName = "";
    private static String sAppName = "";
    private static String sApplicationLabel = "";
    private static String sProcessName = "";
    private static int sProcessId = -1;
    private static long sInstallTime;
    private static long sUpdateTime;


    private static int sScaledTouchSlop;
    private static int sScaledEdgeSlop;

    private GeneralInfoHelper() {}

    public static void init(Context context) {
        long now = System.currentTimeMillis();
        sContext = context.getApplicationContext();
        Resources resources = sContext.getResources();
        initPackageInfo();
        initAndroidId();
        initScreenSize();
        sProcessId = Process.myPid();
        sProcessName = getCurrentProcessName();
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        sScaledTouchSlop = viewConfiguration.getScaledTouchSlop();
        sScaledEdgeSlop = viewConfiguration.getScaledEdgeSlop();
    }

    private static void initPackageInfo() {
        if (TextUtils.isEmpty(sVersionName)) {
            try {
                PackageManager pm = sContext.getPackageManager();
                PackageInfo pi = pm.getPackageInfo(sContext.getPackageName(), 0);
                sVersionName = pi.versionName;
                sVersionCode = pi.versionCode;
                sPackageName = pi.packageName;
                sAppName = pi.applicationInfo.loadLabel(pm).toString();
                sInstallTime = pi.firstInstallTime;
            } catch (PackageManager.NameNotFoundException e) {
                Log.w(TAG, e);
            }
        }
    }

    @SuppressLint("HardwareIds")
    private static void initAndroidId() {
        sAndroidId = Settings.Secure.getString(sContext.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    private static void initScreenSize() {
        WindowManager wm = (WindowManager) sContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point realSize = new Point();
        display.getRealSize(realSize);
        sScreenWidth = Math.min(realSize.x, realSize.y);
        sScreenHeight = Math.max(realSize.x, realSize.y);
        sAspectRatio = BigDecimal.valueOf(getScreenHeight())
                .divide(BigDecimal.valueOf(getScreenWidth()), 2, BigDecimal.ROUND_DOWN)
                .doubleValue();
        Point availableSize = new Point();
        display.getSize(availableSize);
        sAvailableWidth = Math.min(availableSize.x, availableSize.y);
        sAvailableHeight = Math.max(availableSize.x, availableSize.y);
    }

    public static double getAspectRatio() {
        return sAspectRatio;
    }

    public static int getProcessId() {
        return sProcessId;
    }

    @NonNull
    public static String getCurrentProcessName() {
        try {
            return IOUtil.streamToString(new FileInputStream("/proc/self/cmdline")).trim();
        } catch (IOException e) {
            Log.e(TAG, "can't get current process name!", e);
            return "";
        }
    }

    public static Context getContext() {
        return sContext;
    }

    public static String getVersionName() {
        return sVersionName;
    }

    public static int getVersionCode() {
        return sVersionCode;
    }

    public static String getPackageName() {
        return sPackageName;
    }

    public static String getAppName() {
        return sAppName;
    }

    public static String getAndroidId() {
        return sAndroidId;
    }

    public static int getScreenWidth() {
        return sScreenWidth;
    }

    public static int getScreenHeight() {
        return sScreenHeight;
    }

    public static int getAvailableWidth() {
        return sAvailableWidth;
    }

    public static int getAvailableHeight() {
        return sAvailableHeight;
    }


    public static String getApplicationLabel() {
        return sApplicationLabel;
    }

    public static String getProcessName() {
        return sProcessName;
    }

    public static long getInstallTime() {
        return sInstallTime;
    }

    public static long getUpdateTime() {
        return sUpdateTime;
    }

    public static int getScaledTouchSlop() {
        return sScaledTouchSlop;
    }

    public static int getScaledEdgeSlop() {
        return sScaledEdgeSlop;
    }

    @NonNull
    public static String infoToString() {
        return "GeneralInfoHelper{" + '\n' +
                infoString() + '\n' +
                '}';
    }

    public static String infoString() {
        return "versionName=" + sVersionName + '\n' +
                ", versionCode=" + sVersionCode + '\n' +
                ", processId=" + sProcessId + '\n' +
                ", processName=" + sProcessName + '\n' +
                ", packageName=" + sPackageName + '\n' +
                ", appName=" + sAppName + '\n' +
                ", deviceId=" + sAndroidId + '\n' +
                ", screenWidth=" + sScreenWidth + '\n' +
                ", screenHeight=" + sScreenHeight + '\n' +
                ", aspectRatio=" + sAspectRatio + '\n' +
                ", availableWidth=" + sAvailableWidth + '\n' +
                ", availableHeight=" + sAvailableHeight + '\n' +
                ", applicationLabel=" + sApplicationLabel + '\n' +
                ", scaledTouchSlop=" + sScaledTouchSlop + '\n' +
                ", scaledEdgeSlop=" + sScaledEdgeSlop;
    }

}
