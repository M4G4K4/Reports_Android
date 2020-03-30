package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Observer;

public class popUpEditDelete extends AppCompatActivity {

    // Variables coming from itent
    String imgURLID;
    String desc;
    Integer userID;

    EditText description;
    Button delete,edit,close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up_edit_delete);



        // Create Pop up
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width), (int) (height));


        // Get userID tha is logged in
        Intent intent = getIntent();
        imgURLID = intent.getStringExtra("imgURLID");
        desc = intent.getStringExtra("description");
        userID = intent.getIntExtra("userID",0);


        delete = findViewById(R.id.popupEditDeleteBtnDelete);
        close = findViewById(R.id.popupEditDeleteBtnClose);
        edit = findViewById(R.id.popupEditDeleteBtnEdit);
        description = findViewById(R.id.popupEditDeleteDescription);


        description.setText(desc);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    deleteReport();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    editReport();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    private void deleteReport() throws JSONException {
        String url ="http://64.227.36.62/api/deleteReport";

        RequestQueue queue = Volley.newRequestQueue(this);

        JSONObject paramJson = new JSONObject();
        paramJson.put("img", imgURLID);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                paramJson,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(popUpEditDelete.this, "Delete Sucess", Toast.LENGTH_SHORT).show();
                        goToMap();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("Error: " + error + "     " + error.getMessage());
                        Toast.makeText(popUpEditDelete.this, "Register Error", Toast.LENGTH_SHORT).show();
                        if(error instanceof NetworkError) {
                            Toast.makeText(popUpEditDelete.this, "Cannot connect to Internet...Please check your connection!", Toast.LENGTH_SHORT).show();
                        }
                        else if(error instanceof ServerError) {
                            Toast.makeText(popUpEditDelete.this, "The server could not be found. Please try again after some time!!", Toast.LENGTH_SHORT).show();
                        }
                        else if (error instanceof ParseError) {
                            Toast.makeText(popUpEditDelete.this, "Parsing error! Please try again after some time!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        queue.add(jsonObjectRequest);
    }

    private void editReport() throws JSONException {
        String url ="http://64.227.36.62/api/editReport";

        RequestQueue queue = Volley.newRequestQueue(this);

        JSONObject paramJson = new JSONObject();
        paramJson.put("img", imgURLID);
        paramJson.put("description", description.getText().toString());


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.PUT,
                url,
                paramJson,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(popUpEditDelete.this, "Edit Sucess", Toast.LENGTH_SHORT).show();
                        goToMap();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("Error: " + error + "     " + error.getMessage());
                        Toast.makeText(popUpEditDelete.this, "Register Error", Toast.LENGTH_SHORT).show();
                        if(error instanceof NetworkError) {
                            Toast.makeText(popUpEditDelete.this, "Cannot connect to Internet...Please check your connection!", Toast.LENGTH_SHORT).show();
                        }
                        else if(error instanceof ServerError) {
                            Toast.makeText(popUpEditDelete.this, "The server could not be found. Please try again after some time!!", Toast.LENGTH_SHORT).show();
                        }
                        else if (error instanceof ParseError) {
                            Toast.makeText(popUpEditDelete.this, "Parsing error! Please try again after some time!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        queue.add(jsonObjectRequest);
    }

    private void goToMap() {
        finish();

        Intent intent = new Intent(this,Maps.class);
        intent.putExtra("ID", userID);
        startActivity(intent);
    }


}
