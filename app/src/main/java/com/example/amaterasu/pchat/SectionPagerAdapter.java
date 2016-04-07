package com.example.amaterasu.pchat;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by dell on 16/2/16.
 */
public class SectionPagerAdapter extends FragmentPagerAdapter {

    CharSequence[] TabTitles;
    int NumOfTabs;

    public SectionPagerAdapter(FragmentManager fm,CharSequence mTitles[], int mNumbOfTabsumb) {
        super(fm);

        this.TabTitles=mTitles;
        this.NumOfTabs=mNumbOfTabsumb;

    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).

        switch (position){
            case 0:
                OnlineFragment obOnlineFragment=new OnlineFragment();
                return obOnlineFragment;
            case 1:
                RecentFragment obRecentFragment=new RecentFragment();
                return obRecentFragment;
            case 2:
                ContactsFragment obContactsFragment=new ContactsFragment();
                return obContactsFragment;
            default:
                return null;

        }

    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TabTitles[position];
    }
}

