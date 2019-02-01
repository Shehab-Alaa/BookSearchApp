package com.example.dell.booksearchapp;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

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
    private static RecyclerView popularBooksList , fictionBooksList , businessBooksList
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
        parseData.convertData(search , book ,3);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity() , LinearLayoutManager.HORIZONTAL , false);
        list.setLayoutManager(manager);
        list.setHasFixedSize(true);
        adapter = new BookSearchAdapter(getContext() , book , R.layout.book_category_item);
        list.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public static void notifyCategoriesAdapters()
    {
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
