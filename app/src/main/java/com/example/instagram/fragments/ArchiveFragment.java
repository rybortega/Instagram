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
import android.widget.Toast;

import com.example.instagram.EndlessRecyclerViewScrollListener;
import com.example.instagram.R;
import com.example.instagram.activities.MainActivity;
import com.example.instagram.adapters.PostsAdapter;
import com.example.instagram.databinding.FragmentArchiveBinding;
import com.example.instagram.databinding.FragmentNewsfeedBinding;
import com.example.instagram.databinding.ItemPostBinding;
import com.example.instagram.models.Post;
import com.example.instagram.models.User;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ArchiveFragment extends Fragment {

    private final int QUERY_LIMIT = 20;
    private static final String TAG = "ArchiveFragment";

    private List<Post> posts;
    private PostsAdapter adapter;
    private RecyclerView rvPosts;
    private SwipeRefreshLayout swipeContainer;
    private FragmentArchiveBinding fragmentArchiveBinding;
    private EndlessRecyclerViewScrollListener scrollListener;
    private LinearLayoutManager linearLayoutManager;
    private Date lastPost;
    private User user;

    public ArchiveFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentArchiveBinding = FragmentArchiveBinding.inflate(inflater, container, false);
        return fragmentArchiveBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvPosts = fragmentArchiveBinding.rvPosts;
        posts = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(getActivity());
        adapter = new PostsAdapter(getActivity(), posts);
        lastPost = new Date();
        rvPosts.setLayoutManager(linearLayoutManager);
        rvPosts.setAdapter(adapter);
        user = (User) ParseUser.getCurrentUser();

        setUpScrollListener();
        rvPosts.addOnScrollListener(scrollListener);

        posts.clear();
        setUpSwipeContainer();
        MainActivity.showProgressBar();
        queryPosts();
    }

    private void setUpSwipeContainer() {
        swipeContainer = fragmentArchiveBinding.swipeContainer;
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                posts.clear();
                lastPost = new Date();
                queryPosts();
                swipeContainer.setRefreshing(false);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    private void setUpScrollListener() {
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                Log.e(TAG, "load more");
                MainActivity.showProgressBar();
                queryPosts();
                swipeContainer.setRefreshing(false);

            }
        };
    }

    private void queryPosts() {
        Log.i(TAG, "Start querying for new post");

        // Set up query
        ParseRelation<ParseObject> savedPosts = user.getRelation(User.SAVED_TAG);
        ParseQuery query = savedPosts.getQuery();
        query.addDescendingOrder("createdAt");
        query.whereLessThan("createdAt", lastPost);
        query.setLimit(QUERY_LIMIT);

        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> newPosts, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error when querying new posts");
                    return;
                }
                // Update last post time to set condition for next query
                if (newPosts.size() > 0) {
                    lastPost = newPosts.get(newPosts.size() - 1).getCreatedAt();
                }
                posts.addAll(newPosts);
                adapter.notifyDataSetChanged();
                swipeContainer.setRefreshing(false);
                Log.i(TAG, "Query completed, got " + newPosts.size() + " new posts");
                MainActivity.hideProgressBar();
            }
        });
    }
}