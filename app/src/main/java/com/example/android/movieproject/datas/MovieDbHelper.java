package com.example.android.movieproject.datas;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by lavanya on 9/13/16.
 */
public class MovieDbHelper extends SQLiteOpenHelper {
	private static final int DATABASE_VERSION = 1;
	static final String DATABASE_NAME = "moviedatabase.db";
	private static final String TAG = MovieDbHelper.class.getSimpleName();

	public MovieDbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase sqLiteDatabase) {
		Log.d(TAG, "inside of oncreate in moviedatabase");
		final String SQL_CREATE_MOVIEDETAIL_TABLE = "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + " (" +
				MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY, " +
				MovieContract.MovieEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
				MovieContract.MovieEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
				MovieContract.MovieEntry.COLUMN_RELDATE + " INTEGER NOT NULL, " +
				MovieContract.MovieEntry.COLUMN_MOVIEID + " INTEGER NOT NULL, " +
				MovieContract.MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
				MovieContract.MovieEntry.COLUMN_VOTES + " REAL NOT NULL, " +
				" UNIQUE (" + MovieContract.MovieEntry.COLUMN_MOVIEID + ") ON CONFLICT REPLACE);";


		final String SQL_CREATE_MOVIEVIDEO_TABLE = "CREATE TABLE " + MovieContract.VideoEntry.TABLE_NAME + " (" +
				MovieContract.VideoEntry._ID + " INTEGER PRIMARY KEY, " +
				MovieContract.VideoEntry.COLUMN_ID_KEY + " INTEGER NOT NULL, " +
				MovieContract.VideoEntry.COLUMN_MOV_ID + " INTEGER NOT NULL, " +
				MovieContract.VideoEntry.COLUMN_VIDEOS + " TEXT, " +
				" FOREIGN KEY (" + MovieContract.VideoEntry.COLUMN_ID_KEY + ") REFERENCES " +
				MovieContract.MovieEntry.TABLE_NAME + " (" + MovieContract.MovieEntry._ID + ")" +
				" UNIQUE (" + MovieContract.VideoEntry.COLUMN_VIDEOS + ") ON CONFLICT REPLACE );";

		final String SQL_CREATE_MOVIEREVIEW_TABLE = "CREATE TABLE " + MovieContract.ReviewEntry.TABLE_NAME + " (" +
				MovieContract.ReviewEntry._ID + " INTEGER PRIMARY KEY, " +
				MovieContract.ReviewEntry.COLUMN_MOVIEID_KEY + " INTEGER NOT NULL, " +
				MovieContract.ReviewEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
				MovieContract.ReviewEntry.COLUMN_AUTHOR + " TEXT, " +
				MovieContract.ReviewEntry.COLUMN_URL + " TEXT, " +
				" FOREIGN KEY (" + MovieContract.ReviewEntry.COLUMN_MOVIEID_KEY + ") REFERENCES " +
				MovieContract.MovieEntry.TABLE_NAME + " (" + MovieContract.MovieEntry._ID + ")" +
				"UNIQUE (" + MovieContract.ReviewEntry.COLUMN_URL + ") ON CONFLICT REPLACE );";
		sqLiteDatabase.execSQL(SQL_CREATE_MOVIEDETAIL_TABLE);
		sqLiteDatabase.execSQL(SQL_CREATE_MOVIEVIDEO_TABLE);
		sqLiteDatabase.execSQL(SQL_CREATE_MOVIEREVIEW_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
		sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);
		sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.VideoEntry.TABLE_NAME);
		sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.ReviewEntry.TABLE_NAME);
		onCreate(sqLiteDatabase);
	}
}
