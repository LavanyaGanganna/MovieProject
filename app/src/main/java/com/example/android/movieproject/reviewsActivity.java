package com.example.android.movieproject;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.movieproject.datas.Moviereviewsdata;

import java.util.ArrayList;

public class reviewsActivity extends AppCompatActivity {
	private static final String TAG = reviewsActivity.class.getSimpleName();
	LinearLayout linearLayout;
	ProgressBar progressBar;
	final ArrayList<String> urllist = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reviews);
		progressBar = (ProgressBar) findViewById(R.id.progressBar2);
		progressBar.setVisibility(View.VISIBLE);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		Bundle b = getIntent().getBundleExtra("urlbundle");
		ArrayList<String> urlList = b.getStringArrayList("urllist");
		//	Log.d(TAG,"inside review urllist" + urlList.size());
		final ArrayList<TextView> textarraylist = new ArrayList<TextView>();
		if (urlList.size() == 0) {
			int movieid = b.getInt("movieid");
			//		Log.d(TAG, "inside review urllist" + urlList.size() + movieid);
			OkhttpHandlerReviews okhttpHandlerReviews = new OkhttpHandlerReviews(getApplicationContext(), movieid, new OkhttpHandlerReviews.AsyncResponse() {
				@Override
				public void processFinish(ArrayList<Moviereviewsdata> output) {

					for (int i = 0; i < output.size(); i++) {
						//		Log.d(TAG, "The reviewurl" + output.get(i).getPurl());
						urllist.add(output.get(i).getPurl());
					}
					runOnUiThread(new Runnable() {
						@Override
						public void run() {

							linearLayout = (LinearLayout) findViewById(R.id.reviewlinearlayout);
							LinearLayoutCompat.LayoutParams layoutParams = new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
							TextView textView = new TextView(reviewsActivity.this);
							textView.setLayoutParams(layoutParams);
							textView.setPadding(0, 0, 0, 20);
							textView.setText("Reviews:");
							linearLayout.addView(textView);
							progressBar.setVisibility(View.GONE);
							for (int j = 0; j < urllist.size(); j++) {
								if (!(urllist.get(j) == null)) {
									LinearLayoutCompat.LayoutParams layoutParam = new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
									TextView textViews = new TextView(reviewsActivity.this);
									textViews.setLayoutParams(layoutParam);
									textViews.setPadding(0, 0, 0, 20);
									textViews.setClickable(true);
									textViews.setMovementMethod(LinkMovementMethod.getInstance());
									String datas = urllist.get(j);
									String linkedtext = String.format("<a href=\"%s\">" + datas + "</a>", urllist.get(j));
									textViews.setText(Html.fromHtml(linkedtext));
									linearLayout.addView(textViews);
									textarraylist.add(textViews);
								}

							}
							if (textarraylist.size() == 0)
								Toast.makeText(reviewsActivity.this, "No reviews to show", Toast.LENGTH_SHORT).show();
						}

					});

				}
			});

			okhttpHandlerReviews.execute("http://api.themoviedb.org/3/movie/" + movieid + "/reviews?api_key=50bb9b78ca3a650f255ae2006b702c62");

		} else {
			progressBar.setVisibility(View.GONE);
			linearLayout = (LinearLayout) findViewById(R.id.reviewlinearlayout);
			LinearLayoutCompat.LayoutParams layoutParams = new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			TextView textView = new TextView(this);
			textView.setLayoutParams(layoutParams);
			textView.setPadding(0, 0, 0, 20);
			textView.setText("Reviews:");
			linearLayout.addView(textView);

			for (int i = 0; i < urlList.size(); i++) {
				if (!(urlList.get(i) == null)) {
					LinearLayoutCompat.LayoutParams layoutParam = new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
					TextView textViews = new TextView(this);
					textViews.setLayoutParams(layoutParam);
					textViews.setPadding(0, 0, 0, 20);
					textViews.setClickable(true);
					textViews.setMovementMethod(LinkMovementMethod.getInstance());
					String datas = urlList.get(i);
					String linkedtext = String.format("<a href=\"%s\">" + datas + "</a>", urlList.get(i));
					textViews.setText(Html.fromHtml(linkedtext));
					linearLayout.addView(textViews);
					textarraylist.add(textViews);
				}

			}
			if (textarraylist.size() == 0)
				Toast.makeText(this, "No reviews to show", Toast.LENGTH_SHORT).show();
		}
		//	Log.d(TAG, "the arraylistsize" + textarraylist.size());

	}

}


