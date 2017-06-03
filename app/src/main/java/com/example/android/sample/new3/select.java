package com.example.android.sample.new3;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by endoutaichi on 2017/05/26.
 */

public class select {


    public ArrayList<User> select(double latitude, double longitude, Context context,ArrayList<User> adapterlist) {

       // final List<User> adapterlist= new ArrayList<User>();

        if(adapterlist==null) {
            System.out.println("adapterlistがnull");

        }
                ////////////
          //  final ArrayList<String> listItems = new ArrayList<>();


        adapterlist.clear();

            MyOpenHelper helper = new MyOpenHelper(context);
            SQLiteDatabase db = helper.getReadableDatabase();


            double clearlatitude = 0.00002694944;
            double clearlongitude = 0.00032899147;

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

                   // String in = "";

                    if (i == 0) {
                        //そして、dateの日付をcheckdateにいれる
                        checkdata = date.substring(0, date.indexOf(":"));

                        String str = date;
                        String regex = ":";
                        Pattern p = Pattern.compile(regex);

                        Matcher m = p.matcher(str);
                        date = m.replaceFirst(" ");
                        ///////////これは最初のコメント//checkdataをセットする大事な役割
                        //ここで、こんな感じで、inみたいにStringにして、adapterlistにセットするんじゃなくて、
                        //変数にセットするようにする
                        //ここを変える
                        //  in += idnumber + " " + date + " " + name + "  " + comment + "\n";
                        //この上の改行のせいで、あっちで手間取ってたんだな、まあいいや
                        //listItems[i] = in;
                        //String  user=String.valueOf(i);
                        //  User user=(User)user;
                        //ああああああああああ


                        /**
                        ListView listView = (ListView)findViewById(R.id.list_view);

                        ArrayList<User> list = new ArrayList<>();
                        ArrayListAdapter adapter = new ArrayListAdapter(select);
                        adapter.setTweetList(list);
                        listView.setAdapter(adapter);
                         **/

                        User user = new User();
                        user.setData(date);
                        user.setUsername(name);
                        user.setComment(comment);
                        user.setIdnumber(idnumber);
                        adapterlist.add(user);
                        // adapter.notifyDataSetChanged();
                        ///////aaaaaaaaaaaaaaa
                        // adapterlist.add(in);

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

                        //ここも、
                       // in += idnumber + " " + " " + date + " " + name + "  " + comment + "\n";
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


                        //ここも、
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
