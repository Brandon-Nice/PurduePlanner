package com.purdueplanner.purdueplanner;

import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
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

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

                LinearLayout info = new LinearLayout(MapsActivity.this);
                info.setOrientation(LinearLayout.VERTICAL);


                TextView snippet = new TextView(MapsActivity.this);
                snippet.setTextColor(Color.BLACK);
                snippet.setText(marker.getSnippet());

                //info.addView(title);
                info.addView(snippet);

                return info;
            }
        });



        Date date = new Date();
        String currDay = (String) android.text.format.DateFormat.format("EEEE", date);

        String dayLetter = getLetter(currDay);

        final Student currentStudent = ((MyApplication) getApplication()).getStudent();
        ArrayList<Classes> currentStudentClasses = new ArrayList<Classes>();
        currentStudentClasses = currentStudent.getSchedule();

        ArrayList<HashMap<String, Object>> markers = new ArrayList();
        for (Classes specClass: currentStudentClasses)
        {
            if(specClass.getDays().contains(dayLetter)) {
                if (specClass.getLatitude() != null) {
                    boolean alreadyThere = false;
                    int whereAt = 0;
                    for (int i = 0; i < markers.size(); i++) {
                        if (specClass.getLatitude().equals(markers.get(i).get("Latitude")) &&
                                specClass.getLongitude().equals(markers.get(i).get("Longitude"))) {
                            alreadyThere = true;
                            whereAt = i;
                            break;
                        }
                    }
                    if (alreadyThere) {
                        ArrayList<HashMap<String, String>> classes = (ArrayList) markers.get(whereAt).get("Classes");
                        HashMap<String, String> currentClass = new HashMap();
                        currentClass.put("Class", "Class: " + specClass.getMajor() + " "
                                + specClass.getCourseNum() + "\n" + specClass.getLocation());
                        currentClass.put("StartTime", specClass.getStartTime());
                        classes.add(currentClass);
                        markers.get(whereAt).put("Classes", classes);
                    } else {
                        HashMap<String, Object> marker = new HashMap();
                        marker.put("Latitude", specClass.getLatitude());
                        marker.put("Longitude", specClass.getLongitude());
                        ArrayList<HashMap<String, String>> classes = new ArrayList();
                        HashMap<String, String> currentClass = new HashMap();
                        currentClass.put("Class", "Class: " + specClass.getMajor() + " "
                                + specClass.getCourseNum() + "\n" + specClass.getLocation());
                        currentClass.put("StartTime", specClass.getStartTime());
                        classes.add(currentClass);
                        marker.put("Classes", classes);
                        markers.add(marker);
                    }
                }
            }
        }

        for (int i = 0; i < markers.size(); i++)
        {
            HashMap<String, Object> currentMarker = markers.get(i);
            ArrayList<HashMap<String, String>> classes = (ArrayList) currentMarker.get("Classes");
            sortMarkers(classes);
            System.out.println(classes);
            String currentSnippet = "";
            int j = 0;
            while (j < classes.size() - 1)
            {
                currentSnippet += classes.get(j).get("Class") + "\n";
                j++;
            }
            currentSnippet += classes.get(j).get("Class");
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(Double.parseDouble((String) currentMarker.get("Latitude"))
                            , Double.parseDouble((String) currentMarker.get("Longitude"))))
                    .title("")
                    .snippet(currentSnippet));
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

    private void sortMarkers(ArrayList<HashMap<String, String>> markers)
    {
        Comparator<HashMap<String, String>> byTime = new Comparator<HashMap<String, String>>() {
            public int compare(HashMap<String, String> h1, HashMap<String, String> h2) {

                int firstColon = h1.get("StartTime").indexOf(":");
                int firstSpace = h1.get("StartTime").indexOf(" ");
                int firstStartTime = Integer.parseInt(h1.get("StartTime").substring(0, firstColon) +
                        h1.get("StartTime").substring(firstColon + 1, firstSpace));
                if (h1.get("StartTime").contains("pm") && firstStartTime < 1200)
                {
                    firstStartTime += 1200;
                }
                int secondColon = h2.get("StartTime").indexOf(":");
                int secondSpace = h2.get("StartTime").indexOf(" ");
                int secondStartTime = Integer.parseInt(h2.get("StartTime").substring(0, secondColon) +
                        h2.get("StartTime").substring(secondColon + 1, secondSpace));
                if (h2.get("StartTime").contains("pm") && secondStartTime < 1200)
                {
                    secondStartTime += 1200;
                }
                return (firstStartTime - secondStartTime);
            }
        };
        Collections.sort(markers, byTime);
    }

}
