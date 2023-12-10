package com.example.blinin;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;

public class BlinkIn_login extends AppCompatActivity {
    boolean baack = false;
    boolean isConnected;
    Button button;
    RelativeLayout relativeLayout;
    EditText e1, e2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blink_in_login);
        TextView textView = findViewById(R.id.register);
        relativeLayout = findViewById(R.id.rel);
        button = findViewById(R.id.loginbtn);
        e1 = findViewById(R.id.empid);
        e2 = findViewById(R.id.pass);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectivityManager cm =
                        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                isConnected = activeNetwork != null &&
                        activeNetwork.isConnectedOrConnecting();
                String s1 = e1.getText().toString().trim();
                String s2 = e2.getText().toString().trim();
                if (isConnected) {
                    if (s1.equals("abc") && s2.equals("123")) {
                        Intent intent = new Intent(BlinkIn_login.this, Mark_my_attendance.class);
                        startActivity(intent);
                    }
                    else if (s1.equals("admin") && s2.equals("123")) {
                        Intent intent = new Intent(BlinkIn_login.this, Admin.class);
                        startActivity(intent);
                    }
                } else {
                    Snackbar s = Snackbar.make(relativeLayout, getString(R.string.youof), Snackbar.LENGTH_SHORT);
                    s.show();
                }
            }
        });
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BlinkIn_login.this, BlinkIn_register.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (baack) {
            baack = false;
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            super.onBackPressed();

            return;
        } else {
            this.baack = true;
            Snackbar s = Snackbar.make(relativeLayout, getString(R.string.press), Snackbar.LENGTH_SHORT);
            s.show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    baack = false;

                }
            }, 3000);


        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lag, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.eng) {
            Toast.makeText(this, "Language set to English", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.marathi) {
            Toast.makeText(this, "Language set to Marathix`", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

}