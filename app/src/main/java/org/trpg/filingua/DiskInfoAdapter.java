package org.trpg.filingua;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

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
        viewHolder.nameText.setText(list.get(pos).getName());
        viewHolder.capaText.setText(String.format("Used %.1f GB/%.1f GB", list.get(pos).getUsed(),list.get(pos).getMax()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class DriveViewHolder extends RecyclerView.ViewHolder {
        public TextView nameText;
        public TextView capaText;
        public ProgressBar capaBar;

        public DriveViewHolder(View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.disk_label);
            capaText = itemView.findViewById(R.id.disk_space);
            capaBar  = itemView.findViewById(R.id.disk_space_bar);
        }
    }
}
