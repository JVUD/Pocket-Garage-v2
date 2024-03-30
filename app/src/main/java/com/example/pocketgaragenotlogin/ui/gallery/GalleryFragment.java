package com.example.pocketgaragenotlogin.ui.gallery;

import static androidx.core.content.ContextCompat.checkSelfPermission;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pocketgaragenotlogin.R;
import com.example.pocketgaragenotlogin.RecyclerViewAdapter;

import java.util.ArrayList;

public class GalleryFragment extends Fragment {
    public static final String TAG = "GalleryFragment";

    private static final int PERMISSION_REQUEST_CODE = 200;
    private static final int MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE = 1;
    private ArrayList<String> imagePaths;
    private RecyclerView imagesRV;
    private RecyclerViewAdapter imageRVAdapter;
    Button refresh;


    // Initialize GestureDetector

    private int PICK_PDF_REQUEST = 124;
    private int REQUEST_CODE_MANAGE_STORAGE_PERMISSION = 123;

    @SuppressLint({"MissingInflatedId", "NotifyDataSetChanged"})
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_gallery, container, false);

        imagePaths = new ArrayList<>();
        imagesRV = rootView.findViewById(R.id.idRVImages);

        requestPermissions();


        // Initialize the adapter
        imageRVAdapter = new RecyclerViewAdapter(requireContext(), imagePaths);
// Inside your activity or fragment code

        // Set the adapter to the RecyclerView
        imagesRV.setAdapter(imageRVAdapter);

        // Set up RecyclerView layout manager (after setting the adapter)
        GridLayoutManager manager = new GridLayoutManager(requireContext(), 2);
        imagesRV.setLayoutManager(manager);
        refresh = rootView.findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return rootView;
    }



    private boolean checkPermission() {
        int result = checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
//        Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
//        startActivityForResult(intent, REQUEST_CODE_MANAGE_STORAGE_PERMISSION);
        requestPermission();
        if(checkPermission()){
        requestPermission();
        Toast.makeText(requireContext(), "Must request permission", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(requireContext(), "Permissions granted..", Toast.LENGTH_SHORT).show();
            getImagePath();
        }
        Log.e(TAG, "requestPermissions: " + imagePaths.toString());
    }
    @SuppressLint("NotifyDataSetChanged")
    private void refreshFragment() {
        getImagePath();
        imageRVAdapter.notifyDataSetChanged();

        Toast.makeText(requireContext(), "REFRESHED", Toast.LENGTH_SHORT).show();

    }

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }


        }






    private void prepareRecyclerView() {
        imageRVAdapter = new RecyclerViewAdapter(requireContext(), imagePaths);
        GridLayoutManager manager = new GridLayoutManager(requireContext(), 4);
        imagesRV.setLayoutManager(manager);
        imagesRV.setAdapter(imageRVAdapter);
    }

    private void getImagePath() {
        imagePaths = new ArrayList<>();
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
//                imageRVAdapter.notifyDataSetChanged();
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
