package com.example.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;


public class AddNote extends AppCompatActivity {

    Toolbar toolbar;
    EditText title, description;
    Calendar calendar;
    String date;
    String time;
    String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Edit note");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // add back button to the tool bar , android manifested edited to tell the parent activity to be able to know where to go back


        title = findViewById(R.id.title);
        description = findViewById(R.id.description);


        title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() != 0){
                    // Set the title of action bar as the title of the note
                    Objects.requireNonNull(getSupportActionBar()).setTitle(charSequence);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // Get current time and date
        calendar = Calendar.getInstance();
        date = calendar.get(Calendar.DAY_OF_MONTH) + "/" + addValueMonth(calendar.get(Calendar.MONTH) +1)  +  "/" + calendar.get(Calendar.YEAR);
        time = addValueHour(calendar.get(Calendar.HOUR)) + ":" +   addValueMinutes(calendar.get(Calendar.MINUTE));

    }


    // Show time and date correctly if is time less than 10
    private String addValueMinutes(int i){
        if(i<10){
            return "0" + i;
        }else{
            return String.valueOf(i);
        }
    }

    private String addValueHour(int i){
        if(i<10){

            return String.valueOf(i + 12);
        }else{
            return String.valueOf(i);
        }
    }

    private String addValueMonth(int i){
        if(i<10){
            return "0" + i;
        }else{
            return String.valueOf(i);
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.newnote_menu,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.cancelBtn){
            title.getText().clear();
            description.getText().clear();
            onBackPressed();
        }
        else if(item.getItemId() == R.id.saveBtn){

            Notes note = new Notes(title.getText().toString(),description.getText().toString(),time,date);

            DB db = new DB(this);
            db.addNote(note);

            Toast.makeText(this,getResources().getString(R.string.ToastSave) , Toast.LENGTH_SHORT).show();

            goToMain();
        }

        return super.onOptionsItemSelected(item);
    }

    // To reload and update with the new note
    private void goToMain() {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }


}
