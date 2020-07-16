package org.trpg.filingua;

import android.os.Bundle;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.trpg.filingua.R;

import java.util.ArrayList;
import java.util.List;

public class swipeActivity extends AppCompatActivity {
    RecyclerView mRecyclerViewList;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pinned_tab);

        mRecyclerViewList = findViewById(R.id.recyclerView_main);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecyclerViewList.setHasFixedSize(false);
        mRecyclerViewList.setLayoutManager(manager);
        mRecyclerViewList.setAdapter(new RecyclerView.Adapter(createData()) {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return null;
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            }

            @Override
            public int getItemCount() {
                return 0;
            }
        });
    }

    private List<NoteDataModel> createData() {
        List<NoteDataModel> list = new ArrayList<>();
        for (int index = 0; index < 100; index++) {
            // 一行分のデータ
            NoteDataModel rowData = new NoteDataModel();
         /*   rowData.setBitmap(/*なんかの画像*/);
          /*  rowData.setmString("この行は" + (index + 1) + "回目の繰り返しです。");*/
            list.add(rowData);
        }
        return list;
    }
}
