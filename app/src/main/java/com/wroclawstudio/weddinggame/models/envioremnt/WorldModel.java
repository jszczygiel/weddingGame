package com.wroclawstudio.weddinggame.models.envioremnt;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;

import java.util.List;

public class WorldModel {
    private final List<BaseGameObject[]> environment;

    private final int worldSize;
    private ColorDrawable backgroundColor;

    public WorldModel(List<BaseGameObject[]> environment, int color) {
        this.environment = environment;
        this.worldSize = environment.size();
        this.backgroundColor = new ColorDrawable(color);
    }

    public int getWorldSize() {
        return worldSize;
    }

    public List<BaseGameObject[]> getEnvironment() {
        return environment;
    }

    public Drawable getBackgroundColor() {
        return backgroundColor;
    }
}
