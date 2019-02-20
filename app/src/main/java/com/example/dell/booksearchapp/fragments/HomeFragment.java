package com.example.dell.booksearchapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;

import com.example.dell.booksearchapp.models.Book;
import com.example.dell.booksearchapp.adapters.BookSearchAdapter;
import com.example.dell.booksearchapp.converters.JsonParsing;
import com.example.dell.booksearchapp.patterns.Observer;
import com.example.dell.booksearchapp.R;

import java.util.ArrayList;

/**
 * Created by dell on 1/26/2019.
 */

public class HomeFragment extends android.support.v4.app.Fragment implements Observer {

    private ArrayList<Book> books_popularBooks , books_fictionBooks , books_businessBooks , books_historyBooks
    , books_internationalBooks , books_sportBooks ;
    private  RecyclerView popularBooksList , fictionBooksList , businessBooksList
            , historyBooksList , internationalBooksList , sportBooksList;
    private BookSearchAdapter popularBooksAdapter , fictionBooksAdapter , businessBooksAdapter
            , historyBooksAdapter , internationalBooksAdapter , sportBooksAdapter;

    private ProgressBar categoriesLoading;
    private JsonParsing parseData ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment , container , false);

        parseData = new JsonParsing(getContext());
        parseData.addObserver(HomeFragment.this);

        categoriesLoading = view.findViewById(R.id.categories_loading);

        categoriesLoading.setVisibility(View.VISIBLE);

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

    private void setCategory(ArrayList<Book> book , RecyclerView list, BookSearchAdapter adapter , String search)
    {
        // set category books
        book = new ArrayList<>();
        parseData.convertData(search , book);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity() , LinearLayoutManager.HORIZONTAL , false);
        list.setLayoutManager(manager);
        list.setHasFixedSize(true);
        adapter = new BookSearchAdapter(getContext() , book , R.layout.book_category_item);
        list.setAdapter(adapter);
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

    @Override
    public void notifyAdapter() {
        categoriesLoading.setVisibility(View.GONE);
        if (popularBooksList.getAdapter() != null && fictionBooksList.getAdapter() !=null && businessBooksList.getAdapter()!=null
                && historyBooksList.getAdapter() !=null && internationalBooksList.getAdapter()!=null && sportBooksList.getAdapter()!=null)
        {
            popularBooksList.getAdapter().notifyDataSetChanged();
            fictionBooksList.getAdapter().notifyDataSetChanged();
            businessBooksList.getAdapter().notifyDataSetChanged();
            historyBooksList.getAdapter().notifyDataSetChanged();
            internationalBooksList.getAdapter().notifyDataSetChanged();
            sportBooksList.getAdapter().notifyDataSetChanged();
        }
    }
}
