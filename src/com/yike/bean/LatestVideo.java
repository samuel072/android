/**
 * 
 */
package com.yike.bean;

/**
 * @author： fengqingyun2008
 * @Email： fengqingyun2008@gmail.com
 * @version：1.0
 * @创建时间：2015-4-4 下午3:28:52
 * @类说明：专辑详情中的最后一个视频信息
 */
public class LatestVideo<T> {
	private LiveInfo liveInfo;
	private String name;
	private String standardPic;
	private int videoId;
	private int albumId;
	private int isPay;
	private int isNeedPay;
	private int vedioType;
	private T videoPlayInfo;

	public T getVideoPlayInfo() {
		return videoPlayInfo;
	}

	public void setVideoPlayInfo(T videoPlayInfo) {
		this.videoPlayInfo = videoPlayInfo;
	}

	public int getVideoId() {
		return videoId;
	}

	public void setVideoId(int videoId) {
		this.videoId = videoId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStandardPic() {
		return standardPic;
	}

	public void setStandardPic(String standardPic) {
		this.standardPic = standardPic;
	}

	public int getVedioType() {
		return vedioType;
	}

	public void setVedioType(int vedioType) {
		this.vedioType = vedioType;
	}

	public LiveInfo getLiveInfo() {
		return liveInfo;
	}

	public void setLiveInfo(LiveInfo liveInfo) {
		this.liveInfo = liveInfo;
	}

	public int getIsPay() {
		return isPay;
	}

	public void setIsPay(int isPay) {
		this.isPay = isPay;
	}

	public int getIsNeedPay() {
		return isNeedPay;
	}

	public void setIsNeedPay(int isNeedPay) {
		this.isNeedPay = isNeedPay;
	}

	public int getAlbumId() {
		return albumId;
	}

	public void setAlbumId(int albumId) {
		this.albumId = albumId;
	}

}
