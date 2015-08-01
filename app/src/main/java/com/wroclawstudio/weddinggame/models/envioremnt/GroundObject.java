package com.wroclawstudio.weddinggame.models.envioremnt;

import android.graphics.drawable.Drawable;

public class GroundObject extends BaseGameObject {


    private final Drawable drawable;

    public GroundObject(Drawable drawable, int y) {
        super(y);
        this.drawable = drawable;
    }

    @Override
    public Drawable getDrawable() {
        return drawable;
    }
}