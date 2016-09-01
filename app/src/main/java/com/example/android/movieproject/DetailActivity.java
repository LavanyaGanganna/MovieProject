package com.example.android.movieproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class DetailActivity extends AppCompatActivity {

	private static final String TAG =DetailActivity.class.getSimpleName() ;

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
		Bundle mybundle=new Bundle();
		mybundle.putParcelable(getString(R.string.movie_key),moviedatas);
		DetailFragment detailfragment=new DetailFragment();
		detailfragment.setArguments(mybundle);
		FragmentManager fragmentmanager=getSupportFragmentManager();
		FragmentTransaction fragmentransaction=fragmentmanager.beginTransaction();
		fragmentransaction.replace(R.id.placeholder,detailfragment);
		fragmentransaction.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_detail, menu);
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
			Intent settingintent=new Intent(this,SettingsActivity.class);
			startActivity(settingintent);
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}