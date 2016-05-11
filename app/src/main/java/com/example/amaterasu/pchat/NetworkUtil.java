package com.example.amaterasu.pchat;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.example.amaterasu.pchat.app.Config;
import com.example.amaterasu.pchat.helper.PrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.nio.ByteOrder;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

/**
 * Created by dell on 10/4/16.
 */
public class NetworkUtil extends AppCompatActivity{

    private final static String TAG = NetworkUtil.class.getSimpleName();
    private String JSON_STRING;
    public boolean status = false;

    //main server started on app started; always listens on port 50000
    public void startInternalServerThread() {

        Thread serverthread = new Thread() {
            @Override
            public void run() {
                try
                {
                    int port = 50000;
                    Socket socket;
                    ServerSocket serverSocket = new ServerSocket(port);
                    Log.e(TAG, "run: server started");
                    //server always listening
                    while(true) {
                        //Reading the message from the client
                        socket = serverSocket.accept();
                        Log.e(TAG, "run: recd from socket" + socket.getRemoteSocketAddress());
                        ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                        final ChatMessage msg =(ChatMessage) objectInputStream.readObject();

                        msg.setSendFlag(false);
                        Log.e(TAG, "run: from server: "+msg.getType() );
                        Log.e(TAG, "run: msg recd: " + msg.getSendFlag());

                        NetworkUtil.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    ChatScreen.sendRecvChatMessage(msg);
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                } catch (GeneralSecurityException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        };
        serverthread.start();
    }

    //thred to update timpstamp
    public void keep_alive_thread (final Context context){
        Thread keepalive_thread = new Thread() {
            String url_alive = "http://athena.nitc.ac.in/nikhil_b120105cs/update_time.php";
            long sleep_time = 1000;
            @Override
            public void run() {
                while(true){
                    try {
                        URL url = new URL(url_alive);
                        String our_ip = wifiIpAddress(context);
                        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                        httpURLConnection.setRequestMethod("POST");
                        httpURLConnection.setDoOutput(true);
                        //httpURLConnection.setDoInput(true);
                        OutputStream OS = httpURLConnection.getOutputStream();
                        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                        String data = URLEncoder.encode("ip", "UTF-8") + "=" + URLEncoder.encode(our_ip, "UTF-8");
                        bufferedWriter.write(data);
                        bufferedWriter.flush();
                        bufferedWriter.close();
                        OS.close();
                        InputStream IS = httpURLConnection.getInputStream();
                        IS.close();
                        //httpURLConnection.connect();
                        httpURLConnection.disconnect();
                        SystemClock.sleep(1000);


                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        keepalive_thread.start();
    }


    //thread to send msg to user the msg
    public void sendMsgToUser(final ChatMessage chatMessage,final String host)
    {
        Thread sendthread  = new Thread(){
            @Override
            public void run() {
                try
                {
                    Socket socket;
                    int port = 50000;
                    Log.e(TAG, "run: "+port );
                    InetAddress address = InetAddress.getByName(host);
                    Log.e(TAG, "run: addr"+address );
                    socket = new Socket(address, port);
                    //log.d for writing msg
                    Log.e(TAG, "SendMsgUtil: socket created");
                    //Send the message to the server
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                    Log.e(TAG, "SendMsgUtil: msg sent");
                    objectOutputStream.writeObject(chatMessage);
                }
                catch (Exception exception)
                {
                    exception.printStackTrace();
                }
            }
        };
        sendthread.start();
    }

    //clean the mobile number since it is stored in different format on mobile than on server
    public String getParsedMobile(String dirtynumber) {

        char[] mob = new char[10];
        int k = 0, i = 0;
        if (dirtynumber.charAt(0) == '+') i = 3;
        if (dirtynumber.charAt(0) == '0') i = 1;
        for (; i < dirtynumber.length() && k<10; i++) { //add k<10 if it fails here ; dunno why!
            if (dirtynumber.charAt(i) != ' ')
                mob[k++] = dirtynumber.charAt(i);
        }

        Log.e(TAG, "dirtynumber is" + dirtynumber);
        Log.e(TAG, " cleannumber is" + new String(mob));

        return new String(mob);
    }

    //main function that is called to get ip for specific mobile
    public String getSendingIP(String mobile) throws ExecutionException, InterruptedException {

        String parsedmobile = getParsedMobile(mobile);
        String getAsyncJson = new GetJsonData().execute().get();
        String sendingIP = parseJsonDataForIP(getAsyncJson,parsedmobile);
        Log.e(TAG, "getSendingIP: sendingIP" + sendingIP);
        return sendingIP;

    }

    //this parse json server data to get real time IP and status for specific mobile
    public String parseJsonDataForIP(String jsonString, String parsedMobile){

        try {
            Log.e(TAG, "parseJsonData: " + jsonString);
            JSONObject jObj = new JSONObject(jsonString);
            JSONArray jArray = jObj.getJSONArray("server_response");

            for (int i = 0; i < jArray.length(); i++) {

                JSONObject user = jArray.getJSONObject(i);
                // Pulling items from the array
                String mob = user.getString("mobile");
                String stat = user.getString("status");
                Log.e(TAG, "parseJsonData: " + mob);
                if (mob.equals(parsedMobile)) {
                    if(stat.equals("1"))
                        status  = true;
                    else
                        status = false;
                    String ip = user.getString("IP");
                    Log.e(TAG, "parseJsonData: " + ip);
                    return ip;
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    //this is background thread for getting entire server data in json format
    public class GetJsonData extends AsyncTask<Void, Void, String> {

        private String json_url, ip;

        @Override
        protected void onPreExecute() {
            json_url = Config.URL_REQUEST_SERVER_DATA;
        }

        @Override
        protected String doInBackground(Void... params) {

            try {
                URL url = new URL(json_url);
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                InputStream isr = http.getInputStream();
                BufferedReader bufreader = new BufferedReader(new InputStreamReader(isr));
                StringBuilder stringbuilder = new StringBuilder();

                while ((JSON_STRING = bufreader.readLine()) != null) {
                    stringbuilder.append(JSON_STRING + "\n");
                }

                JSON_STRING = stringbuilder.toString().trim();

                bufreader.close();
                isr.close();
                http.disconnect();

                Log.e(TAG, "doInBackground: " + stringbuilder.toString().trim());


                Log.e(TAG, "doInBackground: JSON"+JSON_STRING);

                return stringbuilder.toString().trim();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }


    //test function for phones ip
    protected String wifiIpAddress(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(WIFI_SERVICE);
        int ipAddress = wifiManager.getConnectionInfo().getIpAddress();

        // Convert little-endian to big-endianif needed
        if (ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN)) {
            ipAddress = Integer.reverseBytes(ipAddress);
        }

        byte[] ipByteArray = BigInteger.valueOf(ipAddress).toByteArray();

        String ipAddressString;
        try {
            ipAddressString = InetAddress.getByAddress(ipByteArray).getHostAddress();
        } catch (UnknownHostException ex) {
            Log.e("WIFIIP", "Unable to get host address.");
            ipAddressString = null;
        }
        return ipAddressString;
    }

    //json obj for sending this mobile wifi-ip to server;
    public String jsonSendIPToServer(String ip, String mobile) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("mobile",mobile);
        jsonObject.put("IP", ip);
        Log.e(TAG, "jsonSendIPToServer: "+jsonObject.toString() );
        return jsonObject.toString();

    }


    //main function called to update mobile ip to server
    public void sendMobileIPToServer(String ip) throws JSONException {
        //PrefManager pref = new PrefManager(getApplicationContext());
        //manual set of mobile;
        String mobile = SmsActivity.pref.getMobileNumber();
        Log.e(TAG, "sendMobileIPToServer: pref mob no:"+mobile);
        String jsonInfo = jsonSendIPToServer(ip,mobile);
        new HttpAsyncTask().execute(jsonInfo);
    }



    //post methd to send json to url
    public String postMobileIPMethod(String jsonString, String posturl) throws IOException {
        InputStream inputStream = null;
        String response = "";
        //send the info
        try {
            URL url = new URL(posturl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            OutputStream outputStream = httpURLConnection.getOutputStream();
            outputStream.write(jsonString.getBytes("UTF-8"));
            outputStream.close();
            //receive msg from server
            InputStream isr = httpURLConnection.getInputStream();
            BufferedReader bufreader = new BufferedReader(new InputStreamReader(isr));
            StringBuilder stringbuilder = new StringBuilder();

            while ((response = bufreader.readLine()) != null) {
                stringbuilder.append(response + "\n");
            }

            bufreader.close();
            isr.close();
            httpURLConnection.disconnect();

            return stringbuilder.toString().trim();
        }
        catch (MalformedURLException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;

    }

    //thread to estd connection to server
    class HttpAsyncTask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... msg) {
            try {
                return postMobileIPMethod(msg[0],Config.URL_UPDATE_IP);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.e(TAG, "onPostExecute: "+s );
        }
    }


    //thread to get status for online tab;
    class GetStatusAsync extends AsyncTask<String,Void,String>{


        /*for setting url
        public GetStatusAsync(String url){
            this.url = url;
        }*/


        @Override
        protected String doInBackground(String... msg) {
            String mobile = msg[0];
            try {
                return postMobileIPMethod(jsonSendIPToServer(null,mobile),Config.URL_GETSTATUS_IP);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e(TAG, "onPostExecute: " + s);
        }
    }
}
