package com.example.journalapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import util.JournalUser;

public class MainActivity extends AppCompatActivity {

    Button loginBtn;
    TextView signUpText;
    EditText editTextEmail,editTextPassword;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Users");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginBtn = findViewById(R.id.signinBTN);
        signUpText = findViewById(R.id.signupText);
        editTextEmail = findViewById(R.id.signin_email);
        editTextPassword = findViewById(R.id.signin_password);

        firebaseAuth = FirebaseAuth.getInstance();

        signUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,Signup_Activity.class);
                startActivity(i);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginEmailPasswordUser(editTextEmail.getText().toString().trim(),editTextPassword.getText().toString().trim());
            }
        });
    }

    private void LoginEmailPasswordUser(String email, String pwd) {

        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pwd)){

            firebaseAuth.signInWithEmailAndPassword(email, pwd)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()){

                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                assert user != null;
                                final String currentUserId = user.getUid();

                                collectionReference
                                        .whereEqualTo("userId",currentUserId)
                                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                            @Override
                                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                                                if(error != null){

                                                }
                                                assert value != null;
                                                if(!value.isEmpty()){

                                                    for(QueryDocumentSnapshot snapshots : value){

                                                        JournalUser journalUser = JournalUser.getInstance();
                                                        journalUser.setUsername(snapshots.getString("username"));
                                                        journalUser.setUserId(snapshots.getString("userId"));

                                                        startActivity(new Intent(MainActivity.this,JournalListActivity.class));
                                                        editTextEmail.getText().clear();
                                                        editTextPassword.getText().clear();
                                                    }
                                                }
                                            }
                                        });
                            }else {

                                Toast.makeText(MainActivity.this, "No User Found Please Check Email And Password", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }else{

            Toast.makeText(MainActivity.this, "Please Enter Email & Password", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent i = new Intent(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_HOME);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }
}