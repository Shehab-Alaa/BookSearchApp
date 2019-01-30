package com.example.dell.booksearchapp;

import com.android.volley.toolbox.JsonObjectRequest;

import java.util.ArrayList;

/**
 * Created by dell on 1/26/2019.
 */

public abstract class DataConverter {
    public abstract ArrayList<Book> convertData(String str);
}
