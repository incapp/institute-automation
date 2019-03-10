package com.incapp.instituteautomation.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.incapp.instituteautomation.R;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextEmail;
    private TextView textViewRegisterHere;
    private TextView textViewForgotPassword;
    private EditText editTextPassword;
    private Button buttonLogin;
    private LinearLayout linearLayoutParent;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmail = findViewById(R.id.editText_email);
        textViewRegisterHere = findViewById(R.id.textView_register_here);
        editTextPassword = findViewById(R.id.editText_password);
        buttonLogin = findViewById(R.id.button_login);
        linearLayoutParent = findViewById(R.id.linearLayout_parent);
        progressBar = findViewById(R.id.progressBar);
        textViewForgotPassword = findViewById(R.id.textView_forgot_password);

        textViewRegisterHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        LoginActivity.this,
                        RegisterActivity.class
                );

                startActivity(intent);
                finish();
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        textViewForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = editTextEmail.getText().toString();

                if (email.isEmpty()) {
                    editTextEmail.setError("Required!");
                    editTextEmail.requestFocus();
                } else if (!email.matches(Patterns.EMAIL_ADDRESS.pattern())) {
                    editTextEmail.setError("Invalid Email!");
                    editTextEmail.requestFocus();
                } else {

                    FirebaseAuth.getInstance()
                            .sendPasswordResetEmail(email)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(LoginActivity.this,
                                            "Mail Sent.", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(LoginActivity.this,
                                            e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                }

            }
        });
    }

    private void login() {
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();

        if (email.isEmpty()) {
            editTextEmail.setError("Required!");
            editTextEmail.requestFocus();

        } else if (!email.matches(Patterns.EMAIL_ADDRESS.pattern())) {
            editTextEmail.setError("Invalid Email!");
            editTextEmail.requestFocus();

        } else if (password.isEmpty()) {
            editTextPassword.setError("Required!");
            editTextPassword.requestFocus();

        } else {

            linearLayoutParent.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);

            FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            linearLayoutParent.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);

                            if (task.isSuccessful()) {

                                if (task.getResult().getUser().isEmailVerified()) {
                                    Intent intent = new Intent(
                                            LoginActivity.this,
                                            MainActivity.class
                                    );

                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(LoginActivity.this,
                                            "Verify Email First.", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(LoginActivity.this,
                                        task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }
    }
}
