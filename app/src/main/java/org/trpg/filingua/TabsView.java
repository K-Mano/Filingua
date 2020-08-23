package org.trpg.filingua;

import android.graphics.Canvas;
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

public class TabsView extends Fragment {

    public TabsView(List<FilinguaDatabase.Tab> tabs) {
        this.tabs = tabs;
    }

    private List<FilinguaDatabase.Tab> tabs;
    private RecyclerView tabsList;

    private TabAdapter tAdapter;

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
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            if(tabs.get(viewHolder.getAdapterPosition()).isRemovable()){
                tabs.remove(viewHolder.getAdapterPosition());
                tAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            }
        }
    });
}