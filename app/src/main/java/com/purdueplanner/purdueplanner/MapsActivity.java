package com.purdueplanner.purdueplanner;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Date;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Tried setting a on long click listener, but gets null pointer exception
//        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
//            @Override
//            public void onMapLongClick(LatLng point) {
//                mMap.addMarker(new MarkerOptions()
//                        .position(point)
//                        .title("Home")
//                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
//            }
//        });

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
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.setMyLocationEnabled(true);

        // Add a marker at Purdue University and move the camera
        LatLng purdueUni = new LatLng(40.427976, -86.915479);
        //mMap.addMarker(new MarkerOptions().position(purdueUni).title("Marker at Purdue"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(purdueUni, 15));

        Date date = new Date();
        String currDay = (String) android.text.format.DateFormat.format("EEEE", date);
        System.out.println(currDay);

        String dayLetter = getLetter(currDay);
        System.out.println(dayLetter);

        final Student currentStudent = ((MyApplication) getApplication()).getStudent();
        ArrayList<Classes> currentStudentClasses = new ArrayList<Classes>();
        currentStudentClasses = currentStudent.getSchedule();

        //iterates through every class and de termines the day
        for(Classes specClass : currentStudentClasses) {
            //does the class meet on the current day
            if(specClass.getDays().contains(dayLetter)) {
                //add a marker and building name if lat & long is not null
                if (!specClass.getLatitude().equals("Null") && !specClass.getLongitude().equals("Null")) {
                    mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(Double.parseDouble(specClass.getLatitude())
                            , Double.parseDouble(specClass.getLongitude())))
                            .title(specClass.getLocation())
                            .snippet("Class: " + specClass.getMajor() + " " + specClass.getCourseNum()));
                }

            }
        }
    }

    //getLetter code "requisitioned" from menane
    public String getLetter(String day){
        String letter = "";
        switch(day){
            case "Monday": letter = "M";
                break;
            case "Tuesday": letter = "T";
                break;
            case "Wednesday": letter = "W";
                break;
            case "Thursday": letter = "R";
                break;
            case "Friday": letter = "F";
                break;
            case "Saturday": letter = "S";
                break;
            case "Sunday": letter = "U";
                break;
            //Supposedly there is no classes offered on Sunday's at Purdue, so I won't account for it
        }
        return letter;
    }

}
