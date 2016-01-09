package com.yike.service;

import java.util.List;

import com.yike.bean.Data;
import com.yike.bean.ExploreInfoBean;
import com.yike.bean.ListenMeInfo;
import com.yike.bean.TalkerBean;

public class ExploreService {
    private static ExploreService LOGIC;
    // 广告数据
    private Data advertisementsDatas;// banner
    private TalkerBean taklData;// 点他来讲
    private List<ListenMeInfo> ListenMeData;// 听我说
    private ExploreInfoBean qiangdiaoData;// 腔调
    private ExploreInfoBean chuangyeData;// 创业路演汇
    private ExploreInfoBean laiyikeData;// 来一课
    private ExploreInfoBean liveSeeData;// 今日现场直击

    private ExploreService() {
    }

    public ExploreInfoBean getLiveSeeData() {
        return liveSeeData;
    }

    public void setLiveSeeData(ExploreInfoBean liveSeeData) {
        this.liveSeeData = liveSeeData;
    }

    public static ExploreService getInstance() {
        return LOGIC = LOGIC == null ? new ExploreService() : LOGIC;
    }

    public Data getAdvertisementsDatas() {
        return advertisementsDatas;
    }

    public void setAdvertisementsDatas(Data advertisementsDatas) {
        this.advertisementsDatas = advertisementsDatas;
    }

    public static ExploreService getLOGIC() {
        return LOGIC;
    }

    public static void setLOGIC(ExploreService lOGIC) {
        LOGIC = lOGIC;
    }

    public TalkerBean getTaklData() {
        return taklData;
    }

    public void setTaklData(TalkerBean taklData) {
        this.taklData = taklData;
    }

    public List<ListenMeInfo> getListenMeData() {
        return ListenMeData;
    }

    public void setListenMeData(List<ListenMeInfo> listenMeData) {
        ListenMeData = listenMeData;
    }

    public ExploreInfoBean getQiangdiaoData() {
        return qiangdiaoData;
    }

    public void setQiangdiaoData(ExploreInfoBean qiangdiaoData) {
        this.qiangdiaoData = qiangdiaoData;
    }

    public ExploreInfoBean getChuangyeData() {
        return chuangyeData;
    }

    public void setChuangyeData(ExploreInfoBean chuangyeData) {
        this.chuangyeData = chuangyeData;
    }

    public ExploreInfoBean getLaiyikeData() {
        return laiyikeData;
    }

    public void setLaiyikeData(ExploreInfoBean laiyikeData) {
        this.laiyikeData = laiyikeData;
    }

    public boolean isHaveDate() {
        return taklData != null || ListenMeData != null
                || advertisementsDatas != null || qiangdiaoData != null || chuangyeData != null
                || laiyikeData != null;
    }

}
