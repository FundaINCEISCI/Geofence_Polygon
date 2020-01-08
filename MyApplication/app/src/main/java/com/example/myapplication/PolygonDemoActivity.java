package com.example.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This shows how to draw polygons on a map.
 */
public class PolygonDemoActivity extends AppCompatActivity
        implements  OnItemSelectedListener, OnMapReadyCallback, View.OnTouchListener, GoogleMap.OnCameraMoveListener {

    private static final LatLng CENTER = new LatLng(-20, 130);
    private static final int MAX_WIDTH_PX = 100;
    private static final int MAX_HUE_DEGREES = 360;
    private static final int MAX_ALPHA = 255;

    private static final int PATTERN_DASH_LENGTH_PX = 50;
    private static final int PATTERN_GAP_LENGTH_PX = 10;
    private static final Dot DOT = new Dot();
    private static final Dash DASH = new Dash(PATTERN_DASH_LENGTH_PX);
    private static final Gap GAP = new Gap(PATTERN_GAP_LENGTH_PX);
    private static final List<PatternItem> PATTERN_DOTTED = Arrays.asList(DOT, GAP);
    private static final List<PatternItem> PATTERN_DASHED = Arrays.asList(DASH, GAP);
    private static final List<PatternItem> PATTERN_MIXED = Arrays.asList(DOT, GAP, DOT, DASH, GAP);


    private CheckBox mClickabilityCheckbox;
    public static ArrayList<LatLng> points;
    public boolean is_draw_polygon_button_pressed = false;
    public boolean is_center_camera = false;
    public boolean is_clear = false;
    public boolean is_geoin=false;
    public Location myLocation;


    // These are the options for polygon stroke joints and patterns. We use their
    // string resource IDs as identifiers.

    private static final int[] JOINT_TYPE_NAME_RESOURCE_IDS = {
            R.string.joint_type_default, // Default
            R.string.joint_type_bevel,
            R.string.joint_type_round,
    };

    private static final int[] PATTERN_TYPE_NAME_RESOURCE_IDS = {
            R.string.pattern_solid, // Default
            R.string.pattern_dashed,
            R.string.pattern_dotted,
            R.string.pattern_mixed,
    };


  //  private GoogleMap map;

    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.polygon_demo);

        final Button button_center_camera = findViewById(R.id.button_center_camera);
        button_center_camera.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                is_center_camera = true;


            }
        });

        final Button button_draw_polygon = findViewById(R.id.button_draw_polygon);
        button_draw_polygon.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                is_draw_polygon_button_pressed = true;


                // Code here executes on main thread after user presses button
            }
        });

        final Button button_delete_polygon = findViewById(R.id.button_delete_polygon);
        button_delete_polygon.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                is_clear = true;
                // Code here executes on main thread after user presses button
            }
        });


        final Button button_geoin = findViewById(R.id.button_geoin);
        button_geoin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                is_geoin=true;
                // Code here executes on main thread after user presses button
            }
        });
        points = new ArrayList<LatLng>();


        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    private String[] getResourceStrings(int[] resourceIds) {
        String[] strings = new String[resourceIds.length];
        for (int i = 0; i < resourceIds.length; i++) {
            strings[i] = getString(resourceIds[i]);
        }
        return strings;
    }


    public void onMapReady(final GoogleMap map) {


        map.setOnCameraMoveListener(this);


        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {



            @Override
            public void onMapClick(LatLng point) {

                if(is_geoin){
                    // Instantiating the class MarkerOptions to plot marker on the map
                    MarkerOptions markerOptions = new MarkerOptions();

                    // Setting latitude and longitude of the marker position
                    markerOptions.position(point);

                    // Setting titile of the infowindow of the marker
                    markerOptions.title("Position");

                    // Setting the content of the infowindow of the marker
                    markerOptions.snippet("Latitude:" + point.latitude + "," + "Longitude:" + point.longitude);

                    // Instantiating the class PolylineOptions to plot polyline in the map
                    PolylineOptions polylineOptions = new PolylineOptions();

                    // Setting the color of the polyline
                    polylineOptions.color(Color.RED);

                    // Setting the width of the polyline
                    polylineOptions.width(3);

                    // Adding the taped point to the ArrayList
                    points.add(point);

                    // Setting points of polyline
                    // polylineOptions.addAll(points);


                    // Adding the polyline to the map
                    map.addPolyline(polylineOptions);

                    // Adding the marker to the map
                    map.addMarker(markerOptions);

                    Polygon polygon = map.addPolygon(new PolygonOptions()
                            .addAll(points)
                            .strokeColor(Color.BLUE)
                            .fillColor(Color.argb(20, 0, 255, 0)));

                    if (points.size() == 4)
                            is_geoin = false;
                }

                if (is_draw_polygon_button_pressed ) {
                    // Instantiating the class MarkerOptions to plot marker on the map
                    MarkerOptions markerOptions = new MarkerOptions();

                    // Setting latitude and longitude of the marker position
                    markerOptions.position(point);

                    // Setting titile of the infowindow of the marker
                    markerOptions.title("Position");

                    // Setting the content of the infowindow of the marker
                    markerOptions.snippet("Latitude:" + point.latitude + "," + "Longitude:" + point.longitude);

                    // Instantiating the class PolylineOptions to plot polyline in the map
                    PolylineOptions polylineOptions = new PolylineOptions();

                    // Setting the color of the polyline
                    polylineOptions.color(Color.RED);

                    // Setting the width of the polyline
                    polylineOptions.width(3);

                    // Adding the taped point to the ArrayList
                    points.add(point);

                    // Setting points of polyline
                    // polylineOptions.addAll(points);


                    // Adding the polyline to the map
                    map.addPolyline(polylineOptions);

                    // Adding the marker to the map
                    map.addMarker(markerOptions);

                    Polygon polygon = map.addPolygon(new PolygonOptions()
                            .addAll(points)
                            .strokeColor(Color.BLUE)
                            .fillColor(Color.argb(20, 0, 255, 0)));

                    if (points.size() == 4)
                        is_draw_polygon_button_pressed = false;



                }



                if (is_center_camera) {


                    if (ActivityCompat.checkSelfPermission(PolygonDemoActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(PolygonDemoActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(PolygonDemoActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                    }else{
                        if(!map.isMyLocationEnabled())
                            map.setMyLocationEnabled(true);

                        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
                         myLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                        if (myLocation == null) {
                            Criteria criteria = new Criteria();
                            criteria.setAccuracy(Criteria.ACCURACY_COARSE);
                            String provider = lm.getBestProvider(criteria, true);
                            myLocation = lm.getLastKnownLocation(provider);
                        }

                        if(myLocation!=null){
                            LatLng userLocation = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
                            map.addMarker(new MarkerOptions().position(userLocation).title("Burdayım"));
                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 14), 1500, null);
                        }


                    }

                    is_center_camera = false;
                }
                if(is_clear){
                    map.clear();
                    points.clear();
                    is_clear = false;
                }



                }



        });










        // Override the default content description on the view, for accessibility mode.
        map.setContentDescription(getString(R.string.polygon_demo_description));


        // Move the map so that it is centered on the mutable polygon.
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(CENTER, 4));

        // Add a listener for polygon clicks that changes the clicked polygon's stroke color.
        map.setOnPolygonClickListener(new GoogleMap.OnPolygonClickListener() {
            @Override
            public void onPolygonClick(Polygon polygon) {
                // Flip the red, green and blue components of the polygon's stroke color.
                polygon.setStrokeColor(polygon.getStrokeColor() ^ 0x00ffffff);
            }
        });
    }

    /**
     * Creates a List of LatLngs that form a rectangle with the given dimensions.
     */
    private List<LatLng> createRectangle(LatLng center, double halfWidth, double halfHeight) {
        return Arrays.asList(new LatLng(center.latitude - halfHeight, center.longitude - halfWidth),
                new LatLng(center.latitude - halfHeight, center.longitude + halfWidth),
                new LatLng(center.latitude + halfHeight, center.longitude + halfWidth),
                new LatLng(center.latitude + halfHeight, center.longitude - halfWidth),
                new LatLng(center.latitude - halfHeight, center.longitude - halfWidth));
    }

    private int getSelectedJointType(int pos) {
        switch (JOINT_TYPE_NAME_RESOURCE_IDS[pos]) {
            case R.string.joint_type_bevel:
                return JointType.BEVEL;
            case R.string.joint_type_round:
                return JointType.ROUND;
            case R.string.joint_type_default:
                return JointType.DEFAULT;
        }
        return 0;
    }

    private List<PatternItem> getSelectedPattern(int pos) {
        switch (PATTERN_TYPE_NAME_RESOURCE_IDS[pos]) {
            case R.string.pattern_solid:
                return null;
            case R.string.pattern_dotted:
                return PATTERN_DOTTED;
            case R.string.pattern_dashed:
                return PATTERN_DASHED;
            case R.string.pattern_mixed:
                return PATTERN_MIXED;
            default:
                return null;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
       /* switch (parent.getId()) {
            case R.id.strokeJointTypeSpinner:
                mMutablePolygon.setStrokeJointType(getSelectedJointType(pos));
                break;
            case R.id.strokePatternSpinner:
                mMutablePolygon.setStrokePattern(getSelectedPattern(pos));
                break;
        }*/
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Don't do anything here.
    }





    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();

        if(event.getAction() == MotionEvent.ACTION_UP){


            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:

                    //Log.i("TAG", "touched down");
                    break;
                case MotionEvent.ACTION_MOVE:
                    //Log.i("TAG", "moving: (" + x + ", " + y + ")");
                    break;
                case MotionEvent.ACTION_UP:
                    //Log.i("TAG", "touched up");
                    break;
            }

        }
        return false;
    }





    boolean result = false;
    @Override
    public void onCameraMove() {

        int i;
        int j;



        for (i = 0, j = points.size() - 1; i < points.size(); j = i++) {

    if(is_draw_polygon_button_pressed){
    if ((((points.get(i).longitude > myLocation.getLongitude() != (points.get(j).longitude > myLocation.getLongitude()) &&
            (myLocation.getLatitude() < (points.get(j).latitude - points.get(i).latitude) * (myLocation.getLongitude() - points.get(i).longitude) / (points.get(j).longitude-points.get(i).longitude) + points.get(i).latitude))))== false){
        {

            String number = "XXXXXXX";  // The number on which you want to send SMS
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", number, null)));
        }


    }
    }
    if(is_geoin){
        if (!(((points.get(i).longitude > myLocation.getLongitude() != (points.get(j).longitude > myLocation.getLongitude()) &&
                (myLocation.getLatitude() < (points.get(j).latitude - points.get(i).latitude) * (myLocation.getLongitude() - points.get(i).longitude) / (points.get(j).longitude-points.get(i).longitude) + points.get(i).latitude))))== false){
            {

                String number = "05542596353";  // The number on which you want to send SMS
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", number, null)));
            }


        }
    }




        }


    }
}
