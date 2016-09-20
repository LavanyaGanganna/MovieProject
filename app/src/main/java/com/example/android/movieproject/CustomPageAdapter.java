package com.example.android.movieproject;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by lavanya on 9/12/16.
 */
public class CustomPageAdapter extends FragmentStatePagerAdapter {
	private static final String TAG = CustomPageAdapter.class.getSimpleName();
	private ArrayList<Fragment> myfragments = new ArrayList<Fragment>();
	public String[] fragments = {"TopRated", "Popular", "Favorite"};
	Context mcontext;

	public CustomPageAdapter(FragmentManager fragmentManager, Context context) {
		super(fragmentManager);
		mcontext = context;
	}

	@Override
	public Fragment getItem(int position) {
//		Log.d(TAG, "the fragment clicked" + myfragments.get(position));
		Fragment fragment = null;
		switch (position) {
			case 0:
				fragment = MainActivityFragmentwo.instantiate(mcontext, MainActivityFragmentwo.class.getName());
				return fragment;
			case 1:
				fragment = MainActivityFragment.instantiate(mcontext, MainActivityFragment.class.getName());
				return fragment;
			case 2:
				fragment = MainActivityFragmenthree.instantiate(mcontext, MainActivityFragmenthree.class.getName());
				return fragment;

			default:
				return null;
		}

	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

	@Override
	public int getCount() {
		return fragments.length;
	}

	public CharSequence getPageTitle(int position) {
		return fragments[position];
	}

}

