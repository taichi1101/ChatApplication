package com.example.android.sample.new3;

import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by endoutaichi on 2017/05/26.
 */

public class favorite   {


    String spinnerItems[];
    //--------------------------------------------------------favorite---------------------------------------------------------------------//
    //これをログインした時に usernameを使って、と、newPlaceをinsertしたあとに呼び出す、
    public String[] favorite(Context context,String username) {


        MyOpenHelper helper = new MyOpenHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        if (username != null) {
            String sql = "select placename from favorite where username = '" + username + "';";
            Log.v("placename select:", sql);


            Cursor c =db.rawQuery(sql, null);
            //これで、4つは設定できる
            int count = c.getCount();

            //-----新しいの作るのはいいけど、前のは消さなくていい？-----//
            spinnerItems = new String[count + 2];
            spinnerItems[0] = "GPSの現在地";
            spinnerItems[1] = "googlemapで登録";

            boolean mov = c.moveToFirst();
            int mam = 0;
            while (mov) {

                String in = c.getString(0);
                spinnerItems[mam + 2] = in;
                System.out.println("mam+2" + mam + 2);
                System.out.println("\"" + in + "\"");
                mov = c.moveToNext();
                mam = mam + 1;
                System.out.println("mam" + mam);
            }

            c.close();
            db.close();
        } else if (username == null) {

            spinnerItems = new String[2];
            spinnerItems[0] = "GPSの現在地";
            spinnerItems[1] = "googlemapで登録";



        }
        return spinnerItems;
    }




}

//favorite(usernae)とする


