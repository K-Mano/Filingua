package org.trpg.filingua;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.trpg.filingua.R;

public class ViewHolder extends RecyclerView.ViewHolder {
    public ImageView mImageView;
    public TextView mTextView;

    // コンストラクタ
    public ViewHolder (@NonNull View itemView) {
        super(itemView);
        mImageView = itemView.findViewById(R.id.imageView_adapter_show_bitmap);
        mTextView = itemView.findViewById(R.id.textView_adapter_show_string);
    }
}