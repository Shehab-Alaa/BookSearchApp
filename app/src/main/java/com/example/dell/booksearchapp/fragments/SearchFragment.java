package com.example.dell.booksearchapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.booksearchapp.models.Book;
import com.example.dell.booksearchapp.adapters.BookSearchAdapter;
import com.example.dell.booksearchapp.converters.JsonParsing;
import com.example.dell.booksearchapp.patterns.Observer;
import com.example.dell.booksearchapp.R;
import com.example.dell.booksearchapp.providers.SearchSuggestions;

import java.util.ArrayList;
import java.util.Locale;

import javax.xml.transform.Result;

import static android.app.Activity.RESULT_OK;

/**
 * Created by dell on 1/26/2019.
 */


public class SearchFragment extends android.support.v4.app.Fragment implements Observer {

    private AutoCompleteTextView searchBox;
    private RecyclerView booksList;
    private ArrayList<Book> books;
    private BookSearchAdapter adapter;
    private Context context;
    private ProgressBar searchLoading;
    private JsonParsing parseData;
    private String[] searchSuggestions ;
    private SearchSuggestions databaseSuggestions ;
    private static ArrayAdapter<String> searchAdapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment , container , false);

        searchBox = view.findViewById(R.id.searchBox);
        booksList = view.findViewById(R.id.booksList);
        searchLoading = view.findViewById(R.id.search_loading);

        parseData = new JsonParsing(context);
        parseData.addObserver(SearchFragment.this);

        books = new ArrayList<>();

        LinearLayoutManager manager = new LinearLayoutManager(context);
        booksList.setLayoutManager(manager);
        booksList.setHasFixedSize(true);

        adapter = new BookSearchAdapter(context ,books , R.layout.book_list_item);
        booksList.setAdapter(adapter);

        databaseSuggestions = new SearchSuggestions(getContext());
        notifySuggestionsAdapter();



        searchBox.setOnEditorActionListener(editorListener);
        searchBox.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                speechInput(v);
                return true;
            }
        });

        return view;
    }


    // Override search button in the virtual keyboard mobile
    private TextView.OnEditorActionListener editorListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            switch (actionId)
            {
                case EditorInfo.IME_ACTION_SEARCH:
                    if (searchBox.getText().toString().equals(""))
                        Toast.makeText(context , "please type a book" , Toast.LENGTH_SHORT).show();
                    else {
                        databaseSuggestions.addBook(searchBox.getText().toString());
                        search(searchBox.getText().toString());
                        notifySuggestionsAdapter();
                        searchBox.dismissDropDown();
                    }
                break;
            }
            closeKeyboard();
            return false;
        }
    };


    private void closeKeyboard()
    {
        try {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
           //
        }
    }


    private void speechInput(View view)
    {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL , RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE , Locale.getDefault());
        startActivityForResult(intent , 10);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode)
        {
            case 10:
                if (resultCode == RESULT_OK && data != null)
                {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    search(result.get(0));
                    searchBox.setText(result.get(0));
                }
                break;
        }

    }

    private void search(String bookName)
    {
        searchLoading.setVisibility(View.VISIBLE);
        books.clear();
        parseData.convertData(bookName, books);
    }

    @Override
    public void notifyAdapter() {
        searchLoading.setVisibility(View.GONE);
        if (adapter != null)
            adapter.notifyDataSetChanged();
    }

    public void notifySuggestionsAdapter()
    {
        searchSuggestions = databaseSuggestions.getBooksSuggestions();
        searchAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, searchSuggestions);
        searchBox.setAdapter(searchAdapter);
        searchAdapter.notifyDataSetChanged();
    }
}
