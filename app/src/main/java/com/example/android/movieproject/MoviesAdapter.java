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


	private static final String TAG = MoviesAdapter.class.getSimpleName();
	public ArrayList<Moviedata> mmoviedatas;
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
		if (mmoviedatas.size() != 0) {
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
			int positions = getAdapterPosition();
			if (!(mcontext.getResources().getBoolean(R.bool.isTablet))) {
				Intent detailintent = new Intent(mcontext, DetailActivity.class);
				detailintent.putExtra(mcontext.getString(R.string.movie_key), mmoviedatas.get(positions));
				mcontext.startActivity(detailintent);
			}
			if (mcontext.getResources().getBoolean(R.bool.isTablet)) {
				mcallback = (listitemlistener) mcontext;
				mcallback.onItemselected(mmoviedatas.get(getAdapterPosition()));
			}
		}
	}
}

