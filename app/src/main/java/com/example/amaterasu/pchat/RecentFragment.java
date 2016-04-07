package com.example.amaterasu.pchat;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        /*
        * when conversations are stored
        *
        LoadConversation loadConversation = new LoadConversation(conversations,listView,adapter,getContext());
        loadConversation.execute();
        */

        //Create dummy conversation
        Conversation conversation = new Conversation();
        conversation.setThumb(null);
        conversation.setName("DConv");
        conversation.setDate(UtilityClass.getTimeStamp());

        conversations.add(conversation);

        adapter = new ConversationAdapter(conversations,getContext());
        listView.setAdapter(adapter);


        return rf_View;
    }

}
