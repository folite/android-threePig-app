package com.example.myapplication;

import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.widget.Toast;

import com.google.android.gms.identity.intents.Address;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
//        String placeName="台中市大里區中興路一段";
//        if(placeName.length()>0){
//            Geocoder gc = new Geocoder(MapsActivity.this);
//            List<android.location.Address> addressList=null;
//            try {
//                addressList=gc.getFromLocationName(placeName, 1);
//            } catch (IOException e) {
//// TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//            if(addressList==null || addressList.isEmpty()){
//                Toast.makeText(getApplicationContext(), "找不到該位置", Toast.LENGTH_SHORT).show();
//            }else{
//                android.location.Address address = addressList.get(0);
//                LatLng position = new LatLng(address.getLatitude(),address.getLongitude());
//                String snippet=address.getAddressLine(0);
//                googleMap.addMarker(new MarkerOptions().position(position).snippet(snippet).title(placeName));
//                CameraPosition cameraPosition =
//                        new CameraPosition.Builder()
//                                .target(position)
//                                .zoom(16)
//                                .build();
//                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                //googleMap.moveCamera(CameraUpdateFactory.newLatLng(position));
                //moveMap(position);
            }
//        }
//        mMap = googleMap;
//
//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(0, 0);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in "));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//    }
}
