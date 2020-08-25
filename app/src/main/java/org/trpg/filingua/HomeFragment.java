package org.trpg.filingua;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.app.usage.StorageStatsManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StatFs;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.trpg.filingua.FilinguaDatabase.drawableToBitmap;

public class HomeFragment extends Fragment{

    // コンテキスト
    private Context context;

    // RecyclerView
    private RecyclerView quickView;
    private RecyclerView recentView;
    private RecyclerView driveListView;

    //データセット
    private List<FilinguaDatabase.DiskInfoDataSet> driveInfo;
    private List<FilinguaDatabase.DefaultDataSet> raList;

    // アダプター
    private RecyclerAdapter rAdapter;
    private RecyclerAdapter aAdapter;
    private DiskInfoAdapter dAdapter;

    // パラメータ
    private static final String ARG_COUNT = "count";
    private int cnt;

    // 日付表示
    private TextView date;

    // PtR
    private SwipeRefreshLayout srl;

    // リソースの取得
    private Resources r;

    // icons
    private final Paint swipe_background = new Paint();

    // Drawable(icons)
    private Drawable ICON_HIDE;
    private Drawable ICON_REMOVE;
    private Drawable ICON_FLAG;

    // 新しいインスタンスの作衛
    public static HomeFragment newInstance(int count) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COUNT, count);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        // Viewを取得
        View rootView = inflater.inflate(R.layout.home_view, container, false);

        // Resourcesを取得
        r = getResources();

        // iconを取得
        ICON_HIDE   = r.getDrawable(R.drawable.ic_baseline_visibility_off_24);
        ICON_REMOVE = r.getDrawable(R.drawable.ic_baseline_delete_24);
        ICON_FLAG   = r.getDrawable(R.drawable.ic_baseline_flag_24);

        // RecyclerViewを取得
        quickView     = rootView.findViewById(R.id.quickaccess);
        recentView    = rootView.findViewById(R.id.recent_activity);
        driveListView = rootView.findViewById(R.id.drive_list);

        /* LayoutManagerを設定 */
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
        DividerItemDecoration divider = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        recentView.addItemDecoration(divider);

        // RecyclerViewのItemのスワイプ機能
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

        // RecyclerViewのitemへのonClickListener紐づけ
        dAdapter.setOnItemClickListener(new DiskInfoAdapter.onItemClickListener() {
            @Override
            public void onClick(View view, int pos) {
                FragmentTransaction fTrans = getFragmentManager().beginTransaction();
                //fTrans.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                fTrans.addToBackStack(null);
                MainActivity.getTabs().add(new FilinguaDatabase.Tab(driveInfo.get(pos).getName(), null, R.layout.fragment_window, false));
                fTrans.replace(R.id.container, WindowFragment.newInstance(cnt, String.valueOf(driveInfo.get(pos).getPath())));
                fTrans.commit();
                //Toast.makeText(context, String.valueOf(driveInfo.get(pos).getName()), Toast.LENGTH_SHORT).show();
            }
        });

        // Adapterを設定
        quickView.setAdapter(rAdapter);
        recentView.setAdapter(aAdapter);
        driveListView.setAdapter(dAdapter);

        // 日付表示
        date = rootView.findViewById(R.id.dateView);
        date.setText(DateUtils.formatDateTime(context, System.currentTimeMillis(), DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_WEEKDAY | DateUtils.FORMAT_ABBREV_ALL));

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        Bundle args = getArguments();

        if(args != null){
            int count = args.getInt("count");
            String str = "HomeFragment: "+ count;
            cnt = count + 1;
        }
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
        context = null;
        super.onDetach();
    }

    ItemTouchHelper swipeTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        public static final float ALPHA_FULL = 1.0f;

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return onMove(recyclerView, viewHolder, target);
        }

        @Override
        public void onSwiped( RecyclerView.ViewHolder viewHolder, int direction) {
            if(direction == ItemTouchHelper.LEFT){
                raList.remove(viewHolder.getAdapterPosition());
                aAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            }
            else if(direction == ItemTouchHelper.RIGHT){
                aAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
            }
        }

        @Override
        public void onChildDraw(Canvas base, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                // Get RecyclerView item from the ViewHolder
                View itemView = viewHolder.itemView;
                Bitmap icon;

                if (dX > 0) {
                    // 背景色を設定
                    swipe_background.setARGB(255, 255, 150, 50);

                    // スワイプした距離によって背景を描画
                    base.drawRect((float)itemView.getLeft(), (float)itemView.getTop(), dX, (float)itemView.getBottom(), swipe_background);

                    // DrawableからBitmapを作成
                    if(Math.abs(dX) < itemView.getBottom()-itemView.getTop()){
                        icon = drawableToBitmap(ICON_FLAG,96,96,(int)dX-(itemView.getBottom()-itemView.getTop())/2, (itemView.getTop()+itemView.getBottom())/2, Color.WHITE);
                    }else{
                        icon = drawableToBitmap(ICON_FLAG,96,96,itemView.getLeft()+(itemView.getBottom()-itemView.getTop())/2, (itemView.getTop()+itemView.getBottom())/2, Color.WHITE);
                    }
                } else {
                    // 背景色を設定
                    swipe_background.setARGB(255, 200, 200, 205);

                    // スワイプした距離によって背景を描画
                    base.drawRect((float)itemView.getRight() + dX, (float)itemView.getTop(), (float)itemView.getRight(), (float)itemView.getBottom(), swipe_background);

                    // DrawableからBitmapを作成
                    if(Math.abs(dX) < itemView.getBottom()-itemView.getTop()){
                        icon = drawableToBitmap(ICON_HIDE, 96, 96, itemView.getRight()+(int)dX+(itemView.getBottom()-itemView.getTop())/2, (itemView.getTop()+itemView.getBottom())/2, Color.WHITE);
                    }else{
                        icon = drawableToBitmap(ICON_HIDE, 96, 96, itemView.getRight()-(itemView.getBottom()-itemView.getTop())/2, (itemView.getTop()+itemView.getBottom())/2, Color.WHITE);
                    }
                }

                // アイコンを描画
                base.drawBitmap(icon, new Matrix(), swipe_background);

                final float alpha = ALPHA_FULL - Math.abs(dX) / (float) viewHolder.itemView.getWidth();

                viewHolder.itemView.setAlpha(alpha);
                viewHolder.itemView.setTranslationX(dX);
            } else {
                super.onChildDraw(base, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
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

    private List<FilinguaDatabase.DefaultDataSet> createObject(int amount){
        List<FilinguaDatabase.DefaultDataSet> dataSet = new ArrayList<>();
        for(int i=0;i<amount;i++){
            FilinguaDatabase.DefaultDataSet object = new FilinguaDatabase.DefaultDataSet("card"+i);
            dataSet.add(object);
        }
        return dataSet;
    }

    private List<File> getActivityList(){
        List<File> list = new ArrayList<>();
        return list;
    }

    // 接続されているドライブを取得してリストに格納
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
        File path = null;
        
        // GB(ギガバイト)
        final int GB = 1073741824;

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
                    total = sStatsManager.getTotalBytes(uuid.get(count));
                    used  = total - (sStatsManager.getFreeBytes(uuid.get(count)));
                    path = context.getFilesDir();
                }else{
                    Log.d("StorageManager", String.format("Volume %d is removable storage.", count));
                    //String uid_h = volumes.get(count).getUuid().replace("-","");
                    //uuid.add(UUID.nameUUIDFromBytes(uid_h.getBytes()));
                    StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getAbsolutePath());
                    //total = statFs.getBlockCountLong()*blockSize;
                    total = statFs.getAvailableBytes();
                    //used  = total-statFs.getFreeBlocksLong()*blockSize;
                    used = statFs.getFreeBytes()-total;
                    path = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
                    icon = R.drawable.ic_baseline_sd_storage_24;
                }

                Log.d("StorageManager", String.format("Volume %d loaded.",count));
            }catch (Exception e){
                Log.d("StorageManager", String.format("Error in Volume %d: %s",count ,e));
                Log.d("StorageManager", String.format("Volume %d unloaded.",count));
                // リセット
                total = 0;
                used  = 0;
            }
            // リストに情報を格納
            dataSet.add(new FilinguaDatabase.DiskInfoDataSet(path, volumes.get(count).getDescription(getContext()), total/GB, used/GB, volumes.get(count).isPrimary(), volumes.get(count).isRemovable(),icon));
        }

        Log.d("StorageManager", String.format("Volume information has been successfully updated."));
        return dataSet;
    }
}
