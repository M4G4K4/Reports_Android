package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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

import org.json.JSONException;
import org.json.JSONObject;

public class Register extends AppCompatActivity {


    Button registerBtn;
    TextView name;
    TextView email;
    TextView password;
    TextView confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

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

        if(password.getText().toString().equals(confirmPassword.getText().toString())){
            return true;
        }

        return false;
    }

    private boolean validateRegister() {
        if(!name.getText().toString().equals("")){
            if(!email.getText().toString().equals("")){
                if(!password.getText().toString().equals("")){
                    if(!confirmPassword.getText().toString().equals("")){
                        return  true;
                    }
                }
            }
        }


        return false;
    }


    public void register() throws JSONException {
        String url ="http://172.16.176.120:3000/api/registerUser";

        RequestQueue queue = Volley.newRequestQueue(this);

        JSONObject paramJson = new JSONObject();

            paramJson.put("email", email.getText().toString());
            paramJson.put("password", password.getText().toString());
            paramJson.put("name",name.getText().toString());


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
                            //TODO: unique constrain error

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
}
