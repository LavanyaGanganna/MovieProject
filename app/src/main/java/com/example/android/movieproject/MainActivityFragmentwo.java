package com.example.android.movieproject;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.android.movieproject.datas.Moviedata;
import com.example.android.movieproject.datas.Moviereviewsdata;
import com.example.android.movieproject.datas.Movievideodata;

import java.util.ArrayList;

/**
 * Created by lavanya on 9/12/16.
 */
public class MainActivityFragmentwo extends Fragment {

	private static final String TAG = MainActivityFragment.class.getSimpleName();
	View view;
	RecyclerView recyclerview;
	ProgressBar progressBartwo;
	MoviesAdapter moviesAdapter;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//	uristr=getArguments().getString("urllink2");
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_maintwo, container, false);
		recyclerview = (RecyclerView) view.findViewById(R.id.recyclerviewtwo);
		Display display = getActivity().getWindowManager().getDefaultDisplay();
		DisplayMetrics outMetrics = new DisplayMetrics();
		display.getMetrics(outMetrics);
		float density = getResources().getDisplayMetrics().density;
		float dpWidth = outMetrics.widthPixels / density;
		int columns = Math.round(dpWidth / 200);
		RecyclerView.LayoutManager mlayoutmanager = new GridLayoutManager(getActivity(), columns);
		recyclerview.setLayoutManager(mlayoutmanager);
		progressBartwo = (ProgressBar) view.findViewById(R.id.progressBartwo);
		recyclerview.setAdapter(moviesAdapter);

		return view;

	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		if (moviesAdapter == null) {
			//	Log.d(TAG, "progeess bar VISIBLE");
			progressBartwo.setVisibility(View.VISIBLE);
		}
		updatemoviedata();
	}


	private void updatemoviedata() {

		OkhttpHandler okhttphandler = new OkhttpHandler(getActivity(), new OkhttpHandler.AsyncResponse() {
			@Override
			public void processFinish(ArrayList<Moviedata> output) {

			}

		});
/*	e38c1fbe414f3cbb8d0878562ff7ff5d	*/
		String myuri = Uri.parse("http://api.themoviedb.org/3/movie/top_rated?api_key=50bb9b78ca3a650f255ae2006b702c62").toString();
		okhttphandler.execute(myuri);
	}
}
