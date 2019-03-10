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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.incapp.instituteautomation.R;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextEmail;
    private Button buttonRegister;
    private EditText editTextConfirmPassword;
    private EditText editTextPassword;
    private TextView textViewLoginHere;
    private LinearLayout linearLayoutParent;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextName = findViewById(R.id.editText_name);
        editTextEmail = findViewById(R.id.editText_email);
        buttonRegister = findViewById(R.id.button_register);
        editTextConfirmPassword = findViewById(R.id.editText_confirmPassword);
        editTextPassword = findViewById(R.id.editText_password);
        textViewLoginHere = findViewById(R.id.textView_login_here);
        linearLayoutParent = findViewById(R.id.linearLayout_parent);
        progressBar = findViewById(R.id.progressBar);

        textViewLoginHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        RegisterActivity.this,
                        LoginActivity.class
                );
                startActivity(intent);
                finish();
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

    }

    private void register() {
        final String name = editTextName.getText().toString();
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();
        String confirmPassword = editTextConfirmPassword.getText().toString();

        if (name.isEmpty()) {
            editTextName.setError("Required!");
            editTextName.requestFocus();
        } else if (email.isEmpty()) {
            editTextEmail.setError("Required!");
            editTextEmail.requestFocus();

        } else if (password.isEmpty()) {
            editTextPassword.setError("Required!");
            editTextPassword.requestFocus();

        } else if (confirmPassword.isEmpty()) {
            editTextConfirmPassword.setError("Required!");
            editTextConfirmPassword.requestFocus();

        } else if (!password.equals(confirmPassword)) {
            editTextConfirmPassword.setError("Passwords do not match!");
            editTextConfirmPassword.requestFocus();

        } else if (!email.matches(Patterns.EMAIL_ADDRESS.pattern())) {
            editTextEmail.setError("Invalid Email ID!");
            editTextEmail.requestFocus();

        } else if (password.length() < 6) {
            editTextPassword.setError("Password too short!");
            editTextPassword.requestFocus();

        } else {

            progressBar.setVisibility(View.VISIBLE);
            linearLayoutParent.setVisibility(View.GONE);

            FirebaseAuth.getInstance()
                    .createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            progressBar.setVisibility(View.GONE);
                            linearLayoutParent.setVisibility(View.VISIBLE);

                            if (task.isSuccessful()) {

                                task.getResult().getUser().sendEmailVerification();

                                FirebaseDatabase.getInstance()
                                        .getReference("users")
                                        .child(task.getResult().getUser().getUid())
                                        .setValue(name);

                                Intent intent = new Intent(
                                        RegisterActivity.this,
                                        LoginActivity.class
                                );

                                startActivity(intent);

                                finish();
                            } else {
                                Toast.makeText(RegisterActivity.this,
                                        task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}
