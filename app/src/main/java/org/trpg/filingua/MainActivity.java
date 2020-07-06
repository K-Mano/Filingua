package org.trpg.filingua;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // デフォルト遷移アニメーション
        AnimationSet animation = new AnimationSet(true);

        //Appbarの子Toolbarにメニューを設定
        Toolbar mainToolbar = findViewById(R.id.main_toolbar);
        mainToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener(){
            @Override
            public boolean onMenuItemClick(MenuItem item){
                int id = item.getItemId();
                switch (id){
                    case R.id.searchButton:
                        break;
                    case R.id.tabButton:
                        LayoutInflater inflator = getLayoutInflater();
                        View view=inflator.inflate(R.layout.tablist_view, null, false);
                        view.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in));
                        setContentView(view);
                        break;
                    case R.id.settingsButton:
                        break;
                }
                return true;
            }
        });
        //toolbar.inflateMenu(R.menu.menu_main);

        //TabLayoutの取得
        TabLayout tabLayout = findViewById(R.id.tab_main);
        //ViewPagerに設定するAdapterをセットアップ
        TabAdapter tAdapter = new TabAdapter(getSupportFragmentManager());
        //ViewPagerを宣言
        ViewPager viewPager = findViewById(R.id.pager);
        //Adapterを設定
        viewPager.setAdapter(tAdapter);
        //TabLayoutにViewPagerを設定
        tabLayout.setupWithViewPager(viewPager);

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
    */
}