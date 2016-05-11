package com.example.amaterasu.pchat;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Build;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 12/4/16.
 */
public class OnlineUserAdapter extends BaseAdapter {

    public ArrayList<SelectUser> onlineusers;
    Context _c;
    ViewHolder _v;

    static class ViewHolder{
        ImageView dp;
        TextView username;
    }

    public OnlineUserAdapter(ArrayList<SelectUser> onlinelist,Context context){
        this._c = context;
        this.onlineusers = onlinelist;
    }

    @Override
    public int getCount() {
        return onlineusers.size();
    }

    @Override
    public Object getItem(int position) {
        return onlineusers.get(position);
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
            view = li.inflate(R.layout.each_online, null);
        } else {
            view = convertView;
        }

        _v = new ViewHolder();

        _v.dp = (ImageView) view.findViewById(R.id.pic);
        _v.username = (TextView) view.findViewById(R.id.name);

        final SelectUser user = onlineusers.get(position);

        Bitmap bm = BitmapFactory.decodeResource(view.getResources(), R.mipmap.ic_launcher); // Load default image
        RoundedBitmapDrawable roundedBitmapDrawable =
                RoundedBitmapDrawableFactory.create(view.getResources(), bm);
        roundedBitmapDrawable.setCircular(true);

        _v.dp.setImageDrawable(roundedBitmapDrawable);
        _v.username.setText(user.getName());

        view.setTag(onlineusers);
        return view;
    }
}
