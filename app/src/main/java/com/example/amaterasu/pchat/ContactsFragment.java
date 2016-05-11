package com.example.amaterasu.pchat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.support.v7.widget.SearchView;
import android.widget.Toast;

import com.example.amaterasu.pchat.SelectUserAdapter;
import com.example.amaterasu.pchat.SelectUser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by dell on 15/2/16.
 */
public class ContactsFragment extends Fragment {

    // ArrayList
    ArrayList<SelectUser> selectUsers;
    // Contact List
    ListView listView;

    SelectUserAdapter adapter;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View cf_View = inflater.inflate(R.layout.fragment_contacts, container, false);
        this.selectUsers = HomeScreen.contactList;
        this.listView = (ListView) cf_View.findViewById(R.id.contacts_list);

        this.adapter = new SelectUserAdapter(selectUsers,getContext(),false);
        HomeScreen.searchAdapterObj = adapter;
        listView.setAdapter(this.adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                SelectUser data = selectUsers.get(i);

                Intent intent = new Intent(getContext(), ChatScreen.class);
                intent.putExtra("user_name", data.getName());
                intent.putExtra("group-flag","false");
                intent.putExtra("mobile",data.getPhone());
                intent.putExtra("status",data.getStatus());

                startActivity(intent);

            }
        });
        return cf_View;
    }



}


