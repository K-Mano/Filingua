package org.trpg.filingua;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

public class FilinguaUtility {
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
        String title;
        Date date;
    }
}
