package com.example.pocketgaragenotlogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import com.example.pocketgaragenotlogin.databinding.ActivityRegisterBinding;

public class Register extends AppCompatActivity {
        private ActivityRegisterBinding binding;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            binding = ActivityRegisterBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());

            binding.signUpBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (binding.emailEt.getText().toString().isEmpty() || binding.passwordEt.getText().toString().isEmpty()
                            || binding.usernameEt.getText().toString().isEmpty()){
                        Toast.makeText(getApplicationContext(), "Fields cannot be empty", Toast.LENGTH_SHORT).show();
                    }
                    if (!binding.rpassword.getText().toString().equals(binding.passwordEt.getText().toString())){
                        Toast.makeText(Register.this, "Make sure you wrote your password again", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        FirebaseAuth.getInstance().createUserWithEmailAndPassword(binding.emailEt.getText().toString(), binding.passwordEt.getText().toString())
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()){
                                            HashMap<String, String> userInfo = new HashMap<>();
                                            userInfo.put("email", binding.emailEt.getText().toString());
                                            userInfo.put("username", binding.usernameEt.getText().toString());


                                            FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                    .setValue(userInfo);

                                            startActivity(new Intent(Register.this, Homepage3Activity.class));
                                        }
                                    }
                                });

                    }
                }
            });
        }
}