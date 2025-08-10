package com.example.virtuallibrary;

import static com.example.virtuallibrary.DatabaseHelper.getInstance;
import com.example.virtuallibrary.BookAdapter;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";

    private MaterialToolbar toolbar;
    private BottomNavigationView bottomNavigation;
    private ImageView profileIcon;
    private MaterialCardView searchCard;
    private TabLayout bookTypeTabs;

    private RecyclerView currentReadingRecyclerView;
    private RecyclerView topSellersRecyclerView;
    private RecyclerView freePreviewRecyclerView;
    private RecyclerView priceDropsRecyclerView;
    private RecyclerView newReleasesRecyclerView;
    private RecyclerView reducedEbooksRecyclerView;

    private MaterialCardView genresCard;
    private MaterialCardView topSellingCard;

    private BookAdapter currentReadingAdapter;
    private BookAdapter topSellersAdapter;
    private BookAdapter freePreviewAdapter;
    private BookAdapter priceDropsAdapter;
    private BookAdapter newReleasesAdapter;
    private BookAdapter reducedEbooksAdapter;

    private List<Book> currentReadingBooks;
    private List<Book> topSellersBooks;
    private List<Book> freePreviewBooks;
    private List<Book> priceDropsBooks;
    private List<Book> newReleasesBooks;
    private List<Book> reducedEbooksBooks;

    private DatabaseHelper databaseHelper;

    private String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        databaseHelper = getInstance();

        BottomNavigationView bottomNavigation = findViewById(R.id.bottomNavigation);
        NavigationHelper.setupBottomNavigation(this, bottomNavigation, R.id.nav_home);

        initViews();
        setupToolbar();
        setupTabs();
        setupRecyclerViews();
        setupClickListeners();

        loadDataFromFirebase();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        bottomNavigation = findViewById(R.id.bottomNavigation);
        profileIcon = findViewById(R.id.profileIcon);
        searchCard = findViewById(R.id.searchCard);
        bookTypeTabs = findViewById(R.id.bookTypeTabs);

        currentReadingRecyclerView = findViewById(R.id.currentReadingRecyclerView);
        topSellersRecyclerView = findViewById(R.id.topSellersRecyclerView);
        freePreviewRecyclerView = findViewById(R.id.freePreviewRecyclerView);
        priceDropsRecyclerView = findViewById(R.id.priceDropsRecyclerView);
        newReleasesRecyclerView = findViewById(R.id.newReleasesRecyclerView);
        reducedEbooksRecyclerView = findViewById(R.id.reducedEbooksRecyclerView);

        currentReadingBooks = new ArrayList<>();
        topSellersBooks = new ArrayList<>();
        freePreviewBooks = new ArrayList<>();
        priceDropsBooks = new ArrayList<>();
        newReleasesBooks = new ArrayList<>();
        reducedEbooksBooks = new ArrayList<>();
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    private void setupTabs() {
        bookTypeTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                switch (position) {
                    case 0:
                        showEbooksContent();
                        break;
                    case 1:
                        showAudiobooksContent();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void setupRecyclerViews() {
        currentReadingAdapter = new BookAdapter(this, currentReadingBooks, false);
        currentReadingRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        currentReadingRecyclerView.setAdapter(currentReadingAdapter);

        topSellersAdapter = new BookAdapter(this, topSellersBooks, false);
        topSellersRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        topSellersRecyclerView.setAdapter(topSellersAdapter);

        freePreviewAdapter = new BookAdapter(this, freePreviewBooks, false);
        freePreviewRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        freePreviewRecyclerView.setAdapter(freePreviewAdapter);

        priceDropsAdapter = new BookAdapter(this, priceDropsBooks, false);
        priceDropsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        priceDropsRecyclerView.setAdapter(priceDropsAdapter);

        newReleasesAdapter = new BookAdapter(this, newReleasesBooks, false);
        newReleasesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        newReleasesRecyclerView.setAdapter(newReleasesAdapter);

        reducedEbooksAdapter = new BookAdapter(this, reducedEbooksBooks, false);
        reducedEbooksRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        reducedEbooksRecyclerView.setAdapter(reducedEbooksAdapter);
    }

    private void setupClickListeners() {
        searchCard.setOnClickListener(v -> {});
        profileIcon.setOnClickListener(v -> startActivity(new Intent(this, ProfileActivity.class)));
    }

    private void loadDataFromFirebase() {
        loadCurrentReadingBooks();
        loadTopSellersBooks();
        loadAllBooksAndFilter();
    }

    private void loadCurrentReadingBooks() {
        databaseHelper.getCurrentReadingBooks(currentUserId, new DatabaseHelper.BooksCallback() {
            @Override
            public void onSuccess(List<Book> books) {
                currentReadingBooks.clear();
                currentReadingBooks.addAll(books);
                currentReadingAdapter.notifyDataSetChanged();
                Log.d(TAG, "Current reading books loaded: " + books.size());
            }

            @Override
            public void onError(String error) {
                Log.e(TAG, "Error loading current reading books: " + error);
            }
        });
    }

    private void loadTopSellersBooks() {
        databaseHelper.getBooksByCategory("bestseller", new DatabaseHelper.BooksCallback() {
            @Override
            public void onSuccess(List<Book> books) {
                topSellersBooks.clear();
                topSellersBooks.addAll(books);
                topSellersAdapter.notifyDataSetChanged();
                Log.d(TAG, "Top sellers books loaded: " + books.size());
            }

            @Override
            public void onError(String error) {
                Log.e(TAG, "Error loading top sellers books: " + error);
            }
        });
    }

    private void loadAllBooksAndFilter() {
        databaseHelper.getAllBooks(new DatabaseHelper.BooksCallback() {
            @Override
            public void onSuccess(List<Book> books) {
                filterBooksForSections(books);
                Log.d(TAG, "All books loaded and filtered: " + books.size());
            }

            @Override
            public void onError(String error) {
                Log.e(TAG, "Error loading all books: " + error);
            }
        });
    }

    private void filterBooksForSections(List<Book> allBooks) {
        freePreviewBooks.clear();
        priceDropsBooks.clear();
        newReleasesBooks.clear();
        reducedEbooksBooks.clear();

        for (Book book : allBooks) {
            String category = book.getCategory();
            if (category != null) {
                switch (category) {
                    case "Classic":
                        freePreviewBooks.add(book);
                        break;
                    case "Self-Help":
                        priceDropsBooks.add(book);
                        break;
                    case "Top Sellers":
                        newReleasesBooks.add(book);
                        break;
                    case "Finance":
                        reducedEbooksBooks.add(book);
                        break;
                }
            }
        }

        freePreviewAdapter.notifyDataSetChanged();
        priceDropsAdapter.notifyDataSetChanged();
        newReleasesAdapter.notifyDataSetChanged();
        reducedEbooksAdapter.notifyDataSetChanged();
    }

    private void showEbooksContent() {
        Toast.makeText(this, "Showing Ebooks", Toast.LENGTH_SHORT).show();
    }

    private void showAudiobooksContent() {
        Toast.makeText(this, "Showing Audiobooks", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        bottomNavigation.setSelectedItemId(R.id.nav_home);
    }
}
