package org.trpg.filingua;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.List;

public class DirItemsAdapter extends RecyclerView.Adapter<DirItemsAdapter.DirItemsHolder> {

    private final String TYPE_DIRECTORY = "Directory";
    private final String TYPE_FILE      = "File";

    private final int ICON_DIRECTORY = R.drawable.ic_baseline_folder_24;
    private final int ICON_FILE = R.drawable.ic_baseline_file_24;

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
        if(list.get(pos).isDirectory()){
            viewHolder.type.setText(TYPE_DIRECTORY);
            viewHolder.icon.setImageResource(ICON_DIRECTORY);
        }else if(list.get(pos).isFile()){
            viewHolder.type.setText(TYPE_FILE);
            viewHolder.icon.setImageResource(ICON_FILE);
        }else{
            viewHolder.type.setText("Unknown");
        }
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
        public TextView type;
        public CardView card;
        public ImageView icon;
        public DirItemsHolder(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.item_icon);
            card = itemView.findViewById(R.id.ra_card_base);
            type = itemView.findViewById(R.id.item_type);
            name = itemView.findViewById(R.id.item_name);
        }
    }
}
