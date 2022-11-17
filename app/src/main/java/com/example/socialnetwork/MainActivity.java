package com.example.socialnetwork;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {
    EditText username,password;
    Button submit;
    TextInputLayout linkSignIn;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username = (EditText) findViewById(R.id.addUsername);
        password = (EditText) findViewById(R.id.editTextPassword);
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

        //check Login infos
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!username.getText().toString().equals("") && !password.getText().toString().equals("")){
                    fetchDatabase(username.getText().toString(), password.getText().toString());
                }
            }
        });
    }

    public void openFeedActivity(String username){
        Intent intent = new Intent(this,FeedActivity.class);
        intent.putExtra("username",username);
        startActivity(intent);
        finish();
    }

    public void openSignInActivity(){
        Intent intent = new Intent(this,SignInActivity.class);
        startActivity(intent);
    }

    public void fetchDatabase(String username,String password){

        db.collection("users")
                .whereEqualTo("username", username)
                .whereEqualTo("password",password)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                System.out.println("*************** ICI ***************");
                                System.out.println(document.getId() + " => " + document.getData());
                                System.out.println("*************** ICI ***************");
                                openFeedActivity(username);
                                return;
                            }

                        } else {
                            System.out.println("*************************");
                            System.out.println(task.getException());
                            System.out.println("*************************");
                        }
                    }
                });

    }




}