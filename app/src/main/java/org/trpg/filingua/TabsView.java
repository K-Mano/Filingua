package org.trpg.filingua;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import static androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_DRAG;
import static com.google.android.material.color.MaterialColors.ALPHA_FULL;
import static org.trpg.filingua.FilinguaDatabase.drawableToBitmap;

public class TabsView extends Fragment {

    public TabsView(List<FilinguaDatabase.Tab> tabs) {
        this.tabs = tabs;
    }

    private List<FilinguaDatabase.Tab> tabs;
    private RecyclerView tabsList;

    private TabAdapter tAdapter;

    private Drawable ICON_CLOSE;
    private Drawable ICON_LOCK;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tabs_view, container, false);

        // RecyclerViewを取得
        tabsList = rootView.findViewById(R.id.tabs_list);

        // LayoutManagerを設定
        LinearLayoutManager lManager = new LinearLayoutManager(getActivity());
        lManager.setOrientation(LinearLayoutManager.VERTICAL);
        tabsList.setLayoutManager(lManager);
        tabsList.setHasFixedSize(true);

        // スワイプ機能
        swipeTouchHelper.attachToRecyclerView(tabsList);

        // Adapterを作成
        tAdapter = new TabAdapter(tabs);

        // RecyclerViewのitemへのonClickListener紐づけ
        tAdapter.setOnItemClickListener(new TabAdapter.onItemClickListener() {
            @Override
            public void onClick(View view, int pos) {

            }
        });

        tabsList.setAdapter(tAdapter);

        return rootView;
    }

    ItemTouchHelper swipeTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
            super.onSelectedChanged(viewHolder, actionState);
            if (actionState == ACTION_STATE_DRAG) {
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
            int from = viewHolder.getAdapterPosition();
            int to   = target.getAdapterPosition();
            tAdapter.notifyItemMoved(from, to);
            return true;
        }

        @Override
        public void onChildDraw(Canvas base, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                // Get RecyclerView item from the ViewHolder
                View itemView = viewHolder.itemView;
                Paint swipe_background = new Paint();
                Bitmap icon;
                
                if (dX > 0) {
                    // 背景色を設定
                    //swipe_background.setARGB(255, 255, 150, 50);

                    // スワイプした距離によって背景を描画
                    //base.drawRect((float)itemView.getLeft(), (float)itemView.getTop(), dX, (float)itemView.getBottom(), swipe_background);

                    // DrawableからBitmapを作成
                    if(Math.abs(dX) < itemView.getBottom()-itemView.getTop()){
                        icon = drawableToBitmap(ICON_LOCK,96,96,(int)dX-(itemView.getBottom()-itemView.getTop())/2, (itemView.getTop()+itemView.getBottom())/2, Color.argb(255, 255, 150, 50));
                    }else{
                        icon = drawableToBitmap(ICON_LOCK,96,96,itemView.getLeft()+(itemView.getBottom()-itemView.getTop())/2, (itemView.getTop()+itemView.getBottom())/2, Color.argb(255, 255, 150, 50));
                    }
                } else {
                    // 背景色を設定
                    //swipe_background.setARGB(255, 200, 200, 205);

                    // スワイプした距離によって背景を描画
                    //base.drawRect((float)itemView.getRight() + dX, (float)itemView.getTop(), (float)itemView.getRight(), (float)itemView.getBottom(), swipe_background);

                    // DrawableからBitmapを作成
                    if(Math.abs(dX) < itemView.getBottom()-itemView.getTop()){
                        icon = drawableToBitmap(ICON_CLOSE, 96, 96, itemView.getRight()+(int)dX+(itemView.getBottom()-itemView.getTop())/2, (itemView.getTop()+itemView.getBottom())/2, Color.WHITE);
                    }else{
                        icon = drawableToBitmap(ICON_CLOSE, 96, 96, itemView.getRight()-(itemView.getBottom()-itemView.getTop())/2, (itemView.getTop()+itemView.getBottom())/2, Color.WHITE);
                    }
                }

                // アイコンを描画
                base.drawBitmap(icon, new Matrix(), new Paint());

                final float alpha = ALPHA_FULL - Math.abs(dX) / (float) viewHolder.itemView.getWidth();

                viewHolder.itemView.setAlpha(alpha);
                viewHolder.itemView.setTranslationX(dX);
            } else {
                super.onChildDraw(base, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        }
    });

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            if(tabs.get(viewHolder.getAdapterPosition()).isRemovable()){
                tabs.remove(viewHolder.getAdapterPosition());
                tAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            }
        }
    });
}