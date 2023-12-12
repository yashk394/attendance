package com.example.blinin;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class BlinkIn_register extends AppCompatActivity {
    EditText e1, e2, e3, e4;
    Button button, button1;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference, db;
    addemp addemp;
    boolean isConnected;
    String s1 = "hello", s2 = "world", s3 = "casetitle";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blink_in_register);
        button = findViewById(R.id.can);
        button1 = findViewById(R.id.bttn);
        e1 = findViewById(R.id.name);
        e2 = findViewById(R.id.empi);
        e3 = findViewById(R.id.mail);
        e4 = findViewById(R.id.password);
        e4.setLongClickable(false);
        firebaseDatabase = FirebaseDatabase.getInstance();
        addemp = new addemp();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BlinkIn_register.this, BlinkIn_login.class);
                startActivity(intent);
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectivityManager cm =
                        (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                isConnected = activeNetwork != null &&
                        activeNetwork.isConnectedOrConnecting();
                if(!isConnected) {
                    Snackbar.make(view, "please Connect to Internet", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                else {
                    if (e1.getText().toString().isEmpty() || e2.getText().toString().isEmpty() ||
                            e3.getText().toString().isEmpty() || e4.getText().toString().isEmpty()) {
                        Snackbar.make(view, "please, Fill the data", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                    else {
//                        addDatatoFirebase(e1.getText().toString(), e2.getText().toString(), e3.getText().toString(),
//                                            e4.getText().toString());
//
//                                    Snackbar.make(view, "Employee Added Successfully", Snackbar.LENGTH_LONG)
//                                            .setAction("Action", null).show();
//                                    e1.getText().clear();
//                                    e2.getText().clear();
//                                    e3.getText().clear();
//                                    e4.getText().clear();





                        db =firebaseDatabase.getReference("attendance");
                        db.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.hasChild(e2.getText().toString().trim())) {
                                    Snackbar.make(view, "Alredy Exist", Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                    e2.getText().clear();
                                }
                                else {
                                    addDatatoFirebase(e1.getText().toString(), e2.getText().toString(), e3.getText().toString(),
                                            e4.getText().toString());

                                    Snackbar.make(view, "Employee Added Successfully", Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                    Toast.makeText(BlinkIn_register.this, "Employee Added Successfully", Toast.LENGTH_SHORT).show();
                                    e1.getText().clear();
                                    e2.getText().clear();
                                    e3.getText().clear();
                                    e4.getText().clear();
                                    Intent intent = new Intent(BlinkIn_register.this, BlinkIn_login.class);
                                    startActivity(intent);

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            }
        });
    }
    private void addDatatoFirebase(String s1, String s2, String s3, String s4) {
        addemp.setName(s1);
        addemp.setEmpid(s2);
        addemp.setMail(s3);
        String ss = md5(s4);
        addemp.setPass(ss);
databaseReference = firebaseDatabase.getReference("attendance").child(s2);
databaseReference.setValue(addemp);
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