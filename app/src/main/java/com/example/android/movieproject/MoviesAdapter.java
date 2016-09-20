package com.example.android.movieproject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.movieproject.datas.Moviedata;
import com.example.android.movieproject.datas.Moviereviewsdata;
import com.example.android.movieproject.datas.Movievideodata;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by lavanya on 8/29/16.
 */
public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MyviewHolder> {
	public static ArrayList<String> videouris = new ArrayList<String>();
	public static ArrayList<String> urllist = new ArrayList<String>();
	private static final String TAG = MoviesAdapter.class.getSimpleName();
	ArrayList<Moviedata> mmoviedatas;
	Context mcontext;

	public interface listitemlistener {
		void onItemselected(Moviedata moviedata);
	}

	listitemlistener mcallback;

	public MoviesAdapter(ArrayList<Moviedata> moviearray, Context context) {
		mmoviedatas = moviearray;
		mcontext = context;

	}

	@Override
	public MyviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = null;
		view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_listitem, parent, false);
		MyviewHolder myviewHolder = new MyviewHolder(view);
		return myviewHolder;

	}

	@Override
	public void onBindViewHolder(MyviewHolder holder, int position) {
		Moviedata moviedata = mmoviedatas.get(position);
		String imagepaths = moviedata.getMmoviepath();
		Uri.Builder uribuilder = Uri.parse("http://image.tmdb.org/t/p/w342/").buildUpon();
		uribuilder.appendPath(imagepaths);
		String uri = uribuilder.build().toString();
		String finaluri = null;
		//added this to remove %2f in uri due to / in the posterpath
		try {
			finaluri = java.net.URLDecoder.decode(uri, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		//	Log.d(TAG,"The uri is"+ finaluri);
		Picasso.with(mcontext).load(finaluri).into(holder.imageView);
	}


	@Override
	public int getItemCount() {
		if (mmoviedatas != null)
			return mmoviedatas.size();
		else
			return 0;
	}

	public class MyviewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		private ImageView imageView;

		public MyviewHolder(View itemView) {
			super(itemView);
			imageView = (ImageView) itemView.findViewById(R.id.imagepath);
			itemView.setOnClickListener(this);
		}

		@Override
		public void onClick(View view) {
			int position = getAdapterPosition();
			if (!(mcontext.getResources().getBoolean(R.bool.isTablet))) {
				urllist.clear();
				videouris.clear();

				OkhttpHandlerReviews okhttpHandlerReviews = new OkhttpHandlerReviews(mcontext, mmoviedatas.get(position).getMmovieid(), new OkhttpHandlerReviews.AsyncResponse() {
					@Override
					public void processFinish(ArrayList<Moviereviewsdata> output) {

						for (int i = 0; i < output.size(); i++) {
							//		Log.d(TAG, "The reviewurl" + output.get(i).getPurl());
							urllist.add(output.get(i).getPurl());
						}

					}

				});
				okhttpHandlerReviews.execute("http://api.themoviedb.org/3/movie/" + mmoviedatas.get(position).getMmovieid() + "/reviews?api_key=50bb9b78ca3a650f255ae2006b702c62");


				OkhttpHandlerVideos okhttpHandlerVideos = new OkhttpHandlerVideos(mcontext, mmoviedatas.get(position).getMmovieid(), new OkhttpHandlerVideos.AsyncResponse() {
					@Override
					public void processFinish(ArrayList<Movievideodata> output) {

						for (int i = 0; i < output.size(); i++) {
							//	Log.d(TAG, "The videokey" + output.get(i).getPmoviekeys());
							videouris.add(output.get(i).getPmoviekeys());
						}
					}

				});
				okhttpHandlerVideos.execute("http://api.themoviedb.org/3/movie/" + mmoviedatas.get(position).getMmovieid() + "/videos?api_key=50bb9b78ca3a650f255ae2006b702c62");

				Intent detailintent = new Intent(mcontext, DetailActivity.class);
				detailintent.putExtra(mcontext.getString(R.string.movie_key), mmoviedatas.get(position));
				mcontext.startActivity(detailintent);
			} else if (mcontext.getResources().getBoolean(R.bool.isTablet)) {
				urllist.clear();
				videouris.clear();
				OkhttpHandlerReviews okhttpHandlerReviews = new OkhttpHandlerReviews(mcontext, mmoviedatas.get(position).getMmovieid(), new OkhttpHandlerReviews.AsyncResponse() {
					@Override
					public void processFinish(ArrayList<Moviereviewsdata> output) {

						for (int i = 0; i < output.size(); i++) {
							//	Log.d(TAG, "The reviewurl" + output.get(i).getPurl());
							urllist.add(output.get(i).getPurl());
						}

					}

				});
				okhttpHandlerReviews.execute("http://api.themoviedb.org/3/movie/" + mmoviedatas.get(position).getMmovieid() + "/reviews?api_key=50bb9b78ca3a650f255ae2006b702c62");


				OkhttpHandlerVideos okhttpHandlerVideos = new OkhttpHandlerVideos(mcontext, mmoviedatas.get(position).getMmovieid(), new OkhttpHandlerVideos.AsyncResponse() {
					@Override
					public void processFinish(ArrayList<Movievideodata> output) {

						for (int i = 0; i < output.size(); i++) {
							//	Log.d(TAG, "The videokey" + output.get(i).getPmoviekeys());
							videouris.add(output.get(i).getPmoviekeys());
						}
					}

				});
				okhttpHandlerVideos.execute("http://api.themoviedb.org/3/movie/" + mmoviedatas.get(position).getMmovieid() + "/videos?api_key=50bb9b78ca3a650f255ae2006b702c62");

				mcallback = (listitemlistener) mcontext;
				//	Log.d(TAG, "inside else part");
				mcallback.onItemselected(mmoviedatas.get(getAdapterPosition()));
			}
		}
	}
}
