package com.example.instagram.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.instagram.R;
import com.example.instagram.activities.MainActivity;
import com.example.instagram.databinding.ItemCommentBinding;
import com.example.instagram.databinding.ItemPostBinding;

import com.example.instagram.fragments.DetailFragment;
import com.example.instagram.fragments.ProfileFragment;
import com.example.instagram.models.Comment;
import com.example.instagram.models.Post;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {

    private final String TAG = "CommentsAdapter";
    private Context context;
    private List<Comment> comments;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemCommentBinding itemCommentBinding = ItemCommentBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(itemCommentBinding);
    }

    public CommentsAdapter(Context context, List<Comment> comments) {
        this.context = context;
        this.comments = comments;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comment comment = comments.get(position);
        try {
            holder.bind(comment);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvUsernameComment;
        TextView tvComment;

        public ViewHolder(@NonNull ItemCommentBinding itemCommentBinding) {
            super(itemCommentBinding.getRoot());
            tvUsernameComment = itemCommentBinding.tvUsernameComment;
            tvComment = itemCommentBinding.tvComment;
        }

        public void bind(final Comment comment) throws ParseException {
            tvUsernameComment.setText(comment.getUser());
            tvComment.setText(comment.getContent());
        }
    }

}