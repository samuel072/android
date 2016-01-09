package com.yike.service;

import com.yike.bean.HotKeyWords;
import com.yike.bean.SearchGuessLikeData;

import java.util.List;

/**
 * Created by hmj on 2015/8/16.
 */
public class SearchPageService {

    private static SearchPageService sInstance;

    private SearchGuessLikeData gudessLikeData;

    private List<HotKeyWords> hotWords;


    private SearchPageService() {
    }

    public static SearchPageService getInstance() {
        if (sInstance == null) {
            sInstance = new SearchPageService();
        }
        return sInstance;
    }

    public SearchGuessLikeData getGudessLikeData() {
        return gudessLikeData;
    }

    public void setGudessLikeData(SearchGuessLikeData gudessLikeData) {
        this.gudessLikeData = gudessLikeData;
    }

    public List<HotKeyWords> getHotWords() {
        return hotWords;
    }

    public void setHotWords(List<HotKeyWords> hotWords) {
        this.hotWords = hotWords;
    }

    public boolean isHaveDate() {
        return gudessLikeData != null && hotWords != null;
    }
}
