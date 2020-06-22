package org.trpg.filingua;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
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
            //タブ一覧画面への遷移
            case R.id.tabButton:
                break;
        }
        return true;
    }
    //yeahhhhh!!
}