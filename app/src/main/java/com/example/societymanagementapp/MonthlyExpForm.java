package com.example.societymanagementapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;

public class MonthlyExpForm extends AppCompatActivity implements
        View.OnClickListener {

    DatePickerDialog picker;
    EditText name,amount,date,description;
    Button submit,cancel;
    RadioGroup option;
    Window window;
    RadioButton recur,oneTime,opts;
    FirebaseDatabase database;
    FirebaseStorage storage;
    StorageReference storageReference;
    DatabaseReference myref;
    private int mYear, mMonth, mDay, mHour, mMinute;
    String opt,exName,exAmount,exDate,exDescrip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_exp_form);
        if (Build.VERSION.SDK_INT>=21){
            window=this.getWindow();
            window.setStatusBarColor(this.getResources().getColor(R.color.primaryDark));
        }
        setTitle("Monthly Expenses");
        setupUI();
        database=FirebaseDatabase.getInstance();
        myref=database.getReference().child("monthlyExpenses");




        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate()) {
                    getData();
                    ExpModel model = new ExpModel(opt, exName, exAmount, exDate, exDescrip);
                    myref.child(exName).setValue(model);
                    Toast.makeText(getApplicationContext(), "Submitted", Toast.LENGTH_LONG).show();
                }
                }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent=new Intent(getApplicationContext(),Dashboard.class);
                startActivity(intent);
            }
        });

    }
    public void checkOption() {
        int selectedId = option.getCheckedRadioButtonId();
        opts= (RadioButton) findViewById(selectedId);
        if (selectedId <= 0) {
            oneTime.setError("select");
            Toast.makeText(getApplicationContext(), "Nothing selected", Toast.LENGTH_SHORT).show();
        } else {
            opt = opts.getText().toString();
        }
    }
    public void getData(){
         exName=name.getText().toString();
         exAmount=amount.getText().toString();
         exDate=date.getText().toString();
         exDescrip=description.getText().toString();
       checkOption();
    }
    public void setupUI()
    {
        name=(EditText)findViewById(R.id.meName);
        amount=(EditText)findViewById(R.id.meAmount);
        date=(EditText)findViewById(R.id.meDate);
        description=(EditText)findViewById(R.id.meDescription);
        submit=(Button)findViewById(R.id.Submit_me);
        cancel=(Button)findViewById(R.id.cancel_me);
        option=(RadioGroup)findViewById(R.id.RG_me);
        recur=(RadioButton)findViewById(R.id.RecurRB);
        oneTime=(RadioButton)findViewById(R.id.oneTimeRB);
    }
    private boolean validateName() {
        String val = name.getText().toString().trim();
        if (val.isEmpty()) {
            name.setError("Enter Email");
            return false;
        } else {
            name.setError(null);
            return true;
        }
    }
    private boolean validateAmount() {
        String val = amount.getText().toString().trim();
        if (val.isEmpty()) {
            amount.setError("Enter Email");
            return false;
        } else {
            amount.setError(null);
            return true;
        }
    }
    private boolean validateDate() {
        String val = date.getText().toString().trim();
        if (val.isEmpty()) {
            date.setError("Enter Email");
            return false;
        } else {
            date.setError(null);
            return true;
        }
    }
    private boolean validateDes() {
        String val = description.getText().toString().trim();
        if (val.isEmpty()) {
            description.setError("Enter Email");
            return false;
        } else {
            description.setError(null);
            return true;
        }
    }
    public boolean validate(){
        return validateName() && validateAmount() && validateDate() && validateDes();
    }

    @Override
    public void onClick(View view) {
        if (view == date) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

                //rest of the code

                DatePickerDialog datePickerDialog = new DatePickerDialog(this,R.style.DialogTheme,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
    }
}