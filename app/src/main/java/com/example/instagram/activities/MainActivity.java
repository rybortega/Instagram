package com.example.instagram.activities;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.instagram.R;
import com.example.instagram.databinding.ActivityMainBinding;
import com.example.instagram.databinding.FragmentProfileBinding;
import com.example.instagram.fragments.ComposeFragment;
import com.example.instagram.fragments.NewsfeedFragment;
import com.example.instagram.fragments.ProfileFragment;
import com.example.instagram.models.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseException;
import com.parse.ParseUser;

import org.parceler.Parcels;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_POST = "KeyPost";
    public static final String KEY_USER = "KeyUser";
    private static final String TAG = "MainActivity";

    private BottomNavigationView bottomNavigation;
    private ActivityMainBinding activityMainBinding;
    private Toolbar tbMenu;
    public static ProgressBar progressBar;
    public static FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());

        setUpToolBar();
        setUpProgressBar();

        fragmentManager = getSupportFragmentManager();

        ParseUser user = ParseUser.getCurrentUser();
        try {
            user = user.fetch();
        } catch (ParseException e) {
            Log.e(TAG, "Couldn't fetch current user");
        }

        final Fragment composeFragment = new ComposeFragment();
        final Fragment newsfeedFragment = new NewsfeedFragment();
        final Fragment profileFragment = ProfileFragment.newInstance(Parcels.wrap(user));

        bottomNavigation = activityMainBinding.bottomNavigation;
        bottomNavigation.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment fragment;
                        switch (item.getItemId()) {
                            case R.id.miHome:
                                fragment = newsfeedFragment;
                                break;
                            case R.id.miCompose:
                                fragment = composeFragment;
                                break;
                            case R.id.miProfile:
                            default:
                                fragment = profileFragment;
                                break;
                        }
                        fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                        return true;
                    }
                });

        bottomNavigation.setSelectedItemId(R.id.miHome);
    }

    private void setUpToolBar() {
        tbMenu = activityMainBinding.tbMenu;
    }
    public void setUpProgressBar() {
        progressBar = activityMainBinding.progressBar;
    }
    public static void showProgressBar() {
        progressBar.setVisibility(ProgressBar.VISIBLE);
    }

    public static void hideProgressBar() {
        progressBar.setVisibility(ProgressBar.INVISIBLE);
    }
}