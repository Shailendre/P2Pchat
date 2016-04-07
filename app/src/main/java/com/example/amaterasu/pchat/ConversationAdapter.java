package com.example.amaterasu.pchat;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 2/3/16.
 */
public class ConversationAdapter extends BaseAdapter {

    public List<Conversation> _data;
    private ArrayList<Conversation> arrayList;
    Context _c;
    ViewHolder v;


    public ConversationAdapter(List<Conversation> conversations,Context context){
        _data = conversations;
        _c = context;
        this.arrayList = new ArrayList<Conversation>();
        arrayList.addAll(_data);
    }

    @Override
    public int getCount() {
        return _data.size();
    }

    @Override
    public Object getItem(int position) {
        return _data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            LayoutInflater li = (LayoutInflater) _c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = li.inflate(R.layout.each_conv_entry, null);
            Log.e("Inside", "here--------------------------- In view1");
        } else {
            view = convertView;
            Log.e("Inside", "here--------------------------- In view2");
        }

        v = new ViewHolder();

        v.name = (TextView) view.findViewById(R.id.chat_name);
        v.date = (TextView) view.findViewById(R.id.chat_date);
        v.imageView = (ImageView) view.findViewById(R.id.chat_pic);

        final Conversation data = (Conversation) _data.get(position);
        v.name.setText(data.getName());
        v.date.setText((CharSequence) data.getDate());

        // Set image if exists
        try {

            if (data.getThumb() != null) {
                v.imageView.setImageBitmap(data.getThumb());
            } else {
                v.imageView.setImageResource(R.mipmap.ic_launcher);
            }
            // Seting round image
            Bitmap bm = BitmapFactory.decodeResource(view.getResources(), R.mipmap.ic_launcher); // Load default image
            RoundedBitmapDrawable roundedBitmapDrawable =
                    RoundedBitmapDrawableFactory.create(view.getResources(), bm);
            roundedBitmapDrawable.setCircular(true);
            v.imageView.setImageDrawable(roundedBitmapDrawable);
        } catch (OutOfMemoryError e) {
            // Add default picture
            v.imageView.setImageDrawable(this._c.getDrawable(R.mipmap.ic_launcher));
            e.printStackTrace();
        }

        Log.e("Image Thumb", "--------------" + data.getThumb());

        view.setTag(data);
        return view;


    }


    public void addMembers(Conversation conversation){

        _data.add(conversation);
        notifyDataSetChanged();

    }

    static class ViewHolder{
        ImageView imageView;
        TextView name,date;
    }

}
