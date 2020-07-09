package com.example.instagram.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.instagram.R;
import com.example.instagram.activities.MainActivity;
import com.example.instagram.databinding.FragmentComposeBinding;
import com.example.instagram.models.Post;
import com.example.instagram.models.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcels;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static android.app.Activity.RESULT_OK;


public class ComposeFragment extends Fragment {

    public final String TAG = "ComposeFragment";
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    private static final int LOAD_IMAGE_ACTIVITY_REQUEST_CODE = 1512;

    private String photoFileName = "photo.jpg";
    private ImageView ivCamera;
    private File photoFile;
    private EditText etDescription;
    private Button btShare;
    private User user;
    private FloatingActionButton fabShare;
    private FragmentComposeBinding fragmentComposeBinding;

    public ComposeFragment() {
    }

    public static ComposeFragment newInstance(Parcelable user) {
        ComposeFragment fragment = new ComposeFragment();
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
        fragmentComposeBinding = FragmentComposeBinding.inflate(inflater, container, false);
        ivCamera = fragmentComposeBinding.ivCamera;
        etDescription = fragmentComposeBinding.etDescription;
        btShare = fragmentComposeBinding.btnShare;
        ivCamera = fragmentComposeBinding.ivCamera;
        fabShare = fragmentComposeBinding.fabShare;

        photoFile = getPhotoFileUri(photoFileName);

        // Use camera to update avatar
        if (user != null) {
            btShare.setText("Update Avatar");
        } else {
            // Use camera to compose new post
            btShare.setText("Share");
        }

        // Launch camera when camera icon is clicked
        ivCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchCamera();
            }
        });

        // Launch library when library icon is clicked
        fabShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchLibrary();
            }
        });

        // Start uploading selected/captured image
        btShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String description = etDescription.getText().toString();
                ParseUser currUser = ParseUser.getCurrentUser();
                if (description.isEmpty() && user == null) {
                    Toast.makeText(getContext(), "You should include a description!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (photoFile == null) {
                    Toast.makeText(getContext(), "You should include a photo!", Toast.LENGTH_SHORT).show();
                    return;
                }
                MainActivity.showProgressBar(); // Progress bar will be hidden by savePost or updateAvatar
                if (user == null) {
                    savePost(description, currUser, photoFile);
                } else {
                    try {
                        updateAvatar(photoFile);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        return fragmentComposeBinding.getRoot();
    }

    private void launchLibrary() {
        try {
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*"); // Only accept image types
            startActivityForResult(intent, LOAD_IMAGE_ACTIVITY_REQUEST_CODE);
        } catch (Exception e){
            Log.i(TAG, "Error while opening library", e);
        }
    }

    public void launchCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        Uri fileProvider = FileProvider.getUriForFile(getContext(), "com.codepath.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        intent.resolveActivity(getContext().getPackageManager());
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    public void savePost(String description, ParseUser currUser, File photoFile) {
        MainActivity.showProgressBar();
        Log.e(TAG, "Saving selected file");

        Post post = new Post();
        post.setImg(new ParseFile(photoFile));
        post.setDescription(description);
        post.setUser(currUser);

        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error while saving image");
                    Toast.makeText(getContext(), "Error while saving", Toast.LENGTH_SHORT).show();
                }
                Log.e(TAG,"Done");
                Toast.makeText(getContext(), "Image shared", Toast.LENGTH_SHORT).show();
                goToNewsfeed();
                MainActivity.hideProgressBar();
            }
        });
    }

    public void updateAvatar(File photoFile) throws ParseException {
        MainActivity.showProgressBar();
        Log.e(TAG, "Start updating avatar");

        user.put(User.PROFILE_IMG_TAG, new ParseFile(photoFile));
        user.getCurrentUser().saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.e(TAG, "Saved image");
                    Toast.makeText(getContext(), "Image shared", Toast.LENGTH_SHORT).show();
                    goToProfile();
                } else {
                    Log.e(TAG, "Error", e);
                    Toast.makeText(getContext(), "Error while saving", Toast.LENGTH_SHORT).show();
                }
                MainActivity.hideProgressBar();
            }
        });
    }

    private void goToProfile() {
        ProfileFragment profileFragment = ProfileFragment.newInstance(Parcels.wrap(user));
        MainActivity.fragmentManager.beginTransaction().replace(R.id.flContainer, profileFragment).commit();
    }

    private void goToNewsfeed() {
        NewsfeedFragment newsfeedFragment = new NewsfeedFragment();
        MainActivity.fragmentManager.beginTransaction().replace(R.id.flContainer, newsfeedFragment).commit();
    }

    public File getPhotoFileUri(String fileName) {
        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, "Failed to create directory");
        }

        return new File(mediaStorageDir.getPath() + File.separator + fileName);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            // If user chose to capture new image
            case CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                    ivCamera.setImageBitmap(takenImage);
                } else {
                    Toast.makeText(getContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
                }
                break;
            // If user chose to upload existing image
            case LOAD_IMAGE_ACTIVITY_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();
                    try {
                        // Compress & save selected to photoFile (used for loading)
                        OutputStream os = new BufferedOutputStream(new FileOutputStream(photoFile));
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 1, os);
                        os.close();
                        ivCamera.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        Log.i("TAG", "Picture wasn't selected " + e);
                    }
                    break;
                } else {
                    Toast.makeText(getContext(), "You haven't picked Image", Toast.LENGTH_LONG).show();
                }
                break;
            default:
                Log.e(TAG, "Cannot identify activity result");
        }
    }
}