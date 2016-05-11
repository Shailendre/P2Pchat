package com.example.amaterasu.pchat;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.ContactsContract;
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
import android.util.Log;
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

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.acl.Group;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

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

    static final String TAG = HomeScreen.class.getSimpleName();

    Cursor phones;
    static ArrayList<SelectUser> contactList=null;
    static HashMap<String,String> contacthash = null;
    static SelectUserAdapter searchAdapterObj;

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

        //

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


        //important startup tasks
        //1st fetch the contactlist
        try {
            //singleton approach
            if(contactList==null)
                contactList = getContactsAsync();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //2nd start the server send the mobiles ip to server
        NetworkUtil networkUtil = new NetworkUtil();
        networkUtil.startInternalServerThread();
        networkUtil.keep_alive_thread(this);
        //send the ip;
        try {
            //interval approach is left
            networkUtil.sendMobileIPToServer(networkUtil.wifiIpAddress(this));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //3rd set the online hash
        setContactHash(networkUtil);

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
                Log.e(TAG, "onQueryTextChange: " );
                searchAdapterObj.filter(newText);
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


    public ArrayList<SelectUser> getContactsAsync() throws ExecutionException, InterruptedException {
        phones = HomeScreen.this.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
        ArrayList<SelectUser> contactList = new LoadContact(phones,HomeScreen.this).execute().get();
        return contactList;
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    public void setContactHash(NetworkUtil netutil){
        contacthash = new HashMap<String,String>();
        for (SelectUser su:
             contactList) {
            contacthash.put(netutil.getParsedMobile(su.getPhone()),su.getName());
        }
    }

}