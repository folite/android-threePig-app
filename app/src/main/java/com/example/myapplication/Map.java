package com.example.myapplication;

import android.app.ActionBar;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class Map extends AppCompatActivity implements OnMapReadyCallback {
    String phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        setTitleBar();
        Bundle bundle =getIntent().getExtras();
        phone=bundle.getString("phone");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        String placeName="台中市大里區中興路一段";
        if(placeName.length()>0){
            Geocoder gc = new Geocoder(Map.this);
            List<Address> addressList=null;
            try {
                addressList=gc.getFromLocationName(placeName, 1);
            } catch (IOException e) {
// TODO Auto-generated catch block
                e.printStackTrace();
            }
            if(addressList==null || addressList.isEmpty()){
                Toast.makeText(getApplicationContext(), "找不到該位置", Toast.LENGTH_SHORT).show();
            }else{
                android.location.Address address = addressList.get(0);
                LatLng position = new LatLng(address.getLatitude(),address.getLongitude());
                String snippet=address.getAddressLine(0);
                googleMap.addMarker(new MarkerOptions().position(position).snippet(snippet).title(placeName));
                CameraPosition cameraPosition =
                        new CameraPosition.Builder()
                                .target(position)
                                .zoom(16)
                                .build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                //googleMap.moveCamera(CameraUpdateFactory.newLatLng(position));
                //moveMap(position);
            }
        }
//        mMap = googleMap;
//
//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(0, 0);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in "));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
    private void setTitleBar(){
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
    }
    public void button_ShoppingPage_click(View v) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("phone", phone);
        intent.putExtras(bundle);
        intent.setClass(this, shoppingCart.class);
        startActivityForResult(intent, 0);
    }
    public void button_setPage_click(View v){
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("phone", phone);
        intent.putExtras(bundle);
        intent.setClass(this, SetPage.class);//login_main to register_page
        startActivityForResult(intent, 0);
    }
    public void button_date_click(View v){
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("phone", phone);
        intent.putExtras(bundle);
        intent.setClass(this, resetdatePage.class);
        startActivityForResult(intent, 0);
    }
    public void listpage(View v){
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("phone", phone);
        intent.putExtras(bundle);
        intent.setClass(this, CollectMenuPage.class);
        startActivityForResult(intent, 0);
    }
    public void couponListPage(View v){
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("phone", phone);
        intent.putExtras(bundle);
        intent.setClass(this, couponList.class);
        startActivityForResult(intent, 0);
    }
    public void button_finishOrderList (View v){
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("phone", phone);
        intent.putExtras(bundle);
        intent.setClass(this, finishOrderList.class);
        startActivityForResult(intent, 0);
    }
    public void toHome(View v){
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("phone", phone);
        intent.putExtras(bundle);
        intent.setClass(this, mintPage.class);
        startActivityForResult(intent, 0);
    }
}

