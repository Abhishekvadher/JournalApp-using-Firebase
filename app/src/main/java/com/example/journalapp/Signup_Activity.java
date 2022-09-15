package com.example.journalapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import util.JournalUser;

public class Signup_Activity extends AppCompatActivity {

    EditText signUpEmail,signUpPassword,signUpUsername;
    Button signUpBtn;
    private TextView loginBtn;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        firebaseAuth = FirebaseAuth.getInstance();
        signUpUsername = findViewById(R.id.signup_username);
        signUpEmail = findViewById(R.id.signup_email);
        signUpPassword = findViewById(R.id.signup_password);
        signUpBtn = findViewById(R.id.signupBTN);
        loginBtn = findViewById(R.id.loginText);

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentUser = firebaseAuth.getCurrentUser();
                if(currentUser != null){


                }else {


                }
            }
        };

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(signUpEmail.getText().toString())&& !TextUtils.isEmpty(signUpPassword.getText().toString())&& !TextUtils.isEmpty(signUpUsername.getText().toString())){

                    String email = signUpEmail.getText().toString().trim();
                    String password = signUpPassword.getText().toString().trim();
                    String username = signUpUsername.getText().toString().trim();
                    CreateUserEmailAccount(email,password,username);

                }else {
                    Toast.makeText(Signup_Activity.this, "Empty Fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Signup_Activity.this,MainActivity.class));
            }
        });
    }

    private void CreateUserEmailAccount(String email, String password,final String username) {

        if(!TextUtils.isEmpty(signUpEmail.getText().toString())&& !TextUtils.isEmpty(signUpPassword.getText().toString())) {

            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){

                                currentUser = firebaseAuth.getCurrentUser();
                                assert currentUser != null;
                                final String currentUserId = currentUser.getUid();

                                Map<String,String> userObj = new HashMap<>();
                                userObj.put("userId",currentUserId);
                                userObj.put("username",username);

                                collectionReference.add(userObj)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                                        if(Objects.requireNonNull(task.getResult()).exists()){

                                                            String username = task.getResult().getString("username");

                                                            JournalUser journalUser = JournalUser.getInstance();
                                                            journalUser.setUserId(currentUserId);
                                                            journalUser.setUsername(username);

                                                            Intent i = new Intent(Signup_Activity.this,AddJournalActivity.class);
                                                            i.putExtra("username",username);
                                                            i.putExtra("userId",currentUserId);
                                                            startActivity(i);
                                                        }else{


                                                        }
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {

                                                        Toast.makeText(Signup_Activity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                        });
                            }else{


                            }
                        }
                    });
        }

        }

    @Override
    protected void onStart() {
        super.onStart();

        currentUser = firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);
    }
}