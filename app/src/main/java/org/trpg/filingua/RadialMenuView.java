package org.trpg.filingua;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import static org.trpg.filingua.FilinguaDatabase.drawableToBitmap;

public class RadialMenuView extends LinearLayout  {

    public RadialMenuView(Context context) {
        super(context);
        r = getResources();
        setWillNotDraw(false);
    }

    public RadialMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        r = getResources();
        setWillNotDraw(false);
    }

    public RadialMenuView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        r = getResources();
        setWillNotDraw(false);
    }

    // メニューの固有情報
    private RadialMenuContents content;

    private boolean flag=false;
    private boolean touch=false;
    private boolean drown=false;

    private float posX;
    private float posY;

    private float mPosX = 0;
    private float mPosY = 0;

    private float innerR;
    private float outerR;
    private int bAlpha;
    private int aIcon;

    private Bitmap copy;
    private Bitmap cut;
    private Bitmap delete;

    public void setInnerRadius(float innerR) {
        this.innerR = innerR;
    }

    public void setOuterRadius(float outerR) {
        this.outerR = outerR;
    }

    public void setBackgroundAlpha(int bAlpha) {
        this.bAlpha = bAlpha;
    }

    public void setIconAlpha(int aIcon) {
        this.aIcon = aIcon;
    }

    private Resources r;

    private Drawable ICON_COPY;
    private Drawable ICON_CUT;
    private Drawable ICON_DELETE;

    public void menuToggle(RadialMenuContents content){
        this.content = content;
        if(!flag){
            flag=!(flag);
            ICON_COPY   = r.getDrawable(R.drawable.ic_baseline_file_copy_24);
            ICON_CUT    = r.getDrawable(R.drawable.ic_baseline_move_24);
            ICON_DELETE = r.getDrawable(R.drawable.ic_baseline_delete_24);
            // アニメーションの定義
            MenuOpenAnimation animation = new MenuOpenAnimation(this, 50, 150, 50, 500);
            // アニメーションの起動期間を設定
            animation.setDuration(250);
            this.startAnimation(animation);
        }else{
            // アニメーションの定義
            MenuCloseAnimation animation = new MenuCloseAnimation(this, 150, 50, 500, 50);
            // アニメーションの起動期間を設定
            animation.setDuration(250);
            this.startAnimation(animation);
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    flag=!(flag);
                }
            }, 350);

        }
        //this.content = mode;
    }

    public void setPosition(float posX, float posY){
        this.posX = posX;
        this.posY = posY;
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        if(flag==true){
            // ベース(下)
            Paint base = new Paint();
            base.setAntiAlias(true);
            base.setStrokeWidth(1);
            base.setColor(Color.WHITE);
            base.setStyle(Paint.Style.FILL);

            // メニュー(SelectedLine)
            Paint line = new Paint();
            line.setAntiAlias(true);
            line.setStrokeWidth(1);
            line.setColor(Color.GRAY);
            line.setStyle(Paint.Style.FILL);

            // メニュー(Background)
            Paint back = new Paint();
            back.setAntiAlias(true);
            back.setStrokeWidth(1);
            back.setColor(Color.argb(10,0,0,0));
            back.setStyle(Paint.Style.FILL);

            // ベースを描画
            canvas.drawColor(Color.argb(bAlpha,0,0,0));
            canvas.drawPath(drawRing(innerR, outerR,0 ,180,posX,posY),base);
            canvas.drawPath(drawRing(innerR, outerR,0 ,-180,posX,posY),base);

            // アイコンの描画
            if(!drown){
                copy   = drawableToBitmap(ICON_COPY,128,128, (int)(295*Math.cos(-97.5)+posX), (int)(295*Math.sin(-97.5)+posY), Color.GRAY);
                cut    = drawableToBitmap(ICON_COPY,128,128, (int)(295*Math.cos(152.5)+posX), (int)(295*Math.sin(152.5)+posY), Color.GRAY);
                delete = drawableToBitmap(ICON_DELETE,128,128, (int)(295*Math.cos(-152.5)+posX), (int)(295*Math.sin(-152.5)+posY), Color.GRAY);
                drown = true;
            }

            switch(content){
                case STANDARD_MONO_OPERATION:
                    canvas.drawBitmap(copy,new Matrix(), new Paint());
                    canvas.drawBitmap(cut,new Matrix(), new Paint());
                    canvas.drawBitmap(delete,new Matrix(), new Paint());
                    break;
                case STANDARD_MULTI_OPERATION:
                    //canvas.drawBitmap(copy,new Matrix(), base);
                    //canvas.drawBitmap(cut,new Matrix(), base);
                    //canvas.drawBitmap(delete,new Matrix(), base);
            }

            if(touch){
                canvas.drawPath(drawRing(430,450,(float)Math.toDegrees(Math.atan2(mPosY-posY,mPosX-posX))-30 ,(float)Math.toDegrees(Math.atan2(mPosY-posY,mPosX-posX))+30,posX,posY),line);
            }

            if(Math.sqrt(Math.pow(mPosX-posX,2)+Math.pow(mPosY-posY,2))<=500 && Math.sqrt(Math.pow(mPosX-posX,2)+Math.pow(mPosY-posY,2))>=150){
                if(Math.toDegrees(Math.atan2(mPosY-posY,mPosX-posX))>=-120 && Math.toDegrees(Math.atan2(mPosY-posY,mPosX-posX))<=-75){
                    canvas.drawPath(drawRing(150,430,-120 ,-75,posX,posY),back);
                }
                else if(Math.toDegrees(Math.atan2(mPosY-posY,mPosX-posX))>=-165 && Math.toDegrees(Math.atan2(mPosY-posY,mPosX-posX))<=-120){
                    canvas.drawPath(drawRing(150,430,-165 ,-120,posX,posY),back);
                }
                else if(Math.toDegrees(Math.atan2(mPosY-posY,mPosX-posX))>=-180 && Math.toDegrees(Math.atan2(mPosY-posY,mPosX-posX))<=-165 || Math.toDegrees(Math.atan2(mPosY-posY,mPosX-posX))<=180 && Math.toDegrees(Math.atan2(mPosY-posY,mPosX-posX))>=150){
                    canvas.drawPath(drawRing(150,430,-180 ,-165,posX,posY),back);
                    canvas.drawPath(drawRing(150,430,180 ,150,posX,posY),back);
                }
            }
        }
        this.invalidate();
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
            case MotionEvent.ACTION_MOVE:
                mPosX=event.getX();
                mPosY=event.getY();
                return true;
            case MotionEvent.ACTION_DOWN:
                if(flag){
                    mPosX=event.getX();
                    mPosY=event.getY();
                    touch=true;
                    return true;
                }
                else{
                    return false;
                }
            case MotionEvent.ACTION_UP:
                mPosX=0;
                mPosY=0;
                touch=false;
                //drown=false;
                menuToggle(content);
                return true;
        }
        return false;
    }
}
