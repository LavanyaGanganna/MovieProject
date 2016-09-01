package com.example.android.movieproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
public class MainActivityFragment extends Fragment {
	private static final String TAG = MainActivityFragment.class.getSimpleName();
	View view;
	RecyclerView recyclerview;
	MoviesAdapter mmoviesadapter;
	String choices;
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		 view=inflater.inflate(R.layout.fragment_main,container,false);
		recyclerview= (RecyclerView) view.findViewById(R.id.recyclerview);
		Display display = getActivity().getWindowManager().getDefaultDisplay();
		DisplayMetrics outMetrics = new DisplayMetrics();
		display.getMetrics(outMetrics);
		float density  = getResources().getDisplayMetrics().density;
		float dpWidth  = outMetrics.widthPixels / density;
		int columns = Math.round(dpWidth/200);
		RecyclerView.LayoutManager mlayoutmanager=new GridLayoutManager(getActivity(),columns);
		recyclerview.setLayoutManager(mlayoutmanager);

		recyclerview.setAdapter(mmoviesadapter);
		return view;

	}

	@Override
	public void onStart() {
		super.onStart();
		SharedPreferences sharedpreferences= PreferenceManager.getDefaultSharedPreferences(getContext());
		 String choice=sharedpreferences.getString(getString(R.string.pref_options_key),getString(R.string.pref_options_popular));
		if(mmoviesadapter==null || !(choice.equals(choices)) )
		updatemoviedata(choice);
	}


	public class FetchMoviedata extends AsyncTask<String,Void,Moviedata[]>{

		Moviedata[] movielist;
		@Override
		protected Moviedata[] doInBackground(String... strings) {
			String choice = strings[0];
			HttpURLConnection urlConnection = null;
			BufferedReader reader = null;

			// Will contain the raw JSON response as a string.
			String movieJsonStr = null;
			Uri myuri=null;
			try {
				// Construct the URL for the OpenWeatherMap query
				// Possible parameters are avaiable at OWM's forecast API page, at
				//we can also use uri.parse to build uri
				// http://openweathermap.org/API#forecast
				//API Key 55043ca149550f44459ac5108bf6f7ac
				if(choice.equals(getString(R.string.pref_options_popular))) {

					 myuri = Uri.parse("http://api.themoviedb.org/3/movie/popular?api_key=e38c1fbe414f3cbb8d0878562ff7ff5d").buildUpon().build();
				} else if(choice.equals(getString(R.string.pref_options_toprated))) {

					myuri = Uri.parse("http://api.themoviedb.org/3/movie/top_rated?api_key=e38c1fbe414f3cbb8d0878562ff7ff5d").buildUpon().build();
				}
				URL url = new URL(myuri.toString());
			//	Log.v(TAG, "the url built" + url);

				// Create the request to OpenWeatherMap, and open the connection
				urlConnection = (HttpURLConnection) url.openConnection();
				urlConnection.setRequestMethod("GET");
				urlConnection.setRequestProperty("connection", "close");
				urlConnection.connect();

				// Read the input stream into a String
				InputStream inputStream = urlConnection.getInputStream();
				StringBuffer buffer = new StringBuffer();
				if (inputStream == null) {
					// Nothing to do.
					return null;
				}
				reader = new BufferedReader(new InputStreamReader(inputStream));

				String line;
				while ((line = reader.readLine()) != null) {
					// Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
					// But it does make debugging a *lot* easier if you print out the completed
					// buffer for debugging.
					buffer.append(line + "\n");
				}

				if (buffer.length() == 0) {
					// Stream was empty.  No point in parsing.
					return null;
				}
				movieJsonStr = buffer.toString();
			} catch (IOException e) {
				Log.e(TAG, "Error ", e);
				// If the code didn't successfully get the weather data, there's no point in attemping
				// to parse it.
				return null;
			} finally {
				if (urlConnection != null) {
					urlConnection.disconnect();
				}
				if (reader != null) {
					try {
						reader.close();
					} catch (final IOException e) {
						Log.e(TAG, "Error closing stream", e);
					}
				}
			}
	//		Log.d(TAG, "The Json data" + movieJsonStr);
			try {
				movielist = parsingmoviedata(movieJsonStr);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return movielist;
		}

		@Override
		protected void onPostExecute(Moviedata[] moviedatas) {
			super.onPostExecute(moviedatas);
	//		Log.d(TAG,"INside postexecute");
			mmoviesadapter=new MoviesAdapter(moviedatas,getActivity());
			recyclerview.setAdapter(mmoviesadapter);
			mmoviesadapter.notifyDataSetChanged();
		}
	}
	private void updatemoviedata(String choice){

		//Log.d(TAG,"inside updatemoviedata");
		 choices=choice;
		FetchMoviedata fetchmoviedata=new FetchMoviedata();
		fetchmoviedata.execute(choices);
	}

	private Moviedata[] parsingmoviedata(String moviejsonstring) throws JSONException {
	//	Log.d(TAG,"inside parsing");
		Moviedata[] moviearry;
		JSONObject jsonobject =new JSONObject(moviejsonstring);
		JSONArray resultsarray=jsonobject.getJSONArray("results");
		moviearry=new Moviedata[resultsarray.length()];
		for(int i=0;i<resultsarray.length();i++){
			JSONObject movieobject=resultsarray.getJSONObject(i);
			String imagepath=movieobject.getString("poster_path");
			String overview=movieobject.getString("overview");
			String releasedate=movieobject.getString("release_date");
			String movietitle=movieobject.getString("title");
			Double averagevote=movieobject.getDouble("vote_average");
			Moviedata moviedata=new Moviedata(imagepath,overview,releasedate,movietitle,averagevote);
			moviearry[i]=moviedata;
		}
		return moviearry;
	}
}
