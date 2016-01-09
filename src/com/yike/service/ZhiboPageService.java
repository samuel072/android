package com.yike.service;

import com.yike.bean.Data;
import com.yike.bean.LiveData;

/**
 * @author rendy
 *         直播数据缓存
 */
public class ZhiboPageService {
    private static ZhiboPageService LOGIC;
    // 广告数据
    private Data advertisementsDatas;
    private Data yuyueDatas;
    private Data dujiaData;
    private Data moreDatas;

    /**
     * 正在直播
     */
    private LiveData zhengZaiZhibo;

    /**
     * 直播页面banner
     */
    private LiveData zhiBaoBanner;

    /**
     * 将要直播
     */
    private LiveData jiangYaoZhibo;

    /**
     * 预约的直播节目
     */
    private LiveData yuYueZhibo;

    private ZhiboPageService() {
    }

    public static ZhiboPageService getInstance() {
        return LOGIC = LOGIC == null ? new ZhiboPageService() : LOGIC;
    }

    public void setYuYueZhibo(LiveData yuYueZhibo) {
        this.yuYueZhibo = yuYueZhibo;
    }

    public LiveData getYuYueZhibo() {
        return yuYueZhibo;
    }

    public LiveData getJiangYaoZhibo() {
        return jiangYaoZhibo;
    }

    public void setJiangYaoZhibo(LiveData jiangYaoZhibo) {
        this.jiangYaoZhibo = jiangYaoZhibo;
    }

    public LiveData getZhengZaiZhibo() {
        return zhengZaiZhibo;
    }

    public LiveData getZhiBaoBanner() {
        return zhiBaoBanner;
    }

    public void setZhengZaiZhibo(LiveData zhengZaiZhibo) {
        this.zhengZaiZhibo = zhengZaiZhibo;
    }

    public void setZhiBaoBanner(LiveData zhiBaoBanner) {
        this.zhiBaoBanner = zhiBaoBanner;
    }

    public Data getAdvertisementsDatas() {
        return advertisementsDatas;
    }

    public void setAdvertisementsDatas(Data advertisementsDatas) {
        this.advertisementsDatas = advertisementsDatas;
    }

    public Data getYuyueDatas() {
        return yuyueDatas;
    }

    public void setYuyueDatas(Data yuyueDatas) {
        this.yuyueDatas = yuyueDatas;
    }

    public Data getDujiaData() {
        return dujiaData;
    }

    public void setDujiaData(Data dujiaData) {
        this.dujiaData = dujiaData;
    }

    public Data getMoreDatas() {
        return moreDatas;
    }

    public void setMoreDatas(Data moreDatas) {
        this.moreDatas = moreDatas;
    }

    public boolean isHaveDate() {
        return moreDatas != null || dujiaData != null || advertisementsDatas != null || yuyueDatas != null || zhengZaiZhibo != null || zhiBaoBanner != null || jiangYaoZhibo != null;
    }
}
