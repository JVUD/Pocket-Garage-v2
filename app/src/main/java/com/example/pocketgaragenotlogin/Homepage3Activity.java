package com.example.pocketgaragenotlogin;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pocketgaragenotlogin.databinding.ActivityHomepage3Binding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Homepage3Activity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityHomepage3Binding binding;
    private FirebaseAuth mAuth;
    private TextView textViewUserName;
    private TextView textViewUserEmail;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomepage3Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarHomepage3.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Check if permission is not granted


        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_homepage3);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            startActivity(new Intent(Homepage3Activity.this, Login.class));
        }
        mAuth = FirebaseAuth.getInstance();
        Log.e("HomePage", "onCreate:"+ FirebaseAuth.getInstance().getCurrentUser());
        NavigationView navigationView1 = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        textViewUserName = headerView.findViewById(R.id.Name);
        textViewUserEmail = headerView.findViewById(R.id.Email);

        updateNavigationHeader();
    }
    private void updateNavigationHeader() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();



            FirebaseDatabase.getInstance().getReference().child("Users").child(currentUser.getUid()).child("username").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String username = dataSnapshot.getValue(String.class);
                        String useremail = currentUser.getEmail().toString();
                        textViewUserName.setText(username);
                        textViewUserEmail.setText(useremail);
                    }
                    else {
                        textViewUserEmail.setText("No mail");
                        textViewUserName.setText("No name");

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Handle errors
                }
            });
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.homepage3, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            // Handle settings menu item click
            startActivity(new Intent(this, SettingsActivity.class)); // Start SettingsActivity

            return true;
        }
        else if (id == R.id.action_logout) {
            // Handle log out menu item click
//            FirebaseAuth.getInstance().signOut(); // Sign out the current user
            startActivity(new Intent(this, Login.class)); // Navigate to the login activity
            finish(); // Close the current activity
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_homepage3);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}