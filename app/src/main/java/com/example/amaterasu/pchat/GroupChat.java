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

import com.example.amaterasu.pchat.app.Config;

import java.security.GeneralSecurityException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dell on 29/2/16.
 */
public class GroupChat extends AppCompatActivity {


    ArrayList<SelectUser> contactList;
    ListView listView;
    Cursor phones;
    ContentResolver resolver;
    SelectUserAdapter selectUserAdapter;

    private ImageView imageView;
    private SearchView searchView;
    private EditText editText;
    private SparseBooleanArray grpmembers;
    private int contact_Count;

    static public HashMap<String,ArrayList<String>> groupList = null;

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



        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        contactList = HomeScreen.contactList;

        selectUserAdapter = new SelectUserAdapter(contactList,GroupChat.this,true);
        listView.setAdapter(selectUserAdapter);
        //even the row click toggles the checkbox

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
        final SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //newText is text entered by user to SearchView
                selectUserAdapter.filter(newText);
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
                    String groupName = editText.getText().toString();
                    //add myself
                    boolNameHolder.groupMemberContactList.add(SmsActivity.pref.getMobileNumber());
                    //
                    if(groupList==null)
                        groupList = new HashMap<String,ArrayList<String>>();
                    groupList.put(groupName,boolNameHolder.groupMemberContactList);
                    //
                    setGroupRowForAllMem(groupName, boolNameHolder.groupMemberContactList);
                    Toast.makeText(getApplicationContext(), "New Group Created.", Toast.LENGTH_LONG).show();

                }
            }
        }

        if(id == android.R.id.home){
            onBackPressed();
            return true;
        }

        return true;
    }

    private boolean isValidGrpName(String grpname) {

        String name_regex = "^[A-Za-z]{1,}[.]{0,1}[A-Za-z]{0,}";
        Pattern name_patt = Pattern.compile(name_regex, Pattern.CASE_INSENSITIVE);
        Matcher name_match = name_patt.matcher(grpname);

        return name_match.find();

    }

    private void setGroupRowForAllMem(String grpName, ArrayList<String> grpMemberContactList) {

        Conversation conversation = new Conversation();

        conversation.setThumb(null);
        conversation.setName(grpName);
        conversation.setNumber(null);
        conversation.setGroupflag(true);
        //
        RecentFragment.conversations.add(conversation);
        RecentFragment.listView.setAdapter(RecentFragment.adapter);
        //send set_new group row to all

        for (String receiver :
                grpMemberContactList) {

            if (!receiver.equals(ChatScreen.me)) {

                ChatMessage chatMessage = new ChatMessage();
                chatMessage.setSendFlag(true);
                chatMessage.setType(Config.SET_NEW_GROUP);
                chatMessage.setSender(ChatScreen.me);
                chatMessage.setReceiver(receiver);
                //send the groupinfo
                chatMessage.setGroupname(grpName);
                chatMessage.setGroupmembers(grpMemberContactList);

                try {
                    ChatScreen.sendRecvChatMessage(chatMessage);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (GeneralSecurityException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private BoolNameHolder checkedBoxInfo(int contactCount){

        SelectUser data;
        int count=0;
        ArrayList<String> groupMemberNumbers = new ArrayList<String>();
        String parsedNumberMember;

        NetworkUtil networkUtil = new NetworkUtil();

        BoolNameHolder boolNameHolder = new BoolNameHolder();

        for (int i = 0; i < contactCount; i++) {
            data=contactList.get(i);
            if (data.getCheckedBox() == true) {
                parsedNumberMember = networkUtil.getParsedMobile(data.getPhone());
                groupMemberNumbers.add(parsedNumberMember);
                count++;
            }
        }

        boolNameHolder.groupMemberContactList=groupMemberNumbers;
        boolNameHolder.hasMinMembers=true;

        if(count == 0)
            boolNameHolder.hasMinMembers=false;


        return boolNameHolder;
    }


    static class BoolNameHolder{
        boolean hasMinMembers;
        ArrayList<String> groupMemberContactList;
    }


}