package com.example.android.movieproject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.movieproject.datas.MovieContract;
import com.example.android.movieproject.datas.Moviedata;
import com.example.android.movieproject.datas.Moviereviewsdata;
import com.example.android.movieproject.datas.Movievideodata;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by lavanya on 8/29/16.
 */
public class MainActivityFragment extends Fragment implements OkhttpHandlerpop.AsyncResponse {
	private static final String TAG = MainActivityFragment.class.getSimpleName();
	View view;
	RecyclerView recyclerview;
	ProgressBar progressBar;
	ArrayList<Moviedata> moviedataArrayList = new ArrayList<Moviedata>();
	MoviesAdapter moviesAdapter;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_main, container, false);
		recyclerview = (RecyclerView) view.findViewById(R.id.recyclerview);
		int columns = 0;
		Display display = getActivity().getWindowManager().getDefaultDisplay();
		DisplayMetrics outMetrics = new DisplayMetrics();
		display.getMetrics(outMetrics);
		float density = getResources().getDisplayMetrics().density;
		float dpWidth = outMetrics.widthPixels / density;
		if (getResources().getBoolean(R.bool.isTablet)) {
			columns = Math.round(dpWidth / 350);
		} else {
			columns = Math.round(dpWidth / 200);
		}
		RecyclerView.LayoutManager mlayoutmanager = new GridLayoutManager(getActivity(), columns);
		recyclerview.setLayoutManager(mlayoutmanager);
		recyclerview.addItemDecoration(new SpacesItemDecoration(5));
		progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
		//	recyclerview.setAdapter(moviesAdapter);

		return view;

	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		if (moviesAdapter == null) {
			//	Log.d(TAG,"progeess bar VISIBLE");
			progressBar.setVisibility(View.VISIBLE);
		}
		updatemoviedata();
	}

	@Override
	public void onStart() {
		super.onStart();
		if (getResources().getBoolean(R.bool.isTablet))
			updatemoviedata();
	}


	private void updatemoviedata() {
		Context mcontext = getContext();
		//	String myuri = null;
		if (!(getResources().getBoolean(R.bool.isTablet))) {
			OkhttpHandlerpop okhttphandlerpop = new OkhttpHandlerpop(getActivity(), new OkhttpHandlerpop.AsyncResponse() {
				@Override
				public void processFinish(ArrayList<Moviedata> output) {
				}
			});
			//	Log.d(TAG, "inside update");
			Uri.Builder builder = new Uri.Builder();
			Uri myuri = builder.scheme("http")
					.authority("api.themoviedb.org")
					.appendPath("3")
					.appendPath("movie")
					.appendPath("popular")
					.appendQueryParameter("api_key", "50bb9b78ca3a650f255ae2006b702c62").build();
			okhttphandlerpop.execute(myuri.toString());

		} else if (getResources().getBoolean(R.bool.isTablet)) {
			SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
			String selections = sharedPreferences.getString(getString(R.string.pref_options_key), getString(R.string.pref_options_popular));
			if (selections.equals(getString(R.string.pref_options_popular))) {
				Activity activity = (mcontext instanceof Activity) ? (Activity) mcontext : null;
				Toolbar toolbar = (Toolbar) activity.findViewById(R.id.toolbar);
				((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
				toolbar.setTitle("Popular Movies");
				Uri.Builder builder = new Uri.Builder();
				Uri myuri = builder.scheme("http")
						.authority("api.themoviedb.org")
						.appendPath("3")
						.appendPath("movie")
						.appendPath("popular")
						.appendQueryParameter("api_key", "50bb9b78ca3a650f255ae2006b702c62").build();

				OkhttpHandlerpop okhttpHandlerpop = new OkhttpHandlerpop(getContext(), new OkhttpHandlerpop.AsyncResponse() {
					@Override
					public void processFinish(ArrayList<Moviedata> output) {
						if (output.size() > 0) {
							DetailFragment detailFragment = new DetailFragment();
							Bundle argus = new Bundle();
							argus.putParcelable(getString(R.string.movie_key), output.get(0));
							detailFragment.setArguments(argus);
							getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.tabletdetailholder, detailFragment).addToBackStack(null).commit();
						}
					}
				});
				okhttpHandlerpop.execute(myuri.toString());
			} else if (selections.equals(getString(R.string.pref_options_toprated))) {
				Activity activity = (mcontext instanceof Activity) ? (Activity) mcontext : null;
				Toolbar toolbar = (Toolbar) activity.findViewById(R.id.toolbar);
				((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
				toolbar.setTitle("Toprate Movies");
				Uri.Builder builder = new Uri.Builder();
				Uri myuri = builder.scheme("http")
						.authority("api.themoviedb.org")
						.appendPath("3")
						.appendPath("movie")
						.appendPath("top_rated")
						.appendQueryParameter("api_key", "50bb9b78ca3a650f255ae2006b702c62").build();

				OkhttpHandlerpop okhttpHandlerpop = new OkhttpHandlerpop(getContext(), new OkhttpHandlerpop.AsyncResponse() {
					@Override
					public void processFinish(ArrayList<Moviedata> output) {
						if (output.size() > 0) {
							DetailFragment detailFragment = new DetailFragment();
							Bundle argus = new Bundle();
							argus.putParcelable(getString(R.string.movie_key), output.get(0));
							detailFragment.setArguments(argus);
							getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.tabletdetailholder, detailFragment).addToBackStack(null).commit();
						}
					}
				});
				okhttpHandlerpop.execute(myuri.toString());
			} else if (selections.equals(getString(R.string.pref_options_favorite))) {
				moviedataArrayList.clear();
				Activity activity = (mcontext instanceof Activity) ? (Activity) mcontext : null;
				if (activity != null) {
					Toolbar toolbar = (Toolbar) activity.findViewById(R.id.toolbar);
					if (toolbar != null) {
						((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
						toolbar.setTitle("Favorite Movies");
					}
				}
				Cursor cursor = getActivity().getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI, null, null, null, null);
				if (cursor != null && cursor.moveToFirst()) {
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
					cursor.close();
				}
				if (moviedataArrayList.size() == 0) {
					Toast.makeText(getContext(), "No movies in Favorites", Toast.LENGTH_LONG).show();
					DetailFragment detailFragment = new DetailFragment();
					getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.tabletdetailholder, detailFragment).commit();
				} else {
					MoviesAdapter moviesAdapter = new MoviesAdapter(moviedataArrayList, getContext());
					recyclerview.setAdapter(moviesAdapter);
					moviesAdapter.notifyDataSetChanged();
					DetailFragment detailFragment = new DetailFragment();
					Bundle argus = new Bundle();
					argus.putParcelable(getString(R.string.movie_key), moviedataArrayList.get(0));
					detailFragment.setArguments(argus);
					getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.tabletdetailholder, detailFragment).addToBackStack(null).commit();

				}
			}
		}
	}


	@Override
	public void processFinish(ArrayList<Moviedata> output) {

	}
}

