package org.trpg.filingua;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private List<Filingainfo.tabCard> list;


    public ListAdapter(List<Filingainfo.tabCard> cardObjectList) {
        this.list = cardObjectList;
    }

    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.tab_card, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(inflate);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int pos) {

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    // ViewHolder(固有ならインナークラスでOK)
    class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
    public void remove(int position) {

        ListAdapter.remove(position);
        notifyItemRemoved(position);
    }


    ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

            // 横にスワイプされたら要素を消す
            int swipedPosition = viewHolder.getAdapterPosition();
            ListAdapter adapter = (ListAdapter) list.getAdapter();
            adapter.remove(swipedPosition);
        }
    };
}
