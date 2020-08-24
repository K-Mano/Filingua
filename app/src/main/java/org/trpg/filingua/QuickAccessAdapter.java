package org.trpg.filingua;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class QuickAccessAdapter extends RecyclerView.Adapter<QuickAccessAdapter.QuickAccessViewHolder>{

    private List<FilinguaDatabase.DefaultDataSet> list;
    private int layout;

    public QuickAccessAdapter(List<FilinguaDatabase.DefaultDataSet> dataSet, int layout) {
        this.list   = dataSet;
        this.layout = layout;
    }

    @Override
    public QuickAccessViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(layout,viewGroup,false);
        QuickAccessViewHolder viewHolder = new QuickAccessAdapter.QuickAccessViewHolder(inflate);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(QuickAccessAdapter.QuickAccessViewHolder viewHolder, int pos) {
        viewHolder.card.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //押したとき
                    //startScalingAnimEnter(v);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    //離したとき
                    //startScalingAnimExit(v);
                }
                return false;
            }
        });
    }

    /*
    public void startScalingAnimEnter(View view){
        Animation animation = AnimationUtils.loadAnimation(, R.anim.scale_animation_enter);
        view.startAnimation(animation);
    }

    public void startScalingAnimExit(View view){
        Animation animation = AnimationUtils.loadAnimation(, R.anim.scale_animation_exit);
        view.startAnimation(animation);
    }
    */

    @Override
    public int getItemCount() {
        return list.size();
    }

    //　ViewHolder(デフォルト)
    class QuickAccessViewHolder extends RecyclerView.ViewHolder{
        public CardView card;
        public QuickAccessViewHolder(View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.card);
            /// RecyclerViewのテスト用 ///
        }
    }
}
