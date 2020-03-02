package com.example.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

public class Edit extends AppCompatActivity {

    Toolbar toolbar;
    EditText editTitle;
    EditText editDescription;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        // Toolbar
        toolbar = findViewById(R.id.toolbaredit);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("New note");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // add back button to the tool bar , android manifested edited to tell the parent activity to be able to know where to go back


        Intent intent = getIntent();
        Long id = intent.getLongExtra("ID",0);
        System.out.println("Edit ID: " + id);

        editTitle = findViewById(R.id.editTitle);
        editDescription = findViewById(R.id.editDescription);

        Notes note = new Notes();
        DB db = new DB(this);
        note = db.getNote(id);

        editTitle.setText(note.getTitle());
        editDescription.setText(note.getDescription());

    }

    // Toolbar menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.saveEditBtn){
            Toast.makeText(this, "Save edit clicked", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

}
