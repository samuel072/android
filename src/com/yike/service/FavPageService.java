package com.yike.service;

import com.yike.bean.FavDataItem;

import java.util.List;

/**
 * Created by hmj on 2015/8/16.
 */
public class FavPageService {

    private static FavPageService sInstance;

    private List<FavDataItem> favDataItems;
    private FavPageService() {
    }

    public static FavPageService getInstance() {
        if (sInstance == null) {
            sInstance = new FavPageService();
        }
        return sInstance;
    }

    public void setFavDataItems(List<FavDataItem> favDataItems) {
        this.favDataItems = favDataItems;
    }

    public List<FavDataItem> getFavDataItems() {
        return favDataItems;
    }
}
