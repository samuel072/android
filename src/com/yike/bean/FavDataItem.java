package com.yike.bean;

/**
 * Created by hmj on 2015/8/16.
 */
public class FavDataItem {
    private int albumId;
    private String albumName;
    private String collectionTime;
    private String id;
    private String userId;
    private int videoId;
    private String videoName;
    private String videoPic;
    private int viewCount;

    public void setAlbumId(int albumId) {
        this.albumId = albumId;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public void setCollectionTime(String collectionTime) {
        this.collectionTime = collectionTime;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setVideoId(int videoId) {
        this.videoId = videoId;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public void setVideoPic(String videoPic) {
        this.videoPic = videoPic;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public int getAlbumId() {
        return albumId;
    }

    public String getAlbumName() {
        return albumName;
    }

    public String getCollectionTime() {
        return collectionTime;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public int getVideoId() {
        return videoId;
    }

    public String getVideoName() {
        return videoName;
    }

    public String getVideoPic() {
        return videoPic;
    }

    public int getViewCount() {
        return viewCount;
    }
}
