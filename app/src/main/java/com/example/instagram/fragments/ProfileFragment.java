package com.example.instagram.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.instagram.R;
import com.example.instagram.activities.LoginActivity;
import com.example.instagram.activities.MainActivity;
import com.example.instagram.adapters.PostGridAdapter;
import com.example.instagram.databinding.FragmentProfileBinding;
import com.example.instagram.models.Post;
import com.example.instagram.models.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";
    private User user;
    private FragmentProfileBinding fragmentProfileBinding;
    private ImageView ivProfileImg;
    private TextView tvUsername;
    private GridView gvPosts;
    private List<Post> posts;
    private PostGridAdapter adapter;
    private FloatingActionButton fabLogOut;

    public ProfileFragment() {
    }

    public static ProfileFragment newInstance(Parcelable user) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putParcelable(MainActivity.KEY_USER, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = Parcels.unwrap(getArguments().getParcelable(MainActivity.KEY_USER));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentProfileBinding = FragmentProfileBinding.inflate(inflater, container, false);
        return fragmentProfileBinding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ivProfileImg = fragmentProfileBinding.ivProfileImg;
        tvUsername = fragmentProfileBinding.tvUsername;
        gvPosts = fragmentProfileBinding.gvPosts;
        fabLogOut = fragmentProfileBinding.fabLogout;

        tvUsername.setText(user.getUsername());

        ParseFile profileImg = user.getParseFile("profileImg");
        if (profileImg != null) {
            Glide.with(getActivity()).load(profileImg.getUrl().replaceAll("http", "https")).circleCrop().into(ivProfileImg);
        } else {
            Log.e(TAG, "CANNOT FIND IMAGE");
        }

        // Allow user to update avatar by clicking on current profile picture
        ivProfileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToCompose();
            }
        });

        fabLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser.logOutInBackground(new LogOutCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null) {
                            Log.i(TAG, String.valueOf(e));
                            return;
                        }
                        Log.i(TAG, "Logged out!");
                        Toast.makeText(getContext(), "Logged out successfully", Toast.LENGTH_SHORT).show();
                        goLogIn();
                    }
                });
            }
        });

        posts = new ArrayList<>();
        adapter = new PostGridAdapter(getActivity(), posts);
        gvPosts.setAdapter(adapter);
        queryPosts();
    }

    // Go to compose & send information of current user to update avatar (instead of composing post)
    private void goToCompose() {
        ComposeFragment composeFragment = ComposeFragment.newInstance(Parcels.wrap(user));
        MainActivity.fragmentManager.beginTransaction().replace(R.id.flContainer, composeFragment).commit();
    }

    private void queryPosts() {
        MainActivity.showProgressBar();
        Log.i(TAG, "Start querying for new post in profile");

        // Set up query
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.addDescendingOrder("createdAt");
        query.whereContains("user", user.getObjectId());

        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> newPosts, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error when querying new posts");
                    return;
                }
                posts.addAll(newPosts);
                Log.e(TAG, String.valueOf(posts.size()));
                adapter.notifyDataSetChanged();
                Log.i(TAG, "Query on user " + user.getUsername() + " completed, got " + newPosts.size() + " new posts");
                MainActivity.hideProgressBar();
            }
        });
    }

    private void goLogIn() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }
}