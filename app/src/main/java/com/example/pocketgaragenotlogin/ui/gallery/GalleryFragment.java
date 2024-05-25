package com.example.pocketgaragenotlogin.ui.gallery;

import static android.content.Context.WINDOW_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Dimension;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pocketgaragenotlogin.R;
import com.example.pocketgaragenotlogin.RecyclerViewAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class GalleryFragment extends Fragment {
    private static final int PERMISSION_REQUEST_CODE = 200;
    private ArrayList<String> imagePaths = new ArrayList<>();
    private RecyclerView imagesRV;
    private RecyclerViewAdapter imageRVAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_gallery, container, false);
        // Get the WindowManager service
//        WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
//
//        // Get the Display object
//        Display display = windowManager.getDefaultDisplay();
//
//        // Create a DisplayMetrics object
//        DisplayMetrics displayMetrics = new DisplayMetrics();
//
//        // Populate the DisplayMetrics object
//        display.getMetrics(displayMetrics);
//
//        // Get the width and height in pixels
//        int width = displayMetrics.widthPixels;
        imagesRV = rootView.findViewById(R.id.idRVImages);
        prepareRecyclerView();
        loadImagesFromFirebase();

        return rootView;
    }

    private void checkAndRequestPermissions() {
        if (checkPermission()) {
            loadImagesFromFirebase();
        } else {
            requestPermission();
        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    private void loadImagesFromFirebase() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Log.e("GalleryFragment", "No current user");
            return;
        }

        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("images/").child(currentUser.getUid());

        storageRef.listAll().addOnSuccessListener(listResult -> {
            List<StorageReference> items = listResult.getItems();
            if (items.isEmpty()) {
                Log.e("GalleryFragment", "No images found in Firebase Storage");
            }
            List<Pair<StorageReference, Uri>> imageUris = new ArrayList<>();
            AtomicInteger count = new AtomicInteger(items.size());

            for (StorageReference item : items) {
                item.getDownloadUrl().addOnSuccessListener(uri -> {
                    imageUris.add(new Pair<>(item, uri));
                    if (count.decrementAndGet() == 0) handleImageUris(imageUris);
                }).addOnFailureListener(e -> {
                    count.decrementAndGet();
                    if (count.get() == 0) handleImageUris(imageUris);
                    Log.e("GalleryFragment", "Failed to load image: " + e.getMessage());
                });
            }
        }).addOnFailureListener(e -> Log.e("GalleryFragment", "Failed to list images: " + e.getMessage()));
    }

    private void handleImageUris(List<Pair<StorageReference, Uri>> imageUris) {
        List<Pair<Uri, Long>> imageTimeStamps = new ArrayList<>();
        for (Pair<StorageReference, Uri> pair : imageUris) {
            Uri uri = pair.second;
            pair.first.getMetadata().addOnSuccessListener(metadata -> {
                long creationTimeMillis = metadata.getCreationTimeMillis();
                imageTimeStamps.add(new Pair<>(uri, creationTimeMillis));
                if (imageTimeStamps.size() == imageUris.size()) {
                    Collections.sort(imageTimeStamps, (pair1, pair2) -> pair2.second.compareTo(pair1.second));
                    imagePaths.clear();
                    for (Pair<Uri, Long> timeStampPair : imageTimeStamps) {
                        imagePaths.add(timeStampPair.first.toString());
                        Log.d("GalleryFragment", "Image added: " + timeStampPair.first.toString());
                    }
                    imageRVAdapter.notifyDataSetChanged();
                }
            }).addOnFailureListener(e -> Log.e("GalleryFragment", "Failed to load image metadata: " + e.getMessage()));
        }
    }

    public void prepareRecyclerView() {

        imageRVAdapter = new RecyclerViewAdapter(requireContext(), imagePaths);
        GridLayoutManager manager = new GridLayoutManager(requireContext(), 4);
        imagesRV.setLayoutManager(manager);
        imagesRV.setAdapter(imageRVAdapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            loadImagesFromFirebase();
        } else {
            Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }
}

