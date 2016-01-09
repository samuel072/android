package com.yike.bean;

import java.util.List;

import org.json.JSONArray;

/**
 * @author QIFA 探索 model
 */
public class ExploreInfoBean {
    private int num;
    private String name;
    private String listUrl;
    private List<ExploreCategoryInfo> info;

    public String getImgUrl() {
        return listUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.listUrl = imgUrl;
    }

    public List<ExploreCategoryInfo> getInfo() {
        return info;
    }

    public void setInfo(List<ExploreCategoryInfo> info) {
        this.info = info;
    }

    public int getId() {
        return num;
    }

    public void setId(int id) {
        this.num = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getListUrl() {
        return listUrl;
    }

    public void setListUrl(String listUrl) {
        this.listUrl = listUrl;
    }

}
