package com.example.android.sample.new3;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by endoutaichi on 2017/05/27.
 */

public class ArrayListAdapter extends BaseAdapter {

    private Context ctx;

    LayoutInflater layoutInflater ;
   public ArrayList<User> adapterlist;

    public ArrayListAdapter(Context cotext, ArrayList<User> adapterlist) {
        this.ctx = cotext;
        this.adapterlist = adapterlist;
        this.layoutInflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public ArrayListAdapter(Context ctx) {
        this.ctx = ctx;
        this.layoutInflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setAdapterList(ArrayList<User> adapterlist) {
        this.adapterlist = adapterlist;
    }

    @Override
    public int getCount() {
        return adapterlist.size();
    }

    @Override
    public Object getItem(int position) {
        return adapterlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return adapterlist.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        User user = adapterlist.get(position);
        user.username=adapterlist.get(position).getUsername();
        user.data=adapterlist.get(position).getData();
        user.comment=adapterlist.get(position).getComment();
        user.idnumber=adapterlist.get(position).getIdnumber();
        convertView = layoutInflater.inflate(R.layout.rowdata,parent,false);
        ((TextView)convertView.findViewById(R.id.username)).setText(adapterlist.get(position).getUsername());
        ((TextView)convertView.findViewById(R.id.data)).setText(adapterlist.get(position).getData());
        ((TextView)convertView.findViewById(R.id.comment)).setText(adapterlist.get(position).getComment());
        ((TextView)convertView.findViewById(R.id.idnumber)).setText(adapterlist.get(position).getIdnumber());
        return convertView;
        //return new UserListView(ctx, user.data, user.username, user.comment,user.idnumber);
    }
}
