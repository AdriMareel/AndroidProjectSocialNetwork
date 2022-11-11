package com.example.socialnetwork;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.os.Bundle;

public class SignInActivity extends AppCompatActivity {
    ActionBar actionBar = getActionBar();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);


        actionBar.setDisplayHomeAsUpEnabled(true);
    }
}