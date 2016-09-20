package com.example.android.movieproject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.android.movieproject.datas.Moviedata;
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
public class OkhttpHandlerVideos extends AsyncTask<String, Void, ArrayList<Movievideodata>> {
	private static final String TAG = OkhttpHandlerVideos.class.getSimpleName();
	public static ArrayList<Movievideodata> movievideolist = new ArrayList<Movievideodata>();
	Context mcontext;
	OkHttpClient clients = new OkHttpClient();
	Response response;
	int movieid;
	public AsyncResponse delegate = null;

	/*AsyncResponse*/
	public OkhttpHandlerVideos(Context context, int movieids, AsyncResponse delegate) {
		mcontext = context;
		this.delegate = delegate;
		movieid = movieids;
	}

	public interface AsyncResponse {
		void processFinish(ArrayList<Movievideodata> output);
	}

	@Override
	protected ArrayList<Movievideodata> doInBackground(String... strings) {
		movievideolist.clear();
		Request.Builder builder = new Request.Builder();
		//	Log.d(TAG, "the arg is " + strings[0]);
		builder.url(strings[0]);
		Request request = builder.build();
		try {
			response = clients.newCall(request).execute();
			String moviejsonstr = response.body().string();
			//	Log.d(TAG, "The json data " + moviejsonstr);
			if (moviejsonstr.equals(null)) {
				Log.d(TAG, "the movie json string is null");
				return null;
			}

			JSONObject jsonobject = new JSONObject(moviejsonstr);
			if (!jsonobject.has("status_code")) {

				JSONArray resultarray = jsonobject.getJSONArray("results");
				for (int i = 0; i < resultarray.length(); i++) {
					JSONObject singleobject = resultarray.getJSONObject(i);
					String moviekey = singleobject.getString("key");
					//	Log.d(TAG,"the movieid"+ movieid +" "+ moviekey);
					Movievideodata movievideodata = new Movievideodata(movieid, moviekey);
					movievideolist.add(movievideodata);
				}
			}
			Movievideodata movievideo = new Movievideodata(movieid, null);
			movievideolist.add(movievideo);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		if (delegate != null) {
			delegate.processFinish(movievideolist);
		}
		return movievideolist;
	}

}
