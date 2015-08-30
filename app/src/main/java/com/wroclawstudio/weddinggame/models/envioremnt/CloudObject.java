package com.wroclawstudio.weddinggame.models.envioremnt;

import android.graphics.drawable.Drawable;

public class CloudObject extends BaseGameObject {
    public CloudObject(Drawable drawable, int y) {
        super(drawable, y);
    }

    @Override
    public boolean isSolid() {
        return false;
    }
}