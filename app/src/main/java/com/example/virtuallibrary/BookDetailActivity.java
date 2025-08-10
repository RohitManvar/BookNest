package com.example.virtuallibrary;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class BookDetailActivity extends AppCompatActivity {

    private ImageView bookImageView;
    private TextView titleTextView, authorTextView, descriptionTextView, categoryTextView;
    private RatingBar ratingBar;
    private Button readButton, downloadButton;
    private FloatingActionButton favoriteFab;
    private Book currentBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        initViews();
        setupToolbar();
        loadBookDetails(); // You may replace this with real Firebase logic
        setupClickListeners();
    }

    private void initViews() {
        bookImageView = findViewById(R.id.bookImageView);
        titleTextView = findViewById(R.id.titleTextView);
        authorTextView = findViewById(R.id.authorTextView);
        descriptionTextView = findViewById(R.id.descriptionTextView);
        categoryTextView = findViewById(R.id.categoryTextView);
        ratingBar = findViewById(R.id.ratingBar);
        readButton = findViewById(R.id.readButton);
        downloadButton = findViewById(R.id.downloadButton);
        favoriteFab = findViewById(R.id.favoriteFab);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void loadBookDetails() {
        // Normally you’d fetch from Firebase using bookId
        String bookId = getIntent().getStringExtra("bookId");

        currentBook = new Book(
                bookId,
                "Atomic Habits",
                "James Clear",
                "An easy & proven way to build good habits.",
                "Self-Help",
                "https://example.com/atomic.jpg",
                "https://example.com/atomic.pdf",
                true,
                null // You can set a map of ratings later
        );

        displayBookDetails();
    }

    private void displayBookDetails() {
        titleTextView.setText(currentBook.getTitle());
        authorTextView.setText("by " + currentBook.getAuthor());
        descriptionTextView.setText(currentBook.getDescription());
        categoryTextView.setText(currentBook.getCategory());

        // Calculate average rating (optional logic)
        if (currentBook.getRatings() != null && !currentBook.getRatings().isEmpty()) {
            float total = 0;
            for (int value : currentBook.getRatings().values()) {
                total += value;
            }
            float average = total / currentBook.getRatings().size();
            ratingBar.setRating(average);
        } else {
            ratingBar.setRating(0f);
        }

        Glide.with(this)
                .load(currentBook.getCoverUrl())
                .placeholder(R.drawable.book_placeholder)
                .error(R.drawable.book_placeholder)
                .into(bookImageView);
    }

    private void setupClickListeners() {
        readButton.setOnClickListener(v -> {
            Toast.makeText(this, "Opening PDF reader...", Toast.LENGTH_SHORT).show();
            // TODO: Open PDF reader with currentBook.getPdfUrl()
        });

        downloadButton.setOnClickListener(v -> {
            Toast.makeText(this, "Downloading book...", Toast.LENGTH_SHORT).show();
            // TODO: Implement file download from currentBook.getPdfUrl()
        });

        favoriteFab.setOnClickListener(v -> {
            Toast.makeText(this, "Favorite toggled (implement logic)", Toast.LENGTH_SHORT).show();
            // TODO: Implement Firebase favorite toggle
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
