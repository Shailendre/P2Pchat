package com.example.amaterasu.pchat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * Created by dell on 20/2/16.
 */
public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        ListView listView=(ListView)findViewById(R.id.settings_listView);

        int[] settingsImages={R.drawable.ic_settings_help,R.drawable.ic_settings_account,R.drawable.ic_settings_invite};
        String[] settingsTextView={"Help","Account","Invite friends"};

        listView.setAdapter(new SettingsAdapter(this,settingsImages,settingsTextView));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = null;

                if(i == 0)
                    intent=new Intent(Settings.this,Help.class);
                if(i == 1)
                    intent=new Intent(Settings.this,Account.class);
                if(i == 2)
                    intent=new Intent(Settings.this,FindFriends.class);


                startActivity(intent);

            }
        });

    }

}
