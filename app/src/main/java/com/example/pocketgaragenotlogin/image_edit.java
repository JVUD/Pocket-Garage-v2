package com.example.pocketgaragenotlogin;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pocketgaragenotlogin.R;
import com.example.pocketgaragenotlogin.ui.home.HomeFragment;

public class image_edit extends AppCompatActivity {


    Uri uri;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_edit);
        Intent intent = getIntent();
        bitmap = intent.getParcelableExtra("bitmap_image");
        onPictureTaken(bitmap);
    }

    public void onPictureTaken(Bitmap bitmap) {

        ImageView imageView = findViewById(R.id.imageView);
//        imageView.setImageURI(imageUri);


        imageView.setImageBitmap(bitmap);


    }

}