package com.example.android.movieproject.datas;

/**
 * Created by lavanya on 9/9/16.
 */
public class Movievideodata {
	int pmovieids;
	String pmoviekeys;

	public Movievideodata(int movieids, String moviekeys) {
		pmovieids = movieids;
		pmoviekeys = moviekeys;
	}

	public int getPmovieids() {
		return pmovieids;
	}

	public void setPmovieids(int pmovieids) {
		this.pmovieids = pmovieids;
	}

	public String getPmoviekeys() {
		return pmoviekeys;
	}

	public void setPmoviekeys(String pmoviekeys) {
		this.pmoviekeys = pmoviekeys;
	}
}
