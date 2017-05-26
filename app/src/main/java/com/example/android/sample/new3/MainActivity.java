package com.example.android.sample.new3;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import static com.example.android.sample.new3.MyOpenHelper.CREATE_TABLE;
import static com.example.android.sample.new3.MyOpenHelper.CREATE_TABLE2;
import static com.example.android.sample.new3.MyOpenHelper.CREATE_TABLE3;
import static com.example.android.sample.new3.MyOpenHelper.CREATE_TABLE4;
import static com.example.android.sample.new3.MyOpenHelper.CREATE_TABLE5;
import static com.example.android.sample.new3.MyOpenHelper.CREATE_TABLE_UPDATE;

public class MainActivity extends AppCompatActivity {

    private final int REQUEST_PERMISSION = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SQLiteDatabase db = null;
        MyOpenHelper helper = new MyOpenHelper(MainActivity.this);
        db = helper.getWritableDatabase();


       // db.execSQL("delete from neardb;");
       //db.execSQL("delete from neardbupdate;");

       // db.execSQL(CREATE_TABLE5);
 //ここCREATE_TABLE
       //db.execSQL(CREATE_TABLE);
//ここCREATE_TABLE2
     // db.execSQL(CREATE_TABLE2);
      // db.execSQL(CREATE_TABLE3);


/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
      //  db.execSQL(CREATE_TABLE_UPDATE);
        ///////////////////////////////////////////
       // db.execSQL(CREATE_TABLE4);             //ここここ

        //別のとこにもある
       // String insertsql = "insert into allidd (id) values ('0');";
        //nologinidd
        // db.execSQL(insertsql);

    }


    @Override
    protected void onStart() {
        super.onStart();
        setContentView(R.layout.activity_main);


        if(Build.VERSION.SDK_INT >= 23){
            checkPermission();
        }
        else{
            locationActivity1();


        }
    }



    // 位置情報許可の確認
    public void checkPermission() {
        // 既に許可している
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){

            locationActivity1();
        }
        // 拒否していた場合
        else{
            requestLocationPermission();
        }
    }


    // 許可を求める
    private void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION);

        } else {
            Toast toast = Toast.makeText(this, "許可しない場合はGPSを使ったサービスは使えません", Toast.LENGTH_SHORT);
            toast.show();

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, REQUEST_PERMISSION);

        }
    }

    // 結果の受け取り
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION) {
            // 使用が許可された
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationActivity1();
                return;

            } else {
                // それでも拒否された時の対応
                Toast toast = Toast.makeText(this, "使用はGPSを使わないサービスに限られます。", Toast.LENGTH_SHORT);
                toast.show();
                locationActivity2();
            }
        }
    }



    // Intent でLocation
    private void locationActivity1() {
        Intent intent = new Intent(getApplication(), LocationActivity.class);

        String i="1";
        intent.putExtra("Activity",i);
        startActivity(intent);
    }
    private void locationActivity2(){
        /////許可しない場合
        Intent intent = new Intent(getApplication(), LocationActivity.class);
        String b="2";
        intent.putExtra("Activity",b);
        startActivity(intent);
    }
}