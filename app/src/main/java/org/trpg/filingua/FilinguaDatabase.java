package org.trpg.filingua;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

public class FilinguaDatabase {

    public static class Tab{
        Tab(String name, Path path, int layout, boolean isRemovable){
            this.name   = name;
            this.path   = path;
            this.layout = layout;
            this.isRemovable = isRemovable;
        }
        private boolean isRemovable;
        private String name;
        private Path path;
        private int layout;

        public String getName() {
            return name;
        }

        public boolean isRemovable() {
            return isRemovable;
        }
    }

    // データセット(デフォルト)
    static class DefaultDataSet {
        DefaultDataSet(String name){
            this.name = name;
        }
        private String name;
        public String getName() {
            return name;
        }
    }

    static class DiskInfoDataSet extends DefaultDataSet{
        DiskInfoDataSet(File path, String name, float max, float used, boolean isPrimary, boolean isRemovable, int icon){
            //デフォルトコンストラクタ
            super(name);

            this.path = path;
            this.max   = max;
            this.used  = used;

            percentage = ((float)used/max)*100;

            this.isPrimary   = isPrimary;
            this.isRemovable = isRemovable;

            this.icon = icon;
        }
        private String name;
        // ドライブのパス
        private File path;
        // ストレージ容量
        private float max;
        // ストレージ使用量
        private float used;
        // ストレージ使用率
        private float percentage;
        // プライマリストレージデバイス
        private boolean isPrimary;
        // リムーバブルストレージデバイス
        private boolean isRemovable;
        // アイコン
        private int icon;

        public File getPath() {
            return path;
        }

        public float getMax() {
            return max;
        }

        public void setMax(int max) {
            this.max = max;
        }

        public float getUsed() {
            return used;
        }

        public void setUsed(int used) {
            this.used = used;
        }

        public float getUsedPercentage() {
            return percentage;
        }

        public int getIcon() {
            return icon;
        }
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

    public boolean isExternalStorageWritable(){
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
            return true;
        }
        return false;
    }

    public boolean isExternalStorageReadable(){
        String state = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)){
            return true;
        }
        return false;
    }

}
