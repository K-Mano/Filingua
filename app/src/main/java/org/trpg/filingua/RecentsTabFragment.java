package org.trpg.filingua;

import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class RecentsTabFragment extends Fragment {
    public RecentsTabFragment(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        return inflater.inflate(R.layout.recents_tab, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
    }
}
