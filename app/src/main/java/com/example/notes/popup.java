package com.example.notes;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class popup extends AppCompatActivity {

    Button close, takepicture, cancel, save;
    EditText description;
    ImageView image;


    // Imgur API
    String ClientID = "917669a10ae9a08";
    String ClientSecreat = "ec6a7a3c715b601811debe8781e54c4f928964b2";


    Boolean SaveBtn = false;
    Boolean imageUploaded = false;

    // Logged in userID
    Integer userID;

    // Reponse image url
    String imgURL;

    // Base 64 of the image to be uploaded to imgur
    String base64;

    // Location
    Double longitude;
    Double latitude;
    String morada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);

        // Create Pop up
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width), (int) (height));


        // Get userID tha is logged in
        Intent intent = getIntent();
        userID = intent.getIntExtra("UserID", 0);


        // Buttons
        close = findViewById(R.id.popBtnClose);
        takepicture = findViewById(R.id.popupBtnTakePicture);
        cancel = findViewById(R.id.popupBtnCancel);
        save = findViewById(R.id.popupBtnSave);
        image = findViewById(R.id.popupImagecamera);

        // Edittext
        description = findViewById(R.id.popupReportDescription);

        save.setEnabled(false);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });

        takepicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 100);

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(popup.this, "Save Clicked", Toast.LENGTH_SHORT).show();
                // upload data to api
                try {
                    uploadAPI();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(popup.this, "Image cliked", Toast.LENGTH_SHORT).show();
            }
        });

        description.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                if (canSave()) {
                    save.setEnabled(true);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (canSave()) {
                    save.setEnabled(true);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (canSave()) {
                    save.setEnabled(true);
                }
            }
        });

        // Get GPS position
        GPS gpsTracker = new GPS(this);
        if (gpsTracker.getIsGPSTrackingEnabled()) {
            latitude = gpsTracker.latitude;
            longitude = gpsTracker.longitude;
            morada = gpsTracker.getAddressLine(this);
        }
        else {
            System.out.println("Erro obter location");
        }


    }

    private void uploadAPI() throws JSONException {
        String url ="http://64.227.36.62/api/newReport";

        RequestQueue queue = Volley.newRequestQueue(this);

        JSONObject paramJson = new JSONObject();
        paramJson.put("description", description.getText().toString());
        paramJson.put("longitude", latitude);
        paramJson.put("latitude", longitude);
        paramJson.put("img", imgURL);
        paramJson.put("morada", morada);
        paramJson.put("userID", userID);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                paramJson,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(popup.this, "Report added", Toast.LENGTH_SHORT).show();
                        goBack();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(popup.this, "Upload Error", Toast.LENGTH_SHORT).show();
                        System.out.println("Error: " + error);
                        if(error instanceof NetworkError) {
                            Toast.makeText(popup.this, "Cannot connect to Internet...Please check your connection!", Toast.LENGTH_SHORT).show();
                        }
                        else if(error instanceof ServerError) {
                            Toast.makeText(popup.this, "The server could not be found. Please try again after some time!!", Toast.LENGTH_SHORT).show();
                        }
                        else if (error instanceof ParseError) {
                            Toast.makeText(popup.this, "Parsing error! Please try again after some time!!", Toast.LENGTH_SHORT).show();
                        }
                    }

                });

        queue.add(jsonObjectRequest);
    }

    // Go back to Map activity
    private void goBack() {
        finish();

        Intent intent = new Intent(this,Maps.class);
        intent.putExtra("ID", userID);
        startActivity(intent);
    }

    // After camera activity being closed
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            image.setImageBitmap(bitmap);
            base64 = get64BaseImage(bitmap);
            try {
                uploadImg3();
                System.out.println("Upload success");
            } catch (JSONException e) {
                e.printStackTrace();
                System.out.println("Error upload: " + e.getMessage());
            }
        }
    }

    // Upload image Imgur
    public void uploadImg3() throws JSONException {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://api.imgur.com/3/upload";

        JSONObject paramJson = new JSONObject();
        paramJson.put("image", base64);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                paramJson,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("upload response sucess");
                        try {
                            JSONObject data = response.getJSONObject("data");
                            imgURL = data.getString("link");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(popup.this, "Imgur image upload error", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Authorization", "Client-ID " + ClientID);
                return params;
            }
        };
        queue.add(jsonObjectRequest);
    }

    // IF image is taken and description is written return true
    private boolean canSave(){
        return image.getDrawable() != null && description.getText().toString().equals("");
    }

    public static String get64BaseImage (Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }



}
