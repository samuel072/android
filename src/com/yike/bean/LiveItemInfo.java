package com.yike.bean;

/**
 * 直播页面,每一个直接视频info
 * Created by wangwei-ps on 15-8-15.
 */
public class LiveItemInfo {

    private String name;
    private String type;
    private String category;
    private String pic;
    private String albumId;
    private String videoId;
    private String url;
    private String showtime;
    private int isSubscribe;
    private int likeCount;
    private int viewCount;
    private String videoDetailUrl;

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setShowtime(String showtime) {
        this.showtime = showtime;
    }

    public void setIsSubscribe(int isSubscribe) {
        this.isSubscribe = isSubscribe;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public void setVideoDetailUrl(String videoDetailUrl) {
        this.videoDetailUrl = videoDetailUrl;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getCategory() {
        return category;
    }

    public String getPic() {
        return pic;
    }

    public String getAlbumId() {
        return albumId;
    }

    public String getVideoId() {
        return videoId;
    }

    public String getUrl() {
        return url;
    }

    public String getShowtime() {
        return showtime;
    }

    public int getIsSubscribe() {
        return isSubscribe;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public int getViewCount() {
        return viewCount;
    }

    public String getVideoDetailUrl() {
        return videoDetailUrl;
    }
}
