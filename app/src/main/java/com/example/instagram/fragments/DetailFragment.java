package com.example.instagram.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.instagram.R;
import com.example.instagram.activities.MainActivity;
import com.example.instagram.databinding.FragmentDetailBinding;
import com.example.instagram.databinding.FragmentNewsfeedBinding;
import com.example.instagram.models.Post;
import com.parse.ParseFile;

import org.parceler.Parcels;

public class DetailFragment extends Fragment {

    private Post post;
    private FragmentDetailBinding fragmentDetailBinding;

    ImageView ivProfileImage;
    TextView tvDescription;
    TextView tvUsername;
    ImageView ivImage;
    ImageView ivLike;
    ImageView ivComment;
    ImageView ivShare;
    ImageView ivSave;
    TextView tvTimestamp;

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
        ivSave = fragmentDetailBinding.ivSave;
        tvTimestamp = fragmentDetailBinding.tvTimeStamp;

        setUpViews();

    }

    private void setUpViews() {
        tvUsername.setText(post.getUser().getUsername());
        tvDescription.setText(post.getDescription());
        tvTimestamp.setText(post.getRelativeTime());
        ParseFile image = post.getImg();
        if (image != null) {
            Glide.with(this).load(image.getUrl().replaceAll("http", "https")).into(ivImage);
        }
        ParseFile profileImg = post.getUser().getParseFile("profileImg");
        if (profileImg != null) {
            Glide.with(this).load(profileImg.getUrl().replaceAll("http", "https")).into(ivProfileImage);
        }
    }
}