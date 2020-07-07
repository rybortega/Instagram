package com.example.instagram.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.instagram.R;
import com.example.instagram.activities.MainActivity;
import com.example.instagram.databinding.FragmentProfileBinding;
import com.example.instagram.models.User;
import com.parse.ParseFile;
import com.parse.ParseUser;

import org.parceler.Parcels;

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

        tvUsername.setText(user.getUsername());
        ParseFile profileImg = ParseUser.getCurrentUser().getParseFile("profileImg");
        Log.e(TAG, ParseUser.getCurrentUser().getObjectId().toString());
        if (profileImg != null) {
            Glide.with(getActivity()).load(profileImg.getUrl().replaceAll("http", "https")).into(ivProfileImg);
        } else {
            Log.e(TAG, "CANNOT FIND IMAGE");
        }
    }
}