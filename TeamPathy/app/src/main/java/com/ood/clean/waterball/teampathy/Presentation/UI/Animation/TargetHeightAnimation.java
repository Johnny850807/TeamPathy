package com.ood.clean.waterball.teampathy.Presentation.UI.Animation;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;


public class TargetHeightAnimation extends Animation {
    private final int targetHeight;
    private final int initHeight;
    private final View view;
    private final boolean down;

    public TargetHeightAnimation(View view, int initHeight , int targetHeight, boolean down) {
        this.view = view;
        // remove that wrap_content and set the starting point
        view.getLayoutParams().height = initHeight;
        view.setLayoutParams(view.getLayoutParams());
        this.initHeight = initHeight;
        this.targetHeight = targetHeight;
        this.down = down;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        int heightSpan = targetHeight - initHeight;
        int newHeight;
        if (down) {
            newHeight = initHeight + (int) (heightSpan * interpolatedTime);
        } else {
            newHeight = (int) (targetHeight * (1 - interpolatedTime));
        }
        view.getLayoutParams().height = newHeight;
        view.requestLayout();
    }

    @Override
    public void initialize(int width, int height, int parentWidth,
                           int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
    }

    @Override
    public boolean willChangeBounds() {
        return true;
    }
}
