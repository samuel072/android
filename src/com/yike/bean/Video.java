package com.yike.bean;

public class Video {
	/**
	 *
	 */
	private String videoId;
	private String albumId;
	private String remoteVid;
	private String src;
	private String name;
	private String subname;
	private String installments;
	private String duration;
	private String standardPic;
	private String description;
	private String tag;
	private String createTime;

	public String getVideoId() {
		return videoId;
	}

	public void setVideoId(String videoId) {
		this.videoId = videoId;
	}

	public String getAlbumId() {
		return albumId;
	}

	public void setAlbumId(String albumId) {
		this.albumId = albumId;
	}

	public String getRemoteVid() {
		return remoteVid;
	}

	public void setRemoteVid(String remoteVid) {
		this.remoteVid = remoteVid;
	}

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSubname() {
		return subname;
	}

	public void setSubname(String subname) {
		this.subname = subname;
	}

	public String getInstallments() {
		return installments;
	}

	public void setInstallments(String installments) {
		this.installments = installments;
	}

	public String getDuration() {
		return duration;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getStandardPic() {
		return standardPic;
	}

	public void setStandardPic(String standardPic) {
		this.standardPic = standardPic;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

}