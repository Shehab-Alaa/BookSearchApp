package com.example.dell.booksearchapp;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

/**
 * Created by dell on 1/26/2019.
 */

public class HomeFragment extends android.support.v4.app.Fragment{

    private ArrayList<Book> books_popularBooks , books_fictionBooks , books_businessBooks , books_historyBooks
    , books_internationalBooks , books_sportBooks ;
    private RecyclerView popularBooksList , fictionBooksList , businessBooksList
            , historyBooksList , internationalBooksList , sportBooksList;
    private BookSearchAdapter popularBooksAdapter , fictionBooksAdapter , businessBooksAdapter
            , historyBooksAdapter , internationalBooksAdapter , sportBooksAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment , container , false);



        popularBooksList = view.findViewById(R.id.list_popularBooks);
        setCategory(books_popularBooks , popularBooksList , popularBooksAdapter , "popularbooks&printType=books");

        fictionBooksList = view.findViewById(R.id.list_fictionBooks);
        setCategory(books_fictionBooks , fictionBooksList , fictionBooksAdapter , "fictions&printType=books");

        businessBooksList = view.findViewById(R.id.list_business);
        setCategory(books_businessBooks , businessBooksList , businessBooksAdapter , "business&printType=books");

        historyBooksList = view.findViewById(R.id.list_history);
        setCategory(books_historyBooks , historyBooksList , historyBooksAdapter , "history&printType=books");

        internationalBooksList = view.findViewById(R.id.list_internationalbooks);
        setCategory(books_internationalBooks , internationalBooksList , internationalBooksAdapter , "internationalbooks&printType=books");

        sportBooksList = view.findViewById(R.id.list_sport);
        setCategory(books_sportBooks , sportBooksList , sportBooksAdapter , "sport&printType=books");


        return view;
    }

    public void setCategory(ArrayList<Book> book , RecyclerView list, BookSearchAdapter adapter , String search)
    {
        JsonParsing parseData = new JsonParsing(getContext());
        // set category books
        book = new ArrayList<>();
        book = parseData.convertData(search);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity() , LinearLayoutManager.HORIZONTAL , false);
        list.setLayoutManager(manager);
        list.setHasFixedSize(true);
        adapter = new BookSearchAdapter(getContext() , book , R.layout.book_category_item);
        list.setAdapter(adapter);
    }
}
