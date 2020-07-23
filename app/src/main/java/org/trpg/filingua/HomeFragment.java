package org.trpg.filingua;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.usage.StorageStatsManager;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HomeFragment extends Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        // Viewを設定
        View rootView = inflater.inflate(R.layout.home_view, container, false);
        // RecyclerViewを取得
        RecyclerView recyclerView  = rootView.findViewById(R.id.quickaccess);
        RecyclerView recentView    = rootView.findViewById(R.id.recent_activity);
        RecyclerView driveListView = rootView.findViewById(R.id.drive_list);
        // LayoutManagerを設定
        LinearLayoutManager lManager = new LinearLayoutManager(getActivity());
        lManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(lManager);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager vManager = new LinearLayoutManager(getActivity());
        vManager.setOrientation(LinearLayoutManager.VERTICAL);
        recentView.setLayoutManager(vManager);
        recentView.setHasFixedSize(true);
        LinearLayoutManager dManager = new LinearLayoutManager(getActivity());
        dManager.setOrientation(LinearLayoutManager.VERTICAL);
        driveListView.setLayoutManager(dManager);
        driveListView.setHasFixedSize(true);
        //Dividerの設定
        DividerItemDecoration divider = new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL);
        recentView.addItemDecoration(divider);
        // Adapterを作成
        RecyclerAdapter rAdapter = new RecyclerAdapter(createObject(3), R.layout.qa_card);
        RecyclerAdapter aAdapter = new RecyclerAdapter(createObject(5), R.layout.ra_card);
        DiskInfoAdapter dAdapter = new DiskInfoAdapter(getVolumeInfo());
        // Adapterを設定
        recyclerView.setAdapter(rAdapter);
        recentView.setAdapter(aAdapter);
        driveListView.setAdapter(dAdapter);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
    }

    private List<FilinguaDatabase.DefaultDataSet> createObject(int amount){
        List<FilinguaDatabase.DefaultDataSet> dataSet = new ArrayList<>();
        for(int i=0;i<amount;i++){
            FilinguaDatabase.DefaultDataSet object = new FilinguaDatabase.DefaultDataSet("card"+i);
            dataSet.add(object);
        }
        return dataSet;
    }

    private List<FilinguaDatabase.DiskInfoDataSet> getVolumeInfo() {
        StorageStatsManager sStatsManager = MainActivity.getStorageStatsManager();
        StorageManager sManager = MainActivity.getsManager();

        List<FilinguaDatabase.DiskInfoDataSet> dataSet = new ArrayList<>();
        List<StorageVolume> volumes = sManager.getStorageVolumes();

        // ストレージ識別用UUID
        List<UUID> uuid = new ArrayList<>();

        // 容量情報
        float used;
        float total;

        // GB(ギガバイト)
        final int GB = (int)Math.pow(1024, 3);

        // アイコン
        int icon = 0;
        // デバッグ用コード
        Log.d("StorageManager", "Starting volume information updates");
        Log.d("StorageManager", "############### VOLUME LIST ###############");
        for(int count=0; count<volumes.size(); count++){
            Log.d("StorageManager", String.format("Volume %d: \"%s\", Status=\"%s\"", count, volumes.get(count).getDescription(getContext()), volumes.get(count).getState()));
        }
        Log.d("StorageManager", "###########################################");

        // 情報を取得して構造体に格納
        for(int count=0; count<volumes.size(); count++){
            try {
                if(volumes.get(count).isPrimary()){
                    Log.d("StorageManager", String.format("Volume %d is primary storage.", count));
                    uuid.add(StorageManager.UUID_DEFAULT);
                    icon = R.drawable.ic_baseline_storage_24;
                }else{
                    Log.d("StorageManager", String.format("Volume %d is removable storage.", count));
                    String uid_h = volumes.get(count).getUuid().replace("-","");
                    Log.d("StorageManager", String.format("Volume %d UUID is %s", count, uid_h));
                    uuid.add(UUID.nameUUIDFromBytes(uid_h.getBytes()));
                    icon = R.drawable.ic_baseline_sd_storage_24;
                }
                Log.d("StorageManager", String.format("Volume UUID is \"%s\"", uuid.get(count).toString()));
                total = sStatsManager.getTotalBytes(uuid.get(count));
                used  = total - (sStatsManager.getFreeBytes(uuid.get(count)));
                Log.d("StorageManager", String.format("Volume %d loaded.",count));
            }catch (Exception e){
                Log.d("StorageManager", String.format("Error in Volume %d: %s",count ,e));
                Log.d("StorageManager", String.format("Volume %d unloaded.",count));
                // リセット
                total = 0;
                used  = 0;
            }

            // リストに情報を格納
            dataSet.add(new FilinguaDatabase.DiskInfoDataSet(volumes.get(count).getDescription(getContext()), total/GB, used/GB, volumes.get(count).isPrimary(), volumes.get(count).isRemovable(),icon));
        }

        Log.d("StorageManager", String.format("Volume information has been successfully updated."));
        return dataSet;
    }
}
