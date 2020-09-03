package org.trpg.filingua;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.List;

public class AddressBarAdapter extends RecyclerView.Adapter<AddressBarAdapter.AddressViewHolder> {

    private List<String> paths;
    private AddressBarAdapter.onItemClickListener listener;

    public AddressBarAdapter(List<String> paths) {
        this.paths = paths;
    }

    @Override
    public AddressBarAdapter.AddressViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.address_bar_item, viewGroup, false);
        AddressBarAdapter.AddressViewHolder viewHolder = new AddressBarAdapter.AddressViewHolder(inflate);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AddressBarAdapter.AddressViewHolder viewHolder, final int pos) {
        viewHolder.name.setText(paths.get(pos));
        viewHolder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(view, pos);
            }
        });
    }

    public void setOnItemClickListener(AddressBarAdapter.onItemClickListener listener) {
        this.listener = listener;
    }

    public interface onItemClickListener {
        void onClick(View view, int pos);
    }

    @Override
    public int getItemCount() {
        return paths.size();
    }

    public class AddressViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public CardView card;
        public AddressViewHolder(View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.path_base);
            name = itemView.findViewById(R.id.path_text);
        }
    }
}
