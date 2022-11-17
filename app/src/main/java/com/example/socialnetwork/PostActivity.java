package com.example.socialnetwork;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class PostActivity extends Activity {

    private String username;
    private Button sendButton;
    private EditText content;
    private EditText title;
    private ImageButton postImg;

    private static final int gallery_Pick=1;
    private Uri imageUrl;
    private String description;

    private StorageReference postImgRef;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private String sCurrentDate, sCurrentTime,randomName;
    private String dlUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        //get profile's username
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            username = extras.getString("username");
        }

        postImgRef= FirebaseStorage.getInstance().getReference();

        //initVar
        sendButton=(Button) findViewById(R.id.sendButton);
        content=(EditText) findViewById(R.id.postDescription);
        title=(EditText) findViewById(R.id.postTitle);
        postImg=(ImageButton) findViewById(R.id.postImg);

        //onClickListener pour ajouter une image
        postImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenGallery();
            }
        });

        //vérifications des infos du post avant de l'envoyer
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValidatePostInfo();
            }
        });

        // Initialize and assign variable
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottomNavigationView);

        // Set Home selected
        bottomNavigationView.setSelectedItemId(R.id.create);

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

    private void ValidatePostInfo() {
        description= content.getText().toString();
        String sTitle=title.getText().toString();
        //verif que l'utilisateur a mit une image ou du texte

        if(imageUrl==null &&TextUtils.isEmpty(description)){
            Toast.makeText(this,"Add an image or some text",Toast.LENGTH_SHORT).show();
        }
        //de meme pour le titre
        else if(TextUtils.isEmpty(sTitle)){
            Toast.makeText(this,"Add a title",Toast.LENGTH_SHORT).show();
        }
        else if(imageUrl==null && !TextUtils.isEmpty(description)){
            pushToDatabase();
        }
        else{
            StoringImageToFireBase();
        }

    }

    private void StoringImageToFireBase() {
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
        sCurrentDate = currentDate.format(calForDate.getTime());

        Calendar calForTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
        sCurrentTime = currentTime.format(calForDate.getTime());

        randomName = sCurrentDate + sCurrentTime;

        StorageReference filePath = postImgRef.child("Post Images").child(randomName + imageUrl.getLastPathSegment());
        filePath.putFile(imageUrl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Uri downloadUrl = uri;
                        System.out.println(downloadUrl);
                        dlUrl=uri.toString();
                        pushToDatabase();

                    }
                });
            }



        });
    }

    private void pushToDatabase() {
        //push data into database
        Map<String, Object> data = new HashMap<>();
        String usernamePost = username;
        data.put("title", title.getText().toString());
        data.put("description",content.getText().toString());
        data.put("imageUrl",dlUrl);
        data.put("Creator Username",usernamePost);
        data.put("Date of post",sCurrentDate);
        db.collection("posts").add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
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
    }

    private void OpenGallery() {
        Intent galleryIntent=new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,gallery_Pick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==gallery_Pick && resultCode==RESULT_OK && data!=null){
            imageUrl=data.getData();
            postImg.setImageURI(imageUrl);
        }
    }
}