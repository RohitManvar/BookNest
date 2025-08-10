package com.example.virtuallibrary;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.auth.User;

public class AuthActivity extends AppCompatActivity {
    private EditText emailEditText, passwordEditText, nameEditText;
    private TextInputLayout nameInputLayout;
    private Button authButton;
    private TextView toggleAuthText;
    private boolean isLoginMode = true;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        mAuth = FirebaseAuth.getInstance();

        initViews();
        setupClickListeners();
    }

    private void initViews() {
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        nameEditText = findViewById(R.id.nameEditText);
        nameInputLayout = findViewById(R.id.nameInputLayout);
        authButton = findViewById(R.id.authButton);
        toggleAuthText = findViewById(R.id.toggleAuthText);

        updateUI();
    }

    private void setupClickListeners() {
        authButton.setOnClickListener(v -> handleAuth());
        toggleAuthText.setOnClickListener(v -> toggleAuthMode());
    }

    private void toggleAuthMode() {
        isLoginMode = !isLoginMode;
        updateUI();
    }

    private void updateUI() {
        if (isLoginMode) {
            nameInputLayout.setVisibility(TextView.GONE);
            authButton.setText("Login");
            toggleAuthText.setText("Don't have an account? Sign up");
        } else {
            nameInputLayout.setVisibility(TextView.VISIBLE);
            authButton.setText("Sign Up");
            toggleAuthText.setText("Already have an account? Login");
        }
    }

    private void handleAuth() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString();
        String name = nameEditText.getText().toString().trim();

        if (!validateInputs(email, password, name)) return;

        if (isLoginMode) {
            performLogin(email, password);
        } else {
            performSignUp(email, password, name);
        }
    }

    private boolean validateInputs(String email, String password, String name) {
        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Email is required");
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Invalid email format");
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Password is required");
            return false;
        }

        if (password.length() < 6) {
            passwordEditText.setError("Password must be at least 6 characters");
            return false;
        }

        if (!isLoginMode && TextUtils.isEmpty(name)) {
            nameEditText.setError("Name is required");
            return false;
        }

        return true;
    }

    private void performLogin(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
                    navigateToHome();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Login failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

    private void performSignUp(String email, String password, String name) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    FirebaseUser user = authResult.getUser();
                    if (user != null) {
                        saveUserToDatabase(user.getUid(), name, email);
                    }
                    Toast.makeText(this, "Account created successfully!", Toast.LENGTH_SHORT).show();
                    navigateToHome();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Signup failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

    private void saveUserToDatabase(String uid, String name, String email) {
        FirebaseDatabase.getInstance().getReference("users")
                .child(uid)
                .setValue(new AppUser(name, email, ""))
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to save user info: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void navigateToHome() {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }
}
