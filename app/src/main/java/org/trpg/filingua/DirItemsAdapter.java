package org.trpg.filingua;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.List;

public class DirItemsAdapter extends RecyclerView.Adapter<DirItemsAdapter.DirItemsHolder> {

    private List<File> list;
    private DirItemsAdapter.onItemClickListener listener;

    public DirItemsAdapter(List<File> list) {
        this.list = list;
    }

    @Override
    public DirItemsAdapter.DirItemsHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ra_card, viewGroup, false);
        DirItemsAdapter.DirItemsHolder viewHolder = new DirItemsAdapter.DirItemsHolder(inflate);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(DirItemsAdapter.DirItemsHolder viewHolder, final int pos) {
        viewHolder.name.setText(list.get(pos).getName());
        viewHolder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(view, pos);
            }
        });
    }

    public void setOnItemClickListener(DirItemsAdapter.onItemClickListener listener) {
        this.listener = listener;
    }

    public interface onItemClickListener {
        void onClick(View view, int pos);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class DirItemsHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public CardView card;
        public DirItemsHolder(View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.ra_card_base);
            name = itemView.findViewById(R.id.item_name);
        }
    }
}
