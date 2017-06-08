package com.example.android.sample.new3;

import android.content.Intent;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback,GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;
    Double latitude2=0.0;
    Double longitude2=0.0;
    Double latitude=0.0;
    Double longitude=0.0;
    Globals globals;


   // getDouble(String key) getDouble(String key, double defaultValue)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    //----------------------------------------------------------onMapReady---------------------------------------------------------------//
    @Override
    public void onMapReady(GoogleMap googleMap) {
        System.out.println("onMapReady");
        mMap = googleMap;
        //まず初期値として、今いる場所を指定しないと行けないから,latitudeをintentにsetする
        latitude = getIntent().getDoubleExtra("latitude",latitude);
        longitude= getIntent().getDoubleExtra("longitude",longitude);
        latitude2 = getIntent().getDoubleExtra("latitude2",latitude2);
        longitude2= getIntent().getDoubleExtra("longitude2",longitude2);

        // Intent intent = new Intent(getApplication(), MapActivity.class);
        String activity = getIntent().getStringExtra("Activity");
            if (activity.equals("1")){
            LatLng now = new LatLng(latitude, longitude);
            mMap.addMarker(new MarkerOptions().position(now).title("You are here"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(now));
            CameraUpdate cUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15);
            mMap.moveCamera(cUpdate);
        } else if (activity.equals("2")) {//場所も選択しておらず、GPSも使えない時
            if(latitude2!=0.0){
                LatLng now = new LatLng(latitude2, longitude2);
                mMap.addMarker(new MarkerOptions().position(now).title("You are here"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(now));
                CameraUpdate cUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(latitude2, longitude2), 15);
                mMap.moveCamera(cUpdate);
            }else if(latitude2==0.0) {
                LatLng shibuya = new LatLng(35, 139);
                mMap.addMarker(new MarkerOptions().position(shibuya).title("Marker in Shibuya"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(shibuya));
                CameraUpdate cUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(35, 139), 12);
                mMap.moveCamera(cUpdate);
            }
        }
        // 各種コールバック
        googleMap.setOnMapLongClickListener(this);
    }

    //移動すればGPSを変更して表示したい。
    //ボタンは、現在地を示すボタンが欲しい


    //--------------------------------------------------------onMapLongClick()-----------------------------------------------------------//
    @Override
    public void onMapLongClick(LatLng latLng) {
        mMap.addMarker(new MarkerOptions().title("LongClick").position(latLng).draggable(false));
        Log.i("MapTest", "LongClick:" + latLng.latitude + "," + latLng.longitude);
        ///////////長押し検知されたら、
        latitude2 = latLng.latitude;
        longitude2 = latLng.longitude;

        System.out.println("MapActivity:"+latitude2+":"+longitude2);

        Intent intent = new Intent(getApplication(), LocationActivity.class);
        //ここでActivityの指定だ、それがないなら、やっぱり、まま送るやつをそのまま送る
        String activity = getIntent().getStringExtra("Activity");
        if (activity.equals("1")) {
            String a="1";
        intent.putExtra("Activity",a);
        }else if(activity.equals("2")){
            String b="2";
            intent.putExtra("Activity",b);
        }
        intent.putExtra("latitude2",latitude2);
        intent.putExtra("longitude2",longitude2);
        globals =(Globals)this.getApplication();
        String ok="ok";
        globals.setOk(ok);
        startActivity(intent);

    }


}

