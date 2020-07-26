package org.trpg.filingua;

import android.app.Application;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class DiskInfoAdapter extends RecyclerView.Adapter<DiskInfoAdapter.DriveViewHolder> {

    private List<FilinguaDatabase.DiskInfoDataSet> list;

    public DiskInfoAdapter(List<FilinguaDatabase.DiskInfoDataSet> dataSet) {
        this.list = dataSet;
    }

    @Override
    public DiskInfoAdapter.DriveViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.drive_card, viewGroup, false);
        DriveViewHolder viewHolder = new DriveViewHolder(inflate);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(DriveViewHolder viewHolder, int pos) {
        viewHolder.capaBar.setMax(100);
        viewHolder.capaBar.setMin(0);
        viewHolder.capaBar.setProgress((int)list.get(pos).getUsedPercentage());
        viewHolder.icon.setImageResource(list.get(pos).getIcon());
        viewHolder.nameText.setText(list.get(pos).getName());
        viewHolder.capaText.setText(String.format("%.1f GB/%.1f GB 使用中", list.get(pos).getUsed(),list.get(pos).getMax()));
        viewHolder.itemView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //押したとき
                    startScalingAnimEnter(v);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    //離したとき
                    startScalingAnimExit(v);
                }
                return false;
            }
        });
    }

    private void startScalingAnimEnter(View view){
        Animation animation = AnimationUtils.loadAnimation(MyApplication.getInstance().getApplicationContext(), R.anim.scale_animation_enter);
        view.startAnimation(animation);
    }

    private void startScalingAnimExit(View view){
        Animation animation = AnimationUtils.loadAnimation(MyApplication.getInstance().getApplicationContext(), R.anim.scale_animation_exit);
        view.startAnimation(animation);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class DriveViewHolder extends RecyclerView.ViewHolder {
        public TextView nameText;
        public TextView capaText;
        public ProgressBar capaBar;
        public ImageView icon;
        public DriveViewHolder(View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.disk_label);
            capaText = itemView.findViewById(R.id.disk_space);
            capaBar  = itemView.findViewById(R.id.disk_space_bar);
            icon     = itemView.findViewById(R.id.drive_icon);
        }
    }
}
