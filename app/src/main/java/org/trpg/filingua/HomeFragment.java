package org.trpg.filingua;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.usage.StorageStatsManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HomeFragment extends Fragment{

    // コンテキスト
    private Context context;

    // RecyclerView
    private RecyclerView quickView;
    private RecyclerView recentView;
    private RecyclerView driveListView;

    //データセット
    private List<FilinguaDatabase.DiskInfoDataSet> driveInfo;

    // アダプター
    private RecyclerAdapter rAdapter;
    private RecyclerAdapter aAdapter;
    private DiskInfoAdapter dAdapter;

    private TextView date;

    private SwipeRefreshLayout srl;

    // ディレクトリのリスト
    List<FilinguaDatabase.DefaultDataSet> raList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        // Viewを設定
        View rootView = inflater.inflate(R.layout.home_view, container, false);

        // RecyclerViewを取得
        quickView     = (RecyclerView)rootView.findViewById(R.id.quickaccess);
        recentView    = (RecyclerView)rootView.findViewById(R.id.recent_activity);
        driveListView = (RecyclerView)rootView.findViewById(R.id.drive_list);

        // LayoutManagerを設定
        // クイックアクセス
        LinearLayoutManager lManager = new LinearLayoutManager(getActivity());
        lManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        quickView.setLayoutManager(lManager);
        quickView.setHasFixedSize(true);

        // 履歴
        LinearLayoutManager vManager = new LinearLayoutManager(getActivity());
        vManager.setOrientation(LinearLayoutManager.VERTICAL);
        recentView.setLayoutManager(vManager);
        recentView.setHasFixedSize(true);

        // ドライブリスト
        LinearLayoutManager dManager = new LinearLayoutManager(getActivity());
        dManager.setOrientation(LinearLayoutManager.VERTICAL);
        driveListView.setLayoutManager(dManager);
        driveListView.setHasFixedSize(true);

        //Dividerの設定
        DividerItemDecoration divider = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        recentView.addItemDecoration(divider);

        // スワイプ機能
        swipeTouchHelper.attachToRecyclerView(recentView);

        // Pull to refresh
        srl = rootView.findViewById(R.id.swipe_refresh_layout);
        srl.setOnRefreshListener(srlOnRefreshListener);
        //srl.setColorSchemeColors(getResources().getColor(R.color.red));

        // データセットを作成
        driveInfo = getVolumeInfo();

        // Adapterを作成
        raList = createObject(5);
        rAdapter = new RecyclerAdapter(createObject(3), R.layout.qa_card);
        aAdapter = new RecyclerAdapter(raList, R.layout.ra_card);
        dAdapter = new DiskInfoAdapter(driveInfo);

        // Adapterを設定
        quickView.setAdapter(rAdapter);
        recentView.setAdapter(aAdapter);
        driveListView.setAdapter(dAdapter);

        // 日付表示
        date = (TextView)rootView.findViewById(R.id.dateView);
        date.setText(DateUtils.formatDateTime(context, System.currentTimeMillis(), DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_WEEKDAY | DateUtils.FORMAT_ABBREV_ALL));

        return rootView;
    }

    protected void reload(){
        driveInfo = getVolumeInfo();
        date.setText(DateUtils.formatDateTime(context, System.currentTimeMillis(), DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_WEEKDAY | DateUtils.FORMAT_ABBREV_ALL));
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onDetach(){
        context=null;
        super.onDetach();
    }

    ItemTouchHelper swipeTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped( RecyclerView.ViewHolder viewHolder, int direction) {
            raList.remove(viewHolder.getAdapterPosition());
            aAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
        }
    });

    private SwipeRefreshLayout.OnRefreshListener srlOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    reload();
                    srl.setRefreshing(false);
                }
            }, 1000);
        }
    };

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
                //Log.d("StorageManager", String.format("Volume UUID is \"%s\"", uuid.get(count).toString()));
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
