package com.example.android.movieproject;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by lavanya on 9/1/16.
 */
public class OkhttpHandler extends AsyncTask<String, Void, Moviedata[]> {
	OkHttpClient client = new OkHttpClient();
	Response response;
	Moviedata[] movielist;
	MoviesAdapter mmoviesadapter;
	Context mcontext;

	public OkhttpHandler(Context context) {
		mcontext = context;
	}

	@Override
	protected Moviedata[] doInBackground(String... strings) {
		Request.Builder builder = new Request.Builder();
		builder.url(strings[0]);
		Request request = builder.build();
		try {
			response = client.newCall(request).execute();
			String moviejsonstr = response.body().string();
			try {
				movielist = parsingmoviedata(moviejsonstr);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return movielist;
	}

	private Moviedata[] parsingmoviedata(String moviejsonstring) throws JSONException {
		//	Log.d(TAG,"inside parsing");
		Moviedata[] moviearry;
		if (moviejsonstring.equals(null)) {
			return null;
		}
		JSONObject jsonobject = new JSONObject(moviejsonstring);
		JSONArray resultsarray = jsonobject.getJSONArray("results");
		moviearry = new Moviedata[resultsarray.length()];
		for (int i = 0; i < resultsarray.length(); i++) {
			JSONObject movieobject = resultsarray.getJSONObject(i);
			String imagepath = movieobject.getString("poster_path");
			String overview = movieobject.getString("overview");
			String releasedate = movieobject.getString("release_date");
			String movietitle = movieobject.getString("title");
			Double averagevote = movieobject.getDouble("vote_average");
			Moviedata moviedata = new Moviedata(imagepath, overview, releasedate, movietitle, averagevote);
			moviearry[i] = moviedata;
		}
		return moviearry;
	}

	@Override
	protected void onPostExecute(Moviedata[] moviedatas) {

		super.onPostExecute(moviedatas);
		//		Log.d(TAG,"INside postexecute");
		if (moviedatas == null) {
			Toast.makeText(mcontext, "no internet connection", Toast.LENGTH_SHORT).show();
		}
		mmoviesadapter = new MoviesAdapter(moviedatas, mcontext);
		Activity activity = (mcontext instanceof Activity) ? (Activity) mcontext : null;
		RecyclerView recyclerView = (RecyclerView) activity.findViewById(R.id.recyclerview);
		recyclerView.setAdapter(mmoviesadapter);
		mmoviesadapter.notifyDataSetChanged();
	}
}
