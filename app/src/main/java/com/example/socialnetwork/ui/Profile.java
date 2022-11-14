package com.example.socialnetwork.ui;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class Profile {
    String username;
    String []friends;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public Profile(String username){
        db.collection("users")
                .whereEqualTo("username", username)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                System.out.println("*************** ICI ***************");
                                System.out.println(document.getId() + " => " + document.getData());
                                System.out.println("*************** ICI ***************");
                            }

                        }else {
                            System.out.println("*************************");
                            System.out.println(task.getException());
                            System.out.println("*************************");
                        }
                    }
                });
    }
}
