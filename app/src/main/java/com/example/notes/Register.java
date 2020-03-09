package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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



        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Register.this, "Register BTN", Toast.LENGTH_SHORT).show();
            }
        });



    }
}
