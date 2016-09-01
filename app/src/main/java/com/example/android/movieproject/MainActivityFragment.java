package com.example.android.movieproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
	String choices;
	//MoviesAdapter moviesAdapter;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_main, container, false);
		recyclerview = (RecyclerView) view.findViewById(R.id.recyclerview);
		Display display = getActivity().getWindowManager().getDefaultDisplay();
		DisplayMetrics outMetrics = new DisplayMetrics();
		display.getMetrics(outMetrics);
		float density = getResources().getDisplayMetrics().density;
		float dpWidth = outMetrics.widthPixels / density;
		int columns = Math.round(dpWidth / 200);
		RecyclerView.LayoutManager mlayoutmanager = new GridLayoutManager(getActivity(), columns);
		recyclerview.setLayoutManager(mlayoutmanager);
		//	recyclerview.setAdapter(moviesAdapter);
		return view;

	}

	@Override
	public void onStart() {
		super.onStart();
		SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
		String choice = sharedpreferences.getString(getString(R.string.pref_options_key), getString(R.string.pref_options_popular));
		if (!(choice.equals(choices)))
			updatemoviedata(choice);
	}

	private void updatemoviedata(String choice) {

		//Log.d(TAG,"inside updatemoviedata");
		String myuri = null;
		choices = choice;
		OkhttpHandler okhttphandler = new OkhttpHandler(getActivity());
		if (choice.equals(getString(R.string.pref_options_popular))) {
			//API Key 55043ca149550f44459ac5108bf6f7ac
			myuri = Uri.parse("http://api.themoviedb.org/3/movie/popular?api_key=e38c1fbe414f3cbb8d0878562ff7ff5d").toString();
		} else if (choice.equals(getString(R.string.pref_options_toprated))) {

			myuri = Uri.parse("http://api.themoviedb.org/3/movie/top_rated?api_key=e38c1fbe414f3cbb8d0878562ff7ff5d").toString();
		}
		okhttphandler.execute(myuri);
	}
}

