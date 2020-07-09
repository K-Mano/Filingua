package org.trpg.filingua;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                        View view = inflator.inflate(R.layout.tablist_view, null, false);
                        // アニメーションを開始
                        view.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in));
                        // Viewを移動
                        setContentView(view);
                        break;
                    case R.id.settingsButton:

                }
                return true;
            }
        });

        // TabLayoutの取得
        TabLayout tabLayout = findViewById(R.id.tab_main);
        // ViewPagerに設定するAdapterをセットアップ
        TabAdapter tAdapter = new TabAdapter(getSupportFragmentManager());
        // ViewPagerを宣言
        ViewPager viewPager = findViewById(R.id.pager);
        // Adapterを設定
        viewPager.setAdapter(tAdapter);
        // TabLayoutにViewPagerを設定
        tabLayout.setupWithViewPager(viewPager);
    }
}