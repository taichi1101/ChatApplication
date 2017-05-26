package com.example.android.sample.new3;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class MyOpenHelper extends SQLiteOpenHelper {


    //-----------------dbname指定-----------------//
    public MyOpenHelper(Context context) {
        super(context, "nearplacesdbs", null, 1);

    }


   /** public static final String CREATE_TABLE =
            "create table neardb (data text,username text,comment text," +
                    "latitude double,longitude double)";**/


    //新しいautocrementのidを入れる  id integer primaryは、一度消したら、消えた数字をもう一度使う、でも、誰も消せないからそれでいい、
    // どうせ会社が削除しても、情報と同じくはならないでしょ

            //後々数字が大きくなったら、無駄な分を消したいな、しも3桁とかにしたいそして、タッチされたら、データベースからidのしもさん桁を使ってwhereする。
    public static final String CREATE_TABLE =
            "create table neardb (data text,username text,comment text," +
                    "latitude double,longitude double,idnumber text)";



//一度dbを消さないと、このneardbupdateが使えない、dbnameを変更して、すべてのテーブルをcreateして、値をinsertする.0からスタートするやつ、
    //そして新規登録する

    //もうneardbidを追加した。
    //updateでは、idは、updateしないから、指定しない
    public static final String CREATE_TABLE_UPDATE =
            "create table neardbupdate (data text,username text,comment text," +
                    "latitude double,longitude double,neardbid text)";

    //自分のログインしてるusernameと、変なやつ
    //消すのは、ひと段落してから

     //String sql="insert into near (data,username,comment,latitude,longitude)";


    public static final String CREATE_TABLE2=
            "create table favorite (username text,placename text,latitude double,longitude double)";


    public static final String CREATE_TABLE3="create table user (username text,password text)";
    //create table name text,password text


    public static final String CREATE_TABLE4="create table nologinidd (id text)";


    public static final String CREATE_TABLE5="create table allidd (id text)";











    @Override
    public void onCreate(SQLiteDatabase db) {

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}