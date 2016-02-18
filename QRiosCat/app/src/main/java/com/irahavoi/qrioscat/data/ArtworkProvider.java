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
    public static final String  PROVIDER_NAME = "com.irahavoi.qrioscat.data.ArtworkProvider";
    static final String URL_ARTWORK = "content://" + PROVIDER_NAME + "/artworks";
    static final String URL_ARTWORK_COMMENT = "content://" + PROVIDER_NAME + "/comments";

    public static final Uri CONTENT_URI_ARTWORK = Uri.parse(URL_ARTWORK);
    public static final Uri CONTENT_URI_COMMENT = Uri.parse(URL_ARTWORK_COMMENT);

    public static final int COL_ARTWORK_ID = 0;
    public static final int COL_ARTWORK_NAME = 1;
    public static final int COL_ARTWORK_AUTHOR = 2;
    public static final int COL_ARTWORK_DESCRIPTION = 3;
    public static final int COL_ARTWORK_IMAGE_URL = 4;

    public static final String _ID = "_id";
    public static final String NAME = "name";
    public static final String AUTHOR = "author";
    public static final String DESCRIPTION = "description";
    public static final String IMAGE_URL = "imageUrl";

    public static final String _ARTWORK_ID = "artworkId";

    private static Map<String, String> ARTWORKS_PROJECTION_MAP;

    static final int ARTWORKS = 1;
    static final int ARTWORK_ID = 2;
    static final int COMMENTS = 3;
    static final int COMMENT_ID = 4;

    static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "artworks", ARTWORKS);
        uriMatcher.addURI(PROVIDER_NAME, "artworks/#", ARTWORK_ID);
        uriMatcher.addURI(PROVIDER_NAME, "artworks/#/comments", COMMENTS);
        uriMatcher.addURI(PROVIDER_NAME, "artworks/#/comments/#", COMMENT_ID);
    }

    private SQLiteDatabase db;
    static final String DATABASE_NAME = "qrioscat";
    static final String TABLE_NAME_ARTWORKS = "artworks";
    static final String TABLE_NAME_COMMENTS = "comments";
    static final int DATABASE_VERSION = 1;

    static final String CREATE_DB_TABLE_ARTWORKS = "CREATE TABLE " + TABLE_NAME_ARTWORKS +
            " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            " name TEXT NOT NULL, " +
            " author TEXT NOT NULL, " +
            " description TEXT NOT NULL, " +
            " imageURL TEXT NULL);";

    static final String CREATE_DB_TABLE_COMMENTS = "CREATE TABLE " + TABLE_NAME_COMMENTS +
            " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            " artworkId INTEGER NOT NULL," +
            " comment TEXT NOT NULL," +
            " FOREIGN KEY(artworkId) REFERENCES artworks(_id));";

    /**
     * Database helper for creating and managing data repository.
     */
    private static class DatabaseHelper extends SQLiteOpenHelper{
        DatabaseHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db){
            db.execSQL(CREATE_DB_TABLE_ARTWORKS);
            db.execSQL(CREATE_DB_TABLE_COMMENTS);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_ARTWORKS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_COMMENTS);
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

        switch (uriMatcher.match(uri)){
            case ARTWORKS:
                queryBuilder.setTables(TABLE_NAME_ARTWORKS);
                queryBuilder.setProjectionMap(ARTWORKS_PROJECTION_MAP);
                break;
            case  ARTWORK_ID:
                queryBuilder.setTables(TABLE_NAME_ARTWORKS);
                queryBuilder.appendWhere(_ID + " = " + uri.getPathSegments().get(1));
                break;
            case COMMENTS:
                queryBuilder.setTables(TABLE_NAME_COMMENTS);
                queryBuilder.appendWhere(_ARTWORK_ID + " = " + uri.getPathSegments().get(1));
                break;
            case COMMENT_ID:
                queryBuilder.setTables(TABLE_NAME_COMMENTS);
                queryBuilder.appendWhere(_ARTWORK_ID + " = " + uri.getPathSegments().get(1) + " AND " + _ID + " = " + uri.getPathSegments().get(2));
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        if(sortOrder == null || sortOrder == ""){
            sortOrder = _ID;
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


        long rowId = -1;
        Uri _uri;
        switch (uriMatcher.match(uri)){
            case ARTWORKS:
                rowId = db.insert(TABLE_NAME_ARTWORKS, "", contentValues);
                _uri = ContentUris.withAppendedId(CONTENT_URI_ARTWORK, rowId);
                break;
            case COMMENTS:
                rowId = db.insert(TABLE_NAME_COMMENTS, "", contentValues);
                _uri = ContentUris.withAppendedId(CONTENT_URI_COMMENT, rowId);
                break;
            default:
                throw new IllegalArgumentException("Unknown uri: " + uri);

        }

        if(rowId > 0){
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
                count = db.delete(TABLE_NAME_ARTWORKS, selection, selectionArgs);
                break;
            case ARTWORK_ID:
                String id = uri.getPathSegments().get(1);
                count = db.delete(TABLE_NAME_ARTWORKS, _ID + " = " + id +
                        (!TextUtils.isEmpty(selection)? " AND (" + selection + ")" : ""), selectionArgs);
                break;
            case COMMENTS:
                count = db.delete(TABLE_NAME_COMMENTS, selection, selectionArgs);
                break;
            case COMMENT_ID:
                String artworkId = uri.getPathSegments().get(1);
                String commentId = uri.getPathSegments().get(2);
                count = db.delete(TABLE_NAME_COMMENTS, _ID  + " = " + commentId +
                    " AND " + _ARTWORK_ID + " = " + artworkId
                    + (!TextUtils.isEmpty(selection)? " AND (" + selection + ")" : ""), selectionArgs);
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
                count = db.update(TABLE_NAME_ARTWORKS, contentValues, selection, selectionArgs);
                break;
            case ARTWORK_ID:
                count = db.update(TABLE_NAME_ARTWORKS, contentValues, _ID + " = " + uri.getPathSegments().get(1) +
                        (!TextUtils.isEmpty(selection)? " AND (" + selection + ")" : ""), selectionArgs);
                break;
            case COMMENTS:
                count = db.update(TABLE_NAME_COMMENTS, contentValues, _ARTWORK_ID + " = " +  uri.getPathSegments().get(1) + selection, selectionArgs);
                break;
            case COMMENT_ID:
                count = db.update(TABLE_NAME_ARTWORKS, contentValues, _ID + " = " + uri.getPathSegments().get(1) +
                        _ARTWORK_ID + " = " +  uri.getPathSegments().get(2) +
                        (!TextUtils.isEmpty(selection)? " AND (" + selection + ")" : ""), selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
