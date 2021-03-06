package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.scottyab.aescrypt.AESCrypt;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import static android.hardware.Sensor.TYPE_LIGHT;

public class Register extends AppCompatActivity {


    Button registerBtn;
    TextView name;
    TextView email;
    TextView password;
    TextView confirmPassword;

    private SensorManager sensorManager;
    private Sensor lightSensor;
    private SensorEventListener lightEventListener;

    private float maxValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.DarkTheme);
        setContentView(R.layout.activity_register);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(TYPE_LIGHT);

        if(lightSensor == null){
            Toast.makeText(this, "No light sensor", Toast.LENGTH_SHORT).show();
        }

        maxValue = lightSensor.getMaximumRange();


        lightEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float value = event.values[0];

                if(event.values[0] <= maxValue/2){
                    setTheme(R.style.DarkTheme);
                    setContentView(R.layout.activity_register);
                }else{
                    setTheme(R.style.LightTheme);
                    setContentView(R.layout.activity_register);
                }
            }
            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }

        };





        registerBtn = findViewById(R.id.registerBtn);
        name = findViewById(R.id.registername);
        email = findViewById(R.id.registeremail);
        password = findViewById(R.id.registerpassword);
        confirmPassword = findViewById(R.id.registerconfirmpassword);


        if(!password.getText().toString().equals(confirmPassword.getText().toString())){
            registerBtn.setEnabled(false);
        }

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(validateRegister()){
                        if(validatePassword()){
                            register();
                        }else{
                            Toast.makeText(Register.this, "Passwords don't match", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(Register.this, "All the fields must be filled", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    private boolean validatePassword(){

        return password.getText().toString().equals(confirmPassword.getText().toString());
    }

    private boolean validateRegister() {
        if(!name.getText().toString().equals("")){
            if(!email.getText().toString().equals("")){
                if(!password.getText().toString().equals("")){
                    return !confirmPassword.getText().toString().equals("");
                }
            }
        }


        return false;
    }


    public void register() throws JSONException {
        String url ="http://64.227.36.62/api/registerUser";

        RequestQueue queue = Volley.newRequestQueue(this);

        JSONObject paramJson = new JSONObject();

            paramJson.put("email", email.getText().toString());
            paramJson.put("password", encrypt(password.getText().toString()));
            paramJson.put("name", name.getText().toString());


            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    url,
                    paramJson,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Toast.makeText(Register.this, "Register success", Toast.LENGTH_SHORT).show();
                            finish();
                            onBackPressed();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(Register.this, "Register Error", Toast.LENGTH_SHORT).show();
                            if(error instanceof NetworkError) {
                                Toast.makeText(Register.this, "Cannot connect to Internet...Please check your connection!", Toast.LENGTH_SHORT).show();
                            }
                            else if(error instanceof ServerError) {
                                Toast.makeText(Register.this, "The server could not be found. Please try again after some time!!", Toast.LENGTH_SHORT).show();
                            }
                            else if (error instanceof ParseError) {
                                Toast.makeText(Register.this, "Parsing error! Please try again after some time!!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

            queue.add(jsonObjectRequest);

    }

    public String encrypt(String message){
        String password = "L^A4n<QwN#j>^_D5.+:TH'tp~R5n6XEy";
        try {
            return AESCrypt.encrypt(password, message);
        }catch (GeneralSecurityException e){
            System.out.println("Error: " + e.getMessage());
        }
        return "encrypt error";
    }


    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(lightEventListener,lightSensor,SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(lightEventListener);
    }



}
