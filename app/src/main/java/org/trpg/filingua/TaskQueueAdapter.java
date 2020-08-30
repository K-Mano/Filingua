package org.trpg.filingua;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskQueueAdapter extends RecyclerView.Adapter<TaskQueueAdapter.TaskQueueHolder>{

    private List<MainActivity.TaskModel> list;
    private TaskQueueAdapter.onItemClickListener listener;
    private TaskQueueAdapter.onItemLongClickListener longListener;

    public TaskQueueAdapter(List<MainActivity.TaskModel> list) {
        this.list = list;
    }

    @Override
    public TaskQueueAdapter.TaskQueueHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.task_item, viewGroup, false);
        TaskQueueAdapter.TaskQueueHolder viewHolder = new TaskQueueAdapter.TaskQueueHolder(inflate);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final TaskQueueAdapter.TaskQueueHolder viewHolder, final int pos) {
        Log.d("PositionCount",String.valueOf(pos));
        switch(list.get(pos).getType()){
            case TASK_COPY:
                viewHolder.type.setText("コピー");
                break;
            case TASK_CUT:
                viewHolder.type.setText("移動");
                break;
            case TASK_DELETE:
                viewHolder.type.setText("削除");
                break;
            case TASK_COMPRESS:
                viewHolder.type.setText("圧縮");
                break;
            case TASK_DECOMPRESS:
                viewHolder.type.setText("解凍");
        }
        viewHolder.count.setText(String.format("%d個のアイテム", list.get(pos).getItems().size()));
        viewHolder.state.setText("待機中");
        viewHolder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(view, pos);
            }
        });
        viewHolder.card.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                //longListener.onLongClick(view, pos);
                return true;
            }
        });
    }

    public void setOnItemClickListener(TaskQueueAdapter.onItemClickListener listener) {
        this.listener = listener;
    }

    public void setOnItemLongClickListener(TaskQueueAdapter.onItemLongClickListener longListener) {
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

    public class TaskQueueHolder extends RecyclerView.ViewHolder {
        public TextView type;
        public TextView count;
        public TextView state;
        public CardView card;
        public TaskQueueHolder(View itemView) {
            super(itemView);
            type  = itemView.findViewById(R.id.text_task_type);
            count = itemView.findViewById(R.id.text_item_count);
            state = itemView.findViewById(R.id.text_task_state);
            card  = itemView.findViewById(R.id.task_card);
        }
    }
}
