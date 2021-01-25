package com.example.societymanagementapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MemebrLogin extends AppCompatActivity {
    Button buttonLogin;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("members");
    private FirebaseAuth firebaseAuth;
    private String email, password;
    TextInputEditText emailtxt;
    TextInputEditText passwordtxt;
    ProgressBar progressBar;
    Window window;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memebr_login);
        if (Build.VERSION.SDK_INT>=21){
            window=this.getWindow();
            window.setStatusBarColor(this.getResources().getColor(R.color.primaryDark));
        }
        emailtxt = (TextInputEditText) findViewById(R.id.emailPass);
        passwordtxt=(TextInputEditText)findViewById(R.id.loginPass);
        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.INVISIBLE);


        firebaseAuth = FirebaseAuth.getInstance();

        buttonLogin = findViewById(R.id.loginBtn);
        setTitle("LOGIN");
    }

    @Override
    protected void onStart() {
        super.onStart();
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = emailtxt.getText().toString().trim();
                password = passwordtxt.getText().toString().trim();

                if (validation()) {
                    progressBar.setVisibility(View.VISIBLE);
                    firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Login Successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MemebrLogin.this, Dashboard.class);
                                startActivity(intent);
                                finish();
                            } else {
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(getApplicationContext(), "User not valid", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
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
    private boolean validatePassword() {
        String val = passwordtxt.getText().toString().trim();
        if (val.isEmpty()) {
            passwordtxt.setError("Enter Password");
            return false;
        }
        else if(val.length()<8) {
        passwordtxt.setError("password length must be 8");
        return false;
        }else {
            passwordtxt.setError(null);
            return true;
        }
    }

    private boolean validation() {
        return validateEmail() && validatePassword();
    }

}