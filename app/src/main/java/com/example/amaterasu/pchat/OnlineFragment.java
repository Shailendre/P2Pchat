package com.example.amaterasu.pchat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.support.v7.widget.SearchView;
import android.widget.Toast;

import com.example.amaterasu.pchat.SelectUserAdapter;
import com.example.amaterasu.pchat.SelectUser;
import com.example.amaterasu.pchat.app.Config;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;


/**
 * Created by dell on 15/2/16.
 */
public class OnlineFragment extends Fragment {

    // ArrayList
    ArrayList<SelectUser> selectUsers, onlineUsers=null;
    //HashMap for online users
    static HashMap<String,String> onlinehash = new HashMap<String,String>();
    // Contact List
    ListView listView;
    // Cursor to load contacts list
    Cursor phones, email;
    // Pop up
    ContentResolver resolver;
    SearchView search;
    OnlineUserAdapter onlineUserAdapter;
    SelectUserAdapter selectUserAdapter;
    final String TAG = "Online Frag";
    private SwipeRefreshLayout swipeContainer;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View cf_View = inflater.inflate(R.layout.fragment_online, container, false);
        listView = (ListView) cf_View.findViewById(R.id.online_list);

        swipeContainer = (SwipeRefreshLayout) cf_View.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                //fetchTimelineAsync(0);
                NetworkUtil networkUtil = new NetworkUtil();
                try {
                    onlineUsers = new LoadOnlineUsers(networkUtil,getContext()).execute(selectUsers).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                swipeContainer.setRefreshing(false);

            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        //from Homescreen prefetched approach
        selectUsers = HomeScreen.contactList;

        NetworkUtil netutil = new NetworkUtil();
        try {
            //singleton approach
            if(this.onlineUsers == null)
                this.onlineUsers = new LoadOnlineUsers(netutil,getContext()).execute(selectUsers).get();
            else
                new LoadOnlineUsers(netutil,getContext()).onPostExecute(onlineUsers);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                SelectUser data = onlineUsers.get(i);

                Intent intent = new Intent(getContext(), ChatScreen.class);
                intent.putExtra("user_name", data.getName());
                intent.putExtra("mobile",data.getPhone());
                intent.putExtra("group-flag","false");
                intent.putExtra("status",data.getStatus());
                startActivity(intent);

            }
        });

        return cf_View;
    }


    public class LoadOnlineUsers extends  AsyncTask<ArrayList<SelectUser>,Void,ArrayList<SelectUser>>{

        NetworkUtil netutil;
        Context context;
        ProgressDialog progressDialog=null;
        public LoadOnlineUsers(NetworkUtil netutil, Context context){
            this.netutil = netutil;
            this.context = context;
        }

        @Override
        protected ArrayList<SelectUser> doInBackground(ArrayList<SelectUser>... params) {
            ArrayList<SelectUser> selectUsers = params[0];
            final ArrayList<SelectUser> onlineusers = new ArrayList<SelectUser>();
            for (final SelectUser su:
                 selectUsers) {
                final String sendablenumber = netutil.getParsedMobile(su.getPhone());
                final String status=null;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String jsonstringstatus = netutil.new GetStatusAsync().execute(sendablenumber).get();
                            Log.e(TAG, "run: jsonstring:"+jsonstringstatus);
                            JSONObject jsonObj = new JSONObject(jsonstringstatus);
                            JSONObject jsonresponse = jsonObj.getJSONObject("response");
                            String status = jsonresponse.getString("status");
                            //logic for online status
                            if(status.equals("1")) {
                                status="Online";
                                onlineusers.add(su);
                                Log.e(TAG, "run: user status: online");
                            }
                            else{
                                status="Offline";
                                Log.e(TAG, "run: user status: offline" );
                            }
                            su.setStatus(status);
                            //set hash for online status
                            onlinehash.put(sendablenumber,status);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
            return onlineusers;
        }

        @Override
        protected void onPostExecute(ArrayList<SelectUser> onlineusers) {
            super.onPostExecute(onlineusers);
            if(progressDialog!=null)
                progressDialog.dismiss();
            onlineUserAdapter = new OnlineUserAdapter(onlineusers,context);
            listView.setAdapter(onlineUserAdapter);
            listView.setFastScrollEnabled(true);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=ProgressDialog.show(getContext(),"Loading...","Please be patient while we get your online buddies.");
        }
    }


}


