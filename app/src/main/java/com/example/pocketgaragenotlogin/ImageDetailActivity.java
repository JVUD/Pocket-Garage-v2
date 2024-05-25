package com.example.pocketgaragenotlogin;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

public class ImageDetailActivity extends AppCompatActivity {

    // creating a string variable, image view variable
    // and a variable for our scale gesture detector class.
    String imgPath;
    private ImageView imageView;
    private ScaleGestureDetector scaleGestureDetector;
    Adapter adapter;

    Button delete;
    Button edit;
    String imgName;

    private RecyclerView imagesRV;
    private RecyclerViewAdapter imageRVAdapter;

    // on below line we are defining our scale factor.
    private float mScaleFactor = 1.0f;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail_activity);

        // on below line getting data which we have passed from our adapter class.
        imgPath = getIntent().getStringExtra("imgPath");
        imgName = getImageNameFromPath(imgPath);
        // initializing our image view.
        imageView = findViewById(R.id.idIVImage);
        delete = findViewById(R.id.delete);
        edit = findViewById(R.id.editD);
        // on below line we are initializing our scale gesture detector for zoom in and out for our image.
        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());

        // on below line we are getting our image file from its path.
        Glide.with(this)
                .load(imgPath)
                .placeholder(R.drawable.profile)
                .into(imageView);
        // if the file exists then we are loading that image in our image view.

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteImage();

                finish();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRenameImageDialog();
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        // inside on touch event method we are calling on
        // touch event method and passing our motion event to it.
        scaleGestureDetector.onTouchEvent(motionEvent);
        return true;
    }
    // Inside your fragment class
    // Inside your Fragment class
    private boolean deleteFileByPath(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            return file.delete();

        }
        return false; // File doesn't exist or deletion failed
    }
    private void deleteImage() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl(imgPath);

        storageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(ImageDetailActivity.this, "Image deleted successfully", Toast.LENGTH_SHORT).show();
                setResult(Activity.RESULT_OK);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Failed to delete the file
                Toast.makeText(ImageDetailActivity.this, "Failed to delete image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private String getImageNameFromPath(String imagePath) {
        File file = new File(imagePath);
        return file.getName();
    }

    private void showRenameImageDialog() {
        LayoutInflater inflater = LayoutInflater.from(ImageDetailActivity.this);
        View dialogView = inflater.inflate(R.layout.dialog_rename_image, null);

        EditText editTextNewImageName = dialogView.findViewById(R.id.edit_text_new_image_name);

        AlertDialog.Builder builder = new AlertDialog.Builder(ImageDetailActivity.this);
        builder.setView(dialogView)
                .setTitle("Rename Image")
                .setPositiveButton("Rename", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newImageName = editTextNewImageName.getText().toString();
                        // Perform renaming operation with newImageName
                        renameImage(newImageName, imgPath);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void renameImage(String newImageName, String imgPath) {
        File currentFile = new File("/storage/emulated/0/" + Environment.DIRECTORY_PICTURES + "/Cars/", getImageNameFromPath(imgPath));
        File newFile = new File("/storage/emulated/0/" + Environment.DIRECTORY_PICTURES + "/Cars/", newImageName + ".jpg");

        if (currentFile.exists()) {
            if (currentFile.renameTo(newFile)) {
                // File renamed successfully
                // You may update any UI elements to reflect the new file name
                Toast.makeText(this, "Image renamed successfully", Toast.LENGTH_SHORT).show();
            } else {
                // Failed to rename file
                // Handle error, such as showing a toast message
                Toast.makeText(this, "Failed to rename image", Toast.LENGTH_SHORT).show();
            }
        } else {
            // File doesn't exist
            // Handle error, such as showing a toast message
            Toast.makeText(this, "Image file does not exist", Toast.LENGTH_SHORT).show();
        }
    }



    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        // on below line we are creating a class for our scale
        // listener and  extending it with gesture listener.
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {

            // inside on scale method we are setting scale
            // for our image in our image view.
            mScaleFactor *= scaleGestureDetector.getScaleFactor();
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 10.0f));

            // on below line we are setting
            // scale x and scale y to our image view.
            imageView.setScaleX(mScaleFactor);
            imageView.setScaleY(mScaleFactor);
            return true;
        }
    }
}