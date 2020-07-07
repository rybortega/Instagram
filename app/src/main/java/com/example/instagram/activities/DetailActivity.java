package com.example.instagram.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.instagram.R;
import com.example.instagram.databinding.ActivityDetailBinding;
import com.example.instagram.models.Post;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseFile;

import org.parceler.Parcels;

public class DetailActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private Toolbar tbMenu;
    private ActivityDetailBinding activityDetailBinding;
    private Post post;

    ImageView ivProfileImage;
    TextView tvDescription;
    TextView tvUsername;
    ImageView ivImage;
    ImageView ivLike;
    ImageView ivComment;
    ImageView ivShare;
    ImageView ivSave;
    TextView tvTimestamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityDetailBinding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(activityDetailBinding.getRoot());
        post = Parcels.unwrap(getIntent().getParcelableExtra(MainActivity.KEY_POST));

        ivProfileImage = activityDetailBinding.ivProfileImg;
        tvUsername = activityDetailBinding.tvUsername;
        tvDescription = activityDetailBinding.tvDescription;
        ivImage = activityDetailBinding.ivImg;
        ivLike = activityDetailBinding.ivLike;
        ivComment = activityDetailBinding.ivComment;
        ivShare = activityDetailBinding.ivShare;
        ivSave = activityDetailBinding.ivSave;
        tvTimestamp = activityDetailBinding.tvTimeStamp;

        setUpBottomNavigationView();
        setUpToolBar();
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

    private void setUpBottomNavigationView() {
        bottomNavigationView = activityDetailBinding.bottomNavigation;
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.miHome:
                        goMainActivity();
                        return true;
                    case R.id.miCompose:
                        goCameraActivity();
                        return true;
                    case R.id.miProfile:
                        goProfileActivity();
                        return true;
                    default: return true;
                }
            }
        });
    }

    private void setUpToolBar() {
        tbMenu = activityDetailBinding.tbMenu;
    }

    private void goMainActivity() {
        startActivity(new Intent(DetailActivity.this, MainActivity.class));
    }

    private void goCameraActivity() {
        startActivity(new Intent(DetailActivity.this, CameraActivity.class));
    }

    private void goProfileActivity() {
        startActivity(new Intent(DetailActivity.this, ProfileActivity.class));
    }
}