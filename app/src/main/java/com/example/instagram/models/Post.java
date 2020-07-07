package com.example.instagram.models;

import android.os.Parcelable;
import android.text.format.DateUtils;
import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@ParseClassName("Post")
public class Post extends ParseObject implements Parcelable {

    public final String TAG = "PostModel";

    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_USER = "user";
    public static final String KEY_TIME = "createdAt";

    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String description) {
        put(KEY_DESCRIPTION, description);
    }

    public ParseFile getImg() {
        return getParseFile(KEY_IMAGE);
    }

    public void setImg(ParseFile img) {
        put(KEY_IMAGE, img);
    }

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

    public String getTime() {
        Date date = getCreatedAt();
        DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
        return df.format(date);
    }

    public String getRelativeTime() {
        Date date = getCreatedAt();
        Log.e(TAG, date.toString());
        return DateUtils.getRelativeTimeSpanString(date.getTime(), System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
    }

}
