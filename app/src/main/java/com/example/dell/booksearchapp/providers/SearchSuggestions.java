package com.example.dell.booksearchapp.providers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.ArrayAdapter;

import com.example.dell.booksearchapp.fragments.SearchFragment;

import java.util.ArrayList;

/**
 * Created by dell on 2/20/2019.
 */

public class SearchSuggestions extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "SearchTools";
    public static final String BOOK_NAME_COL = "bookName";
    public static final String TABLE_NAME = "Suggestions";

    private Context mContext;

    public SearchSuggestions(Context context) {
        super(context, DATABASE_NAME , null, 2);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
       db.execSQL("create table " + TABLE_NAME + "( "
               + " id INTEGER PRIMARY KEY AUTOINCREMENT , "
               + BOOK_NAME_COL + " TEXT , CONSTRAINT name_constrain UNIQUE (bookName) )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("Drop table if exists " + TABLE_NAME);
        onCreate(db);
    }

    public void addBook(String bookName)
    {
       SQLiteDatabase db = this.getWritableDatabase();
       ContentValues value = new ContentValues();
       value.put(BOOK_NAME_COL , bookName);
       db.insert(TABLE_NAME , null , value);

    }

    public String[] getBooksSuggestions()
    {
        ArrayList<String> suggestions = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME ,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            suggestions.add(cursor.getString(1));
            cursor.moveToNext();
        }
          return suggestions.toArray(new String[suggestions.size()]);
    }

}
