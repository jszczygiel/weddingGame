package com.wroclawstudio.weddinggame.models.characters;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;

public class BaseCharacterObject {
    private final Drawable stepTwoAnimation;
    private final Drawable standingAnimation;
    private final Drawable stepOneAnimation;
    private int x;
    private int y;
    private Rect rect;

    public BaseCharacterObject(Drawable stepOneAnimation, Drawable stepTwoAnimation, Drawable standingAnimation) {
        this.stepOneAnimation = stepOneAnimation;
        this.stepTwoAnimation = stepTwoAnimation;
        this.standingAnimation = standingAnimation;
    }

    public Drawable getStepOneAnimation() {
        return stepOneAnimation;
    }

    public Drawable getStepTwoAnimation() {
        return stepTwoAnimation;
    }

    public Drawable getStandingAnimation() {
        return standingAnimation;
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }


    public void setY(int y) {
        this.y = y;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }


    public void changeY(int delta) {
        rect.offset(0,delta);
    }

    public void setBounds(int left, int top, int right, int bottom) {
        rect=new Rect(left,top,right,bottom);
    }

    public Rect getBounds() {
        return rect;
    }
}
