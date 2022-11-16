package com.example.socialnetwork.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.socialnetwork.Post;
import com.example.socialnetwork.R;

import java.util.List;

public class PostAdapter extends BaseAdapter {
    private final List<Post> postList;
    private final LayoutInflater inflater=LayoutInflater.from(context);
    public View view=inflater.inflate(R.layout.fragment_home,null);

    public PostAdapter(List<Post> postList) {
        this.postList = postList;
    }

    @Override
    public int getCount() {
        return null!=postList ?postList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return null != postList ? postList.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View view = inflater.inflate(R.layout.post_list,null);

        final  Post post=(Post) 

        return view;
    }
}
