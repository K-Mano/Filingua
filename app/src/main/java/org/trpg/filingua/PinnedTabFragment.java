package org.trpg.filingua;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ListAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;



public class PinnedTabFragment extends Fragment {
    private RecyclerAdapter sAdapter;

    public RecyclerAdapter getsAdapter() {
        return sAdapter;
    }

    public void setsAdapter(RecyclerAdapter sAdapter) {
        this.sAdapter = sAdapter;
    }

    public PinnedTabFragment(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        int hoge=5;
        View view = inflater.inflate(R.layout.pinned_tab, container, false);

        RecyclerView tabview      = view.findViewById(R.id.listview1);
        LinearLayoutManager tManager = new LinearLayoutManager(getActivity());
        tManager.setOrientation(LinearLayoutManager.VERTICAL);
        tabview.setLayoutManager(tManager);

        tabview.setHasFixedSize(true);
        sAdapter  = new RecyclerAdapter(createObject(hoge),R.layout.pin_card);
        tabview.setAdapter(sAdapter);
        swipeToDismissTouchHelper.attachToRecyclerView(tabview);


        return view;
    }




        ItemTouchHelper swipeToDismissTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped( RecyclerView.ViewHolder viewHolder, int direction) {
                    List<FilinguaDatabase.DefaultDataSet> cardList = new ArrayList<>();
                    cardList.remove(viewHolder.getAdapterPosition());
                    sAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            }

            @Override
            public void onChildDrawOver(Canvas c, RecyclerView recyclerView,
                                        RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                        int actionState, boolean isCurrentlyActive) {
            }

        });





    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
    }

    private List<FilinguaDatabase.DefaultDataSet> createObject(int amount){
        List<FilinguaDatabase.DefaultDataSet> DefaltDataSetList = new ArrayList<>();
        for(int i=0;i<amount;i++){
            FilinguaDatabase.DefaultDataSet object = new FilinguaDatabase.DefaultDataSet("a"+i);

            DefaltDataSetList.add(object);
        }
        return DefaltDataSetList;
    }
}

