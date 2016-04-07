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
public class AccountAdapter extends BaseAdapter {

    int[] accountImages;
    String[] accountTextView;
    Context context;
    LayoutInflater layoutInflater;

    @Override
    public int getCount() {
        return accountImages.length;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder=new Holder();
        View rowView;

        rowView=layoutInflater.inflate(R.layout.account_each_entry,null);
        holder.imageView=(ImageView)rowView.findViewById(R.id.account_imageView);
        holder.textView=(TextView)rowView.findViewById(R.id.account_textView);

        holder.imageView.setImageResource(accountImages[position]);
        holder.textView.setText(accountTextView[position]);


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

    public AccountAdapter(Context context,int[] accountImages,String[] accountTextView){
        this.context=context;
        this.accountImages=accountImages;
        this.accountTextView=accountTextView;
        layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public class Holder{
        ImageView imageView;
        TextView textView;
    }

}
