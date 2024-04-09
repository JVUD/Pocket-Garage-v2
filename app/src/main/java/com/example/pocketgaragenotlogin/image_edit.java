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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
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
    String model;
    String mark;

    RatingBar ratingBar;
    Boolean isSelected;

    HomeFragment homeFragment = new HomeFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_edit);
        Intent intent = getIntent();
        bitmap = intent.getParcelableExtra("bitmap");
        onPictureTaken(bitmap);
        getSupportActionBar().setTitle("Add new car");
        Spinner spinnerModel = findViewById(R.id.spinnerModel);
        ArrayAdapter<CharSequence> models = ArrayAdapter.createFromResource(this, R.array.Car_Models, android.R.layout.simple_spinner_item);
        models.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerModel.setAdapter(models);
        Spinner spinnerGen = findViewById(R.id.spinnerGen);

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

                String name = model_text + "_" + gen_text + "_" + String.valueOf(rating_val);
                saveImageToFolder(bitmap, name);
                finish();
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