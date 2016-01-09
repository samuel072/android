package com.yike.bean;

public class TopicDetailBean {
    private String name;
    private String standardPic;
    private String videoDetailUrl;
    private int albumId;
    private int videoId;
    private String description;
    private long modifyTime;
    private long showTime;
    private long endTime;

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

    public int getAlbumId() {
        return albumId;
    }

    public void setAlbumId(int albumId) {
        this.albumId = albumId;
    }

    public int getVideoId() {
        return videoId;
    }

    public void setVideoId(int videoId) {
        this.videoId = videoId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoDetailUrl() {
        return videoDetailUrl;
    }

    public void setVideoDetailUrl(String videoDetailUrl) {
        this.videoDetailUrl = videoDetailUrl;
    }

    public long getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(long modifyTime) {
        this.modifyTime = modifyTime;
    }

    public long getShowTime() {
        return showTime;
    }

    public void setShowTime(long showTime) {
        this.showTime = showTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

}
