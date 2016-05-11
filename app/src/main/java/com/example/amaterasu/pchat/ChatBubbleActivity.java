package com.example.amaterasu.pchat;

import android.app.Activity;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;



//this class is extra not useed anywhere; had same stuff as chatscreen.class


public class ChatBubbleActivity extends Activity {
    private static final String TAG = ChatBubbleActivity.class.getSimpleName();

    static ChatArrayAdapter chatArrayAdapter;
    static ListView listView;
    static EditText chatText;
    static Button buttonSend;

    Intent intent;
    static boolean side = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        setContentView(R.layout.activity_chat);
    }
}