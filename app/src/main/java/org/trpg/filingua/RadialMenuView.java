package org.trpg.filingua;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

public class RadialMenuView extends LinearLayout {
    public RadialMenuView(Context context){
        super(context);
        setWillNotDraw(false);
    }
    public RadialMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
    }

    // メニューの固有情報
    private String title;
    private RadialMenuContents content;

    private boolean touch=false;

    private float posX;
    private float posY;

    private float mPosX = 0;
    private float mPosY = 0;

    public RadialMenuResult Show(int posX, int posY){
        return null;
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        if(touch==true){
            // ベース(下)
            Paint base = new Paint();
            base.setAntiAlias(true);
            base.setStrokeWidth(1);
            base.setColor(Color.rgb(240,240,240));
            base.setStyle(Paint.Style.FILL);

            // ベース(上)
            Paint innerBase = new Paint();
            innerBase.setAntiAlias(true);
            innerBase.setStrokeWidth(1);
            innerBase.setColor(Color.WHITE);
            innerBase.setStyle(Paint.Style.FILL);

            // メニュー(SelectedOverlay)
            Paint lay1=new Paint();
            lay1.setAntiAlias(true);
            lay1.setStrokeWidth(1);
            lay1.setColor(Color.GRAY);
            lay1.setStyle(Paint.Style.FILL);

            // メニュー(SelectedLineOverlay)
            Paint lay2=new Paint();
            lay2.setAntiAlias(true);
            lay2.setStrokeWidth(1);
            lay2.setColor(Color.argb(10,0,0,0));
            lay2.setStyle(Paint.Style.FILL);


            // ベースを描画
            canvas.drawColor(Color.argb(50,0,0,0));
            canvas.drawPath(drawRing(150,450,0 ,180,posX,posY),base);
            canvas.drawPath(drawRing(150,450,0 ,-180,posX,posY),base);
            canvas.drawPath(drawRing(150,375,0 ,180,posX,posY),innerBase);
            canvas.drawPath(drawRing(150,375,0 ,-180,posX,posY),innerBase);

            if(Math.sqrt(Math.pow(mPosX-posX,2)+Math.pow(mPosY-posY,2))<=400 && Math.sqrt(Math.pow(mPosX-posX,2)+Math.pow(mPosY-posY,2))>=150){
                if(Math.toDegrees(Math.atan2(posY-mPosY,posX-mPosX))>=45 && Math.toDegrees(Math.atan2(posY-mPosY,posX-mPosX))<=135){
                    canvas.drawPath(drawRing(150,375,-45 ,-135,posX,posY),lay2);
                    canvas.drawPath(drawRing(375,395,-45 ,-135,posX,posY),lay1);
                }
                else if(Math.toDegrees(Math.atan2(posY-mPosY,posX-mPosX))>=-45 && Math.toDegrees(Math.atan2(posY-mPosY,posX-mPosX))<=45){
                    canvas.drawPath(drawRing(150,375,-135,-225,posX,posY),lay2);
                    canvas.drawPath(drawRing(375,395,-135,-225,posX,posY),lay1);
                }
                else if(Math.toDegrees(Math.atan2(posY-mPosY,posX-mPosX))>=-135 && Math.toDegrees(Math.atan2(posY-mPosY,posX-mPosX))<=-45){
                    canvas.drawPath(drawRing(150,375,135,45,posX,posY),lay2);
                    canvas.drawPath(drawRing(375,395,135,45,posX,posY),lay1);
                }
                else{
                    canvas.drawPath(drawRing(150,375,-45,45,posX,posY),lay2);
                    canvas.drawPath(drawRing(375,395,-45,45,posX,posY),lay1);
                }
            }
        }
        this.invalidate();
    }

    public Path drawRing(float innerR, float outerR, float startAngle, float endAngle, float posX, float posY){
        Path path = new Path();

        path.moveTo(posX+(float)(outerR*Math.cos(Math.toRadians(startAngle))),posY+(float)(outerR*Math.sin(Math.toRadians(startAngle))));
        path.arcTo(new RectF(posX-outerR,posY-outerR,posX+outerR,posY+outerR),startAngle,endAngle-startAngle);
        path.lineTo(posX+(float)(innerR*Math.cos(Math.toRadians(endAngle))),posY+(float)(innerR*Math.sin(Math.toRadians(endAngle))));
        path.arcTo(new RectF(posX-innerR, posY-innerR, posX+innerR, posY+innerR), endAngle, startAngle-endAngle);
        path.close();
        return path;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                posX = event.getX();
                posY = event.getY();
                //touch = true;
                return false;
            case MotionEvent.ACTION_UP:
                touch = false;
                mPosX = 0;
                mPosY = 0;
                posX  = 0;
                posY  = 0;
                return false;
            case MotionEvent.ACTION_MOVE:
                mPosX=event.getX();
                mPosY=event.getY();
                break;
        }
        return false;
    }
    public static Bitmap drawableToBitmap(Drawable drawable, int height, int width, int centerX, int centerY, int color){

        if(drawable instanceof BitmapDrawable){
            return ((BitmapDrawable)drawable).getBitmap();
        }

        int left = centerX-(width/2);
        int top = centerY-(height/2);

        Bitmap bitmap = Bitmap.createBitmap(2000, 2000, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        PorterDuff.Mode mode = PorterDuff.Mode.SRC_ATOP;
        drawable.setBounds(left, top, left+width, top+height);
        drawable.setColorFilter(color, mode);
        drawable.draw(canvas);

        return bitmap;
    }
}
