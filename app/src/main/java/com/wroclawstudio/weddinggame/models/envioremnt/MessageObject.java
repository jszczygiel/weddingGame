package com.wroclawstudio.weddinggame.models.envioremnt;

import android.graphics.drawable.Drawable;

public class MessageObject extends BaseGameObject {
    public MessageObject(String text,int y) {
        super(null, y);
        this.text = text;
    }

    final private String text;

    @Override
    public boolean isSolid() {
        return false;
    }

    public String getText() {
        return text;
    }
}