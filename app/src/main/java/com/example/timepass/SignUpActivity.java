package com.example.timepass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.timepass.Model.UserInformation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    private EditText etEmail;
    private EditText etPassword;
    private EditText etName;
    private EditText etMobile;

    private Button btnRegister;

    private static final String TAG="SignUpActivity";

    private FirebaseDatabase userInfoDatabase;
    private DatabaseReference reference;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);



        etEmail=(EditText)findViewById(R.id.etEmail);
        etPassword=(EditText)findViewById(R.id.etPassword);
        etMobile=(EditText)findViewById(R.id.etNumber);
        etName=(EditText)findViewById(R.id.etName);
        btnRegister=(Button)findViewById(R.id.btnSignUp);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authenticationListner();
                startActivity(new Intent(SignUpActivity.this,MainActivity.class));
                finish();
            }
        });

        userInfoDatabase=FirebaseDatabase.getInstance();
        reference=userInfoDatabase.getReference("Users");
        mAuth=FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser();
    }

    private void authenticationListner(){
        final String email=etEmail.getText().toString();
        final String password=etPassword.getText().toString();

        if(TextUtils.isEmpty(email)&&(TextUtils.isEmpty(password))){
            Toast.makeText(this,"Fill all the fields",Toast.LENGTH_SHORT).show();
        }
        else{
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    UserInformation userInformation=new UserInformation();
                    userInformation.setEmail(email);
                    userInformation.setPassword(password);
                    userInformation.setNumber(etMobile.getText().toString());
                    userInformation.setName(etName.getText().toString());
                    userInformation.setRupees(0);
                    userInformation.setPoints(0);

                    reference.child(currentUser.getUid()).setValue(userInformation).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(SignUpActivity.this,"Successfully Registered",Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SignUpActivity.this,"ERROR"+e.getMessage(),Toast.LENGTH_SHORT).show();

                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(SignUpActivity.this,"ERROR"+e.getMessage(),Toast.LENGTH_SHORT).show();

                }
            });
        }
    }
}
