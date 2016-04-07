package com.example.amaterasu.pchat;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by dell on 20/2/16.
 */
public class SettingsAdapter extends BaseAdapter {

    int[] settingsImages;
    String[] settingsTextView;
    Context context;
    LayoutInflater layoutInflater;

    @Override
    public int getCount() {
        return settingsImages.length;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder=new Holder();
        View rowView;

        rowView=layoutInflater.inflate(R.layout.settings_each_entry,null);
        holder.imageView=(ImageView)rowView.findViewById(R.id.settings_imageView);
        holder.textView=(TextView)rowView.findViewById(R.id.settings_textView);

        holder.imageView.setImageResource(settingsImages[position]);
        holder.textView.setText(settingsTextView[position]);


        return rowView;

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    public SettingsAdapter(Context context,int[] settingsImages,String[] settingsTextView){
        this.context=context;
        this.settingsImages=settingsImages;
        this.settingsTextView=settingsTextView;
        layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public class Holder{
        ImageView imageView;
        TextView textView;
    }

}
