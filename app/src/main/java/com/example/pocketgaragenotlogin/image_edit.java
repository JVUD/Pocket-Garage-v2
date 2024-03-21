package com.example.pocketgaragenotlogin;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pocketgaragenotlogin.R;
import com.example.pocketgaragenotlogin.ui.gallery.GalleryFragment;
import com.example.pocketgaragenotlogin.ui.home.HomeFragment;
import com.example.pocketgaragenotlogin.ui.item_model;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class image_edit extends AppCompatActivity {


    Uri uri;
    Bitmap bitmap;
    Button button;
//    FirebaseAuth mAuth;
//    FirebaseDatabase firebaseDatabase;
    EditText model_et, gen_et;
    RatingBar ratingBar;
    HomeFragment homeFragment = new HomeFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_edit);
        Intent intent = getIntent();
        bitmap = intent.getParcelableExtra("bitmap_image");
        onPictureTaken(bitmap);

        button = findViewById(R.id.addcar);
        model_et = findViewById(R.id.model);
        gen_et = findViewById(R.id.gen);
        ratingBar = findViewById(R.id.ratingBar);

//        mAuth = FirebaseAuth.getInstance();
//        firebaseDatabase = FirebaseDatabase.getInstance();

        button.setOnClickListener(new View.OnClickListener() {
                @Override
            public void onClick(View v) {
                String model_text = String.valueOf(model_et.getText()), gen_text = String.valueOf(gen_et.getText());
                Float rating_val = ratingBar.getRating();
                if (model_text.isEmpty()){
                    Toast.makeText(getApplicationContext(), "No Model name entered", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (gen_text.isEmpty()){
                    Toast.makeText(getApplicationContext(), "No Gen entered", Toast.LENGTH_SHORT).show();
                    return;
                }
                String name = model_text + "_" + gen_text + "_" + String.valueOf(rating_val);
                saveImageToFolder(bitmap, name);
//                item_model im = new item_model(model_text, gen_text, rating_val, bitmap);
//                DatabaseReference usersRef = firebaseDatabase.getReference().child("auto");
//                usersRef.setValue(im);

            }
        });
    }

    public void onPictureTaken(Bitmap bitmap) {

        ImageView imageView = findViewById(R.id.imageView);
//        imageView.setImageURI(imageUri);


        imageView.setImageBitmap(bitmap);


    }
    public void saveImageToFolder(Bitmap bitmap, String name) {
        // Check if external storage is available
        String state = Environment.getExternalStorageState();
        if (!Environment.MEDIA_MOUNTED.equals(state)) {
            Toast.makeText(getApplicationContext(), "External storage not available", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get the external storage directory
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Cars");
        if (!storageDir.exists()) {
            storageDir.mkdirs(); // Create the directory if it doesn't exist
        }

        try {
            // Create a file to save the image
            File imageFile = new File(storageDir, name + ".jpg");

            // Write the bitmap data to the file
            FileOutputStream fos = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();

            // Notify the system that a new file has been created and is available for indexing
            MediaScannerConnection.scanFile(getApplicationContext(), new String[]{imageFile.getAbsolutePath()}, null,
                    (path, uri) -> {
                        // Image saved, display a toast or perform any other action
                        Toast.makeText(getApplicationContext(), "Image saved to folder", Toast.LENGTH_SHORT).show();
                    });
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Failed to save image", Toast.LENGTH_SHORT).show();
        }
    }

}