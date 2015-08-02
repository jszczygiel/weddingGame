package com.wroclawstudio.weddinggame.models.envioremnt;

import android.graphics.drawable.Drawable;

public abstract class BaseGameObject {
    private final int y;
    private final Drawable drawable;

    public BaseGameObject(Drawable drawable, int y) {
        this.y = y;
        this.drawable = drawable;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public int getY() {
        return y;
    }
}
