package com.example.android.sample.new3;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by endoutaichi on 2017/05/26.
 */

public class select {


    public ArrayList<User> select(double latitude, double longitude, Context context,ArrayList<User> adapterlist,Double spinnermath) {
        // final List<User> adapterlist= new ArrayList<User>();

        System.out.println("selectクラス select latitude:"+latitude+":longitude:"+longitude+":spinnermatch:"+spinnermath);
        if(adapterlist==null) {
            System.out.println("adapterlistがnull");
        }
          //  final ArrayList<String> listItems = new ArrayList<>();
        adapterlist.clear();

        MyOpenHelper helper = new MyOpenHelper(context);
            SQLiteDatabase db = helper.getReadableDatabase();

        //ここで、spinnerの状況から、取得した値をかけて、sqlから取得する
        //ここで、ベースが、30だから、(*4の状態で30として、)
        //30(1) 60(2) 200(7) 1000(33) 30000(100)とする
        Double bai=0.0;
        if(spinnermath==30.0){
          bai=1.0;
        }else if(spinnermath==60.0){
            bai=2.0;
        }else if(spinnermath==200.0){
            bai=7.0;
        }else if(spinnermath==1000.0){
            bai=33.0;
        } else if (spinnermath == 3000.0){
            bai=100.0;
        }
        double clearlatitude = 0.00002694944*4*bai;
        double clearlongitude = 0.00032899147*4*bai;

        //idnumberを取得
            String sql = "select data,username,comment,idnumber from neardb where latitude - " + latitude + " <= " + clearlatitude + " and "
                    + latitude + " - latitude <= " + clearlatitude +
                    " and longitude - " + longitude + " <= " + clearlongitude + " and "
                    + longitude + " - longitude <= " + clearlongitude + ";";

        Cursor c = db.rawQuery(sql, null);
                    c.moveToFirst();
        if(c.getCount()!=0) {
                String checkdata = "初期値";
                for (int i = 0; i < c.getCount(); i++) {
                    //SQL文の結果から、必要な値を取り出す
                    String date = c.getString(0);
                    String name = c.getString(1);
                    String comment = c.getString(2);
                    String idnumber = c.getString(3);
                    if (i == 0) {
                        //そして、dateの日付をcheckdateにいれる
                        checkdata = date.substring(0, date.indexOf(":"));
                        String str = date;
                        String regex = ":";
                        Pattern p = Pattern.compile(regex);
                        Matcher m = p.matcher(str);
                        date = m.replaceFirst(" ");
                        User user = new User();
                        user.setData(date);
                        user.setUsername(name);
                        user.setComment(comment);
                        user.setIdnumber(idnumber);
                        adapterlist.add(user);
                        // adapter.notifyDataSetChanged();
                    } else if (date.startsWith(checkdata)) { //checkdateは前のコメントの日付
                        //前のコメントと日にちが同じ場合は月日を省く
                        date = date.replaceAll(checkdata, "");
                        String str = date;
                        String regex = ":";
                        //:が指定されてるから " "から文字にする
                        //regex似ついてしらべる
                        Pattern p = Pattern.compile(regex);
                        Matcher m = p.matcher(str);
                        date = m.replaceFirst(" ");
                        //もともと date = m.replaceFirst("");
                        // これはdateの中の2/20 4:3のスペースだから":"を" "にしっかりしとく
                        //スペースを一つきちんと""にする

                        User user = new User();
                        user.setData(date);
                        user.setUsername(name);
                        user.setComment(comment);
                        user.setIdnumber(idnumber);
                        adapterlist.add(user);
                        //adapterlist.add(in);
                    } else if (!(date.startsWith(checkdata))) {
                        checkdata = date.substring(0, date.indexOf(":"));
                        String str = date;
                        String regex = ":";
                        Pattern p = Pattern.compile(regex);
                        Matcher m = p.matcher(str);
                        date = m.replaceFirst(" ");
                        //in += idnumber + " " + date + " " + name + "  " + comment + "\n";
                        //adapterlist.add(in);
                        User user = new User();
                        user.setData(date);
                        user.setUsername(name);
                        user.setComment(comment);
                        user.setIdnumber(idnumber);
                        adapterlist.add(user);
                    }
                    c.moveToNext();
                }
                c.close();
                db.close();
                Log.d("whileからでた", "はじかれた,次はとくにない？");
            }else if(c.getCount()==0) {
            }
        return adapterlist;
        }
    }