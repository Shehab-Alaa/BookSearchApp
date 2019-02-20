package com.example.dell.booksearchapp.converters;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.dell.booksearchapp.models.Book;
import com.example.dell.booksearchapp.patterns.Observer;
import com.example.dell.booksearchapp.patterns.Subject;
import com.example.dell.booksearchapp.patterns.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by dell on 1/26/2019.
 */

public class JsonParsing  implements Subject {

    private String url = "https://www.googleapis.com/books/v1/volumes?q=";
    private String maxBooks = "&maxResults=40";
    private Context context;
    private ArrayList<Observer> observers  = new ArrayList<>();

    public JsonParsing(Context context)
    {
        this.context = context;
    }

    public void convertData(String searchInput , final ArrayList<Book> books ) {

        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url + searchInput + maxBooks, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONArray items = response.getJSONArray("items");

                            for (int i =0;i<items.length();i++)
                            {
                               JSONObject item_book = items.getJSONObject(i);
                               String bookId = null;
                               String bookTitle = null;
                               String bookAuthor = null;
                               String bookDescription = null;
                               int isFavorite = 0;
                               String categories = "";
                               String bookImageLink = null;
                               double averageRating = 0.0;

                               bookId = item_book.getString("id");

                               JSONObject volumeInfo = item_book.getJSONObject("volumeInfo");
                              try {
                                  bookTitle = volumeInfo.getString("title");

                                  JSONArray authors = volumeInfo.getJSONArray("authors");
                                  bookAuthor = authors.getString(0);

                                  bookDescription = volumeInfo.getString("description");

                                  JSONArray bookCategories = volumeInfo.getJSONArray("categories");
                                  for (int c = 0;c < bookCategories.length();c++)
                                  {
                                      categories += bookCategories.getString(c);
                                      if ((c+1) != bookCategories.length())
                                          categories += " / ";
                                  }

                                  JSONObject imageLinks = volumeInfo.getJSONObject("imageLinks");
                                  bookImageLink = imageLinks.getString("thumbnail");
                                  averageRating = volumeInfo.getDouble("averageRating");
                              }catch (Exception e)
                              {

                              }

                              if (bookId != null && bookTitle != null && bookAuthor != null
                                      && bookDescription != null && bookImageLink != null) {
                                  Book book = new Book(bookId, bookTitle, bookAuthor, averageRating, bookImageLink
                                          , categories, bookDescription, isFavorite);
                                  books.add(book);
                              }

                            }

                            notifyAllObservers();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
             //
            }
        });

        VolleySingleton.getInstance(context).addRequest(request);

    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyAllObservers() {
       for (Observer observer : observers)
       {
           observer.notifyAdapter();
       }
    }
}
