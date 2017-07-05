//New3 あ つぎは、Adapterを自作して、listの中の文字をクラスごとに受け取る
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
import android.widget.SpinnerAdapter;
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
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.android.sample.new3.R.id.spinner;
import static com.example.android.sample.new3.R.id.tool_bar;


public class LocationActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        ListView.OnItemClickListener,ListView.OnItemLongClickListener {
    //onMapReadyをコールバックすれば自動でonMapLeadyが呼ばれる

   private String spinnername=null;


    favorite favorite = new favorite();
    //クラス変数にする
    String toyou = null;
    String updatetouchusernamescopy;
    //listViewの定義
    protected ListView listView;
    String deleteitem = null;
    String choiceitem=null;
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

    String placename;


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
    private Spinner spinner2;
    //String spinnerplace; deleteitemで代用
    Double spinnermath=30.0;
    private TextView textViewSpinner;
    String spinnerItems[];
    String spinnerMetter[];

    public String ok="";

    //position
    Integer spinnerposition;

    private Menu menu;



   public ArrayList<User> adapterlist;
    //--------------------------------static のクラス変数 usernameを定義、usernameをnullに----------------------------------------//
    static String username = null;

    //---------------------------------------------------------onCreate()------------------------------------------------------------------//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        spinnerMetter = new String[5];
        spinnerMetter[0] = "30m";
        spinnerMetter[1] = "60m";
        spinnerMetter[2] ="200m";
        spinnerMetter[3] ="1km";
        spinnerMetter[4] ="3km";
        //このしたが、キーボードが押されないようにしれる
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(tool_bar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");
        mResolvingError = false;
        //nullになるから、ここでよんどく
        MyOpenHelper helper = new MyOpenHelper(LocationActivity.this);
        //spinnerItems= favorite.favorite(LocationActivity.this,username);//これでok
    }

    //----------------------------------------------onCreateOptionMenu : onOptionItemSelected-----------------------------------------------//
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    //Menu#findItemでMenuItemインスタンスを取得し、MenuItem#setTitleを使用
    @Override
    public boolean onPrepareOptionsMenu (Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // メニューアイテムを取得
        MenuItem action_deleteplace = (MenuItem) menu.findItem(R.id.action_deleteplace);
        MenuItem action_updateplace = (MenuItem) menu.findItem(R.id.action_updateplace);
        MenuItem action_deleteuser = (MenuItem) menu.findItem(R.id.action_deleteuser);
        MenuItem action_setlogin = (MenuItem) menu.findItem(R.id.action_setlogin);
        MenuItem action_nowscreenset = (MenuItem) menu.findItem(R.id.action_nowscreenset);

        if (deleteitem != null) {
            if(username==null){
                action_deleteuser.setVisible(false);
                action_deleteuser.setVisible(false);
                action_nowscreenset.setVisible(false);
                action_setlogin.setTitle("ログイン/新規登録");

                //ここで、使ったmenuを使って、押されたボタンの、titleを取得して、それ次第で、レイアウトを表示する
            }else if(username!=null){
                action_nowscreenset.setVisible(true);
                action_deleteuser.setVisible(true);
                action_deleteuser.setTitle(username + "/アカウント");
                action_setlogin.setTitle(username + "/ログアウト");
            }

            if (deleteitem.equals("GPSの現在地")) {
                action_deleteplace.setVisible(false);
                action_updateplace.setVisible(false);

            } else if (deleteitem.equals("googlemapで検索")) {
                action_deleteplace.setVisible(false);
                action_updateplace.setVisible(false);

            } else {
                if (username != null) {
                    action_nowscreenset.setVisible(false);
                    action_deleteplace.setVisible(true);
                    action_updateplace.setVisible(true);
                }
            }
        }
        return true;
    }



    //ifelse2は、違う、現在地のlatitudeを取得しないといけない
    public void ifelse2(double zz){//押された時のみ、onStart()だから繰り返しにはならない
        spinnermath=zz;//押された時に代入しておく、これは、onStart()でリセットされて、初期値の30.0になる?
        //ここで、latitude2が、mapのと違ければ、つまり、okが解除されてたら、
        //latitude2を使う
            if(latitude!=0.0&&latitude!=0){
                //なんでlatitudeが2のやつになってんの？
                select(latitude,longitude,spinnermath);

            }else{
                //現在地が取得できない場合は、listを、とりあえず削除するメソッドを
                Toast toast = Toast.makeText(LocationActivity.this, "GPSがOFFです", Toast.LENGTH_SHORT);
                toast.show();
                //OFFの場合は、onStart()?
                onStart();
            }
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

            //ログインしてる時はこっち
            if (username != null && !true == isNum(username)){//その時は、ログアウトのみの表示

                //alertを作る前で、endo/ログアウトになってる場合は、ログアウト専用のalertを表示するようにする

                final View layoutlogout = inflater.inflate(R.layout.dialog_logout, (ViewGroup) findViewById(R.id.layout_root));
                AlertDialog.Builder builderlogout = new AlertDialog.Builder(this);
                builderlogout.setView(layoutlogout);
                builderlogout.setNeutralButton("ログアウト", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (username != null && !true == isNum(username)) {
                            Toast.makeText(LocationActivity.this, "ログアウトしました。", Toast.LENGTH_SHORT).show();
                            username = null;
                            MyOpenHelper helper = new MyOpenHelper(LocationActivity.this);
                            SQLiteDatabase db = helper.getWritableDatabase();
                            spinnerItems = favorite.favorite(LocationActivity.this, username);//これでok
                        } else {
                            Toast.makeText(LocationActivity.this, "ログインしていません", Toast.LENGTH_SHORT).show();
                        }
                        onStart();
                    }
                });
                builderlogout.create().show();
            }else {

                //////////////


                /**
                 // MenuItem action_deleteplace = (MenuItem) menu.findItem(R.id.action_deleteplace);
                 // action_deleteplace.getTitle();

                 final  View layout = inflater.inflate(R.layout.dialog_contact_us, (ViewGroup) findViewById(R.id.layout_root));

                 Alart alart=new Alart();
                 InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                 alart.newSet(inflater,this,layout,inputMethodManager);

                 System.out.println("aaaaaaazzzzzzzzzzzzeeeeeeeeeettttttttt"+username);
                 //username =alart.getUsername();
                 System.out.println("aaaaaaaaaakkkkkkkkkkkeeeeeeeeeeessssssssiiiiiiiii"+username);
                 // spinnerItems = favorite.favorite(LocationActivity.this, username);//これでok
                 // arrayadapter();

                 // onStart();
                 **/

                ///////////////////////////////////////////////////////////////////////ここに、新しい、サイトの情報/////////////////////////
/**
 // いろいろな準備 (1)

 MyConfirmDialog.OnResultListener impl = new MyConfirmDialog.OnResultListener() {
 public void onOK() {
 // (1)やMyActivity のメンバ変数を使った処理
 }
 };

 new MyConfirmDialog().show(this, impl);
 **/

                ///////////////////////////////////////////////////////////////////////この間　


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
                                spinnerItems = favorite.favorite(LocationActivity.this, username);//これでok

                                onStart();
                                System.out.println("新規登録完了" + username);
                                Toast toast = Toast.makeText(LocationActivity.this, "登録完了しました。ログインしました", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        }
                    }
                });
                //ここで、別のメソッドを呼び出して、それにって、if(else)で、ボタンの数、

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
                            spinnerItems = favorite.favorite(LocationActivity.this, username);//これでok
                            arrayadapter();
                            //onStart();
                            //いちいちonStartしてるから帰る

                        } else if (count == 0) {
                            Toast toast = Toast.makeText(LocationActivity.this, "ログインできませんでした。", Toast.LENGTH_SHORT);
                            toast.show();
                            getusername2.setText("");
                            getpassword2.setText("");

                        } else {
                            Toast toast = Toast.makeText(LocationActivity.this, "ログインできませんでした。count:" + count, Toast.LENGTH_SHORT);
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

            }//ここは、ログイン/新規登録 とmenuが表示されてる場合は、出ない
//-------------------------------------------------現在表示のおきにいり削除--------------------------------------------------//
        } else if (id == R.id.action_deleteplace) {
            // カスタムビューを設定
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(
                    LAYOUT_INFLATER_SERVICE);
            final View layout = inflater.inflate(R.layout.dialog_deleteplace_us, (ViewGroup) findViewById(R.id.layout_deleteplace));
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(layout);

            if (deleteitem.equals("GPSの現在地")) {
                Toast toast = Toast.makeText(LocationActivity.this, "お気に入りが選択されていません" + deleteitem, Toast.LENGTH_SHORT);
                toast.show();

            } else if (deleteitem.equals("googlemapで検索")) {
                Toast toast = Toast.makeText(LocationActivity.this, "お気に入りが選択されていません", Toast.LENGTH_SHORT);
                toast.show();

            } else {
                //ここで、textにお気に入り、deleteitemをsetする
                TextView deletetext = (TextView) layout.findViewById(R.id.textView);
                deletetext.setText(deleteitem+"を削除しますか？");

                builder.setPositiveButton("削除します。", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String sql = "delete from favorite where  placename  = '" + deleteitem + "' " +
                                "and username = '" + username + "'" +
                                "and  latitude  = '" + latitude2 + "'" +
                                "and longitude  = '" + longitude2 + "';";
                        System.out.println("現在地のお気に入り削除" + username);
                        MyOpenHelper helper = new MyOpenHelper(LocationActivity.this);
                        SQLiteDatabase db = helper.getWritableDatabase();
                        Cursor c = db.rawQuery(sql, null);
                        System.out.println(c.getCount());
                        c.moveToFirst();
                        spinnerItems = favorite.favorite(LocationActivity.this, username);//これでok
                        //リスナーの終わり、
                         arrayadapter();

                        //やっぱり、latitude2にlatitudeを代入することが一番
                        ifelse2(spinnermath);
                       latitude2=0;
                       longitude2=0;

                    }
                });
                builder.create().show();

            }
            return super.onOptionsItemSelected(item);


//---------------------------------------------------現在表示のお気に入りの名前変更-------------------------------------------//
        } else if (id == R.id.action_updateplace) {
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(
                    LAYOUT_INFLATER_SERVICE);
            final View layout = inflater.inflate(R.layout.dialog_updateplace_us, (ViewGroup) findViewById(R.id.layout_updateplace));
            AlertDialog.Builder builder = new AlertDialog.Builder(LocationActivity.this);
            builder.setView(layout);
            if (deleteitem.equals("GPSの現在地")) {
                Toast toast = Toast.makeText(LocationActivity.this, "お気に入りが選択されていません" , Toast.LENGTH_SHORT);
                toast.show();

            } else if (deleteitem.equals("googlemapで検索")) {
                Toast toast = Toast.makeText(LocationActivity.this, "お気に入りが選択されていません", Toast.LENGTH_SHORT);
                toast.show();

            } else {
                EditText updateplace2 = (EditText) layout.findViewById(R.id.updateplace);
                updateplace2.setText(deleteitem);
                updateplace2.setSelection(updateplace2.getText().length());


                builder.setPositiveButton("名称変更", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        EditText updateplace2 = (EditText) layout.findViewById(R.id.updateplace);
                        String updateplace = updateplace2.getText().toString();
                        updateplace = updateplace.trim();
                        //deleteitemでもいい
                        if (updateplace.length() == 0) {
                            Toast toast = Toast.makeText(LocationActivity.this, "入力してください", Toast.LENGTH_SHORT);
                            toast.show();
                        } else if (updateplace.length() != 0) {
                            String sql = "update  favorite set placename ='" + updateplace + "' " +
                                    "where  username = '" + username + "'" +
                                    "and placename = '" + deleteitem + "'" +
                                    "and  latitude  = '" + latitude2 + "'" +
                                    "and longitude  = '" + longitude2 + "';";
                            System.out.println(deleteitem);
                            MyOpenHelper helper = new MyOpenHelper(LocationActivity.this);
                            SQLiteDatabase db = helper.getWritableDatabase();
                            Cursor c = db.rawQuery(sql, null);
                            System.out.println(c.getCount());
                            c.moveToFirst();
                            spinnerItems = favorite.favorite(LocationActivity.this, username);//これでok
                            //onStart();
                            arrayadapter();
                            spinner.setSelection(spinnerposition);
                            //これで、現在選択されているspinnerが表示される
                            //削除した場合は、GPSの現在地が選択、その時は、ここは、呼ばれない
                        }
                    }

                });
                AlertDialog alertDialog = builder.create();
                alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface arg0) {
                        EditText updateplace2 = (EditText) layout.findViewById(R.id.updateplace);
                        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.showSoftInput(updateplace2, 0);
                    }
                });
                alertDialog.show();
            }
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


                if (username == null) {
                    //ログインしてくださいと表示
                    Toast toast = Toast.makeText(LocationActivity.this, "ログインしてください", Toast.LENGTH_SHORT);
                    toast.show();

                }else if (username != null) {
                    builder.setPositiveButton("登録", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {


                            EditText getplacename = (EditText) layout.findViewById(R.id.placename);
                            //入力した文字をトースト出力する
                            placename = getplacename.getText().toString();
                            placename = placename.trim();
                            if (placename.length() == 0) {
                                Toast toast = Toast.makeText(LocationActivity.this, "名称が入力されていません", Toast.LENGTH_SHORT);
                                toast.show();
                            } else if (placename.length() != 0) {

                                //ここに埋め込んだ
                                MyOpenHelper helper = new MyOpenHelper(LocationActivity.this);
                                SQLiteDatabase db = helper.getWritableDatabase();

                                //ここのif()はselectのためじゃなくて、insertでlatitudeかlatitude2のどちらを使うかを判定するため、
                                if (latitude2 != 0) {
                                    String sql = "insert into favorite (username,placename,latitude,longitude) " +
                                            "values('" + username + "','" + placename + "'," + latitude2 + "," + longitude2 + ");";
                                    db.execSQL(sql);
                                    //favoriteににinsetしたからfavoriteよんでspinner更新
                                    spinnerItems = favorite.favorite(LocationActivity.this, username);//これでok
                                    //onStart();
                                    select(latitude2, longitude2, spinnermath);
                                } else if (latitude2 == 0) {
                                    String sql = "insert into favorite (username,placename,latitude,longitude) " +
                                            "values('" + username + "','" + placename + "'" +
                                            "," + latitude + "," + longitude + ");";
                                    db.execSQL(sql);
                                    select(latitude, longitude, spinnermath);
                                    //favoriteににinsetしたからfavoriteよんでspinner更新
                                    spinnerItems = favorite.favorite(LocationActivity.this, username);//これでok
                                    //onStart();
                                }
                                arrayadapter();
                                setSelection(spinner, placename);
                                // 作ったspinnerの名前から、selectするmethodを呼び出してる
                                //ここで、新しく登録したspinnerを選択するようにするから、違うやつ
                                // spinner.setSelection(spinnerposition);
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

        }

        return super.onOptionsItemSelected(item);
    }
    ////これを、新しく作られたspinnerのなで、EditTextから取得して、引数itemに入れてyobidasu
    public static void setSelection(Spinner spinner, String item) {
        // ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerItems);
        SpinnerAdapter adapter = spinner.getAdapter();
        int index = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            if (adapter.getItem(i).equals(item)) {
                index = i; break;
            }
        }
        spinner.setSelection(index);
    }

    //----------------------------------------------------------onStart()-------------------------------------------------------------------//
    @Override
    protected void onStart() {
        super.onStart();

        username="endo";

        listView = (ListView) findViewById(R.id.list_view);
        // LocationRequest を生成して精度、インターバルを設定
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(16);

        String activity = getIntent().getStringExtra("Activity");

        Alart alart=new Alart();
        if(alart.getUsername()==null) {
            System.out.println("nullllllllllllllllllllllllllllllllllnulllllllllllllllnullllllllllnullllllllll");
            // if(getIntent().getStringExtra("sername")==null) {
            //  }else if(getIntent().getStringExtra("sername")!=null){
        }else if(alart.getUsername()!=null){
            //String getusername = getIntent().getStringExtra("sername");
            //username=getusername;
            username=alart.getUsername();
            spinnerItems = favorite.favorite(LocationActivity.this, username);//これでok
            arrayadapter();
            String aaaa=null;
           // getIntent().putExtra("username",aaaa);
            alart.setUsername(aaaa);
        }





        Globals globals;
       globals =(Globals)this.getApplication();
        if (globals.getOk() != null) {
            ok = globals.getOk();
            if (ok.equals("ok")) {
                latitude2 = getIntent().getDoubleExtra("latitude2", latitude2);
                longitude2 = getIntent().getDoubleExtra("longitude2", longitude2);
                globals.setOk("not ok");
                ok=globals.getOk();
            }
        }
        System.out.println("latitude2:" + latitude2 + ":longitude2:" + longitude2);
        //----------intentが1の時は、自動でon------------//
        if (activity.equals("1")) {
            Switch mSwitch = (Switch)
                    findViewById(R.id.swtOnOff);
            mSwitch.setChecked(true);  // 状態をONに
            if (latitude2 != 0) {//MapActivityから呼ばれた場合は、ここで、ロングクリックの場所をselectする
                System.out.println("onstart() latitude2" + latitude2 + ":" + longitude2);
                System.out.println("selectはしなくても、latitude2が使われるからだいじょぶ");
                // select(latitude2,longitude2);
            }
        } else if (activity.equals("2")) {//すでにlatitude2は0じゃなくなっているはず、一つめのifで
            //ここnullでやらなくて大丈夫かな？
            if (latitude2 != 0) {//MapActivityから呼ばれた場合は、ここで、ロングクリックの場所をselectする
                System.out.println("onstart() latitude2" + latitude2 + ":" + longitude2);
                //select(latitude2, longitude2);
                //一度latitudeとかにsetされれば、intentでMainに飛ばしてもいいならおk
            } else if (latitude2 == 0) {
                 Toast toast = Toast.makeText(this, "コミュにティーを選んでください", Toast.LENGTH_SHORT);
                //標準でどこかを表示する
                toast.show();
            }
        }
//--------------------------------------------------toolbarの設定-------------------------------------------------------------------------//
        Toolbar toolbar = (Toolbar) findViewById(tool_bar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");


        //---------------- Spinner の選択されているアイテムを設定-------------//
        spinner = (Spinner) findViewById(R.id.spinner);



        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Spinner spinner = (Spinner) parent;
                String item = (String) spinner.getSelectedItem();
                deleteitem = item;
                //クラス変数に入れる。今選択されていたspinnerを後で指定できるように
                spinnerposition=position;

                //あ
                //違うのを選択しても、これになるから、intentにnullをpushする
                //spinner = (Spinner) findViewById(R.id.spinner);

               if("null" != getIntent().getStringExtra("title")){
                    String maptitle= getIntent().getStringExtra("title");
                    setSelection(spinner,maptitle);
                   Intent intent = getIntent();
                    intent.putExtra("title", "null");
                     }

                //あ
                //削除の場合はいらない
                //------------------------------------------spinner初回起動------------------------------------------------//
                if (spinner.isFocusable() == false) {
                    spinner.setFocusable(true);
                    String activity = getIntent().getStringExtra("Activity");
                    if (item.equals("GPSの現在地")) {
                        if (activity.equals("1")) {
                            //gpsがoffにされたら、activityを2にする、 そして、クラス変数にする
                            startFusedLocation();
                            //これで初期化現在地だから
                            if (latitude2 == 0) {
                                latitude2 = 0;
                                longitude2 = 0;
                            }
                            but = null;
                        } else if (activity.equals("2")) {
                            Toast.makeText(getApplicationContext(), "何もしない", Toast.LENGTH_SHORT).show();
                            //これで初期化現在地だから
                            if (latitude2 == 0) {
                                latitude2 = 0;
                                longitude2 = 0;
                            }
                            but = null;
                        }
                        return;
                    }
                }

//--------------------------------------------gpsの現在地---------------------------------------------------------------------------//
                if (item.equals("GPSの現在地")) {
                    System.out.println("GPSの現在地");
                    String activity = getIntent().getStringExtra("Activity");
                    if (activity.equals("1")) {
                        //gpsがoffにされたら、activityを2にする、 そして、クラス変数にする
                        startFusedLocation();
                        //これで初期化現在地だから
                        latitude2 = 0;
                        longitude2 = 0;
                        but = null;
                    } else if (activity.equals("2")) {
                        //何もしない//これで初期化現在地だから
                        latitude2 = 0;
                        longitude2 = 0;
                        but = null;
                    }

//------------------------------------------------------googlemapで検索-----------------------------------------------------------------//
                } else if (item.equals("googlemapで登録")) {
                    System.out.println("googlemapで登録");
                    /**
                     //この下はonCrateでやること
                     setContentView(R.layout.activity_map);
                     SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                     .findFragmentById(R.id.map);
                     mapFragment.getMapAsync(LocationActivity.this);
                     **/
                    //代わりに、MapActivityを呼ぶ
                    String one = "1";
                    String two = "2";
                        Intent intent = new Intent(getApplication(), MapActivity.class);
                        String activity = getIntent().getStringExtra("Activity");
                    //でも一度マップでlongClickしてたら3になってるから、
                    //ちなみにonStartで123をifしてる
                    if (activity.equals("1")) {
                        intent.putExtra("Activity", one);
                        intent.putExtra("latitude", latitude);
                        intent.putExtra("longitude", longitude);
                        System.out.println("googleで検索activity.equals(1)");
                    } else if (activity.equals("2")) {
                        if (latitude2 != 0) {
                            //たとえgpsがなくともいまコメントしてる 場所(latitude2)があれば、それを使う
                            //GPSを自動で動かしてる時は、equals("1")となるから大丈夫
                            intent.putExtra("Activity", two);
                            //Activityに1を入れると、次から2なのにgpsを使おうとしちゃう
                            intent.putExtra("latitude2", latitude2);
                            intent.putExtra("longitude2", longitude2);
                        } else if (latitude2 == 0) {
                            intent.putExtra("Activity", two);
                            System.out.println("Googleで検索activity.equals(2)");
                        }
                    }
                    String usernamea;
                    if(username == null) {
                        intent.putExtra("usernamea", "null");
                        usernamea = getIntent().getStringExtra("usernamea");
                        Toast toasta = Toast.makeText(LocationActivity.this, usernamea + ":元の" + username, Toast.LENGTH_SHORT);
                        toasta.show();
                    }else {
                        intent.putExtra("usernamea", username);
                        usernamea = getIntent().getStringExtra("username");
                    }

                    startActivity(intent);
//---------------------------------------------else つまり、お気に入り----------------------------------------------------------------//
                } else {
                    System.out.println("else");
                    String sql = "select latitude,longitude from favorite where placename = '" + item + "' " +
                            "and username = '" + username + "';";
                    MyOpenHelper helper = new MyOpenHelper(LocationActivity.this);
                    SQLiteDatabase db = helper.getWritableDatabase();
                    Cursor c = db.rawQuery(sql, null);
                    c.moveToFirst();
                    latitude2 = c.getDouble(0);
                    longitude2 = c.getDouble(1);
                    System.out.println(latitude2 + "z:" + longitude2);
                    but = item;
                    deleteitem = item;
                    deleteitem = (String) spinner.getSelectedItem();
                    //elseの時に、お気に入りの場所をspinnerpalaceに入れてる。
                    //editの時に、""じゃなければ、
                    select(latitude2, longitude2,spinnermath);

                }
            }
            //アイテムが選択されなかった

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinnerItems = favorite.favorite(LocationActivity.this, username);//これでok


        arrayadapter();

        //ここから倍率のspinner
        //---------------- Spinner の選択されているアイテムを設定-------------//
        spinner2 = (Spinner) findViewById(R.id.spinner2);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Spinner spinner2 = (Spinner) parent;
                String item = (String) spinner2.getSelectedItem();
                choiceitem=item;
                //------------------------------------------spinner初回起動------------------------------------------------//
                if (spinner2.isFocusable() == false) {
                    spinner2.setFocusable(true);
                    if (choiceitem.equals("30m")) {
                        //これで初期化現在地だから
                        spinnermath=30.0;
                    }
                    return;
                }
                Double aa=30.0;
                Double bb=60.0;
                Double cc=200.0;
                Double dd=1000.0;
                Double ee=3000.0;
                if(choiceitem.equals("30m")) {
                    ifelse(aa);
                } else if (choiceitem.equals("60m")) {
                    ifelse(bb);
                } else if (choiceitem.equals("200m")) {
                   ifelse(cc);
                } else if (choiceitem.equals("1km")) {
                    ifelse(dd);
                } else if (choiceitem.equals("3km")) {
                   ifelse(ee);
                }
            }

            //selectで取得してるタイミングが遅いってことは、

            public void ifelse(double zz){//押された時のみ、onStart()だから繰り返しにはならない
                spinnermath=zz;//押された時に代入しておく、これは、onStart()でリセットされて、初期値の30.0になる?
                if(latitude2!=0.0){
                    System.out.println("aaaaa"+String.valueOf(spinnermath));
                    select(latitude2,longitude2,spinnermath);
                }else if(latitude2==0.0){
                    if(latitude!=0.0){
                        select(latitude,longitude,spinnermath);
                    }else {
                        Toast toast = Toast.makeText(LocationActivity.this, "GPSをONにするか場所を選んでください", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }else{
                }
            }


            @Override
                public void onNothingSelected (AdapterView < ? > parent){
                    System.out.println("選択されてません");
                }
        });
        ArrayAdapter<String> madapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,spinnerMetter );
        madapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(madapter2);
        spinner2.setFocusable(false);


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
                                select(latitude, longitude,spinnermath);
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
                            select(latitude2, longitude2,spinnermath);
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
                                select(latitude2, longitude2,spinnermath);
                            }
                        } else if (activity.equals("2")) {
                            if (latitude2 != 0 && longitude2 != 0) {
                                String sql2 = "insert into neardb(data,username,comment,latitude,longitude,idnumber)" +
                                        " values ('" + data + "','" + username + "','" + comment + "'," + latitude2 + "," + longitude2 + "," + idnumber + ");";
                                db.execSQL(sql2);
                                select(latitude2, longitude2,spinnermath);
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


    //spinnerItemsを更新した時に呼び出す
    public void arrayadapter() {
        //リスナーの終わり、
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerItems);
        //ここにcreateFromResourceがないことだけ
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        // 初回起動時の対応。
        spinner.setFocusable(false);
    }

    //----------------------------------------------------------onClick--------------------------------------------------------------------//
    String updatedata = null;
    String updatetouchusername = null;
    String updatecomment = null;
    EditText editText;
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d(position + "", id + "(1089)");
        String data = adapterlist.get(position).getData();
        final String updateid = adapterlist.get(position).getIdnumber();
        updatedata = data;
        //データベースからは、idnumberがあるかどうかで判断するから、dataとかはいらない
        updatetouchusername = adapterlist.get(position).getUsername();
        updatecomment = adapterlist.get(position).getComment();
        //-------------------------自分のコメントか他人のコメントかif-----------------------------------//
        String barter = updatetouchusername;//よびにbarterを作っておく
        //">"を含んでる場合のif文
        if (updatetouchusername.indexOf(">") != -1) {//ある
            updatetouchusername = updatetouchusername.substring(0, updatetouchusername.indexOf(">"));
        }
        if (updatetouchusername.equals(username) && (!true == isNum(updatetouchusername))) {
            if (barter.indexOf(">") == 1) {
                updatetouchusername = barter;
            }
            //--------------ここで、修正しますか？を確認のダイアログを出力--------------//
            LayoutInflater inflater = (LayoutInflater) LocationActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
            final View layout = inflater.inflate(R.layout.dialog_edit_comment, (ViewGroup) findViewById(R.id.layout_edit));
            // アラーとダイアログ を生成
            AlertDialog.Builder builder = new AlertDialog.Builder(LocationActivity.this);
            //-----------------------------このalartdialongとこから--------------------------------->
            builder.setView(layout);
            editText = (EditText) layout.findViewById(R.id.editor_text);
            editText.setText(updatecomment);
            editText.setSelection(editText.getText().length());
            //-----------------------------------//select二発---------------------------------//
            //さっき取得した、先頭のupdateidをwhere句で指定
            String sqlselect = "select * from neardb where  idnumber == '" + updateid + "';";
            MyOpenHelper helper = new MyOpenHelper(LocationActivity.this);
            SQLiteDatabase db = helper.getWritableDatabase();
            Cursor c = db.rawQuery(sqlselect, null);
            c.moveToFirst();
            System.out.println(updatedata + "," + updatetouchusername + "," + updatecomment + "," + updateid + "(1149)");
            if (c != null && c.getCount() != 0) {
                System.out.println("getCount!=0(1153)");
                for (int i = 0; i < c.getCount(); i++) {
                    datex = c.getString(0);//data
                    namex = c.getString(1); //name
                    commentx = c.getString(2); //comment
                    latitudex = c.getDouble(3);//latitude?
                    longitudex = c.getDouble(4);//longitude?
                    idnumberx = c.getString(5);//id
                    System.out.println("latitudex:" + latitudex + ",longitudex:" + longitudex + "(1161)");
                    c.moveToNext();
                }
            }
                //---------------------------selectした値が、updatedatabaseにもない場合----------------------------------------------//
                //２つ目のselect//
                //where句で、先ほど取得したidnumberxをセット neardbupdateにて
                String sqlselectupdate = "select * from neardbupdate where neardbid == '" + idnumberx + "';";
                Cursor cc = db.rawQuery(sqlselectupdate, null);
                cc.moveToFirst();
                //updateから取得できたってことは、もうupdateにinsert済みだから、<編集--を削除して、更新ボタンが押されたら、update
                if (cc != null && cc.getCount() != 0) {
                    System.out.println("cc.getCount:" + cc.getCount() + "(1172)");
//-----------更新済みdatabaseにこの情報は登録されてたから、<から右を削除--------------//
                    String lastIndex = updatecomment.substring(0, updatecomment.lastIndexOf("<"));
                    editText = (EditText) layout.findViewById(R.id.editor_text);
                    // EditText にコメントの表示にふさわしくない部分を省略した、テキストを設定
                    editText.setText(lastIndex);
                    System.out.println("lastIndex:" + lastIndex + "(1179)");
                } else if (cc.getCount() == 0) { //つまり、insertする必要  今回は、editTextの事だから、 何もしないでset
                    //２回目の時に、cc.getCount()!=0になるから、そしたら、<編集----->を編集して、setすればいい
                    //updatecommentも同じ
                    editText.setText(updatecomment);
                    //ここが呼ばれてるか重要
                    //--------------------------大きいブロック、updbのupdateと、insert、neardbのupdateをおこなう-------------------------------------//
              }
            editText.setSelection(editText.getText().length());
                //-------------------------------------------------------ここまででselectする----------------------------------------------------//
                // EditText のインプットタイプを設定
                editText.setInputType(InputType.TYPE_CLASS_TEXT);

                //-------------------------------------更新ボタン-----------------------------------------------//
                builder.setPositiveButton("更新", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String getedittext = editText.getText().toString();
                        //---------------------------------select--------------------------//
                        if (getedittext.length() == 0) {
                            Toast toast = Toast.makeText(LocationActivity.this, "入力してください", Toast.LENGTH_SHORT);
                            toast.show();
                            //------------文字がEditTextに入ってるか-----------//
                        } else if (getedittext.length() != 0) {
                            System.out.println(updatedata + "," + updatetouchusername + "," + updatecomment + "," + updateid + "(1204)");
                            String sqlselect = "select * from neardb where  idnumber == '" + updateid + "';";
                            System.out.println("sqlsqlect" + sqlselect + "(1206)");
                            MyOpenHelper helper = new MyOpenHelper(LocationActivity.this);
                            SQLiteDatabase db = helper.getWritableDatabase();
                            Cursor c = db.rawQuery(sqlselect, null);
                            c.moveToFirst();
                            if (c == null) {
                                System.out.println("select neardb null(1212)");
                            } else if (c != null) {
                                for (int i = 0; i < c.getCount(); i++) {
                                    datex = c.getString(0);//data
                                    namex = c.getString(1); //name
                                    commentx = c.getString(2); //comment
                                    latitudex = c.getDouble(3);//latitude?
                                    longitudex = c.getDouble(4);//longitude?
                                    idnumberx = c.getString(5);//idnumber
                                    c.moveToNext();
                                    String datexx = "";
                                    datexx += datex + ":";
                                    System.out.println("謎datexx" + datexx + "(1224)");
                                    System.out.println("datex:" + datex);
                                    System.out.println("commentx:" + commentx + ",latitudex:" + latitudex + ",longitudex:" + longitudex);
                                }
                            }
//--------------------------------------------selectした値が、updatedatabaseにもない場合----------------------------------------------//
                            String sqlselectupdate = "select * from neardbupdate where  neardbid == '" + idnumberx + "';";
                            Cursor cc = db.rawQuery(sqlselectupdate, null);
                            cc.moveToFirst();
                            Calendar calendar = Calendar.getInstance();
                            int month = calendar.get(Calendar.MONTH) + 1;
                            int day = calendar.get(Calendar.DATE);
                            int hour = calendar.get(Calendar.HOUR);
                            int minute = calendar.get(Calendar.MINUTE);
                            datareset2 = "<編集" + month + "/" + day + ":" + hour + ":" + minute + ">";
                            //updateから取得できたってことは、もうupdateにinsert済みだから、<編集--を削除して、更新ボタンが押されたら、update
                            if (cc != null && cc.getCount() != 0) {

                                //updateのneardbidがneardbのidnumberのとこのコメントをupdateしかし、できてない、
                                //だけど、neardbのupdateはできてる
                                System.out.println("cc!=null(1241)");
                                //ここでは、neardbidを使い、incrementのidは、使わない
                                String sqlupdate = "update neardbupdate set comment = '" + getedittext +datareset2 + "' where neardbid == '" + idnumberx + "';";
                                System.out.println("sqlupdateした:" + sqlupdate + "(1249)");
                                cc.close();
                                MyOpenHelper helpers = new MyOpenHelper(LocationActivity.this);
                                SQLiteDatabase dbs = helpers.getWritableDatabase();
                                Cursor update = dbs.rawQuery(sqlupdate, null);
                                update.moveToFirst();
                                System.out.println("ゲットカウントupdate,neardbupdate:" + update.getCount());
                                update.close();
                                db.close();
                                //------------一度もupdateしてないコメントは、ここに来るようになってる-------------//
                            } else if (cc == null || cc.getCount() == 0) {
                                //------------------ここで、insertしてる neardbupdateに---------------------//
                                //getedittextの一番後ろの'を
                                System.out.println("getedittext:"+getedittext);
                                String updateinsert = "insert into neardbupdate (data,username,comment,latitude,longitude,neardbid)" +
                                        " values ('" + datex + "','" + updatetouchusername + "','" + getedittext + datareset2 + "'," + latitudex + "," + longitudex + "," + idnumberx + ");";
                                System.out.println("updateinsert:" + updateinsert + "(1259)");
                                db.execSQL(updateinsert);
                            }
                        }
//-------------------------------------------これは、普通にneardbのupdate--------------------------------------------------------//
                        //idnumberxを、neardbに使うのはいいけど、updateの法に使うのはどう？
                        String sqlupdate = "update neardb set comment = '" + getedittext + datareset2 + "' where  idnumber == '" + idnumberx + "';";
                        MyOpenHelper helper = new MyOpenHelper(LocationActivity.this);
                        SQLiteDatabase db = helper.getWritableDatabase();
                        Cursor update = db.rawQuery(sqlupdate, null);
                        update.moveToFirst();
                        System.out.println("update.getCount()" + update.getCount() + "1274)");
                        update.close();
                        db.close();
                        System.out.println("latitudex:" + latitudex + "longitudex:" + longitudex + "(1280)");
                        //selectしても、listは、更新されてない
                        //   ---------------------そして、select----------------------------------------------------------------//
                        select(latitudex, longitudex,spinnermath);
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
            } else {
                String updatetouchusernamecopy=null;
                //そして、"  "より前を取り出す
                //これは、クラス変数にする　String updatetouchusernamescopy;
                if (updatetouchusername.indexOf(">") != -1) {
                    updatetouchusernamecopy = updatetouchusername.substring(0, updatetouchusername.indexOf(">"));
                } else {
                    //含まない場合は、そのまま使用
                    updatetouchusernamecopy = updatetouchusername;
                }
                if (toyou == null) {
                    toyou = ">" + updatetouchusernamecopy;
                } else if (toyou.indexOf(">") != -1) {//含まれている場合は、すでにusernameは、入っているから、あとは、toutchusernameだけ
                    System.out.println("4");
                   if(toyou==null){
                       System.out.println("toyou==null");
                   }else if(updatetouchusernamecopy==null){
                       System.out.println("updatecopy==null");
                   }
                    if (toyou.indexOf(updatetouchusernamecopy) != -1) {//すでに、この人へが、追加されている場合は、消す
                        toyou = toyou.replaceFirst(updatetouchusernamecopy, ""); //一番最初の頭があったら、""にする
                        System.out.println("すでにあるから、"+updatetouchusernamecopy+"を消す");
                        if (toyou.indexOf(",,") != -1) {//つまり2人以上が選択されているということ
                            toyou = toyou.replace(",,", ",");  //上のやつで、消したら、それを、今度は、,,を防ぐ
                        }
                        if (toyou.equals(">")) {//そして、もう、toyouが">"のみになったら、、toyouをnullにする
                            toyou = null;
                        } else { //つまり一つのusernameを入れて消した時は、>しかないから、charAt(1)はない状態
                            char fo = toyou.charAt(1);
                            //0は、まだ、>を消す前だから、1にする
                            String stringfor = String.valueOf(fo);
                            char la = toyou.charAt(toyou.length() - 1);
                            String stringafter = String.valueOf(la);

                            if (stringfor.equals(",")) {
                                toyou = toyou.replaceFirst(",", "");
                                System.out.println("前方一致:" + toyou);

                            } else if (stringafter.equals(",")) {
                                toyou = toyou.substring(0, toyou.lastIndexOf(","));
                                System.out.println("後方一致:" + toyou);
                            }
                        }
                    } else if (toyou.indexOf(updatetouchusernamecopy) == -1) {//まだ、このユーザーが含まれていなかった場合は、普通に追加
                        toyou += "," + updatetouchusername;
                    }
                }
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
                //usernameに" "と、> と、 , と、数字のみは使えなくする
            }
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


    //----------------------------------------------------LongClick------------------------------------------------------------------------//
    @Override
    public boolean onItemLongClick(AdapterView parent, View view, int position, long id) {
        // return true;これ最後に入れないと、Clickも作動する
        String updateid = adapterlist.get(position).getIdnumber();
        updatetouchusername = adapterlist.get(position).getUsername();
        String barter = updatetouchusername;//よびにbarterを作っておく
        if (updatetouchusername.indexOf(">") != -1) {
            String updatetouchusernamecheck = updatetouchusername.substring(0, updatetouchusername.indexOf(">"));
            updatetouchusername = updatetouchusernamecheck;
        }
        if (updatetouchusername.equals(username) && (!true == isNum(updatetouchusername))) {
            if (barter.indexOf(">") == 1) {
                updatetouchusername = barter;
            }
            LayoutInflater inflater = (LayoutInflater) LocationActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
            final View layout = inflater.inflate(R.layout.dialog_deletecomment_us, (ViewGroup) findViewById(R.id.layout_deleteplace));
// アラーとダイアログ を生成
            AlertDialog.Builder builder = new AlertDialog.Builder(LocationActivity.this);
            builder.setView(layout);
            String sqlselect = "select * from neardb where idnumber == '" + updateid + "';";
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
                    String sqldelete = "delete from neardb where   idnumber == '" + idnumberx + "';";
                    String sqldeleteupdate = "delete from neardbupdate where   neardbid == '" + idnumberx + "';";
                    MyOpenHelper helper = new MyOpenHelper(LocationActivity.this);
                    SQLiteDatabase db = helper.getWritableDatabase();
                    Cursor bb = db.rawQuery(sqldelete, null);
                    Cursor dd = db.rawQuery(sqldeleteupdate, null);
                    bb.moveToFirst();
                    dd.moveToFirst();
                    bb.close();
                    Cursor update = db.rawQuery(sqldelete, null);
                    update.moveToFirst();
                    update.close();
                    db.close();
                    select(latitudex, longitudex,spinnermath);
                }
            });
            builder.create().show();
        }
        return true;
    }

    //--------------------------------------------------------onSwitchClicked()-----------------------------------------------------------//
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

    //----------------------------------------------------------startFusedLocation()----------------------------------------------------//
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
            System.out.println("connected now");
        } else {
            System.out.println("not connected");
        }
        if (currentLocation != null && currentLocation.getTime() > 20000) {
            location = currentLocation;
//現在地を取得してる
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            String activity = getIntent().getStringExtra("Activity");
            if (activity.equals("1")) {
                if (latitude2 == 0) {
                    select(latitude, longitude,spinnermath);
                } else if (latitude2 != 0) {
                    select(latitude2, longitude2,spinnermath);
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


    @Override
    protected void onStop() {
        super.onStop();
        stopFusedLocation();
    }


//------------------------------------------------------------onLocationChanged--------------------------------------------------------//
    //あるサイトによると2秒ごとに呼ばれるらしいからべつにok
    //呼ばれてなくない?再度スタートした時だけじゃん？
    //そん時はてらているのやつを使う

    @Override
    public void onLocationChanged(Location location) {
        ListView listView = (ListView) findViewById(R.id.list_view);
        //こっちのtextViewも後々
        textLog += "onLocationChanged\n";
        textView.setText("");
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


//-------------------------------------------------------select()---------------------------------------------------------------------//

   public void select(double latitude,double longitude,double spinnermath){
       adapterlist = new ArrayList<>();
      select selectclass=new select();
             selectclass.select(latitude,longitude,this,adapterlist,spinnermath);
       if(selectclass.select(latitude,longitude,this,adapterlist,spinnermath)!=null) {
           adapterlist =selectclass.select(latitude,longitude,this,adapterlist,spinnermath);
       }
       listView = (ListView) findViewById(R.id.list_view);
       if(adapterlist!=null) {
           ArrayListAdapter adapter = new ArrayListAdapter(LocationActivity.this, adapterlist);
           adapter.setAdapterList(adapterlist);
           listView.setAdapter(adapter);
          //adapter.notifyDataSetChanged();
          // System.out.println("selectが呼ばれたりして、adapter.notifyDataSetChanged();された");
       }
       listView.setOnItemClickListener(this);
       listView.setOnItemLongClickListener(this);
       //onStart();
   }
}

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