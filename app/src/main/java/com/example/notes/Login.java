package com.example.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.scottyab.aescrypt.AESCrypt;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.GeneralSecurityException;

public class Login extends AppCompatActivity {

    String key2 = "L^A4n<QwN#j>^_D5.+:TH'tp~R5n6XEy";
    EditText email;
    EditText password;
    Button loginbtn;
    TextView registerText;
    TextView anonymousLogin;

    String CHANNEL_ID = "reportsID";
    String CHANNEL_NAME = "Reports";
    String CHANNEL_DESC = "Reports Notification";


    Integer MY_SOCKET_TIMEOUT_MS = 100000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME,NotificationManager.IMPORTANCE_DEFAULT );
            channel.setDescription(CHANNEL_DESC);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        // Notificação local
        //NotificationHelper.displayNotification(this,"title","Body");

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if(task.isSuccessful()){
                            System.out.println("Token: " + task.getResult().getToken());
                            try {
                                sendToken(task.getResult().getToken());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else{
                            System.out.println("Erro token: " + task.getException());
                        }
                    }
                });



        email = findViewById(R.id.loginemail);
        password = findViewById(R.id.loginpassword);
        registerText = findViewById(R.id.registerText);
        loginbtn = findViewById(R.id.loginBtn);
        anonymousLogin = findViewById(R.id.anonymusLogin);


        // Register text
        registerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),Register.class);
                startActivity(intent);
            }
        });


        // Login Button
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(validateFields()){
                        login();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        // Btn Notas
        anonymousLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),MainActivity.class);
                startActivity(intent);
            }
        });

    }

    private void sendToken(String token) throws JSONException {
        String url ="http://64.227.36.62/api/newToken";

        RequestQueue queue = Volley.newRequestQueue(this);

        JSONObject paramJson = new JSONObject();
        paramJson.put("token", token);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                paramJson,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("Reponse Token: " + response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("Error token: " + error);
                        System.out.println("Error token: " + error.getMessage());

                    }
                });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(jsonObjectRequest);
    }

    private boolean validateFields() {

        if(!email.getText().toString().equals("")){
            if(!password.getText().toString().equals("")){
                return true;
            }else{
                Toast.makeText(this, "Password field must be filled", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "Email field must be filled", Toast.LENGTH_SHORT).show();
        }

        return false;
    }

    // Http request to API
    public void login() throws JSONException {
        String url ="http://64.227.36.62/api/checkUser2/" + email.getText().toString() + "/"+ encrypt(password.getText().toString());

        RequestQueue queue = Volley.newRequestQueue(this);

        JSONObject paramJson = new JSONObject();
        paramJson.put("email", email.getText().toString());
        paramJson.put("password", password.getText().toString());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                paramJson,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Toast.makeText(Login.this, "Login success", Toast.LENGTH_SHORT).show();
                            int userID;
                            userID = response.getInt("ID");
                            goToMap(userID);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Login.this, "Login Error", Toast.LENGTH_SHORT).show();
                        System.out.println("Error: " + error.getMessage());
                        if(error instanceof NetworkError) {
                            Toast.makeText(Login.this, "Cannot connect to Internet...Please check your connection!", Toast.LENGTH_SHORT).show();
                        }
                        else if(error instanceof ServerError) {
                            Toast.makeText(Login.this, "The server could not be found. Please try again after some time!!", Toast.LENGTH_SHORT).show();
                        }
                        else if (error instanceof ParseError) {
                            Toast.makeText(Login.this, "Parsing error! Please try again after some time!!", Toast.LENGTH_SHORT).show();
                        }
                    }

                });

        queue.add(jsonObjectRequest);
    }

    private void goToMap(int userID) {
        finish();

        Intent intent = new Intent(this,Maps.class);
        intent.putExtra("ID", userID);
        startActivity(intent);
    }


    public String encrypt(String message){

        try {
            return AESCrypt.encrypt(key2, message);
        }catch (GeneralSecurityException e){
            System.out.println("Error: " + e.getMessage());
        }
        return "encrypt error";
    }


}