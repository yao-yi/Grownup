package com.gtechoogle.autoreddetect.utils;

import android.content.Context;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by edy on 2016/1/23.
 */
public class Utils {
    private static String UITAG = "UIMoneyPacket";
    private static String ServiceTAG = "ServiceDetector";
    public static int ON = 1;
    public static int OFF  = 0;

    public static void UILog(String value) {
        Log.d(UITAG,value);
    }
    public static void ServiceLog(String value) {
        Log.d(ServiceTAG,value);
    }
    public static boolean isAccessibilitySettingsOn(Context mContext) {
        int accessibilityEnabled = 0;
        final String service = "com.gtechoogle.autoreddetect/com.gtechoogle.autoreddetect.AutoDetectService";
        boolean accessibilityFound = false;
        try {
            accessibilityEnabled = Settings.Secure.getInt(
                    mContext.getApplicationContext().getContentResolver(),
                    android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
            UILog("accessibilityEnabled = " + accessibilityEnabled);
        } catch (Settings.SettingNotFoundException e) {
            UILog("Error finding setting, default accessibility to not found: "
                    + e.getMessage());
        }
        TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');

        if (accessibilityEnabled == 1) {
            UILog("***ACCESSIBILIY IS ENABLED*** -----------------");
            String settingValue = Settings.Secure.getString(
                    mContext.getApplicationContext().getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                TextUtils.SimpleStringSplitter splitter = mStringColonSplitter;
                splitter.setString(settingValue);
                while (splitter.hasNext()) {
                    String accessabilityService = splitter.next();

                    UILog("-------------- > accessabilityService :: " + accessabilityService);
                    if (accessabilityService.equalsIgnoreCase(service)) {
                        UILog("We've found the correct setting - accessibility is switched on!");
                        return true;
                    }
                }
            }
        } else {
            UILog("***ACCESSIBILIY IS DISABLED***");
        }

        return accessibilityFound;
    }
}
