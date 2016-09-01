package com.example.android.movieproject;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lavanya on 8/29/16.
 */
public class Moviedata implements Parcelable {
	String mmoviepath;
	String moverview;
	String mreleasedate;
	String mtitle;
	Double mvotes;

	Moviedata(String moviepath, String movieoverview, String releasedate, String title, Double votes) {
		mmoviepath = moviepath;
		moverview = movieoverview;
		mreleasedate = releasedate;
		mtitle = title;
		mvotes = votes;
	}

	protected Moviedata(Parcel in) {
		mmoviepath = in.readString();
		moverview = in.readString();
		mreleasedate = in.readString();
		mtitle = in.readString();
		mvotes = in.readDouble();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mmoviepath);
		dest.writeString(moverview);
		dest.writeString(mreleasedate);
		dest.writeString(mtitle);
		dest.writeDouble(mvotes);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<Moviedata> CREATOR = new Creator<Moviedata>() {
		@Override
		public Moviedata createFromParcel(Parcel in) {
			return new Moviedata(in);
		}

		@Override
		public Moviedata[] newArray(int size) {
			return new Moviedata[size];
		}
	};

	public String getMoverview() {
		return moverview;
	}

	public void setMoverview(String moverview) {
		this.moverview = moverview;
	}

	public String getMreleasedate() {
		return mreleasedate;
	}

	public void setMreleasedate(String mreleasedate) {
		this.mreleasedate = mreleasedate;
	}

	public String getMtitle() {
		return mtitle;
	}

	public void setMtitle(String mtitle) {
		this.mtitle = mtitle;
	}

	public Double getMvotes() {
		return mvotes;
	}

	public void setMvotes(Double mvotes) {
		this.mvotes = mvotes;
	}

	public String getMmoviepath() {
		return mmoviepath;
	}

	public void setMmoviepath(String mmoviepath) {
		this.mmoviepath = mmoviepath;
	}
}
