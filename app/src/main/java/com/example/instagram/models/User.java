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
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

@ParseClassName("_User")
public class User extends ParseUser implements Parcelable {

    public final String TAG = "UserModel";

    public static String PROFILE_IMG_TAG = "profileImg";
    public static String SAVED_TAG = "savedPosts";

    public boolean postSaved(ParseObject post) throws ParseException {
        ParseRelation<ParseObject> likedUsers = getRelation(SAVED_TAG);
        int count = likedUsers.getQuery().whereContains("objectId", post.getObjectId()).count();
        Log.i(TAG, String.valueOf(count));
        return count > 0;
    }

    public void attemptToSave(ParseObject post) throws ParseException {
        ParseRelation<ParseObject> savedPosts = getRelation(SAVED_TAG);
        if (postSaved(post)) {
            savedPosts.remove(post);
            Log.i(TAG, "Saved!");
        } else {
            savedPosts.add(post);
            Log.i(TAG, "Unsaved!");
        }
        save();
    }
}
