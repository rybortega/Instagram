package com.example.instagram.adapters;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.instagram.databinding.ItemPostBinding;

import com.example.instagram.models.Post;
import com.parse.ParseFile;

import org.w3c.dom.Text;

import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    public interface OnClickListener {
        void onClickListener(int position);
    }

    private final String TAG = "PostsAdapter";
    private Context context;
    private List<Post> posts;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemPostBinding itemPostBinding = ItemPostBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(itemPostBinding);
    }

    public PostsAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    // Clean all elements of the recycler
    public void clear() {
        posts.clear();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Post> list) {
        posts.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivProfileImage;
        TextView tvDescription;
        TextView tvUsername;
        ImageView ivImage;
        ImageView ivLike;
        ImageView ivComment;
        ImageView ivShare;
        ImageView ivSave;

        public ViewHolder(@NonNull ItemPostBinding itemPostBinding) {
            super(itemPostBinding.getRoot());
            ivProfileImage = itemPostBinding.ivProfileImg;
            tvUsername = itemPostBinding.tvUsername;
            tvDescription = itemPostBinding.tvDescription;
            ivImage = itemPostBinding.ivImg;
            ivLike = itemPostBinding.ivLike;
            ivComment = itemPostBinding.ivComment;
            ivShare = itemPostBinding.ivShare;
            ivSave = itemPostBinding.ivSave;
        }

        public void bind(final Post post) {
            tvUsername.setText(post.getUser().getUsername());
            tvDescription.setText(post.getDescription());
            ParseFile image = post.getImg();
            if (image != null) {
                Glide.with(context).load(image.getUrl().replaceAll("http", "https")).into(ivImage);
            }
        }
    }
}