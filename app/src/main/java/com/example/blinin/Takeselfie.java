package com.example.blinin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Takeselfie extends AppCompatActivity {
Button button, button1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_takeselfie);
        button = findViewById(R.id.check_in);
        button1 = findViewById(R.id.checkout);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Takeselfie.this, upload.class);
                startActivity(intent);
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Takeselfie.this, upload.class);
                startActivity(intent);
            }
        });
    }
}