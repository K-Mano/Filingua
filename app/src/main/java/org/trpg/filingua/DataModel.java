package org.trpg.filingua;

import android.graphics.Bitmap;

public class DataModel {
    private Bitmap mBitmap;
    private String mString;

    public Bitmap getBitmap () {
        return mBitmap;
    }

    public String getString () {
        return mString;
    }

    public void setBitmap (Bitmap mBitmap) {
        this.mBitmap = mBitmap;
    }

    public void setString (String mString) {
        this.mString = mString;
    }
}