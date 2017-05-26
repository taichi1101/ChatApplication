
//そ、5/22 コメントの修正
package com.example.android.sample.new3;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.android.sample.new3.R.id.tool_bar;


public class LocationActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,OnMapReadyCallback,GoogleMap.OnMapLongClickListener,
        ListView.OnItemClickListener,ListView.OnItemLongClickListener {
    //onMapReadyをコールバックすれば自動でonMapLeadyが呼ばれる




    favorite favorite = new favorite();

    //クラス変数にする
    String toyou = null;

    String updatetouchusernamescopy;



    //listViewの定義
    protected ListView listView;

    String deleteitem = null;
    private TextView textView;
    private String textLog = "start \n";

    // LocationClient の代わりにGoogleApiClientを使います
    private GoogleApiClient mGoogleApiClient;
    private boolean mResolvingError = false;

    private FusedLocationProviderApi fusedLocationProviderApi;

    private LocationRequest locationRequest;
    private Location location;
    private long lastLocationTime = 0;


    //------------------------------------------------latitude,longitude,latitude2,longitude2に0を代入----------------------------------------//
    double latitude = 0;
    double longitude = 0;
    double latitude2 = 0;
    double longitude2 = 0;
    String but = null;

    //この下の2つは、updateする時に、コメントを更新する時に、現在地を取得して、selectする時に使う
    Double latitudex;
    Double longitudex;

    String idnumberx;

    String datex;
    String namex;
    String commentx;
    String datareset2;

    String idnumber;

    private Spinner spinner;
    private TextView textViewSpinner;
    String spinnerItems[];
    ArrayList<String> listItems = new ArrayList<>();

    //--------------------------------static のクラス変数 usernameを定義、usernameをnullに----------------------------------------//

    static String username = null;





    //---------------------------------------------------------onCreate()------------------------------------------------------------------//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //このしたが、キーボードが押されないようにしれる
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(tool_bar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");
        mResolvingError = false;

        //nullになるから、ここでよんどく
        MyOpenHelper helper = new MyOpenHelper(LocationActivity.this);
        SQLiteDatabase db = helper.getWritableDatabase();
        //spinnerItems= favorite.favorite(LocationActivity.this,username);//これでok

    }


    //----------------------------------------------onCreateOptionMenu : onOptionItemSelected-----------------------------------------------//
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //------------------------------------------------------------新規登録/ログイン--------------------------------------------------------//
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //一つ目menuボタンを押した時の処理/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        int id = item.getItemId();


        if (id == R.id.action_setlogin) {

            // カスタムビューを設定
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(
                    LAYOUT_INFLATER_SERVICE);
            final View layout = inflater.inflate(R.layout.dialog_contact_us, (ViewGroup) findViewById(R.id.layout_root));


            AlertDialog.Builder builder = new AlertDialog.Builder(this);
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

                        Toast toast = Toast.makeText(LocationActivity.this, "入力されていません", Toast.LENGTH_SHORT);
                        toast.show();
                    } else if (getusername.length() != 0 && password.length() != 0) {

                        MyOpenHelper helper = new MyOpenHelper(LocationActivity.this);
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

                            Toast toast = Toast.makeText(LocationActivity.this, "usernameがすでに使われています", Toast.LENGTH_SHORT);
                            toast.show();

                        } else if (checkusername == null) {
                            //新規登録する
                            String insertsql = "insert into user (username,password) " +
                                    "values('" + getusername + "','" + password + "');";
                            db.execSQL(insertsql);
                            username = getusername;


                            spinnerItems= favorite.favorite(LocationActivity.this,username);//これでok

                          // db.close();
                            //usernameがnullになってる

                            System.out.println("さささささ");

                            System.out.println(Arrays.toString(spinnerItems));


                            onStart();

                            System.out.println("新規登録完了" + username);
                            Toast toast = Toast.makeText(LocationActivity.this, "登録完了しました。ログインしました", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }

                }
            });

            builder.setNeutralButton("ログアウト", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                  if (username !=null && !true == isNum(username)) {

                        Toast.makeText(LocationActivity.this, "ログアウトしました" , Toast.LENGTH_SHORT).show();

                        username=null;
                      MyOpenHelper helper = new MyOpenHelper(LocationActivity.this);
                      SQLiteDatabase db = helper.getWritableDatabase();
                      spinnerItems= favorite.favorite(LocationActivity.this,username);//これでok


                  }else {
                        Toast.makeText(LocationActivity.this, "ログインしていません" , Toast.LENGTH_SHORT).show();

                    }

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

                    MyOpenHelper helper = new MyOpenHelper(LocationActivity.this);
                    SQLiteDatabase db = helper.getWritableDatabase();

                    Cursor c = db.rawQuery(sql, null);


                    Integer count = c.getCount();
                    if (count != 0) {

                        c.moveToFirst();
                        String checkusername = c.getString(0);
                        //ログインする
                        Toast toast = Toast.makeText(LocationActivity.this, "ログインしました", Toast.LENGTH_SHORT);
                        toast.show();
                        //そして、usernameにset
                        username = checkusername;
                        System.out.println("ログイン完了" + username);
                        spinnerItems= favorite.favorite(LocationActivity.this,username);//これでok


                    } else if (count == 0) {

                        Toast toast = Toast.makeText(LocationActivity.this, "ログインできませんでした。", Toast.LENGTH_SHORT);
                        toast.show();
                        getusername2.setText("");
                        getpassword2.setText("");

                    } else {
                        Toast toast = Toast.makeText(LocationActivity.this, "ログインできませんでした。count:"+count, Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }

            });

            AlertDialog alertDialog = builder.create();


            alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface arg0) {
                    EditText getusername2 = (EditText) layout.findViewById(R.id.username);

                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.showSoftInput(getusername2, 0);

                }
            });

            alertDialog.show();

//-------------------------------------------------現在表示のおきにいり削除--------------------------------------------------//


        } else if (id == R.id.action_deleteplace) {

            // カスタムビューを設定
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(
                    LAYOUT_INFLATER_SERVICE);
            final View layout = inflater.inflate(R.layout.dialog_deleteplace_us, (ViewGroup) findViewById(R.id.layout_deleteplace));


            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(layout);

            builder.setPositiveButton("削除します。", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {


                    if (deleteitem.equals("GPSの現在地")) {
                        Toast toast = Toast.makeText(LocationActivity.this, "お気に入りが選択されていません" + deleteitem, Toast.LENGTH_SHORT);
                        toast.show();

                    } else if (deleteitem.equals("googlemapで検索")) {
                        Toast toast = Toast.makeText(LocationActivity.this, "お気に入りが選択されていません", Toast.LENGTH_SHORT);
                        toast.show();

                    } else {

                        String sql = "delete from favorite where  placename  = '" + deleteitem + "' " +
                                "and username = '" + username + "'" +
                                "and  latitude  = '" + latitude2 + "'" +
                                "and longitude  = '" + longitude2 + "';";

                        System.out.println("現在地のお気に入り削除" + username);

                        System.out.println(sql);
                        System.out.println(deleteitem);
                        MyOpenHelper helper = new MyOpenHelper(LocationActivity.this);
                        SQLiteDatabase db = helper.getWritableDatabase();
                        Cursor c = db.rawQuery(sql, null);


                        System.out.println(c.getCount());
                        c.moveToFirst();

                        spinnerItems= favorite.favorite(LocationActivity.this,username);//これでok

                        onStart();

                    }
                }
            });
            builder.create().show();

            return super.onOptionsItemSelected(item);


//---------------------------------------------------現在表示のお気に入りの名前変更-------------------------------------------//

        } else if (id == R.id.action_updateplace) {


            LayoutInflater inflater = (LayoutInflater) this.getSystemService(
                    LAYOUT_INFLATER_SERVICE);
            final View layout = inflater.inflate(R.layout.dialog_updateplace_us, (ViewGroup) findViewById(R.id.layout_updateplace));


            AlertDialog.Builder builder = new AlertDialog.Builder(LocationActivity.this);
            builder.setView(layout);

            builder.setPositiveButton("名称変更", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {


                    EditText updateplace2 = (EditText) layout.findViewById(R.id.updateplace);

                    String updateplace = updateplace2.getText().toString();

                    updateplace = updateplace.trim();


                    if (updateplace.length() == 0) {

                        Toast toast = Toast.makeText(LocationActivity.this, "入力してください", Toast.LENGTH_SHORT);
                        toast.show();
                    } else if (updateplace.length() != 0) {


                        if (deleteitem.equals("GPSの現在地")) {
                            Toast toast = Toast.makeText(LocationActivity.this, "お気に入りが選択されていません" + deleteitem, Toast.LENGTH_SHORT);
                            toast.show();

                        } else if (deleteitem.equals("googlemapで検索")) {
                            Toast toast = Toast.makeText(LocationActivity.this, "お気に入りが選択されていません", Toast.LENGTH_SHORT);
                            toast.show();

                        } else {

                            String sql = "update  favorite set placename ='" + updateplace + "' " +
                                    "where  username = '" + username + "'" +
                                    "and placename = '" + deleteitem + "'" +
                                    "and  latitude  = '" + latitude2 + "'" +
                                    "and longitude  = '" + longitude2 + "';";


                            System.out.println(sql);
                            System.out.println(deleteitem);
                            MyOpenHelper helper = new MyOpenHelper(LocationActivity.this);
                            SQLiteDatabase db = helper.getWritableDatabase();
                            Cursor c = db.rawQuery(sql, null);


                            System.out.println(c.getCount());
                            c.moveToFirst();

                            spinnerItems= favorite.favorite(LocationActivity.this,username);//これでok

                            onStart();

                        }

                    }
                }

            });

            AlertDialog alertDialog = builder.create();

            alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface arg0) {
                    EditText  updateplace2= (EditText) layout.findViewById(R.id.updateplace);

                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.showSoftInput(updateplace2, 0);

                }
            });

            alertDialog.show();
            return super.onOptionsItemSelected(item);

           //---------------------------------------使用中のuserを削除--------------------------------------------------//


        } else if (id == R.id.action_deleteuser) {

            // カスタムビューを設定
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(
                    LAYOUT_INFLATER_SERVICE);
            final View layout = inflater.inflate(R.layout.dialog_delete_user, (ViewGroup) findViewById(R.id.layout_deleteuser));


            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(layout);

            builder.setPositiveButton("アカウント削除", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {


                    EditText deleteusername = (EditText) layout.findViewById(R.id.username);
                    EditText deletepassword = (EditText) layout.findViewById(R.id.password);


                    String deleteu = deleteusername.getText().toString();
                    String deletep= deletepassword.getText().toString();


                    if (username != null) {

                        if (username.equals(deleteu)) {

                            String sql = "select * from user where username = '" + deleteu + "' and password = '" + deletep + "';";

                            MyOpenHelper helper = new MyOpenHelper(LocationActivity.this);
                            SQLiteDatabase db = helper.getWritableDatabase();
                            Cursor c = db.rawQuery(sql, null);
                            c.moveToFirst();
                            Integer count = c.getCount();


                            if (count != 0) {
                                String checkselectusername = c.getString(0);
                                String checkselectpassword = c.getString(1);
                                //ログインする
                                //EditTextをcleanだ
                                deleteusername.setText("");
                                deletepassword.setText("");


                                //そして、削除ボタンをおして、selectして、nullじゃなければ、削除する。
                                if (checkselectusername != null && checkselectpassword != null) {

                                    String deleteusersql = "delete from user where  username  = '" + deleteu + "' " +
                                            "and password = '" + deletep + "';";
                                    System.out.println(deleteusersql);
                                    Cursor cz = db.rawQuery(deleteusersql, null);
                                    System.out.println(c.getCount());
                                    cz.moveToFirst();

                                    username = null;
                                    Toast toast = Toast.makeText(LocationActivity.this, "削除しました。別アカウントでログインしてください。", Toast.LENGTH_SHORT);
                                    toast.show();
                                    //削除しました。のToast

                                } else {
                                    //Toast、使用しているアカウント情報が間違えています。
                                    Toast toast = Toast.makeText(LocationActivity.this, "アカウント情報が間違えています", Toast.LENGTH_SHORT);
                                    toast.show();

                                }


                            }
                        } else {
                            Toast toast = Toast.makeText(LocationActivity.this, "ログインしているユーザ名と違います。", Toast.LENGTH_SHORT);
                            toast.show();


                        }

                    }else if(username ==null){

                        Toast toast = Toast.makeText(LocationActivity.this, "ログインしていません username:" + username , Toast.LENGTH_SHORT);
                        toast.show();

                    }
                }
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface arg0) {
                    EditText  deleteusernamesoft= (EditText) layout.findViewById(R.id.username);

                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.showSoftInput(deleteusernamesoft, 0);

                }
            });

            alertDialog.show();

            //ここでも、最初からキーボードを出しておく
            return super.onOptionsItemSelected(item);




//---------------------------------------------------------表示画面を登録---------------------------------------------------------------//
        } else if (id == R.id.action_nowscreenset) {
            // カスタムビューを設定
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(
                    LAYOUT_INFLATER_SERVICE);
            final View layout = inflater.inflate(R.layout.dialog_placename_us, (ViewGroup) findViewById(R.id.layout_place));
            // アラーとダイアログ を生成
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(layout);

            builder.setPositiveButton("登録", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {


                    EditText getplacename = (EditText) layout.findViewById(R.id.placename);
                    //入力した文字をトースト出力する
                    String placename = getplacename.getText().toString();
                    placename = placename.trim();

                    if (placename.length() == 0) {

                        Toast toast = Toast.makeText(LocationActivity.this, "名称が入力されていません", Toast.LENGTH_SHORT);
                        toast.show();
                    } else if (placename.length() != 0) {

                        if (username == null) {


                            //ログインしてくださいと表示
                            Toast toast = Toast.makeText(LocationActivity.this, "ログインしてください", Toast.LENGTH_SHORT);
                            toast.show();

                        } else if (username != null) {
                            //ここに埋め込んだ
                            MyOpenHelper helper = new MyOpenHelper(LocationActivity.this);
                            SQLiteDatabase db = helper.getWritableDatabase();


    //ここのif()はselectのためじゃなくて、insertでlatitudeかlatitude2のどちらを使うかを判定するため、
                            if (latitude2 != 0) {

                                String sql = "insert into favorite (username,placename,latitude,longitude) " +
                                        "values('" + username + "','" + placename + "'," + latitude2 + "," + longitude2 + ");";


                                db.execSQL(sql);
                                select(latitude2, longitude2);

                                //favoriteににinsetしたからfavoriteよんでspinner更新
                                spinnerItems= favorite.favorite(LocationActivity.this,username);//これでok



                            } else if (latitude2 == 0) {
                                //String sql="insert into favorite (username,placename,latitude,longitude) " +
                                /// "values('ta','甲子園',34.721085, 135.362127);";
                                String sql = "insert into favorite (username,placename,latitude,longitude) " +
                                        "values('" + username + "','" + placename + "'" +
                                        "," + latitude + "," + longitude + ");";


                                db.execSQL(sql);
                                select(latitude, longitude);

                                //favoriteににinsetしたからfavoriteよんでspinner更新
                                spinnerItems= favorite.favorite(LocationActivity.this,username);//これでok


                                System.out.println("onStartよぶ");
                                onStart();
                                System.out.println("onstart読んだ");

                            }
                        }

                    }
                }
            });


            //getplacename
            AlertDialog alertDialog = builder.create();


            alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface arg0) {
                    EditText getplacename = (EditText) layout.findViewById(R.id.placename);

                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.showSoftInput(getplacename, 0);

                }
            });

            alertDialog.show();

        }
        return super.onOptionsItemSelected(item);
    }






    //----------------------------------------------------------onStart()-------------------------------------------------------------------//
    @Override
    protected void onStart() {
        super.onStart();


        System.out.println("onstat呼ばれた");

        listView = (ListView) findViewById(R.id.list_view);


        // LocationRequest を生成して精度、インターバルを設定
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(16);


        //----------intentが1の時は、自動でon------------//
        String activity = getIntent().getStringExtra("Activity");
        if (activity.equals("1")) {
            Switch mSwitch = (Switch)
                    findViewById(R.id.swtOnOff);
            mSwitch.setChecked(true);  // 状態をONに
        }

        activity = getIntent().getStringExtra("Activity");
        if (activity.equals("1")) {

            //ここで、spinnerのやつやったほうがいい
            //けど、onStart()がまだタイミング的に理解してないから、とりまパス。

        } else if (activity.equals("2")) {
            Toast toast = Toast.makeText(this, "コミュにティーを選んでください", Toast.LENGTH_SHORT);
            //標準でどこかを表示する
            toast.show();

        }


//--------------------------------------------------toolbarの設定-------------------------------------------------------------------------//
        Toolbar toolbar = (Toolbar) findViewById(tool_bar);
        setSupportActionBar(toolbar);

        toolbar.setTitle("");


        //2つ目 spinnerのコード/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        //----こういうのは、最初に違う形で判断するspinnerではやらない----//

        //---------------- Spinner の選択されているアイテムを設定-------------//
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Spinner spinner = (Spinner) parent;
                String item = (String) spinner.getSelectedItem();

                deleteitem = item;


                //------------------------------------------spinner初回起動------------------------------------------------//

                if (spinner.isFocusable() == false) {
                    spinner.setFocusable(true);

                     String activity = getIntent().getStringExtra("Activity");


                    if (item.equals("GPSの現在地")) {
                        if (activity.equals("1")) {
                            //gpsがoffにされたら、activityを2にする、 そして、クラス変数にする
                            startFusedLocation();

                            //これで初期化現在地だから
                            latitude2 = 0;
                            longitude2 = 0;
                            but = null;
                       } else if (activity.equals("2")) {
                            Toast.makeText(getApplicationContext(), "何もしない", Toast.LENGTH_SHORT).show();
                            //これで初期化現在地だから
                            latitude2 = 0;
                            longitude2 = 0;
                            but = null;
                        }
                        return;
                    }
                }


//--------------------------------------------gpsの現在地---------------------------------------------------------------------------//
                if (item.equals("GPSの現在地")) {

                    String activity = getIntent().getStringExtra("Activity");
                    if (activity.equals("1")) {
                        //gpsがoffにされたら、activityを2にする、 そして、クラス変数にする
                        startFusedLocation();
                        //これで初期化現在地だから
                        latitude2 = 0;
                        longitude2 = 0;
                        but = null;
                        Toast.makeText(getApplicationContext(), "startFusedLocationがされた", Toast.LENGTH_SHORT).show();
                    } else if (activity.equals("2")) {
                        //何もしない
                        //select(latitude, longitude);　これはおかしいよね？
                        Toast.makeText(getApplicationContext(), "何もしない", Toast.LENGTH_SHORT).show();

                        //これで初期化現在地だから
                        latitude2 = 0;
                        longitude2 = 0;
                        but = null;
                    }


//------------------------------------------------------googlemapで検索-----------------------------------------------------------------//
                } else if (item.equals("googlemapで検索")) {
                    setContentView(R.layout.activity_maps);
                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.map);
                    mapFragment.getMapAsync(LocationActivity.this);


//---------------------------------------------else つまり、お気に入り----------------------------------------------------------------//
                } else {
                    String sql = "select latitude,longitude from favorite where placename = '" + item + "' " +
                            "and username = '" + username + "';";


                    MyOpenHelper helper = new MyOpenHelper(LocationActivity.this);
                    SQLiteDatabase db = helper.getWritableDatabase();
                    Cursor c = db.rawQuery(sql, null);

                    if (c != null) {

                    }

                    System.out.println(c.getCount());
                    c.moveToFirst();

                    latitude2 = c.getDouble(0);
                    longitude2 = c.getDouble(1);

                    System.out.println(latitude2 + "z:" + longitude2);

                    but = item;
                    deleteitem = item;

                    deleteitem = (String) spinner.getSelectedItem();

                    select(latitude2, longitude2);
                }
            }

            //アイテムが選択されなかった
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        MyOpenHelper helper = new MyOpenHelper(LocationActivity.this);
        SQLiteDatabase db = helper.getWritableDatabase();
        spinnerItems= favorite.favorite(LocationActivity.this,username);//これでok


        spinnerItems= favorite.favorite(LocationActivity.this,username);//これでok


        //usernameがnullになってる

        System.out.println("あああああ");
        System.out.println(Arrays.toString(spinnerItems));


        //リスナーの終わり、
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerItems);

        //ここにcreateFromResourceがないことだけ
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // 初回起動時の対応
        spinner.setFocusable(false);



//---------------------------------------------ここからはコメント送信ボタン、spinner----------------------------------------------------//
        Button buttonTransmission = (Button) findViewById(R.id.buttonTransmission);
        buttonTransmission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MyOpenHelper helper = new MyOpenHelper(LocationActivity.this);
                SQLiteDatabase db = helper.getWritableDatabase();


                EditText editText = (EditText) findViewById(R.id.Transmission);
                String comment = editText.getText().toString();

                comment = comment.trim();

                if (comment.length() == 0) {

                    Toast toast = Toast.makeText(LocationActivity.this, "コメントが入力されていません", Toast.LENGTH_SHORT);
                    toast.show();
                } else if (comment.length() != 0) {

                    String insertsql = "insert into allidd (id) values ('0');";
                    //nologinidd
                  // db.execSQL(insertsql);


                    String selectidsql = "select id from allidd;";
                    Cursor selectid = db.rawQuery(selectidsql, null);
                    selectid.moveToFirst();

                    String getidstring = selectid.getString(0);
                    Integer id = Integer.parseInt(getidstring);
                    id = id + 1;
                    int zz = id;
                    String stringid = String.valueOf(zz);
                    idnumber = stringid;

                    String updateidsql = "update allidd set id = '" + stringid + "';";
                    Cursor updateid = db.rawQuery(updateidsql, null);
                    updateid.moveToFirst();


                    ///---------------ここでEditTextを空に-------------///
                    textLog = "";
                    editText.setText(textLog);


                    //calendarの取得
                    Calendar calendar = Calendar.getInstance();
                    int month = calendar.get(Calendar.MONTH) + 1;
                    int day = calendar.get(Calendar.DATE);
                    int hour = calendar.get(Calendar.HOUR);
                    int minute = calendar.get(Calendar.MINUTE);
                    String data = month + "/" + day + ":" + hour + ":" + minute;



                    String keepusername= username;
                    //---------------------データベースからidを取得しそれに1をたす-----------------------//

                    int idd;

                    if (username == null || (true == isNum(username))) {


                        //String selectsql = "select id from nologinidd;";
                        String selectsql = "select id from allidd;";
                        Cursor selectc = db.rawQuery(selectsql, null);
                        selectc.moveToFirst();


                        String getidtext = selectc.getString(0);
                        idd = Integer.parseInt(getidtext);
                        idd = idd + 1;
                        int jj = idd;

                        String strid = String.valueOf(jj);


                       if(toyou!=null) {
                            username = strid+toyou;
                        }else {
                            username = strid;
                        }


                        String updatesql = "update allidd set id = '" + strid + "';";
                        Cursor updatec = db.rawQuery(updatesql, null);
                        updatec.moveToFirst();

                        //----------------------ここまでで、***でないユーザー名を作成-----------------------//


                        String sql = null;
                        String activity = getIntent().getStringExtra("Activity");
                        if (activity.equals("1")) {

                            if (latitude2 == 0) {
                                sql = "insert into neardb(data,username,comment,latitude,longitude,idnumber)" +
                                        " values ('" + data + "','" + username + "','" + comment + "'," + latitude + "," + longitude + "," + idnumber + ");";


                                db.execSQL(sql);

                                select(latitude, longitude);
                            } else if (latitude2 != 0) {
                                sql = "insert into neardb(data,username,comment,latitude,longitude,idnumber)" +
                                        " values ('" + data + "','" + username + "','" + comment + "'," + latitude2 + "," + longitude2 + "," + idnumber + ");";
                                db.execSQL(sql);
                            }
                            startFusedLocation();

                        } else if (activity.equals("2")) {
                            String sql2 = "insert into neardb(data,username,comment,latitude,longitude,idnumber)" +
                                    " values ('" + data + "','" + username + "','" + comment + "'," + latitude2 + "," + longitude2 + "," + idnumber + ");";
                            db.execSQL(sql2);
                            select(latitude2, longitude2);

                        }
                        db.close();



                    } else if (username!= null) {//usernameがある場合、ログインしている場合

                        //ここで、insertする前にusernameに>などを組み入れる
                        if(toyou!=null) {
                            keepusername=username;
                            username =username+toyou;
                        }

                        String sql = null;

                        String activity = getIntent().getStringExtra("Activity");
                        if (activity.equals("1")) {

                            if (latitude2 == 0 && longitude2 == 0) {
                                sql = "insert into neardb(data,username,comment,latitude,longitude,idnumber)" +
                                        " values ('" + data + "','" + username + "','" + comment + "'," + latitude + "," + longitude + "," + idnumber + ");";
                                db.execSQL(sql);

                                startFusedLocation();
                            } else if (latitude2 != 0 && longitude2 != 0) {
                                sql = "insert into neardb(data,username,comment,latitude,longitude,idnumber)" +
                                        " values ('" + data + "','" + username + "','" + comment + "'," + latitude2 + "," + longitude2 + ","+idnumber+");";
                                db.execSQL(sql);

                                select(latitude2, longitude2);
                            }


                        } else if (activity.equals("2")) {
                            if (latitude2 != 0 && longitude2 != 0) {
                                String sql2 = "insert into neardb(data,username,comment,latitude,longitude,idnumber)" +
                                        " values ('" + data + "','" + username + "','" + comment + "'," + latitude2 + "," + longitude2 + "," + idnumber + ");";
                                db.execSQL(sql2);
                                select(latitude2, longitude2);
                                db.close();

                            } else if (latitude2 != 0 && longitude2 != 0) {

                                Toast toast = Toast.makeText(LocationActivity.this, "GPSをONにするか、場所を選択してください", Toast.LENGTH_SHORT);
                                toast.show();


                            }
                        }
                    }

                    //ここで、クラス変数のusernameに戻す
                    username=keepusername;
                    //そして、toyouをnullに戻す
                    //つまり、ログインuserでも、みログインuserでも、とりあえず、>があってもなくても、nullで、リセット
                    toyou=null;
                }

                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

            }
        });
        // 初回起動時の対応
        spinner.setFocusable(false);


    }


//----------------------------------------------------------onClick--------------------------------------------------------------------//

    String updatedata = null;
    String updatetouchusername = null;
    String updatecomment = null;

    EditText editText;


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d(position + "", id + "");


        //3つめ listのクリック/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        System.out.println("SHORT");


        if (username == null) {
            //nullの人でも、編集はできないけど、宛先選択はできるからこれはいらない
        }

        //spinnerだったらselectedItemだけどListViewは、違う
        listView = (ListView) parent;
        String item = (String) listView.getItemAtPosition(position);


        try {


            final String updateid = item.substring(0, item.indexOf(" "));
            System.out.println("updateid" + updateid + ":e");
            item = item.substring(item.indexOf(" ") + 1);

            String getdesdata = item.substring(0, item.indexOf(":"));
            String getusedesdata = null;
            Integer copyposition = position;
            Integer minus = 0;
            while (getdesdata.indexOf("/") == -1) {
                // 最初の:の前に"/"を含まない限り、

                minus = copyposition--;
                // / を含んだものを取得するまで-1されたpositionで、itemを取得
                String includes = (String) listView.getItemAtPosition(minus);

                includes = includes.substring(includes.indexOf(" ") + 1);
                getdesdata = includes.substring(0, includes.indexOf(" "));
                getusedesdata = getdesdata;
            }
            //  "/"を含んだpositionの何月何日を取得して、timeago(時間の前)に入れる
            String timeago = getusedesdata;


            //-------------------"/"を含む場合のdata username commentの取得----------------//

            String getdata = item.substring(0, item.indexOf(":"));

            System.out.println("getdata" + getdata);


            //getitemに/が含まれれば次の:まで取得

            //3--if("/") /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

            if (getdata.indexOf("/") != -1) {          //////////取得してる場合の、***あとは、取得してる endo  つまり最後


                //何月何日が入ってるほうは、日と時間の間の、" "を:にして、
                String dataand = item.replaceFirst(" ", ":");

                //最初のスペース(username)までの間を取得して、dataにセット
                updatedata = dataand.substring(0, dataand.indexOf(" "));

                //usernameの判定
                //" "を削除したdataandから1つめの" "から後ろを取り出す
                String usernameand = dataand.substring(dataand.indexOf(" ") + 1);

                //そして、"  "より前を取り出す
                updatetouchusername = usernameand.substring(0, usernameand.indexOf(" "));
                System.out.println(updatetouchusername + " touchusername");


                updatecomment = usernameand.substring(usernameand.indexOf(" ") + 1);
                //" "を削除した
                updatecomment = updatecomment.replaceFirst(" ", "");
                updatecomment = updatecomment.replaceFirst("\\n", "");


                //--------------------------------自分のコメントか他人のコメントかif-----------------------------------//


   String barter=updatetouchusername;//よびにbarterを作っておく

                //">"を含んでる場合のif文
                if(updatetouchusername.indexOf(">")!=-1) {


                    String updatetouchusernamecheck = updatetouchusername.substring(0, updatetouchusername.indexOf(">"));

                    System.out.println("短くする前の:"+updatetouchusername+"したあとの:"+updatetouchusernamecheck);

                    updatetouchusername=updatetouchusernamecheck;
                }


                //3:if("/")--if(username)--/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

                if (updatetouchusername.equals(username) && (!true == isNum(updatetouchusername))) {

                    if(barter.indexOf(">")==1) {
                    updatetouchusername=barter;
                        System.out.println("5barterを入れたupdatetouchusername:"+updatetouchusername);
                    }


                    //--------------ここで、修正しますか？を確認のダイアログを出力--------------//
                    LayoutInflater inflater = (LayoutInflater) LocationActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
                    final View layout = inflater.inflate(R.layout.dialog_edit_comment, (ViewGroup) findViewById(R.id.layout_edit));
                    // アラーとダイアログ を生成
                    AlertDialog.Builder builder = new AlertDialog.Builder(LocationActivity.this);

                    //----------------------------------このalartdialongとこから--------------------------------->

                    builder.setView(layout);
                    editText = (EditText) layout.findViewById(R.id.editor_text);

                    //-----------------------------------//select二発---------------------------------//


                    System.out.println(updatedata + "," + updatetouchusername + "," + updatecomment);

                    //さっき取得した、先頭のupdateidをwhere句で指定
                    String sqlselect = "select * from neardb where data == '" + updatedata + "' and comment == '" + updatecomment + "'" +
                            " and username == '" + updatetouchusername + "' and idnumber == '" + updateid + "';";


                    System.out.println("sqlselect1つめ :" + sqlselect);

                    MyOpenHelper helper = new MyOpenHelper(LocationActivity.this);
                    SQLiteDatabase db = helper.getWritableDatabase();
                    Cursor c = db.rawQuery(sqlselect, null);


                    c.moveToFirst();


                    if (c != null && c.getCount() != 0) {
                        Log.d("c!=null 次はfor()", "nullじゃない確定");

                        System.out.println("c.getCount() :" + c.getCount());

                        for (int i = 0; i < c.getCount(); i++) {


                            datex = c.getString(0);//data
                            namex = c.getString(1); //name
                            commentx = c.getString(2); //comment
                            latitudex = c.getDouble(3);//latitude?
                            longitudex = c.getDouble(4);//longitude?
                            idnumberx = c.getString(5);//id
                            String in = "";

                            in += datex + " " + namex + "  " + commentx + " " + idnumberx + "\n";


                            c.moveToNext();
                        }


                        //--------------------------------------------selectした値が、updatedatabaseにもない場合----------------------------------------------//

                        //２つ目のselect//

                        //where句で、先ほど取得したidnumberxをセット neardbupdateにて
                        String sqlselectupdate = "select * from neardbupdate where data == '" + datex + "' and username == '" + namex + "'" +
                                " and comment == '" + commentx + "' and latitude == " + latitudex + " and longitude == " + longitudex + " and neardbid == '" + idnumberx + "';";


                        Cursor cc = db.rawQuery(sqlselectupdate, null);

                        cc.moveToFirst();


                        //updateから取得できたってことは、もうupdateにinsert済みだから、<編集--を削除して、更新ボタンが押されたら、update

                        if (cc != null && cc.getCount() != 0) {
                            for (int i = 0; i < cc.getCount(); i++) {


                                String date = cc.getString(0);//data
                                String name = cc.getString(1); //name
                                String comment = cc.getString(2); //comment
                                Double latitudez = cc.getDouble(3);//latitude?
                                Double longitudez = cc.getDouble(4);//longitude?
                                String neardbid = cc.getString(5);//idnumber

                                cc.moveToNext();
                                System.out.println(date + ":" + name + ":" + comment + ":" + latitudez + ":" + longitudez + ":" + neardbid);


//------------------------------------------更新済みdatabaseにこの情報は登録されてたから、<から右を削除----------------------------------------------------------------//
                                String lastIndex = commentx.substring(0, commentx.lastIndexOf("<"));

                                editText = (EditText) layout.findViewById(R.id.editor_text);

                                // EditText にコメントの表示にふさわしくない部分を省略した、テキストを設定
                                editText.setText(lastIndex);


                                editText.setSelection(editText.getText().length());

                            }

                        } else if (cc.getCount() == 0) { //つまり、insertする必要  今回は、editTextの事だから、 何もしないでset

                            editText = (EditText) layout.findViewById(R.id.editor_text);

                            //２回目の時に、cc.getCount()!=0になるから、そしたら、<編集----->を編集して、setすればいい

                            //updatecommentも同じ
                            editText.setText(updatecomment);
                            editText.setSelection(editText.getText().length());

                            System.out.println("updatecomment " + updatecomment);
                            //ccがnullだったら、何も削除しないでそのままセット
                        }


                        //--------------------------大きいブロック、updbのupdateと、insert、neardbのupdateをおこなう-------------------------------------//

                        // 大きいブロック
                    } else if (c == null) {
                        System.out.println("cがnull");
                    } else {
                        System.out.println("nullではない");
                    }


                    //-------------------------------------------------------ここまででselectする----------------------------------------------------//

                    // EditText のインプットタイプを設定
                    editText.setInputType(InputType.TYPE_CLASS_TEXT);


                    //-------------------------------------更新ボタン-----------------------------------------------//
                    builder.setPositiveButton("更新", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {


                            // EditText のテキストを取得
                            String getedittext = editText.getText().toString();

                            //---------------------------------select--------------------------//
                            if (getedittext.length() == 0) {

                                Toast toast = Toast.makeText(LocationActivity.this, "入力してください", Toast.LENGTH_SHORT);
                                toast.show();
                                //------------文字がEditTextに入ってるか-----------//
                            } else if (getedittext.length() != 0) {
                                //newcommentは、EditTextからgetしたgetedittext

                                System.out.println(updatedata + "," + updatetouchusername + "," + updatecomment + "," + updateid);

                                String sqlselect = "select * from neardb where data == '" + updatedata + "' and comment == '" + updatecomment + "'" +
                                        " and username == '" + updatetouchusername + "' and idnumber == '" + updateid + "';";


                                System.out.println("sqlselect :" + sqlselect);

                                MyOpenHelper helper = new MyOpenHelper(LocationActivity.this);
                                SQLiteDatabase db = helper.getWritableDatabase();
                                Cursor c = db.rawQuery(sqlselect, null);


                                c.moveToFirst();

                                if (c != null) {

                                    for (int i = 0; i < c.getCount(); i++) {

                                        datex = c.getString(0);//data
                                        namex = c.getString(1); //name
                                        commentx = c.getString(2); //comment
                                        latitudex = c.getDouble(3);//latitude?
                                        longitudex = c.getDouble(4);//longitude?
                                        idnumberx = c.getString(5);//idnumber

                                        String in = "";

                                        in += datex + " x" + namex + " x " + commentx + " x " + idnumberx + "\n";
                                        System.out.println("in :" + in);

                                        String newin = "";
                                        newin += datex + " x " + namex + " x " + commentx + " x " + latitudex + " x " + longitudex + " x " + idnumberx + "\n";

                                        System.out.println("newin" + newin);
                                        c.moveToNext();

                                    }
                                }

//--------------------------------------------selectした値が、updatedatabaseにもない場合----------------------------------------------//

                                String sqlselectupdate = "select * from neardbupdate where data == '" + datex + "' and comment == '" + commentx + "'" +
                                        " and username == '" + namex + "' and latitude = " + latitudex + " and longitude = " + longitudex + " and neardbid == '" + idnumberx + "';";

                                Cursor cc = db.rawQuery(sqlselectupdate, null);
                                cc.moveToFirst();


                                Calendar calendar = Calendar.getInstance();
                                int month = calendar.get(Calendar.MONTH) + 1;
                                int day = calendar.get(Calendar.DATE);
                                int hour = calendar.get(Calendar.HOUR);
                                int minute = calendar.get(Calendar.MINUTE);
                                datareset2 = "<編集" + month + "/" + day + ":" + hour + ":" + minute + ">";

                                //updateから取得できたってことは、もうupdateにinsert済みだから、<編集--を削除して、更新ボタンが押されたら、update
                                Log.d("c!=null 次はfor()x", "nullじゃない確定x");

                                System.out.println("cc.getCount()x :" + cc.getCount());

                                if (cc != null && cc.getCount() != 0) {


                                    for (int i = 0; i < cc.getCount(); i++) {


                                        String date = cc.getString(0);//data
                                        String name = cc.getString(1); //name
                                        String comment = cc.getString(2); //comment
                                        Double latitudez = cc.getDouble(3);//latitude?
                                        Double longitudez = cc.getDouble(4);//longitude?
                                        String neardbid = cc.getString(5);//idnumber

                                        String in = "";

                                        in += date + " " + name + "  " + comment + " " + neardbid + "\n";
                                        c.moveToNext();

                                    }
                                    //ここでは、neardbidを使い、incrementのidは、使わない
                                    String sqlupdate = "update neardbupdate set comment = '" + getedittext + datareset2 + "' where data == '" + updatedata + "' and comment =='" + updatecomment + "'" +
                                            " and username == '" + updatetouchusername + "' and neardbid == '" + idnumberx + "';";

                                    cc.close();

                                    Cursor update = db.rawQuery(sqlupdate, null);
                                    update.moveToFirst();

                                    update.close();
                                    db.close();


                                    //------------一度もupdateしてないコメントは、ここに来るようになってる-------------//
                                } else if (cc == null || cc.getCount() == 0) {

                                    //------------------ここで、insertしてる neardbupdateに---------------------//

                                    String updateinsert = "insert into neardbupdate (data,username,comment,latitude,longitude,neardbid)" +
                                            " values ('" + datex + "','" + updatetouchusername + "','" + getedittext + datareset2 + "'," + latitudex + "," + longitudex + "," + idnumberx + ");";

                                    db.execSQL(updateinsert);

                                } else {
                                    System.out.println("まじ");
                                }
                            }

//-------------------------------------------これは、普通にneardbのupdate--------------------------------------------------------//


                            Calendar calendar = Calendar.getInstance();
                            int month = calendar.get(Calendar.MONTH) + 1;
                            int day = calendar.get(Calendar.DATE);
                            int hour = calendar.get(Calendar.HOUR);
                            int minute = calendar.get(Calendar.MINUTE);
                            String datareset = "<編集" + month + "/" + day + ":" + hour + ":" + minute + ">";

                            String sqlupdate = "update neardb set comment = '" + getedittext + datareset + "' where data == '" + updatedata + "' and comment =='" + updatecomment + "'" +
                                    " and username == '" + updatetouchusername + "' and  idnumber == '" + idnumberx + "';";


                            MyOpenHelper helper = new MyOpenHelper(LocationActivity.this);
                            SQLiteDatabase db = helper.getWritableDatabase();

                            Cursor update = db.rawQuery(sqlupdate, null);
                            update.moveToFirst();

                            //-------update-----//

                            update.close();
                            db.close();

//----------------------------------------------------そして、select----------------------------------------------------------------//

                            select(latitudex, longitudex);

                        }

                    });
                    //更新のボタンここまで


                    AlertDialog alertDialog = builder.create();
                    alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface arg0) {
                            EditText getedittext = (EditText) layout.findViewById(R.id.editor_text);

                            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputMethodManager.showSoftInput(getedittext, 0);

                        }
                    });

                    alertDialog.show();



     //3:if("/")--else()--/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

                } else {

                     //spinnerだったらselectedItemだけどListViewは、違う
                    listView = (ListView) parent;
                    String itemtoyou = (String) listView.getItemAtPosition(position);

                    itemtoyou = itemtoyou.substring(itemtoyou.indexOf(" ") + 1);


                    //-------------------"/"を含む場合のdata username commentの取得----------------//


                    //何月何日が入ってるほうは、日と時間の間の、" "を:にして、
                    String dataands = itemtoyou.replaceFirst(" ", ":");

                    //最初のスペース(username)までの間を取得して、dataにセット
                    String updatedatas = dataands.substring(0, dataands.indexOf(" "));

                    //usernameの判定
                    dataands.substring(0, dataands.indexOf(" "));

                    //" "を削除したdataandから1つめの" "から後ろを取り出す
                    String usernameands = dataands.substring(dataands.indexOf(" ") + 1);

                    //そして、"  "より前を取り出す
                    String updatetouchusernames = usernameands.substring(0, usernameands.indexOf(" "));


                    //これは、クラス変数にする　String updatetouchusernamescopy;
                    if(updatetouchusernames.indexOf(">") != -1) {

                         updatetouchusernamescopy = updatetouchusernames.substring(0,updatetouchusernames.indexOf(">"));
                    }else{
                        //含まない場合は、そのまま使用
                        updatetouchusernamescopy = updatetouchusernames;
                    }


                    if(toyou==null){

                        System.out.println("toyouはnull");

                        toyou = ">" + updatetouchusernamescopy;


                    } else if (toyou.indexOf(">") != -1) {//含まれている場合は、すでにusernameは、入っているから、あとは、toutchusernameだけ

                        System.out.println("pppp else if  >がある");

                        if (toyou.indexOf(updatetouchusernamescopy) !=- 1) {//すでに、この人へが、追加されている場合は、消す


                            System.out.println("aaa:"+toyou);
                            toyou = toyou.replaceFirst(updatetouchusernamescopy , ""); //一番最初の頭があったら、""にする
                            System.out.println("bbb:"+toyou);

                             if (toyou.indexOf(",,") != -1) {//つまり2人以上が選択されているということ
                                toyou = toyou.replace(",,", ",");  //上のやつで、消したら、それを、今度は、,,を防ぐ
                                System.out.println("toyou:after2:"+toyou);
                            }


                            if (toyou.equals(">")) {//そして、もう、toyouが">"のみになったら、、toyouをnullにする
                                toyou = null;
                                System.out.println("toyou.equals>:"+toyou);
                            }else{ //つまり一つのusernameを入れて消した時は、>しかないから、charAt(1)はない状態
                                System.out.println("toyou:" + toyou);
                                char fo = toyou.charAt(1);
                                //0は、まだ、>を消す前だから、1にする
                                String stringfor = String.valueOf(fo);


                                char la = toyou.charAt(toyou.length() - 1);
                                String stringafter = String.valueOf(la);

                                System.out.println("stringfor:" + stringfor + "      stringafter:" + stringafter);


                                //  char foo = toyou.charAt(0);
                                // String stringforo = String.valueOf(fo);
                                //System.out.println("ああああああああああ:"+stringforo);

                                if (stringfor.equals(",")) {
                                    toyou = toyou.replaceFirst(",", "");
                                    System.out.println("前方一致:" + toyou);


                                } else if (stringafter.equals(",")) {
                                    toyou = toyou.substring(0, toyou.lastIndexOf(","));
                                    System.out.println("後方一致:" + toyou);

                                }

                            }

                        }else if (toyou.indexOf(updatetouchusernamescopy) == -1) {//まだ、このユーザーが含まれていなかった場合は、普通に追加
                            toyou += "," + updatetouchusername;
                            System.out.println("まだupdatetouchusernaemcopyは含まれていない");
                             System.out.println("toyouに普通に追加"+toyou);

                        }

                    }

                    //ここまでが、>が含まれているコード

                    if(toyou==null){
                        Toast toasttoyou = Toast.makeText(this, "宛先は選択されてません", Toast.LENGTH_SHORT);
                        toasttoyou.show();

                    }else {
                        String cuttoyou = (toyou.substring(toyou.indexOf(">") + 1));

                        //左側(username)を切る選択者だけにする

                        Toast toasttoyou = Toast.makeText(this, "コメント先として" + cuttoyou + "を選択中", Toast.LENGTH_SHORT);
                        toasttoyou.show();
                    }
                    //usernameに" "と、> と、 , と、数字のみは使えなくする


                }





   //3:else  /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

                    //--------------------------------------else------------------------------------//
                    //-------------------"/"を含まない場合のdata username commentの取得----------------//
                } else {

                ///////もうここでは、itemを編集する必要は、ない、もう省かれていて、そんで、idは、取得してる
                String updateidelse = updateid;


                //ここのから文字があるかがわからない？
                String dataandand = item.replaceFirst("  ", ":");
                String nextdata = dataandand.substring(0, dataandand.indexOf(" "));

                //---data username commentを取得----//

                //---------------------------こっちのdataは、dataにtimeage(何月/何日)を追加する----------------------------//
                updatedata = timeago + nextdata;
                //先頭の" "をとってあるdataandandの" "より後ろを取り出し、"  "より前を取り出す
                String usernameanda = dataandand.substring(dataandand.indexOf(" ") + 1);
                updatetouchusername = usernameanda.substring(0, usernameanda.indexOf("  "));

                //usernameandの"  "より後ろを取り出す 取り出せん？
                updatecomment = usernameanda.substring(usernameanda.indexOf("  ") + 1);

                updatecomment = updatecomment.replaceFirst(" ", "");
                updatecomment = updatecomment.replaceFirst("\\n", "");

                //--------------------------------自分のコメントか他人のコメントかif-----------------------------------//
                //-----------------------------updatetouchusernameが数字のみであったら、取得できない、falseじゃなきゃ進めない-------------//


                //すべて数字でのusernameは、登録できないようにする


                //ここから下は、">"を入れていた場合の処理  //elseは、else ifじゃないから大丈夫
                String barter = updatetouchusername;//よびにbarterを作っておく

                //">"を含んでる場合のif文
                if (updatetouchusername.indexOf(">") != -1) {

                    String updatetouchusernamecheck = updatetouchusername.substring(0, updatetouchusername.indexOf(">"));
                    //ここで、<から左を切り出したものを、
                    //updatetouchusernameに保存
                    updatetouchusername = updatetouchusernamecheck;
                }

 //3:else()--if(username)--/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

                if (updatetouchusername.equals(username) && (!true == isNum(updatetouchusername))) {
                    //もし、あらかじめつくったbarterに"<"が含まれていた場合は、updatetouchusernameに代入をしてるから、それを、もとに戻す
                    if (barter.indexOf(">") !=-1) {
                        updatetouchusername = barter;
                    }
                    //ここから下は普通


                    String str = updatecomment;
                    byte[] arr = str.getBytes();
                    for (int i = 0; i < arr.length; i++) {
                        System.out.println("バイトの出力" + Integer.toHexString(arr[i] & 0xff));
                    }
                    //改行コードはいってた


                    //--------------ここで、修正しますか？を確認のダイアログを出力--------------//
                    LayoutInflater inflater = (LayoutInflater) LocationActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
                    final View layout = inflater.inflate(R.layout.dialog_edit_comment, (ViewGroup) findViewById(R.id.layout_edit));
                    // アラーとダイアログ を生成
                    AlertDialog.Builder builder = new AlertDialog.Builder(LocationActivity.this);
                    builder.setView(layout);


                    //-----------------------------------select二発---------------------------------//


                    String sqlselect = "select * from neardb where data == '" + updatedata + "' and comment == '" + updatecomment + "'" +
                            " and username == '" + updatetouchusername + "' and idnumber == '" + updateidelse + "';";

                    MyOpenHelper helper = new MyOpenHelper(LocationActivity.this);
                    SQLiteDatabase db = helper.getWritableDatabase();
                    Cursor c = db.rawQuery(sqlselect, null);


                    c.moveToFirst();

                    if (c != null && c.getCount() != 0) {

                        for (int i = 0; i < c.getCount(); i++) {

                            datex = c.getString(0);//data
                            namex = c.getString(1); //name
                            commentx = c.getString(2); //comment
                            latitudex = c.getDouble(3);//latitude?
                            longitudex = c.getDouble(4);//longitude?
                            idnumberx = c.getString(5);//idnumber


                            String in = "";

                            in += datex + " " + namex + "  " + commentx + " " + idnumberx + "\n";

                            //表示
                            String newin = "";
                            newin += datex + " " + namex + " " + commentx + " " + latitudex + " " + longitudex + " " + idnumberx + "\n";


                            c.moveToNext();
                        }

//--------------------------------------------selectした値が、updatedatabaseにもない場合----------------------------------------------//

                        //２つ目のselect//


                        String sqlselectupdate = "select * from neardbupdate where data == '" + datex + "' and username == '" + namex + "'" +
                                " and comment == '" + commentx + "' and latitude == " + latitudex + " and longitude == " + longitudex + " and neardbid == '" + idnumberx + "';";


                        Cursor cc = db.rawQuery(sqlselectupdate, null);

                        cc.moveToFirst();
                        if (cc != null && cc.getCount() != 0) {

                            for (int i = 0; i < cc.getCount(); i++) {


                                String date = cc.getString(0);//data
                                String name = cc.getString(1); //name
                                String comment = cc.getString(2); //comment
                                Double latitudez = cc.getDouble(3);//latitude?
                                Double longitudez = cc.getDouble(4);//longitude?
                                String neardbid = cc.getString(5);//neardbid

                                cc.moveToNext();
                                System.out.println(date + ":" + name + ":" + comment + ":" + latitudez + ":" + longitudez);
                            }


//------------------------------------------更新済みdatabaseにこの情報は登録されてたから、<から右を削除----------------------------------------------------------------//
                            String lastIndex = commentx.substring(0, commentx.lastIndexOf("<"));

                            editText = (EditText) layout.findViewById(R.id.editor_text);

                            // EditText にコメントの表示にふさわしくない部分を省略した、テキストを設定
                            editText.setText(lastIndex);

                            //terataillから、カーソルを最後の文字に合わせるコードを見つけた
                            editText.setSelection(editText.getText().length());


                        } else if (cc.getCount() == 0) { //つまり、insertする必要  今回は、editTextの事だから、 何もしないでset

                            editText = (EditText) layout.findViewById(R.id.editor_text);

                            // EditText にテキストを設定
                            editText.setText(updatecomment);

                            //terataillから、カーソルを最後の文字に合わせるコードを見つけた
                            editText.setSelection(editText.getText().length());

                        }

                    } else if (c == null) {
                        System.out.println("cがnull");
                    } else {
                        System.out.println("nullではない");
                    }


                    //-------------------------------------------------------ここまででselectする----------------------------------------------------//

                    editText.setInputType(InputType.TYPE_CLASS_TEXT);


                    builder.setPositiveButton("更新", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            // EditText のテキストを取得
                            String getedittext = editText.getText().toString();

                            //-----------------select-------------------//

                            if (getedittext.length() == 0) {

                                Toast toast = Toast.makeText(LocationActivity.this, "入力してください", Toast.LENGTH_SHORT);
                                toast.show();
                            } else if (getedittext.length() != 0) {

                                //newcommentは、EditTextからgetしたgetedittext
                                System.out.println(updatedata + "," + updatetouchusername + "," + updatecomment);

                                String sqlselect = "select * from neardb where data == '" + updatedata + "' and comment == '" + updatecomment + "'" +
                                        " and username == '" + updatetouchusername + "' and idnumber == '" + idnumberx + "';";


                                System.out.println("sqlselect :" + sqlselect);

                                MyOpenHelper helper = new MyOpenHelper(LocationActivity.this);
                                SQLiteDatabase db = helper.getWritableDatabase();
                                Cursor c = db.rawQuery(sqlselect, null);

                                c.moveToFirst();


                                if (c != null) {
                                    System.out.println("c.getCount() :" + c.getCount());

                                    for (int i = 0; i < c.getCount(); i++) {


                                        datex = c.getString(0);//data
                                        namex = c.getString(1); //name
                                        commentx = c.getString(2); //comment
                                        latitudex = c.getDouble(3);//latitude?
                                        longitudex = c.getDouble(4);//longitude?
                                        idnumberx = c.getString(5);//idnumber

                                        String in = "";

                                        in += datex + " x" + namex + " x " + commentx + " x " + idnumberx + "\n";

                                        System.out.println("in :" + in);

                                        c.moveToNext();
                                    }


//--------------------------------------------selectした値が、updatedatabaseにもない場合----------------------------------------------//


                                    String sqlselectupdate = "select * from neardbupdate where data == '" + datex + "' and comment == '" + commentx + "'" +
                                            " and username == '" + namex + "' and latitude = " + latitudex + " and longitude = " + longitudex + " and neardbid == '" + idnumberx + "';";

                                    Cursor cc = db.rawQuery(sqlselectupdate, null);


                                    cc.moveToFirst();


                                    Calendar calendar = Calendar.getInstance();
                                    int month = calendar.get(Calendar.MONTH) + 1;
                                    int day = calendar.get(Calendar.DATE);
                                    int hour = calendar.get(Calendar.HOUR);
                                    int minute = calendar.get(Calendar.MINUTE);
                                    datareset2 = "<編集" + month + "/" + day + ":" + hour + ":" + minute + ">";


                                    if (cc != null && cc.getCount() != 0) {

                                        cc.moveToFirst();

                                        for (int i = 0; i < cc.getCount(); i++) {


                                            String date = cc.getString(0);//data
                                            String name = cc.getString(1); //name
                                            String comment = cc.getString(2); //comment
                                            Double latitudez = cc.getDouble(3);//latitude?
                                            Double longitudez = cc.getDouble(4);//longitude?
                                            String neardbid = cc.getString(5);//idnumber

                                            String in = "";


                                            in += date + " " + name + "  " + comment + " " + neardbid + "\n";

                                            System.out.println("in cc :" + in);
                                            cc.moveToNext();
                                        }
                                        //----------------------neardbupdateにすでに登録されていたから、 EditTextに入力された値で、さらにupdateする--------------------//

                                        String sqlupdate = "update neardbupdate set comment = '" + getedittext + datareset2 + "' where data == '" + updatedata + "' and comment =='" + updatecomment + "'" +
                                                " and username == '" + updatetouchusername + "' and neardbid == '" + idnumberx + "';";


                                        Cursor update = db.rawQuery(sqlupdate, null);
                                        update.moveToFirst();


                                        cc.close();

                                        //------------一度もupdateしてないコメントは、ここに来るようになってる-------------//
                                    } else if (cc == null || cc.getCount() == 0) {//nullだけの判定じゃまずい？から文字の可能性もある？


                                        String updateinsert = "insert into neardbupdate (data,username,comment,latitude,longitude,neardbid)" +
                                                " values ('" + datex + "','" + updatetouchusername + "','" + getedittext + datareset2 + "'," + latitudex + "," + longitudex + ",'" + idnumberx + "');";


                                        db.execSQL(updateinsert);


                                    } else {
                                    }

                                }


//-------------------------------------------これは、普通にneardbのupdate--------------------------------------------------------//

                                Calendar calendar = Calendar.getInstance();
                                int month = calendar.get(Calendar.MONTH) + 1;
                                int day = calendar.get(Calendar.DATE);
                                int hour = calendar.get(Calendar.HOUR);
                                int minute = calendar.get(Calendar.MINUTE);
                                String datareset = "<編集" + month + "/" + day + ":" + hour + ":" + minute + ">";


                                String sqlupdate = "update neardb set comment = '" + getedittext + datareset + "' where data == '" + updatedata + "' and comment =='" + updatecomment + "'" +
                                        " and username == '" + updatetouchusername + "' and idnumber == '" + idnumberx + "' and idnumber == '" + idnumberx + "';";


                                Cursor update = db.rawQuery(sqlupdate, null);
                                update.moveToFirst();


                                //---------close();----------//


                                c.close();
                                update.close();
                                db.close();

//----------------------------------------------------そして、select----------------------------------------------------------------//

                                select(latitudex, longitudex);
                            }
                        }
                    });


                    AlertDialog alertDialog = builder.create();


                    alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface arg0) {
                            EditText getedittext = (EditText) layout.findViewById(R.id.editor_text);

                            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputMethodManager.showSoftInput(getedittext, 0);

                        }
                    });
                    alertDialog.show();


 //3:else()--else()--/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

//---------------------------------------------------違うユーザーがタップされた--------------------------------------------------------//

                } else {


                    listView = (ListView) parent;
                    String itemtoyou = (String) listView.getItemAtPosition(position);

                    System.out.println("itemtoyou:"+itemtoyou);
                    //itemtoyou:16   3:45 17  さ

                    String dataandands = itemtoyou.replaceFirst("   ", ":");
                    System.out.println("dataandands:"+dataandands);
                    String usernameandas = dataandands.substring(dataandands.indexOf(" ") + 1);
                    System.out.println("usernameandas:"+usernameandas);
                    String updatetouchusernames= usernameandas.substring(0, usernameandas.indexOf("  "));
                    System.out.println("updatetouchusernames:"+updatetouchusernames);



                    if (updatetouchusernames.indexOf(">") != -1) {
                        System.out.println("aabbbb");

                        updatetouchusernamescopy = updatetouchusernames.substring(0, updatetouchusernames.indexOf(">"));
                    } else {
                        //含まない場合は、そのまま使用
                        updatetouchusernamescopy = updatetouchusernames;
                    }


                    if (toyou == null) {

                        System.out.println("toyouはnull");
                        toyou = ">" + updatetouchusernamescopy;


                    } else if (toyou.indexOf(">") != -1) {//含まれている場合は、すでにusernameは、入っているから、あとは、toutchusernameだけ



                        if (toyou.indexOf(updatetouchusernamescopy) != -1) {//すでに、この人へが、追加されている場合は、消す

                            System.out.println("toyou:before:" + toyou);

                            toyou = toyou.replaceFirst(updatetouchusernamescopy, ""); //一番最初の頭があったら、""にする
                            System.out.println("toyou:after:" + toyou);



                            if (toyou.indexOf(",,") != -1) {//つまり2人以上が選択されているということ
                                toyou = toyou.replace(",,", ",");  //上のやつで、消したら、それを、今度は、,,を防ぐ

                            }
                            /////////
                            if (toyou.equals(">")) {//そして、もう、toyouが">"のみになったら、、toyouをnullにする
                                toyou = null;
                                System.out.println("toyou.equals>:"+toyou);

                            }else{ //つまり一つのusernameを入れて消した時は、>しかないから、charAt(1)はない状態
                                System.out.println("toyou:" + toyou);
                                char fo = toyou.charAt(1);
                                //0は、まだ、>を消す前だから、1にする
                                String stringfor = String.valueOf(fo);


                                char la = toyou.charAt(toyou.length() - 1);
                                String stringafter = String.valueOf(la);

                                System.out.println("stringfor:" + stringfor + "      stringafter:" + stringafter);


                                //  char foo = toyou.charAt(0);
                                // String stringforo = String.valueOf(fo);
                                //System.out.println("ああああああああああ:"+stringforo);

                                if (stringfor.equals(",")) {
                                    toyou = toyou.replaceFirst(",", "");
                                    System.out.println("前方一致:" + toyou);


                                } else if (stringafter.equals(",")) {
                                    toyou = toyou.substring(0, toyou.lastIndexOf(","));
                                    System.out.println("後方一致:" + toyou);

                                }

                            }
                            ///////


                        } else if (toyou.indexOf(updatetouchusernamescopy) == -1) {//まだ、このユーザーが含まれていなかった場合は、普通に追加
                            toyou += "," + updatetouchusername;
                            System.out.println("まだupdatetouchusernaemcopyは含まれていない");
                            System.out.println("toyouに普通に追加" + toyou);

                        }
                    }


                   /** char fo = toyou.charAt(0);
                        String stringfor = String.valueOf(fo);
                        if (stringfor.equals(",")) {
                            toyou=toyou.replaceFirst(",", "");
                            System.out.println("先頭の、をけす");
                        }
                    **/


                    //ここまでが、>が含まれているコード

                    if (toyou == null) {
                        Toast toasttoyou = Toast.makeText(this, "宛先は選択されてません", Toast.LENGTH_SHORT);
                        toasttoyou.show();

                    } else {


                        String cuttoyou = (toyou.substring(toyou.indexOf(">") + 1));

                        //左側(username)を切る選択者だけにする

                        Toast toasttoyou = Toast.makeText(this, "コメント先として" + cuttoyou + "を選択中", Toast.LENGTH_SHORT);
                        toasttoyou.show();
                    }

                    ///ここは、Click,LongClickの時に使う
                    //username< の<から右は、削除して、判断するようにする
                    //usernameに" "と、> と、 , と、数字のみは使えなくする

                }
            }

        } catch (NullPointerException e) {
            System.out.println("NullPointerException" + e);
        //} catch (Throwable e) {
        //   System.out.println("予期しない例外が発生:" + e);
        }

    }


    //LongClicのコード /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    //----------------------------------------------------LongClick------------------------------------------------------------------------//
    @Override
    public boolean onItemLongClick(AdapterView parent, View view, int position, long id) {
        Log.d(position + "", id + "");


        // return true;これ最後に入れないと、Clickも作動する

        // try {


        listView = (ListView) parent;
        String item = (String) listView.getItemAtPosition(position);

        //ここで、idのある先頭をカットしてupdateidに代入し、itemを元の短さにする
        //itemの" "より左を取得して、その後、削除 " "も削除
        final String deleteid = item.substring(0, item.indexOf(" "));
        System.out.println("deleteid" + deleteid);

        //updateid+" "より右を取得
        item = item.substring(item.indexOf(" ") + 1);

        System.out.println("LONG");

        //idは、作ったから、あとは、それを、指定して、where文を作ること、


        String getdesdata = item.substring(0, item.indexOf(":"));

        System.out.println(getdesdata + "　1.5");
        String getusedesdata = null;

        Integer copyposition = position;
        Integer minus = 0;


        while (getdesdata.indexOf("/") == -1) {
            // 最初の:の前に"/"を含まない限り、

            minus = copyposition--;
            System.out.println("copyposition " + copyposition);
            System.out.println("minus " + minus);

            // / を含んだものを取得するまで-1されたpositionで、itemを取得
            String includes = (String) listView.getItemAtPosition(minus);
            // /を含んだものの、" "の前を
            getdesdata = includes.substring(includes.indexOf(" ")+1);

            getdesdata = getdesdata.substring(0, getdesdata.indexOf(" "));

            getusedesdata = getdesdata;

        }


      // "/"を含んだpositionの何月何日を取得して、timeago(時間の前)に入れる
        String timeago = getusedesdata;

        //-------------------"/"を含む場合のdata username commentの取得----------------//

        String getdata = item.substring(0, item.indexOf(":"));


//LongClick if("/")  /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        //getitemに/が含まれれば次の:まで取得
        if (getdata.indexOf("/") != -1) {
            System.out.println("4");
            //何月何日が入ってるほうは、日と時間の間の、" "を:にして、
            String dataand = item.replaceFirst(" ", ":");
            System.out.println("dataand " + dataand);

            //最初のスペース(username)までの間を取得して、dataにセット
            updatedata = dataand.substring(0, dataand.indexOf(" "));
            System.out.println("data " + updatedata);

            //usernameの判定
            //" "を削除したdataandから1つめの" "から後ろを取り出す
            String usernameand = dataand.substring(dataand.indexOf(" ") + 1);

            //そして、"  "より前を取り出す
            updatetouchusername = usernameand.substring(0, usernameand.indexOf(" "));
            //usernameには、" "は入れられなくする

            //usernameandの"  "より後ろを取り出す
            updatecomment = usernameand.substring(usernameand.indexOf(" ") + 1);
            //" "を削除した
            updatecomment = updatecomment.replaceFirst(" ", "");
            updatecomment = updatecomment.replaceFirst("\\n", "");


            //--------------------------------自分のコメントか他人のコメントかif-----------------------------------//

            System.out.println(updatetouchusername + ":" + username + ":");
            System.out.println(updatetouchusername + ":" + username);


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

            //ここで、CLICKと同じコードメモしてるやつ
            String barter = updatetouchusername;//よびにbarterを作っておく
            if (updatetouchusername.indexOf(">") != -1) {

                String updatetouchusernamecheck = updatetouchusername.substring(0, updatetouchusername.indexOf(">"));
                        updatetouchusername = updatetouchusernamecheck;
            }
//LongClick if("/")--if(username)--/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

            if (updatetouchusername.equals(username) && (!true == isNum(updatetouchusername))) {
                    if (barter.indexOf(">") == 1) {
                        updatetouchusername = barter;
                    }


                LayoutInflater inflater = (LayoutInflater) LocationActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);

                final View layout = inflater.inflate(R.layout.dialog_deletecomment_us, (ViewGroup) findViewById(R.id.layout_deleteplace));

// アラーとダイアログ を生成
                AlertDialog.Builder builder = new AlertDialog.Builder(LocationActivity.this);
                builder.setView(layout);
                String sqlselect = "select * from neardb where data == '" + updatedata + "' and comment == '" + updatecomment + "'" +
                        " and username == '" + updatetouchusername + "' and idnumber == '" + deleteid + "';";

                MyOpenHelper helper = new MyOpenHelper(LocationActivity.this);
                SQLiteDatabase db = helper.getWritableDatabase();
                Cursor c = db.rawQuery(sqlselect, null);
                c.moveToFirst();


                if (c != null && c.getCount() != 0) {
                    for (int i = 0; i < c.getCount(); i++) {
                        datex = c.getString(0);//data
                        namex = c.getString(1); //name
                        commentx = c.getString(2); //comment
                        latitudex = c.getDouble(3);//latitude?
                        longitudex = c.getDouble(4);//longitude?
                        idnumberx = c.getString(5);//idnumberx
                        c.moveToNext();
                    }
                } else if (c == null) {
                    System.out.println("まず、cがnull");
                } else {
                    System.out.println("nullではない");
                }
                builder.setPositiveButton("削除", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String sqldelete = "delete from neardb where  data == '" + datex + "' and comment == '" + commentx + "'" +
                                " and username == '" + namex + "'" +
                                " and latitude == " + latitudex + " and longitude == " + longitudex + " and idnumber == '" + idnumberx + "';";


                        String sqldeleteupdate = "delete from neardbupdate where  data == '" + datex + "' and comment == '" + commentx + "'" +
                                " and username == '" + namex + "'" +
                                " and latitude == " + latitudex + " and longitude == " + longitudex + " and neardbid == '" + idnumberx + "';";

                        MyOpenHelper helper = new MyOpenHelper(LocationActivity.this);
                        SQLiteDatabase db = helper.getWritableDatabase();
                        Cursor bb = db.rawQuery(sqldelete, null);

                        Cursor dd = db.rawQuery(sqldeleteupdate, null);

                        bb.moveToFirst();
                        dd.moveToFirst();



                            for (int i = 0; i < bb.getCount(); i++) {

                                bb.moveToNext();
                            }
                            bb.close();

                            Cursor update = db.rawQuery(sqldelete, null);
                            update.moveToFirst();

                            update.close();

                            db.close();

                        select(latitudex, longitudex);

                    }

                });
                builder.create().show();
            }



//LongClick else--/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        } else {
            //先頭の" "を":"にして、dataandandに代入
            // String dataandand = item.replaceFirst(" ","");
            String dataandand = item.replaceFirst("  ", ":");
            //dataandandの最初の" "までをnextdataに代入

            String nextdata = dataandand.substring(0, dataandand.indexOf(" "));
            //---data username commentを取得----//

            //------------------------------こっちのdataは、dataにtimeage(何月/何日)を追加する----------------------------//
            updatedata = timeago + nextdata;
            //username
            //先頭の" "をとってあるdataandandの" "より後ろを取り出し、"  "より前を取り出す
            String usernameand = dataandand.substring(dataandand.indexOf(" ") + 1);
            updatetouchusername = usernameand.substring(0, usernameand.indexOf("  "));
            //usernameandの"  "より後ろを取り出す 取り出せん？

            updatecomment = usernameand.substring(usernameand.indexOf("  ") + 1);

            updatecomment = updatecomment.replaceFirst(" ", "");
            updatecomment = updatecomment.replaceFirst("\\n", "");



            //--------------------------------自分のコメントか他人のコメントかif-----------------------------------//
            //--------------------------------updatetouchusernameが数字のみであったら、取得は、できない、だからfalseじゃなきゃ進めない-------------//




            //ここで、CLICKと同じコードメモしてるやつ
            String barter = updatetouchusername;//よびにbarterを作っておく
            if (updatetouchusername.indexOf(">") != -1) {

                String updatetouchusernamecheck = updatetouchusername.substring(0, updatetouchusername.indexOf(">"));
                updatetouchusername = updatetouchusernamecheck;
            }


 //LongClick else()--if(username)--/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

            if (updatetouchusername.equals(username) && (!true == isNum(updatetouchusername))) {
                if (barter.indexOf(">") == 1) {
                    updatetouchusername = barter;
                }

                //--------------ここで、修正しますか？を確認のダイアログを出力--------------//
                LayoutInflater inflater = (LayoutInflater) LocationActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);

                final View layout = inflater.inflate(R.layout.dialog_deletecomment_us, (ViewGroup) findViewById(R.id.layout_deleteplace));
                // アラーとダイアログ を生成
                AlertDialog.Builder builder = new AlertDialog.Builder(LocationActivity.this);
                builder.setView(layout);


//-----------------------------------selectする---------------------------------//


                System.out.println(updatedata + "," + updatetouchusername + "," + updatecomment);


                String sqlselect = "select * from neardb where data == '" + updatedata + "' and comment == '" + updatecomment + "'" +
                        " and username == '" + updatetouchusername + "' and idnumber == '" + deleteid + "';";



                //一つ目なんだから、latitudeとかは、知るわけない

                System.out.println("sqlselect1つめ :" + sqlselect);

                MyOpenHelper helper = new MyOpenHelper(LocationActivity.this);
                SQLiteDatabase db = helper.getWritableDatabase();
                Cursor c = db.rawQuery(sqlselect, null);


                c.moveToFirst();


                if (c != null && c.getCount() != 0) {
                    Log.d("c!=null 次はfor()", "nullじゃない確定");

                    System.out.println("c.getCount() :" + c.getCount());

                    for (int i = 0; i < c.getCount(); i++) {



                        datex = c.getString(0);//data
                        namex = c.getString(1); //name
                        commentx = c.getString(2); //comment
                        latitudex = c.getDouble(3);//latitude?
                        longitudex = c.getDouble(4);//longitude?
                        idnumberx = c.getString(5);//idnumberx


                        System.out.println("commentx:" + commentx);


                        String in = "";

                        in += datex + " " + namex + "  " + commentx + " " + idnumberx + "\n";

                        System.out.println("in :" + in);


                        //表示
                        String newin = "";
                        newin += datex + " " + namex + " " + commentx + " " + latitudex + " " + longitudex + " " + idnumberx + "\n";

                        System.out.println("newin :" + newin);

                        c.moveToNext();
                    }



                } else if (c == null) {
                    System.out.println("cがnull");
                } else {
                    System.out.println("nullではないけどelse");
                }



                builder.setPositiveButton("削除", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        //-----------------select-------------------//


                        //deleteに変える
                        String sqldelete = "delete from neardb where  data == '" + datex + "' and comment == '" + commentx + "'" +
                                " and username == '" + namex + "'" +
                                " and latitude == " + latitudex + " and longitude == " + longitudex + " and idnumber == '" + idnumberx + "';";


                        String sqldeleteupdate = "delete from neardbupdate where  data == '" + datex + "' and comment == '" + commentx + "'" +
                                " and username == '" + namex + "'" +
                                " and latitude == " + latitudex + " and longitude == " + longitudex + " and neardbid == '" + idnumberx + "';";


                        System.out.println("sqldelete : " + sqldelete);


                        MyOpenHelper helper = new MyOpenHelper(LocationActivity.this);
                        SQLiteDatabase db = helper.getWritableDatabase();
                        Cursor bb = db.rawQuery(sqldelete, null);

                        Cursor dd = db.rawQuery(sqldeleteupdate, null);


                        System.out.println("bb.getCount() " + bb.getCount());
                        System.out.println("dd.getCount()  " + dd.getCount());


                        bb.moveToFirst();
                        dd.moveToFirst();


                        if (bb != null) {


                            for (int i = 0; i < bb.getCount(); i++) {

                                bb.moveToNext();
                            }


                            //---------close();----------//
                            bb.close();

                            Cursor update = db.rawQuery(sqldelete, null);
                            update.moveToFirst();

                            update.close();

                            db.close();


                        }

                        select(latitudex, longitudex);

                    }

                });
                builder.create().show();

            }

        }
  //  } catch (Exception e) {
  //      System.out.println(e);
   // }

        return true;
    }


    //これでtureが返されれば、全部数字だから、
    static boolean isNum(String username) {
        try {
            Integer.parseInt(username);
            return true;
        } catch (NumberFormatException e) {


            return false;
        }

    }





//onSwitchClicked() --/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //--------------------------------------------------------onSwitchClicked()-----------------------------------------------------------//
    //swtch
    public void onSwitchClicked(View view) {
        Switch swtOnOff = (Switch) view;
        if (swtOnOff.isChecked()) { // ON状態になったとき
            Toast toast = Toast.makeText(LocationActivity.this, "GPSをONしました", Toast.LENGTH_SHORT);
            toast.show();

            String activity = getIntent().getStringExtra("Activity");
            if (activity.equals("1")) {
                startFusedLocation();

            } else if (activity.equals("2")) {
//GPSが許可されていないのにgpsのonボタンが押された時は、MainActivityの検査に行く
                Intent intent = new Intent(LocationActivity.this, MainActivity.class);
                startActivity(intent);
            }


        } else if (!(swtOnOff.isChecked())) {
            Toast toast = Toast.makeText(LocationActivity.this, "GPSをOFFしました", Toast.LENGTH_SHORT);
            toast.show();
            stopFusedLocation();
        }
    }



//startFusedLocation() --/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //----------------------------------------------------------startFusedLocation()----------------------------------------------------//
    //startFusedLocation()
    private void startFusedLocation() {


        if (!mResolvingError) {


            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this) //failedListenerがある
                    .build();

            if (mGoogleApiClient.isConnected()) {
            } else {
                mGoogleApiClient.connect();
            }
        } else {
        }
    }



//onConnected() --/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //-------------------------------------------------------------onConnected()-------------------------------------------------------//
    @Override
    public void onConnected(Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationProviderApi = LocationServices.FusedLocationApi;

        Location currentLocation = fusedLocationProviderApi.getLastLocation(mGoogleApiClient);

        if (mGoogleApiClient.isConnected()) {
            System.out.println("CONNECTED now");
        } else {
            System.out.println("not CONNECTED");
        }

        if (currentLocation != null && currentLocation.getTime() > 20000) {

            location = currentLocation;

//現在地を取得してる
            latitude = location.getLatitude();
            longitude = location.getLongitude();


            String activity = getIntent().getStringExtra("Activity");
            if (activity.equals("1")) {

                if (latitude2 == 0) {
                    select(latitude, longitude);
                } else if (latitude2 != 0) {

                    select(latitude2, longitude2);

                }
            }

        } else {

            // バックグラウンドから戻ってしまうと例外が発生する場合がある
            try {
                fusedLocationProviderApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this);

                Executors.newScheduledThreadPool(1).schedule(new Runnable() {
                    @Override
                    public void run() {
                        fusedLocationProviderApi.removeLocationUpdates(mGoogleApiClient, LocationActivity.this);
                    }
                }, 60000, TimeUnit.MILLISECONDS);


            } catch (Exception e) {
                System.out.println(e);
                Toast toast = Toast.makeText(this, "例外が発生、位置情報のPermissionを許可していますか？", Toast.LENGTH_SHORT);
                toast.show();

                //MainActivityに戻す
                finish();
                //finishは、問題がある、対策をすればいいが、むずいから、(ネットで調べればいっぱいある、

            }
        }

    }


    private void stopFusedLocation() {
        mGoogleApiClient.disconnect();
        Log.d("onStop()", "stopFusedLocation");

    }


    //onMapReady --/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //----------------------------------------------------------onMapReady---------------------------------------------------------------//
    private GoogleMap mMap;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        String activity = getIntent().getStringExtra("Activity");
        if (activity.equals("1")) {
            LatLng now = new LatLng(latitude, longitude);
            mMap.addMarker(new MarkerOptions().position(now).title("You are here"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(now));
            CameraUpdate cUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 12);
            mMap.moveCamera(cUpdate);
        } else if (activity.equals("2")) {
            LatLng shibuya = new LatLng(35, 139);
            mMap.addMarker(new MarkerOptions().position(shibuya).title("Marker in Shibuya"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(shibuya));
            CameraUpdate cUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(35, 139), 12);
            mMap.moveCamera(cUpdate);
        }

        setContentView(R.layout.activity_maps);
        MapFragment map = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        map.getMapAsync(this);

        // 各種コールバック
        googleMap.setOnMapLongClickListener(this);
    }




//onMapLongClick() --/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //--------------------------------------------------------onMapLongClick()-----------------------------------------------------------//
    @Override
    public void onMapLongClick(LatLng latLng) {
        mMap.addMarker(new MarkerOptions().title("LongClick").position(latLng).draggable(false));

        Log.i("MapTest", "LongClick:" + latLng.latitude + "," + latLng.longitude);

        ///////////長押し検知されたら、
        latitude2 = latLng.latitude;
        latitude2 = latLng.latitude;
        select(latitude2, longitude2);
    }



    @Override
    protected void onStop() {
        super.onStop();
        stopFusedLocation();
    }



//onLocationChanged --/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

//------------------------------------------------------------onLocationChanged--------------------------------------------------------//
    //あるサイトによると2秒ごとに呼ばれるらしいからべつにok
    //呼ばれてなくない?再度スタートした時だけじゃん？
    //そん時はてらているのやつを使う


    @Override
    public void onLocationChanged(Location location) {

        ListView listView = (ListView) findViewById(R.id.list_view);
        //こっちのtextViewも後々

        textLog += "onLocationChanged\n";
        textView.setText(textLog);


        lastLocationTime = location.getTime() - lastLocationTime;
        textLog += "onLocationChanged:定期的に呼ばれてる?\n";
        textLog += "Latitude:" + String.valueOf(location.getLatitude()) + "\n";
        textLog += "Longitude:" + String.valueOf(location.getLongitude()) + "\n";
        textView.setText(textLog);


        latitude = location.getLatitude();
        longitude = location.getLongitude();


        MyOpenHelper helper = new MyOpenHelper(LocationActivity.this);
        SQLiteDatabase db = helper.getReadableDatabase();

        double clearlatitude = 0.00002694944;
        double clearlongitude = 0.00032899147;


        String sql = "select data,username,comment from neardb where latitude - " + latitude + " <= " + clearlatitude + " and "
                + latitude + " - latitude <= " + clearlatitude +
                " and longitude - " + longitude + " <= " + clearlongitude + " and "
                + longitude + " - longitude <= " + clearlongitude + ";";


        Cursor c = db.rawQuery(sql, null);

        boolean mov = c.moveToFirst();

        while (mov) {

            textView.setText(String.format("%s    %s : %s", c.getString(0), c.getString(1), c.getString(2)));
            mov = c.moveToNext();

        }
        c.close();
        db.close();

    }


    ////////////別
    @Override
    public void onConnectionSuspended(int i) {
        Log.d("onConnectionSuspended()", "");
    }


    private static final int REQUEST_RESOLVE_ERROR = 1001;
    private static final String DIALOG_ERROR = "dialog_error";

   ////////////別
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (mResolvingError) {
            return;
        } else if (connectionResult.hasResolution()) {
            try {
                mResolvingError = true;
                connectionResult.startResolutionForResult(this, REQUEST_RESOLVE_ERROR);
            } catch (IntentSender.SendIntentException e) {

                mGoogleApiClient.connect();
            }
        } else {
            showErrorDialog(connectionResult.getErrorCode());
            mResolvingError = true;
            System.out.println("else");
        }
    }

    private void showErrorDialog(int errorCode) {
        System.out.println("showErrorDialog");
        ErrorDialogFragment dialogFragment = new ErrorDialogFragment();
        Bundle args = new Bundle();
        args.putInt(DIALOG_ERROR, errorCode);
        dialogFragment.setArguments(args);
        dialogFragment.show(getSupportFragmentManager(), "errordialog");
    }

    public void onDialogDismissed() {
        System.out.println("onDialogDismissed");
        mResolvingError = false;
    }



    public static class ErrorDialogFragment extends DialogFragment {
        public ErrorDialogFragment() {
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            int errorCode = this.getArguments().getInt(DIALOG_ERROR);
            return GoogleApiAvailability.getInstance().getErrorDialog(
                    this.getActivity(), errorCode, REQUEST_RESOLVE_ERROR);
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            ((LocationActivity) getActivity()).onDialogDismissed();
            System.out.println("onDialogDismissed");
        }
    }


   // select() エラーが出るようになってから


//select() --/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

//-------------------------------------------------------select()---------------------------------------------------------------------//

    //新しくselect()メソッド
    //latutide2も
   public void select(double latitude,double longitude){


      select selectclass=new select();
             selectclass.select(latitude,longitude,this);

       final ArrayList<String> listItems = selectclass.select(latitude,longitude,this);



       listView = (ListView) findViewById(R.id.list_view);
       ArrayAdapter<String> adapterlist = new ArrayAdapter<String>(LocationActivity.this, android.R.layout.simple_list_item_1, listItems);
       adapterlist.setDropDownViewResource(android.R.layout.simple_list_item_1);
       listView.setAdapter(adapterlist);

//listView.setOnItemClickListener(new LocationActivity());
       listView.setOnItemClickListener(this);

       listView.setOnItemLongClickListener(this);


   }







}



//Locationにほうちでいい
//--------------------------------------------------自動更新ができなかった時用のTimer----------------------------------------------------//

/**
 //5分おきにデータベースから近い人のコメントを取得して、textにセット
 //タイマー
 Timer timer = null;

 // タイマーを開始する
 //starttimerは、createのとこにおく
 private void startTimer() {

 int firstInterval = 100;
 int interval = 100;

 // Timerオブジェクトの生成
 timer = new Timer();

 // タイマーを開始する
 timer.schedule(new TimerTask() {

 // タイマーが満了した
 @Override
 public void run() {

 //下のコードをosを選択し、書いた
 Handler handler = new Handler();
 handler.post(new Runnable() {
 @Override
 public void run() {

 //この中でデータベースのクラスを呼ぶ
 Log.w("tag", "Timer has expired.");

 Intent dbIntent = new Intent(LocationActivity.this,
 ShowDataBase.class);
 startActivity(dbIntent);

 }
 });
 }

 }, firstInterval, interval);
 }
 **/



