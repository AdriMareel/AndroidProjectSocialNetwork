package com.example.socialnetwork;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SignInActivity extends AppCompatActivity {
    EditText username,password;
    Button submit;
    TextInputLayout linkSignIn;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        username = (EditText) findViewById(R.id.editTextUsername);
        password = (EditText) findViewById(R.id.editTextPassword);
        submit = findViewById(R.id.submitButton);


        submit.setOnClickListener(new View.OnClickListener() {
            boolean stop = false;

            @Override
            public void onClick(View view) {
                if(!username.getText().toString().equals("") && !password.getText().toString().equals("")){

                    //check si username déjà dans la db
                    db.collection("users")
                            .whereEqualTo("username", username.getText().toString())
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    //username déjà pris
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            Toast usernameErrorToast = Toast.makeText(getApplicationContext(),"Le nom d'utilisateur renseigné est déjà utilisé, veuillez réessayer.",Toast.LENGTH_LONG);
                                            usernameErrorToast.show();
                                            stop = true;
                                        }

                                    } else {
                                        System.out.println("*************************");
                                        System.out.println(task.getException());
                                        System.out.println("*************************");
                                    }
                                }
                            });

                    if(stop){
                        return;
                    }

                    System.out.println("*************************");
                    System.out.println(stop);
                    System.out.println("*************************");


                    //push data into database
                    if(!stop) {
                        ArrayList<String> followers = new ArrayList<>();
                        ArrayList<String> following = new ArrayList<>();
                        Map<String, Object> data = new HashMap<>();
                        data.put("password", password.getText().toString());
                        data.put("username", username.getText().toString());
                        data.put("followers", followers);
                        data.put("following", following);

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
                        openNavigationActivity();
                    }

                }

                else{
                    System.out.print("Veuillez rensigner votre username et mdp");
                }
            }
        });

    }

    public void openNavigationActivity(){
        Intent intent = new Intent(this,NavigationActivity.class);
        startActivity(intent);
        finish();
    }

}