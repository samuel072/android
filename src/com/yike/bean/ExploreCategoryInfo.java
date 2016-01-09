package com.yike.bean;

public class ExploreCategoryInfo {
    private String address;
    private int categoryId;
    private long id;
    private String title;
    private String userUuid;
    private String videoDetailUrl;
    private long videoId;
    private String thumbnail;
    private int state;
    private TimeInfo startTime;
    private TimeInfo endTime;
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public int getCategoryId() {
        return categoryId;
    }
    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getUserUuid() {
        return userUuid;
    }
    public void setUserUuid(String userUuid) {
        this.userUuid = userUuid;
    }
    public String getVideoDetailUrl() {
        return videoDetailUrl;
    }
    public void setVideoDetailUrl(String videoDetailUrl) {
        this.videoDetailUrl = videoDetailUrl;
    }
    public long getVideoId() {
        return videoId;
    }
    public void setVideoId(long videoId) {
        this.videoId = videoId;
    }
    public String getThumbnail() {
        return thumbnail;
    }
    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
    public int getState() {
        return state;
    }
    public void setState(int state) {
        this.state = state;
    }
    public TimeInfo getStartTime() {
        return startTime;
    }
    public void setStartTime(TimeInfo startTime) {
        this.startTime = startTime;
    }
    public TimeInfo getEndTime() {
        return endTime;
    }
    public void setEndTime(TimeInfo endTime) {
        this.endTime = endTime;
    }
    
    
}
