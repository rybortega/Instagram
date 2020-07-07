package com.example.instagram.models;

import android.os.Parcelable;
import android.util.Log;

import com.example.instagram.activities.MainActivity;
import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

@ParseClassName("_User")
public class User extends ParseUser implements Parcelable {

    public final String TAG = "UserModel";

    public static String PROFILE_IMG_TAG = "profileImg";
    public static String USERNAME_TAG = "username";

    public void setProfileImg(ParseFile file) {
        put(PROFILE_IMG_TAG, file);
    }
    public ParseFile getProfileImg() {
        Log.e(TAG, "TRIED TO GET IMG");
        return getParseFile(PROFILE_IMG_TAG);
    }

    public List<Post> getPosts() {
        final List<Post> posts = new ArrayList<>();
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.addDescendingOrder("createdAt");
        query.whereContains("user", getObjectId());

        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> newPosts, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error when querying new posts");
                    return;
                }
                posts.addAll(newPosts);
                Log.i(TAG, "Query on user " + getUsername() + " completed, got " + newPosts.size() + " new posts");
                MainActivity.hideProgressBar();
            }
        });

        return posts;
    }
}
