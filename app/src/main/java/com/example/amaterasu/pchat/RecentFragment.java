package com.example.amaterasu.pchat;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.nfc.Tag;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 15/2/16.
 */
public class RecentFragment extends Fragment {

    static ArrayList<Conversation> conversations;

    static ListView listView;

    static ConversationAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rf_View = inflater.inflate(R.layout.fragment_recent, container, false);
        conversations = new ArrayList<Conversation>();
        listView = (ListView) rf_View.findViewById(R.id.chat_list);

        //for recent chats

        DBHandlerMsg db= new DBHandlerMsg(getActivity());
        List<Conversation> convs = db.getAllConv();
        if(convs!=null) {
            Log.e("Recent Fragment", "Starting For Loop");
            for (Conversation conv : convs) {
                Conversation conversation = new Conversation();
                conversation.setName(conv.getName());

                conversation.setNumber(conv.getNumber());
                Log.e("Recent Fragment", "Inside Loop");
                conversations.add(conversation);
            }
        }

        adapter = new ConversationAdapter(conversations,getContext());
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Conversation data = conversations.get(i);

                Intent intent = new Intent(getContext(), ChatScreen.class);

                intent.putExtra("user_name", data.getName());
                intent.putExtra("mobile", data.getNumber());
                intent.putExtra("group-flag","true");

                if(!data.getGroupflag()) {
                    intent.putExtra("status", OnlineFragment.onlinehash.get(data.getNumber()));
                    intent.putExtra("group-flag","false");
                }


                startActivity(intent);

            }
        });
        //

        return rf_View;
    }

}
