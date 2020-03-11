package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {

    //TODO: unique constrain error
    //TODO: encrypt password


    EditText email;
    EditText password;
    Button loginbtn;
    TextView registerText;
    TextView anonymousLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
        String url ="http://172.16.176.120:3000/api/checkUser2/" + email.getText().toString() + "/"+password.getText().toString();

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
                        String statusCode = response.toString().substring(0, 57);

                        Toast.makeText(Login.this, "Login success", Toast.LENGTH_SHORT).show();
                        finish();
                        goToMap();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(Login.this, "Login Error", Toast.LENGTH_SHORT).show();

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

    private void goToMap() {
        Intent intent = new Intent(this,Map.class);
        startActivity(intent);
    }


}
