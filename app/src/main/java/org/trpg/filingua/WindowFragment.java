package org.trpg.filingua;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    public WindowFragment() {

    }

    // TODO: Rename and change types and number of parameters
    public static WindowFragment newInstance(int count) {
        WindowFragment fragment = new WindowFragment();
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
        return inflater.inflate(R.layout.fragment_window, container, false);
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
}