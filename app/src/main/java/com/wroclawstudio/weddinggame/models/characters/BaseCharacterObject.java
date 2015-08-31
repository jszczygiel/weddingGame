package com.wroclawstudio.weddinggame.models.characters;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;

public class BaseCharacterObject {
    public static final int STANDING = 0;
    public static final int LEFT_STEP = 1;
    public static final int RIGHT_STEP = 2;
    private final Drawable stepTwoAnimation;
    private final Drawable standingAnimation;
    private final Drawable stepOneAnimation;
    private Rect rect;
    private int currentDrawable=STANDING;
    private boolean canJump=false;
    private boolean canMove=true;

    public BaseCharacterObject(Drawable stepOneAnimation, Drawable stepTwoAnimation, Drawable standingAnimation) {
        this.stepOneAnimation = stepOneAnimation;
        this.stepTwoAnimation = stepTwoAnimation;
        this.standingAnimation = standingAnimation;
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

    public void setCurrentDrawable(int currentDrawable) {
        this.currentDrawable = currentDrawable;
    }

    public void setCanJump(boolean canJump) {
        this.canJump=canJump;
    }

    public boolean canJump() {
        return canJump;
    }

    public void setCanMove(boolean canMove) {
        this.canMove=canMove;
    }

    public boolean canMove() {
        return canMove;
    }
}
