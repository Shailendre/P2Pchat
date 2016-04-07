package com.example.amaterasu.pchat;

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
import java.util.ArrayList;
import java.util.List;

// Load data on background
class LoadContact extends AsyncTask<Void, Void, Void> {

    private ArrayList<SelectUser> selectUsers;
    private ListView listView;
    private Cursor phones;
    private ContentResolver resolver;
    static SelectUserAdapter selectUserAdapter;
    private Context context;
    private boolean showCheckBox;

    public LoadContact(ArrayList<SelectUser> selectUsers, ListView listView,Cursor phones, ContentResolver resolver, SelectUserAdapter __adapter, Context context,boolean showCheckBox){

        this.selectUsers = selectUsers;
        this.listView = listView;
        this.phones = phones;
        this.resolver = resolver;
        selectUserAdapter = __adapter;
        this.context = context;
        this.showCheckBox = showCheckBox;

    }

    public ArrayList<SelectUser> getSelectUsers(){
        return selectUsers;
    }

    public ListView getListView(){
        return listView;
    }

    public Cursor getPhones(){
        return phones;
    }

    public ContentResolver getResolver(){
        return resolver;
    }

    public SelectUserAdapter getAdapter(){
        return selectUserAdapter;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected Void doInBackground(Void... voids) {
        // Get Contact list from Phone

        if (phones != null) {
            Log.e("count", "" + phones.getCount());
            if (phones.getCount() == 0) {
                Toast.makeText(context, "No contacts in your contact list.", Toast.LENGTH_LONG).show();
            }

            while (phones.moveToNext()) {
                Bitmap bit_thumb = null;
                String id = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
                String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                String EmailAddr = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA2));
                String image_thumb = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI));
                try {
                    if (image_thumb != null) {
                        bit_thumb = MediaStore.Images.Media.getBitmap(resolver, Uri.parse(image_thumb));
                    } else {
                        Log.e("No Image Thumb", "--------------");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                SelectUser selectUser = new SelectUser();
                selectUser.setThumb(bit_thumb);
                selectUser.setName(name);
                selectUser.setPhone(phoneNumber);
                selectUser.setEmail(id);
                selectUser.setCheckedBox(false);
                selectUsers.add(selectUser);
            }
        } else {
            Log.e("Cursor close 1", "----------------");
        }
        //phones.close();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        selectUserAdapter = new SelectUserAdapter(selectUsers, context,showCheckBox);
        listView.setAdapter(selectUserAdapter);

        listView.setFastScrollEnabled(true);
    }
}