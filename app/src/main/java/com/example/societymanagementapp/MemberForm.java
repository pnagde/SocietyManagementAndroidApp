package com.example.societymanagementapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MemberForm extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final int PIC_CROP = 2;
    private EditText firstEdit, lastEdit, addressEdit, emailEdit, passwordEdit, mobileEdit, plotdEdit, plotwEdit, plotnEdit;
    private String firstName, lastName, address, emailId, password, sex, type_h, fullName, area;
    private RadioButton male, female, trans, home, plot, gender, type;
    private RadioGroup male_female, home_plot;
    private Button picUpload;
    FirebaseStorage storage;
    FirebaseAuth firebaseAuth;
    Context context;
    TextView plotHome;
    private Spinner spinner, spinnerRole, postSpinner;
    String UserMobileNumber, plot_number;
    private int plotSize_width, plotSize_depth;
    private String sector, role, post;
    private ImageView profile_photo;
    private Button userSubmit, UserCancel;
    public static final int PICK_IMAGE = 1;
    int code = 0;
    private String url;

    // views for button
    private Button btnSelect, btnUpload;

    Window window;
    // view for image view
    private ImageView imageView;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("members");

    ProgressBar progressBar;
    private FirebaseAuth mauth;
    // Uri indicates, where the image will be picked from
    private Uri filePath;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acivity_form_member);
        setTitle("MEMBER FORM");
        if (Build.VERSION.SDK_INT>=21){
            window=this.getWindow();
            window.setStatusBarColor(this.getResources().getColor(R.color.primaryDark));
        }
        setupUI();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        spinner.setOnItemSelectedListener(this);
        spinnerRole.setOnItemSelectedListener(this);
        postSpinner.setOnItemSelectedListener(this);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.sectors_list, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        ArrayAdapter adapter1 = ArrayAdapter.createFromResource(this, R.array.role_list, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRole.setAdapter(adapter1);
        ArrayAdapter adapter2 = ArrayAdapter.createFromResource(this, R.array.post_list, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        postSpinner.setAdapter(adapter2);
        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.INVISIBLE);

        userSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    getData();
                    sendData();
                    mauth = FirebaseAuth.getInstance();
                    mauth.createUserWithEmailAndPassword(emailId, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                progressBar.setVisibility(View.VISIBLE);
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = mauth.getCurrentUser();
                                Toast.makeText(getApplicationContext(), "Successfull Submit", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), MemebrLogin.class);
                                startActivity(intent);
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(getApplicationContext(), "SignUp failed",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                {
                    Toast.makeText(getApplicationContext(), "Submit failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
        UserCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                Intent intent2 = new Intent(getApplicationContext(), MemebrLogin.class);
                startActivity(intent2);
                finish();
            }
        });
    }

    //result of given action from pick image
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
                profile_photo.setImageBitmap(bitmap);
                uploadImage();//upload image to the cloud storage
            } catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
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
                            "images/" + "MembersProfilePicture/"
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
                                            url = uri.toString();
                                        }
                                    });
                                    progressDialog.dismiss();
                                    Toast
                                            .makeText(MemberForm.this,
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
                                    .makeText(MemberForm.this,
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

    public void pickImage(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
        code = 1;
    }


    private void setupUI() {
        //this Method Setup all the components of activity
        firstEdit = (EditText) findViewById(R.id.name);
        lastEdit = (EditText) findViewById(R.id.InputLastname);
        addressEdit = (EditText) findViewById(R.id.inputAddress);
        emailEdit = (EditText) findViewById(R.id.inputEmail);
        passwordEdit = (EditText) findViewById(R.id.inputPassword);
        plotHome = (TextView) findViewById(R.id.plothomeEdit);
        male_female = (RadioGroup) findViewById(R.id.radiobuttonG1);
        home_plot = (RadioGroup) findViewById(R.id.radiobuttonG2);
        male = findViewById(R.id.radioMale);
        female = findViewById(R.id.radioFemale);
        home = findViewById(R.id.radioHome);
        plot = findViewById(R.id.radioPlot);
        mobileEdit = (EditText) findViewById(R.id.InputMobile);
        plotnEdit = findViewById(R.id.inputPlotNumber);
        plotwEdit = findViewById(R.id.inputSizeWidth);
        plotdEdit = findViewById(R.id.inputSizeDepth);
        userSubmit = (Button) findViewById(R.id.submitButton);
        UserCancel = (Button) findViewById(R.id.cancelButton);
        profile_photo = (ImageView) findViewById(R.id.profile_pic);
        spinner = (Spinner) findViewById(R.id.spinnerSector);
        spinnerRole = (Spinner) findViewById(R.id.RoleSpinner);
        postSpinner = (Spinner) findViewById(R.id.PostSpinner);
    }

    private void getData() {
        //This Method get data from Ui
        firstName = firstEdit.getText().toString().trim();
        lastName = lastEdit.getText().toString().trim();
        fullName = firstName + " " + lastName;
        address = addressEdit.getText().toString();
        emailId = emailEdit.getText().toString().trim();
        password = passwordEdit.getText().toString().trim();
        UserMobileNumber = mobileEdit.getText().toString().trim();
        plot_number = plotnEdit.getText().toString().trim();
        sector = spinner.getSelectedItem().toString();
        role = spinnerRole.getSelectedItem().toString();
        post = postSpinner.getSelectedItem().toString();
        plotSize_width = Integer.parseInt(plotwEdit.getText().toString());
        plotSize_depth = Integer.parseInt(plotdEdit.getText().toString());
        area = String.valueOf(plotSize_depth + plotSize_width);
        checkType();
        checkSex();

    }

    public void checkType() {
        int selectedId = home_plot.getCheckedRadioButtonId();
        type = (RadioButton) findViewById(selectedId);
        if (selectedId <= 0) {
            plot.setError("select");
            Toast.makeText(getApplicationContext(), "Nothing selected", Toast.LENGTH_SHORT).show();
        } else {
            type_h = type.getText().toString();
        }
    }

    public void checkSex() {
        int selectedId = male_female.getCheckedRadioButtonId();
        gender = (RadioButton) findViewById(selectedId);
        if (selectedId <= 0) {//Grp is your radio group object
            female.setError("select");//Set error to last Radio button
        } else {
            sex = gender.getText().toString();
        }
    }

    private void sendData() {
        Member member = new Member(firstName + " " + lastName, address, emailId, password, sex, type_h, UserMobileNumber,
                plot_number, sector, role, post, area, url);
        myRef.child(member.getName()).setValue(member);
    }

    boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    public void onClickRadio(View view) {

        if (male.isSelected()) {
            plotHome.setText(male.getText().toString());
        } else {
            plotHome.setText(female.getText().toString());
        }

    }

    private boolean validateFirstName() {
        String val = firstEdit.getText().toString().trim();
        if (val.isEmpty()) {
            firstEdit.setError("Field not be Empty");
            return false;
        } else {
            firstEdit.setError(null);
            return true;
        }
    }

    private boolean validateLastName() {
        String val = lastEdit.getText().toString().trim();
        if (val.isEmpty()) {
            lastEdit.setError("Field not be Empty");
            return false;
        } else {
            lastEdit.setError(null);
            return true;
        }
    }

    private boolean validateMobileNumber() {
        String val = mobileEdit.getText().toString().trim();
        if (val.isEmpty()) {
            mobileEdit.setError("Field not be Empty");
            return false;
        } else {
            mobileEdit.setError(null);
            return true;
        }
    }

    private boolean validateWidth() {
        String val = plotwEdit.getText().toString().trim();
        if (val.isEmpty()) {
            plotwEdit.setError("Field not be Empty");
            return false;
        } else {
            plotwEdit.setError(null);
            return true;
        }
    }

    private boolean validateDepth() {
        String val = plotdEdit.getText().toString().trim();
        if (val.isEmpty()) {
            plotdEdit.setError("Field not be Empty");
            return false;
        } else {
            plotdEdit.setError(null);
            return true;
        }
    }

    private boolean validateAddress() {
        String val = addressEdit.getText().toString().trim();
        if (val.isEmpty()) {
            addressEdit.setError("Field not be Empty");
            return false;
        } else {
            addressEdit.setError(null);
            return true;
        }
    }

    private boolean validatePlotNumber() {
        String val = plotnEdit.getText().toString().trim();
        if (val.isEmpty()) {
            plotnEdit.setError("Field not be Empty");
            return false;
        } else {
            plotnEdit.setError(null);
            return true;
        }
    }

    private boolean validateEmail() {
        String val = emailEdit.getText().toString().trim();
        if (val.isEmpty()) {
            emailEdit.setError("Field not be Empty");
            return false;
        } else {
            emailEdit.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        String val = passwordEdit.getText().toString().trim();
        if (val.isEmpty()) {
            passwordEdit.setError("Field not be Empty");
            return false;
        } else {
            passwordEdit.setError(null);
            return true;
        }
    }

    private boolean validate() {
        return validateFirstName() && validateAddress() && validateLastName() && validateDepth()
                && validateEmail() && validateMobileNumber() && validatePlotNumber() &&
                validatePassword() && validateWidth();
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}