package com.example.pocketgaragenotlogin.ui.gallery;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pocketgaragenotlogin.R;
import com.example.pocketgaragenotlogin.RecyclerViewAdapter;

import java.util.ArrayList;

public class GalleryFragment extends Fragment {

    private static final int PERMISSION_REQUEST_CODE = 200;
    private ArrayList<String> imagePaths;
    private RecyclerView imagesRV;
    private RecyclerViewAdapter imageRVAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_gallery, container, false);

        imagePaths = new ArrayList<>();
        imagesRV = rootView.findViewById(R.id.idRVImages);

        // Initialize the adapter
        imageRVAdapter = new RecyclerViewAdapter(requireContext(), imagePaths);

        // Set the adapter to the RecyclerView
        imagesRV.setAdapter(imageRVAdapter);

        // Set up RecyclerView layout manager (after setting the adapter)
        GridLayoutManager manager = new GridLayoutManager(requireContext(), 4);
        imagesRV.setLayoutManager(manager);

        requestPermissions();

        return rootView;
    }
    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        if (checkPermission()) {
            Toast.makeText(requireContext(), "Permissions granted..", Toast.LENGTH_SHORT).show();
            getImagePath();
        } else {
            requestPermission();
        }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    private void prepareRecyclerView() {
        imageRVAdapter = new RecyclerViewAdapter(requireContext(), imagePaths);
        GridLayoutManager manager = new GridLayoutManager(requireContext(), 4);
        imagesRV.setLayoutManager(manager);
        imagesRV.setAdapter(imageRVAdapter);
    }

    private void getImagePath() {
        // Define the directory path for the "Cars" folder within the "Pictures" directory
        String directoryPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/Cars/";

        // Query images from the specified directory path
        final String[] projection = {MediaStore.Images.Media.DATA};
        final String selection = MediaStore.Images.Media.DATA + " like ?";
        final String[] selectionArgs = new String[]{"%" + directoryPath + "%"};
        final String orderBy = MediaStore.Images.Media._ID;

        Cursor cursor = requireActivity().getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                orderBy
        );

        if (cursor != null) {
            int count = cursor.getCount();
            if (count > 0) {
                for (int i = 0; i < count; i++) {
                    cursor.moveToPosition(i);
                    int dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                    imagePaths.add(cursor.getString(dataColumnIndex));
                }
                imageRVAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(requireContext(), "No images found in the Cars directory.", Toast.LENGTH_SHORT).show();
            }
            cursor.close();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (storageAccepted) {
                        Toast.makeText(requireContext(), "Permissions Granted..", Toast.LENGTH_SHORT).show();
                        getImagePath();
                    } else {
                        Toast.makeText(requireContext(), "Permissions denied, Permissions are required to use the app..", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }
}
