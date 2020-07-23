package org.trpg.filingua;

import android.graphics.Path;
import android.graphics.RectF;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

public class FilinguaDatabase {
    private class FileInfo{
        FileInfo(String path, int size){
            this.path = new File(path);
            this.size = size;
        }
        File path;
        int size;
    }

    private class Directory{
        Directory(String path, int size, ArrayList<FileInfo> list){
            this.path = path;
            this.size = size;
            fileList = list;
        }
        String path;
        int size;
        ArrayList<FileInfo> fileList;
    }

    public FileInfo setFileInfo(String filepath,int filesize){
        FileInfo info = new FileInfo(filepath, filesize);
        return info;
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
        DiskInfoDataSet(String name, float max, float used, boolean isPrimary, boolean isRemovable){
            //デフォルトコンストラクタ
            super(name);

            this.max   = max;
            this.used  = used;

            percentage = ((float)used/max)*100;

            this.isPrimary   = isPrimary;
            this.isRemovable = isRemovable;
        }

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
