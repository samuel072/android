package com.yike.service;

import java.util.List;

import com.yike.bean.AdBannerData;
import com.yike.bean.Data;
import com.yike.bean.HomePageItemInfo;

/**
 * 
 * @author rendy 首页逻辑数据处理
 */
public class FirstPageService {
	private static FirstPageService LOGIC;
	// 广告数据
	private Data advertisementsDatas;
	private Data yuyueDatas;
	private Data dujiaData;
	private Data moreDatas;

	private List<AdBannerData> adBannerdatas;

	private List<HomePageItemInfo> homePageData;

	private FirstPageService() {
	}

	public static FirstPageService getInstance() {
		return LOGIC = LOGIC == null ? new FirstPageService() : LOGIC;
	}

	public Data getAdvertisementsDatas() {
		return advertisementsDatas;
	}

	public void setAdvertisementsDatas(Data advertisementsDatas) {
		this.advertisementsDatas = advertisementsDatas;
	}

	public List<HomePageItemInfo> getHomePageData() {
		return homePageData;
	}

	public void setHomePageData(List<HomePageItemInfo> homePageData) {
		this.homePageData = homePageData;
	}

	public void setMoreDatas(Data moreDatas) {
		this.moreDatas = moreDatas;
	}

	public void setYuyueDatas(Data yuyueDatas) {
		this.yuyueDatas = yuyueDatas;
	}

	public void setDujiaData(Data dujiaData) {
		this.dujiaData = dujiaData;
	}

	public Data getYuyueDatas() {
		return yuyueDatas;
	}

	public Data getDujiaData() {
		return dujiaData;
	}

	public Data getMoreDatas() {
		return moreDatas;
	}

	public boolean isHaveDate() {
		return moreDatas != null || dujiaData != null
				|| advertisementsDatas != null || yuyueDatas != null;
	}

	public List<AdBannerData> getAdBannerdatas() {
		return adBannerdatas;
	}

	public void setAdBannerdatas(List<AdBannerData> adBannerdatas) {
		this.adBannerdatas = adBannerdatas;
	}
}
