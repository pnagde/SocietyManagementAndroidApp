package com.example.societymanagementapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class SocietyDoc extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;
    Button chooserBtn, uploaderBtn,cancel;
    TextView alert;
    ImageView chooseDoc;
    EditText name;
    String docName;
    private Uri ImageUri;
    ArrayList ImageList = new ArrayList();
    private int upload_count = 0;
    private ProgressDialog progressDialog;
    ArrayList urlStrings;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_society_doc);
        setTitle("Upload Document");
        uploaderBtn = findViewById(R.id.uploader);
        cancel = findViewById(R.id.cancelUpload);
        chooserBtn = findViewById(R.id.doc);
        alert = findViewById(R.id.textView);
        name=(EditText)findViewById(R.id.Doc_name);
        uploaderBtn.setVisibility(View.GONE);
        progressDialog = new ProgressDialog(SocietyDoc.this);
        progressDialog.setMessage("Uploading Images please Wait.........!!!!!!");
        chooserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(intent, PICK_IMAGE);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SocietyDoc.this,Dashboard.class);
                startActivity(intent);
            }
        });
        uploaderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                docName = name.getText().toString();
                if (validateName()) {
                    urlStrings = new ArrayList<>();
                    progressDialog.show();
                    alert.setText("If Loading Takes to long press button again");
                    StorageReference ImageFolder = FirebaseStorage.getInstance().getReference();

                    for (upload_count = 0; upload_count < ImageList.size(); upload_count++) {

                        Uri IndividualImage = (Uri) ImageList.get(upload_count);
                        final StorageReference ImageName = ImageFolder.child("Documents/" + IndividualImage.getLastPathSegment());

                        ImageName.putFile(IndividualImage).addOnSuccessListener(
                                new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        ImageName.getDownloadUrl().addOnSuccessListener(
                                                new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        urlStrings.add(String.valueOf(uri));


                                                        if (urlStrings.size() == ImageList.size()) {
                                                            storeLink(urlStrings);
                                                        }
                                                    }
                                                }
                                        );
                                    }
                                }
                        );


                    }


                }
            }
        });//
    }
    private void storeLink(ArrayList<String> urlStrings) {
        docName=name.getText().toString();

        HashMap<String, String> hashMap = new HashMap<>();

        for (int i = 0; i <urlStrings.size() ; i++) {
            hashMap.put("ImgLink"+i, urlStrings.get(i));

        }
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("images").child(docName);

        databaseReference.push().setValue(hashMap)
                .addOnCompleteListener(
                        new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(SocietyDoc.this, "Successfully Uploaded", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(SocietyDoc.this,"Failed",Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                ).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SocietyDoc.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        progressDialog.dismiss();
        alert.setText("Uploaded Successfully");
        uploaderBtn.setVisibility(View.GONE);
        ImageList.clear();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {
            if (resultCode == RESULT_OK) {


                if (data.getClipData() != null) {

                    int countClipData = data.getClipData().getItemCount();
                    int currentImageSlect = 0;

                    while (currentImageSlect < countClipData) {

                        ImageUri = data.getClipData().getItemAt(currentImageSlect).getUri();
                        ImageList.add(ImageUri);
                        currentImageSlect = currentImageSlect + 1;
                    }

                    alert.setVisibility(View.VISIBLE);
                    alert.setText("You have selected" + ImageList.size() + "Images");
                    chooserBtn.setVisibility(View.GONE);
                    uploaderBtn.setVisibility(View.VISIBLE);


                } else {
                    Toast.makeText(SocietyDoc.this, "Please Select Multiple Images", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }
    public boolean validateName(){
        String val=name.getText().toString().trim();
        if(val.isEmpty()){
            name.setError("Required");
        return false;
        }
        else{
            name.setError(null);
        return true;
        }
    }
}