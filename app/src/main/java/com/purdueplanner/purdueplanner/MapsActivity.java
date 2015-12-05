package com.purdueplanner.purdueplanner;

import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.firebase.client.Firebase;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.jar.Manifest;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
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
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }
            @Override
            public void onMarkerDrag(Marker marker) {

            }
            @Override
            public void onMarkerDragEnd(Marker marker) {
                LatLng latlng = marker.getPosition();
                double lat = latlng.latitude;
                double lon = latlng.longitude;
                //Add the lat and long to the student in database
                Firebase.setAndroidContext(getApplicationContext());
                Student currentStudent = null;
                if (getApplication() != null) {
                    currentStudent = ((MyApplication) getApplication()).getStudent();
                }
                // Go to the current student in the firebase databse
                Firebase ref = new Firebase("https://purduescheduler.firebaseio.com/Students/" + currentStudent.getId());
                // Go to the current student's home location in the firebase data
                Firebase scheduleRef = ref.child("HomeLocation");
                HashMap<String, Double> homelocation = new HashMap<String, Double>();
                homelocation.put("Latitude", lat);
                homelocation.put("Longitude", lon);
                // Put all of the classes in the database for the students
                scheduleRef.setValue(homelocation);

                currentStudent.setLatitude(lat);
                currentStudent.setLongitude(lon);
            }
        });
        // Set the view to Purdue University and move the camera
        LatLng purdueUni = new LatLng(40.427976, -86.915479);
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

        if (currentStudent.getLatitude() != 0) {
            Marker homeMarker = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(currentStudent.getLatitude()
                            , currentStudent.getLongitude()))
                    .title("Home")
                    .draggable(true)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
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
