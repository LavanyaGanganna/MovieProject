package com.example.android.movieproject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.android.movieproject.datas.Moviedata;
import com.example.android.movieproject.datas.Moviereviewsdata;
import com.example.android.movieproject.datas.Movievideodata;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {

	private static final String TAG = DetailActivity.class.getSimpleName();
	public static final String FORECAST_SHARE_HASHTAG = "#Movieproject";
	int movieid = 0;
	Uri videouri;
	Intent shareintent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		toolbar.setTitle(R.string.detail_appbar_title);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(R.string.detail_appbar_title);
		Intent detailintent = getIntent();
		Moviedata moviedatas = detailintent.getParcelableExtra(getString(R.string.movie_key));
		movieid = moviedatas.getMmovieid();
		Log.d(TAG, "got movieid" + movieid);
		Bundle mybundle = new Bundle();
		mybundle.putParcelable(getString(R.string.movie_key), moviedatas);
		DetailFragment detailfragment = new DetailFragment();
		detailfragment.setArguments(mybundle);
		FragmentManager fragmentmanager = getSupportFragmentManager();
		FragmentTransaction fragmentransaction = fragmentmanager.beginTransaction();
		fragmentransaction.replace(R.id.placeholder, detailfragment);
		fragmentransaction.commit();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_detail, menu);
		MenuItem menuItem = menu.findItem(R.id.Shareaction);
		ShareActionProvider mshareactionprovider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
		if (mshareactionprovider != null) {
			mshareactionprovider.setShareIntent(shareintent);
		} else {
			Log.d(TAG, "share action provider is null");
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			Intent settingintent = new Intent(this, SettingsActivity.class);
			startActivity(settingintent);
			return true;
		}
		if (id == R.id.Shareaction) {
			if (MoviesAdapter.videouris.size() > 0) {
				videouri = Uri.parse("http://www.youtube.com/watch?v=" + MoviesAdapter.videouris.get(0)).buildUpon().build();
			}
			shareintent = new Intent(android.content.Intent.ACTION_SEND);
			String sharebody = videouri + FORECAST_SHARE_HASHTAG;
			shareintent.setType("text/plain");
			shareintent.putExtra(Intent.EXTRA_TEXT, sharebody);
			startActivity(Intent.createChooser(shareintent, "share via"));
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
