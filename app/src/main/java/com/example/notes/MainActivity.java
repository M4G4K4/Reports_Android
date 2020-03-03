package com.example.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    //TODO: Edit functionality
    //TODO: Delete funcionality
    //TODO: landscape layout at least in 1 screan



    Toolbar toolbar;
    RecyclerView recyclerView;
    Adapter adapter;
    List<Notes> notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DB db = new DB(this);
        notes = db.getNotes();

        recyclerView = findViewById(R.id.listNotes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter(this,notes);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,
                2,GridLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);
    }




    // Toolbar menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent = new Intent(this,AddNote.class);
        startActivity(intent);

        return super.onOptionsItemSelected(item);
    }



/*
    // Long press sub menu
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        getMenuInflater().inflate(R.menu.sub_menu,menu);
    }


    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
       switch (item.getItemId()){
           case R.id.subMenu_Delete:
               Toast.makeText(this, "Delte option", Toast.LENGTH_SHORT).show();
               return true;

           case R.id.subMenu_Edit:
               Toast.makeText(this, "Edit option", Toast.LENGTH_SHORT).show();

               return true;

           default:

               return  super.onContextItemSelected(item);
       }

    }
    */




} // end main activity
