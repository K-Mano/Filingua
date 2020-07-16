package org.trpg.filingua;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;

public class RecentsTabFragment extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        // Viewを設定
        View rootView = inflater.inflate(R.layout.recents_tab, container, false);
        // RecyclerViewを取得
        RecyclerView recyclerView = rootView.findViewById(R.id.quickaccess);
        RecyclerView recentView   = rootView.findViewById(R.id.recent_activity);
        // LayoutManagerを設定
        LinearLayoutManager lManager = new LinearLayoutManager(getActivity());
        lManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(lManager);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager vManager = new LinearLayoutManager(getActivity());
        vManager.setOrientation(LinearLayoutManager.VERTICAL);
        recentView.setLayoutManager(vManager);
        DividerItemDecoration divider = new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL);
        recentView.addItemDecoration(divider);
        recentView.setHasFixedSize(true);
        // Adapterを作成
        RecyclerAdapter rAdapter = new RecyclerAdapter(createObject(), R.layout.qa_card);
        RecyclerAdapter aAdapter = new RecyclerAdapter(createObject(), R.layout.ra_card);
        // Adapterを設定
        recyclerView.setAdapter(rAdapter);
        recentView.setAdapter(aAdapter);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
    }

    private List<FilinguaDatabase.CardObject> createObject(){
        List<FilinguaDatabase.CardObject> cardObjectList = new ArrayList<>();
        for(int i=0;i<4;i++){
            FilinguaDatabase.CardObject object = new FilinguaDatabase.CardObject();
            object.title="card"+i;
            cardObjectList.add(object);
        }
        return cardObjectList;
    }
}
