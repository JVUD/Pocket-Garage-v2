package com.example.pocketgaragenotlogin.ui.home;

import static androidx.camera.core.impl.UseCaseConfig.*;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Rational;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.Preview;
import androidx.camera.core.impl.PreviewConfig;
import androidx.camera.core.impl.UseCaseConfig;
import androidx.camera.core.impl.UseCaseConfig.Builder;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;

import com.example.pocketgaragenotlogin.R;
import com.example.pocketgaragenotlogin.databinding.FragmentHomeBinding;
import com.example.pocketgaragenotlogin.image_edit;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    Button edit;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
    edit = root.findViewById(R.id.btnCapture);
    edit.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getActivity(), image_edit.class);
            // If you need to pass data to the activity

            startActivity(intent);
        }
    });
        return root;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void onDestroyView() {
            super.onDestroyView();
            binding = null;
        }

}