package com.example.android.movieproject;

import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.movieproject.datas.MovieContract;
import com.example.android.movieproject.datas.Moviedata;
import com.example.android.movieproject.datas.Moviereviewsdata;
import com.example.android.movieproject.datas.Movievideodata;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static java.lang.Math.toIntExact;

/**
 * Created by lavanya on 8/29/16.
 */
public class DetailFragment extends Fragment {
	private static final String TAG = DetailFragment.class.getSimpleName();
	//public static ArrayList<String> videouris = new ArrayList<String>();
	@BindView(R.id.movietitle)
	TextView mtitle;
	@BindView(R.id.movieoverview)
	TextView moverview;
	@BindView(R.id.releaseyear)
	TextView myear;
	@BindView(R.id.voteaverage)
	TextView ratingvotes;
	@BindView(R.id.imageposters)
	ImageView imageView;
	@BindView(R.id.reviewbutton)
	Button button;
	@BindView(R.id.playtrailer1)
	Button playbutton1;
	protected Bundle arguments;
	@BindView(R.id.playtrailer2)
	Button playbutton2;
	@BindView(R.id.FavoriteButton)
	Button favoritebutton;
	View view;
	int movieids;
	//ArrayList<String> urllist = new ArrayList<String>();
	//ArrayList<ContentValues> contentValuesArrayList=new ArrayList<ContentValues>();
	String yearsy;
	Moviedata moviedata;


	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_detail, container, false);
		ButterKnife.bind(this, view);
		arguments = getArguments();
		if (arguments != null) {
			moviedata = arguments.getParcelable(getString(R.string.movie_key));
			movieids = moviedata.getMmovieid();
			Log.d(TAG, "got the data");
			mtitle.setText(moviedata.getMtitle());
			moverview.setText(moviedata.getMoverview());
			String relyear = moviedata.getMreleasedate();
			String[] arry = relyear.split("-");
			yearsy = arry[0];
			myear.setText(arry[0]);
			Double votes = moviedata.getMvotes();
			String displayvotes = votes + "/10";
			ratingvotes.setText(displayvotes);
			String moviepath = moviedata.getMmoviepath();
			Uri.Builder uribuilder = Uri.parse("http://image.tmdb.org/t/p/w342/").buildUpon();
			uribuilder.appendPath(moviepath);
			String uri = uribuilder.build().toString();
			String finaluri = null;
			//added this to remove %2f in uri due to / in the posterpath
			try {
				finaluri = java.net.URLDecoder.decode(uri, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			Picasso.with(getActivity()).load(finaluri).into(imageView);

			button.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {

					Bundle bundle = new Bundle();
					bundle.putStringArrayList("urllist", MoviesAdapter.urllist);
					bundle.putInt("movieid", movieids);
					Intent reviewintent = new Intent(getContext(), reviewsActivity.class);
					reviewintent.putExtra("urlbundle", bundle);
					startActivity(reviewintent);
				}


			});
			playbutton1.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {

					if ((MoviesAdapter.videouris.size() > 0) && (MoviesAdapter.videouris.get(0) != null)) {

						Uri videouri = Uri.parse("http://www.youtube.com/watch?v=" + MoviesAdapter.videouris.get(0)).buildUpon().build();
						//		Log.d(TAG,"the videouri"+ videouri);
						Intent trailintent = new Intent(Intent.ACTION_VIEW, videouri);
						startActivity(trailintent);
					} else {
						Toast.makeText(getContext(), "No trailer to show", Toast.LENGTH_SHORT).show();
					}
				}

			});

			playbutton2.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {

					if ((MoviesAdapter.videouris.size() > 1) && (MoviesAdapter.videouris.get(1) != null)) {
						Uri videouri = Uri.parse("http://www.youtube.com/watch?v=" + MoviesAdapter.videouris.get(1)).buildUpon().build();
						//	Log.d(TAG,"the videouri"+ videouri);
						Intent trailintent = new Intent(Intent.ACTION_VIEW, videouri);
						startActivity(trailintent);

					} else {
						Toast.makeText(getContext(), "No trailer to show", Toast.LENGTH_SHORT).show();
					}
				}
			});

			favoritebutton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					Uri movieuri = MovieContract.MovieEntry.buildMovieUri(moviedata.getMmovieid());
					Cursor cursor = getActivity().getContentResolver().query(movieuri, null, null, null, null);
					if (cursor != null && cursor.moveToFirst()) {
						Toast.makeText(getContext(), "Exists in Favorite", Toast.LENGTH_SHORT).show();

					} else {
						ContentValues contentValues = new ContentValues();
						contentValues.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, moviedata.getMmoviepath());
						contentValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, moviedata.getMoverview());
						contentValues.put(MovieContract.MovieEntry.COLUMN_RELDATE, yearsy);
						contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIEID, moviedata.getMmovieid());
						contentValues.put(MovieContract.MovieEntry.COLUMN_TITLE, moviedata.getMtitle());
						contentValues.put(MovieContract.MovieEntry.COLUMN_VOTES, moviedata.getMvotes());
						//	contentValuesArrayList.add(contentValues);
						Uri returi = getContext().getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, contentValues);
						long rowid = ContentUris.parseId(returi);
						//	Log.d(TAG, "the uri is " + returi);
						ContentValues contentValues1 = new ContentValues();

						for (int i = 0; i < MoviesAdapter.videouris.size(); i++) {
							contentValues1.put(MovieContract.VideoEntry.COLUMN_ID_KEY, (int) rowid);
							contentValues1.put(MovieContract.VideoEntry.COLUMN_VIDEOS, MoviesAdapter.videouris.get(i));
							contentValues1.put(MovieContract.VideoEntry.COLUMN_MOV_ID, moviedata.getMmovieid());
							Uri returin = getContext().getContentResolver().insert(MovieContract.VideoEntry.CONTENT_URI, contentValues1);
							//		Log.d(TAG, "the uri is " + returin + "the i is " + i);
						}
						ContentValues contentValues2 = new ContentValues();
						for (int i = 0; i < MoviesAdapter.urllist.size(); i++) {
							contentValues2.put(MovieContract.ReviewEntry.COLUMN_MOVIEID_KEY, (int) rowid);
							contentValues2.put(MovieContract.ReviewEntry.COLUMN_MOVIE_ID, moviedata.getMmovieid());
							contentValues2.put(MovieContract.ReviewEntry.COLUMN_AUTHOR, "sheik");
							contentValues2.put(MovieContract.ReviewEntry.COLUMN_URL, MoviesAdapter.urllist.get(i));
							Uri returint = getContext().getContentResolver().insert(MovieContract.ReviewEntry.CONTENT_URI, contentValues2);
							//		Log.d(TAG, "the uri is " + returint);
						}
						Toast.makeText(getContext(), "Added to Favorites", Toast.LENGTH_SHORT).show();
					}

				}
			});

		}

		return view;

	}
}

