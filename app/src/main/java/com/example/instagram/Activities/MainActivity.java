package com.example.instagram.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toolbar;

import com.example.instagram.R;
import com.example.instagram.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private Toolbar tbMenu;
    private ImageView ivCompose;
    private ImageView ivDirect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());

        setUpBottomNavigationView();
        setUpToolBar();

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
                        goComposeActivity();
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

    private void goComposeActivity() {
        startActivity(new Intent(MainActivity.this, ComposeActivity.class));
    }

    private void goProfileActivity() {
        startActivity(new Intent(MainActivity.this, ProfileActivity.class));
    }
}