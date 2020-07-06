package com.example.instagram.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;

import com.example.instagram.R;
import com.example.instagram.databinding.ActivityMainBinding;
import com.example.instagram.models.Post;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private BottomNavigationView bottomNavigationView;
    private Toolbar tbMenu;
    private ImageView ivCompose;
    private ImageView ivDirect;
    private List<Post> posts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());

        setUpBottomNavigationView();
        setUpToolBar();
        queryPosts();

    }

    private void queryPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error when querying new posts");
                    return;
                }
                posts = objects;
            }
        });
    }

    private void setUpBottomNavigationView() {
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigation);
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
        tbMenu = (Toolbar) findViewById(R.id.tbMenu);
    }

    private void goMainActivity() {
        startActivity(new Intent(MainActivity.this, MainActivity.class));
    }

    private void goCameraActivity() {
        startActivity(new Intent(MainActivity.this, CameraActivity.class));
    }

    private void goProfileActivity() {
        startActivity(new Intent(MainActivity.this, ProfileActivity.class));
    }

}