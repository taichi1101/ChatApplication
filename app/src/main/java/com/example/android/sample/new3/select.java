package com.example.android.sample.new3;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by endoutaichi on 2017/05/26.
 */

public class select {


    public ArrayList<String> select(double latitude, double longitude,Context context) {

        final ArrayList<String> listItems = new ArrayList<>();

        listItems.clear();

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



        if (c.getCount()!=0) {

            Integer count = c.getCount();
            String checkdata = "初期値";


            System.out.println("c.getCountやでーーな"+c.getCount());

            for (int i = 0; i < c.getCount(); i++) {
                //SQL文の結果から、必要な値を取り出す


                String date = c.getString(0);
                String name = c.getString(1);
                String comment = c.getString(2);
                String idnumber=c.getString(3);

                String in = "";


                if (i == 0) {
                    //そして、dateの日付をcheckdateにいれる
                    checkdata = date.substring(0, date.indexOf(":"));


                    String str = date;
                    String regex = ":";
                    Pattern p = Pattern.compile(regex);

                    Matcher m = p.matcher(str);
                    date = m.replaceFirst(" ");
                    ///////////これは最初のコメント//checkdataをセットする大事な役割


                    //ここを変える
                    in += idnumber+" "+date + " " + name + "  " + comment + "\n";


                    //この上の改行のせいで、あっちで手間取ってたんだな、まあいいや
                    //listItems[i] = in;

                    listItems.add(in);

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
                    in += idnumber+" "+" "+ date + " " + name + "  " + comment + "\n";
                    //スペースを一つきちんと""にする


                    listItems.add(in);


                } else if (!(date.startsWith(checkdata))) {

                    checkdata = date.substring(0, date.indexOf(":"));

                    String str = date;
                    String regex = ":";
                    Pattern p = Pattern.compile(regex);

                    //ここら辺よくわかんないな
                    //普通に:を""にしてるだけ？
                    Matcher m = p.matcher(str);
                    date = m.replaceFirst(" ");


                    //ここも、
                    in += idnumber+" "+date + " " + name + "  " + comment + "\n";

                    listItems.add(in);
                    //listItems[i] = in;

                    //そして、dateをcheckdateにいれる
                    //dateは、前のコメントの日付のはず
                }

                //ここでListViewにするように

//-------------------------------------------------------ArrayAdapterにlistItemsをset----------------------------------------------------//

                /**
                listView = (ListView) findViewById(R.id.list_view);
                ArrayAdapter<String> adapterlist = new ArrayAdapter<String>(LocationActivity.this, android.R.layout.simple_list_item_1, listItems);
                adapterlist.setDropDownViewResource(android.R.layout.simple_list_item_1);
                listView.setAdapter(adapterlist);
                 **/

//--------------------------------------------setOnItemClickListener(new LocationActivity());------------------------------------------//
                //listView.setOnItemClickListener(new LocationActivity());
              //  listView.setOnItemClickListener(this);

               // listView.setOnItemLongClickListener(this);


                //自分でActtivityをnewしては、いけない

                //ArrayAdapterをspinnerで使ってた
                c.moveToNext();


            }

            // System.out.println("listItemsの配列のlength"+listItems.length);
            c.close();
            db.close();

            Log.d("whileからでた", "はじかれた,次はとくにない？");
        }else if(c.getCount()==0) {




            // String in = "";
            // listItems[i] = in;

            listItems.clear();

            // listItems.add(in);




//-------ArrayAdapterにlistItemsをset-----//
            //これで、listViewを作る



        }




//----------------------------------------------------spinnerじゃなくて、listviewのadapter-----------------------------------------------//


        //spinnerの初回起動でstartFusedLocation()でlistItemsを取得する

        //ListView listView = (ListView) findViewById(R.id.list_view);



    return listItems;

    }

}
