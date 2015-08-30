package com.wroclawstudio.weddinggame.models.envioremnt;

import android.graphics.drawable.Drawable;

public class EnemyObject extends BaseGameObject {
    public EnemyObject(Drawable drawable, int y) {
        super(drawable, y);
    }

    @Override
    public boolean isSolid() {
        return true;
    }

    @Override
    public boolean isLeathal() {
        return true;
    }
}