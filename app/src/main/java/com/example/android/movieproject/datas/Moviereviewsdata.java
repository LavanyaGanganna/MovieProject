package com.example.android.movieproject.datas;

/**
 * Created by lavanya on 9/9/16.
 */
public class Moviereviewsdata {
	int pmovieids;
	String pauthor;
	String purl;

	public Moviereviewsdata(int movieids, String author, String url) {
		pmovieids = movieids;
		pauthor = author;
		purl = url;
	}

	public int getPmovieids() {
		return pmovieids;
	}

	public String getPauthor() {
		return pauthor;
	}

	public String getPurl() {
		return purl;
	}
}
