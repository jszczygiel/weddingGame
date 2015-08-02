package com.wroclawstudio.weddinggame.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;

public class ViewUtils {

    private ViewUtils() {
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static Drawable getDrawable(Context context, int resourceId) {
        Drawable drawable;
        if (SystemUtils.isLollypopOrGreater()) {
            drawable = context.getResources().getDrawable(resourceId, context.getTheme()).mutate();
        } else {
            drawable = context.getResources().getDrawable(resourceId).mutate();
        }
        return drawable;
    }
}
