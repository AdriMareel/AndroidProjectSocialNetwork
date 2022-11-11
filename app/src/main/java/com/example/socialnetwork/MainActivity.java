package com.example.socialnetwork;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    EditText username,password;
    Button submit;
    TextInputLayout linkSignIn;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username = (EditText) findViewById(R.id.editTextUsername);
        password = (EditText) findViewById(R.id.editTextPassword);




        //Listeners
        submit = findViewById(R.id.submitButton);
        linkSignIn = findViewById(R.id.til_signin);


        //redirect sign in
        linkSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open Sign In Activity and close the main one
                openSignInActivity();
            }
        });




        //push to database
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //cas où il n'y a pas d'erreur de connexion
                if(!username.getText().toString().equals("") && !password.getText().toString().equals("")){

                    //push data into database
                    Map<String, Object> data = new HashMap<>();
                    data.put("password", password.getText().toString());
                    data.put("username",username.getText().toString());

                    db.collection("users").add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding document", e);
                            }
                        });


                    System.out.println("********* PUSH DATABASE *************");

                    //switching activity
                    openHomeActivity();

                }

                else{
                    System.out.print("Veuillez rensigner votre username et mdp");
                }
            }
        });
    }

    public void openHomeActivity(){
        Intent intent = new Intent(this,NavigationActivity.class);
        startActivity(intent);
        finish();
    }

    public void openSignInActivity(){
        Intent intent = new Intent(this,SignInActivity.class);
        startActivity(intent);
    }




}