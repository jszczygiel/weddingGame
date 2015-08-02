package com.wroclawstudio.weddinggame.utils;

import android.graphics.Rect;

public class EngineUtils {

    public static boolean intersectsVerticlly(Rect a, Rect b) {
        return  a.top < b.bottom && b.top < a.bottom;
    }
}
