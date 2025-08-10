package com.example.virtuallibrary;

import android.app.Activity;
import android.content.Intent;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class NavigationHelper {

    public static void setupBottomNavigation(Activity activity, BottomNavigationView bottomNavigation, int currentPageId) {
        // Set the current item as selected
        bottomNavigation.setSelectedItemId(currentPageId);

        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            // Don't navigate if already on the current page
            if (itemId == currentPageId) {
                return true;
            }

            Intent intent = null;

            if (itemId == R.id.nav_home) {
                intent = new Intent(activity, HomeActivity.class);
            } else if (itemId == R.id.nav_library) {
                //intent = new Intent(activity, LibraryActivity.class);
            } else if (itemId == R.id.nav_favorites) {
                intent = new Intent(activity, FavoritesActivity.class);
            } else if (itemId == R.id.profileIcon) {
                //intent = new Intent(activity, ProfileA.class);
            }

            if (intent != null) {
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                activity.startActivity(intent);

                // Add smooth transition animation
                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                return true;
            }

            return false;
        });
    }
}