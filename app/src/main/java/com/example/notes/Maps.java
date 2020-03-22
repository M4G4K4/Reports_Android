package com.example.notes;

import androidx.fragment.app.FragmentActivity;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

public class Maps extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    // How to use Glide:
    // Glide.with(context).load("YourUrl").into(imageView);

    Integer userID;
    String imgURL;


    ArrayList<HashMap<String, String>> location = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);




        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Get user ID
        Intent intent = getIntent();
        userID = intent.getIntExtra("ID",0);



        FloatingActionButton fab = findViewById(R.id.fabmap);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addReport();
            }
        });


    }


    // Fab icon cliked , open popup
    private void addReport() {
        Intent intent = new Intent(getBaseContext(), popup.class);
        intent.putExtra("UserID", userID);
        startActivity(intent);

        //startActivity(new Intent(Maps.this,popup.class));
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


/*
        // Add a marker in Sydney and move the camera
        LatLng braga = new LatLng(41.55032, -8.42005);


        mMap.addMarker(new MarkerOptions().position(braga).title("Braga marker"));
*/


        /*
        for (int i= 0; i< location.size() ;i++){
            System.out.println("adadadadadadadadadadadad");
            LatLng points = new LatLng( Double.parseDouble(location.get(i).get("latitude")) , Double.parseDouble(location.get(i).get("longitude")) );
            mMap.addMarker(new MarkerOptions().position(points).title(location.get(i).get("description")));
        }
*/
        System.out.println("Before for Size: " + location.size());
        for (int a =0; a<location.size();a++) {

            HashMap<String, String> tmpData = (HashMap<String, String>) location.get(a);
            Set<String> key = tmpData.keySet();
            Iterator it = key.iterator();
            while (it.hasNext()) {
                String hmKey = (String)it.next();
                String hmData = tmpData.get(hmKey);

                System.out.println("Key: "+hmKey +" & Data: "+hmData);
                it.remove(); // avoids a ConcurrentModificationException
            }

        }

    }


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

                            }
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







}
