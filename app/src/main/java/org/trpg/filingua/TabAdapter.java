package org.trpg.filingua;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TabAdapter extends FragmentPagerAdapter {
    private CharSequence[] tabTitles={"recents","pinned"};
    public TabAdapter(FragmentManager fm) {
        super(fm);
    }
    @Override
    public CharSequence getPageTitle(int position){
        return tabTitles[position];
    }
    @Override
    public Fragment getItem(int position){
        switch(position){
            case 0:
                return new RecentsTabFragment();
            case 1:
                return new PinnedTabFragment();
            default:
                return null;
        }
    }
    @Override
    public int getCount(){
        return tabTitles.length;
    }
}
