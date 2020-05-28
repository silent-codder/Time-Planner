package com.example.time.Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.time.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;


public class UserRegistration extends AppCompatActivity {

    EditText name;
    EditText email;
    EditText password;
    Button birth_date;
    EditText profession;
    Button btn_submit;
    TextView login;

    Calendar calendar;
    int day,month,year;

    private FirebaseAuth auth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_registration);

        name = findViewById(R.id.first_name);
        email =  findViewById(R.id.email);
        password = findViewById(R.id.password);
        birth_date =  findViewById(R.id.birth_date);
        profession =  findViewById(R.id.profession);
        login = findViewById(R.id.login);
        btn_submit =  findViewById(R.id.btn_register);

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        calendar = Calendar.getInstance();

        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);

        month += 1;

        birth_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(UserRegistration.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {

                        month += 1;
                        birth_date.setText(day + "-" + month + "-" + year);
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserRegistration.this, UserLogin.class));
                finish();
            }
        });


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String txt_name = name.getText().toString();
                String txt_profession = profession.getText().toString();
                String txt_dob = birth_date.getText().toString();
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();

                if (TextUtils.isEmpty(txt_name) || TextUtils.isEmpty(txt_profession)
                        || TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password) || TextUtils.isEmpty(txt_dob)){
                    Toast.makeText(UserRegistration.this, "Fill up all information !!", Toast.LENGTH_SHORT).show();
                }else if (password.length() < 6){
                    Toast.makeText(UserRegistration.this, "Password too short !!", Toast.LENGTH_SHORT).show();
                }
                else {
                    registerUser(txt_name, txt_profession, txt_dob , txt_email , txt_password );
                }
            }
        });
    }

    private void registerUser(String name, String profession, String dob, String email, String password) {

        auth.createUserWithEmailAndPassword(email , password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                HashMap<String , Object> map = new HashMap<>();
                map.put("Name" , name);
                map.put("Profession" , profession);
                map.put("DOB" , dob);
                map.put("Email" , email);

                databaseReference.child("Users").child(auth.getCurrentUser().getUid()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(UserRegistration.this, "Complete Registration !!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(UserRegistration.this, MainActivity.class)
                                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            finish();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UserRegistration.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
