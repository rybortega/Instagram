package com.example.instagram.models;

import android.os.Parcelable;
import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Comment")
public class Comment extends ParseObject implements Parcelable {

    private static final String TAG = "CommentModel";
    public final String KEY_CONTENT = "Content";
    public final String KEY_AUTHOR = "Author";

    public String getUser() throws ParseException {
        ParseUser user = getParseUser(KEY_AUTHOR);
        Log.e(TAG, user.getObjectId());
        return user.fetchIfNeeded().getUsername();
    }

    public String getContent() {
        return getString(KEY_CONTENT);
    }
}
