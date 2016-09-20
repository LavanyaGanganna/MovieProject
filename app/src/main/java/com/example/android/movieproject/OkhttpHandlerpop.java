package com.example.android.movieproject;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.movieproject.datas.Moviedata;
import com.example.android.movieproject.datas.Moviereviewsdata;
import com.example.android.movieproject.datas.Movievideodata;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by lavanya on 9/16/16.
 */
public class OkhttpHandlerpop extends AsyncTask<String, Void, ArrayList<Moviedata>> {
	private static final String TAG = OkhttpHandlerpop.class.getSimpleName();
	/*To get getsupportfrgmentmaager()
	private FragmentActivity fragmentActivity; */
	OkHttpClient client = new OkHttpClient();
	Response response;
	Context mcontext;
	MoviesAdapter popmoviesadapter;
	public static ArrayList<Moviedata> moviepop = new ArrayList<Moviedata>();
	public AsyncResponse delegate = null;

	/*AsyncResponse*/
	public OkhttpHandlerpop(Context context, AsyncResponse delegate) {
		mcontext = context;
		this.delegate = delegate;
	}

	public interface AsyncResponse {
		void processFinish(ArrayList<Moviedata> output);
	}


	@Override
	protected ArrayList<Moviedata> doInBackground(String... strings) {

		Request.Builder builder = new Request.Builder();
		builder.url(strings[0]);
		//	Log.d(TAG, "the arg is " + strings[0]);
		Request request = builder.build();
		moviepop.clear();

		try {
			response = client.newCall(request).execute();
			String moviejsonstr = response.body().string();
			//	Log.d(TAG, "The json data " + moviejsonstr);
			try {
				//	Long start = System.currentTimeMillis();
				if (moviejsonstr.equals(null)) {
					return null;
				}
				parsingmoviedata(moviejsonstr);

			} catch (JSONException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (delegate != null) {
			delegate.processFinish(moviepop);
		}
		return moviepop;
	}

	private void parsingmoviedata(String moviejsonstring) throws JSONException {
		//	Log.d(TAG,"inside parsing");

		JSONObject jsonobject = new JSONObject(moviejsonstring);
		final JSONArray resultsarray = jsonobject.getJSONArray("results");
		for (int i = 0; i < resultsarray.length(); i++) {
			final JSONObject movieobject = resultsarray.getJSONObject(i);

			try {
				//		Log.i("Thread", "Running parallely");
				String imagepath = null;
				imagepath = movieobject.getString("poster_path");
				String overview = movieobject.getString("overview");
				String releasedate = movieobject.getString("release_date");
				int movieid = movieobject.getInt("id");
				String movietitle = movieobject.getString("title");
				Double averagevote = movieobject.getDouble("vote_average");
				//		Log.d(TAG, "The   movie id" + movieid);
				Moviedata moviedata = new Moviedata(imagepath, overview, releasedate, movietitle, averagevote, movieid);
				moviepop.add(moviedata);

			} catch (JSONException e) {
				e.printStackTrace();
			}

		}

	}

	@Override
	protected void onPostExecute(ArrayList<Moviedata> moviedatas) {
		//	Log.d(TAG,"inside post execute");
		super.onPostExecute(moviedatas);
		//	delegate.processFinish(moviedatas);
		ConnectivityManager connectivityManager = (ConnectivityManager) mcontext.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		boolean status = (activeNetworkInfo != null && activeNetworkInfo.isConnected());
		//		Log.d(TAG,"INside postexecute");
		if (moviedatas == null) {
			Toast.makeText(mcontext, "no internet connection", Toast.LENGTH_LONG).show();
		}
		if (status == false) {
			Toast.makeText(mcontext, "no internet connection", Toast.LENGTH_LONG).show();
		}
		popmoviesadapter = new MoviesAdapter(moviedatas, mcontext);

		if (popmoviesadapter != null) {
			//	Log.d(TAG,"inside post execute mmoviesadapter");
			Activity activity = (mcontext instanceof Activity) ? (Activity) mcontext : null;
			ProgressBar progressBar = (ProgressBar) activity.findViewById(R.id.progressBar);
			if (progressBar != null && progressBar.getVisibility() == View.VISIBLE) {
				progressBar.setVisibility(View.GONE);
			}
			RecyclerView recyclerView = (RecyclerView) activity.findViewById(R.id.recyclerview);
			if (recyclerView != null && popmoviesadapter != null) {
				recyclerView.setAdapter(popmoviesadapter);
				recyclerView.getRecycledViewPool().clear();//impt for recyclerview error offset position1
			}
			popmoviesadapter.notifyDataSetChanged();
		}

	}

}

