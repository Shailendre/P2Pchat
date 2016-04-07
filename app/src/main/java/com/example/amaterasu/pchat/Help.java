package com.example.amaterasu.pchat;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by dell on 29/2/16.
 */
public class Help extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help);
    }

    public void about(View view){
        Intent intent = null;
        intent = new Intent(Help.this, About.class);
        startActivity(intent);
    }

    public void FAQ(View view){
        Intent intent = null;
        intent = new Intent(Help.this, Faq.class);
        startActivity(intent);
    }

    public void contactUs(View view){
        Intent intent = null;
        intent = new Intent(Help.this, ContactUs.class);
        startActivity(intent);
    }

}
