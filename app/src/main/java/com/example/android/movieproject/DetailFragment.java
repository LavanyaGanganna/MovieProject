package com.example.android.movieproject;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;

/**
 * Created by lavanya on 8/29/16.
 */
public class DetailFragment extends Fragment {
	private static final String TAG = DetailFragment.class.getSimpleName();
	TextView mtitle,moverview,myear,ratingvotes;
	ImageView imageView;
	Bundle arguments;
	View view;
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		 view=inflater.inflate(R.layout.fragment_detail,container,false);

		mtitle=(TextView)view.findViewById(R.id.movietitle);
		moverview=(TextView)view.findViewById(R.id.movieoverview);
		myear=(TextView)view.findViewById(R.id.releaseyear);
		ratingvotes=(TextView)view.findViewById(R.id.voteaverage);
		imageView=(ImageView)view.findViewById(R.id.imageposters);
		arguments=getArguments();
		Moviedata moviedata=arguments.getParcelable(getString(R.string.movie_key));
		mtitle.setText(moviedata.getMtitle());
		moverview.setText(moviedata.getMoverview());
		String relyear=moviedata.getMreleasedate();
		String[] arry=relyear.split("-");
		myear.setText(arry[0]);
		Double votes=moviedata.getMvotes();
		String displayvotes=votes+"/10";
		ratingvotes.setText(displayvotes);
		String moviepath=moviedata.getMmoviepath();
		Uri.Builder uribuilder=Uri.parse("http://image.tmdb.org/t/p/w342/").buildUpon();
		uribuilder.appendPath(moviepath);
		String uri=uribuilder.build().toString();
		String finaluri= null;
		//added this to remove %2f in uri due to / in the posterpath
		try {
			finaluri = java.net.URLDecoder.decode(uri, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	//	Log.d(TAG,"The uri is"+ finaluri);
		Picasso.with(getActivity()).load(finaluri).into(imageView);
		return view;
	}

}
