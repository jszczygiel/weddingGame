package com.wroclawstudio.weddinggame.models.envioremnt;

import android.graphics.drawable.Drawable;

public class GrassObject extends BaseGameObject {
    public GrassObject(Drawable drawable, int y) {
        super(drawable, y);
    }

    @Override
    public boolean isSolid() {
        return false;
    }
}
