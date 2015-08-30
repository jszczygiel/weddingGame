package com.wroclawstudio.weddinggame.models.envioremnt;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;

import com.wroclawstudio.weddinggame.models.characters.BaseCharacterObject;

import java.util.List;

public class WorldModel {
    private final List<BaseGameObject[]> environment;

    private final int worldSize;
    private final BaseCharacterObject playerCharacter;
    private final ColorDrawable backgroundColor;

    public WorldModel(List<BaseGameObject[]> environment, int worldSize,int color, BaseCharacterObject playerCharacter) {
        this.environment = environment;
        this.worldSize = worldSize;
        this.backgroundColor = new ColorDrawable(color);
        this.playerCharacter=playerCharacter;
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

    public BaseCharacterObject getPlayerCharacter() {
        return playerCharacter;
    }


}
