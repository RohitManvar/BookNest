package com.example.virtuallibrary;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private static final int SPLASH_DURATION = 1000; // 1 second splash (optional)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Apply dark/light theme based on user preference
        applyTheme();

        setContentView(R.layout.activity_main);

        // Delay to show splash screen
        new Handler().postDelayed(() -> {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

            if (currentUser != null) {
                // User is logged in
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(intent);
            } else {
                // User is not logged in
                Intent intent = new Intent(MainActivity.this, AuthActivity.class);
                startActivity(intent);
            }

            finish(); // Close MainActivity
        }, SPLASH_DURATION);
    }

    private void applyTheme() {
        boolean isDarkMode = PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean("dark_mode", false);

        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
}
