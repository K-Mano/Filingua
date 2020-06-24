package org.trpg.filingua;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;

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
        TabAdapter adapter = new TabAdapter(getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.pager);
        viewPager.setAdapter(adapter);
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