package org.trpg.filingua;

import android.util.ArraySet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.List;
import java.util.Set;

import static org.trpg.filingua.MainActivity.setMode;

public class DirItemsAdapter extends RecyclerView.Adapter<DirItemsAdapter.DirItemsHolder> {

    private final String TYPE_DIRECTORY = "ディレクトリ";
    private final String TYPE_FILE      = "ファイル";

    private final int ICON_DIRECTORY = R.drawable.ic_baseline_folder_24;
    private final int ICON_FILE = R.drawable.ic_baseline_file_24;

    private List<File> list;
    private DirItemsAdapter.onItemClickListener listener;
    private DirItemsAdapter.onItemLongClickListener longListener;

    private Boolean isSelectableMode;
    private Boolean isAlwaysSelectable;
    private Set<Integer> selectedItemPositionsSet = new ArraySet<>();

    public DirItemsAdapter(List<File> list, boolean isAlwaysSelectable) {
        this.list = list;
        this.isAlwaysSelectable = isAlwaysSelectable;

        //isAlwaysSelectableがONのときは最初から選択モード
        isSelectableMode = isAlwaysSelectable;
    }

    @Override
    public DirItemsAdapter.DirItemsHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ra_card, viewGroup, false);
        DirItemsAdapter.DirItemsHolder viewHolder = new DirItemsAdapter.DirItemsHolder(inflate);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final DirItemsAdapter.DirItemsHolder viewHolder, final int pos) {
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
                //選択モードでないときは普通のクリックとして扱う
                if(!isSelectableMode && !isAlwaysSelectable){
                    listener.onClick(view, pos);
                }
                else {
                    if(isSelectedItem(pos)) removeSelectedItem(pos);
                    else addSelectedItem(pos);

                    onBindViewHolder(viewHolder, pos);
                }
                if(isSelectableMode){
                    setMode(DisplayMode.MODE_S_COMMANDS);
                }else{
                    setMode(DisplayMode.MODE_N_COMMANDS);
                }
            }
        });
        viewHolder.card.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                longListener.onLongClick(view, pos);
                //ロングクリックで選択モードに入る
                if (isSelectedItem(pos)) removeSelectedItem(pos);
                else addSelectedItem(pos);

                onBindViewHolder(viewHolder, pos);
                if(isSelectableMode){
                    setMode(DisplayMode.MODE_S_COMMANDS);
                }else{
                    setMode(DisplayMode.MODE_N_COMMANDS);
                }
                return true;
            }
        });

        //このアイテムが選択済みの場合はチェックを入れる（✓のイメージを表示する）
        if(isSelectedItem(pos)){
            viewHolder.checkbox.setVisibility(View.VISIBLE);
        }
        else {
            viewHolder.checkbox.setVisibility(View.GONE);
        }
    }

    public void setOnItemClickListener(DirItemsAdapter.onItemClickListener listener) {
        this.listener = listener;
    }

    public void setOnItemLongClickListener(DirItemsAdapter.onItemLongClickListener longListener) {
        this.longListener = longListener;
    }

    public interface onItemClickListener {
        void onClick(View view, int pos);
    }

    public interface onItemLongClickListener {
        void onLongClick(View view, int pos);
    }
    @Override
    public int getItemCount() {
        return list.size();
    }
    //選択済みのアイテムのPositionが記録されたSetを外部に渡す
    Set<Integer> getSelectedItemPositions(){
        return selectedItemPositionsSet;
    }

    //指定されたPositionのアイテムが選択済みか確認する
    private Boolean isSelectedItem(int position){
        return selectedItemPositionsSet.contains(position);
    }

    //選択モードでないときは選択モードに入る
    private void addSelectedItem(int position){
        if(selectedItemPositionsSet.isEmpty() && !isAlwaysSelectable) {
            isSelectableMode = true;
        }
        selectedItemPositionsSet.add(position);
    }

    //選択モードで最後の一個が選択解除された場合は、選択モードをOFFにする
    private void removeSelectedItem(int position){
        selectedItemPositionsSet.remove(position);
        if(selectedItemPositionsSet.isEmpty() && !isAlwaysSelectable){
            isSelectableMode = false;
        }
    }

    public class DirItemsHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView type;
        public CardView card;
        public ImageView icon;
        public ConstraintLayout checkbox;
        public DirItemsHolder(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.item_icon);
            card = itemView.findViewById(R.id.ra_card_base);
            type = itemView.findViewById(R.id.item_type);
            name = itemView.findViewById(R.id.item_name);
            checkbox = itemView.findViewById(R.id.selectable_indicator);
        }
    }
}
