package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class Edit extends AppCompatActivity {

    TextView editTitle;
    TextView editDescription;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);


        Intent intent = getIntent();
        Long id = intent.getLongExtra("ID",0);
        System.out.println("Edit ID: " + id);



    }
}
