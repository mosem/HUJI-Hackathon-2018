package com.omri.dev.firgunappclient;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
//import android.support.design.widget.ImageView.ImageButton;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.omri.dev.firgunappclient.RestClient.FirgunRestClientUsage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static com.omri.dev.firgunappclient.R.id.map;

public class MainMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 666;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationClient;
    private boolean buttons_revealed = false;
//    private Location lastUserLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                createNewFirgunDialog();
                //Toast.makeText(getApplicationContext(), "Send new firgun", Toast.LENGTH_SHORT).show();
                FloatingActionButton fab1 = (FloatingActionButton) findViewById(R.id.fab1);
                FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.fab2);
                FloatingActionButton fab3 = (FloatingActionButton) findViewById(R.id.fab3);
                if (buttons_revealed) {
                    Animation hide_fab_1 = AnimationUtils.loadAnimation(getApplication(), R.anim.fab1_hide);
                    Animation hide_fab_2 = AnimationUtils.loadAnimation(getApplication(), R.anim.fab2_hide);
                    Animation hide_fab_3 = AnimationUtils.loadAnimation(getApplication(), R.anim.fab3_hide);
                    hideFAB(fab1, hide_fab_1,  (int) (fab1.getHeight() * 1.7));
                    hideFAB(fab2, hide_fab_2,  (int) (fab2.getHeight() * 1.7*2));
                    hideFAB(fab3, hide_fab_3,  (int) (fab3.getHeight() * 1.7*3));
                }
                else {
                    Animation fab1_anim = AnimationUtils.loadAnimation(getApplication(), R.anim.fab1_show);
                    Animation fab2_anim = AnimationUtils.loadAnimation(getApplication(), R.anim.fab2_show);
                    Animation fab3_anim = AnimationUtils.loadAnimation(getApplication(), R.anim.fab3_show);
                    displayFAB(fab1, fab1_anim, (int) (fab1.getHeight() * 1.7));
                    displayFAB(fab2, fab2_anim, (int) (fab2.getHeight() * 1.7*2));
                    displayFAB(fab3, fab3_anim, (int) (fab3.getHeight() * 1.7*3));
                }
                buttons_revealed = !buttons_revealed;
            }
        });

        FloatingActionButton fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        FloatingActionButton fab3 = (FloatingActionButton) findViewById(R.id.fab3);
        fab1.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View view) {
                createNewFirgunDialog();
                                   }
                               });

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewFirgunDialog();
            }
        });

        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewFirgunDialog();
            }
        });
//
//        ImageButton btn=(ImageButton)findViewById(R.id.widget45);
//        btn.setOnClickListener(new OnClickListener() {
//
//            public void onClick(View v) {
//                Intent inf=new Intent(TestbuttontestActivity.this,Activityfullscreen.class);
//
//                startActivity(inf);
//            }
//        });


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    private void displayFAB(FloatingActionButton fab, Animation fab_anim, int botMarginH) {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) fab.getLayoutParams();
        layoutParams.bottomMargin += botMarginH;
        fab.setLayoutParams(layoutParams);
        fab.startAnimation(fab_anim);
        fab.setClickable(true);
    }

    private void hideFAB(FloatingActionButton fab, Animation fab_anim, int botMarginH) {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) fab.getLayoutParams();
        layoutParams.bottomMargin -= botMarginH;
        fab.setLayoutParams(layoutParams);
        fab.startAnimation(fab_anim);
        fab.setClickable(false);
    }

    @SuppressWarnings({"MissingPermission"})
    private void createNewFirgunDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Firgun description");

        // Set up the input
        final EditText input = new EditText(this);

        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("Send a Firgun!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                mFusedLocationClient.getLastLocation()
                        .addOnSuccessListener(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                // Got last known location. In some rare situations this can be null.
                                if (location == null) {
                                    Toast.makeText(getApplicationContext(),
                                            "Waiting for location",
                                            Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    String userFirgunText = input.getText().toString();

                                    JSONObject jsonObj = new JSONObject();

                                    try {
                                        final Location lastLoc = location;
                                        RequestParams params = new RequestParams();
                                        params.put("category", "eco");
                                        params.put("longitude", location.getLongitude());
                                        params.put("latitude", location.getLatitude());
                                        params.put("description", userFirgunText);
                                        //TODO: send json object server
                                        FirgunRestClientUsage.postFirgun(params, new JsonHttpResponseHandler() {
                                            @Override
                                            public void onSuccess(int statusCode, Header[] headers, JSONObject response){
                                                try {
                                                    Toast.makeText(getApplicationContext(),
                                                            "New Firgun was sent: " +
                                                                    "\nLocation: " + lastLoc.getLatitude() + "," +
                                                                    lastLoc.getLongitude(),
                                                            Toast.LENGTH_SHORT).show();

                                                }
                                                catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                    }

                                    catch(JSONException ex) {
                                        ex.printStackTrace();
                                    }
                                    clearFirguns();
                                    loadFirguns();
                                }
                            }
                        });
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public void scheduleRefreshFirguns() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                clearFirguns();
                loadFirguns();
                handler.postDelayed(this, 20 * 1000);
            }
        }, 20*1000);
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

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
        } else {
            enableLocationFunctions();
        }

        loadFirguns();
        scheduleRefreshFirguns();

//        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
//            @Override
//            public void onMapLongClick(LatLng latLng) {
//                mMap.addMarker(new MarkerOptions()
//                        .position(latLng)
//                        .title("Firgun location")
//                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
//                /* Intent i = new Intent();
//                String locationString = latLng.latitude + "," + latLng.longitude;
//                i.setData(Uri.parse(locationString));
//                setResult(RESULT_OK, i);
//                finish(); */
//            }
//        });
    }

    @SuppressWarnings({"MissingPermission"})
    private void enableLocationFunctions() {
        mMap.setMyLocationEnabled(true);
        zoomToCurrentLocation();
    }

    private void clearFirguns() {
        try {
            mMap.clear();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadFirguns() {
        try {
            FirgunRestClientUsage.getAllFirguns(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response){
                    try {
                        JSONArray result = (JSONArray)response.get("_items");
                        for (int i = 0; i < result.length(); i++) {
                            JSONObject currObject = (JSONObject) result.get(i);
                            String description = currObject.getString("description");
                            double latitude = currObject.getDouble("latitude");
                            double longitude = currObject.getDouble("longitude");
                            String category = currObject.getString("category");
                            if(category.equals("eco")){
                                mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(latitude, longitude))
                                .title(description)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.green_firgun)));
                            }
                            else if(category.equals("people")){
                                mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(latitude, longitude))
                                .title(description)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.red_firgun)));
                            }
                            else{
                                mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(latitude, longitude))
                                .title(description)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.purple_firgun)));
                            }

                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
                    JSONArray o = timeline;
//                // Pull out the first event on the public timeline
//                JSONObject firstEvent = timeline.get(0);
//                String tweetText = firstEvent.getString("text");
//
//                // Do something with the response
//                System.out.println(tweetText);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings({"MissingPermission"})
    private void zoomToCurrentLocation() {
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Save the user last location for further user
//                            lastUserLocation = location;

                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 13));

                            CameraPosition cameraPosition = new CameraPosition.Builder()
                                    .target(new LatLng(location.getLatitude(), location.getLongitude()))
                                    .zoom(18)
                                    .build();
                            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                        }
                    }
                });
    }
}
