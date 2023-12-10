package com.example.blinin;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

public class BlinkIn_register extends AppCompatActivity {
    EditText e1, e2, e3, e4;
    Button button, button1;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    addemp addemp;
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
        addemp =new addemp();
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
                if (e1.getText().toString().isEmpty() || e2.getText().toString().isEmpty() || e3.getText().toString().isEmpty() || e4.getText().toString().isEmpty()) {
                    Toast.makeText(BlinkIn_register.this, getString(R.string.pleaseent), Toast.LENGTH_SHORT).show();

                }
                else {
                    addDatatoFirebase(e1.getText().toString(),e2.getText().toString(),e3.getText().toString(),
                            e4.getText().toString());
                    Snackbar.make(view, "Employee Added", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    e1.getText().clear();
                    e2.getText().clear();
                    e3.getText().clear();
                    e4.getText().clear();


                }
            }
        });
    }

    private void addDatatoFirebase(String toString, String toString1, String toString2, String toString3) {
        addemp.setName(toString);
        addemp.setEmpid(toString1);
        addemp.setMail(toString2);
        addemp.setPass(toString3);
        databaseReference = firebaseDatabase.getReference();
        String id = databaseReference.push().getKey();
        Toast.makeText(this, ""+id, Toast.LENGTH_SHORT).show();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                databaseReference.child(id).setValue(addemp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Fail to add data " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }
//    private void deny() {
//        e1.getText().clear();
//        e2.getText().clear();
//        e3.getText().clear();
//        e4.getText().clear();
//        finish();
//    }
//    public void sendData() {
//
//        databaseReference = firebaseDatabase.getReference("attendance");
//        String id = databaseReference.push().getKey();
//        addemp = new addemp(e1.getText().toString().trim(), e2.getText().toString().trim(), e3.getText().toString().trim(), e4.getText().toString().trim());
//        databaseReference.setValue(addemp);
//        databaseReference2 = FirebaseDatabase.getInstance().getReference("attendance");
//        databaseReference2.child(id).setValue(addemp);
//    }
}