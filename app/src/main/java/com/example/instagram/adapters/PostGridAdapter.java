package com.example.instagram.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.instagram.R;
import com.example.instagram.activities.MainActivity;

import com.example.instagram.fragments.DetailFragment;
import com.example.instagram.models.Post;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.List;

public class PostGridAdapter extends BaseAdapter {

    private final String TAG = "PostGridAdapter";
    private Context context;
    private List<Post> posts;

    public PostGridAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }
    @Override
    public int getCount() {
        return posts.size();
    }

    @Override
    public Post getItem(int position) {
        return posts.get(position);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ImageView ivImage = new ImageView(context);
        ParseFile image = posts.get(position).getImg();
        if (image != null) {
            Glide.with(context).load(image.getUrl()
                    .replaceAll("http", "https"))
                    .apply(new RequestOptions().override(500, 500))
                    .into(ivImage);
        }
        ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DetailFragment detailFragment = DetailFragment.newInstance(Parcels.wrap(posts.get(position)));
                MainActivity.fragmentManager.beginTransaction().replace(R.id.flContainer, detailFragment).commit();
            }
        });
        return ivImage;
    }

}