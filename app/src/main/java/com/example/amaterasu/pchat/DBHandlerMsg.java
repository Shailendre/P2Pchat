package com.example.amaterasu.pchat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.MatrixCursor;
import android.util.Log;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 15-04-2016.
 */
public class DBHandlerMsg extends SQLiteOpenHelper{
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "msgsInfo";

    // tableS name
    private static final String TABLE_MSGS = "msgs";
    private static final String TABLE_CONV = "conv";

    // Shops Table Columns names
    private static final String KEY_LEFT = "isleft";
    private static final String KEY_ID = "id";
    private static final String KEY_TYPE = "type";
    private static final String KEY_CONTENT = "content";
    private static final String KEY_SENDER = "sender";
    private static final String KEY_RECEIVER = "receiver";
    private static final String KEY_SENDFLAG="sendflag";

    //Conv table columns name
    private static final String KEY_CONV_ID = "conv_id";
    private static final String KEY_CONV_NAME = "conv_name";
    private static final String KEY_CONV_NUMBER = "conv_number";

    // for user name
    private String username;

    //
    Context context;


    //TAG
    private String TAG = DBHandlerMsg.class.getSimpleName();

    public void setUsername(String username) {
        this.username = username;
    }


    public String getUsername() {
        return username;
    }

    public DBHandlerMsg(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_MSGS_TABLE = "CREATE TABLE " + TABLE_MSGS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TYPE + " TEXT," + KEY_LEFT + " INTEGER,"
                + KEY_CONTENT + " TEXT," + KEY_SENDER + " TEXT," + KEY_RECEIVER + " TEXT," + KEY_SENDFLAG + " INTEGER)";
        String CREATE_CONV_TABLE = "CREATE TABLE " + TABLE_CONV + "("
                + KEY_CONV_ID + " INTEGER PRIMARY KEY," + KEY_CONV_NAME + " TEXT," + KEY_CONV_NUMBER + " TEXT)";
        db.execSQL(CREATE_MSGS_TABLE);
        db.execSQL(CREATE_CONV_TABLE);
        Log.e(TAG, "onCreate: Conv db created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MSGS);
        // Creating tables again
        onCreate(db);
    }

    // Adding new msg
    public void addMsg(ChatMessage msg) {

        SQLiteDatabase db = this.getWritableDatabase();
        List<ChatMessage> msgs;
        String number;

        if(msg.getSendFlag()==true) {
            msgs = getConversation(msg.getReceiver());
            number=msg.getReceiver();
        }
        else {
            msgs = getConversation(msg.getSender());
            number=msg.getSender();
        }
        //searhing if this users is already in recent chats
        if(msgs.isEmpty()) {
            Conversation conversation = new Conversation();
            conversation.setName(getUsername());

            conversation.setNumber(number);
            RecentFragment.conversations.add(0,conversation);
            RecentFragment.listView.setAdapter(RecentFragment.adapter);

            ContentValues values= new ContentValues();
            values.put(KEY_CONV_NAME,getUsername());
            values.put(KEY_CONV_NUMBER,number);
            db.insert(TABLE_CONV, null, values);
            Log.e("DB add:","Conversation entry added");

        }

        ContentValues values = new ContentValues();
        values.put(KEY_TYPE,msg.getType());
        values.put(KEY_LEFT,msg.isLeft()==true?1:0);
        values.put(KEY_CONTENT, msg.getMessage());
        values.put(KEY_SENDER,msg.getSender());
        values.put(KEY_RECEIVER,msg.getReceiver());
        values.put(KEY_SENDFLAG,msg.getSendFlag()==true?1:0);

        // Inserting Row
        db.insert(TABLE_MSGS, null, values);
        db.close(); // Closing database connection
    }

    // Getting All users from conv_db
    public List<Conversation> getAllConv()
    {
        List<Conversation> convList = new ArrayList<Conversation>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CONV;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Conversation conv = new Conversation();

                conv.setId(Integer.parseInt(cursor.getString(0)));
                conv.setName(cursor.getString(1));
                conv.setNumber(cursor.getString(2));

                // Adding contact to list
                convList.add(conv);
                Log.e("Db.handler:", "Conv added to Listconv");
            } while (cursor.moveToNext());
        }
        // return msg list
        return convList;
    }

    // Getting All msgs from db
    public List<ChatMessage> getAllMsgs() {
        List<ChatMessage> msgList = new ArrayList<ChatMessage>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_MSGS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ChatMessage msg = new ChatMessage();

                msg.setId(Integer.parseInt(cursor.getString(0)));
                msg.setType(cursor.getString(1));
                msg.setLeft(cursor.getInt(2) == 1);
                msg.setMessage(cursor.getString(3));
                msg.setSender(cursor.getString(4));
                msg.setReceiver(cursor.getString(5));
                msg.setSendFlag(cursor.getInt(6) == 1);
                // Adding contact to list

                msgList.add(msg);
            } while (cursor.moveToNext());
        }
        // return msg list
        return msgList;
    }

    // Getting All msgs from db
    public List<ChatMessage> getConversation(String person) {  //person is contact whose chat screen you will open
        List<ChatMessage> msgList = new ArrayList<ChatMessage>();
        // Select All Query

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db. rawQuery("SELECT * FROM msgs WHERE receiver = ? OR sender = ?", new String[]{person, person});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ChatMessage msg = new ChatMessage();
                msg.setId(Integer.parseInt(cursor.getString(0)));
                msg.setType(cursor.getString(1));
                msg.setLeft(cursor.getInt(2) == 1);
                msg.setMessage(cursor.getString(3));
                msg.setSender(cursor.getString(4));
                msg.setReceiver(cursor.getString(5));
                msg.setSendFlag(cursor.getInt(6) == 1);
                // Adding contact to list
                msgList.add(msg);
            } while (cursor.moveToNext());
        }
        // return msg list
        return msgList;
    }


    // Getting one msg from db
    /*public Msg getMsg(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_MSGS, new String[]{KEY_ID,KEY_TYPE,
                         KEY_CONTENT}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Msg msg = new Msg(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2));
        // return shop
        return msg;
    }*/

    /*
    // Getting msgs Count
    public int getShopsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_MSGS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();

        // return count
        return count ;
    }

    // Updating a msg in db
    public int updateMsg(Msg msg) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TYPE, msg.getType());
        values.put(KEY_CONTENT, msg.getContent());

        // updating row
        return db.update(TABLE_MSGS, values, KEY_ID + " = ?",
                new String[]{String.valueOf(msg.getId())});
    }
    */
    //Deleting a msg from db
    public void deleteMsg(ChatMessage msg) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MSGS, KEY_ID + " = ?",
                new String[]{String.valueOf(msg.getId())});
        db.close();
    }

    //Extra Delete this after done
    public ArrayList<Cursor> getData(String Query){
        //get writable database
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        String[] columns = new String[] { "mesage" };
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2= new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);


        try{
            String maxQuery = Query ;
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.rawQuery(maxQuery, null);


            //add value to cursor2
            Cursor2.addRow(new Object[] { "Success" });

            alc.set(1,Cursor2);
            if (null != c && c.getCount() > 0) {


                alc.set(0,c);
                c.moveToFirst();

                return alc ;
            }
            return alc;

        } catch(Exception ex){

            Log.d("printing exception", ex.getMessage());

            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+ex.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        }


    }


}
