package com.example.dell.booksearchapp;

import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by dell on 1/26/2019.
 */

public class FavoriteFragment extends android.support.v4.app.Fragment {

    private static ArrayList<Book> favoriteBooks;
    private  RecyclerView favoriteBooksList;
    private Context context;
    private static BookSearchAdapter adapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.favorite_fragment , container , false);
        favoriteBooks = new ArrayList<>();

        // get favorite books from database
        favoriteBooks = getFavoriteBooks();

        favoriteBooksList = view.findViewById(R.id.favorite_books_list);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        favoriteBooksList.setLayoutManager(layoutManager);
        favoriteBooksList.setHasFixedSize(true);
        adapter = new BookSearchAdapter(context , favoriteBooks , R.layout.book_list_item);
        favoriteBooksList.setAdapter(adapter);

        Toast.makeText(context , "" + favoriteBooks.size() , Toast.LENGTH_LONG).show();
        return view;
    }

    public ArrayList<Book> getFavoriteBooks()
    {
        ArrayList<Book> result = new ArrayList<>();
        Book book;
        String URL = "content://com.example.provider.College/books";
        Uri books = Uri.parse(URL);
        // select all and sort books by there titles
        Cursor cursor = getContext().getContentResolver().query(books,null,null,null,"title");
        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {
            book = new Book();

            book.setBookId(cursor.getString(cursor.getColumnIndex("book_id")));
            book.setBookTitle(cursor.getString(cursor.getColumnIndex("title")));
            book.setBookAuthor(cursor.getString(cursor.getColumnIndex("author")));
            book.setBookDescription(cursor.getString(cursor.getColumnIndex("description")));
            book.setBookCoverLink(cursor.getString(cursor.getColumnIndex("image")));
            book.setIsFavorite(cursor.getInt(cursor.getColumnIndex("favorite")));
            book.setBookCategories(cursor.getString(cursor.getColumnIndex("categories")));
            book.setAverageRating(cursor.getDouble(cursor.getColumnIndex("rate")));

            result.add(book);

            cursor.moveToNext();
        }

        return result;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && adapter !=null)
        {
            adapter.notifyDataSetChanged();
        }
    }

    public static void notifyAdapter()
    {
        adapter.notifyDataSetChanged();
        adapter.notifyItemRangeChanged(0 , favoriteBooks.size());
    }
}
