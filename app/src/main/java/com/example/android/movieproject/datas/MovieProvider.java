package com.example.android.movieproject.datas;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by lavanya on 9/13/16.
 */
public class MovieProvider extends ContentProvider {
	private static final UriMatcher sUriMatcher = buildUriMatcher();
	private static final String TAG = MovieProvider.class.getSimpleName();
	private MovieDbHelper movieDbHelper;
	static final int MOVIES = 100;
	static final int MOVIES_WITH_MOVIEID = 101;
	static final int MOVIES_WITH_VIDEOS = 200;
	static final int MOVIES_WITH_REVIEWS = 201;

	private static final SQLiteQueryBuilder sMovieByQueryBuilder;

	static {
		sMovieByQueryBuilder = new SQLiteQueryBuilder();
		sMovieByQueryBuilder.setTables(MovieContract.MovieEntry.TABLE_NAME);
	}

	static UriMatcher buildUriMatcher() {
		final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
		final String authority = MovieContract.CONTENT_AUTHORITY;
		matcher.addURI(authority, MovieContract.PATH_MOVIEDATA, MOVIES);
		matcher.addURI(authority, MovieContract.PATH_MOVIEDATA + "/#", MOVIES_WITH_MOVIEID);
		matcher.addURI(authority, MovieContract.PATH_VIDEODATA, MOVIES_WITH_VIDEOS);
		matcher.addURI(authority, MovieContract.PATH_REVIEWDATA, MOVIES_WITH_REVIEWS);
		return matcher;
	}

	@Override
	public boolean onCreate() {
		movieDbHelper = new MovieDbHelper(getContext());
		return true;
	}

	@Nullable
	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionargs, String sortorder) {
		Cursor retcursor = null;

		switch (sUriMatcher.match(uri)) {
			case MOVIES:
				retcursor = movieDbHelper.getReadableDatabase().query(MovieContract.MovieEntry.TABLE_NAME, projection, selection, selectionargs, null, null, sortorder);
				break;
			case MOVIES_WITH_MOVIEID:
				Log.d(TAG, "inside movies with movieid");
				final String My_query = "SELECT * FROM " + MovieContract.MovieEntry.TABLE_NAME + " WHERE " + MovieContract.MovieEntry.TABLE_NAME + " . " + MovieContract.MovieEntry.COLUMN_MOVIEID + " =? ";
				int movieid = MovieContract.MovieEntry.getmovieidfromuri(uri);
				//selection=MovieContract.MovieEntry.TABLE_NAME + " . " + MovieContract.MovieEntry.COLUMN_MOVIEID + " =? ";
				selectionargs = new String[]{Integer.toString(movieid)};
				//	retcursor=sMovieByQueryBuilder.query(movieDbHelper.getReadableDatabase(),projection,selection,selectionargs,null,null,sortorder);
				retcursor = movieDbHelper.getReadableDatabase().rawQuery(My_query, selectionargs);
				if (retcursor.moveToFirst()) {
					String title = retcursor.getString(retcursor.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_TITLE));
					//	Log.d(TAG, "the title is" + title);
				}
				return retcursor;

			default:
				throw new UnsupportedOperationException("Unknown uri: " + uri);

		}
		retcursor.setNotificationUri(getContext().getContentResolver(), uri);
		return retcursor;

	}

	@Nullable
	@Override
	public String getType(Uri uri) {
		final int match = sUriMatcher.match(uri);
		switch (match) {
			case MOVIES:
				return MovieContract.MovieEntry.CONTENT_TYPE;
			case MOVIES_WITH_MOVIEID:
				return MovieContract.MovieEntry.CONTENT_ITEM_TYPE;
			case MOVIES_WITH_VIDEOS:
				return MovieContract.VideoEntry.CONTENT_TYPE;
			case MOVIES_WITH_REVIEWS:
				return MovieContract.ReviewEntry.CONTENT_TYPE;
			default:
				throw new UnsupportedOperationException("Unknown uri: " + uri);
		}

	}

	@Nullable
	@Override
	public Uri insert(Uri uri, ContentValues contentValues) {
		SQLiteDatabase db = movieDbHelper.getWritableDatabase();
		final int match = sUriMatcher.match(uri);
		Uri returi;
		switch (match) {
			case MOVIES:
				long _id = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, contentValues);
				if (_id > 0) {
					returi = MovieContract.MovieEntry.buildMovieUri(_id);
				} else {
					throw new android.database.SQLException("Failed to insert row into " + uri);
				}
				break;
			case MOVIES_WITH_VIDEOS:
				long _ids = db.insert(MovieContract.VideoEntry.TABLE_NAME, null, contentValues);
				if (_ids > 0) {
					returi = MovieContract.VideoEntry.buildMovieUri(_ids);
				} else {
					throw new android.database.SQLException("Failed to insert row into " + uri);
				}
				break;
			case MOVIES_WITH_REVIEWS:
				long _ides = db.insert(MovieContract.ReviewEntry.TABLE_NAME, null, contentValues);
				if (_ides > 0) {
					returi = MovieContract.ReviewEntry.buildMovieUri(_ides);
				} else {
					throw new android.database.SQLException("Failed to insert row into " + uri);
				}
				break;
			default:
				throw new UnsupportedOperationException("Unknown uri: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return returi;

	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionargs) {
		final SQLiteDatabase db = movieDbHelper.getWritableDatabase();
		final int match = sUriMatcher.match(uri);
		int rowsdeleted = 0;
		/* if selection equals null delete all the rows*/
		if (selection == null) {
			selection = "1";
		}
		switch (match) {
			case MOVIES:
				rowsdeleted = db.delete(MovieContract.MovieEntry.TABLE_NAME, selection, selectionargs);
				break;
			case MOVIES_WITH_VIDEOS:
				rowsdeleted = db.delete(MovieContract.VideoEntry.TABLE_NAME, selection, selectionargs);
				break;
			case MOVIES_WITH_REVIEWS:
				rowsdeleted = db.delete(MovieContract.ReviewEntry.TABLE_NAME, selection, selectionargs);
				break;
			default:
				throw new UnsupportedOperationException("Unknown uri: " + uri);
		}
		if (rowsdeleted != 0) {
			getContext().getContentResolver().notifyChange(uri, null);
		}
		return rowsdeleted;


	}

	@Override
	public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionargs) {
		final SQLiteDatabase db = movieDbHelper.getWritableDatabase();
		final int match = sUriMatcher.match(uri);
		int rowsupdated;
		switch (match) {
			case MOVIES:
				rowsupdated = db.update(MovieContract.MovieEntry.TABLE_NAME, contentValues, selection, selectionargs);
				break;

			default:
				throw new UnsupportedOperationException("Unknown uri: " + uri);
		}
		if (rowsupdated != 0) {
			getContext().getContentResolver().notifyChange(uri, null);
		}

		return rowsupdated;

	}

	public int bulkInsert(Uri uri, ContentValues[] contentValues) {
/* always use begin transaction with insert*/
		final SQLiteDatabase db = movieDbHelper.getWritableDatabase();
		final int match = sUriMatcher.match(uri);

		switch (match) {
			case MOVIES:
				int rowcount = 0;
				db.beginTransaction();
				try {
					for (ContentValues values : contentValues) {
						//	normalizedate(values);
						long id = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, values);
						if (id != -1) {
							rowcount++;
							//                 Log.d(TAG, "THE ROWCOUNT IS" + rowcount + " " + id);
						}
					}
					db.setTransactionSuccessful();

				} finally {
					db.endTransaction();
					//db.close();
				}
				getContext().getContentResolver().notifyChange(uri, null);
				return rowcount;
			default:
				return super.bulkInsert(uri, contentValues);
		}

	}

}
