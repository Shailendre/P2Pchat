package com.example.amaterasu.pchat;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.List;

/**
 * Created by dell on 29/2/16.
 */
public class Account extends AppCompatActivity {

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account);

        ListView listView = (ListView) findViewById(R.id.account_listView);
        final Context context = this;
        int[] accountImages = {R.drawable.ic_account_change_name, R.drawable.ic_account_delete_account};
        String[] accountTextView = {"Change Name", "Delete Account"};

        listView.setAdapter(new AccountAdapter(this, accountImages, accountTextView));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = null;

                if (i == 0) {
                    invokePrompt(context,"",true);
                }
                if (i == 1)
                    invokePrompt(context,"Are you sure you want to delete your account?",false);

            }
        });
    }


    public void invokePrompt(Context context,String msg, boolean setEditText){

        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage(msg);
        builder1.setCancelable(true);
        String positiveMsg = "Yes",negativeMsg="No";


        if(setEditText){
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View promptView = layoutInflater.inflate(R.layout.change_name, null);
            builder1.setView(promptView);
            positiveMsg = "Change It";
            negativeMsg = "Cancel";
        }



        builder1.setPositiveButton(
                positiveMsg,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });


        builder1.setNegativeButton(
                negativeMsg,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();

    }
}


