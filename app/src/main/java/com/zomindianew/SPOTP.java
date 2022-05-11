package com.zomindianew;
/**
 * Class :
 * Task : This class
 * Author: playstore.apps.android@gmail.com
 */

import android.content.Context;

public class SPOTP {

    private static final String OTP = "otp";
    private static final String TIME = "time";

    public static String getOTP(Context context) {
        try {
            return context.getSharedPreferences("OTP", Context.MODE_PRIVATE).getString(OTP, "");
        } catch (Exception e) {
            e.printStackTrace();
            return context.getSharedPreferences("OTP", Context.MODE_PRIVATE).getLong(OTP,0)+"";
        }
    }

    public static void setOTP(Context context, String value) {
        context.getSharedPreferences("OTP", Context.MODE_PRIVATE).edit().putString(OTP, value + "").apply();
    }

    public static long getTime(Context context) {
        return context.getSharedPreferences("OTP", Context.MODE_PRIVATE).getLong(TIME, 0);
    }

    public static void setTime(Context context, long value) {
        context.getSharedPreferences("OTP", Context.MODE_PRIVATE).edit().putLong(TIME, value).apply();
    }

    public static void clear(Context context) {
        context.getSharedPreferences("OTP", Context.MODE_PRIVATE).edit().clear().apply();
    }
}