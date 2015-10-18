package com.purdueplanner.purdueplanner;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

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
        mMap = googleMap;

        // Add a marker at Purdue University and move the camera
        LatLng purdueUni = new LatLng(40.427976, -86.915479);
        mMap.addMarker(new MarkerOptions().position(purdueUni).title("Marker at Purdue"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(purdueUni, 16));

        // Adds markers at important buildings
        LatLng Lawson = new LatLng(40.427735, -86.917002);
        LatLng Elliot = new LatLng(40.427931, -86.914915);
        LatLng Forney = new LatLng(40.429507, -86.913933);
        LatLng Physics = new LatLng(40.430079, -86.913262);
        LatLng Armstrong = new LatLng(40.430961, -86.914684);
        LatLng Stewart = new LatLng(40.425045, -86.912638);
        LatLng Hicks = new LatLng(40.424514, -86.912686);
        LatLng Math = new LatLng(40.426137, -86.915768);
        LatLng Recitation = new LatLng(40.425834, -86.915202);
        LatLng StanleyCoulter = new LatLng(40.426417, -86.914431);
        LatLng MSEE = new LatLng(40.429353, -86.912628);
        LatLng ElectricalEngin = new LatLng(40.428574, -86.911958);

        mMap.addMarker(new MarkerOptions().position(Lawson).title("Lawson"));
        mMap.addMarker(new MarkerOptions().position(Elliot));
        mMap.addMarker(new MarkerOptions().position(Forney));
        mMap.addMarker(new MarkerOptions().position(Physics));
        mMap.addMarker(new MarkerOptions().position(Armstrong));
        mMap.addMarker(new MarkerOptions().position(Stewart));
        mMap.addMarker(new MarkerOptions().position(Hicks));
        mMap.addMarker(new MarkerOptions().position(Math));
        mMap.addMarker(new MarkerOptions().position(Recitation));
        mMap.addMarker(new MarkerOptions().position(StanleyCoulter));
        mMap.addMarker(new MarkerOptions().position(MSEE));
        mMap.addMarker(new MarkerOptions().position(ElectricalEngin));

    }
}
