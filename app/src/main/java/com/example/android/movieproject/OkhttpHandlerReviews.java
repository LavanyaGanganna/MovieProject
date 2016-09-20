package com.example.android.movieproject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

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
 * Created by lavanya on 9/19/16.
 */
public class OkhttpHandlerReviews extends AsyncTask<String, Void, ArrayList<Moviereviewsdata>> {
	private static final String TAG = OkhttpHandlerReviews.class.getSimpleName();
	public static ArrayList<Moviereviewsdata> moviereviewlist = new ArrayList<Moviereviewsdata>();
	Context mcontext;
	OkHttpClient clients = new OkHttpClient();
	Response response;
	int movieid;
	public AsyncResponse delegate = null;

	public OkhttpHandlerReviews(Context context, int movieids, AsyncResponse delegate) {
		mcontext = context;
		this.delegate = delegate;
		movieid = movieids;
	}

	public interface AsyncResponse {
		void processFinish(ArrayList<Moviereviewsdata> output);
	}

	@Override
	protected ArrayList<Moviereviewsdata> doInBackground(String... strings) {
		moviereviewlist.clear();
		String moviejsonstrs = null;
		Request.Builder builders = new Request.Builder();
		builders.url(strings[0]);
		Request request = builders.build();
		try {
			Response responses = clients.newCall(request).execute();
			moviejsonstrs = responses.body().string();
			//	Log.d(TAG,"the json videos data" + moviejsonstrs);
			JSONObject jsonobject = new JSONObject(moviejsonstrs);

			if (!jsonobject.has("status_code")) {
				JSONArray jsonarray = jsonobject.getJSONArray("results");
				for (int i = 0; i < jsonarray.length(); i++) {
					JSONObject singles = jsonarray.getJSONObject(i);
					String author = singles.getString("author");
					String movieurl = singles.getString("url");

					Moviereviewsdata moviereviewsdata = new Moviereviewsdata(movieid, author, movieurl);
					moviereviewlist.add(moviereviewsdata);
				}
			} else {
				Moviereviewsdata moviereviews = new Moviereviewsdata(movieid, null, null);
				moviereviewlist.add(moviereviews);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if (delegate != null) {
			delegate.processFinish(moviereviewlist);
		}

		return moviereviewlist;
	}


}
