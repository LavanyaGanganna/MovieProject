package com.example.android.movieproject;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.movieproject.datas.MovieContract;
import com.example.android.movieproject.datas.Moviedata;
import com.example.android.movieproject.datas.Moviereviewsdata;
import com.example.android.movieproject.datas.Movievideodata;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lavanya on 8/29/16.
 */
public class DetailFragment extends Fragment {
	private static final String TAG = DetailFragment.class.getSimpleName();
	public static ArrayList<String> videouris = new ArrayList<String>();
	public static ArrayList<String> urllist = new ArrayList<String>();
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
	public static String yearsy;
	public static Moviedata moviedata;


	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_detail, container, false);
		ButterKnife.bind(this, view);
		arguments = getArguments();
		if (arguments != null) {
			moviedata = arguments.getParcelable(getString(R.string.movie_key));
			if (moviedata != null) {
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
				videouris.clear();
				OkhttpHandlerVideos okhttpHandlerVideos = new OkhttpHandlerVideos(getContext(), moviedata.getMmovieid(), new OkhttpHandlerVideos.AsyncResponse() {
					@Override
					public void processFinish(ArrayList<Movievideodata> output) {

						for (int i = 0; i < output.size(); i++) {
							//	Log.d(TAG, "The videokey" + output.get(i).getPmoviekeys());
							videouris.add(output.get(i).getPmoviekeys());
						}
					}

				});
				okhttpHandlerVideos.execute("http://api.themoviedb.org/3/movie/" + moviedata.getMmovieid() + "/videos?api_key=50bb9b78ca3a650f255ae2006b702c62");
				urllist.clear();

				OkhttpHandlerReviews okhttpHandlerReviews = new OkhttpHandlerReviews(getContext(), moviedata.getMmovieid(), new OkhttpHandlerReviews.AsyncResponse() {
					@Override
					public void processFinish(ArrayList<Moviereviewsdata> output) {

						for (int i = 0; i < output.size(); i++) {
							//		Log.d(TAG, "The reviewurl" + output.get(i).getPurl());
							urllist.add(output.get(i).getPurl());
						}

					}

				});
				okhttpHandlerReviews.execute("http://api.themoviedb.org/3/movie/" + moviedata.getMmovieid() + "/reviews?api_key=50bb9b78ca3a650f255ae2006b702c62");

				button.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {

						Bundle bundle = new Bundle();
						bundle.putStringArrayList("urllist", urllist);
						bundle.putInt("movieid", movieids);
						Intent reviewintent = new Intent(getContext(), ReviewsActivity.class);
						reviewintent.putExtra("urlbundle", bundle);
						startActivity(reviewintent);
					}


				});
				playbutton1.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {

						if ((videouris.size() > 0) && (videouris.get(0) != null)) {

							Uri videouri = Uri.parse("http://www.youtube.com/watch?v=" + videouris.get(0)).buildUpon().build();
							//		Log.d(TAG,"the videouri"+ videouri);
							Intent trailintent = new Intent(Intent.ACTION_VIEW, videouri);
							if (trailintent.resolveActivity(getActivity().getPackageManager()) != null) {
								startActivity(trailintent);
							}

						} else {
							Toast.makeText(getContext(), "No trailer to show", Toast.LENGTH_SHORT).show();
						}
					}

				});

				playbutton2.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {

						if ((videouris.size() > 1) && (videouris.get(1) != null)) {
							Uri videouri = Uri.parse("http://www.youtube.com/watch?v=" + videouris.get(1)).buildUpon().build();
							//	Log.d(TAG,"the videouri"+ videouri);
							Intent trailintent = new Intent(Intent.ACTION_VIEW, videouri);
							if (trailintent.resolveActivity(getActivity().getPackageManager()) != null) {
								startActivity(trailintent);
							}


						} else {
							Toast.makeText(getContext(), "No trailer to show", Toast.LENGTH_SHORT).show();
						}
					}
				});

				favoritebutton.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						final MyDatabaseQuery myDatabaseQuery = new MyDatabaseQuery(getContext());
						Uri movieuri = MovieContract.MovieEntry.buildMovieUri(moviedata.getMmovieid());
						myDatabaseQuery.execute(movieuri.toString());

					}
				});

			}
		}

		return view;

	}
}

