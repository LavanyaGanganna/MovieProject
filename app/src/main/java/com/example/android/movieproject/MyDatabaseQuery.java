package com.example.android.movieproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.android.movieproject.datas.MovieContract;
import com.example.android.movieproject.datas.Moviedata;

import java.util.ArrayList;

/**
 * Created by lavanya on 9/22/16.
 */
public class MyDatabaseQuery extends AsyncTask<String, Void, Cursor> {
	private static final String TAG = MyDatabaseQuery.class.getSimpleName();
	Context mcontext;

	public MyDatabaseQuery(Context context) {
		mcontext = context;

	}

	@Override
	protected Cursor doInBackground(String... strings) {
		Log.d(TAG, "the argument is" + strings[0]);
		Cursor cursor = mcontext.getContentResolver().query(Uri.parse(strings[0]), null, null, null, null);
		Log.d(TAG, "The cursor is" + cursor);

		return cursor;
	}

	@Override
	protected void onPostExecute(Cursor cursor) {
		super.onPostExecute(cursor);
		if (cursor != null) {
			Toast.makeText(mcontext, "Exists in Favorite", Toast.LENGTH_SHORT).show();

		} else {
			ContentValues contentValues = new ContentValues();
			contentValues.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, DetailFragment.moviedata.getMmoviepath());
			contentValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, DetailFragment.moviedata.getMoverview());
			contentValues.put(MovieContract.MovieEntry.COLUMN_RELDATE, DetailFragment.yearsy);
			contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIEID, DetailFragment.moviedata.getMmovieid());
			contentValues.put(MovieContract.MovieEntry.COLUMN_TITLE, DetailFragment.moviedata.getMtitle());
			contentValues.put(MovieContract.MovieEntry.COLUMN_VOTES, DetailFragment.moviedata.getMvotes());
			Uri returi = mcontext.getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, contentValues);
			Toast.makeText(mcontext, "Added to Favorites", Toast.LENGTH_SHORT).show();
		}
	}
}

