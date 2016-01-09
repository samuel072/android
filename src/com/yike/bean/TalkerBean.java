package com.yike.bean;

/**
 * @author QIFA 点他来讲 model
 */
public class TalkerBean {
    private int id;
    private String name;
    private int points;
    private String image;
    private String talkerListUrl;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTalkerListUrl() {
        return talkerListUrl;
    }

    public void setTalkerListUrl(String talkerListUrl) {
        this.talkerListUrl = talkerListUrl;
    }

    @Override
    public String toString() {
        return "TalkerBean [id=" + id + ", name=" + name + ", points=" + points + ", image=" + image
                + ", talkerListUrl=" + talkerListUrl + "]";
    }

}
