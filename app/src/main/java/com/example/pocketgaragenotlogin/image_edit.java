package com.example.pocketgaragenotlogin;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pocketgaragenotlogin.R;

public class image_edit extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_edit);

        // Your activity initialization code
    }

    public void onPictureTaken(Uri imageUri) {
        // Handle the imageUri in your activity
        // For example, you can display the image in an ImageView
        ImageView imageView = findViewById(R.id.imageView);
        imageView.setImageURI(imageUri);
    }

}