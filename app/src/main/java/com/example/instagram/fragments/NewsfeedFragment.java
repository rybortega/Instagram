package com.example.instagram.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.instagram.R;
import com.example.instagram.activities.MainActivity;
import com.example.instagram.adapters.PostsAdapter;
import com.example.instagram.databinding.FragmentNewsfeedBinding;
import com.example.instagram.databinding.ItemPostBinding;
import com.example.instagram.models.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class NewsfeedFragment extends Fragment {

    private static final String TAG = "NewsfeedFragment";
    private List<Post> posts;
    private PostsAdapter adapter;
    private RecyclerView rvPosts;
    private SwipeRefreshLayout swipeContainer;
    private FragmentNewsfeedBinding fragmentNewsfeedBinding;

    public NewsfeedFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentNewsfeedBinding = FragmentNewsfeedBinding.inflate(inflater, container, false);
        return fragmentNewsfeedBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvPosts = fragmentNewsfeedBinding.rvPosts;
        posts = new ArrayList<>();
        adapter = new PostsAdapter(getActivity(), posts);

        rvPosts.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvPosts.setAdapter(adapter);

        setUpSwipeContainer();
        queryPosts();
    }

    private void setUpSwipeContainer() {
        swipeContainer = fragmentNewsfeedBinding.swipeContainer;
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                queryPosts();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    private void queryPosts() {
        MainActivity.showProgressBar();
        Log.i(TAG, "Start querying for new post");
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.addDescendingOrder("createdAt");
        Log.e(TAG, "here");
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> newPosts, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error when querying new posts");
                    return;
                }
                //posts.clear();
                posts.addAll(newPosts);
                rvPosts.getAdapter().notifyDataSetChanged();
                swipeContainer.setRefreshing(false);
                Log.i(TAG, "Query completed, got " + posts.size() + " new posts");
                MainActivity.hideProgressBar();
            }
        });
    }
}