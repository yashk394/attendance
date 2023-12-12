package com.example.blinin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

public class upload extends AppCompatActivity {

    Button uploadd;
    ImageView imageView;
    Uri uri,u;
    boolean isConnected;
    StorageTask storageTask;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    AlertDialog.Builder builder;
    ProgressDialog progressDialog;
    private static final int CAMERA_REQUEST = 1888;

    int a = 0,b=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        uploadd = findViewById(R.id.buttonup);
        imageView = findViewById(R.id.imgg);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("uploading,please wait..!");
        progressDialog.setCancelable(false);

        builder = new AlertDialog.Builder(this);
        storageReference = FirebaseStorage.getInstance().getReference("uploads");
        databaseReference = FirebaseDatabase.getInstance().getReference("uploads");

        uploadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectivityManager cm =
                        (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                isConnected = activeNetwork != null &&
                        activeNetwork.isConnectedOrConnecting();
                if(!isConnected) {
                    Toast.makeText(getApplicationContext(), "please,connect to internet", Toast.LENGTH_SHORT).show();
                }
                else {
                    a = 1;
                    if (u!=null) {
                        caall();
                        Recordmyattendance();
                    }
                    else
                    {
                        Toast.makeText(upload.this, "please select image..!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    private void Recordmyattendance() {
        
    }

    public void choose() {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(intent, PICK_IMAGE_REQUEST);
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == PICK_IMAGE_REQUEST) {
//            if (resultCode == RESULT_OK) {
//                uri = data.getData();
//                Picasso.get().load(uri).into(imageView);
//                if (uri != null) {
//                    a = 0;
//                    u=uri;
//                } else {
//                    a=0;
//                    Toast.makeText(getApplicationContext(), "no file selected", Toast.LENGTH_SHORT).show();
//                    Toast.makeText(this, "please select image", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//        }
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {

            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            // Set the captured image to the ImageView
            imageView.setImageBitmap(imageBitmap);

            // Save the captured image URI for uploading to Firebase
            u = getImageUri(getApplicationContext(), imageBitmap);
        }
    }
    private Uri getImageUri(Context context, Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Title", null);
        return Uri.parse(path);
    }
    private String getfilextension(Uri urii) {
        if (urii == null) {
            // Handle the case where 'u' is null, for example, return a default extension
            return "jpg";
        }

        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(urii));

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.cam, menu);
        return super.onCreateOptionsMenu(menu);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.ad) {
            choose();
            b=2;
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void caall()
    {
        if (a == 1) {
            progressDialog.show();

            final StorageReference storageReference1 = storageReference.child(System.currentTimeMillis() + "." + getfilextension(uri));
            storageReference1.putFile(u).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storageReference1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                }
                            }, 400);
                            Toast.makeText(upload.this, "upload successfully", Toast.LENGTH_SHORT).show();
                            databaseReference = FirebaseDatabase.getInstance().getReference().child("Upload");
                            String uploadid = databaseReference.push().getKey();
                            Uploadimage uploadimage = new Uploadimage(String.valueOf(uri));
                            databaseReference.child(uploadid).setValue(uploadimage);
                            progressDialog.dismiss();
                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(upload.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {

                }
            });
        }


    }}