package com.example.android.movieproject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
 * Created by lavanya on 9/1/16.
 */
public class OkhttpHandler extends AsyncTask<String, Void, ArrayList<Moviedata>> {
	private static final String TAG = OkhttpHandler.class.getSimpleName();
	OkHttpClient clients = new OkHttpClient();
	Response response;
	MoviesAdapter mmoviesadapter;
	Context mcontext;

	final ArrayList<Moviedata> movietop = new ArrayList<>();
	public AsyncResponse delegate = null;

	/*AsyncResponse*/

	public interface AsyncResponse {
		void processFinish(ArrayList<Moviedata> output);
	}

	public OkhttpHandler(Context context, AsyncResponse delegate) {
		mcontext = context;
		this.delegate = delegate;
	}

	@Override
	protected ArrayList<Moviedata> doInBackground(String... strings) {

		Request.Builder builder = new Request.Builder();
		//	Log.d(TAG, "the arg is " + strings[0]);
		builder.url(strings[0]);
		Request request = builder.build();
		movietop.clear();
		try {
			response = clients.newCall(request).execute();
			String moviejsonstr = response.body().string();
			//	Log.d(TAG, "The json data " + moviejsonstr);
			try {
				//	Long start = System.currentTimeMillis();
				if (moviejsonstr.equals(null)) {
					Log.d(TAG, "the movie json string is null");
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
			delegate.processFinish(movietop);
		}

		return movietop;

	}

	private void parsingmoviedata(String moviejsonstring) throws JSONException {
		//	Log.d(TAG,"inside parsing");

		JSONObject jsonobjects = new JSONObject(moviejsonstring);
		final JSONArray resultsarrays = jsonobjects.getJSONArray("results");
		for (int i = 0; i < resultsarrays.length(); i++) {
			final JSONObject movieobjects = resultsarrays.getJSONObject(i);

			try {
				//		Log.i("Thread", "Running parallely");
				String imagepath = null;
				imagepath = movieobjects.getString("poster_path");
				String overview = movieobjects.getString("overview");
				String releasedate = movieobjects.getString("release_date");
				int movieid = movieobjects.getInt("id");
				String movietitle = movieobjects.getString("title");
				Double averagevote = movieobjects.getDouble("vote_average");
				//		Log.d(TAG, "The  thread movie id" + movieid);
				Moviedata moviedata = new Moviedata(imagepath, overview, releasedate, movietitle, averagevote, movieid);
				movietop.add(moviedata);

			} catch (JSONException e) {
				e.printStackTrace();
			}

		}

	}

	@Override
	protected void onPostExecute(ArrayList<Moviedata> moviedatas) {

		super.onPostExecute(moviedatas);
		//	Log.d(TAG,"the videolist" + movievideolist.size() +" "+ moviereviewlist.size());
		ConnectivityManager connectivityManager
				= (ConnectivityManager) mcontext.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		boolean status = (activeNetworkInfo != null && activeNetworkInfo.isConnected());
		//		Log.d(TAG,"INside postexecute");
		if (moviedatas == null) {
			Toast.makeText(mcontext, "no internet connection", Toast.LENGTH_LONG).show();
		}
		if (!status) {
			Toast.makeText(mcontext, "no internet connection", Toast.LENGTH_LONG).show();
		}
		if (moviedatas != null)
			mmoviesadapter = new MoviesAdapter(moviedatas, mcontext);

		if (mmoviesadapter != null) {
			Activity activity = (mcontext instanceof Activity) ? (Activity) mcontext : null;
			if (activity != null) {
				ProgressBar progressBartwo = (ProgressBar) activity.findViewById(R.id.progressBartwo);
				if (progressBartwo != null && progressBartwo.getVisibility() == View.VISIBLE) {
					progressBartwo.setVisibility(View.GONE);
				}
				Log.d(TAG, "progeess bar invisible  and inside setadapter");
				RecyclerView recyclerView = (RecyclerView) activity.findViewById(R.id.recyclerviewtwo);
				if (recyclerView != null && mmoviesadapter != null) {
					recyclerView.setAdapter(mmoviesadapter);
					mmoviesadapter.notifyDataSetChanged();
				}
			}
		}

	}

}

