package com.wroclawstudio.weddinggame.models.envioremnt;

import android.graphics.drawable.Drawable;

public abstract class BaseGameObject {
    private final int y;

    protected BaseGameObject(int y) {
        this.y = y;
    }

    public abstract Drawable getDrawable();

    public int getY() {
        return y;
    }
}
