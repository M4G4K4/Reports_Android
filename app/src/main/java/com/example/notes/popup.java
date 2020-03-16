package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Button;
import android.widget.EditText;

public class popup extends AppCompatActivity {

    Button close,takepicture,cancel,save;
    EditText description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);


        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.8),(int)(height*.6));

        // Buttons
        close = findViewById(R.id.popBtnClose);
        takepicture = findViewById(R.id.popupBtnTakePicture);
        cancel = findViewById(R.id.popupBtnCancel);
        save = findViewById(R.id.popupBtnSave);

        // Edittext
        description = findViewById(R.id.popupReportDescription);


    }
}
