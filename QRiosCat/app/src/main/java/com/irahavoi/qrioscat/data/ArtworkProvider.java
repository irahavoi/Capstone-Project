package com.irahavoi.qrioscat.data;

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
import android.text.TextUtils;

import java.util.Map;

/**
 * Created by irahavoi on 2016-02-13.
 */
public class ArtworkProvider extends ContentProvider {
    static final String  PROVIDER_NAME = "com.irahavoi.qrioscat.data.ArtworkProvider";
    static final String URL = "content://" + PROVIDER_NAME + "/artworks";

    public static final Uri CONTENT_URI = Uri.parse(URL);

    public static final String _ID = "_id";
    public static final String NAME = "name";
    public static final String AUTHOR = "author";
    public static final String DESCRIPTION = "description";
    public static final String IMAGE_URL = "imageUrl";

    private static Map<String, String> ARTWORKS_PROJECTION_MAP;

    static final int ARTWORKS = 1;
    static final int ARTWORK_ID = 2;

    static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "artworks", ARTWORKS);
        uriMatcher.addURI(PROVIDER_NAME, "artworks/#", ARTWORK_ID);
    }

    private SQLiteDatabase db;
    static final String DATABASE_NAME = "qrioscat";
    static final String ARTWORK_TABLE_NAME = "artworks";
    static final int DATABASE_VERSION = 1;
    static final String CREATE_DB_TABLE = "CREATE TABLE " + ARTWORK_TABLE_NAME +
            " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            " name TEXT NOT NULL, " +
            " author TEXT NOT NULL, " +
            " imageURL TEXT NULL);";

    /**
     * Database helper for creating and managing data repository.
     */
    private static class DatabaseHelper extends SQLiteOpenHelper{
        DatabaseHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db){
            db.execSQL(CREATE_DB_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
            db.execSQL("DROP TABLE IF EXISTS " + ARTWORK_TABLE_NAME);
            onCreate(db);
        }
    }



    @Override
    public boolean onCreate() {
        Context context = getContext();
        DatabaseHelper helper = new DatabaseHelper(context);
        db = helper.getWritableDatabase();

        return db != null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(ARTWORK_TABLE_NAME);

        switch (uriMatcher.match(uri)){
            case ARTWORKS:
                queryBuilder.setProjectionMap(ARTWORKS_PROJECTION_MAP);
                break;
            case  ARTWORK_ID:
                queryBuilder.appendWhere(_ID + " = " + uri.getPathSegments().get(1));
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        if(sortOrder == null || sortOrder == ""){
            sortOrder = NAME;
        }

        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);

        /**
         * Watching a content URI for changes.
         */
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        //TODO
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        //Add a new artwork record
        long rowId = db.insert(ARTWORK_TABLE_NAME, "", contentValues);

        if(rowId > 0){
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowId);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }

        throw new SQLException("Failed to insert a record into " + uri);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)){
            case ARTWORKS:
                count = db.delete(ARTWORK_TABLE_NAME, selection, selectionArgs);
                break;
            case ARTWORK_ID:
                String id = uri.getPathSegments().get(1);
                count = db.delete(ARTWORK_TABLE_NAME, _ID + " = " + id +
                        (!TextUtils.isEmpty(selection)? " AND (" + selection + ")" : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return count;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        int count = 0;

        switch (uriMatcher.match(uri)){
            case ARTWORKS:
                count = db.update(ARTWORK_TABLE_NAME, contentValues, selection, selectionArgs);
                break;
            case ARTWORK_ID:
                count = db.update(ARTWORK_TABLE_NAME, contentValues, _ID + " = " + uri.getPathSegments().get(1) +
                        (!TextUtils.isEmpty(selection)? " AND (" + selection + ")" : ""), selectionArgs);
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
