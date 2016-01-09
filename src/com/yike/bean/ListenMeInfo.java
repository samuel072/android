package com.yike.bean;

/**
 * Created by hmj on 2015/8/14.
 */
public class ListenMeInfo {
    private int id;
    private String title;
    private String modify_time;
    private String detail_url;
    private String more_url;
    private String thumbnail;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDetail_url() {
        return detail_url;
    }
    public void setDetail_url(String detail_url) {
        this.detail_url = detail_url;
    }
    public String getMore_url() {
        return more_url;
    }
    public void setMore_url(String more_url) {
        this.more_url = more_url;
    }
    public String getThumbnail() {
        return thumbnail;
    }
    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
    public String getModify_time() {
        return modify_time;
    }
    public void setModify_time(String modify_time) {
        this.modify_time = modify_time;
    }
    @Override
    public String toString() {
        return "ListenMeInfo [id=" + id + ", title=" + title + ", modify_time=" + modify_time
                + ", detail_url=" + detail_url + ", more_url=" + more_url + ", thumbnail=" + thumbnail + "]";
    }

   
}
