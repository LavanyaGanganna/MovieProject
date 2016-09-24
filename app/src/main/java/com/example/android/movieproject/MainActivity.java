package com.example.android.movieproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.movieproject.datas.MovieContract;
import com.example.android.movieproject.datas.Moviedata;

import java.util.ArrayList;
import java.util.Vector;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.listitemlistener {

	private static final String TAG = MainActivity.class.getSimpleName();
	private static final String MOVIE_FRAGMENT = "MFTAG";
	private static final String MOVIE_DETAILFRAGMENT = "DFTAG";
	TabLayout tabLayout;
	ViewPager viewPager;
	Moviedata copymoviedata;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		toolbar.setTitle(R.string.appbar_title);

		boolean tabletsize = getResources().getBoolean(R.bool.isTablet);

		if (tabletsize) {
			Log.d(TAG, "inside the tablet");

			if (savedInstanceState == null) {
				getSupportFragmentManager().beginTransaction().replace(R.id.tabletplaceholder, new MainActivityFragment(), MOVIE_FRAGMENT).addToBackStack(null).commit();
				getSupportFragmentManager().beginTransaction().replace(R.id.tabletdetailholder, new DetailFragment(), MOVIE_DETAILFRAGMENT).addToBackStack(null).commit();
			}

		} else {

			viewPager = (ViewPager) findViewById(R.id.viewpager);
			final CustomPageAdapter pageradapter = new CustomPageAdapter(getSupportFragmentManager(), getApplicationContext());
			if (viewPager != null) {
				viewPager.setAdapter(pageradapter);
				viewPager.setOffscreenPageLimit(3);
			}

			//	pageradapter.notifyDataSetChanged();
			tabLayout = (TabLayout) findViewById(R.id.tablayoutview);
			if (tabLayout != null) {
				tabLayout.setupWithViewPager(viewPager);

				viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

				tabLayout.setTabsFromPagerAdapter(pageradapter);


				tabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {
					@Override
					public void onTabSelected(TabLayout.Tab tab) {
						viewPager.setCurrentItem(tab.getPosition());
						pageradapter.notifyDataSetChanged();

					}

					@Override
					public void onTabUnselected(TabLayout.Tab tab) {
						//Log.d(TAG,"the tab is" + tab.getPosition());
						viewPager.setCurrentItem(tab.getPosition());
					}

					@Override
					public void onTabReselected(TabLayout.Tab tab) {
						//	Log.d(TAG,"the tab is" + tab.getPosition());
						viewPager.setCurrentItem(tab.getPosition());
					}
				});

			}
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (!(getResources().getBoolean(R.bool.isTablet))) {
			Log.d(TAG, "the pagepositon" + viewPager.getCurrentItem());
			outState.putInt("tabposition", viewPager.getCurrentItem());
		} else
			outState.putParcelable("saveddata", copymoviedata);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		if (!(getResources().getBoolean(R.bool.isTablet))) {
			Log.d(TAG, "inside restore");
			viewPager.setCurrentItem(savedInstanceState.getInt("tabposition"));
		} else
			savedInstanceState.getParcelable("saveddata");

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
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

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onItemselected(Moviedata moviedata) {
		if (getResources().getBoolean(R.bool.isTablet)) {
			Log.d(TAG, "inside itemselected interface");
			Bundle arguments = new Bundle();
			arguments.putParcelable(getString(R.string.movie_key), moviedata);
			copymoviedata = moviedata;
			DetailFragment detailFragment = new DetailFragment();
			detailFragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction().replace(R.id.tabletdetailholder, detailFragment, MOVIE_DETAILFRAGMENT).commit();
		}

	}


}


