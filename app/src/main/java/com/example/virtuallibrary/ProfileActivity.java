package com.example.virtuallibrary;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

public class ProfileActivity extends AppCompatActivity {

    // Header / profile
    private ImageView closeButton;
    private TextView userNameText, userEmailText;

    // Menu items
    private LinearLayout notificationsItem,
            bookNestSettingsItem,
            helpFeedbackItem;

    // Footer links
    private TextView privacyPolicyText,
            termsOfServiceText,
            aboutBookNestText;

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Make sure this layout name matches the file you posted
        setContentView(R.layout.activity_profile);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        bindViews();
        loadUserData();
        setUpClickListeners();
    }

    /** Find all views defined in the XML */
    private void bindViews() {
        closeButton          = findViewById(R.id.closeButton);
        userNameText         = findViewById(R.id.userNameText);
        userEmailText        = findViewById(R.id.userEmailText);

        notificationsItem    = findViewById(R.id.notificationsItem);
        bookNestSettingsItem = findViewById(R.id.bookNestSettingsItem);
        helpFeedbackItem     = findViewById(R.id.helpFeedbackItem);

        privacyPolicyText    = findViewById(R.id.privacyPolicyText);
        termsOfServiceText   = findViewById(R.id.termsOfServiceText);
        aboutBookNestText    = findViewById(R.id.aboutBookNestText);
    }

    /** Populate profile name/email from SharedPreferences (or defaults) */
    private void loadUserData() {
        String userName  = prefs.getString("user_name",  "Rohit Manvar");
        String userEmail = prefs.getString("user_email", "rohitmanvar123@gmail.com");

        userNameText.setText(userName);
        userEmailText.setText(userEmail);
    }

    /** Wire up all click actions */
    private void setUpClickListeners() {

        // Close sheet / activity
        closeButton.setOnClickListener(v -> finish());

        // Notifications
        notificationsItem.setOnClickListener(v ->
                Toast.makeText(this, "Open notifications screen", Toast.LENGTH_SHORT).show()
        );

        // App-level settings
        bookNestSettingsItem.setOnClickListener(v -> {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        });

        // Help & feedback – send email example
        helpFeedbackItem.setOnClickListener(v -> {
            Intent email = new Intent(Intent.ACTION_SENDTO,
                    Uri.parse("mailto:support@booknest.app?subject=Help%20%26%20Feedback"));
            startActivity(Intent.createChooser(email, "Send feedback"));
        });

        // Footer links
        privacyPolicyText.setOnClickListener(v ->
                openLink("https://booknest.app/privacy"));

        termsOfServiceText.setOnClickListener(v ->
                openLink("https://booknest.app/terms"));

        aboutBookNestText.setOnClickListener(v ->
                openLink("https://booknest.app/about"));
    }

    private void openLink(String url) {
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(i);
    }

    /* Refresh profile info when returning from a child screen */
    @Override
    protected void onResume() {
        super.onResume();
        loadUserData();
    }
}
