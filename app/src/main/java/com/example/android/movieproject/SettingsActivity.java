package com.example.android.movieproject;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;

public class SettingsActivity extends AppCompatPreferenceActivity implements Preference.OnPreferenceChangeListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.pref_general);
		bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_options_key)));

	}

	private void bindPreferenceSummaryToValue(Preference preference) {
		// Set the listener to watch for value changes.
		preference.setOnPreferenceChangeListener(this);

		// Trigger the listener immediately with the preference's
		// current value.
		onPreferenceChange(preference, PreferenceManager
				.getDefaultSharedPreferences(preference.getContext())
				.getString(preference.getKey(), ""));
	}


	@Override
	public boolean onPreferenceChange(Preference preference, Object value) {
		String stringValue = value.toString();

		if (preference instanceof ListPreference) {
			// For list preferences, look up the correct display value in
			// the preference's 'entries' list (since they have separate labels/values).
			ListPreference listPreference = (ListPreference) preference;
			int prefIndex = listPreference.findIndexOfValue(stringValue);
			if (prefIndex >= 0) {
				preference.setSummary(listPreference.getEntries()[prefIndex]);
			}
		}

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
			if (!super.onOptionsItemSelected(item)) {
				NavUtils.navigateUpFromSameTask(this);
			}
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
