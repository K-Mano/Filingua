package org.trpg.filingua;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.usage.StorageStatsManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    // ストレージ情報
    private static StorageStatsManager sStatsManager;
    private static StorageManager sManager;
    private static List<StorageVolume> sVolumes;
    public static List<StorageVolume> getAllVolumes() {
        return sVolumes;
    }
    public static StorageStatsManager getStorageStatsManager() {
        return sStatsManager;
    }
    public static StorageManager getsManager() {
        return sManager;
    }

    private boolean viewSwitch = false;

    // Fragmentを作成
    HomeFragment home_frag;
    PinnedTabFragment pin_frag;
    // フラグメントのコントローラ
    FragmentTransaction fTrans = getSupportFragmentManager().beginTransaction();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(Build.VERSION.SDK_INT>=23){
            checkPermission();
        }

        if(savedInstanceState==null){
            home_frag = new HomeFragment();
            pin_frag = new PinnedTabFragment();
            fTrans.replace(R.id.container, home_frag);
            fTrans.commit();
        }

        // Appbarの子Toolbarにメニューを設定
        Toolbar mainToolbar = findViewById(R.id.main_toolbar);
        // Toolbarのメニュー項目のClickイベントリスナー
        mainToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener(){
            @Override
            public boolean onMenuItemClick(MenuItem item){
            int id = item.getItemId();
            switch (id){
                case R.id.searchButton:
                    break;
                case R.id.tabButton:
                    LayoutInflater inflator = getLayoutInflater();
                    // Viewにアニメーションを設定
                    //View view = inflator.inflate(R.layout.tablist_view, null, false);
                    // アニメーションを開始
                    //view.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in));
                    // Viewを移動
                    //setContentView(view);
                    break;
                case R.id.settingsButton:
            }
            return true;
            }
        });

        // ViewPagerに設定するAdapterをセットアップ
        //TabAdapter tAdapter = new TabAdapter(getSupportFragmentManager());
        // ViewPagerを宣言
        //ViewPager viewPager = findViewById(R.id.pager);
        // Adapterを設定
        //viewPager.setAdapter(tAdapter);

        // SAFの取得
        sManager = (StorageManager)getSystemService(Context.STORAGE_SERVICE);
        sStatsManager = (StorageStatsManager)getSystemService(Context.STORAGE_STATS_SERVICE);
        sVolumes = sManager.getStorageVolumes();

    }
    // permissionの確認
    public void checkPermission() {
        // 既に許可
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            //setUpWriteExternalStorage();
        }
        // 拒否
        else{
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

    public void switchPinHomeClicked(View view){
        viewSwitch = !viewSwitch;
        if(viewSwitch==true){
            fTrans.replace(R.id.container, pin_frag);
        }else{
            fTrans.replace(R.id.container, home_frag);
        }
        fTrans.commit();
    }
}