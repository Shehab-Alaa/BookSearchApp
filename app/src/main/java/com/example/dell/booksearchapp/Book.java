package com.example.dell.booksearchapp;

import android.os.Parcelable;

/**
 * Created by dell on 1/26/2019.
 */

public class Book  {
    private String bookId;
    private String bookTitle;
    private String bookAuthor;
    private String bookDescription;
    private String bookCoverLink;
    private int isFavorite;
    private String bookCategories;
    private double averageRating;

    public Book()
    {

    }

    public Book(String bookId ,String bookTitle , String bookAuthor , double averageRating , String bookCoverLink ,
                String bookCategories , String bookDescription , int isFavorite)
    {
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.bookAuthor = bookAuthor;
        this.averageRating = averageRating;
        this.bookCoverLink = bookCoverLink;
        this.bookCategories = bookCategories;
        this.bookDescription = bookDescription;
        this.isFavorite = isFavorite;
    }


    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    public void setBookDescription(String bookDescription) {
        this.bookDescription = bookDescription;
    }

    public void setBookCoverLink(String bookCoverLink) {
        this.bookCoverLink = bookCoverLink;
    }

    public void setIsFavorite(int isFavorite) {
        this.isFavorite = isFavorite;
    }

    public void setBookCategories(String bookCategories) {
        this.bookCategories = bookCategories;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public String getBookId() {
        return bookId;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public String getBookDescription() {
        return bookDescription;
    }

    public String getBookCoverLink() {
        return bookCoverLink;
    }

    public int isFavorite() {
        return isFavorite;
    }

    public String getBookCategories() {
        return bookCategories;
    }

    public double getAverageRating() {
        return averageRating;
    }
}
