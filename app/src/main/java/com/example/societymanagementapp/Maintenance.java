package com.example.societymanagementapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Maintenance extends AppCompatActivity {
    EditText maintenanceName;
    RadioButton fix,perSqrFit,status;
    RadioGroup options;
    Button submit,cancel;
    private String name,option;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance);
        setTitle("Maintenance Form");
        firebaseDatabase=FirebaseDatabase.getInstance();
        reference=firebaseDatabase.getReference().child("Maintenance");
        maintenanceName=(EditText)findViewById(R.id.mainName);
        fix=(RadioButton)findViewById(R.id.fix);
        perSqrFit=(RadioButton)findViewById(R.id.RBM);
        options=(RadioGroup)findViewById(R.id.RBG);
        submit=(Button)findViewById(R.id.submitM);
        cancel=(Button)findViewById(R.id.CancelM);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),Dashboard.class);
                startActivity(intent);
                finish();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateName() & checkOption()) {
                    name = maintenanceName.getText().toString();
                    perSqrFit.setError(null);
                    ModelMaintenance modelMaintenance = new ModelMaintenance(name, option);
                    reference.child(modelMaintenance.getName()).setValue(modelMaintenance);
                    Toast.makeText(getApplicationContext(),"Submitted",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public boolean checkOption() {
        int selectedId = options.getCheckedRadioButtonId();
        status= (RadioButton) findViewById(selectedId);
        if (selectedId <= 0) {
            perSqrFit.setError("select");
            Toast.makeText(getApplicationContext(), "Nothing selected", Toast.LENGTH_SHORT).show();
        return false;
        } else {
            option = status.getText().toString();
            perSqrFit.setError(null);
        return true;
        }
    }
    private boolean validateName() {
        String val = maintenanceName.getText().toString().trim();
        if (val.isEmpty()) {
            maintenanceName.setError("Enter Name");

            return false;
        } else {
            maintenanceName.setError(null);

            return true;
        }
    }
}