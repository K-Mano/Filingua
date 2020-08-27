package org.trpg.filingua;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;




public class PinnedTabFragment extends Fragment {
    List<FilinguaDatabase.DefaultDataSet> cardList;
    public RecyclerAdapter sAdapter;

    //RecyclerView recyclerView;



    public RecyclerAdapter getsAdapter() {
        return sAdapter;
    }

    public void setsAdapter(RecyclerAdapter sAdapter) {
        this.sAdapter = sAdapter;
    }

    int swipedPos = -1;
    public PinnedTabFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        int hoge=5;

        View view = inflater.inflate(R.layout.pinned_tab, container, false);

        cardList=createObject(hoge);

        RecyclerView tabview      = view.findViewById(R.id.listview1);
        LinearLayoutManager tManager = new LinearLayoutManager(getActivity());
        tManager.setOrientation(LinearLayoutManager.VERTICAL);
        tabview.setLayoutManager(tManager);

        tabview.setHasFixedSize(true);
        sAdapter  = new RecyclerAdapter(cardList,R.layout.pin_card);
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




            cardList.remove(viewHolder.getAdapterPosition());
            sAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());


        }
        public static final float ALPHA_FULL = 1.0f;

        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView,
                                RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                int actionState, boolean isCurrentlyActive) {
            if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                // Get RecyclerView item from the ViewHolder
                View itemView = viewHolder.itemView;
                //  ApplicationManager lManager = new ApplicationManager(getActivity());


                Paint p = new Paint();
                Drawable icon;
                Bitmap icons;
                Resources r = getResources();
                icon= getResources().getDrawable(R.drawable.anim_home_pin);
                if (dX > 0) {
                    /* Set your color for positive displacement */
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inPreferredConfig = Bitmap.Config.ARGB_8888;








                    p.setARGB(255, 255, 0, 0);


                    // Draw Rect with varying right side, equal to displacement dX
                    c.drawRect((float) itemView.getLeft(), (float) itemView.getTop(), dX,
                            (float) itemView.getBottom(), p);

                    // Set the image icon for Right swipe

                    icons=drawableToBitmap(icon,96,96,itemView.getLeft()+(itemView.getBottom()-itemView.getTop())/2, (itemView.getTop()+itemView.getBottom())/2,Color.WHITE);
                    c.drawBitmap(icons,
                            new Matrix(),

                            p);
                } else {


                    //      icons = drawableToBitmap(icon);

                    /* Set your color for negative displacement */
                    p.setARGB(255, 0, 255, 0);

                    // Draw Rect with varying left side, equal to the item's right side plus negative displacement dX
                    c.drawRect((float) itemView.getRight() + dX, (float) itemView.getTop(),
                            (float) itemView.getRight(), (float) itemView.getBottom(), p);

                    //Set the image icon for Left swipe

                    icons=drawableToBitmap(icon,96,96,itemView.getRight()-(itemView.getBottom()-itemView.getTop())/2, (itemView.getTop()+itemView.getBottom())/2,Color.WHITE);
                    c.drawBitmap(icons,
                            new Matrix(),

                            p);
                }
                // Fade outっぽいやつ
                final float alpha = ALPHA_FULL - Math.abs(dX) / (float) viewHolder.itemView.getWidth();
                viewHolder.itemView.setAlpha(alpha);
                viewHolder.itemView.setTranslationX(dX);

            } else {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        }







    });








    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
    }
    private int convertDpToPx(int dp){
        return Math.round(dp * (getResources().getDisplayMetrics().xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }




    private List<FilinguaDatabase.DefaultDataSet> createObject(int amount){
        List<FilinguaDatabase.DefaultDataSet> DefaltDataSetList = new ArrayList<>();
        for(int i=0;i<amount;i++){
            FilinguaDatabase.DefaultDataSet object = new FilinguaDatabase.DefaultDataSet("a"+i);

            DefaltDataSetList.add(object);
        }
        return DefaltDataSetList;
    }
    public static Bitmap drawableToBitmap(Drawable drawable, int height, int width, int centerX, int centerY, int color){

        if(drawable instanceof BitmapDrawable){
            return ((BitmapDrawable)drawable).getBitmap();
        }

        int left = centerX-(width/2);
        int top = centerY-(height/2);

        Bitmap bitmap = Bitmap.createBitmap(2000, 2000, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        PorterDuff.Mode mode = PorterDuff.Mode.SRC_ATOP;
        drawable.setBounds(left, top, left+width, top+height);
        drawable.setColorFilter(color, mode);
        drawable.draw(canvas);

        return bitmap;
    }



}
