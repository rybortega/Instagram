package com.example.instagram.fragments;

import android.content.Intent;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.instagram.R;
import com.example.instagram.activities.LoginActivity;
import com.example.instagram.activities.MainActivity;
import com.example.instagram.adapters.PostGridAdapter;
import com.example.instagram.adapters.PostsAdapter;
import com.example.instagram.databinding.FragmentCommentBinding;
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
public class CommentFragment extends Fragment {

    private static final String TAG = "CommentFragment";

    private Post post;
    private FragmentCommentBinding fragmentCommentBinding;
    private EditText etComment;
    private Button btPost;

    public CommentFragment() {
    }

    public static CommentFragment newInstance(Parcelable post) {
        CommentFragment fragment = new CommentFragment();
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
        fragmentCommentBinding = FragmentCommentBinding.inflate(inflater, container, false);
        return fragmentCommentBinding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        etComment = fragmentCommentBinding.edComment;
        btPost = fragmentCommentBinding.btnPost;

        btPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String comment = etComment.getText().toString();
                post.postComment(comment, getActivity());
                goToDetail();
            }
        });
    }

    public void goToDetail() {
        DetailFragment detailFragment = DetailFragment.newInstance(Parcels.wrap(post));
        MainActivity.fragmentManager.beginTransaction().replace(R.id.flContainer, detailFragment).commit();
    }
}