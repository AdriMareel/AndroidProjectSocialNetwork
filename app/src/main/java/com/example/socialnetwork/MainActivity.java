package com.example.socialnetwork;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

public class MainActivity extends AppCompatActivity {
    EditText username,password;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username = (EditText) findViewById(R.id.editTextUsername);
        password = (EditText) findViewById(R.id.editTextPassword);

        //Listener sur le bouton submit
        submit = findViewById(R.id.submitButton);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //cas où il n'y a pas d'erreur de connexion
                if(!username.getText().toString().equals("") && !password.getText().toString().equals("")){
                    System.out.println("------------------------");
                    System.out.println(username.getText().toString());
                    System.out.println(password.getText().toString());
                    System.out.println("------------------------");

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
        //Intent intent = new Intent(this,HomeActivity.class);
        //startActivity(intent);

        Intent intent = new Intent(this,NavigationActivity.class);
        startActivity(intent);
        finish();
    }




}