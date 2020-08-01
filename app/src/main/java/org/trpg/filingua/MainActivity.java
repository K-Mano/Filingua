package org.trpg.filingua;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Bundle;
import android.Manifest;
import android.app.SearchManager;
import android.app.usage.StorageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.Manifest;
import android.app.usage.StorageStatsManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.provider.SearchRecentSuggestions;
import android.transition.Fade;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
    FloatingActionButton fab;

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

        fab = findViewById(R.id.home_pin_switch);

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

        /*
        //インテントを取得し、アクションを確認してクエリを取得します
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);

            //コンテンツプロバイダが宣言したのと同じ権限とデータベース　モードが必要
            SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
                    MySuggestionProvider.AUTHORITY, MySuggestionProvider.MODE);

            //1つめのパラメータとして検索クエリ文字列を受け取る
            //候補の二つ目として含める２つ目の文字列(または null)を必要に応じて受け取る
            suggestions.saveRecentQuery(query,null);

        }

         */

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

    public Fragment getVisibleFragment(){
        FragmentManager fragmentManager = MainActivity.this.getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        if(fragments != null){
            for(Fragment fragment : fragments){
                if(fragment != null && fragment.isVisible())
                    return fragment;
            }
        }
        return null;
    }

    public void switchPinHomeClicked(View view){
        FragmentTransaction fTrans = getSupportFragmentManager().beginTransaction();
        viewSwitch = !viewSwitch;
        home_frag = new HomeFragment();
        pin_frag = new PinnedTabFragment();
        if(viewSwitch==true){
            home_frag.setExitTransition(new Fade());
            pin_frag.setEnterTransition(new Fade());
            fTrans.replace(R.id.container, pin_frag);
            fab.setImageResource(R.drawable.ic_baseline_home_24);
        }else{
            home_frag.setEnterTransition(new Fade());
            pin_frag.setExitTransition(new Fade());
            fTrans.replace(R.id.container, home_frag);
            fab.setImageResource(R.drawable.ic_security_pin);
        }
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