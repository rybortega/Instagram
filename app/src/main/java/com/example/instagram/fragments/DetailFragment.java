package com.example.instagram.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.instagram.DoubleClickListener;
import com.example.instagram.R;
import com.example.instagram.activities.MainActivity;
import com.example.instagram.adapters.CommentsAdapter;
import com.example.instagram.adapters.PostsAdapter;
import com.example.instagram.databinding.FragmentDetailBinding;
import com.example.instagram.databinding.FragmentNewsfeedBinding;
import com.example.instagram.models.Comment;
import com.example.instagram.models.Post;
import com.example.instagram.models.User;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.parceler.Parcels;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DetailFragment extends Fragment {

    private static final String TAG = "DetailFragment";
    private Post post;
    private FragmentDetailBinding fragmentDetailBinding;

    ImageView ivProfileImage;
    TextView tvDescription;
    TextView tvUsername;
    ImageView ivImage;
    ImageView ivLike;
    ImageView ivComment;
    ImageView ivShare;
    TextView tvUsernameDescription;
    TextView tvNumLike;
    TextView tvNumComment;
    TextView tvTimestamp;
    RecyclerView rvComments;
    CommentsAdapter adapter;
    List<Comment> comments;
    LinearLayoutManager linearLayoutManager;
    EditText etComment;
    Button btnComment;
    User user;

    public DetailFragment() {
    }

    public static DetailFragment newInstance(Parcelable post) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(MainActivity.KEY_POST, post);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            post = Parcels.unwrap(getArguments().getParcelable(MainActivity.KEY_POST));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentDetailBinding = FragmentDetailBinding.inflate(inflater, container, false);
        return fragmentDetailBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ivProfileImage = fragmentDetailBinding.ivProfileImg;
        tvUsername = fragmentDetailBinding.tvUsername;
        tvDescription = fragmentDetailBinding.tvDescription;
        ivImage = fragmentDetailBinding.ivImg;
        ivLike = fragmentDetailBinding.ivLike;
        ivComment = fragmentDetailBinding.ivComment;
        ivShare = fragmentDetailBinding.ivShare;
        tvTimestamp = fragmentDetailBinding.tvTimeStamp;
        tvUsernameDescription = fragmentDetailBinding.tvUsernameDescription;
        tvNumLike = fragmentDetailBinding.tvNumLike;
        rvComments = fragmentDetailBinding.rvComment;
        etComment = fragmentDetailBinding.edComment;
        btnComment = fragmentDetailBinding.btnComment;
        user = (User) ParseUser.getCurrentUser();

        try {
            setUpViews();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        comments = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(getActivity());
        adapter = new CommentsAdapter(getActivity(), comments);
        rvComments.setLayoutManager(linearLayoutManager);
        rvComments.setAdapter(adapter);

        queryComments();
    }

    private void setUpViews() throws ParseException {
        tvUsername.setText(post.getUser().getUsername());
        tvDescription.setText(post.getDescription());
        tvTimestamp.setText(post.getRelativeTime());
        tvUsernameDescription.setText(post.getUser().getUsername());

        ParseFile image = post.getImg();
        if (image != null) {
            Glide.with(this).load(image.getUrl().replaceAll("http", "https")).into(ivImage);
        }
        ParseFile profileImg = post.getUser().getParseFile("profileImg");
        if (profileImg != null) {
            Glide.with(this).load(profileImg.getUrl().replaceAll("http", "https")).circleCrop().into(ivProfileImage);
        }

        // Set up like-related views
        updateLike();
        updateNumLike();
        ivLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    post.attemptToLike(ParseUser.getCurrentUser());
                    updateLike();
                    updateNumLike();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        // Allow user to comment via comment button
        ivComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etComment.requestFocus();
            }
        });

        // Update save-related views
        updateSaved();
        ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    user.attemptToSave(post);
                    updateSaved();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        // Profile image leads to profile fragment
        ivProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    goToProfile();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        // Username leads to profile fragment
        tvUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    goToProfile();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        // Post input comment when comment button is clicked
        btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String comment = etComment.getText().toString();
                post.postComment(comment, getActivity());
                etComment.setText("");
                queryComments();
            }
        });

        // Allow user to like with double click
        ivImage.setOnClickListener(new DoubleClickListener() {
            @Override
            public void onSingleClick(View v) {
            }

            @Override
            public void onDoubleClick(View v) {
                try {
                    post.attemptToLike(ParseUser.getCurrentUser());
                    updateLike();
                    updateNumLike();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void updateSaved() throws ParseException {
        if (user.postSaved(post)) {
            ivShare.setImageResource(R.drawable.nav_inbox_paper_plane);
        } else {
            ivShare.setImageResource(R.drawable.direct);
        }
    }

    private void updateLike() throws ParseException {
        if (post.userLiked(ParseUser.getCurrentUser())) {
            ivLike.setImageResource(R.drawable.ufi_heart_active);
        } else {
            ivLike.setImageResource(R.drawable.ufi_heart);
        }
    }

    private void updateNumLike() throws ParseException {
        int numLike = post.getNumLike();
        if (numLike > 1) {
            tvNumLike.setText("" + numLike + " likes");
        } else {
            tvNumLike.setText("" + numLike + " like");
        }
    }

    private void queryComments() {
        Log.i(TAG, "Start querying for new comments");

        // Set up query
        ParseQuery<Comment> query = ParseQuery.getQuery(Comment.class);
        query.include(Post.KEY_USER);
        query.whereContains("post", post.getObjectId());
        query.addDescendingOrder("createdAt");

        query.findInBackground(new FindCallback<Comment>() {
            @Override
            public void done(List<Comment> newComments, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error when querying new comments", e);
                    return;
                }
                comments.clear();
                comments.addAll(newComments);
                Log.e(TAG, String.valueOf(comments.size()));
                adapter.notifyDataSetChanged();
                Log.i(TAG, "Query completed, got " + newComments.size() + " comments");
            }
        });
    }

    private void goToProfile() throws ParseException {
        ProfileFragment profileFragment = ProfileFragment.newInstance(Parcels.wrap(post.getUser().fetch()));
        MainActivity.fragmentManager.beginTransaction().replace(R.id.flContainer, profileFragment).commit();
    }
}