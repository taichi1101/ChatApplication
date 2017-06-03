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


    /**
     listView = (ListView) findViewById(R.id.list_view);
     ArrayAdapter<String> adapterlist = new ArrayAdapter<String>(LocationActivity.this, android.R.layout.simple_list_item_1, listItems);
     adapterlist.setDropDownViewResource(android.R.layout.simple_list_item_1);
     listView.setAdapter(adapterlist);
     **/


    private Context ctx;

    LayoutInflater layoutInflater ;
   // ArrayList<User> tweetList;

   public ArrayList<User> adapterlist;


    // private List<User> adapterlist;

    public ArrayListAdapter(Context cotext, ArrayList<User> adapterlist) {
        this.ctx = cotext;
        this.adapterlist = adapterlist;
        this.layoutInflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    ////ここおおおおおおおおおおおおおお
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

    /////////////////////////////



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

    private final class UserListView extends LinearLayout {

        private TextView v_data;
        private TextView v_username;
        private TextView v_comment;
        private TextView v_idnumber;

        public UserListView(Context context, String data, String username, String comment,String idnumber) {
            super(context);

            setOrientation(LinearLayout.VERTICAL);

          //  layoutInflater.inflate(R.layout.rowdata,parent,false);

            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);

            param.setMargins(5, 3, 5, 0);


            v_idnumber = new TextView(context);
            v_idnumber.setText(idnumber);
            v_idnumber.setTextSize(10f);
            v_idnumber.setTextColor(Color.WHITE);
            addView(v_idnumber, param);


            v_data = new TextView(context);
            v_data.setText(data);
            v_data.setTextSize(10f);
            v_data.setTextColor(Color.WHITE);
            addView(v_data, param);

            v_username = new TextView(context);
            v_username.setText(username);
            v_username.setTextSize(15f);
            v_username.setTextColor(Color.GRAY);
            addView(v_username, param);

            v_comment = new TextView(context);
            v_comment.setText(comment);
            v_comment.setTextSize(15f);
            v_comment.setTextColor(Color.BLACK);
            addView(v_comment, param);



           // return convertView;





        }
    }

}
