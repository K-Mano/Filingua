import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.trpg.filingua.DataModel;
import org.trpg.filingua.R;
import org.trpg.filingua.ViewHolder;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<ViewHolder> {

    private List<DataModel> insertDataList;
    private ViewHolder noteViewHolder;

    public Adapter (List<DataModel> list) {
        this.insertDataList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder (@NonNull ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.pinned_tab, viewGroup, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder (@NonNull ViewHolder viewHolder, int i) {
        noteViewHolder.mImageView.setImageBitmap(insertDataList.get(i).getBitmap());
        noteViewHolder.mTextView.setText(insertDataList.get(i).getString());
    }

    @Override
    public int getItemCount () {
        return insertDataList.size();
    }
}