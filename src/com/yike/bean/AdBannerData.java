package com.yike.bean;

/**
 * Created by hmj on 2015/8/14.
 */
public class AdBannerData {
    private int id;
    private String bigImage;
    private String smallImage;
    private int type;
    private int platform;
    private String url;
    private int sort;
    private String typeDesc;

    public int getId() {
        return id;
    }

    public String getBigImage() {
        return bigImage;
    }

    public String getSmallImage() {
        return smallImage;
    }

    public int getType() {
        return type;
    }

    public int getPlatform() {
        return platform;
    }

    public String getUrl() {
        return url;
    }

    public int getSort() {
        return sort;
    }

    public String getTypeDesc() {
        return typeDesc;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setBigImage(String bigImage) {
        this.bigImage = bigImage;
    }

    public void setSmallImage(String smallImage) {
        this.smallImage = smallImage;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setPlatform(int platform) {
        this.platform = platform;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }
}
