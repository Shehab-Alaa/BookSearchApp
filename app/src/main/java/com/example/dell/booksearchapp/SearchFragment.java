package com.example.dell.booksearchapp;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by dell on 1/26/2019.
 */


public class SearchFragment extends android.support.v4.app.Fragment {

    private EditText searchBox;
    private ImageButton search_btn;
    private RecyclerView booksList;
    private ArrayList<Book> books;
    private BookSearchAdapter adapter;
    private Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment , container , false);

        search_btn = view.findViewById(R.id.search_btn);
        searchBox = view.findViewById(R.id.searchBox);
        booksList = view.findViewById(R.id.booksList);

        books = new ArrayList<>();

        LinearLayoutManager manager = new LinearLayoutManager(context);
        booksList.setLayoutManager(manager);
        booksList.setHasFixedSize(true);

        adapter = new BookSearchAdapter(context ,books , R.layout.book_list_item);
        booksList.setAdapter(adapter);


        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               if (searchBox.getText().toString().equals(""))
               {
                   Toast.makeText(context , "please type a book name" , Toast.LENGTH_SHORT).show();
               }
               else
               {
                   JsonParsing jsonParsing = new JsonParsing(context);
                   //books = new ArrayList<>();
                   books = jsonParsing.convertData(searchBox.getText().toString());
                   adapter.notifyDataSetChanged();


                   try {
                       InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                       imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                   } catch (Exception e) {
                       // TODO: handle exception
                   }

               }
            }
        });

        /*
        searchBox.setOnEditorActionListener(editorListener);
*/
        return view;
    }

    /*
    private TextView.OnEditorActionListener editorListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            switch (actionId)
            {
                case EditorInfo.IME_ACTION_SEARCH:
                    Toast.makeText(context , "search" , Toast.LENGTH_SHORT).show();
                break;
            }
            return false;
        }
    };
*/


}
