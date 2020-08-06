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
import android.transition.Fade;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import org.xmlpull.v1.XmlSerializer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Context context;

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

    File settings;

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

        context = MainActivity.this.getApplicationContext();
        settings = new File(context.getFilesDir(), "settings.xml");

        if(Build.VERSION.SDK_INT>=23){
            checkPermission();
        }

        // 設定ファイルの読み込み/書き込み
        if(!settings.exists()){
            writeXml(settings);
            Log.d("FileStreaming", "Created Settings File.");
        }else{
            Log.d("FileStreaming", "Settings File is exist.");
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

        // FAB
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
        
        //インテントを取得し、アクションを確認してクエリを取得します
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //doMySearch(query);
        }
    }

    // XML書き出し
    public void writeXml(File file){
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