package com.yike.bean;

import java.util.List;

import android.R.integer;

/**
 * Created by wangwei-ps on 15-8-15.
 */
public class HomePageData {
    private List<HomePageItemInfo> data;
    private String description;
    private int displayOrder;
    private String focusName;
    private int id;
    private String objectId;
    private int subscribe;
    private int type;
    private String typeDesc;
    private int total;

    public void setData(List<HomePageItemInfo> data) {
        this.data = data;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public void setFocusName(String focusName) {
        this.focusName = focusName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public void setSubscribe(int subscribe) {
        this.subscribe = subscribe;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<HomePageItemInfo> getData() {
        return data;
    }

    public String getDescription() {
        return description;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public String getFocusName() {
        return focusName;
    }

    public int getId() {
        return id;
    }

    public String getObjectId() {
        return objectId;
    }

    public int getSubscribe() {
        return subscribe;
    }

    public int getType() {
        return type;
    }

    public String getTypeDesc() {
        return typeDesc;
    }

    public int getTotal() {
        return total;
    }
}
