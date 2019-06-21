package com.example.dell.booksearchapp.activities;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.booksearchapp.R;
import com.example.dell.booksearchapp.fragments.FavoriteFragment;
import com.example.dell.booksearchapp.models.Book;
import com.example.dell.booksearchapp.providers.BooksProvider;
import com.squareup.picasso.Picasso;

import com.example.dell.booksearchapp.providers.BooksProvider;

public class BookInformation extends AppCompatActivity {

    private ImageView info_bookCover;
    private TextView info_bookTitle;
    private TextView info_categories;
    private TextView info_author;
    private TextView book_description;
    private ImageView favorite;
    private TextView info_averageRate;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    private Book book;
    private int bookPosition;

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
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingtoolbar_id);
        collapsingToolbarLayout.setTitleEnabled(true);

        book = (Book)getIntent().getExtras().getSerializable("currentBook");
        bookPosition = getIntent().getExtras().getInt("PositionList");

        // set Book information

        collapsingToolbarLayout.setTitle(book.getBookTitle());
        info_bookTitle.setText(book.getBookTitle());
        info_averageRate.setText(String.valueOf(book.getAverageRating()));
        info_author.setText(book.getBookAuthor());
        if (book.isFavorite() == 0) {
            favorite.setBackgroundResource(R.drawable.unfavorite);
        }
        else {
            favorite.setBackgroundResource(R.drawable.favorite_tab_icon);
        }
        info_categories.setText(book.getBookCategories());
        book_description.setText(book.getBookDescription());

        //Picasso.get().load(book.getBookCoverLink()).into(info_bookCover);
        MainActivity.picassoInstance.load(book.getBookCoverLink()).into(info_bookCover);


        favorite.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {

                if (book.isFavorite() == 0) {
                    book.setIsFavorite(1);
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

    private void addToDatabase()
    {
        ContentValues values = new ContentValues();

        values.put("book_id" , book.getBookId());
        values.put("title" , book.getBookTitle());
        values.put("author" , book.getBookAuthor());
        values.put("description" , book.getBookDescription());
        values.put("image" , book.getBookCoverLink());
        values.put("favorite" , book.isFavorite());
        values.put("categories" , book.getBookCategories());
        values.put("rate" , book.getAverageRating());

        Uri uri = getContentResolver().insert(BooksProvider.CONTENT_URI , values);
        Toast.makeText(this , "the book is added to favorite" , Toast.LENGTH_SHORT).show();

    }

    private void deleteFromDatabase()
    {
        int rowAffected = getContentResolver().delete(BooksProvider.CONTENT_URI , null ,new String[]{book.getBookId()});
        if (rowAffected > 0)
            Toast.makeText(this , "the book is removed from favorite" , Toast.LENGTH_SHORT).show();
    }
}
