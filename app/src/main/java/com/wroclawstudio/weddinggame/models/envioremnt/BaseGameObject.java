package com.wroclawstudio.weddinggame.models.envioremnt;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;

public abstract class BaseGameObject {
    private final int y;
    private final Drawable drawable;
    private final Rect rect;

    public BaseGameObject(Drawable drawable, int y) {
        this.y = y;
        this.drawable = drawable;
        this.rect = new Rect();
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public int getY() {
        return y;
    }

    public Rect getBounds() {
        return rect;
    }

    public void setBounds(int left, int top, int right, int bottom) {
        rect.bottom = bottom;
        rect.top = top;
        rect.right = right;
        rect.left = left;
        getDrawable().setBounds(rect);
    }

    public boolean isSolid() {
        return true;
    }

    public boolean isLeathal() {
        return false;
    }
}
