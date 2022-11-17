package com.example.socialnetwork;

import static java.lang.Math.round;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileActivity extends AppCompatActivity {


    String username;
    RecyclerView recyclerView;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    MyAdapter myAdapter;
    ArrayList<Post> list;
    ArrayList<String> followers = new ArrayList<>();
    ArrayList<String> following = new ArrayList<>();

    ImageView avatarIv,bgIv;
    TextView nameIv,nbFollowersIv,nbFollowingIv,descriptionIv;
    ImageView img;
    int imgRes = 0;

    int KTC = (int) 273.15;

    public static String BaseUrl = "https://api.openweathermap.org/";
    public static String AppId = "da4f60186a7a5cacdbffe1153ac1d6eb";
    public static String lat = "50.6088047";
    public static String lon = "3.0357831";

    private TextView weatherData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //get profile's username
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            username = extras.getString("username");
        }

        weatherData = findViewById(R.id.textView);
        img = findViewById(R.id.img);


        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrentData();
            }
        });

        //get data
        nameIv = findViewById(R.id.nameIv);
        nbFollowersIv = findViewById(R.id.nbFollowers);
        nbFollowingIv = findViewById(R.id.nbFollowing);

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
                                followers = (ArrayList<String>) document.getData().get("followers");
                                following = (ArrayList<String>) document.getData().get("followers");
                                return;
                            }

                        }else {
                            System.out.println("*************************");
                            System.out.println(task.getException());
                            System.out.println("*************************");
                        }
                    }
                });

        nameIv.setText(username);
        nbFollowersIv.setText(Integer.toString(followers.size()));
        nbFollowingIv.setText(Integer.toString(following.size()));

        recyclerView = findViewById(R.id.profilePostList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        myAdapter = new MyAdapter(this, list);
        recyclerView.setAdapter(myAdapter);

        db.collection("posts")
                .whereEqualTo("Creator Username", username)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                System.out.println("ON EST LA OUAIS CA MARCHE 2");
                                System.out.println(document.getData());
                                Post post = new Post();
                                post.postUsername = (String) document.getData().get("Creator Username");
                                post.postContent = (String) document.getData().get("description");
                                post.postTitle = (String) document.getData().get("title");
                                post.postDate = (String) document.getData().get("Date of post");
                                post.postImageUrl= (String) document.getData().get("imageUrl");
                                list.add(post);

                                System.out.println(post.postTitle);
                            }
                        }
                        myAdapter.notifyDataSetChanged();
                    }
                });

        // Initialize and assign variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // Set Home selected
        bottomNavigationView.setSelectedItemId(R.id.profile);

        // Perform item selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.create:
                        startActivity(new Intent(getApplicationContext(), PostActivity.class).putExtra("username", username));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    case R.id.feed:
                        startActivity(new Intent(getApplicationContext(), FeedActivity.class).putExtra("username", username));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    case R.id.search:
                        startActivity(new Intent(getApplicationContext(), SearchActivity.class).putExtra("username", username));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class).putExtra("username", username));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                }
                return false;
            }
        });
    }

    void getCurrentData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        WeatherService service = retrofit.create(WeatherService.class);
        Call<WeatherResponse> call = service.getCurrentWeatherData(lat, lon, AppId);
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(@NonNull Call<WeatherResponse> call, @NonNull Response<WeatherResponse> response) {
                if (response.code() == 200) {
                    WeatherResponse weatherResponse = response.body();
                    assert weatherResponse != null;

                    String stringBuilder =
                            "Temperature: " +
                            round(weatherResponse.main.temp - KTC) +
                            "\n" +
                            "MIN: " +
                            round(weatherResponse.main.temp_min - KTC)  +
                            "\n" +
                            "MAX: " +
                            round(weatherResponse.main.temp_max - KTC) +
                            "\n" +
                            "Humidity: " +
                            weatherResponse.main.humidity +
                            "\n";

                    weatherData.setText(stringBuilder);
                    //Change the image when the imageView is touched
                    if ( round(weatherResponse.main.temp - KTC) >= 10) {
                        imgRes = R.drawable.sun;
                    } else if (round(weatherResponse.main.temp - KTC) <= 10) {
                        imgRes = R.drawable.neige;
                    img.setImageResource(imgRes);


                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<WeatherResponse> call, @NonNull Throwable t) {
                weatherData.setText(t.getMessage());
            }
        });
    }

}