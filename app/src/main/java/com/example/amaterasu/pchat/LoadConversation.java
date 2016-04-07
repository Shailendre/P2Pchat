package com.example.amaterasu.pchat;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.widget.ListView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by dell on 2/3/16.
 */
public class LoadConversation extends AsyncTask<Void,Void, Void> {

    private ArrayList<Conversation> conversations;
    private ListView listView;
    static ConversationAdapter conversationAdapter;
    private Context context;


    public LoadConversation(ArrayList<Conversation> conversations, ListView listView, ConversationAdapter adapter,Context context) {
        this.conversations = conversations;
        this.listView = listView;
        this.context = context;
        conversationAdapter=adapter;
    }

    public ArrayList<Conversation> getConversations(){
        return conversations;
    }

    public ListView getListView(){
        return  listView;
    }

    public Context getContext(){
        return context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }
    @Override
    protected Void doInBackground(Void... params) {
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        conversationAdapter = new ConversationAdapter(conversations, context);
        listView.setAdapter(conversationAdapter);

        listView.setFastScrollEnabled(true);
    }
}
