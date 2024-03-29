package com.ali.lantool;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.ali.lantool.com.ali.utils.GeneralInfoHelper;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by su on 18-1-2.
 */

public class SampleApplication extends Application {

    public static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
        GeneralInfoHelper.init(this);
        initSharedPreference();
    }

    private void initSharedPreference() {
        Set<String> set = new HashSet<>();
        set.add("a");
        set.add("b");
        set.add("c");
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.edit()
                .putInt("int_test", 1)
                .putLong("long_test", 0x80000000)
                .putFloat("float_test", 1.1f)
                .putBoolean("boolean_test", true)
                .putString("string_test", "string...")
                .putStringSet("set_test", set)
                .apply();
    }

    public static Context getContext() {
        return sContext;
    }
}
