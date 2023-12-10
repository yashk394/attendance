package com.example.blinin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toolbar;

import com.airbnb.lottie.LottieAnimationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        getSupportActionBar().hide();
        Thread thread = new Thread() {
            public void run() {
                try {
                    Thread.sleep(4000);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                finally {
                    Intent intent =new Intent(MainActivity.this, BlinkIn_login.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
        thread.start();

    }

}