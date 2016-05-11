package com.example.amaterasu.pchat;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.amaterasu.pchat.R;
import com.example.amaterasu.pchat.SelectUser;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;

/**
 * Created by dell on 16-02-2016.
 */
public class SelectUserAdapter extends BaseAdapter {

    private ArrayList<SelectUser> arraylist;
    Context _c;
    ViewHolder v;
    boolean setCheckBox;
    private List<SelectUser> _data;


    public SelectUserAdapter(List<SelectUser> selectUsers, Context context, boolean setCheckBox) {
        _c = context;
        this._data = selectUsers;
        this.setCheckBox=setCheckBox;
        this.arraylist = new ArrayList<SelectUser>();
        this.arraylist.addAll(_data);
    }

    @Override
    public int getCount() {
        return _data.size();
    }

    @Override
    public Object getItem(int i) {
        return _data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view = convertView;
        if (view == null) {
            LayoutInflater li = (LayoutInflater) _c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = li.inflate(R.layout.each_entry, null);
        } else {
            view = convertView;
        }


        //hiding the checkbox layout in contactsFragment but not in groupchat
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.checkbox_layout);

        if (!setCheckBox)
            linearLayout.setVisibility(View.INVISIBLE);

        v = new ViewHolder();

        v.title = (TextView) view.findViewById(R.id.name);
        v.check = (CheckBox) view.findViewById(R.id.checkbox);
        v.phone = (TextView) view.findViewById(R.id.no);
        v.imageView = (ImageView) view.findViewById(R.id.pic);

        final SelectUser data = (SelectUser) _data.get(i);
        v.title.setText(data.getName());
        v.check.setChecked(data.getCheckedBox());
        v.phone.setText(data.getPhone());

        // Set image if exists
        try {

            if (data.getThumb() != null) {
                v.imageView.setImageBitmap(data.getThumb());
            } else {
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

        // Set check box listener android

        v.check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox checkBox = (CheckBox) view;
                if (checkBox.isChecked()) {
                    data.setCheckedBox(true);
                } else {
                    data.setCheckedBox(false);
                }
            }
        });

        view.setTag(data);
        return view;
    }

    // Filter Class

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        Log.e("filter()", "filter: "+charText );

        _data.clear();
        if (charText.length() == 0) {
            _data.addAll(arraylist);
        } else {
            for (SelectUser wp : arraylist) {
                if (wp.getName().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    Log.e("inside", "filter: "+wp.getName() );
                    _data.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }


    static class ViewHolder {
        ImageView imageView;
        TextView title, phone;
        CheckBox check;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }
}



