package com.example.virtuallibrary;
import com.example.virtuallibrary.Book;
import android.util.Log;
import androidx.annotation.NonNull;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper {

    private final DatabaseReference databaseReference;

    public DatabaseHelper() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    // Singleton instance
    private static DatabaseHelper instance;
    public static DatabaseHelper getInstance() {
        if (instance == null) {
            instance = new DatabaseHelper();
        }
        return instance;
    }

    // Callback interface
    public interface BooksCallback {
        void onSuccess(List<Book> books);
        void onError(String error);
    }

    /**
     * 1. Get all books from Firebase
     */
    public void getAllBooks(BooksCallback callback) {
        databaseReference.child("books").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Book> books = new ArrayList<>();
                for (DataSnapshot bookSnapshot : snapshot.getChildren()) {
                    Book book = bookSnapshot.getValue(Book.class);
                    if (book != null) {
                        books.add(book);
                    }
                }
                callback.onSuccess(books);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onError("Error fetching all books: " + error.getMessage());
            }
        });
    }

    /**
     * 2. Get books by category (e.g., "Top Sellers", "Classic")
     */
    public void getBooksByCategory(String category, BooksCallback callback) {
        getAllBooks(new BooksCallback() {
            @Override
            public void onSuccess(List<Book> allBooks) {
                List<Book> filtered = new ArrayList<>();
                for (Book book : allBooks) {
                    if (book.getCategory() != null &&
                            book.getCategory().equalsIgnoreCase(category)) {
                        filtered.add(book);
                    }
                }
                callback.onSuccess(filtered);
            }

            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
    }

    /**
     * 3. Get current reading books using readingHistory
     */
    public void getCurrentReadingBooks(String userId, BooksCallback callback) {
        databaseReference.child("users").child(userId).child("readingHistory")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<String> bookIds = new ArrayList<>();
                        for (DataSnapshot item : snapshot.getChildren()) {
                            String bookId = item.child("bookId").getValue(String.class);
                            if (bookId != null) {
                                bookIds.add(bookId);
                            }
                        }

                        getAllBooks(new BooksCallback() {
                            @Override
                            public void onSuccess(List<Book> allBooks) {
                                List<Book> readingBooks = new ArrayList<>();
                                for (Book book : allBooks) {
                                    if (bookIds.contains(book.getId())) {
                                        readingBooks.add(book);
                                    }
                                }
                                callback.onSuccess(readingBooks);
                            }

                            @Override
                            public void onError(String error) {
                                callback.onError(error);
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        callback.onError("Error fetching reading history: " + error.getMessage());
                    }
                });
    }

    /**
     * 4. Add book to favorites
     */
    public void addBookToFavorites(String userId, String bookId) {
        databaseReference.child("users").child(userId).child("favorites").child(bookId).setValue(true);
    }

    /**
     * 5. Remove book from favorites
     */
    public void removeBookFromFavorites(String userId, String bookId) {
        databaseReference.child("users").child(userId).child("favorites").child(bookId).removeValue();
    }

    /**
     * 6. Get favorite books from /users/{uid}/favorites
     */
    public void getFavoriteBooks(String userId, BooksCallback callback) {
        databaseReference.child("users").child(userId).child("favorites")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot favSnapshot) {
                        List<String> favIds = new ArrayList<>();
                        for (DataSnapshot snapshot : favSnapshot.getChildren()) {
                            favIds.add(snapshot.getKey());
                        }

                        getAllBooks(new BooksCallback() {
                            @Override
                            public void onSuccess(List<Book> allBooks) {
                                List<Book> favBooks = new ArrayList<>();
                                for (Book book : allBooks) {
                                    if (book.getId() != null && favIds.contains(book.getId())) {
                                        favBooks.add(book);
                                    }
                                }
                                callback.onSuccess(favBooks);
                            }

                            @Override
                            public void onError(String error) {
                                callback.onError(error);
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        callback.onError("Error fetching favorites: " + error.getMessage());
                    }
                });
    }
}
