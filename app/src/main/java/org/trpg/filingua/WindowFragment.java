package org.trpg.filingua;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_DRAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WindowFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WindowFragment extends Fragment {

    // パラメータ
    private static final String ARG_COUNT = "count";

    private String tCount;
    private int cnt;

    private String currentPath;
    private DirItemsAdapter iAdapter;
    private List<File> items = new ArrayList<>();

    private RecyclerView dirItems;

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

        File[] fileArray = new File(currentPath).listFiles();
        for(int i=0; fileArray.length>i; i++){
            items.add(fileArray[i]);
        }

        // RecyclerViewを取得
        dirItems = rootView.findViewById(R.id.dir_items);

        // LayoutManagerを設定
        LinearLayoutManager lManager = new LinearLayoutManager(getActivity());
        lManager.setOrientation(LinearLayoutManager.VERTICAL);
        dirItems.setLayoutManager(lManager);
        dirItems.setHasFixedSize(true);

        // スワイプ機能
        swipeTouchHelper.attachToRecyclerView(dirItems);

        // Adapterを作成
        iAdapter = new DirItemsAdapter(items);

        // RecyclerViewのitemへのonClickListener紐づけ
        iAdapter.setOnItemClickListener(new DirItemsAdapter.onItemClickListener() {
            @Override
            public void onClick(View view, int pos) {

            }
        });

        dirItems.setAdapter(iAdapter);
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
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            items.remove(viewHolder.getAdapterPosition());
            iAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
        }
    });
}