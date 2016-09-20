package com.example.android.movieproject;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.android.movieproject.datas.MovieContract;
import com.example.android.movieproject.datas.Moviedata;

import java.util.ArrayList;

/**
 * Created by lavanya on 9/12/16.
 */
public class MainActivityFragmenthree extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
	private static final String TAG = MainActivityFragmenthree.class.getSimpleName();
	RecyclerView recyclerView;
	ArrayList<Moviedata> moviedataArrayList = new ArrayList<Moviedata>();
	private static final int MOVIES_LOADER = 0;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		//	Log.d(TAG,"inside fragmentthree");
		View view = inflater.inflate(R.layout.frag_mainthree, container, false);
		recyclerView = (RecyclerView) view.findViewById(R.id.recyclerviewthree);
		Display display = getActivity().getWindowManager().getDefaultDisplay();
		DisplayMetrics outMetrics = new DisplayMetrics();
		display.getMetrics(outMetrics);
		float density = getResources().getDisplayMetrics().density;
		float dpWidth = outMetrics.widthPixels / density;
		int columns = Math.round(dpWidth / 200);
		RecyclerView.LayoutManager mlayoutmanager = new GridLayoutManager(getActivity(), columns);
		recyclerView.setLayoutManager(mlayoutmanager);
		moviedataArrayList.clear();
		getLoaderManager().initLoader(MOVIES_LOADER, null, this);
		return view;
	}


	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		CursorLoader cursorLoader = new CursorLoader(getActivity(), MovieContract.MovieEntry.CONTENT_URI, null, null, null, null);
		return cursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		if (cursor.moveToFirst()) {
			do {
				String posterpath = cursor.getString(cursor.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_POSTER_PATH));
				String overview = cursor.getString(cursor.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_OVERVIEW));
				String title = cursor.getString(cursor.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_TITLE));
				int reldate = cursor.getInt(cursor.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_RELDATE));
				int movid = cursor.getInt(cursor.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_MOVIEID));
				double votes = cursor.getDouble(cursor.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_VOTES));
				Moviedata moviedata = new Moviedata(posterpath, overview, Integer.toString(reldate), title, votes, movid);
				//	Log.d(TAG,"the movie titles" + title);
				moviedataArrayList.add(moviedata);
				//	Log.d(TAG,"Adding to Arraylists in three");
			} while (cursor.moveToNext());
		}

		if (moviedataArrayList.size() == 0) {
			Toast.makeText(getContext(), "No movies in Favorites", Toast.LENGTH_LONG).show();
		}
		MoviesAdapter moviesAdapter = new MoviesAdapter(moviedataArrayList, getContext());
		recyclerView.setAdapter(moviesAdapter);
		moviesAdapter.notifyDataSetChanged();
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		moviedataArrayList.clear();
	}
}
