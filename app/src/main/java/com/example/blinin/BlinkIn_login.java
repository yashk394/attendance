package com.example.blinin;

import androidx.annotation.NonNull;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class BlinkIn_login extends AppCompatActivity {
    boolean baack = false;
    boolean isConnected;
    Button button;
    RelativeLayout relativeLayout;
    EditText e1, e2;
    DatabaseReference databaseReference;
    String pas = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blink_in_login);
        TextView textView = findViewById(R.id.register);
        relativeLayout = findViewById(R.id.rel);
        button = findViewById(R.id.loginbtn);
        e1 = findViewById(R.id.empid);
        e2 = findViewById(R.id.pass);
        databaseReference = FirebaseDatabase.getInstance().getReference("attendance");
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

                    if (e1.getText().toString().isEmpty() || e2.getText().toString().isEmpty()) {
                        Snackbar.make(view, "please, Fill the data", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }

                    else if (s1.equals("abc") && s2.equals("123")) {
                        Intent intent = new Intent(BlinkIn_login.this, Mark_my_attendance.class);
                        startActivity(intent);
                    }
                    else if (s1.equals("admin") && s2.equals("123")) {
                        Intent intent = new Intent(BlinkIn_login.this, Admin.class);
                        startActivity(intent);
                    }
                    else {
                                databaseReference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.hasChild(e1.getText().toString().trim())) {
                                            pas =snapshot.child(e1.getText().toString().trim()).child("pass").getValue().toString().trim();
                                            String ss = md5(e2.getText().toString().trim());
                                            if (pas.equals(ss)) {
                                                Intent intent = new Intent(BlinkIn_login.this, Mark_my_attendance.class);
                                                startActivity(intent);
                                                Toast.makeText(BlinkIn_login.this, "Logged In Successfully!!!", Toast.LENGTH_SHORT).show();
                                            }
                                            else {
                                                Snackbar.make(view, "Record Not Found", Snackbar.LENGTH_LONG)
                                                        .setAction("Action", null).show();
                                                e1.getText().clear();
                                                e2.getText().clear();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
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
    public String md5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i=0; i<messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}