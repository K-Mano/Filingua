package org.trpg.filingua;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;

public class ColorideScrollView extends NestedScrollView {
    public ColorideScrollView(@NonNull Context context) {
        super(context);
        init();
    }

    public ColorideScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ColorideScrollView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    int max;
    private float scrollAmount;
    private Color startColor;
    private Color endColor;

    public Color getStartColor() {
        return startColor;
    }

    public void setStartColor(Color startColor) {
        this.startColor = startColor;
    }

    public Color getEndColor() {
        return endColor;
    }

    public void setEndColor(Color endColor) {
        this.endColor = endColor;
    }

    public float getScrollAmount() {
        return scrollAmount;
    }

    public void setScrollAmount(float scrollAmount) {
        this.scrollAmount = scrollAmount;
    }

    ArgbEvaluator evaluator;

    private void init(){
        evaluator = new ArgbEvaluator();

        this.setOnScrollChangeListener(new OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                max = (int)((v.getChildAt(0).getMeasuredHeight()-v.getMeasuredHeight())*0.5);
                if(scrollY==0){
                    scrollAmount = 0;
                }else{
                    if((float)scrollY/max>1){
                        scrollAmount = 1;
                    }else{
                        scrollAmount = (float)scrollY/max;
                    }
                }
                v.setBackgroundColor((Integer)evaluator.evaluate(scrollAmount, Color.argb(0,255,255,255), Color.argb(255,255,255,255)));
            }
        });
    }
}
