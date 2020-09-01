package org.trpg.filingua;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.Manifest;
import android.app.SearchManager;
import android.app.usage.StorageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.provider.SearchRecentSuggestions;
import android.provider.ContactsContract;
import android.transition.Fade;
import android.util.Log;
import android.util.Xml;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import org.xmlpull.v1.XmlSerializer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import static androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_DRAG;
import static org.trpg.filingua.FilinguaDatabase.drawableToBitmap;

public class MainActivity extends AppCompatActivity {

    private Context context;

    // ストレージ情報
    private static StorageStatsManager sStatsManager;
    private static StorageManager sManager;
    private static List<StorageVolume> sVolumes;

    public static StorageStatsManager getStorageStatsManager() {
        return sStatsManager;
    }

    public static StorageManager getsManager() {
        return sManager;
    }

    private File settings;

    private boolean viewSwitch = false;
    private static FloatingActionButton fab;

    private boolean tab = false;

    // BottomSheet関係
    private BottomSheetBehavior sheetBehavior;
    private CardView bottom_sheet;
    private TextView bSheetState;
    private Button commitTasks;
    private Button clearTasks;
    private RecyclerView tasks_list;

    // タスク関係
    public List<TaskModel> tasks = new ArrayList<>();
    private List<File> testItems = new ArrayList<>();
    private TaskQueueAdapter tAdapter;

    // Fragmentを作成
    private HomeFragment home_frag;
    private PinnedTabFragment pin_frag;

    // フラグメントのコントローラ
    FragmentTransaction fTrans;
    private RadialMenuView rmv;

  //  private FragmentTransaction fTrans;

    // FABのモード
    public static DisplayMode mode = DisplayMode.MODE_HOME;
    public static void setMode(DisplayMode mode) {
        MainActivity.mode = mode;
        switch(mode) {
            case MODE_HOME:
                fab.setImageResource(R.drawable.ic_security_pin);
                break;
            case MODE_PIN:
                fab.setImageResource(R.drawable.ic_baseline_home_24);
                break;
            case MODE_N_COMMANDS:
                fab.setImageResource(R.drawable.ic_baseline_add_24);
                break;
            case MODE_S_COMMANDS:
                fab.setImageResource(R.drawable.ic_baseline_edit_24);
        }
    }

    // Tabのリスト
    private static List<FilinguaDatabase.Tab> tabs = new ArrayList<>();

    public static List<FilinguaDatabase.Tab> getTabs() {
        return tabs;
    }

    // リソースの取得
    private Resources r;
    
    // Drawable(icons)
    private Drawable ICON_DELETE;
    
    public enum TaskType {
        TASK_DELETE,
        TASK_COPY,
        TASK_CUT,
        TASK_COMPRESS,
        TASK_DECOMPRESS
    }

    public static class TaskModel{
        private List<File> items;
        private TaskType type;
        TaskModel(List<File> items, TaskType type){
            this.items = items;
            this.type = type;
        }
        public List<File> getItems() {
            return items;
        }
        public TaskType getType() {
            return type;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_main);
        Button btn = (Button) findViewById(R.id.button3);
        btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick (View v){
                rmv.menuToggle();
            }

        });

        rmv = findViewById(R.id.Radial);


        // Contextの取得
        context = MainActivity.this.getApplicationContext();

        // Resourcesを取得
        r = getResources();

        // iconを取得
        ICON_DELETE = r.getDrawable(R.drawable.ic_baseline_delete_24);
        
        // fabを取得
        fab = findViewById(R.id.global_fab);

        String test = "This is a test!";

/*
        for(int i=0; i<5; i++){
            try {
                FileOutputStream fos = openFileOutput(String.format("file_%d.txt", i + 1), Context.MODE_PRIVATE);
                fos.write(test.getBytes());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e){
                e.printStackTrace();
            }
        }
        File s = new File(getFilesDir().getPath(), "directory_");
        s.mkdir();
        for(int i=0; i<3; i++){
            try{
                File f = new File(String.format(getFilesDir().getPath(), String.format("directory_%d",i+1)));
                if(!f.exists()){
                    f.mkdir();
                }
            }catch(Exception e){
              
            }
        }
        for(int i=0; i<3; i++){
            try{
                File f = new File(String.format("%s/directory_",getFilesDir()), String.format("directory_%d",i+1));
                if(!f.exists()){
                    f.mkdir();
                }
            } catch (Exception e) {

            }
        }*/

        if(Build.VERSION.SDK_INT>=23){
            checkPermission();
        }

        if(savedInstanceState == null){
            fTrans = getSupportFragmentManager().beginTransaction();
            getTabs().add(new FilinguaDatabase.Tab("Home", null, R.layout.home_view, false));
            fTrans.replace(R.id.container, HomeFragment.newInstance(0));
            fTrans.commit();
        }

        // Appbarの子Toolbarにメニューを設定
        Toolbar mainToolbar = findViewById(R.id.main_toolbar);

        // Toolbarのメニュー項目のClickイベントリスナー
        mainToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.tabButton:
                        fTrans = getSupportFragmentManager().beginTransaction();
                        if (tab == false) {
                            tab = true;
                            fTrans.replace(R.id.container, new TabsView(getTabs()));
                        } else {
                            tab = false;
                            fTrans.replace(R.id.container, new HomeFragment());
                        }
                        fTrans.commit();
                        break;
                    case R.id.settingsButton:
                }
                return true;
            }
        });

        // SAFの取得
        sManager = (StorageManager) getSystemService(Context.STORAGE_SERVICE);
        sStatsManager = (StorageStatsManager) getSystemService(Context.STORAGE_STATS_SERVICE);
        sVolumes = sManager.getStorageVolumes();

        // BottomSheetの取得
        bottom_sheet = findViewById(R.id.sheet_base);
        sheetBehavior = BottomSheetBehavior.from(bottom_sheet);
        bSheetState = findViewById(R.id.text_sheet_state);
        tasks_list = findViewById(R.id.tasks_list);

        commitTasks = findViewById(R.id.button_commit);
        clearTasks = findViewById(R.id.button_clear);

        // ドライブリスト
        LinearLayoutManager dManager = new LinearLayoutManager(this);
        dManager.setOrientation(LinearLayoutManager.VERTICAL);
        tasks_list.setLayoutManager(dManager);
        tasks_list.setHasFixedSize(false);

        // Dividerの設定
        DividerItemDecoration divider = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        tasks_list.addItemDecoration(divider);

        // RecyclerViewのItemのスワイプ機能
        swipeTouchHelper.attachToRecyclerView(tasks_list);

        tasks.add(new TaskModel(testItems, TaskType.TASK_COPY));
        tasks.add(new TaskModel(testItems, TaskType.TASK_CUT));
        tasks.add(new TaskModel(testItems, TaskType.TASK_DELETE));
        tasks.add(new TaskModel(testItems, TaskType.TASK_COMPRESS));
        tasks.add(new TaskModel(testItems, TaskType.TASK_DECOMPRESS));

        tAdapter = new TaskQueueAdapter(tasks);

        // RecyclerViewのitemへのonClickListener紐づけ
        tAdapter.setOnItemClickListener(new TaskQueueAdapter.onItemClickListener() {
            @Override
            public void onClick(View view, int pos) {
                
            }
        });
        
        // Adapterを設定
        tasks_list.setAdapter(tAdapter);
        
        commitTasks.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View v) {
               /*
               for(int i=0; i<tasks.size(); i++) {
                   doTasks(tasks.get(i).getType(), tasks.get(i).getItems());
               }
               */
           }
        });
        clearTasks.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                clearTasks();
            }
        });

        // callback for do something
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(View view, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        bSheetState.setText("スワイプでタスク キューを非表示");
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                        bSheetState.setText("スワイプでタスク キューを表示");
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(View view, float v) {
                tasks_list.setAlpha(v);
                fab.setAlpha(1.0f-v);
                if(v==1){
                    fab.setVisibility(View.GONE);
                }else{
                    fab.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    ItemTouchHelper swipeTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT) {
        public static final float ALPHA_FULL = 1.0f;

        @Override
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
            super.onSelectedChanged(viewHolder, actionState);
            if(actionState == ACTION_STATE_DRAG){
                viewHolder.itemView.setAlpha(0.6f);
            }
        }

        @Override
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            viewHolder.itemView.setAlpha(1.0f);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            tAdapter.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
            return true;
        }

        @Override
        public void onSwiped( RecyclerView.ViewHolder viewHolder, int direction) {
            if(direction==ItemTouchHelper.LEFT){
                tasks.remove(viewHolder.getAdapterPosition());
                tAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            }
        }

        @Override
        public void onChildDraw(Canvas base, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                // Get RecyclerView item from the ViewHolder
                Paint swipe_background = new Paint();
                View itemView = viewHolder.itemView;
                Bitmap icon;

                // DrawableからBitmapを作成
                if(Math.abs(dX) < itemView.getBottom()-itemView.getTop()){
                    icon = drawableToBitmap(ICON_DELETE, 64, 64, itemView.getRight()+(int)dX+(itemView.getBottom()-itemView.getTop())/2, (itemView.getTop()+itemView.getBottom())/2, Color.WHITE);
                }else{
                    icon = drawableToBitmap(ICON_DELETE, 64, 64, itemView.getRight()-(itemView.getBottom()-itemView.getTop())/2, (itemView.getTop()+itemView.getBottom())/2, Color.WHITE);
                }

                // 右にスワイプ
                if(dX < 0) {
                    // 背景色を設定
                    swipe_background.setARGB(255, 255, 50, 50);

                    // スワイプした距離によって背景を描画
                    base.drawRect((float)itemView.getRight() + dX, (float)itemView.getTop(), (float)itemView.getRight(), (float)itemView.getBottom(), swipe_background);
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
    
    // タスクの実行
    private void doTasks(TaskType type, List<File> items){
        switch(type){
            case TASK_DELETE:
                break;
            case TASK_COPY:
            case TASK_CUT:
            case TASK_COMPRESS:
            case TASK_DECOMPRESS:
        }
    }

    private void clearTasks(){
        ClearTasksDialog ctd = new ClearTasksDialog();
        ctd.show(getSupportFragmentManager(), "ClearDialog");
        //tasks.clear();
    }

    public static boolean newFile(File file){
        try{
            return true;
        }catch(Exception e){
            return false;
        }
    }

    public static boolean newDir(File dir){
        try{
            dir.mkdir();
            return true;
        }catch(Exception e){
            return false;
        }
    }

    // XML書き出し
    public void writeXml(File file) {
        try {
            // ファイル出力ストリームを作る
            BufferedWriter bufferWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file.getName(), false)));

            XmlSerializer xmlse = Xml.newSerializer();
            xmlse.setOutput(bufferWriter);

            OutputStream out = this.openFileOutput(file.getPath(), MODE_PRIVATE);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
            xmlse.setOutput(writer);

            // ドキュメントのスタート
            xmlse.startDocument(null, Boolean.TRUE);
            xmlse.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);

            // データ入力
            xmlse.startTag(null, "rootNode");


            // バッファにためてあるモノを書き込む
                /*
                for (int i = 0; i < this.mItemList.size(); i++) {
                    XmlItem curr = this.mItemList.get(i);

                    // Itemノードを書き出す
                    xmlse.startTag(null, "item");
                    xmlse.attribute(null, "no", curr.no + "");
                    xmlse.text(curr.text);
                    xmlse.endTag(null, "item");
                }
                */

            xmlse.endTag(null, "rootNode");

            // タグの終了と書き出し
            xmlse.endDocument();
            xmlse.flush();

            out.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // permissionの確認
    public void checkPermission() {
        // 既に許可
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            //setUpWriteExternalStorage();
        }
        // 拒否
        else {
            requestPermission();
        }
    }

    // 許可を求める
    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
    }

    public Fragment getVisibleFragment() {
        FragmentManager fragmentManager = MainActivity.this.getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (fragment != null && fragment.isVisible())
                    return fragment;
            }
        }
        return null;
    }

    public void global_Fab_Click(View view){
        FragmentTransaction fTrans = getSupportFragmentManager().beginTransaction();
        switch(mode){
            case MODE_HOME:
                // 初期化
                home_frag = new HomeFragment();
                pin_frag = new PinnedTabFragment();

                // 遷移アニメーションの設定
                home_frag.setExitTransition(new Fade());
                pin_frag.setEnterTransition(new Fade());

                // フラグメントの置き換え
                fTrans.replace(R.id.container, pin_frag);

                // DisplayModeの変更
                setMode(DisplayMode.MODE_PIN);
                break;
            case MODE_PIN:
                // 初期化
                home_frag = new HomeFragment();
                pin_frag = new PinnedTabFragment();

                // 遷移アニメーションの設定
                home_frag.setEnterTransition(new Fade());
                pin_frag.setExitTransition(new Fade());

                // フラグメントの置き換え
                fTrans.replace(R.id.container, home_frag);

                // DisplayModeの変更
                setMode(DisplayMode.MODE_HOME);
                break;
            case MODE_N_COMMANDS:
                break;
            case MODE_S_COMMANDS:
        }
        // 変更の確定
        fTrans.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // SearchViewを取得し、検索可能な構成を設定する
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        //SearchView searchView = (SearchView) menu.findItem(R.id.searchButton).getActionView();
        // 現在のアクティビティが検索可能なアクティビティであると仮定します
        //searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        //searchView.setIconifiedByDefault(false);

        return true;
    }
}
