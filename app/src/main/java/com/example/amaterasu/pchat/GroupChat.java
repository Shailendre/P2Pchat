package com.example.amaterasu.pchat;

import android.app.SearchManager;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dell on 29/2/16.
 */
public class GroupChat extends AppCompatActivity {


    ArrayList<SelectUser> selectUsers;
    ListView listView;
    Cursor phones;
    ContentResolver resolver;
    SelectUserAdapter adapter;

    private ImageView imageView;
    private SearchView searchView;
    private EditText editText;
    private SparseBooleanArray grpmembers;
    private int contact_Count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_chat);

        Toolbar toolbar = (Toolbar) findViewById(R.id.groupchat_toolbar);
        setSupportActionBar(toolbar);


        imageView = (ImageView) findViewById(R.id.groupchat_imageView);
        imageView.setImageResource(R.drawable.ic_groupchat_icon);

        editText = (EditText) findViewById(R.id.groupchat_name);

        listView = (ListView) this.findViewById(R.id.groupchat_listView);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        selectUsers = new ArrayList<SelectUser>();
        resolver = this.getContentResolver();

        phones = this.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");


        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        LoadContact loadContact = new LoadContact(selectUsers, listView, phones, resolver, adapter, this, true);
        loadContact.execute();

        //even the row click toggles the checkbox
        listView = loadContact.getListView();
        selectUsers = loadContact.getSelectUsers();
        /*
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                SelectUser data = selectUsers.get(position);

                CheckBox checkBox = (CheckBox) view;
                if (checkBox.isChecked()) {
                    data.setCheckedBox(true);
                } else {
                    data.setCheckedBox(false);
                }

            }
        });
        */

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_groupchat_screen, menu);

        searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.icon_groupchat_search));
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

        int id = item.getItemId();

        if (id == R.id.icon_groupchat_next) {
            if (!isValidGrpName(editText.getText().toString())) {
                Toast.makeText(getApplicationContext(), "Invalid Group Name!", Toast.LENGTH_LONG).show();
            } else {
                contact_Count = listView.getCount();
                BoolNameHolder boolNameHolder = checkedBoxInfo(contact_Count);
                if (!boolNameHolder.hasMinMembers)
                    Toast.makeText(getApplicationContext(), "Atleast one member required. "+ contact_Count, Toast.LENGTH_LONG).show();
                    //when all condition area met
                else {
                    setGroupRow(editText.getText().toString(),boolNameHolder.groupNames);
                    Toast.makeText(getApplicationContext(), "New Group Created.", Toast.LENGTH_LONG).show();

                }
            }
        }
        return true;
    }

    private boolean isValidGrpName(String grpname) {

        String name_regex = "^[A-Za-z]{1,}[.]{0,1}[A-Za-z]{0,}";
        Pattern name_patt = Pattern.compile(name_regex, Pattern.CASE_INSENSITIVE);
        Matcher name_match = name_patt.matcher(grpname);

        return name_match.find();

    }

    private void setGroupRow(String grpName, String grpMemNames) {

        Conversation conversation = new Conversation();

        conversation.setThumb(null);
        conversation.setName(grpName);
        conversation.setDate(grpMemNames);
        //
        RecentFragment.conversations.add(conversation);
        RecentFragment.listView.setAdapter(RecentFragment.adapter);

    }


    private BoolNameHolder checkedBoxInfo(int contactCount){

        SelectUser data;
        int count=0;
        String groupNames="";

        BoolNameHolder boolNameHolder = new BoolNameHolder();

        for (int i = 0; i < contactCount; i++) {
            data=selectUsers.get(i);
            if (data.getCheckedBox() == true) {
                groupNames +=(data.getName() + " ");
                count++;
            }
        }

        boolNameHolder.groupNames=groupNames;
        boolNameHolder.hasMinMembers=true;

        if(count == 0)
            boolNameHolder.hasMinMembers=false;


        return boolNameHolder;
    }


    static class BoolNameHolder{
        boolean hasMinMembers;
        String groupNames;
    }

}