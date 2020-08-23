package org.trpg.filingua;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TabAdapter extends RecyclerView.Adapter<TabAdapter.TabsViewHolder> {

    private List<FilinguaDatabase.Tab> list;
    private TabAdapter.onItemClickListener listener;

    public TabAdapter(List<FilinguaDatabase.Tab> tabs) {
        this.list = tabs;
    }

    @Override
    public TabAdapter.TabsViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.tab_card,viewGroup,false);
        TabAdapter.TabsViewHolder viewHolder = new TabAdapter.TabsViewHolder(inflate);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TabAdapter.TabsViewHolder viewHolder, final int pos) {
        viewHolder.title.setText(list.get(pos).getName());
        viewHolder.tab_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(view, pos);
            }
        });
    }

    public void setOnItemClickListener(TabAdapter.onItemClickListener listener) {
        this.listener = listener;
    }

    public interface onItemClickListener {
        void onClick(View view, int pos);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    //　ViewHolder(デフォルト)
    class TabsViewHolder extends RecyclerView.ViewHolder{
        public CardView tab_card;
        public TextView title;
        public TabsViewHolder(View itemView) {
            super(itemView);
            tab_card = itemView.findViewById(R.id.tab_card);
            title    = itemView.findViewById(R.id.tab_name);
        }
    }
}
