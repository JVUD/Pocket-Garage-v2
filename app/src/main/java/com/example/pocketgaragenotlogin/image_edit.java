package com.example.pocketgaragenotlogin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.example.pocketgaragenotlogin.ui.home.HomeFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

public class image_edit extends AppCompatActivity {


    Uri uri;
    Bitmap bitmap;
    Button button;
    String model;
    String mark;

    RatingBar ratingBar;
    Boolean isSelected;
    private DatabaseReference firebaseDatabase;
    private FirebaseAuth mAuth;
    HomeFragment homeFragment = new HomeFragment();

    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_edit);
        Intent intent = getIntent();
        bitmap = intent.getParcelableExtra("bitmap");
        onPictureTaken(bitmap);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        getSupportActionBar().setTitle("Add new car");
        Spinner spinnerModel = findViewById(R.id.spinnerModel);
        ArrayAdapter<CharSequence> models = ArrayAdapter.createFromResource(this, R.array.Car_Models, android.R.layout.simple_spinner_item);
        models.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerModel.setAdapter(models);
        Spinner spinnerGen = findViewById(R.id.spinnerGen);
        mAuth = FirebaseAuth.getInstance();
        button = findViewById(R.id.addcar);
        spinnerModel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mark = parent.getItemAtPosition(position).toString();

                ArrayAdapter<CharSequence> gen = new ArrayAdapter<>(image_edit.this, android.R.layout.simple_spinner_item); // Initialize to a default ArrayAdapter
                if(mark.equals("Toyota")) {
                    gen = ArrayAdapter.createFromResource(image_edit.this, R.array.Toyota, android.R.layout.simple_spinner_item);
                }
                else if(mark.equals("Honda")) {
                    gen = ArrayAdapter.createFromResource(image_edit.this, R.array.Honda, android.R.layout.simple_spinner_item);
                }
                else if(mark.equals("Ford")) {
                    gen = ArrayAdapter.createFromResource(image_edit.this, R.array.Ford, android.R.layout.simple_spinner_item);
                }
                else if(mark.equals("Chevrolet")) {
                    gen = ArrayAdapter.createFromResource(image_edit.this, R.array.Chevrolet, android.R.layout.simple_spinner_item);
                }else if(mark.equals("BMW")) {
                    gen = ArrayAdapter.createFromResource(image_edit.this, R.array.BMW, android.R.layout.simple_spinner_item);
                }
                else if(mark.equals("Audi")) {
                    gen = ArrayAdapter.createFromResource(image_edit.this, R.array.Audi, android.R.layout.simple_spinner_item);
                }
                else if(mark.equals("Mercedes-Benz")) {
                    gen = ArrayAdapter.createFromResource(image_edit.this, R.array.MercedesBenz, android.R.layout.simple_spinner_item);
                }
                else if(mark.equals("Volkswagen")) {
                    gen = ArrayAdapter.createFromResource(image_edit.this, R.array.Volkswagen, android.R.layout.simple_spinner_item);
                }
                else if(mark.equals("Hyundai")) {
                    gen = ArrayAdapter.createFromResource(image_edit.this, R.array.Hyundai, android.R.layout.simple_spinner_item);
                }
                else if(mark.equals("Nissan")) {
                    gen = ArrayAdapter.createFromResource(image_edit.this, R.array.Volkswagen, android.R.layout.simple_spinner_item);
                }
                ((TextView) parent.getChildAt(0)).setTextSize(30);


                // Add other conditions for other car brands here...

                gen.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // Set drop-down view resource
                spinnerGen.setAdapter(gen);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                isSelected = false;
            }
        });
        spinnerGen.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                model = parent.getItemAtPosition(position).toString();
                ((TextView) parent.getChildAt(0)).setTextSize(30);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                isSelected = false;
            }
        });

        ratingBar = findViewById(R.id.ratingBar);

//        mAuth = FirebaseAuth.getInstance();
//        firebaseDatabase = FirebaseDatabase.getInstance();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String model_text = mark;
                String gen_text = model;
                Float rating_val = ratingBar.getRating();

                boolean notificationEnabled = sharedPreferences.getBoolean("sync", true);

                String name = model_text + "_" + gen_text + "_" + String.valueOf(rating_val);

                if (notificationEnabled) {
                    Bitmap VHSbitmap =  VHSFilter.applyVHSFilter(bitmap);
                    saveImageToFolder(VHSbitmap, name);
                    uploadAndSaveImage(VHSbitmap, name);
                }
                else {
                    saveImageToFolder(bitmap, name);
                    uploadAndSaveImage(bitmap, name);
                }

                finish();
//                item_model im = new item_model(model_text, gen_text, rating_val, bitmap);
//                DatabaseReference usersRef = firebaseDatabase.getReference().child("auto");
//                usersRef.setValue(im);

            }
        });
    }
    private void uploadAndSaveImage(Bitmap bitmap, String name) {

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null && bitmap != null) {
            firebaseDatabase = FirebaseDatabase.getInstance().getReference(); // Initialize firebaseDatabase
            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
            if(storageRef == null){
                Toast.makeText(this, "Null", Toast.LENGTH_SHORT).show();
            }
            String imageId = UUID.randomUUID().toString();
            StorageReference imagesRef = storageRef.child("images/" + user.getUid() + "/" + imageId + "[" + name + ".jpg" + "]");
            if(imagesRef == null){
                Toast.makeText(this, "Null", Toast.LENGTH_SHORT).show();
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageData = baos.toByteArray();

            UploadTask uploadTask = imagesRef.putBytes(imageData);

            uploadTask.addOnCompleteListener(task -> {
                if (task.isSuccessful()) {

                    imagesRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString();

                        DatabaseReference userRef = firebaseDatabase.child("Users").child(user.getUid()).child("profileImageUrls");
                        String imageKey = userRef.push().getKey();
                        userRef.child(imageKey).setValue(imageUrl)
                                .addOnCompleteListener(saveTask -> {
                                    if (saveTask.isSuccessful()) {
                                        Toast.makeText(this, "Image URL saved to database.", Toast.LENGTH_SHORT).show();

                                    } else {
                                        Toast.makeText(this, "Failed to save image URL to database.", Toast.LENGTH_SHORT).show();
                                    }

                                });
                    }).addOnFailureListener(e -> {
                        Toast.makeText(this, "Failed to retrieve download URL: " + e.getMessage(), Toast.LENGTH_SHORT).show();

                    });
                } else {
                    Toast.makeText(this, "Failed to upload image: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
        } else {
            Toast.makeText(this, "User or image is null.", Toast.LENGTH_SHORT).show();

        }
    }
    public void onPictureTaken(Bitmap bitmap) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean notificationEnabled = sharedPreferences.getBoolean("sync", true);
        if (notificationEnabled) {
            Bitmap bitmap1 =  VHSFilter.applyVHSFilter(bitmap);

            ImageView imageView = findViewById(R.id.imageView);
            imageView.setImageBitmap(bitmap1);
        }
        else {
            ImageView imageView = findViewById(R.id.imageView);
            imageView.setImageBitmap(bitmap);
        }


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