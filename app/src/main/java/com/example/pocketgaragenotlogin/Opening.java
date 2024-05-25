package com.example.pocketgaragenotlogin;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Opening extends AppCompatActivity {

        FirebaseAuth mAuth;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_opening);
            mAuth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = mAuth.getCurrentUser();
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (currentUser == null ) {

                        Intent intent = new Intent(Opening.this, Login.class);
                        startActivity(intent);
                        finish();// Optional: Finish this activity if you don't want to return to it
                    }
                    else{
                        Intent intent = new Intent(Opening.this, Homepage3Activity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }, 1000);
        }
    }

