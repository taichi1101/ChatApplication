package com.example.android.sample.new3;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by endoutaichi on 2017/06/09.
 */

public class Alart extends Activity {

    //神威登録した、→削除するする

  public Alart(){
        this.username=null;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    /**
    public Alart(LayoutInflater inflater, Context context){
        this.inflater=inflater;
        this.context=context;

    }
     **/

    String username;
    LayoutInflater inflater;
    Context context;
    AlertDialog.Builder builder;
    View layout;
    InputMethodManager inputMethodManager;
    AlertDialog alertDialog;
    EditText getusername2;


    public void newSet(LayoutInflater inflaters, final Context contexts,View layouts,InputMethodManager inputMethodManagers) {

        inflater=inflaters;
        context=contexts;
        layout=layouts;
        inputMethodManager=inputMethodManagers;

     //final  View layout = inflater.inflate(R.layout.dialog_contact_us, (ViewGroup) findViewById(R.id.layout_root));
        builder = new AlertDialog.Builder(context);
        builder.setView(layout);
        builder.setPositiveButton("新規登録", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                EditText getusername2 = (EditText) layout.findViewById(R.id.username);
                EditText getpassword2 = (EditText) layout.findViewById(R.id.password);
                String getusername = getusername2.getText().toString();
                String password = getpassword2.getText().toString();
                getusername = getusername.trim();
                password = password.trim();
                if (getusername.length() == 0 || password.length() == 0) {
                    Toast toast = Toast.makeText(context, "入力されていません", Toast.LENGTH_SHORT);
                    toast.show();
                } else if (getusername.length() != 0 && password.length() != 0) {
                    MyOpenHelper helper = new MyOpenHelper(context);
                    SQLiteDatabase db = helper.getWritableDatabase();
                    String sql = "select username,password from user where username = '" + getusername + "' and password = '" + password + "';";
                    Cursor c = db.rawQuery(sql, null);
                    c.moveToFirst();
                    String checkusername = null;
                    for (int i = 1; i <= c.getCount(); i++) {
                        checkusername = c.getString(0);
                    }
                    //こっちはusernameが使われてなければok
                    if (checkusername != null) {
                        Toast toast = Toast.makeText(context, "usernameがすでに使われています", Toast.LENGTH_SHORT);
                        toast.show();
                    } else if (checkusername == null) {
                        //新規登録する
                        String insertsql = "insert into user (username,password) " +
                                "values('" + getusername + "','" + password + "');";
                        db.execSQL(insertsql);
                        //username = getusername;
                        username="endo";
                        //spinnerItems = favorite.favorite(context, username);//これでok

                       // onStart();
                        System.out.println("新規登録完了" + username);
                        Toast toast = Toast.makeText(context, "登録完了しました。ログインしました", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
                //return?
                //どうやって戻るんだっけ？
            }

        });
        //ここで、別のメソッドを呼び出して、それにって、if(else)で、ボタンの数、


        //ここで、使ったmenuを使って、押されたボタンの、titleを取得して、それ次第で、レイアウトを表示する
        //使うのは、
        builder.setNeutralButton("ログアウト", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (username != null && !true == isNum(username)) {
                    Toast.makeText(context, "ログアウトしました", Toast.LENGTH_SHORT).show();
                    username = null;
                    //spinnerItems = favorite.favorite(context, username);//これでok
                } else {
                    Toast.makeText(context, "ログインしていません", Toast.LENGTH_SHORT).show();
                }
                //ログアウトの時は、onStart()していい、ログインの時は、
                //onStart();
            }
        });
        builder.setNegativeButton("ログイン", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                EditText getusername2 = (EditText) layout.findViewById(R.id.username);
                EditText getpassword2 = (EditText) layout.findViewById(R.id.password);
                String getusername = getusername2.getText().toString();
                String password = getpassword2.getText().toString();
                //データベースから取得してあればログイン
                String sql = "select username from user where username = '" + getusername + "' and password = '" + password + "';";
                System.out.println(sql);
                MyOpenHelper helper = new MyOpenHelper(context);
                SQLiteDatabase db = helper.getWritableDatabase();
                Cursor c = db.rawQuery(sql, null);
                Integer count = c.getCount();
                if (count != 0) {
                    c.moveToFirst();
                    String checkusername = c.getString(0);
                    //ログインする
                    Toast toast = Toast.makeText(context, "ログインしました", Toast.LENGTH_SHORT);
                    toast.show();
                    //そして、usernameにset
                    System.out.println("checkusername"+checkusername);
                    System.out.println("username for:"+username);
                    username = checkusername;
                    username="endo";

                    Alart alart=new Alart();
                    alart.setUsername(username);

                    username =alart.getUsername();

                    System.out.println("username afteraza:"+username);
                    System.out.println("ログイン完了" + username);
                    //この下は、引数次第で、あっちでやればいいif elseで
                    //spinnerItems = favorite.favorite(context, username);//これでok
                   // arrayadapter();
                    //onStart();

                     //Intent intent = new Intent(context, LocationActivity.class);
                       //intent.putExtra("username", username);
                        //startActivity(intent);


                    //563で、のonStart()で処理してる
                    //いちいちonStartしてるから帰る

                } else if (count == 0) {
                    Toast toast = Toast.makeText(context, "ログインできませんでした。", Toast.LENGTH_SHORT);
                    toast.show();
                    getusername2.setText("");
                    getpassword2.setText("");

                } else {
                    Toast toast = Toast.makeText(context, "ログインできませんでした。count:" + count, Toast.LENGTH_SHORT);
                    toast.show();
                }

            }

        });
        alertDialog = builder.create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface arg0) {

                System.out.println("onShow()"+username);
                getusername2 = (EditText) layout.findViewById(R.id.username);
                //inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(getusername2, 0);
            }

        });
        alertDialog.show();

    }


    static boolean isNum(String username) {
        try {
            Integer.parseInt(username);
            return true;
        } catch (NumberFormatException e) {


            return false;
        }

    }
    //LocationActivity254


}
//561で、やってる