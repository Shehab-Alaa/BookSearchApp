package com.example.dell.booksearchapp;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by dell on 1/28/2019.
 */

public class BooksProvider extends ContentProvider {

    static final String PROVIDER_NAME = "com.example.provider.College";
    static final String URL = "content://" + PROVIDER_NAME + "/books";
    static final Uri CONTENT_URI = Uri.parse(URL);

    static final int BOOKS = 1;
    static final int BOOKs_ID = 2;

    // compare uris
    static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME , "books" , BOOKS); // all books
        uriMatcher.addURI(PROVIDER_NAME , "books/#" , BOOKs_ID); // specific book
    }


    static final String DATABASE_NAME = "Books";
    static final String TABLE_NAME = "FavoriteBooks";
    static final int DATABASE_VERSION = 1;
    static final String CREATE_DB_TABLE =
            "create table if not exists " + TABLE_NAME +
                    " ( id INTEGER PRIMARY KEY AUTOINCREMENT , " +
                    "book_id TEXT , " +
                    "title TEXT , " +
                    "author TEXT , " +
                    "description TEXT , " +
                    "image TEXT , " +
                    "favorite INTEGER , " +
                    "categories TEXT , " +
                    "rate REAL );";


    private static class BooksDataBaseHelper extends SQLiteOpenHelper {

        BooksDataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DB_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
        }

    }


    private SQLiteDatabase db;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        BooksDataBaseHelper bookDbHelper = new BooksDataBaseHelper(context);
        db = bookDbHelper.getWritableDatabase();
        if (db == null)
            return false;
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(TABLE_NAME);
        Cursor cursor = queryBuilder.query(db , projection , selection , selectionArgs , null ,
        null , sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver() , uri);
        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
      long rowsAffected = db.insert(TABLE_NAME , "" , values);
      if (rowsAffected > 0 )
      {
          Uri _uri = ContentUris.withAppendedId(CONTENT_URI , rowsAffected);
          getContext().getContentResolver().notifyChange(_uri , null);
          return _uri;
      }
      throw new SQLException("Failed to add this record to " + uri);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int rowAffected = db.delete(TABLE_NAME , "book_id = ?" , selectionArgs);
        getContext().getContentResolver().notifyChange(uri , null);
        return rowAffected;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri))
        {
            //  get all books records
            case BOOKS:
                return "vnd.android.cursor.dir/vnd.com.example.books";
            // get a specific book
            case BOOKs_ID:
                return "vnd.android.cursor.item/vnd.com.example.books";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

}
