package com.anas.googlemaps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.anas.googlemaps.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.model.PolygonOptions;

import java.io.IOException;
import java.util.ArrayList;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng jaspur = new LatLng(29.27960452973069, 78.82456536159232);
        mMap.addMarker(new MarkerOptions().position(jaspur).title("Jaspur"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(jaspur));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(jaspur,16f));

        //Circle Overlays
        mMap.addCircle(new CircleOptions()
                .center(jaspur)
                .radius(1000) //1km
                .fillColor(R.color.teal_200)
                .strokeColor(R.color.purple_200));

        //polygon
        mMap.addPolygon(new PolygonOptions()  //china
                .add(new LatLng(29.55428247029191, 106.54872598609364),
                        new LatLng(32.924776946615836, 89.26668289093709),
                        new LatLng(40.96470207885011, 91.47193699787405),
                        new LatLng(39.540605162622384, 109.12925640428475))
                .fillColor(R.color.teal_200)
                .strokeColor(R.color.teal_700));

        //Ground - streets not visible but places are
        mMap.addGroundOverlay(new GroundOverlayOptions()
                .position(new LatLng(28.46966808971759, 77.03858666466564),100000f,100000f) //New Delhi
                .image(BitmapDescriptorFactory.fromResource(R.drawable.iphone))
                .clickable(true));

        //setting map type
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);


        // for setting click on map i.e. here adding marker on click
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {

                mMap.addMarker(new MarkerOptions().position(latLng).title("clicked here"));

                //for getting address line
                Geocoder geocoder=new Geocoder(MapsActivity.this);
                ArrayList<Address> arrAddresses=new ArrayList<>();
                try {
                    arrAddresses = (ArrayList<Address>) geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                    Log.d("Address : ",arrAddresses.get(0).getAddressLine(0));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.map_type_items,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.mtypeNormal:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                return true;
            case R.id.mtypeHybid:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                return true;
            case R.id.mtypeSatellite:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                return true;
            case R.id.mtypeTerrain:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                return true;
            case R.id.mtypeNone:
                mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}