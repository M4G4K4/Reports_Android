package com.example.notes;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Maps extends FragmentActivity implements OnMapReadyCallback {

    GoogleMap mMap;


    Integer userID;
    String imgURL;

    String random = "string";

    ArrayList<HashMap<String, String>> location = new ArrayList<>();


    FloatingActionButton fab,fab2;

    SharedPreferences settings;
    Boolean subscribed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        try {
            requestReports();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Get user ID
        Intent intent = getIntent();
        userID = intent.getIntExtra("ID",0);

        fab = findViewById(R.id.fabmap);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addReport();
            }
        });

        fab2 = findViewById(R.id.fabMapNotification);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subscribeTopic();
            }
        });

        verifySubscribeState();

    }



    // Fab icon cliked , open popup
    private void addReport() {
        Intent intent = new Intent(getBaseContext(), popup.class);
        intent.putExtra("UserID", userID);
        startActivity(intent);

        //startActivity(new Intent(Maps.this,popup.class));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(Maps.this));


        googleMap.setOnInfoWindowLongClickListener(new GoogleMap.OnInfoWindowLongClickListener() {
            @Override
            public void onInfoWindowLongClick(Marker marker) {
                String markerTitle = marker.getTitle();
                String [] arrStr = markerTitle.split("#",2);
                if(marker.getAlpha() == 0.99F){
                    editDeleteMarker(arrStr[1],arrStr[0]);
                }
            }
        });



    }

    private void editDeleteMarker(String imgURLID,String description) {
        Intent intent = new Intent(getBaseContext(), popUpEditDelete.class);
        intent.putExtra("imgURLID", imgURLID);
        intent.putExtra("description",description);
        intent.putExtra("userID",userID);
        startActivity(intent);
    }


    // Get all reports , All the markers from the DB
    private void requestReports() throws JSONException {
        String url ="http://64.227.36.62/api/getAllReports";

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try{
                            HashMap<String, String> map = new HashMap<String, String>();
                            // Loop through the array elements
                            for(int i=0;i<response.length();i++){
                                // Get current json object
                                JSONObject data = response.getJSONObject(i);

                                map.put("description", data.getString("description"));
                                map.put("longitude",data.getString("longitude"));
                                map.put("latitude",data.getString("latitude"));
                                map.put("img",data.getString("img"));
                                map.put("morada",data.getString("morada"));
                                map.put("userID",data.getString("userID"));

                                location.add(map);

                                LatLng point;

                                // Markers that are from the logged in user
                                if(data.getInt("userID") == userID){
                                    point = new LatLng( data.getDouble("longitude"), data.getDouble("latitude") );
                                    mMap.addMarker(new MarkerOptions()
                                            .position(point)
                                            .alpha(0.99F)
                                            .title(data.getString("description") + "#" + data.getString("img"))
                                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                                            .snippet(data.getString("morada"))
                                    );

                                }else{
                                    point = new LatLng( data.getDouble("longitude"), data.getDouble("latitude") );
                                    mMap.addMarker(new MarkerOptions()
                                            .position(point)
                                            .alpha(1)
                                            .title(data.getString("description") + "#" + data.getString("img"))
                                            .snippet(data.getString("morada"))
                                    );
                                }

                            }
                            // Position camera around Porto
                            LatLng point = new LatLng(41.14961, -8.61099);
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 7));
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        System.out.println("Response Error");
                    }
                }
        );
        queue.add(jsonArrayRequest);
    }



    private void subscribeTopic(){
        settings = getSharedPreferences("subscribed", 0);
        subscribed = settings.getBoolean("subscribed", false);

        //No Subscribed
        if(subscribed){
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("subscribed", false);
            editor.commit();
            FirebaseMessaging.getInstance().unsubscribeFromTopic("notification2");
            Toast.makeText(this, "Unscribed", Toast.LENGTH_SHORT).show();
            fab2.setImageResource(R.drawable.ic_notifications_off_black_24dp);
        }
        //Is subscription
        else{
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("subscribed", true);
            editor.commit();
            FirebaseMessaging.getInstance().subscribeToTopic("notification2");
            Toast.makeText(this, "Subscribed to notifications", Toast.LENGTH_SHORT).show();
            fab2.setImageResource(R.drawable.ic_notifications_active_black_24dp);
        }
    }

    private void verifySubscribeState(){
        settings = getSharedPreferences("subscribed", 0);
        subscribed = settings.getBoolean("subscribed", false);

        if(subscribed){
            fab2.setImageResource(R.drawable.ic_notifications_active_black_24dp);
        }else{
            fab2.setImageResource(R.drawable.ic_notifications_off_black_24dp);        }
    }


}





