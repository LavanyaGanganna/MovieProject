package com.example.android.movieproject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RemoteViews;

import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;

/**
 * Created by lavanya on 8/29/16.
 */
public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MyviewHolder> {

	private static final String TAG = MoviesAdapter.class.getSimpleName();
	Moviedata[] mmoviedatas;
	Context mcontext;

	public MoviesAdapter(Moviedata[] moviearray, Context context){
		mmoviedatas=moviearray;
		mcontext=context;
	}
	@Override
	public MyviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_listitem,parent,false);
		MyviewHolder myviewHolder=new MyviewHolder(view);
		return myviewHolder;
	}

	@Override
	public void onBindViewHolder(MyviewHolder holder, int position) {
		Moviedata moviedata=mmoviedatas[position];
		String imagepaths=moviedata.getMmoviepath();
		Uri.Builder uribuilder=Uri.parse("http://image.tmdb.org/t/p/w342/").buildUpon();
		uribuilder.appendPath(imagepaths);
		String uri=uribuilder.build().toString();
		String finaluri= null;
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
		return mmoviedatas.length;
	}

	public class MyviewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
		private ImageView imageView;
		public MyviewHolder(View itemView) {
			super(itemView);
			imageView= (ImageView) itemView.findViewById(R.id.imagepath);
			itemView.setOnClickListener(this);
		}

		@Override
		public void onClick(View view) {
			int position=getAdapterPosition();
			Intent detailintent=new Intent(mcontext,DetailActivity.class);
			detailintent.putExtra(mcontext.getString(R.string.movie_key),mmoviedatas[position]);
			mcontext.startActivity(detailintent);
		}
	}
}
