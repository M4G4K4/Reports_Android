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
        Objects.requireNonNull(getSupportActionBar()).setTitle("Edit note");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // add back button to the tool bar , android manifested edited to tell the parent activity to be able to know where to go back


        Intent intent = getIntent();
        long id = intent.getLongExtra("ID",0);
        System.out.println("Edit ID: " + id);

        editTitle = findViewById(R.id.editTitle);
        editDescription = findViewById(R.id.editDescription);

        Notes note = new Notes();
        DB db = new DB(this);
        note = db.getNote(id);

        editTitle.setText(note.getTitle());
        editDescription.setText(note.getDescription());

        db.close();
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

        switch (item.getItemId()){
            case R.id.saveEditBtn:
                Toast.makeText(this, "Save Button cliked ", Toast.LENGTH_SHORT).show();

                Intent intent = getIntent();
                long id = intent.getLongExtra("ID",0);


                DB db = new DB(this);
                Notes note = new Notes();
                note = db.getNote(id);

                note.setTitle(editTitle.getText().toString());
                note.setDescription(editDescription.getText().toString());

                db.editNote(note);
                db.close();

                goToMain();
                return true;

            case R.id.deleteEditBtn:
                Toast.makeText(this, "Delete clicked", Toast.LENGTH_SHORT).show();

                Intent intentDelete = getIntent();
                id = intentDelete.getLongExtra("ID", 0);
                DB dbdelete = new DB(this);
                dbdelete.deleteNote(id);

                dbdelete.close();
                goToMain();
                return true;

            default:
                return super.onContextItemSelected(item);
        }

    }

    // To reload and update the recycler view with the changes
    private void goToMain() {
        finish();
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

}
