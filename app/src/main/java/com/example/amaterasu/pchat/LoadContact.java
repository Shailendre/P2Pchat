package com.example.amaterasu.pchat;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.amaterasu.pchat.ChatScreen;
import com.example.amaterasu.pchat.SelectUser;
import com.example.amaterasu.pchat.SelectUserAdapter;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// Load data on background
class LoadContact extends AsyncTask<Void, Void, ArrayList<SelectUser>> {

    ArrayList<SelectUser> contactlist;
    Cursor phones;
    Context context;
    ProgressDialog progressDialog=null;

    public LoadContact(Cursor phones, Context context){
        this.contactlist = new ArrayList<SelectUser>();
        this.phones = phones;
        this.context = context;
    }

    public ArrayList<SelectUser> getSelectUsers(){
        return contactlist;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = ProgressDialog.show(context,"Loading...","Please be patient while we retrieve your contact list.");

    }

    @Override
    protected ArrayList<SelectUser> doInBackground(Void... voids) {
        // Get Contact list from Phone
        if (phones != null) {
            Log.e("count", "" + phones.getCount());
            if (phones.getCount() == 0) {
                Toast.makeText(context, "No contacts in your contact list.", Toast.LENGTH_LONG).show();
            }
            while (phones.moveToNext()) {
                String id = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
                String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                SelectUser selectUser = new SelectUser();

                selectUser.setName(name);
                selectUser.setPhone(phoneNumber);
                selectUser.setStatus("Offline");
                contactlist.add(selectUser);

            }
        }
        return contactlist;
    }


    @Override
    protected void onPostExecute(ArrayList<SelectUser> contactlist) {
        super.onPostExecute(contactlist);
        if(progressDialog!=null)
            progressDialog.dismiss();
    }

    /*
    public void setAdapter(ArrayList<SelectUser> selectUsers){
        selectUserAdapter = new SelectUserAdapter(selectUsers, context,showCheckBox);
        listView.setAdapter(selectUserAdapter);
        listView.setFastScrollEnabled(true);
    }*/
}