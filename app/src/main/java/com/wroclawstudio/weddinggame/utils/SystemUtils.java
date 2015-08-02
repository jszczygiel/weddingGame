package com.wroclawstudio.weddinggame.utils;

import android.os.Build;

public class SystemUtils {
    private SystemUtils() {
    }

    public static boolean isLollypopOrGreater() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }
}
