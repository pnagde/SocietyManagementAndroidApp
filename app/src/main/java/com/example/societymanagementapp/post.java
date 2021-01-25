package com.example.societymanagementapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

public class post extends AppCompatActivity {


    final static int PICK_IMAGE=1;
    int code;
    EditText nametxt,respotxt;
    Button submit;
    ImageView post_pic;
    private Uri filePath;
    FirebaseDatabase firebaseDatabase;
    FirebaseStorage storage;
     DatabaseReference databaseReference;
     StorageReference storageReference;
     private String doc_url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        setTitle("POST FORM");

        nametxt=(EditText)findViewById(R.id.name_txt);
        respotxt=(EditText)findViewById(R.id.respo_txt);
        submit=(Button)findViewById(R.id.post_button);
        post_pic=(ImageView)findViewById(R.id.imageViewPost);
        storage = FirebaseStorage.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        storageReference = storage.getReference();
        databaseReference=firebaseDatabase.getReference("Post");
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=nametxt.getText().toString();
                String respo=respotxt.getText().toString();
                if(validate()) {
                    ModelPost post = new ModelPost(name, respo, doc_url);
                    databaseReference.child(name).setValue(post);
                    Snackbar snackbar = Snackbar
                            .make(view, "submitted", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
                else{
                    Snackbar snackbar = Snackbar
                            .make(view, "Failed", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });

    }
    public void uploadImage() {

        if (filePath != null) {
            // Defining the child of storageReference
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("uploading...");
            progressDialog.show();
            final StorageReference ref
                    = storageReference
                    .child(
                            "images/" + "SocietyImages/"
                                    + UUID.randomUUID().toString());

            // adding listeners on upload
            // or failure of image
            ref.putFile(filePath)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot) {

                                    // Image uploaded successfully
                                    // Dismiss dialog
                                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            doc_url = uri.toString();
                                        }
                                    });
                                    progressDialog.dismiss();
                                    Toast
                                            .makeText(post.this,
                                                    "Image Uploaded!!",
                                                    Toast.LENGTH_SHORT)
                                            .show();
                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast
                                    .makeText(post.this,
                                            "Failed " + e.getMessage(),
                                            Toast.LENGTH_SHORT)
                                    .show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage(
                                            "Uploaded "
                                                    + (int) progress + "%");
                                }
                            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            // Get the Uri of data
            filePath = data.getData();
            try {

                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContentResolver(),
                                filePath);
                post_pic.setImageBitmap(bitmap);
                uploadImage();//upload image to the cloud storage
            } catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }

    public void pickImage(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
        code = 1;
    }
    private boolean validateName(){
            String val = nametxt.getText().toString().trim();
            if (val.isEmpty()) {
                nametxt.setError("Enter Name");
                return false;
            } else {
                nametxt.setError(null);
                return true;
            }
    }
    private boolean validateRespo(){
        String val = respotxt.getText().toString().trim();
        if (val.isEmpty()) {
            respotxt.setError("Enter Responsibilities");
            return false;
        } else {
            respotxt.setError(null);
            return true;
        }
    }
    private boolean validate(){
        return validateName()&&validateRespo();
    }
}