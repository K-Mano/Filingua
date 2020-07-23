package org.trpg.filingua;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;


import android.Manifest;
import android.app.SearchManager;
import android.app.usage.StorageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.Toast;


import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Appbarの子Toolbarにメニューを設定
        Toolbar toolbar = findViewById(R.id.main_toolbar);
        toolbar.inflateMenu(R.menu.menu_main);
        //TabLayoutの取得
        TabLayout tabLayout = findViewById(R.id.tab_main);
        //ViewPagerに設定するAdapterをセットアップ
        TabAdapter adapter = new TabAdapter(getSupportFragmentManager());
        //ViewPagerを宣言
        ViewPager viewPager = findViewById(R.id.pager);
        //Adapterを設定
        viewPager.setAdapter(adapter);
        //TabLayoutにViewPagerを設定
        tabLayout.setupWithViewPager(viewPager);



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

        
        //インテントを取得し、アクションを確認してクエリを取得します
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //doMySearch(query);
        }

    }
    /*
    //ツールバーメニューの生成
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }
    //メニューの選択されたボタンへの処理
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            //検索画面への遷移
            case R.id.searchButton:
                break;
            //タブ一覧画面への遷移
            case R.id.tabButton:
                break;
            //設定画面への遷移
            case R.id.settingsButton:

        }
        return true;
    }
    */
    /*
    public void unchi(View view){
        TextView textView = (TextView)findViewById(R.id.text);
        textView.setText("うんち!");
    }
s
    */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // SearchViewを取得し、検索可能な構成を設定する
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.searchButton).getActionView();
        // 現在のアクティビティが検索可能なアクティビティであると仮定します
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);

        return true;
    }

}