// Spencer Jones
// MDV3832-0 - 062024
// Book.java

package com.example.jonesspencer_ce07;

public class Book {
    // Variables to store book title and image URL
    private String title;
    private String imageUrl;

    // Constructor
    public Book(String title, String imageUrl) {
        this.title = title;
        this.imageUrl = imageUrl;
    }

    // Getter method for book title
    public String getTitle() {
        return title;
    }

    // Getter method for book image URL
    public String getImageUrl() {
        return imageUrl;
    }
}