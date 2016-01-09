/**
 * 
 */
package com.yike.bean;

/**
 * @author： fengqingyun2008
 * @Email： fengqingyun2008@gmail.com
 * @version：1.0
 * @创建时间：2015-4-4 下午3:43:44
 * @类说明：
 */
public class AlbumLastVideo<T> {
	private LatestVideo<T> latestVideo;
	private String name;

	public LatestVideo<T> getLatestVideo() {
		return latestVideo;
	}

	public void setLatestVideo(LatestVideo<T> latestVideo) {
		this.latestVideo = latestVideo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
