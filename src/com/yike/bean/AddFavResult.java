package com.yike.bean;

/**
 * Created by wangwei-ps on 15-8-17.
 */
public class AddFavResult {
    protected int albumId;
    protected String albumName;
    protected long collectionTime;
    protected String id;
    protected String userId;
    protected String videoId;
    protected String videoName;
    protected String videoPic;
    protected int viewCount;

    public void setAlbumId(int albumId) {
        this.albumId = albumId;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public void setCollectionTime(long collectionTime) {
        this.collectionTime = collectionTime;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setVideoId(String videoId) {
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

    public long getCollectionTime() {
        return collectionTime;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getVideoId() {
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
