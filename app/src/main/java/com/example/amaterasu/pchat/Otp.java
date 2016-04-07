package com.example.amaterasu.pchat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by dell on 14/2/16.
 * this class is for rendering otp activity.
 */
public class Otp extends AppCompatActivity {


    Button nxt_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otp_activity);

        //initialize the components
        nxt_btn = (Button) findViewById(R.id.nxt_btn);


        //define what happens on next_btn click
        nxt_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Otp.this, HomeScreen.class));
            }
        });

    }
}
