package com.example.dell.booksearchapp;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class BookInformation extends AppCompatActivity {

    private ImageView info_bookCover;
    private TextView info_bookTitle;
    private TextView info_categories;
    private TextView info_author;
    private TextView book_description;
    private ImageView favorite;
    private TextView info_averageRate;

    private int bookPosition;
    private String bookId;
    private String bookTitle;
    private String bookAuthor;
    private String bookDescription;
    private String bookCoverLink;
    private int isFavorite;
    private String bookCategories;
    private double averageRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_information);

        info_author = findViewById(R.id.info_bookAuthor);
        info_averageRate = findViewById(R.id.info_averageRate);
        info_bookCover = findViewById(R.id.info_bookCover);
        info_bookTitle = findViewById(R.id.info_bookTitle);
        info_categories = findViewById(R.id.info_bookCategories);
        book_description = findViewById(R.id.info_description);
        favorite = findViewById(R.id.info_favorite);


        bookPosition = getIntent().getExtras().getInt("PositionList"); // to remove item from favorite using it's position
        bookId = getIntent().getExtras().getString("BookId");
        bookTitle = getIntent().getExtras().getString("BookTitle");
        bookAuthor = getIntent().getExtras().getString("BookAuthor");
        bookDescription = getIntent().getExtras().getString("BookDescription");
        bookCoverLink = getIntent().getExtras().getString("BookCover");
        isFavorite = getIntent().getExtras().getInt("Favorite");
        bookCategories = getIntent().getExtras().getString("Categories");
        averageRating = getIntent().getExtras().getDouble("AverageRating");


        // set Book information
        info_bookTitle.setText(bookTitle);
        info_averageRate.setText(String.valueOf(averageRating));
        info_author.setText(bookAuthor);
        if (isFavorite == 0) {
            favorite.setBackgroundResource(R.drawable.unfavorite);
        }
        else {
            favorite.setBackgroundResource(R.drawable.favorite_tab_icon);
        }
        info_categories.setText(bookCategories);
        book_description.setText(bookDescription);
        Picasso.get().load(bookCoverLink).into(info_bookCover);


        final Book book = new Book(bookId , bookTitle ,  bookAuthor ,  averageRating ,  bookCoverLink ,
                 bookCategories ,  bookDescription ,  1); // to add it to favorite list

        favorite.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {

                if (isFavorite == 0) {
                    favorite.setBackgroundResource(R.drawable.favorite_tab_icon);
                    boolean unique = FavoriteFragment.notifyItemAdded(book);
                    if (unique)
                        addToDatabase();
                    // else it already exist in favorite list you cannot add it twice
                }
                else {
                    favorite.setBackgroundResource(R.drawable.unfavorite);
                    deleteFromDatabase();
                    FavoriteFragment.notifyItemRemoved(bookPosition);
                    finish();
                }
            }
        });
    }

    public void addToDatabase()
    {
        ContentValues values = new ContentValues();

        values.put("book_id" , bookId);
        values.put("title" , bookTitle);
        values.put("author" , bookAuthor);
        values.put("description" , bookDescription);
        values.put("image" , bookCoverLink);
        values.put("favorite" , 1);
        values.put("categories" , bookCategories);
        values.put("rate" , averageRating);

        Uri uri = getContentResolver().insert(BooksProvider.CONTENT_URI , values);
        Toast.makeText(this , "the book is added to favorite" , Toast.LENGTH_SHORT).show();

    }

    public void deleteFromDatabase()
    {
        int rowAffected = getContentResolver().delete(BooksProvider.CONTENT_URI , null ,new String[]{bookId});
        if (rowAffected > 0)
            Toast.makeText(this , "the book is removed from favorite" , Toast.LENGTH_SHORT).show();
    }
}
