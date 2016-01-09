package com.yike.bean;

import java.util.List;

public class SearchData<T, R> {

	private List<T> albumList;
	private List<R> videoList;

	public List<T> getAlbumList() {
		return albumList;
	}

	public void setAlbumList(List<T> albumList) {
		this.albumList = albumList;
	}

	public List<R> getVideoList() {
		return videoList;
	}

	public void setVideoList(List<R> videoList) {
		this.videoList = videoList;
	}

}
