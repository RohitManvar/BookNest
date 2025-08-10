package com.example.virtuallibrary;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class FavoritesActivity extends AppCompatActivity {
    private RecyclerView favoritesRecyclerView;
    private BookAdapter bookAdapter;
    private List<Book> favoriteBooks;
    private List<Book> allFavoriteBooks;
    private TabLayout favoriteTabs;
    private TextView favoritesCount;
    private ImageView searchIcon, sortIcon, layoutToggle;
    private LinearLayout emptyStateLayout;
    private MaterialButton exploreButton;
    private BottomNavigationView bottomNavigation;

    private boolean isGridView = true;
    private String currentFilter = "All";

    private final String currentUserId = "sample_user_id"; // Replace with actual FirebaseAuth.getUid()

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        initViews();
        setupToolbar();
        setupTabs();
        initRecyclerView();
        setupClickListeners();
        setupBottomNavigation();
        loadFavoriteBooks();
    }

    private void initViews() {
        favoritesRecyclerView = findViewById(R.id.favoritesRecyclerView);
        favoriteTabs = findViewById(R.id.favoriteTabs);
        favoritesCount = findViewById(R.id.favoritesCount);
        searchIcon = findViewById(R.id.searchIcon);
        sortIcon = findViewById(R.id.sortIcon);
        layoutToggle = findViewById(R.id.layoutToggle);
        emptyStateLayout = findViewById(R.id.emptyStateLayout);
        exploreButton = findViewById(R.id.exploreButton);
        bottomNavigation = findViewById(R.id.bottomNavigation);
    }

    private void setupToolbar() {
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    private void setupTabs() {
        favoriteTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        currentFilter = "All";
                        break;
                    case 1:
                        currentFilter = "Ebooks"; // placeholder category
                        break;
                    case 2:
                        currentFilter = "Audiobooks"; // placeholder category
                        break;
                }
                filterFavorites();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void initRecyclerView() {
        favoriteBooks = new ArrayList<>();
        allFavoriteBooks = new ArrayList<>();
        bookAdapter = new BookAdapter(this, favoriteBooks, true);

        updateLayoutManager();
        favoritesRecyclerView.setAdapter(bookAdapter);
    }

    private void updateLayoutManager() {
        if (isGridView) {
            favoritesRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            layoutToggle.setImageResource(R.drawable.ic_grid);
        } else {
            favoritesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            layoutToggle.setImageResource(R.drawable.ic_list);
        }
    }

    private void setupClickListeners() {
        searchIcon.setOnClickListener(v -> {
            // TODO: Launch search activity
        });

        sortIcon.setOnClickListener(v -> {
            showSortDialog();
        });

        layoutToggle.setOnClickListener(v -> {
            isGridView = !isGridView;
            updateLayoutManager();
        });

        exploreButton.setOnClickListener(v -> finish());
    }

    private void setupBottomNavigation() {
        NavigationHelper.setupBottomNavigation(this, bottomNavigation, R.id.nav_favorites);
    }

    private void loadFavoriteBooks() {
        allFavoriteBooks.clear();

        DatabaseHelper.getInstance().getFavoriteBooks(currentUserId, new DatabaseHelper.BooksCallback() {
            @Override
            public void onSuccess(List<Book> books) {
                allFavoriteBooks.addAll(books);
                filterFavorites();
            }

            @Override
            public void onError(String error) {
                favoritesCount.setText("Error loading favorites");
                emptyStateLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    private void filterFavorites() {
        favoriteBooks.clear();

        if (currentFilter.equals("All")) {
            favoriteBooks.addAll(allFavoriteBooks);
        } else {
            for (Book book : allFavoriteBooks) {
                if (book.getCategory() != null &&
                        book.getCategory().toLowerCase().contains(currentFilter.toLowerCase())) {
                    favoriteBooks.add(book);
                }
            }
        }

        updateUI();
    }

    private void updateUI() {
        if (favoriteBooks.isEmpty()) {
            favoritesRecyclerView.setVisibility(View.GONE);
            emptyStateLayout.setVisibility(View.VISIBLE);
            favoritesCount.setText("No favorites");
        } else {
            favoritesRecyclerView.setVisibility(View.VISIBLE);
            emptyStateLayout.setVisibility(View.GONE);

            String countText = favoriteBooks.size() == 1 ?
                    "1 favorite" : favoriteBooks.size() + " favorites";
            favoritesCount.setText(countText);
        }

        bookAdapter.notifyDataSetChanged();
    }

    private void showSortDialog() {
        // TODO: Implement sort feature
    }

    public void removeFromFavorites(Book book) {
        DatabaseHelper.getInstance().removeBookFromFavorites(currentUserId, book.getId());
        allFavoriteBooks.remove(book);
        filterFavorites();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadFavoriteBooks();
    }
}
