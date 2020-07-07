package com.example.instagram.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.instagram.R;
import com.example.instagram.activities.MainActivity;
import com.example.instagram.adapters.PostGridAdapter;
import com.example.instagram.adapters.PostsAdapter;
import com.example.instagram.databinding.FragmentProfileBinding;
import com.example.instagram.models.Post;
import com.example.instagram.models.User;
import com.parse.ParseFile;
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
    private LinearLayoutManager linearLayoutManager;
    private PostGridAdapter adapter;

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

        tvUsername.setText(user.getUsername());

        ParseFile profileImg = ParseUser.getCurrentUser().getParseFile("profileImg");
        if (profileImg != null) {
            Glide.with(getActivity()).load(profileImg.getUrl().replaceAll("http", "https")).into(ivProfileImg);
        } else {
            Log.e(TAG, "CANNOT FIND IMAGE");
        }

        posts = user.getPosts();
        Log.e(TAG, posts.toString());
        linearLayoutManager = new LinearLayoutManager(getActivity());
        adapter = new PostGridAdapter(getActivity(), posts);
        gvPosts.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}