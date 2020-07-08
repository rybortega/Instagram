package com.example.instagram.activities;

import android.app.Application;

import com.example.instagram.models.Comment;
import com.example.instagram.models.Post;
import com.example.instagram.models.User;
import com.parse.Parse;
import com.parse.ParseObject;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Post.class);
        ParseObject.registerSubclass(User.class);
        ParseObject.registerSubclass(Comment.class);
        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.networkInterceptors().add(httpLoggingInterceptor);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("simple-instagram-hatruong") // should correspond to APP_ID env variable
                .clientKey(null)  // set explicitly unless clientKey is explicitly configured on Parse server
                .clientBuilder(builder)
                .server("https://simple-instagram-hatruong.herokuapp.com/parse/").build());
    }
}