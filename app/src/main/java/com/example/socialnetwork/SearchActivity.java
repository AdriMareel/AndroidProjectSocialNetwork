package com.example.socialnetwork;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class SearchActivity extends AppCompatActivity {

    String username;
    TextView textView;
    Button submit;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //get profile's username
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            username = extras.getString("username");
        }

        textView = findViewById(R.id.addUsername);
        submit = findViewById(R.id.addButton);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!textView.getText().toString().equals("")){
                    addUser(username,textView.getText().toString());
                }
                else{
                    Toast.makeText(getApplicationContext(),"Le champ est vide",Toast.LENGTH_SHORT).show();
                }
            }
        });


        // Initialize and assign variable
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottomNavigationView);

        // Set Home selected
        bottomNavigationView.setSelectedItemId(R.id.search);

        // Perform item selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId())
                {
                    case R.id.create:
                        startActivity(new Intent(getApplicationContext(),PostActivity.class).putExtra("username",username));
                        overridePendingTransition(0,0);
                        finish();
                        return true;
                    case R.id.feed:
                        startActivity(new Intent(getApplicationContext(),FeedActivity.class).putExtra("username",username));
                        overridePendingTransition(0,0);
                        finish();
                        return true;
                    case R.id.search:
                        startActivity(new Intent(getApplicationContext(),SearchActivity.class).putExtra("username",username));
                        overridePendingTransition(0,0);
                        finish();
                        return true;
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(),ProfileActivity.class).putExtra("username",username));
                        overridePendingTransition(0,0);
                        finish();
                        return true;
                }
                return false;
            }
        });
    }

    public void addUser(String currentUser, String userToAdd) {

        DocumentReference docRef = db.collection("users").document(currentUser);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                List<String> following = (List<String>) task.getResult().get("following");
                following.add(userToAdd);
                db.collection("users").document(currentUser).update("following", following).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
            }
        });

        DocumentReference docRef2 = db.collection("users").document(userToAdd);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                List<String> followers = (List<String>) task.getResult().get("followers");
                followers.add(currentUser);
                db.collection("users").document(userToAdd).update("followers", followers).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(),"Vous suivez désormais "+ userToAdd, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


        //DocumentReference docRef2 = db.collection("users").document(currentUser);
        //List<String> followers = (List<String>) docRef.get().getResult().get("following");
        //followers.add(currentUser);
        //db.collection("users").document(currentUser).update("followers", followers);

    }
}