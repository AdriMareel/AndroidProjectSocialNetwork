package com.example.socialnetwork;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter <MyAdapter.MyViewHolder> {

    Context context;
    ArrayList<Post> list;
    FirebaseStorage storage = FirebaseStorage.getInstance();

    public MyAdapter(Context context, ArrayList<Post> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Post post = list.get(position);
        holder.username.setText(post.getPostUsername());
        holder.content.setText(post.getPostContent());
        holder.creationDate.setText(post.getPostDate());
        Picasso.get().load(post.getPostImageUrl()).resize(1165,0).into(holder.postImage);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView username, content,creationDate;
        ImageView postImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);


            username = itemView.findViewById(R.id.tvUsername);
            content = itemView.findViewById(R.id.tvContent);
            creationDate=itemView.findViewById(R.id.tvDate);
            postImage = itemView.findViewById(R.id.ivImage);


        }
    }


}
