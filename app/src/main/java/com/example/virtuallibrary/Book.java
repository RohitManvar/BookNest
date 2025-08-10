package com.example.virtuallibrary;

import java.util.Map;

public class Book {
    private String id;
    private String title;
    private String author;
    private String description;
    private String coverUrl;
    private String pdfUrl;
    private String category;
    private boolean isFree;
    private Map<String, Integer> ratings;

    public Book() {
        // Required for Firebase
    }

    public Book(String id, String title, String author, String description, String coverUrl,
                String pdfUrl, String category, boolean isFree, Map<String, Integer> ratings) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.description = description;
        this.coverUrl = coverUrl;
        this.pdfUrl = pdfUrl;
        this.category = category;
        this.isFree = isFree;
        this.ratings = ratings;
    }

    // Getters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getDescription() { return description; }
    public String getCoverUrl() { return coverUrl; }
    public String getPdfUrl() { return pdfUrl; }
    public String getCategory() { return category; }
    public boolean isFree() { return isFree; }
    public Map<String, Integer> getRatings() { return ratings; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setAuthor(String author) { this.author = author; }
    public void setDescription(String description) { this.description = description; }
    public void setCoverUrl(String coverUrl) { this.coverUrl = coverUrl; }
    public void setPdfUrl(String pdfUrl) { this.pdfUrl = pdfUrl; }
    public void setCategory(String category) { this.category = category; }
    public void setFree(boolean free) { isFree = free; }
    public void setRatings(Map<String, Integer> ratings) { this.ratings = ratings; }
}