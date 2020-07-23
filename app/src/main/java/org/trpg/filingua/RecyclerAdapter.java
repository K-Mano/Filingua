package org.trpg.filingua;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<FilinguaDatabase.DefaultDataSet> list;
    private int layout;

    public RecyclerAdapter(List<FilinguaDatabase.DefaultDataSet> dataSet, int layout) {
        this.list   = dataSet;
        this.layout = layout;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(layout,viewGroup,false);
        RecyclerView.ViewHolder viewHolder = new ViewHolderDefault(inflate);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int pos) {

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    //　ViewHolder(デフォルト)
    class ViewHolderDefault extends RecyclerView.ViewHolder{
        public ViewHolderDefault(View itemView) {
            super(itemView);
            /// RecyclerViewのテスト用 ///
        }
    }

}
