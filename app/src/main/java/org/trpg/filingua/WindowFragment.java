package org.trpg.filingua;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_DRAG;
import static com.google.android.material.color.MaterialColors.ALPHA_FULL;
import static org.trpg.filingua.FilinguaDatabase.drawableToBitmap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WindowFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WindowFragment extends Fragment {

    private Context context;
    // パラメータ
    private static final String ARG_COUNT = "count";

    private String tCount;
    private int cnt;

    private String currentPath;
    private DirItemsAdapter iAdapter;
    private AddressBarAdapter bAdapter;
    private List<File> items = new ArrayList<>();
    private List<String> paths = new ArrayList<>();

    private RecyclerView dirItems;
    private RecyclerView addressBar;

    private Resources r;

    private Drawable ICON_REMOVE;
    private Drawable ICON_FLAG;

    public WindowFragment(String path) {
        currentPath = path;
    }

    // TODO: Rename and change types and number of parameters
    public static WindowFragment newInstance(int count, String path) {
        WindowFragment fragment = new WindowFragment(path);
        Bundle args = new Bundle();
        args.putInt(ARG_COUNT, count);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_window, container, false);

        context = rootView.getContext();

        try{
            File[] fileArray = new File(currentPath).listFiles();
            for(int i=0; fileArray.length>i; i++){
                items.add(fileArray[i]);
            }
        }catch(NullPointerException e){
            e.printStackTrace();
        }

        Log.d("Loader",String.format("CurrentPath: %s",currentPath));
        String[] p = currentPath.split("/");
        for(int i=1; i<p.length; i++) {
            paths.add(p[i]);
        }

        r = getResources();

        // iconの取得
        ICON_REMOVE = r.getDrawable(R.drawable.ic_baseline_delete_24);
        ICON_FLAG   = r.getDrawable(R.drawable.ic_baseline_flag_24);

        // RecyclerViewを取得
        dirItems   = rootView.findViewById(R.id.dir_items);
        addressBar = rootView.findViewById(R.id.address_bar);

        /* LayoutManagerを設定 */
        // ファイル/ディレクトリ表示用
        LinearLayoutManager lManager = new LinearLayoutManager(getActivity());
        lManager.setOrientation(LinearLayoutManager.VERTICAL);
        dirItems.setLayoutManager(lManager);
        dirItems.setHasFixedSize(true);

        // アドレスバー
        LinearLayoutManager hManager = new LinearLayoutManager(getActivity());
        hManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        addressBar.setLayoutManager(hManager);
        addressBar.setHasFixedSize(true);

        // Separatorの追加
        DividerItemDecoration divider = new DividerItemDecoration(rootView.getContext(), DividerItemDecoration.HORIZONTAL);
        divider.setDrawable(r.getDrawable(R.drawable.ic_separator));
        addressBar.addItemDecoration(divider);

        // スワイプ機能
        swipeTouchHelper.attachToRecyclerView(dirItems);

        // Adapterを作成
        iAdapter = new DirItemsAdapter(items);
        bAdapter = new AddressBarAdapter(paths);

        // RecyclerViewのitemへのonClickListener紐づけ
        iAdapter.setOnItemClickListener(new DirItemsAdapter.onItemClickListener() {
            @Override
            public void onClick(View view, int pos) {
                if(new File(items.get(pos).getPath()).isDirectory()){
                    FragmentTransaction fTrans = getFragmentManager().beginTransaction();
                    fTrans.replace(R.id.container,WindowFragment.newInstance(0, items.get(pos).getPath()));
                    fTrans.addToBackStack(null);
                    fTrans.commit();
                }
            }
        });

        bAdapter.setOnItemClickListener(new AddressBarAdapter.onItemClickListener() {
            @Override
            public void onClick(View view, int pos) {

            }
        });

        dirItems.setAdapter(iAdapter);
        addressBar.setAdapter(bAdapter);

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

    ItemTouchHelper swipeTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target){
            return false;
        }
        @Override
        public void onSwiped( RecyclerView.ViewHolder viewHolder, int direction) {
            if(direction == ItemTouchHelper.LEFT){
                items.remove(viewHolder.getAdapterPosition());
                bAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            }
            else if(direction == ItemTouchHelper.RIGHT){
                bAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
            }
        }

        @Override
        public void onChildDraw(Canvas base, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                // Get RecyclerView item from the ViewHolder
                View itemView = viewHolder.itemView;

                Paint swipe_background = new Paint();
                Bitmap icon;

                // 左にスワイプ
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
                }
                // 右にスワイプ
                else {
                    // 背景色を設定
                    swipe_background.setARGB(255,255,50,50);

                    // スワイプした距離によって背景を描画
                    base.drawRect((float)itemView.getRight() + dX, (float)itemView.getTop(), (float)itemView.getRight(), (float)itemView.getBottom(), swipe_background);

                    // DrawableからBitmapを作成
                    if(Math.abs(dX) < itemView.getBottom()-itemView.getTop()){
                        icon = drawableToBitmap(ICON_REMOVE, 96, 96, itemView.getRight()+(int)dX+(itemView.getBottom()-itemView.getTop())/2, (itemView.getTop()+itemView.getBottom())/2, Color.WHITE);
                    }else{
                        icon = drawableToBitmap(ICON_REMOVE, 96, 96, itemView.getRight()-(itemView.getBottom()-itemView.getTop())/2, (itemView.getTop()+itemView.getBottom())/2, Color.WHITE);
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
}