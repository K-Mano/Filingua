package org.trpg.filingua;

import android.os.Handler;
import android.os.Looper;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class MenuOpenAnimation extends Animation {
    private int innerStartPos;
    private int innerEndPos;
    private int outerStartPos;
    private int outerEndPos;

    private RadialMenuView view;

    MenuOpenAnimation(RadialMenuView view, int innerStartPos, int innerEndPos, int outerStartPos, int outerEndPos) {
        this.view = view;
        this.innerStartPos = innerStartPos;
        this.innerEndPos = innerEndPos;
        this.outerStartPos = outerStartPos;
        this.outerEndPos = outerEndPos;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation transformation) {
        final float inner = innerStartPos + ((innerEndPos - innerStartPos) * interpolatedTime);
        final float outer = outerStartPos + ((outerEndPos - outerStartPos) * interpolatedTime);
        final int alpha = (int)(50*interpolatedTime);

        view.setBackgroundAlpha(alpha);
        view.setOuterRadius(outer);
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setInnerRadius(inner);
            }
        }, 100);
        view.requestLayout();
    }
}
