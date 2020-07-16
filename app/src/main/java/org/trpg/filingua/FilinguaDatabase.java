package org.trpg.filingua;

import android.graphics.Path;
import android.graphics.RectF;

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
    static class CardObject {
        CardObject(/*String title, Date createdDate, FileInfo fileInfo*/){
            this.title = title;
            this.createdDate = createdDate;
            this.fileInfo = fileInfo;
        }
        String title;
        Date createdDate;
        FileInfo fileInfo;

    }
}
