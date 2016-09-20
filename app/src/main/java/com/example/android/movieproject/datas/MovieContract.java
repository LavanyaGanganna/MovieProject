package com.example.android.movieproject.datas;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by lavanya on 9/13/16.
 */
public class MovieContract {
	public static final String CONTENT_AUTHORITY = "com.example.android.movieproject";
	public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
	public static final String PATH_MOVIEDATA = "moviedetail";
	public static final String PATH_VIDEODATA = "movietrailer";
	public static final String PATH_REVIEWDATA = "moviereview";

	public static final class MovieEntry implements BaseColumns {
		public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIEDATA).build();
		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIEDATA;
		public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIEDATA;
		public static final String TABLE_NAME = "moviedetail";
		public static final String COLUMN_POSTER_PATH = "poster_path";
		public static final String COLUMN_OVERVIEW = "overview";
		public static final String COLUMN_RELDATE = "releasedate";
		public static final String COLUMN_MOVIEID = "movieid";
		public static final String COLUMN_TITLE = "title";
		public static final String COLUMN_VOTES = "votes";

		public static int getmovieidfromuri(Uri uri) {
			return Integer.parseInt(uri.getPathSegments().get(1));
		}

		public static Uri buildMovieUri(long id) {
			return ContentUris.withAppendedId(CONTENT_URI, id);
		}

		public static Uri buildmoviesfavoriteuri(int movieid) {
			return CONTENT_URI.buildUpon().appendPath(Integer.toString(movieid)).build();
		}
	}

	public static final class VideoEntry implements BaseColumns {
		public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_VIDEODATA).build();
		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_VIDEODATA;
		public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_VIDEODATA;
		public static final String TABLE_NAME = "movietrailer";
		public static final String COLUMN_ID_KEY = "movie_ids";
		public static final String COLUMN_MOV_ID = "movieids";
		public static final String COLUMN_VIDEOS = "trailers";

		public static Uri buildMovieUri(long id) {
			return ContentUris.withAppendedId(CONTENT_URI, id);
		}
	}

	public static final class ReviewEntry implements BaseColumns {
		public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_REVIEWDATA).build();
		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEWDATA;
		public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEWDATA;
		public static final String TABLE_NAME = "moviereview";
		public static final String COLUMN_MOVIEID_KEY = "movie_id";
		public static final String COLUMN_MOVIE_ID = "movieides";
		public static final String COLUMN_AUTHOR = "author";
		public static final String COLUMN_URL = "movieurl";

		public static Uri buildMovieUri(long id) {
			return ContentUris.withAppendedId(CONTENT_URI, id);
		}
	}
}
