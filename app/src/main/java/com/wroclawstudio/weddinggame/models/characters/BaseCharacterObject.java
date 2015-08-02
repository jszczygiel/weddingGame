package com.wroclawstudio.weddinggame.models.characters;

import android.graphics.drawable.Drawable;

public class BaseCharacterObject {
    private final Drawable stepTwoAnimation;
    private final Drawable standingAnimation;
    private final Drawable stepOneAnimation;
    private int x;
    private int y;

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

}
