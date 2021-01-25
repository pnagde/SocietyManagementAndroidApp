package com.example.societymanagementapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SuperAdmin extends AppCompatActivity {
    TextInputEditText nametxt,emailtxt,mobiletxt,passwordtxt;
    Button submit,cancel;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_admin);
        nametxt=(TextInputEditText)findViewById(R.id.fullnameAdmin);
        emailtxt=(TextInputEditText)findViewById(R.id.emailIdAdmin);
        mobiletxt=(TextInputEditText)findViewById(R.id.mobileAdmin);
        passwordtxt=(TextInputEditText)findViewById(R.id.passwordAdmin);
        submit=(Button)findViewById(R.id.adminButton);
        cancel=(Button)findViewById(R.id.adminButtonCancel);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate()){
                    String email=emailtxt.getText().toString().trim();
                    String password=passwordtxt.getText().toString().trim();
                    auth=FirebaseAuth.getInstance();
                    auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getApplicationContext(), "Successfull Submit", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SuperAdmin.this, MemebrLogin.class);
                                startActivity(intent);
                                finish();
                            }
                            else
                            {
                                Toast.makeText(SuperAdmin.this,"User already Exists",Toast.LENGTH_LONG);
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(SuperAdmin.this,"Registration Failed",Toast.LENGTH_LONG).show();
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SuperAdmin.this, MemebrLogin.class);
                startActivity(intent);
            }
        });
    }
   public boolean validateName(){
       String val = nametxt.getText().toString().trim();
       if (val.isEmpty()) {
           nametxt.setError("Enter Name");
           return false;
       } else {
           nametxt.setError(null);
           return true;
       }
   }
    private boolean validateEmail() {
        String val = emailtxt.getText().toString().trim();
        if (val.isEmpty()) {
            emailtxt.setError("Enter Email");
            return false;
        } else {
            emailtxt.setError(null);
            return true;
        }
    }
    private boolean validateMobile() {
        String val = mobiletxt.getText().toString().trim();
        if (val.isEmpty()) {
            mobiletxt.setError("Enter Mobile");
            return false;
        } else {
            mobiletxt.setError(null);
            return true;
        }
    }
    private boolean validatePassword() {
        String val = passwordtxt.getText().toString().trim();
        if (val.isEmpty()) {
            passwordtxt.setError("Enter password");
            return false;
        }else if(val.length()<8) {
            passwordtxt.setError("should be 8 length alphahnumeric word");
            return false;
        } else {
            passwordtxt.setError(null);
            return true;
        }
    }
    private boolean validate(){
        return validateName() & validateEmail()  & validateMobile() & validatePassword();
    }
}