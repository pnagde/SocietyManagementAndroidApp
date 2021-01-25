package com.example.societymanagementapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Calendar;
import java.util.UUID;

public class SocietyForm extends AppCompatActivity implements AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {


    DatabaseReference reference;
    FirebaseDatabase firebaseDatabase;
    ImageView societyimage;
    EditText fullnameTxt,AddressTxt,MobileTxt,cityTxt,pincodeTxt,emailTxt,billpermonthTxt;
    private String fullname,address,mobile,city,pincode,email,billpermonth,month,year,society_pic_url,status,startmonth;
    Spinner spinnerMonth,spinnerYear;
    Button submit,cancel;
    RadioButton activate,deactivate;
    RadioGroup userStatus;
    FirebaseStorage storage;
    RadioButton sats;
    private Uri filePath;
    public static final int PICK_IMAGE = 1;
    int code=0;
    private String url;
    Window window;
    StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_society_form);
        setTitle("Society Form");
        setupUI();
        //database
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        if (Build.VERSION.SDK_INT>=21){
            window=this.getWindow();
            window.setStatusBarColor(this.getResources().getColor(R.color.primaryDark));
        }

        spinnerMonth.setOnItemSelectedListener(this);
        spinnerYear.setOnItemSelectedListener(this);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.month_list, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMonth.setAdapter(adapter);
        ArrayAdapter adapter1 = ArrayAdapter.createFromResource(this, R.array.years_list, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerYear.setAdapter(adapter1);
        firebaseDatabase=FirebaseDatabase.getInstance();
        reference=firebaseDatabase.getReference("Society_details");
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),Dashboard.class);
                startActivity(intent);
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate()) {
                    getData();
                    Society society = new Society(fullname, mobile, society_pic_url, city,
                            address, pincode, email, billpermonth, startmonth, status, year, month);
                    reference.child(society.getFullName()).setValue(society);
                    Toast.makeText(getApplicationContext(), "Submitted", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), Dashboard.class);
                    startActivity(intent);
                }
            }
        });


        /*
- Name*
- Mobile Number*
- Photo/Logo/Icon
- City*
- Address
- Pin Code
- Email Id
- Submit & Cancel Button
- Billing per month* - in INR
- Billing start month - Drop down with month and year selection
- Radio button "Activate"   "Deactivate". On deactivate selection logout and deactivate all the users of that society.
         */
    }

    private void setupUI(){
        fullnameTxt=(EditText) findViewById(R.id.fullNameSociety);
        AddressTxt=(EditText)findViewById(R.id.addressSociety);
        MobileTxt=(EditText)findViewById(R.id.mobileNumSociety);
        cityTxt=(EditText)findViewById(R.id.citySociety);
        pincodeTxt=(EditText)findViewById(R.id.pincodeSociety);
        emailTxt=(EditText)findViewById(R.id.emailSociety);
        billpermonthTxt=(EditText)findViewById(R.id.billPerMonth);
        spinnerMonth=(Spinner) findViewById(R.id.spinnerDate);
        spinnerYear=(Spinner)findViewById(R.id.spinnerYear);
        submit=(Button) findViewById(R.id.SocietySubmit);
        cancel=(Button) findViewById(R.id.societyCancel);
        societyimage=(ImageView)findViewById(R.id.societyImage);
        activate=(RadioButton)findViewById(R.id.userActivate);
        deactivate=(RadioButton)findViewById(R.id.userDeactivate);
        userStatus=(RadioGroup)findViewById(R.id.groupRadio);
    }
    private void getData(){
        fullname=fullnameTxt.getText().toString();
        email=emailTxt.getText().toString().trim();
        address=AddressTxt.getText().toString();
        city=cityTxt.getText().toString();
        pincode=pincodeTxt.getText().toString();
        mobile=MobileTxt.getText().toString();
        billpermonth=billpermonthTxt.getText().toString();
        checkUserStatus();
        year=spinnerYear.getSelectedItem().toString();
        month=spinnerMonth.getSelectedItem().toString();
        startmonth=month+" "+year;
    }
    public void checkUserStatus() {
        int selectedId = userStatus.getCheckedRadioButtonId();
         sats= (RadioButton) findViewById(selectedId);
        if (selectedId <= 0) {
            deactivate.setError("select");
            Toast.makeText(getApplicationContext(), "Nothing selected", Toast.LENGTH_SHORT).show();
        } else {
            status = sats.getText().toString();
        }
    }
    public void pickImage(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
        code = 1;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

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
                                            society_pic_url = uri.toString();
                                        }
                                    });
                                    progressDialog.dismiss();
                                    Toast
                                            .makeText(SocietyForm.this,
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
                                    .makeText(SocietyForm.this,
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
                societyimage.setImageBitmap(bitmap);
                uploadImage();//upload image to the cloud storage
            } catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }
    private boolean validateName() {
        String val = fullnameTxt.getText().toString().trim();
        if (val.isEmpty()) {
            fullnameTxt.setError("Enter Email");
            return false;
        } else {
            fullnameTxt.setError(null);
            return true;
        }
    }
    private boolean validateEmail() {
        String val = emailTxt.getText().toString().trim();
        if (val.isEmpty()) {
            emailTxt.setError("Enter Email");
            return false;
        } else {
            emailTxt.setError(null);
            return true;
        }
    }
    private boolean validateAddress() {
        String val = AddressTxt.getText().toString().trim();
        if (val.isEmpty()) {
            AddressTxt.setError("Enter address");
            return false;
        } else {
            AddressTxt.setError(null);
            return true;
        }
    }
    private boolean validateCity() {
        String val = cityTxt.getText().toString().trim();
        if (val.isEmpty()) {
            cityTxt.setError("Enter city");
            return false;
        } else {
            cityTxt.setError(null);
            return true;
        }
    }
    private boolean validatePincode() {
        String val = pincodeTxt.getText().toString().trim();
        if (val.isEmpty()) {
            pincodeTxt.setError("Enter Pincode");
            return false;
        } else {
            pincodeTxt.setError(null);
            return true;
        }
    }
    private boolean validateBillPerMonth() {
        String val = billpermonthTxt.getText().toString().trim();
        if (val.isEmpty()) {
            billpermonthTxt.setError("Enter bill");
            return false;
        } else {
            billpermonthTxt.setError(null);
            return true;
        }
    }
    private boolean validateDate() {
        String val = spinnerMonth.getSelectedItem().toString();
        if (val.isEmpty()) {
            TextView errorText = (TextView)spinnerMonth.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText("Select please");//changes the selected item text to this
            return false;
        } else {
            fullnameTxt.setError(null);
            return true;
        }
    }
    private boolean validateOption() {
        String val = deactivate.getText().toString().trim();
        if (val.isEmpty()) {
            deactivate.setError("Enter Options");
            return false;
        } else {
            deactivate.setError(null);
            return true;
        }
    }
    private boolean validateYear() {
        String val = spinnerYear.getSelectedItem().toString();
        if (val.isEmpty()) {
            TextView errorText = (TextView)spinnerYear.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText("Select please");//changes the selected item text to this
            return false;
        } else {
            fullnameTxt.setError(null);
            return true;
        }
    }

    private boolean validate(){
        return validateName() && validateEmail() && validateAddress() &&validateCity() &&validatePincode() &&validateBillPerMonth()
                &&validateDate() &&validateOption() && validateYear();
    }

}