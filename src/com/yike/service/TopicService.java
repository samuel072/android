package com.yike.service;

import java.util.List;

import com.yike.bean.TopicBean;

/**
 * 
 * @author rendy 专题界面
 */
public class TopicService {

	private static TopicService LOGIC;
	private List<TopicBean> datas;

	public static TopicService getInstance() {
		return LOGIC = LOGIC == null ? new TopicService() : LOGIC;
	}

	public List<TopicBean> getDatas() {
		return datas;
	}

	public void setDatas(List<TopicBean> datas) {
		this.datas = datas;
	}

	public boolean isHaveDate() {
		return datas != null;
	}
}
