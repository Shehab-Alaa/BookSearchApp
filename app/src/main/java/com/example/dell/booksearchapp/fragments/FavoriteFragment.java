package com.example.dell.booksearchapp.fragments;

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
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.dell.booksearchapp.models.Book;
import com.example.dell.booksearchapp.adapters.BookSearchAdapter;
import com.example.dell.booksearchapp.R;

import java.util.ArrayList;

/**
 * Created by dell on 1/26/2019.
 */

public class FavoriteFragment extends android.support.v4.app.Fragment {

    private static ArrayList<Book> favoriteBooks;
    private  RecyclerView favoriteBooksList;
    private static Context context;
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

        return view;
    }

    private ArrayList<Book> getFavoriteBooks()
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

   public static void notifyItemRemoved(int position)
   {
       if (adapter != null) {
           favoriteBooks.remove(position);
           adapter.notifyItemRemoved(position);
           adapter.notifyItemRangeChanged(position, favoriteBooks.size());
       }
   }

   public static boolean notifyItemAdded(Book book)
   {
       if (favoriteBooks.contains(book)) {
           Toast.makeText(context, "the book is already added to favorite", Toast.LENGTH_SHORT).show();
           return false;
       }
       else {
           favoriteBooks.add(book);
           if (adapter != null) {
               adapter.notifyDataSetChanged();
           }
       }
       return true;
   }


   public static boolean isFavorite(Book book)
   {
       if (favoriteBooks.contains(book)) {
           return true;
       }
       return false;
   }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            try {
                InputMethodManager mImm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                mImm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                mImm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
            } catch (Exception e) {
                //
            }
        }
    }

}
