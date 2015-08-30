package com.wroclawstudio.weddinggame.models.envioremnt;

import android.graphics.drawable.Drawable;

public class CastleObject extends BaseGameObject {
    public CastleObject(Drawable drawable, int y) {
        super(drawable, y);
    }

    @Override
    public boolean isSolid() {
        return false;
    }
}