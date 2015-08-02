package com.wroclawstudio.weddinggame.models.characters;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;

public class BaseCharacterObject {
    private static final int STANDING = 0;
    private static final int LEFT_STEP = 1;
    private static final int RIGHT_STEP = 2;
    private final Drawable stepTwoAnimation;
    private final Drawable standingAnimation;
    private final Drawable stepOneAnimation;
    private int x;
    private int y;
    private Rect rect;
    private int currentDrawable=STANDING;

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



    public void changeY(int delta) {
        rect.offset(0, delta);
    }
    public void changeX(int delta) {
        rect.offset(0, delta);
    }

    public void setBounds(int left, int top, int right, int bottom) {
        rect = new Rect(left, top, right, bottom);
    }

    public Rect getBounds() {
        return rect;
    }

    public Drawable getCurrentDrawable() {
        switch (currentDrawable) {
            default:
            case STANDING:
                return standingAnimation;
            case LEFT_STEP:
                return stepOneAnimation;
            case RIGHT_STEP:
                return stepTwoAnimation;

        }
    }
}
