package com.example.amaterasu.pchat;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.SearchView;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
//import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amaterasu.pchat.ContactsFragment;

import java.security.acl.Group;

public class HomeScreen extends AppCompatActivity{
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of tsections. We ua
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.

     /**
     * The {@link ViewPager} that will host the section contents.
     */

    SectionPagerAdapter obSectionPagerAdapter;
    ViewPager obViewPager;
    SlidingTabLayout tabs;
    CharSequence[] TabTitles={"ONLINE", "CHATS", "CONTACTS"};
    int NumOfTabs=3;


    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        obSectionPagerAdapter = new SectionPagerAdapter(getSupportFragmentManager(),TabTitles,NumOfTabs);

        // Set up the ViewPager with the sections adapter.
        obViewPager = (ViewPager) findViewById(R.id.container);
        obViewPager.setAdapter(obSectionPagerAdapter);
        //
        tabs=(SlidingTabLayout)findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true);
        //
        tabs.setViewPager(obViewPager);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu_home_screen, menu);


        searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.icon_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // newText is text entered by user to SearchView
                LoadContact.selectUserAdapter.filter(newText);
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.icon_settings)
            startActivity(new Intent(this,Settings.class));

        if (id == R.id.icon_search){}

        if(id == R.id.icon_group_chat)
            startActivity(new Intent(this, GroupChat.class));

        return super.onOptionsItemSelected(item);
    }


}